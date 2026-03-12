package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.entity.Hall;
import com.cinema.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hall")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    @GetMapping("/list")
    public Result<List<Hall>> getHallList() {
        List<Hall> halls = hallService.getHallList();
        return Result.success(halls);
    }

    @GetMapping("/detail/{id}")
    public Result<Hall> getHallDetail(@PathVariable Long id) {
        Hall hall = hallService.getHallDetail(id);
        return Result.success(hall);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> createHall(@RequestBody Hall hall) {
        hallService.createHall(hall);
        return Result.success();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateHall(@RequestBody Hall hall) {
        hallService.updateHall(hall);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return Result.success();
    }
}
