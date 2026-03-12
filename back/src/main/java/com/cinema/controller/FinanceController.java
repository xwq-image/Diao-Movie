package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.entity.BalanceLog;
import com.cinema.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/daily-report")
    public Result<Map<String, Object>> getDailyReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Map<String, Object> report = financeService.getDailyReport(date);
        return Result.success(report);
    }

    @GetMapping("/monthly-report")
    public Result<Map<String, Object>> getMonthlyReport(
            @RequestParam String month) {
        YearMonth ym = YearMonth.parse(month);
        Map<String, Object> report = financeService.getMonthlyReport(ym);
        return Result.success(report);
    }

    @GetMapping("/movie-revenue")
    public Result<List<Map<String, Object>>> getMovieRevenue() {
        List<Map<String, Object>> data = financeService.getMovieRevenue();
        return Result.success(data);
    }

    @GetMapping("/balance-logs")
    public Result<List<BalanceLog>> getBalanceLogs(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<BalanceLog> logs = financeService.getBalanceLogs(startDate, endDate);
        return Result.success(logs);
    }
}
