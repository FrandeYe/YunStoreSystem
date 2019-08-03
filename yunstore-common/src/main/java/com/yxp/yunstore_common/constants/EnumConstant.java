package com.yxp.yunstore_common.constants;


public final class EnumConstant {

	public enum SelectTimeType {
	  TODAY(1, "今日"), YESTERDAY(2, "昨天"), WEEK(3, "近七天"), MONTH(4, "近30天");

		private int value;

		private String name;

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private SelectTimeType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public static SelectTimeType getEnum(int value) {
			for (SelectTimeType c : SelectTimeType.values()) {
				if (value == c.getValue()) {
					return c;
				}
			}
			return null;
		}
	}

	/**请求处理结果*/
	public enum AppErrorCodes {

		SUCCESS(0, "处理成功"), FAILURE(-1, "处理失败");

		private int value;
		private String name;

		private AppErrorCodes(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		public static AppErrorCodes getEnum(int value) {
			for (AppErrorCodes c : AppErrorCodes.values()) {
				if (value == c.getValue()) {
					return c;
				}
			}
			return null;
		}
	}
	
	
	
	public enum DisabledFlag {
		NO(0,"禁用"),YES(1,"启用");
		
		private int value;
		private String name;

		private DisabledFlag(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static DisabledFlag getEnum(int value) {
			for (DisabledFlag p : DisabledFlag.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	/**更新类型*/
	public enum UpdateType {
		CREATE(0,"创建"),UPDATE(1,"更新");
		
		private int value;
		private String name;

		private UpdateType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static UpdateType getEnum(int value) {
			for (UpdateType p : UpdateType.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	/**消息状态*/
	public enum NoticeStatus{
		NEW(0, "未读"), VISITED(1, "已读"),IGNORE(3, "忽略");
		private int value;
		private String name;
		
		private NoticeStatus(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static NoticeStatus getEnum(int value) {
			for (NoticeStatus p : NoticeStatus.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	
	/**交易类型*/
	public enum TraderType{
		BUYGOODS(1, "购买商品"), REFUND(2, "退款");
		private int value;
		private String name;
		
		private TraderType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static TraderType getEnum(int value) {
			for (TraderType p : TraderType.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	
	/**收入支出*/
	public enum InAndOut{
		INCOME(1, "收入"), EXPEND(2, "支出");
		private int value;
		private String name;
		
		private InAndOut(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static InAndOut getEnum(int value) {
			for (InAndOut p : InAndOut.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	
	/**支付渠道*/
	public enum PaySource{
		AliPay(1, "支付宝支付"), WeiXin(2, "微信支付");
		private int value;
		private String name;
		
		private PaySource(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static PaySource getEnum(int value) {
			for (PaySource p : PaySource.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	
	/**支付状态*/
	public enum PayStatus{
		PAYED(1, "已付款"), WAIT(2, "待付款");
		private int value;
		private String name;
		
		private PayStatus(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static PayStatus getEnum(int value) {
			for (PayStatus p : PayStatus.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	
	/**素材类型*/
	public enum MaterialType{
		VIDEO(1, "视频"), MUSIC(2, "音乐");
		private int value;
		private String name;
		
		private MaterialType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
		
		public static MaterialType getEnum(int value) {
			for (MaterialType p : MaterialType.values()) {
				if (p.getValue() == value) {
					return p;
				}
			}
			return null;
		}
	}
	
	
	
}
