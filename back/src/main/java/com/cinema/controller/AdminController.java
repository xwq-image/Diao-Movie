package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.entity.User;
import com.cinema.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public Result<List<User>> getUserList() {
        List<User> users = adminService.getUserList();
        return Result.success(users);
    }

    @GetMapping("/staff")
    public Result<List<User>> getStaffList() {
        List<User> staff = adminService.getStaffList();
        return Result.success(staff);
    }

    @PostMapping("/create-staff")
    public Result<Void> createStaff(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String nickname = params.get("nickname");
        String role = params.get("role");
        String password = params.get("password");

        adminService.createStaff(phone, nickname, role, password);
        return Result.success();
    }

    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String password = params.get("password").toString();

        adminService.resetPassword(userId, password);
        return Result.success();
    }

    @PostMapping("/toggle-user-status")
    public Result<Void> toggleUserStatus(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Integer status = Integer.valueOf(params.get("status").toString());

        adminService.toggleUserStatus(userId, status);
        return Result.success();
    }
}
