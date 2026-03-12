package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    private Long userId;
    private Long scheduleId;
    private BigDecimal totalPrice;
    private Integer seatCount;
    private String status; // unpaid, paid, completed, refunded, cancelled
    private String source; // online, offline
    private String payType; // balance, cash
    private LocalDateTime payTime;
    private LocalDateTime refundTime;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
