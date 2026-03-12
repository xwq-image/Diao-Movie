package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.*;
import com.cinema.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderMapper orderMapper;
    private final OrderSeatMapper orderSeatMapper;
    private final ScheduleMapper scheduleMapper;
    private final HallSeatMapper hallSeatMapper;
    private final UserMapper userMapper;
    private final BalanceLogMapper balanceLogMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    
    // 创建订单
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createOrder(Long userId, Long scheduleId, List<Long> seatIds) {
        // 获取排期信息
        Schedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null) {
            throw new RuntimeException("排期不存在");
        }
        
        // 使用Redis分布式锁防止超卖
        String lockKey = "seat_lock:" + scheduleId;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(lock)) {
            throw new RuntimeException("系统繁忙，请稍后重试");
        }
        
        try {
            // 检查座位是否可用
            for (Long seatId : seatIds) {
                if (isSeatSold(scheduleId, seatId)) {
                    throw new RuntimeException("座位已被占用");
                }
            }
            
            // 创建订单
            Order order = new Order();
            order.setOrderNo("ORDER" + System.currentTimeMillis() + userId);
            order.setUserId(userId);
            order.setScheduleId(scheduleId);
            order.setSeatCount(seatIds.size());
            order.setTotalPrice(schedule.getPrice().multiply(new BigDecimal(seatIds.size())));
            order.setStatus("unpaid");
            order.setSource("online");
            
            orderMapper.insert(order);
            
            // 保存订单座位关联
            for (Long seatId : seatIds) {
                HallSeat seat = hallSeatMapper.selectById(seatId);
                OrderSeat orderSeat = new OrderSeat();
                orderSeat.setOrderId(order.getId());
                orderSeat.setSeatId(seatId);
                orderSeat.setRowNum(seat.getRowNum());
                orderSeat.setColNum(seat.getColNum());
                orderSeatMapper.insert(orderSeat);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", order.getId());
            result.put("orderNo", order.getOrderNo());
            result.put("totalPrice", order.getTotalPrice());
            
            return result;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }
    
    // 支付订单
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, Long userId, String payPassword) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        if (!"unpaid".equals(order.getStatus())) {
            throw new RuntimeException("订单状态异常");
        }
        
        // 验证支付密码
        User user = userMapper.selectById(userId);
        if (user.getPayPassword() == null) {
            throw new RuntimeException("请先设置支付密码");
        }
        
        if (!passwordEncoder.matches(payPassword, user.getPayPassword())) {
            throw new RuntimeException("支付密码错误");
        }
        
        // 检查余额
        if (user.getBalance().compareTo(order.getTotalPrice()) < 0) {
            throw new RuntimeException("余额不足");
        }
        
        // 扣除余额
        BigDecimal balanceBefore = user.getBalance();
        user.setBalance(user.getBalance().subtract(order.getTotalPrice()));
        userMapper.updateById(user);
        
        // 记录余额流水
        BalanceLog log = new BalanceLog();
        log.setUserId(userId);
        log.setType("consume");
        log.setAmount(order.getTotalPrice());
        log.setBalanceBefore(balanceBefore);
        log.setBalanceAfter(user.getBalance());
        log.setOrderId(orderId);
        log.setRemark("购票消费");
        balanceLogMapper.insert(log);
        
        // 更新订单状态
        order.setStatus("paid");
        order.setPayType("balance");
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }
    
    // 退票
    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        if (!"paid".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不支持退票");
        }
        
        // 检查是否在开场前2小时
        Schedule schedule = scheduleMapper.selectById(order.getScheduleId());
        LocalDateTime showTime = LocalDateTime.of(schedule.getShowDate(), schedule.getStartTime());
        LocalDateTime now = LocalDateTime.now();
        
        if (now.plusHours(2).isAfter(showTime)) {
            throw new RuntimeException("开场前2小时内不支持退票");
        }
        
        // 退款到余额
        User user = userMapper.selectById(userId);
        BigDecimal balanceBefore = user.getBalance();
        user.setBalance(user.getBalance().add(order.getTotalPrice()));
        userMapper.updateById(user);
        
        // 记录余额流水
        BalanceLog log = new BalanceLog();
        log.setUserId(userId);
        log.setType("refund");
        log.setAmount(order.getTotalPrice());
        log.setBalanceBefore(balanceBefore);
        log.setBalanceAfter(user.getBalance());
        log.setOrderId(orderId);
        log.setRemark("退票退款");
        balanceLogMapper.insert(log);
        
        // 更新订单状态
        order.setStatus("refunded");
        order.setRefundTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }
    
    // 获取用户订单列表
    public List<Map<String, Object>> getUserOrders(Long userId, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        
        List<Order> orders = orderMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Order order : orders) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", order.getId());
            item.put("orderNo", order.getOrderNo());
            item.put("totalPrice", order.getTotalPrice());
            item.put("seatCount", order.getSeatCount());
            item.put("status", order.getStatus());
            item.put("createTime", order.getCreateTime());
            item.put("payTime", order.getPayTime());
            
            // 获取排期和影片信息
            Schedule schedule = scheduleMapper.selectById(order.getScheduleId());
            if (schedule != null) {
                item.put("showDate", schedule.getShowDate());
                item.put("startTime", schedule.getStartTime());
            }
            
            result.add(item);
        }
        
        return result;
    }
    
    // 检查座位是否已售
    private boolean isSeatSold(Long scheduleId, Long seatId) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getScheduleId, scheduleId);
        orderWrapper.in(Order::getStatus, Arrays.asList("paid", "completed"));
        
        List<Order> orders = orderMapper.selectList(orderWrapper);
        
        for (Order order : orders) {
            LambdaQueryWrapper<OrderSeat> seatWrapper = new LambdaQueryWrapper<>();
            seatWrapper.eq(OrderSeat::getOrderId, order.getId());
            seatWrapper.eq(OrderSeat::getSeatId, seatId);
            
            if (orderSeatMapper.selectCount(seatWrapper) > 0) {
                return true;
            }
        }
        
        return false;
    }
}
