package com.yxp.yunstore_common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class EncryptUtil {

	private static final String KEY_MD5 = "MD5";
	
	private static final String KEY_SHA = "SHA";
	
	private static final int CODE_LENGTH = 6;
	
	
	public static String MD5(String str, String salt) {
		
		try {
			
			MessageDigest md = MessageDigest.getInstance(KEY_MD5);
			/**加盐*/
			salt = SHA(salt);
			str+=salt;
			
			BASE64Encoder encoder = new BASE64Encoder();
			byte[] strByteArray = md.digest(str.getBytes());
			
			return encoder.encode(strByteArray);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
        return null;
	}
	
	
	public static String SHA(String str) {
		
		try {
			MessageDigest md = MessageDigest.getInstance(KEY_SHA);
			
			BASE64Encoder encoder = new BASE64Encoder();
			byte[] strByteArray = md.digest(str.getBytes());
			
			return encoder.encode(strByteArray);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static String getRandomString() {
		
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*~?";
		int strLength = str.length();
	    Random random = new Random();
	    StringBuffer sb = new StringBuffer();
	     
	    for(int i=0; i<CODE_LENGTH; i++){
	    	 
	      int number = random.nextInt(strLength);
	      sb.append(str.charAt(number));
	       
	    }
	     
		return sb.toString();
	}
	
	public static String getRandomCode() {
		
		String str = "abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ23456789";
		int strLength = str.length();
	    Random random = new Random();
	    StringBuffer sb = new StringBuffer();
	     
	    for(int i=0; i<CODE_LENGTH; i++){
	    	 
	      int number = random.nextInt(strLength);
	      sb.append(str.charAt(number));
	       
	    }
	     
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		String salt = "yunyun";//getRandomString();
		String saltSHA = SHA(salt);
		String pwd = MD5("123456", saltSHA);
		System.out.println(salt + ">>>" + pwd + ">>>" + saltSHA);
		
	}
}
