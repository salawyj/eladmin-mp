
-- 手机验证码
CREATE TABLE `sms_code` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `phone` VARCHAR(20) NOT NULL,
                            `code` VARCHAR(10) NOT NULL COMMENT '验证码',
                            `type` TINYINT DEFAULT 1 COMMENT '1-登录 2-注册',
                            `ip` VARCHAR(50) COMMENT '请求IP',
                            `expire_time` DATETIME NOT NULL COMMENT '过期时间',
                            `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                            INDEX `idx_phone` (`phone`)
);


-- ----------------------------
-- Table structure for app_agent
-- ----------------------------
DROP TABLE IF EXISTS `app_agent`;
CREATE TABLE `app_agent` (
                             `agent_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `name` varchar(100) NOT NULL COMMENT '名称',
                             `agent_url` varchar(1024) DEFAULT NULL COMMENT '智能体的链接',
                             `description` varchar(255) DEFAULT NULL COMMENT '描述',
                             `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                             `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`agent_id`) USING BTREE,
                             UNIQUE KEY `uniq_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='app智能体表';

-- ----------------------------
-- Records of app_agent
-- ----------------------------
BEGIN;
INSERT INTO `app_agent` (`agent_id`, `name`, `agent_url`, `description`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, '普通用户', '<iframe src="http://8.217.1.253/chatbot/DDxqHjtMtMpI1GML" style="width: 100%; height: 100%; min-height: 700px" frameborder="0" allow="microphone"> </iframe>', 's说明-',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
COMMIT;


DROP TABLE IF EXISTS `app_amount`;
CREATE TABLE `app_amount` (
                              `amount_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              `app_user_id` bigint(20) NOT NULL COMMENT 'app用户id',
                              `recharge_total` DECIMAL(9,2) DEFAULT NULL COMMENT '充值总额',
                              `gift_total` DECIMAL(9,2) DEFAULT NULL COMMENT '获赠总额',
                              `recharge_balance` DECIMAL(9,2) DEFAULT NULL COMMENT '充值余额',
                              `gift_balance` DECIMAL(9,2) DEFAULT NULL COMMENT '获赠余额',
                              `unit_` varchar(255) DEFAULT NULL COMMENT '人民币，元',
                              `invite_num` bigint(20) NOT NULL COMMENT '邀请人数量',
                              `gift_num` bigint(20) NOT NULL COMMENT '获赠人数量',
                              `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                              `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`amount_id`) USING BTREE,
                              UNIQUE KEY `uniq_name` (`app_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='app金额表';

-- ----------------------------
-- Records of app_amount
-- ----------------------------
BEGIN;
INSERT INTO `app_amount` (`amount_id`, `app_user_id`, `recharge_total`,  `gift_total`,  `recharge_balance`,  `gift_balance`, `unit_`, `invite_num`,`gift_num`, `create_by`, `update_by`,`create_time`, `update_time`) VALUES (2, 2, 2.50, 2.00,2.50,0.50,'元',  0,0,NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');

COMMIT;

DROP TABLE IF EXISTS `app_gift_amount`;
CREATE TABLE `app_gift_amount` (
                                   `gift_amount_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `app_user_id` bigint(20) NOT NULL COMMENT 'app用户id',
                                   `gift_amount` DECIMAL(9,2) DEFAULT NULL COMMENT '获赠金额',
                                   `gift_balance` DECIMAL(9,2) DEFAULT NULL COMMENT '获赠余额',
                                   `unit_` varchar(255) DEFAULT NULL COMMENT '人民币，元',
                                   `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                                   `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `invalid_time` datetime DEFAULT NULL COMMENT '失效时间',
                                   `status_` TINYINT DEFAULT 1 COMMENT '0-失效 1-有效',
                                   PRIMARY KEY (`gift_amount_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='app获赠金额记录表';


-- ----------------------------
-- Table structure for app_invite_rules app邀请规则表
-- ----------------------------
DROP TABLE IF EXISTS `app_invite_rules`;
CREATE TABLE `app_invite_rules` (
                                    `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `name` varchar(100) NOT NULL COMMENT '名称',
                                    `invite_num` bigint(20) DEFAULT NULL COMMENT '邀请人数',
                                    `gift_balance` DECIMAL(9,2)  DEFAULT NULL COMMENT '获赠余额',
                                    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                                    `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='app邀请规则表';

-- ----------------------------
-- Records of app_invite_rules
-- ----------------------------
BEGIN;
INSERT INTO `app_invite_rules` (`rule_id`, `name`, `invite_num`, `gift_balance`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, '赠送余额', 1, '0.50',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
INSERT INTO `app_invite_rules` (`rule_id`, `name`, `invite_num`, `gift_balance`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (3, '赠送余额', 5, '3.00',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
INSERT INTO `app_invite_rules` (`rule_id`, `name`, `invite_num`, `gift_balance`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (4, '赠送余额', 10, '4.00',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
INSERT INTO `app_invite_rules` (`rule_id`, `name`, `invite_num`, `gift_balance`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (5, '赠送余额', 20, '6.00',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
INSERT INTO `app_invite_rules` (`rule_id`, `name`, `invite_num`, `gift_balance`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (6, '赠送余额', 30, '12.00',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
INSERT INTO `app_invite_rules` (`rule_id`, `name`, `invite_num`, `gift_balance`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (7, '赠送余额', 50, '25.00',  NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');


COMMIT;


-- ----------------------------
-- Table structure for 用户邀请
-- ----------------------------
DROP TABLE IF EXISTS `app_invite_code`;
CREATE TABLE `app_invite_code` (
                                   `invite_code_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `code` VARCHAR(10) DEFAULT NULL COMMENT '邀请码',
                                   `app_user_id` bigint(20) NOT NULL COMMENT '所属用户ID',
                                   `status` TINYINT DEFAULT 0 COMMENT '0-未使用 1-已使用',
                                   `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   `expire_time` DATETIME COMMENT '过期时间',
                                   PRIMARY KEY (`invite_code_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='app用户邀请码表';

DROP TABLE IF EXISTS `app_invite_record`;
CREATE TABLE `app_invite_record` (
                                     `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `inviter_id` bigint(20) NOT NULL COMMENT '邀请用户id',
                                     `invitee_id` bigint(20) NOT NULL COMMENT '被邀请用户id',
                                     `reward_status` TINYINT DEFAULT 0 COMMENT '0-未发放 1-已发放',
                                     `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                     PRIMARY KEY (`record_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='app用户邀请记录表';

