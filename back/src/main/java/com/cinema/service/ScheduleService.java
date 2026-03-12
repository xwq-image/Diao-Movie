package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.*;
import com.cinema.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    
    private final ScheduleMapper scheduleMapper;
    private final MovieMapper movieMapper;
    private final HallMapper hallMapper;
    private final HallSeatMapper hallSeatMapper;
    private final OrderMapper orderMapper;
    private final OrderSeatMapper orderSeatMapper;
    
    // 获取排期列表（管理后台用）
    public List<Map<String, Object>> getScheduleList(LocalDate date) {
        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
        if (date != null) {
            wrapper.eq(Schedule::getShowDate, date);
        }
        wrapper.orderByAsc(Schedule::getShowDate, Schedule::getStartTime);
        
        List<Schedule> schedules = scheduleMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Schedule schedule : schedules) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", schedule.getId());
            item.put("movieId", schedule.getMovieId());
            item.put("hallId", schedule.getHallId());
            item.put("showDate", schedule.getShowDate());
            item.put("startTime", schedule.getStartTime());
            item.put("endTime", schedule.getEndTime());
            item.put("price", schedule.getPrice());
            
            Movie movie = movieMapper.selectById(schedule.getMovieId());
            if (movie != null) {
                item.put("movieTitle", movie.getTitle());
            }
            
            Hall hall = hallMapper.selectById(schedule.getHallId());
            if (hall != null) {
                item.put("hallName", hall.getName());
            }
            
            result.add(item);
        }
        
        return result;
    }
    
    // 创建排期
    public void createSchedule(Schedule schedule) {
        scheduleMapper.insert(schedule);
    }
    
    // 更新排期
    public void updateSchedule(Schedule schedule) {
        scheduleMapper.updateById(schedule);
    }
    
    // 删除排期
    public void deleteSchedule(Long id) {
        scheduleMapper.deleteById(id);
    }
    
    // 获取影片的排期列表
    public List<Map<String, Object>> getMovieSchedules(Long movieId, LocalDate date) {
        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Schedule::getMovieId, movieId);
        if (date != null) {
            wrapper.eq(Schedule::getShowDate, date);
        }
        wrapper.orderByAsc(Schedule::getShowDate, Schedule::getStartTime);
        
        List<Schedule> schedules = scheduleMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Schedule schedule : schedules) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", schedule.getId());
            item.put("showDate", schedule.getShowDate());
            item.put("startTime", schedule.getStartTime());
            item.put("endTime", schedule.getEndTime());
            item.put("price", schedule.getPrice());
            
            // 获取影厅信息
            Hall hall = hallMapper.selectById(schedule.getHallId());
            item.put("hallName", hall.getName());
            item.put("totalSeats", hall.getTotalSeats());
            
            // 计算剩余座位
            int soldSeats = getSoldSeatsCount(schedule.getId());
            item.put("availableSeats", hall.getTotalSeats() - soldSeats);
            
            result.add(item);
        }
        
        return result;
    }
    
    // 获取排期的座位信息
    public Map<String, Object> getScheduleSeats(Long scheduleId) {
        Schedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null) {
            throw new RuntimeException("排期不存在");
        }
        
        // 获取影厅座位布局
        LambdaQueryWrapper<HallSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HallSeat::getHallId, schedule.getHallId());
        wrapper.orderByAsc(HallSeat::getRowNum, HallSeat::getColNum);
        List<HallSeat> seats = hallSeatMapper.selectList(wrapper);
        
        // 获取已售座位
        Set<Long> soldSeatIds = getSoldSeatIds(scheduleId);
        
        // 构建座位数据
        List<Map<String, Object>> seatList = new ArrayList<>();
        for (HallSeat seat : seats) {
            Map<String, Object> seatInfo = new HashMap<>();
            seatInfo.put("id", seat.getId());
            seatInfo.put("row", seat.getRowNum());
            seatInfo.put("col", seat.getColNum());
            seatInfo.put("type", seat.getSeatType());
            seatInfo.put("status", soldSeatIds.contains(seat.getId()) ? "sold" : "available");
            seatList.add(seatInfo);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("scheduleId", scheduleId);
        result.put("price", schedule.getPrice());
        result.put("seats", seatList);
        
        return result;
    }
    
    // 获取已售座位数量
    private int getSoldSeatsCount(Long scheduleId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getScheduleId, scheduleId);
        wrapper.in(Order::getStatus, Arrays.asList("paid", "completed"));
        
        List<Order> orders = orderMapper.selectList(wrapper);
        int count = 0;
        for (Order order : orders) {
            count += order.getSeatCount();
        }
        return count;
    }
    
    // 获取已售座位ID集合
    private Set<Long> getSoldSeatIds(Long scheduleId) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getScheduleId, scheduleId);
        orderWrapper.in(Order::getStatus, Arrays.asList("paid", "completed"));
        
        List<Order> orders = orderMapper.selectList(orderWrapper);
        Set<Long> seatIds = new HashSet<>();
        
        for (Order order : orders) {
            LambdaQueryWrapper<OrderSeat> seatWrapper = new LambdaQueryWrapper<>();
            seatWrapper.eq(OrderSeat::getOrderId, order.getId());
            List<OrderSeat> orderSeats = orderSeatMapper.selectList(seatWrapper);
            
            for (OrderSeat orderSeat : orderSeats) {
                seatIds.add(orderSeat.getSeatId());
            }
        }
        
        return seatIds;
    }
}
