package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.User;
import com.cinema.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    // 获取普通用户列表
    public List<User> getUserList() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRole, "user");
        wrapper.orderByDesc(User::getCreateTime);
        
        List<User> users = userMapper.selectList(wrapper);
        // 清空密码字段
        users.forEach(user -> {
            user.setPassword(null);
            user.setPayPassword(null);
        });
        return users;
    }
    
    // 获取员工列表
    public List<User> getStaffList() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getRole, "seller", "finance", "admin");
        wrapper.orderByDesc(User::getCreateTime);
        
        List<User> staff = userMapper.selectList(wrapper);
        // 清空密码字段
        staff.forEach(user -> {
            user.setPassword(null);
            user.setPayPassword(null);
        });
        return staff;
    }
    
    // 创建员工账号
    public void createStaff(String phone, String nickname, String role, String password) {
        // 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 验证角色
        if (!"seller".equals(role) && !"finance".equals(role) && !"admin".equals(role)) {
            throw new RuntimeException("角色不合法");
        }
        
        User user = new User();
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setRole(role);
        String encoded = passwordEncoder.encode(password);
        user.setPassword(encoded);
        user.setPayPassword(encoded);
        user.setBalance(BigDecimal.ZERO);
        user.setStatus(1);
        
        userMapper.insert(user);
    }
    
    // 重置密码
    public void resetPassword(Long userId, String password) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setPassword(passwordEncoder.encode(password));
        userMapper.updateById(user);
    }
    
    // 切换用户状态
    public void toggleUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setStatus(status);
        userMapper.updateById(user);
    }
}
