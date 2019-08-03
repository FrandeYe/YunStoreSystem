package com.yxp.yunstore_common.utils.demo;

/**
 * 
 * 生产者
 */
public class Producer implements Runnable{

	Pro pro = null;
	
	public Producer(Pro pro) {
		this.pro = pro;
	}
	
	
	@Override
	public void run() {
		int i = 0;
		
		while (true) {
			
			if (i == 0) {
				pro.set("张三", "男");
			}else {
				pro.set("李四", "女");
			}
			
			i = (i+1)%2;
			
		}
		
	}

}
