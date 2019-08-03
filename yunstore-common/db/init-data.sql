/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.5.15 : Database - db_yunstore
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_yunstore` /*!40100 DEFAULT CHARACTER SET utf8 */;

/*Data for the table `system_admin` */

insert  into `system_admin`(`id`,`code`,`user_type`,`login_name`,`real_name`,`password`,`salt`,`role_id`,`parent_id`,`disabled_flag`,`last_login_ip`,`last_login_time`,`super_flag`,`mobile`,`login_error`,`remark`,`head_url`,`gender`,`created_at`,`updated_at`) values (1,'',1,'yxp','yxp','0rw/7PSCNIMnMprGYooBMA==','A9sO@i',0,0,1,NULL,NULL,1,NULL,0,NULL,'assets/default/img/default_head.png',0,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
