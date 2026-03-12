package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("hall_seat")
public class HallSeat {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long hallId;
    private Integer rowNum;
    private Integer colNum;
    private String seatType; // normal, vip, aisle
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
