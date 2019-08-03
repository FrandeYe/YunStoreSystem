package com.yxp.yunstore_common.utils.demo;

public class Pro {

	/**姓名*/
	private String name;
	
	/**性别*/
	private String gender;
	
	private boolean full = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public synchronized void set(String name, String gender) {
		if (full) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
			this.name = name;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.gender = gender;
			full = true;
			notify();
	}
	
	public synchronized void get() {
		if (!full) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			System.out.println(this.name + "----" + this.gender);
			full = false;
			notify();
	}
}
