-- ============================================
-- 影院票务管理系统 - 数据库初始化脚本
-- ============================================

DROP DATABASE IF EXISTS cinema_ticket;
CREATE DATABASE cinema_ticket DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cinema_ticket;

-- ----------------------------
-- 用户表（C端用户 + B端员工）
-- ----------------------------
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `phone` VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码（BCrypt加密）',
  `pay_password` VARCHAR(255) COMMENT '支付密码（BCrypt加密）',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(500) COMMENT '头像URL',
  `role` ENUM('user','seller','finance','admin') NOT NULL DEFAULT 'user' COMMENT '角色',
  `balance` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0=禁用,1=正常',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除,1=已删除',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_phone (`phone`),
  INDEX idx_role (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 影片表
-- ----------------------------
CREATE TABLE `movie` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '影片ID',
  `title` VARCHAR(100) NOT NULL COMMENT '片名',
  `poster` VARCHAR(500) COMMENT '海报URL',
  `director` VARCHAR(100) COMMENT '导演',
  `actors` VARCHAR(500) COMMENT '主演',
  `genre` VARCHAR(100) COMMENT '类型',
  `duration` INT NOT NULL COMMENT '时长（分钟）',
  `release_date` DATE COMMENT '上映日期',
  `description` TEXT COMMENT '剧情简介',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0=下架,1=热映,2=即将上映',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_status (`status`),
  INDEX idx_release_date (`release_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影片表';

-- ----------------------------
-- 影厅表
-- 注意：`rows` 是 MySQL 保留字，实体类中需要用 @TableField("`rows`") 注解
-- ----------------------------
CREATE TABLE `hall` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '影厅ID',
  `name` VARCHAR(50) NOT NULL COMMENT '影厅名称',
  `rows` INT NOT NULL COMMENT '座位排数',
  `cols` INT NOT NULL COMMENT '座位列数',
  `total_seats` INT NOT NULL COMMENT '总座位数',
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影厅表';

-- ----------------------------
-- 座位布局表
-- ----------------------------
CREATE TABLE `hall_seat` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '座位ID',
  `hall_id` BIGINT NOT NULL COMMENT '影厅ID',
  `row_num` INT NOT NULL COMMENT '排号',
  `col_num` INT NOT NULL COMMENT '列号',
  `seat_type` ENUM('normal','vip','aisle') NOT NULL DEFAULT 'normal' COMMENT '座位类型',
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_hall_row_col (`hall_id`, `row_num`, `col_num`),
  INDEX idx_hall_id (`hall_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位布局表';

-- ----------------------------
-- 排期表
-- ----------------------------
CREATE TABLE `schedule` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '排期ID',
  `movie_id` BIGINT NOT NULL COMMENT '影片ID',
  `hall_id` BIGINT NOT NULL COMMENT '影厅ID',
  `show_date` DATE NOT NULL COMMENT '放映日期',
  `start_time` TIME NOT NULL COMMENT '开始时间',
  `end_time` TIME NOT NULL COMMENT '结束时间',
  `price` DECIMAL(10,2) NOT NULL COMMENT '票价',
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_movie_id (`movie_id`),
  INDEX idx_hall_id (`hall_id`),
  INDEX idx_show_date (`show_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排期表';

-- ----------------------------
-- 订单表（order 是 MySQL 保留字，须用反引号）
-- ----------------------------
CREATE TABLE `order` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `schedule_id` BIGINT NOT NULL COMMENT '排期ID',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '总价',
  `seat_count` INT NOT NULL COMMENT '座位数量',
  `status` ENUM('unpaid','paid','completed','refunded','cancelled') NOT NULL DEFAULT 'unpaid' COMMENT '订单状态',
  `source` ENUM('online','offline') NOT NULL DEFAULT 'online' COMMENT '来源：online=C端,offline=售票员',
  `pay_type` ENUM('balance','cash') COMMENT '支付方式',
  `pay_time` DATETIME COMMENT '支付时间',
  `refund_time` DATETIME COMMENT '退款时间',
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_order_no (`order_no`),
  INDEX idx_user_id (`user_id`),
  INDEX idx_schedule_id (`schedule_id`),
  INDEX idx_status (`status`),
  INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- 订单座位关联表
-- ----------------------------
CREATE TABLE `order_seat` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `seat_id` BIGINT NOT NULL COMMENT '座位ID',
  `row_num` INT NOT NULL COMMENT '排号',
  `col_num` INT NOT NULL COMMENT '列号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`),
  INDEX idx_seat_id (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单座位关联表';

-- ----------------------------
-- 余额流水表
-- ----------------------------
CREATE TABLE `balance_log` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流水ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `type` ENUM('recharge','consume','refund') NOT NULL COMMENT '类型',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `balance_before` DECIMAL(10,2) NOT NULL COMMENT '变动前余额',
  `balance_after` DECIMAL(10,2) NOT NULL COMMENT '变动后余额',
  `order_id` BIGINT COMMENT '关联订单ID',
  `remark` VARCHAR(255) COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (`user_id`),
  INDEX idx_type (`type`),
  INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额流水表';

-- ============================================
-- 初始化超级管理员账号
-- 手机号：13800000000  密码：admin123  支付密码与登录密码一致
-- ============================================
INSERT INTO `user` (`phone`, `password`, `pay_password`, `nickname`, `role`)
VALUES ('13800000000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '超级管理员', 'admin');
