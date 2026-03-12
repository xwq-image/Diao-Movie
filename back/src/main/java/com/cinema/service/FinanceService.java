package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.BalanceLog;
import com.cinema.entity.Movie;
import com.cinema.entity.Order;
import com.cinema.entity.Schedule;
import com.cinema.mapper.BalanceLogMapper;
import com.cinema.mapper.MovieMapper;
import com.cinema.mapper.OrderMapper;
import com.cinema.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FinanceService {
    
    private final OrderMapper orderMapper;
    private final BalanceLogMapper balanceLogMapper;
    private final ScheduleMapper scheduleMapper;
    private final MovieMapper movieMapper;
    
    // 销售日报
    public Map<String, Object> getDailyReport(LocalDate date) {
        LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
        
        // 查询当日所有已支付订单
        LambdaQueryWrapper<Order> paidWrapper = new LambdaQueryWrapper<>();
        paidWrapper.in(Order::getStatus, "paid", "completed");
        paidWrapper.between(Order::getPayTime, startTime, endTime);
        List<Order> paidOrders = orderMapper.selectList(paidWrapper);
        
        // 查询当日退票订单
        LambdaQueryWrapper<Order> refundWrapper = new LambdaQueryWrapper<>();
        refundWrapper.eq(Order::getStatus, "refunded");
        refundWrapper.between(Order::getRefundTime, startTime, endTime);
        List<Order> refundOrders = orderMapper.selectList(refundWrapper);
        
        // 统计数据
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal onlineRevenue = BigDecimal.ZERO;
        BigDecimal offlineRevenue = BigDecimal.ZERO;
        int orderCount = paidOrders.size();
        int onlineOrderCount = 0;
        int offlineOrderCount = 0;
        int ticketCount = 0;
        
        for (Order order : paidOrders) {
            totalRevenue = totalRevenue.add(order.getTotalPrice());
            ticketCount += order.getSeatCount();
            
            if ("online".equals(order.getSource())) {
                onlineRevenue = onlineRevenue.add(order.getTotalPrice());
                onlineOrderCount++;
            } else {
                offlineRevenue = offlineRevenue.add(order.getTotalPrice());
                offlineOrderCount++;
            }
        }
        
        // 退票统计
        BigDecimal refundAmount = BigDecimal.ZERO;
        int refundCount = refundOrders.size();
        for (Order order : refundOrders) {
            refundAmount = refundAmount.add(order.getTotalPrice());
        }
        
        // 净收入
        BigDecimal netRevenue = totalRevenue.subtract(refundAmount);
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("onlineRevenue", onlineRevenue);
        result.put("offlineRevenue", offlineRevenue);
        result.put("orderCount", orderCount);
        result.put("onlineOrderCount", onlineOrderCount);
        result.put("offlineOrderCount", offlineOrderCount);
        result.put("ticketCount", ticketCount);
        result.put("refundAmount", refundAmount);
        result.put("refundCount", refundCount);
        result.put("netRevenue", netRevenue);
        
        return result;
    }
    
    // 销售月报
    public Map<String, Object> getMonthlyReport(YearMonth yearMonth) {
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startTime = LocalDateTime.of(firstDay, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(lastDay, LocalTime.MAX);

        LambdaQueryWrapper<Order> paidWrapper = new LambdaQueryWrapper<>();
        paidWrapper.in(Order::getStatus, "paid", "completed");
        paidWrapper.between(Order::getPayTime, startTime, endTime);
        List<Order> paidOrders = orderMapper.selectList(paidWrapper);

        LambdaQueryWrapper<Order> refundWrapper = new LambdaQueryWrapper<>();
        refundWrapper.eq(Order::getStatus, "refunded");
        refundWrapper.between(Order::getRefundTime, startTime, endTime);
        List<Order> refundOrders = orderMapper.selectList(refundWrapper);

        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalOrders = paidOrders.size();
        int ticketCount = 0;
        for (Order order : paidOrders) {
            totalRevenue = totalRevenue.add(order.getTotalPrice());
            ticketCount += order.getSeatCount();
        }

        BigDecimal refundAmount = BigDecimal.ZERO;
        int refundCount = refundOrders.size();
        for (Order order : refundOrders) {
            refundAmount = refundAmount.add(order.getTotalPrice());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("totalOrders", totalOrders);
        result.put("ticketCount", ticketCount);
        result.put("refundAmount", refundAmount);
        result.put("refundCount", refundCount);
        result.put("netRevenue", totalRevenue.subtract(refundAmount));
        return result;
    }

    // 影片票房统计
    public List<Map<String, Object>> getMovieRevenue() {
        List<Movie> allMovies = movieMapper.selectList(null);

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.in(Order::getStatus, "paid", "completed");
        List<Order> paidOrders = orderMapper.selectList(orderWrapper);

        // scheduleId → movieId 映射
        Set<Long> scheduleIds = new HashSet<>();
        for (Order o : paidOrders) {
            scheduleIds.add(o.getScheduleId());
        }

        Map<Long, Long> scheduleMovieMap = new HashMap<>();
        if (!scheduleIds.isEmpty()) {
            List<Schedule> schedules = scheduleMapper.selectBatchIds(scheduleIds);
            for (Schedule s : schedules) {
                scheduleMovieMap.put(s.getId(), s.getMovieId());
            }
        }

        // 按电影汇总票房
        Map<Long, BigDecimal> revenueMap = new HashMap<>();
        Map<Long, Integer> ticketMap = new HashMap<>();
        for (Order o : paidOrders) {
            Long movieId = scheduleMovieMap.get(o.getScheduleId());
            if (movieId == null) continue;
            revenueMap.merge(movieId, o.getTotalPrice(), BigDecimal::add);
            ticketMap.merge(movieId, o.getSeatCount(), Integer::sum);
        }

        // 统计每部电影的排期数
        LambdaQueryWrapper<Schedule> scheduleWrapper = new LambdaQueryWrapper<>();
        List<Schedule> allSchedules = scheduleMapper.selectList(scheduleWrapper);
        Map<Long, Integer> scheduleCountMap = new HashMap<>();
        for (Schedule s : allSchedules) {
            scheduleCountMap.merge(s.getMovieId(), 1, Integer::sum);
        }

        // 组装结果，按票房降序
        List<Map<String, Object>> result = new ArrayList<>();
        for (Movie movie : allMovies) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", movie.getId());
            item.put("title", movie.getTitle());
            item.put("revenue", revenueMap.getOrDefault(movie.getId(), BigDecimal.ZERO));
            item.put("ticketCount", ticketMap.getOrDefault(movie.getId(), 0));
            item.put("scheduleCount", scheduleCountMap.getOrDefault(movie.getId(), 0));
            result.add(item);
        }

        result.sort((a, b) -> ((BigDecimal) b.get("revenue")).compareTo((BigDecimal) a.get("revenue")));
        return result;
    }

    // 获取资金流水
    public List<BalanceLog> getBalanceLogs(LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<BalanceLog> wrapper = new LambdaQueryWrapper<>();
        
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);
            wrapper.between(BalanceLog::getCreateTime, start, end);
        }
        
        wrapper.orderByDesc(BalanceLog::getCreateTime);
        return balanceLogMapper.selectList(wrapper);
    }
}
