package com.cinema.service;

import com.cinema.entity.*;
import com.cinema.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final OrderMapper orderMapper;
    private final OrderSeatMapper orderSeatMapper;
    private final ScheduleMapper scheduleMapper;
    private final HallSeatMapper hallSeatMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sellTicket(Long sellerId, Long scheduleId, List<Long> seatIds) {
        Schedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null) {
            throw new RuntimeException("排期不存在");
        }

        String lockKey = "seat_lock:" + scheduleId;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(lock)) {
            throw new RuntimeException("系统繁忙，请稍后重试");
        }

        try {
            for (Long seatId : seatIds) {
                if (isSeatOccupied(scheduleId, seatId)) {
                    throw new RuntimeException("座位已被占用");
                }
            }

            Order order = new Order();
            order.setOrderNo("SELL" + System.currentTimeMillis() + sellerId);
            order.setUserId(sellerId);
            order.setScheduleId(scheduleId);
            order.setSeatCount(seatIds.size());
            order.setTotalPrice(schedule.getPrice().multiply(new BigDecimal(seatIds.size())));
            order.setStatus("paid");
            order.setSource("offline");
            order.setPayType("cash");
            order.setPayTime(LocalDateTime.now());

            orderMapper.insert(order);

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

    private boolean isSeatOccupied(Long scheduleId, Long seatId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order> orderWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getScheduleId, scheduleId);
        orderWrapper.in(Order::getStatus, Arrays.asList("unpaid", "paid", "completed"));

        List<Order> orders = orderMapper.selectList(orderWrapper);
        for (Order order : orders) {
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderSeat> seatWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            seatWrapper.eq(OrderSeat::getOrderId, order.getId());
            seatWrapper.eq(OrderSeat::getSeatId, seatId);
            if (orderSeatMapper.selectCount(seatWrapper) > 0) {
                return true;
            }
        }
        return false;
    }
}
