-- ============================================
-- 影院票务管理系统 - 测试数据
-- 执行前请确保已执行 init.sql
-- ============================================

USE cinema_ticket;

-- ============================================
-- 1. 测试用户（所有密码均为 admin123）
-- BCrypt hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K
-- ============================================

-- 普通用户（含余额和支付密码，支付密码: 123456）
-- 支付密码 BCrypt hash: $2a$10$EqKcp1WFKs3D.YLwDCMSGeQEOzJRkP0k0Xq8JIDF.7FRjL1KzHNfi
INSERT INTO `user` (`phone`, `password`, `pay_password`, `nickname`, `role`, `balance`) VALUES
('13900001111', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K',
 '$2a$10$EqKcp1WFKs3D.YLwDCMSGeQEOzJRkP0k0Xq8JIDF.7FRjL1KzHNfi',
 '测试用户张三', 'user', 500.00),
('13900002222', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K',
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '测试用户李四', 'user', 200.00),
('13900003333', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K',
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '测试用户王五', 'user', 0.00);

-- 售票员（支付密码与登录密码一致）
INSERT INTO `user` (`phone`, `password`, `pay_password`, `nickname`, `role`) VALUES
('13800001111', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '售票员小刘', 'seller'),
('13800002222', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '售票员小陈', 'seller');

