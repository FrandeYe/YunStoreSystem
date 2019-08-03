package com.yxp.yunstore_common.service.base;


import com.jfinal.kit.Kv;
import com.yxp.yunstore_common.constants.EnumConstant.AppErrorCodes;

/**
 * APP 返回数据类
 * @author 作者：likanghong
 * @version 创建时间：2018年7月28日 下午12:16:17
 */
public class Result extends Kv {
    private static final long serialVersionUID = -8938145532092484557L;

	/**
	 * 返回默认成功状态
	 * @return
	 */
	public static Result success() {
	    return new Result();
	}
	
	/**
	 * 自定义消息返回的成功状态
	 * @param message
	 * @return
	 */
	public static Result success(String message) {
        return new Result(message);
    }
	
	/**
	 * 自定义状态码和自定义消息的成功状态
	 * @param code
	 * @param message
	 * @return
	 */
	public static Result success(Integer code,String message) {
        return new Result(code, message);
    }
	
	/**
	 * 返回默认的失败状态
	 * @param kv
	 * @return
	 */
	public static Result failure() {
        return new Result(AppErrorCodes.FAILURE.getValue(), AppErrorCodes.FAILURE.getName());
    }
	
	/**
     * 自定义消息返回的失败状态
     * @param message
     * @return
     */
	public static Result failure(String message) {
        return new Result(AppErrorCodes.FAILURE.getValue(), message);
    }
	
	/**
	 * 自定义状态码和自定义消息的失败状态
	 * @param code
	 * @param message
	 * @return
	 */
	public static Result failure(Integer code, String message) {
        return new Result(code, message);
    }
	
	// 构造方法
	private Result() {
	    this.set("code", AppErrorCodes.SUCCESS.getValue());
	    this.set("message", AppErrorCodes.SUCCESS.getName());
	}
    
	private Result(String message) {
	    this.set("code", AppErrorCodes.SUCCESS.getValue());
        this.set("message", message);
    }
    
	private Result(int code, String message) {
        this.set("code", code);
        this.set("message", message);
    }

	public int getCode() {
		return this.getInt("code");
	}

	public String getMessage() {
		return this.getStr("message");
	}

    // 判断是否成功
    public boolean isSuccess() {
        return getCode() == AppErrorCodes.SUCCESS.getValue() ? true : false;
    }
}
