package com.yxp.yunstore_common.utils.demo;

/**
 * 消费者
 */
public class Consumer implements Runnable{

	Pro pro = null;
	
	public Consumer(Pro pro) {
		this.pro = pro;
	}
	
	
	@Override
	public void run() {
		while(true) {
			pro.get();
		}
	}

}
