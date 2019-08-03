package com.yxp.yunstore_common.utils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.jfinal.upload.UploadFile;
import com.yxp.yunstore_common.service.base.Result;


public class FileUtil {

	
	public static Result upload(UploadFile uploadFile) {
		Result result = Result.success();
		
		try {
			String fileName = null;
			if (null != uploadFile) {
				fileName = uploadFile.getFileName();
			}else {
				return Result.failure();
			}
			
			String ext = fileName.substring(fileName.indexOf("."), fileName.length());
			
			String newFileName = new Date().getTime() + UUID.randomUUID().toString() + ext;
			
			String savePath = getDiskPath(DateUtil.dateToYMD()) + File.separator + newFileName;
			
			String basePath = uploadFile.getUploadPath();
			String newPath = basePath + File.separator +savePath;
			File newFile = new File(newPath);
			
			FileUtils.copyFile(uploadFile.getFile(), newFile);
			
			uploadFile.getFile().delete();
			
			result.set("fileName", newFileName).set("savePath", savePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	* @Title: getDiskPath  
	* @Description: 按年月日生成路径
	* @param date: yyyy-MM-dd
	* @return yyyy/MM/dd
	 */
	public static String getDiskPath(String date) {
		StringBuilder diskPath = new StringBuilder("");
		for (String d : date.split("-")) {
			diskPath.append(d).append(File.separator);
		}
		return diskPath.toString().substring(0, diskPath.length() - 1);
	}
	
	
}
