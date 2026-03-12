package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.common.SecurityUtil;
import com.cinema.entity.BalanceLog;
import com.cinema.entity.User;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/set-pay-password")
    public Result<Void> setPayPassword(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        String oldPassword = params.get("oldPassword") != null ? params.get("oldPassword").toString() : null;
        Object newPwd = params.get("newPassword");
        if (newPwd == null || newPwd.toString().isEmpty()) {
            throw new RuntimeException("新密码不能为空");
        }
        String newPassword = newPwd.toString();

        userService.setPayPassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @PostMapping("/recharge")
    public Result<Void> recharge(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        BigDecimal amount = new BigDecimal(params.get("amount").toString());

        userService.recharge(userId, amount);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @GetMapping("/balance-logs")
    public Result<List<BalanceLog>> getBalanceLogs() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<BalanceLog> logs = userService.getBalanceLogs(userId);
        return Result.success(logs);
    }

    @PostMapping("/update-nickname")
    public Result<Void> updateNickname(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        Object nick = params.get("nickname");
        if (nick == null || nick.toString().trim().isEmpty()) {
            throw new RuntimeException("昵称不能为空");
        }
        userService.updateNickname(userId, nick.toString().trim());
        return Result.success();
    }

    @PostMapping("/upload-avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = SecurityUtil.getCurrentUserId();
        if (file.isEmpty()) throw new RuntimeException("文件不能为空");

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;

        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "avatar";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(dir, fileName);
        file.transferTo(dest);

        String avatarUrl = "/uploads/avatar/" + fileName;
        userService.updateAvatar(userId, avatarUrl);

        Map<String, String> result = new java.util.HashMap<>();
        result.put("url", avatarUrl);
        return Result.success(result);
    }
}
