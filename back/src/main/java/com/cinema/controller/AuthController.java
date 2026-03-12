package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.common.SecurityUtil;
import com.cinema.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String nickname = params.get("nickname");
        String password = params.get("password");

        Map<String, Object> result = authService.register(phone, nickname, password);
        return Result.success(result);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");

        Map<String, Object> result = authService.login(phone, password);
        return Result.success(result);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        Map<String, Object> userInfo = authService.getUserInfo(userId);
        return Result.success(userInfo);
    }
}
