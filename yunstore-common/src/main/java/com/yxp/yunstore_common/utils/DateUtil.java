package com.yxp.yunstore_common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
* @ClassName: DateUtil  
* @Description: 日期工具类
 */
public class DateUtil {

	 private static final SimpleDateFormat DAY = getFormat("yyyy-MM-dd");
	
	 
	 private static SimpleDateFormat getFormat(String format) {
	        return new SimpleDateFormat(format);
	 }
	 
	 
	public static String dateToYMD() {
		DateFormat format = DAY;
		return format.format(new Date());
	}
	
	
	
	 /**
     * 本月第一天
     * @return
     */
    public static String MonthFirstDay() {
        // 规定返回日期格式
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        // 设置为第一天
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return DAY.format(gcLast.getTime()) + " 00:00:00";
    }
	
    /**
     * 本月最后一天
     * 
     * @return
     */
    public static String MonthLastDay() {
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return DAY.format(calendar.getTime()) + " 23:59:59";
    }

    
    /**
     * 根据月份获取一共多少天
     * @param time
     * @return
     */
    public static int daysByCount(String time) {
    	String[] dayArr = time.split("-");
    	int year = Integer.valueOf(dayArr[0]);
    	int month = Integer.valueOf(dayArr[1]);
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.YEAR, year);
    	calendar.set(Calendar.MONTH, month - 1);
    	calendar.set(Calendar.DATE, 1);
    	calendar.roll(Calendar.DATE, -1);
    	return calendar.get(Calendar.DATE);
    }
    
    /**
     * 获取上月份
     * @return
     */
    public static String lastMonth() {
		LocalDate today = LocalDate.now();
		today = today.minusMonths(1);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM");
		return formatters.format(today);
	}
    
    /**
     * 获取上个月第一天日期
     * @return
     */
    public static String lastMonthFirstDay() {
    	return lastMonth() + "-01" + " 00:00:00";
    }
    
    
    /**
     * 获取上个月最后一天日期
     * @return
     */
    public static String lastMonthLastDay() {
    	String month = lastMonth();
    	return month + "-" + daysByCount(month) + " 23:59:59";
    }
    
    
    
    /**
     * 昨天
     * @return
     */
    public static String yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return DAY.format(calendar.getTime());
    }
    
    
}
