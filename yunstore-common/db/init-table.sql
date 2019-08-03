##数据库 db_yunstore
##系统管理用户
CREATE TABLE `system_admin`( 
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`code` VARCHAR(100) DEFAULT '' COMMENT '用户编号，自动生成',
	`user_type` TINYINT(2) DEFAULT 1 COMMENT '用户类型',
	`login_name` VARCHAR(100) NOT NULL COMMENT '登录名',
	`real_name` VARCHAR(100) DEFAULT '' COMMENT '真实姓名',
	`password` VARCHAR(100) NOT NULL COMMENT '密码',
	`salt` VARCHAR(100) NOT NULL COMMENT '密码加密salt',
	`role_id` INT  DEFAULT 0 COMMENT '角色id',
	`parent_id` INT DEFAULT 0 COMMENT '上级id',
	`disabled_flag` TINYINT(2) DEFAULT 0 COMMENT '是否禁用',
	`last_login_ip` VARCHAR(50)  COMMENT '最后登录ip',
	`last_login_time` DATETIME  COMMENT '最后登录时间',
	`super_flag` TINYINT(2) DEFAULT 0 COMMENT '是否超级管理员',
	`mobile` VARCHAR(20)  COMMENT '手机号',
	`login_error` TINYINT(2) DEFAULT 0 COMMENT '登录错误次数',
	`remark` VARCHAR(255)  COMMENT '备注',
	`head_url` VARCHAR(100)  COMMENT '头像',
	`gender` TINYINT(2) DEFAULT 0  COMMENT '性别',
	`created_at` DATETIME  COMMENT '创建时间',
	`updated_at` DATETIME  COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

##删除用户表
CREATE TABLE `system_admin_invalid`( 
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`admin_id` INT(11) NOT NULL COMMENT '被删除用户id',
	`code` VARCHAR(100) DEFAULT '' COMMENT '用户编号，自动生成',
	`user_type` TINYINT(2) DEFAULT 1 COMMENT '用户类型',
	`login_name` VARCHAR(100) NOT NULL COMMENT '登录名',
	`real_name` VARCHAR(100) DEFAULT '' COMMENT '真实姓名',
	`password` VARCHAR(100) NOT NULL COMMENT '密码',
	`salt` VARCHAR(100) NOT NULL COMMENT '密码加密salt',
	`role_id` INT  DEFAULT 0 COMMENT '角色id',
	`parent_id` INT DEFAULT 0 COMMENT '上级id',
	`disabled_flag` TINYINT(2) DEFAULT 0 COMMENT '是否禁用',
	`last_login_ip` VARCHAR(50)  COMMENT '最后登录ip',
	`last_login_time` DATETIME  COMMENT '最后登录时间',
	`super_flag` TINYINT(2) DEFAULT 0 COMMENT '是否超级管理员',
	`mobile` VARCHAR(20)  COMMENT '手机号',
	`login_error` TINYINT(2) DEFAULT 0 COMMENT '登录错误次数',
	`remark` VARCHAR(255)  COMMENT '备注',
	`head_url` VARCHAR(100)  COMMENT '头像',
	`gender` TINYINT(2) DEFAULT 0  COMMENT '性别',
	`created_at` DATETIME  COMMENT '创建时间',
	`updated_at` DATETIME  COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

##省市县区域
CREATE TABLE `system_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `region_code` varchar(100) NOT NULL COMMENT '地区code',
  `region_level` tinyint(2) NOT NULL COMMENT '地区级别，1：省，2：市，3：区',
  `region_name` varchar(100) NOT NULL COMMENT '地区名称',
  `alias_name` varchar(100) DEFAULT NULL COMMENT '地区别名',
  `city_flag` tinyint(2) DEFAULT '0' COMMENT '是否直辖市',
  `parent_id` int(11) DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48041 DEFAULT CHARSET=utf8;

##笔记信息
CREATE TABLE `doc_noteinfo`( 
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`notebook_id` INT  DEFAULT 0 COMMENT '笔记本id',
	`remark` VARCHAR(255)  COMMENT '备注',
	`title` VARCHAR(100)  COMMENT '标题',
	`content` TEXT  COMMENT '笔记内容',
	`created_at` DATETIME  COMMENT '创建时间',
	`updated_at` DATETIME  COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;