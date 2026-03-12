package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("order_seat")
public class OrderSeat {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId;
    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
