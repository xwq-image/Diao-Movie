package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.common.SecurityUtil;
import com.cinema.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/sell-ticket")
    public Result<Map<String, Object>> sellTicket(@RequestBody Map<String, Object> params) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        Long scheduleId = Long.valueOf(params.get("scheduleId").toString());

        List<Long> seatIds = new ArrayList<>();
        List<?> rawIds = (List<?>) params.get("seatIds");
        for (Object id : rawIds) {
            seatIds.add(Long.valueOf(id.toString()));
        }

        Map<String, Object> result = sellerService.sellTicket(sellerId, scheduleId, seatIds);
        return Result.success(result);
    }
}
