package com.yxp.yunstore_common.utils.demo;

public class Main {

	public static void main(String[] args) {
		Pro pro = new Pro();
		new Thread(new Producer(pro)).start();
		new Thread(new Consumer(pro)).start();
	}
	
}
