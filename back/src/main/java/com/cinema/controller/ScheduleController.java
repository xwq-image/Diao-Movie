package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.entity.Schedule;
import com.cinema.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getScheduleList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Map<String, Object>> schedules = scheduleService.getScheduleList(date);
        return Result.success(schedules);
    }
    
    @GetMapping("/movie/{movieId}")
    public Result<List<Map<String, Object>>> getMovieSchedules(
            @PathVariable Long movieId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Map<String, Object>> schedules = scheduleService.getMovieSchedules(movieId, date);
        return Result.success(schedules);
    }
    
    @GetMapping("/{scheduleId}/seats")
    public Result<Map<String, Object>> getScheduleSeats(@PathVariable Long scheduleId) {
        Map<String, Object> seats = scheduleService.getScheduleSeats(scheduleId);
        return Result.success(seats);
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> createSchedule(@RequestBody Schedule schedule) {
        scheduleService.createSchedule(schedule);
        return Result.success();
    }
    
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateSchedule(@RequestBody Schedule schedule) {
        scheduleService.updateSchedule(schedule);
        return Result.success();
    }
    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return Result.success();
    }
}
