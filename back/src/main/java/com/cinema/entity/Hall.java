package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("hall")
public class Hall {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;

    @TableField("`rows`")
    private Integer rows;

    @TableField("`cols`")
    private Integer cols;

    private Integer totalSeats;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