-- 财务人员（支付密码与登录密码一致）
INSERT INTO `user` (`phone`, `password`, `pay_password`, `nickname`, `role`) VALUES
('13800003333', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE7u8y8qY4KRmgZ.K', '财务小赵', 'finance');

-- ============================================
-- 2. 影片数据（海报使用 picsum.photos 随机图片）
-- status: 0=下架, 1=热映, 2=即将上映
-- ============================================
INSERT INTO `movie` (`title`, `poster`, `director`, `actors`, `genre`, `duration`, `release_date`, `description`, `status`) VALUES
-- ID=1 热映（排期引用）
('星际穿越2：时间尽头', 'https://picsum.photos/seed/interstellar2/300/450', '克里斯托弗·诺兰',
 '马修·麦康纳,安妮·海瑟薇,迈克尔·凯恩,杰西卡·查斯坦', '科幻/冒险', 169, '2026-01-25',
 '库珀再次穿越虫洞，发现一个正在崩塌的平行维度。为了拯救两个宇宙中的人类文明，他必须在时间的尽头做出终极抉择。', 1),

-- ID=2 热映（排期引用）
('唐人街探案4', 'https://picsum.photos/seed/chinatown4/300/450', '陈思诚',
 '王宝强,刘昊然,妻夫木聪,托尼·贾', '喜剧/悬疑', 136, '2026-02-01',
 '唐仁和秦风受邀前往伦敦，卷入一桩皇室珠宝失窃案。层层迷雾之下隐藏着惊天阴谋，CRIMASTER排名再次洗牌。', 1),

-- ID=3 热映（排期引用）
('长安三万里', 'https://picsum.photos/seed/changan3w/300/450', '谢君伟',
 '杨天翔,凌振赫,吴俊全,宣晓鸣', '动画/历史', 168, '2026-02-14',
 '安史之乱后，高适回忆起与李白跨越数十年的深厚友谊。盛唐诗人们的理想与现实在历史洪流中交织碰撞。', 1),

-- ID=4 热映（排期引用）
('哪吒之魔童闹海', 'https://picsum.photos/seed/nezha2/300/450', '饺子',
 '吕艳婷,囧森瑟夫,瀚墨,陈浩', '动画/奇幻', 144, '2026-01-29',
 '哪吒与敖丙携手闯入龙族秘境，面对远古封印中苏醒的混沌之力。天命难违，但我命由我不由天。', 1),

-- ID=5 热映
('悬崖之上2', 'https://picsum.photos/seed/cliff2/300/450', '张艺谋',
 '张译,于和伟,秦海璐,朱亚文', '谍战/悬疑', 132, '2026-02-20',
 '1940年冬，特工小组再次潜入哈尔滨执行代号"火种"的秘密任务。一场殊死博弈在冰城上演。', 1),

-- ID=6 热映
('满江红2', 'https://picsum.photos/seed/river2/300/450', '张艺谋',
 '沈腾,易烊千玺,张译,雷佳音', '悬疑/喜剧', 145, '2026-02-10',
 '南宋末年，一封藏有惊天秘密的密信引发朝堂震动。小人物们用智慧和勇气书写属于自己的传奇。', 1),

-- ID=7 热映
('消失的她2', 'https://picsum.photos/seed/missing2/300/450', '崔睿',
 '朱一龙,倪妮,文咏珊,杜江', '悬疑/犯罪', 128, '2026-02-28',
 '律师何非收到一封来自"已故"委托人的求救信。真相与谎言在暗流涌动的海岛上逐层揭开。', 1),

-- ID=8 热映
('封神第二部：战火西岐', 'https://picsum.photos/seed/fengshen2/300/450', '乌尔善',
 '费翔,于适,娜然,此沙', '奇幻/动作', 155, '2026-01-20',
 '姬发率领西岐大军对抗殷商，众神加入战场。一场关乎人间命运的封神大战拉开帷幕。', 1),

-- ID=9 即将上映
('阿凡达3：带种者', 'https://picsum.photos/seed/avatar3/300/450', '詹姆斯·卡梅隆',
 '萨姆·沃辛顿,佐伊·索尔达娜,西格妮·韦弗', '科幻/冒险', 182, '2026-04-18',
 '杰克·萨利一家被迫离开海洋部落，踏上前往潘多拉火山地带的旅程，遇见了驾驭火焰的灰烬族人。', 2),

-- ID=10 即将上映
('蜘蛛侠：新纪元', 'https://picsum.photos/seed/spiderman2026/300/450', '乔·瓦茨',
 '汤姆·赫兰德,赞达亚,本尼迪克特·康伯巴奇', '动作/科幻', 148, '2026-05-01',
 '多元宇宙的裂缝再次撕开，彼得·帕克必须联合各个维度的蜘蛛侠阻止现实崩塌。', 2),

-- ID=11 即将上映
('你的名字2', 'https://picsum.photos/seed/yourname2/300/450', '新海诚',
 '神木隆之介,上白石萌音,成田凌', '动画/爱情', 112, '2026-04-05',
 '那场彗星事件过去多年后，男女主角在东京重逢。一场新的时空异变正悄然降临，命运再次将两人紧紧相连。', 2),

-- ID=12 即将上映
('速度与激情11', 'https://picsum.photos/seed/fast11/300/450', '路易斯·莱特里尔',
 '范·迪塞尔,杰森·莫玛,查理兹·塞隆', '动作/冒险', 140, '2026-05-16',
 '多姆和他的家族将面对迄今为止最强大的敌人。从地下赛道到外太空，一场终极决战即将展开。', 2),

-- ID=13 即将上映
('名侦探柯南：银幕中的侦探', 'https://picsum.photos/seed/conan2026/300/450', '立川让',
 '高山南美,山崎和佳奈,小山力也', '动画/悬疑', 110, '2026-04-12',
 '柯南一行人参加电影节时遭遇连环爆炸案。凶手竟然模仿经典电影中的犯罪手法，一场银幕内外的推理对决开始了。', 2),

-- ID=14 即将上映
('沙丘3', 'https://picsum.photos/seed/dune3/300/450', '丹尼斯·维伦纽瓦',
 '提莫西·查拉梅,赞达亚,弗洛伦斯·皮尤', '科幻/冒险', 172, '2026-06-20',
 '保罗·厄崔迪统治的帝国面临内忧外患。当远方星系的未知势力入侵，他必须面对预言中最残酷的未来。', 2),

-- ID=15 下架
('误杀3', 'https://picsum.photos/seed/wusha3/300/450', '甘露',
 '肖央,任达华,陈冲,姜皓文', '犯罪/悬疑', 118, '2025-12-28',
 '一个普通父亲为了保护家人，精心策划了一场完美的反击。法律的灰色地带里，正义与罪恶的界限愈发模糊。', 0);

-- ============================================
-- 3. 影厅数据
-- ============================================
INSERT INTO `hall` (`name`, `rows`, `cols`, `total_seats`) VALUES
('1号厅（标准厅）', 8, 10, 80),
('2号厅（巨幕厅）', 10, 12, 120),
('3号厅（VIP厅）', 6, 8, 48),
('4号厅（标准厅）', 8, 10, 80);

-- ============================================
-- 4. 座位数据 - 为所有影厅生成座位
-- ============================================

-- 1号厅：8排10列 = 80座 (普通座)
INSERT INTO `hall_seat` (`hall_id`, `row_num`, `col_num`, `seat_type`)
SELECT 1, r.n, c.n, 'normal'
FROM
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8) r
CROSS JOIN
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) c;

-- 2号厅：10排12列 = 120座 (普通座)
INSERT INTO `hall_seat` (`hall_id`, `row_num`, `col_num`, `seat_type`)
SELECT 2, r.n, c.n, 'normal'
FROM
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) r
CROSS JOIN
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) c;

