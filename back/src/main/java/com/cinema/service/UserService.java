package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.BalanceLog;
import com.cinema.entity.User;
import com.cinema.mapper.BalanceLogMapper;
import com.cinema.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final BalanceLogMapper balanceLogMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void setPayPassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getPayPassword() != null && !user.getPayPassword().isEmpty()) {
            if (oldPassword == null || oldPassword.isEmpty()) {
                throw new RuntimeException("请输入原支付密码");
            }
            if (!passwordEncoder.matches(oldPassword, user.getPayPassword())) {
                throw new RuntimeException("原支付密码错误");
            }
        }

        user.setPayPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
    
    // 充值
    @Transactional(rollbackFor = Exception.class)
    public void recharge(Long userId, BigDecimal amount) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        BigDecimal balanceBefore = user.getBalance();
        user.setBalance(user.getBalance().add(amount));
        userMapper.updateById(user);
        
        // 记录余额流水
        BalanceLog log = new BalanceLog();
        log.setUserId(userId);
        log.setType("recharge");
        log.setAmount(amount);
        log.setBalanceBefore(balanceBefore);
        log.setBalanceAfter(user.getBalance());
        log.setRemark("账户充值");
        balanceLogMapper.insert(log);
    }
    
    // 获取余额流水
    public List<BalanceLog> getBalanceLogs(Long userId) {
        LambdaQueryWrapper<BalanceLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BalanceLog::getUserId, userId);
        wrapper.orderByDesc(BalanceLog::getCreateTime);
        return balanceLogMapper.selectList(wrapper);
    }
    
    public void updateNickname(Long userId, String nickname) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        if (!"user".equals(user.getRole())) throw new RuntimeException("仅普通用户可修改昵称");
        user.setNickname(nickname);
        userMapper.updateById(user);
    }

    public void updateAvatar(Long userId, String avatarUrl) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);
    }

    // 获取用户信息
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 清空密码字段
        user.setPassword(null);
        user.setPayPassword(null);
        return user;
    }
}
