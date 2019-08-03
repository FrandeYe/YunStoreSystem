ALTER TABLE `system_admin`   
  ADD COLUMN `province_name` varchar(100)  default ''  COMMENT '所在省名称' AFTER `mobile`;
  
ALTER TABLE `system_admin`   
  ADD COLUMN `province_code` varchar(100)  default ''  COMMENT '所在省编码' AFTER `mobile`;
  
ALTER TABLE `system_admin`   
  ADD COLUMN `city_name` varchar(100)  default ''  COMMENT '所在市名称' AFTER `mobile`;
  
ALTER TABLE `system_admin`   
  ADD COLUMN `city_code` varchar(100)  default ''  COMMENT '所在市编码' AFTER `mobile`;
  
ALTER TABLE `system_admin`   
  ADD COLUMN `county_name` varchar(100)  default ''  COMMENT '所在区/县名称' AFTER `mobile`;
  
ALTER TABLE `system_admin`   
  ADD COLUMN `county_code` varchar(100)  default ''  COMMENT '所在区/县编码' AFTER `mobile`;