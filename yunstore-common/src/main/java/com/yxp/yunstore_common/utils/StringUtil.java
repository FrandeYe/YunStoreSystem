package com.yxp.yunstore_common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtil {

	private static String DEFAULT_CODE = "UTF-8";

	public static String[] split(String property, String separator) throws Exception {
		if (separator == null || separator.length() == 0)
			return property.split(",");
		else
			return property.split(separator);
	}

	public static String toString(List<String> list, String separator) {
		if (isNotEmpty(separator)) {
			separator = ",";
		}
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s).append(",");
		}
		if (sb.length() < 1) {
			return null;
		} else {
			return sb.substring(0, sb.length() - 1);
		}
	}

	public static List<String> array2list(String properties[]) {
		if (properties.length > 0)
			return Arrays.asList(properties);
		else
			return null;
	}

	public static boolean isNotEmpty(String property) {
		return property != null && property.trim().length() > 0 ? true : false;
	}

	public static boolean isNotEmpty(Object property) {
		return property != null ? true : false;
	}

	public static boolean isNotEmpty(Boolean property) {
		return property != null && property;
	}

	public static boolean isNotEmpty(Date property) {
		return property != null ? true : false;
	}

	public static boolean isNotEmpty(Object[] properties) {
		return properties != null && properties.length > 0 ? true : false;
	}

	public static boolean isNotEmpty(Double properties) {
		return properties != null && properties != 0d ? true : false;
	}

	public static boolean isEmpty(Object properties) {
		return properties == null ? true : false;
	}

	public static boolean isEmpty(String[] properties) {
		return properties == null || properties.length == 0 ? true : false;
	}

	public static boolean isEmpty(String properties) {
		return properties == null || properties.length() == 0 || properties.trim().length()==0 ? true : false;
	}

	public static boolean isEmpty(Double properties) {
		return properties == null || properties == 0d ? true : false;
	}

	public static boolean isNotContain(String property, String value) {
		return property != null && property.trim().contains(value) ? false : true;
	}

	public static <T> boolean isNotEmpty(Collection<T> property) {
		return property != null && property.size() > 0 ? true : false;
	}

	public static boolean isNotEmpty(Integer property) {
		return property != null && property != 0 ? true : false;
	}

	public static String date(int day) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(calendar.getTime());
		return dateStr;
	}

	public static Date string2date(String property, String pattern) throws Exception {
		if (isNotEmpty(property)) {
			if (isNotEmpty(pattern)) {
				return new SimpleDateFormat(pattern).parse(property);
			}
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(property);
	}

	public static String currentTimestamps() {
		try {
			return date2string(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "201401010101";
	}

	public static String nowNumber() {
		try {
			return date2string(new Date(), "yyyyMMddHHmmss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "201401010101";
	}

	public static String date2string(String pattern) throws Exception {
		if (isNotEmpty(pattern)) {
			return new SimpleDateFormat(pattern).format(new Date());
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String date2string(Date property, String pattern) throws Exception {
		if (property == null)
			return "";
		if (isNotEmpty(pattern)) {
			return new SimpleDateFormat(pattern).format(property);
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(property);
	}

	public static String date2string(Date property) {
		if (property == null)
			return "";
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(property);
	}

	public static String date3string(Date property) {
		return new SimpleDateFormat("yyyy-MM-dd").format(property);
	}

	public static boolean compareEndDateIsBeyondCurrenDate(Date date) throws Exception {
		if (StringUtil.isNotEmpty(date)) {
			final SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = d.format(new Date()).replace("-", "");
			String endDate = d.format(date).replace("-", "");
			return Long.parseLong(currentDate) > Long.parseLong(endDate);
		}
		return false;
	}

	public static int object2int(Object property) {
		if (isNotEmpty(property)) {
			if (isNotEmpty(property.toString())) {
				return Integer.parseInt(property.toString());
			}
		}
		return 0;
	}

	public static Date nextYearToday() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(calendar.getTime());
		return sdf.parse(dateStr);
	}

	public static Date string2date(String property) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(property);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date string2sdate(String property) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(property);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long ipToNum(String ip) throws Exception {
		Long nip = null;
		if (isNotEmpty(ip)) {
			String[] ips = ip.split("\\.");
			if (ips.length == 4) {
				nip = 256 * (Long.parseLong(ips[2]) + 256 * (Long.parseLong(ips[1]) + 256 * Long.parseLong(ips[0]))) + Long.parseLong(ips[3]);
			}
		}
		return nip;
	}

	// public static boolean isMobile(String mobile) {
	// try {
	// if (isEmpty(mobile))
	// return false;
	// Pattern p = Pattern
	// .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	// Matcher m = p.matcher(mobile);
	// return m.matches();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	/**
	 * 手机号验证
	 * 
	 * @author ： 2016年12月5日下午4:34:46
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @author ： 2016年12月5日下午4:34:21
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}
	
	/**
	 * 电话号码验证
	 * 
	 * @author ： 2016年12月5日下午4:34:21
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhoneAndMobile(String str) {
		Pattern p1 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^((0[0-9]{2,4})-)([0-9]{7,8})(-([0-9]{3,}))?$|^(400-)([0-9]{7})$|^(13|14|15|16|18|17|19)[0-9]{9}$"); // 验证带区号的
		m = p1.matcher(str);
		b = m.matches();
		return b;
	}
	

	/**
	 * 
	 * TODO 密码md5加密
	 * 
	 * @param password
	 * @return
	 */
	public static String md5(String password) {
		MessageDigest md;
		try {
			// 生成一个MD5加密计算摘要
			md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(password.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	/**
	 * 
	 * TODO date类型当前日期
	 * 
	 * @return
	 */
	public static Date newDate() {
		Date date = new Date();
		return date;
	}

	public static String getYesterdays() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	public static String getCurrentMonthFirstDay() {
		return new SimpleDateFormat("yyyy-MM-01").format(new Date());
	}

	/**
	 * 
	 * TODO 生成当前日期
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getToday() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 
	 * TODO 生成like条件
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String like(String property) throws Exception {
		if (isNotEmpty(property)) {
			return String.format("%%%s%%", property);
		}
		return "";
	}

	/**
	 * 
	 * TODO 解码
	 * 
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public static String decode(String property) {
		if (isEmpty(property)) {
			return "";
		}
		try {
			return URLDecoder.decode(property, DEFAULT_CODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * TODO 加码
	 * 
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public static String encode(String property) throws Exception {
		if (isEmpty(property)) {
			return "";
		}
		return URLEncoder.encode(property, DEFAULT_CODE);
	}

	public static Date getBeforeMonth(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - months);
		return calendar.getTime();
	}

	public static Date getNextMonth(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months);
		return calendar.getTime();
	}

	/**
	 * 
	 * TODO 获取前30天的日期
	 * 
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public static String ThirtyDays(String property) throws Exception {
		Calendar calendar = Calendar.getInstance();// 日历对象
		Date startDate = StringUtil.string2sdate(property);
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, -30);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * 发布产品选择有效期
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getNextDate(int daycount) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (daycount == 10 || daycount == 20) {
			cal.add(Calendar.DATE, daycount);
		} else {
			cal.add(Calendar.MONTH, daycount);
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
	}

	/**
	 * 发布产品修改生效时间转换
	 */
	public static long getNextDay(String startDate, String stopDate) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long diff;
		// 获得两个时间的毫秒时间差异
		diff = sd.parse(stopDate).getTime() - sd.parse(startDate).getTime();
		long day = diff / nd;// 计算差多少天
		System.out.println("时间相差：" + day + "天");
		return day;
	}

	/**
	 * 计算日期差值(返回天数)
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static long minusDate(Date endDate, Date beginDate) {
		return (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 计算日期差值(返回天数)
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static long minusDate(Date endDate) {
		return (endDate.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24);
	}


	public static String decimalFormat(BigDecimal decimal, String pattern) {
		DecimalFormat myformat = new DecimalFormat();
		if (decimal.doubleValue() == 0) {
			return "0.00";
		}
		if (isNotEmpty(pattern)) {
			myformat.applyPattern(pattern);
		} else {
			myformat.applyPattern("##,###.00");
		}
		return myformat.format(decimal);
	}

	/**
	 * 去除字符串全角和半角空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trimStr(String str) {

		return str == null ? str : str.replaceAll("\\s*]", " ").replaceAll(" ", "").trim();
	}

	public static void main(String[] args) {
//		String s = "20000";
//		System.out.println(StringUtil.isInteger(s));
		System.out.println(StringUtil.isEmail("192@qq.com"));;
	}

	/**
	 * 验证是否为正整数 包括0 如001
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[0-9]{1,}$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否为正浮点数（double和float）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * email校验
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		Pattern pattern = Pattern.compile("([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static Date string2Date(String property, String pattern) {
		if (isNotEmpty(property)) {
			if (isNotEmpty(pattern)) {
				try {
					return new SimpleDateFormat(pattern).parse(property);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(property);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
