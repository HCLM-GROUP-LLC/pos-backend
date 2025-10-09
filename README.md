# 关于本项目

## java技术选型

| 名称       | 官网                   |
|----------|----------------------|
| Sa-Token | https://sa-token.cc/ |

## [ER图](./pos-doc/POS系统E-R图设计.md)

## [数据库物理建模优化报告](./pos-doc/数据库物理建模优化报告.md)

## [POS系统数据库迁移文件](./pos-doc/V1__create_pos_system.sql)

## [代码规范](./pos-doc/代码规范.md)

## 模块说明

| 名称                  | 说明                                                   |
|---------------------|------------------------------------------------------|
| pos-common          | 公共模块                                                 |
| pos-satoken-starter | [安全验证模块](./pos-common/pos-satoken-starter/README.md) |
| pos-merchant        | 商户web端，给商户使用,不需要权限控制                                 |
| pos-terminal        | 门店ios端，给门店员工使用，按角色权限控制                               |