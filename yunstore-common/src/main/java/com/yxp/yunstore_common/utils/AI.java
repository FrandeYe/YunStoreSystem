package com.yxp.yunstore_common.utils;

import java.util.Random;

public class AI {

	public static int[] sort1() {
		int[] intArr = new int[100000];
		for(int i = 10000; i >0 ; i--) {
			int temp = new Random().nextInt(10000);
			intArr[i] = temp;
		}
		System.out.println("intArr.length="+intArr.length);
		return intArr;
	}
	
	public static void main(String[] args) {
		System.out.println(args);
		p();
	}
	
	
	/**
	 * int [][] 高度 长度
	 * */
	public static void p() {
		int [][] que = new int[10][6];
		int width = que[0].length;
		int height = que.length;
		System.out.println("width:"+width + "-height:"+height);
		
		
		System.out.println("begin");
		
		for(int x = 0; x < height; x++) {
			for(int y = 0; y <= width; y++) {
				if (y > 0) {
					System.out.print("$");
				}
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("end");
	}
	
}