-- 3号厅VIP：6排8列 = 48座 (VIP座)
INSERT INTO `hall_seat` (`hall_id`, `row_num`, `col_num`, `seat_type`)
SELECT 3, r.n, c.n, 'vip'
FROM
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6) r
CROSS JOIN
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8) c;

-- 4号厅：8排10列 = 80座 (普通座)
INSERT INTO `hall_seat` (`hall_id`, `row_num`, `col_num`, `seat_type`)
SELECT 4, r.n, c.n, 'normal'
FROM
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8) r
CROSS JOIN
  (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) c;

-- ============================================
-- 5. 排期数据（今天 ~ 后天，共3天）
-- ============================================
INSERT INTO `schedule` (`movie_id`, `hall_id`, `show_date`, `start_time`, `end_time`, `price`) VALUES
-- === 今天 ===
-- 1号厅
(1, 1, CURDATE(), '09:30:00', '12:23:00', 45.00),
(2, 1, CURDATE(), '13:00:00', '15:09:00', 40.00),
(3, 1, CURDATE(), '16:00:00', '18:23:00', 42.00),
(1, 1, CURDATE(), '19:30:00', '22:23:00', 50.00),
-- 2号厅（巨幕加价）
(1, 2, CURDATE(), '10:00:00', '12:53:00', 60.00),
(3, 2, CURDATE(), '14:00:00', '16:23:00', 55.00),
(2, 2, CURDATE(), '17:00:00', '19:09:00', 55.00),
(1, 2, CURDATE(), '20:00:00', '22:53:00', 65.00),
-- 3号厅VIP
(1, 3, CURDATE(), '14:30:00', '17:23:00', 88.00),
(3, 3, CURDATE(), '19:00:00', '21:23:00', 88.00),
-- 4号厅
(4, 4, CURDATE(), '10:00:00', '11:46:00', 35.00),
(2, 4, CURDATE(), '13:00:00', '15:09:00', 40.00),
(4, 4, CURDATE(), '16:00:00', '17:46:00', 35.00),

-- === 明天 ===
(1, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:30:00', '12:23:00', 45.00),
(2, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '13:00:00', '15:09:00', 40.00),
(3, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '16:00:00', '18:23:00', 42.00),
(1, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '12:53:00', 60.00),
(2, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '16:09:00', 55.00),
(1, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '19:00:00', '21:53:00', 88.00),
(4, 4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:46:00', 35.00),
(4, 4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:46:00', 35.00),

-- === 后天 ===
(1, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '12:53:00', 45.00),
(2, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '16:09:00', 40.00),
(1, 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '12:53:00', 60.00),
(3, 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '16:23:00', 55.00),
(1, 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '19:00:00', '21:53:00', 88.00);

-- ============================================
-- 6. 测试订单和余额流水（张三的历史订单）
-- ============================================

-- 张三已支付订单（排期1，座位3排5列、3排6列）
INSERT INTO `order` (`order_no`, `user_id`, `schedule_id`, `total_price`, `seat_count`, `status`, `source`, `pay_type`, `pay_time`)
VALUES ('ORDER20260306100001', 2, 1, 90.00, 2, 'paid', 'online', 'balance', NOW());

INSERT INTO `order_seat` (`order_id`, `seat_id`, `row_num`, `col_num`) VALUES
(1, 25, 3, 5),
(1, 26, 3, 6);

-- 张三的余额流水
INSERT INTO `balance_log` (`user_id`, `type`, `amount`, `balance_before`, `balance_after`, `remark`) VALUES
(2, 'recharge', 600.00, 0.00, 600.00, '账户充值'),
(2, 'consume', 90.00, 600.00, 510.00, '购票消费');

-- 张三待支付订单（排期5，座位5排6列）
INSERT INTO `order` (`order_no`, `user_id`, `schedule_id`, `total_price`, `seat_count`, `status`, `source`)
VALUES ('ORDER20260306100002', 2, 5, 60.00, 1, 'unpaid', 'online');

INSERT INTO `order_seat` (`order_id`, `seat_id`, `row_num`, `col_num`) VALUES
(2, 146, 5, 6);

-- ============================================
-- 测试账号速查表
-- ============================================
-- | 角色     | 手机号      | 密码     | 支付密码 |
-- |----------|-------------|----------|----------|
-- | 管理员   | 13800000000 | admin123 | admin123 |
-- | 售票员   | 13800001111 | admin123 | admin123 |
-- | 售票员   | 13800002222 | admin123 | admin123 |
-- | 财务     | 13800003333 | admin123 | admin123 |
-- | 用户张三 | 13900001111 | admin123 | 123456   |
-- | 用户李四 | 13900002222 | admin123 | admin123 |
-- | 用户王五 | 13900003333 | admin123 | admin123 |
-- ============================================
