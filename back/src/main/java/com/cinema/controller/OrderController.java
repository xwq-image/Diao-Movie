package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.common.SecurityUtil;
import com.cinema.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<Map<String, Object>> createOrder(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long scheduleId = Long.valueOf(params.get("scheduleId").toString());

        List<Long> seatIds = new ArrayList<>();
        List<?> rawIds = (List<?>) params.get("seatIds");
        for (Object id : rawIds) {
            seatIds.add(Long.valueOf(id.toString()));
        }

        Map<String, Object> result = orderService.createOrder(userId, scheduleId, seatIds);
        return Result.success(result);
    }

    @PostMapping("/pay")
    public Result<Void> payOrder(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long orderId = Long.valueOf(params.get("orderId").toString());
        String payPassword = params.get("payPassword").toString();

        orderService.payOrder(orderId, userId, payPassword);
        return Result.success();
    }

    @PostMapping("/refund")
    public Result<Void> refundOrder(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long orderId = Long.valueOf(params.get("orderId").toString());

        orderService.refundOrder(orderId, userId);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getUserOrders(
            @RequestParam(required = false) String status) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Map<String, Object>> orders = orderService.getUserOrders(userId, status);
        return Result.success(orders);
    }
}
