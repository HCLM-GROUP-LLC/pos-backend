-- 2025/10/14 创建员工手机号字段 吴涵华
ALTER TABLE `pos_db`.`employees`
    ADD COLUMN `phone_number` varchar(255) NULL DEFAULT NULL COMMENT '手机号' AFTER `last_name`;
-- 2025/10/14 创建商家ID字段 吴涵华
ALTER TABLE pos_db.devices ADD COLUMN `merchant_id` char(36) NOT NULL COMMENT '商家ID' AFTER `device_id`;
