/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.18-log : Database - db_yunstore
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_yunstore` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_yunstore`;

/*Table structure for table `system_admin` */

DROP TABLE IF EXISTS `system_admin`;

CREATE TABLE `system_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '上级id',
  `role_id` int(11) DEFAULT NULL COMMENT '关联角色id',
  `code` varchar(255) DEFAULT NULL COMMENT '用户编码，自动生成，生成规则：由上级（创建人）的id + “-” + 自身id + “-”',
  `login_name` varchar(100) NOT NULL COMMENT '系统用户登陆名',
  `password` varchar(100) NOT NULL COMMENT '用户登陆密码',
  `creator_id` int(11) NOT NULL COMMENT '创建人编号ID',
  `salt` varchar(50) NOT NULL COMMENT '用户密码加密用的盐值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '账号状态，0禁用， 1启用',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '用户上次登陆ip',
  `last_login_time` datetime DEFAULT NULL COMMENT '用户上次登陆系统的时间',
  `super_flag` tinyint(2) DEFAULT '0' COMMENT '是否为超级管理员，0不是，1是',
  `mobile` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `login_error` tinyint(2) DEFAULT '0' COMMENT '登录错误次数，超过3次，账号被禁用5分钟',
  `remark` varchar(255) DEFAULT NULL COMMENT '用户备注',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `user_type` tinyint(2) DEFAULT NULL COMMENT '用户类型，暂时不用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
