##一.将php时间戳类型更改为时间类型


ALTER TABLE `lr_address` add COLUMN `addtime` datetime NULL;
UPDATE `lr_address` SET  `addtime` = FROM_UNIXTIME(`add_time`);
ALTER TABLE lr_address DROP add_time;
ALTER TABLE lr_address CHANGE addtime add_time datetime NULL;

4-6
ALTER TABLE `lr_goods_order` add COLUMN `unpaid_expiry_time2` datetime NULL;
UPDATE `lr_goods_order` SET  `unpaid_expiry_time2` = FROM_UNIXTIME(`unpaid_expiry_time`);
ALTER TABLE lr_goods_order DROP unpaid_expiry_time;
ALTER TABLE lr_goods_order CHANGE unpaid_expiry_time2 unpaid_expiry_time datetime NULL;

##二.兼容emoji表情
Winows中的配置文件为my.ini。

1.修改mysql的配置文件:
[client]
default-character-set=utf8mb4
[mysqld]
character-set-client-handshake = FALSE
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
init_connect=’SET NAMES utf8mb4'
[mysql]
default-character-set=utf8mb4

2.更改字段字符集
MODIFY COLUMN `message` varchar(200) CHARACTER SET utf8mb4 NULL DEFAULT NULL AFTER `rating`
