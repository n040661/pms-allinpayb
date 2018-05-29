package com.dominator.utils.system;

import java.math.BigDecimal;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class Constants {
	public static final String DES3_PASSWORD = "1qaz2wsx"; // aes加密密码
	public static final String DES3_ENCODE = "utf-8";

	/**
	 * token校验
	 */
	public static final boolean TOKEN_CHECK = true;

	public static final int DAY = 60*60*24;

	/**
	 * token过期时间 （单位秒）long
	 */
	public static final long TOKEN_TIMES = 60*60*24*5 ;
	
	/**
	 * token过期时间 （单位秒）int
	 */
	public static final int TOKEN_TIMES_INT = 60*60*24*30 ;
	
	/**
	 * SMS过期时间 （单位秒）int
	 */
	public static final int SMS_TIMES_INT = 60*5 ;

	/**
	 * 一天
	 */
	public static final int TIMES_DAY = 60*60*24 ;

	/**
	 * 一个月
	 */
	public static final int TIMES_MONTH = 60*60*24*30 ;
	
	
	/**
	 * 图像验证码过期时间 （单位秒）int
	 */
	public static final int PIC_TIMES_INT = 60*2 ;
	


	/**
	 * 系统异常400
	 */
	public static final String SYS_ERROR = "400";
	/**
	 * 请求成功200
	 */
	public static final String REQ_SUCCESS = "200";
	/**
	 * 请求参数缺少102
	 */
	public static final String REQ_PARAM_MISS = "102";
	/**
	 * 请求过期103
	 */
	public static final String REQ_TOKEN_FALSE = "103";



	/**
	 * 请求参数错误105
	 */
	public static final String REQ_PARAM_ERROR = "105";

	/**
	 * 请求结果为空
	 */
	public static final String REQ_RESULT_NULL = "106";
	
	/**
	 * 订单生成失败
	 */
	public static final String REQ_ORDER_ERROR= "107";
	/**
	 * 交易失败
	 */
	public static final String REQ_TRADE_ERROR= "109";
	
	/**
	 * 更新失败
	 */
	public static final String REQ_UPDATE_ERROR= "110";


	/**
	 * 登陆成功
	 */
	public static final String REQ_LOGIN_SUCCESS="登陆成功";

	/**
	 * 请求成功
	 */
	public static final String MSG_SUCCESS = "请求成功";
	/**
	 * 请求失败
	 */
	public static final String MSG_FAILED = "请求失败";
	/**
	 * 请求参数缺少
	 */
	public static final String MSG_PARAM_MISS = "请求参数缺少";
	
	/**
	 * 请求参数错误
	 */
	public static final String MSG_PARAM_ERROR = "请求参数错误";
	/**
	 * 系统异常
	 */
	public static final String MSG_EXCEPTION = "系统异常";
	/**
	 * 没有结果
	 */
	public static final String MSG_NULL = "没有结果";
	/**
	 * 订单生成失败
	 */
	public static final String ORDER_FAILED = "订单生成失败";

	/**
	 * 交易失败
	 */
	public static final String MSG_TRADE_ERROR= "交易失败";
	
	/**
	 * 更新失败
	 */
	public static final String MSG_UPDATE_ERROR= "更新失败";
	/***************************************************************************************************************************************/

	/**
	 * 银生宝接口地址
	 */
	public static final String YSB_PAY_URL = "http://180.166.114.152:18086/auth/page/authPay";
	/**
	 * 前端回调地址
	 */
	public static final String YSB_RESPONSE_URL = "http://192.168.1.100";
	/**
	 * 后台回调地址
	 */
	public static final String YSB_BACK_RESPONSE_URL = "http://192.168.1.100";

	/**
	 * 商户号
	 */
	public static final String YSB_MERCHANT_ACCOUNT_ID = "2120160704163009001";

	/**
	 * 银生宝key
	 */
	public static final String YSB_PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMBC/mknPjV9Wnr6"
			+ "2VRQPcG1/VRD+gkuZMr5p0/MndOAkKIA/Vi7QRhm8aDh1RSs0CKhYO+9adlKlORH"
			+ "0o29fHE8WcZX5kCtV6bSxmbRmF4CsDyahB8ATacz1sZJcdjsxruom9flnJEOr1MN"
			+ "gy8iRMOYXKtED6r5Vqo+TPsNqlI7AgMBAAECgYALCs351Bsqj0yHC6k3wnQJkB5A"
			+ "nLbbmCKJ1dTaLHJUF2o3I79pPTf74s2148KNMW+yzXn3yvUtWa87fTSGl39yF/hz"
			+ "2sSZlfHv5HRUp8NwECWSvcZgpGnh3lXegt3L97RicIVZ7UGbOLmAjoxfRJ/o9Sf0"
			+ "JLEk7EpFBOqzrqhC4QJBAOait5rd46RyeaYcSjJAheN0WD1hp/owYutWPylYaPut"
			+ "D/rezaOCy3swE5DrfzbmIXuHDeAjtyxq56AQTV6Iss8CQQDVZ+cSbP+MVAxdnlaQ"
			+ "Ghm27VhNR5JPqx9E+7WYmNYhgFwtndwar14Jyj82EXFHpp6A4yEE42ivUoGnO9JR"
			+ "jLTVAkEAncDw60oYulPe54L0MTk3G2RqMtoIRrYwAx7EJyik7njqtEMPz2bYfdNd"
			+ "nsOmCUwR3Od80zI6yXP6KN7dvOqtuwJAFypCMQPX8ZmmBfuMFfRfWjYFYc4lGbTP"
			+ "niNcoQXmKJjQaLs8C0GCCboEho/6Jfb2ObNuACPXV0Czhj2+JNALSQJAMvKU2ita"
			+ "boyKy/MrayAtdplGQR3cHqDzSatRjbQgAoXehafI3c81yXYUS0UvMN8FcQ/GCR5Z" + "P40xdasxp1aRlg==";

	public static final String YSB_KEY = "abcde12345";



	/**
	 * 分享添加商币数量
	 * 
	 * @author Administrator
	 */
	public static final class shareCloudMoney {
		/**
		 * 微信朋友圈
		 */
		public static final Integer wxpyq = 2;
		/**
		 * 微信好友
		 */
		public static final Integer wxhy = 1;
		/**
		 * 新浪微博
		 */
		public static final Integer xlwb = 1;
		/**
		 * QQ好友
		 */
		public static final Integer qqhy = 1;
		/**
		 * qq空间
		 */
		public static final Integer qqkj = 1;
		/**
		 * 二维码（暂无）
		 */
		public static final Integer ewmfx = 1;
		/**
		 * 最大分享额度
		 */
		public static final Integer mtxe = 5;

	}

	/**
	 * ip注册限制数
	 */
	public static final int IP_REG_TIMES = 5;

	/**
	 * ip登录限制数
	 */
	public static final int IP_LOGIN_TIMES = 5;

	/**
	 * 
	 * 用户银行卡配置
	 *
	 */
	public static final class BANKCARD {
		public static final Integer USERCARDS = 5;
		public static final Integer MERCHANTCARDS = 5;

	}

	/**
	 * 身份认证apikey
	 */
	public static final String ID_API_KEY = "fc819689b1b0d522abfb7df77049d5e6";

	/**
	 * 身份认证url
	 */
	public static final String ID_URL = "http://api.id98.cn/api/idcard";

	public static final String PIC_URL = "";
	/**
	 * 注册时间分界
	 */
	public static final String REG_TIME_LINE ="2016-08-16 00:00:00";
	/**
	 * 
	 * 微信公众号支付
	 *
	 */
	public static final class WX {
		public static final String APP_ID  = "wx37dc608022d668fc";
		public static final String MCH_ID  = "1322674901";
		public static final String DEVICE_INFO  = "WEB";
		public static final String BODY  = "H5PAY";
		public static final String TRADE_TYPE  = "JSAPI";
		public static final String KEY="09689b0395ade8809c92587e64062b17";

	}

	/**
	 * 支付宝请求参数
	 */
	//支付宝appid
	public static final String AL_app_id = "2017011805188573";
	
	//支付宝服务器主动通知商户服务器里指定的页面http/https路径
	public static final String AL_notify_url = "https://www.jydsapp.com/jydsApi/api/v2/common/alOrderUI";
	
	//支付宝应用私钥
	public static final String AL_APP_PRIVATE_KEY_RSA = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJj1ayvhjjiT6Ng68uoLlvDewSwfV+xorBvCN387p2vNb8XTgG0lfReDd4IsYzxhdQHTUgt2YCvX1AXD5T66sXZCA3sM0joTjS72hgVmyJ2QZMwwVkPvcW1ZbHZtJZDgTYQ/I1F8uQa+F0n+iCe7cHKQXOOr9LAnlce/yZLOY4vzAgMBAAECgYEAmDk17TfOWFQrGHaiJXZfQ1wMlyKiV+5bykxg+sidd9W99PzmBLmYYnw9xVjzcbXzwltSfYHTeA6VPR+V1/XogSN9m7czIpu1ZAMHm6dEdxygi0/pa+O7W/HQijpe42ICX+Bd1+Y8JszbqIW7V1Ai8pgUrzBKuIPuEXUNavCpkDECQQDVOti3XUE9363M07XTZrmR/j+gAdS3Suqiaytksbn7dpfxAwu9EasA3PtAMEP3Kxs+IHB8ABLxJdKQ8G/nK6aPAkEAt6Oz1Bt3PeFQ/jFUsMfUpn0QPWH2AlM95240k//i6rBXGF6d88M44XZK8ElIzrnnkCEWWx2kuwH65r3RZyxWXQJAXSqQJFYnunwwOU3LpWm97U80x5nlMo0WRg8jb6TECV6A9vqIt3yvxfnIAfzUxXtdNOEBpPx3SkxnW5LiohQNewJBAJWV+33jgZYeW2Mzu9DkdgvIA9p8WXA7bkl+M8X6wlR5n/hx2igd9c9yFEj26R+7vyxgR12hH/ZU/pPGOzTXoOECQQCDxXoe43beEON4yLSTtOoQVspcn3295MHZA3hi3EKkfl1jogc66pcXqaWhMOho6NNoIaAlLfRXYPX4DFNoup8f"; 
	//支付宝公钥
	public static final String AL_ALIPAY_PUBLIC_KEY_SRA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB"; 
	

	/**
	 * 微信请求参数
	 */
	/**
	    * 预支付请求地址
	    */
	   public static final String  WX_PrepayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	   /**
	    * 查询订单地址
	    */
	   public static final String  WX_OrderUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

	   /**
	    * 关闭订单地址
	    */
	   public static final String  WX_CloseOrderUrl = "https://api.mch.weixin.qq.com/pay/closeorder";

	   /**
	    * 申请退款地址
	    */
	   public static final String  WX_RefundUrl = "https://api.mch.weixin.qq.com/secapi/pay/pay";

	   /**
	    * 查询退款地址
	    */
	   public static final String  WX_RefundQueryUrl = "https://api.mch.weixin.qq.com/pay/refundquery";

	   /**
	    * 下载账单地址
	    */
	   public static final String  WX_DownloadBillUrl = "https://api.mch.weixin.qq.com/pay/downloadbill";

	 
	   
	   
	   /**
	    * 简历返现比例
	    */
	   public static final BigDecimal  RESUME_RATE= new BigDecimal("0.2");
	   
	   
	   /**
	    * 简历返现邀请人比例
	    */
	   public static final BigDecimal  RESUME_INVITE_RATE= new BigDecimal("0.2");
	   
	   
	   /**
	    * 最低价
	    */
	   public static final BigDecimal LOWEST_PRICE = new BigDecimal("100");
	   
	   //微信回调地址
	   public static String WX_notify_url = "";
	   
	   /**
	     * 用户相关
	     * @author Administrator
	     *
	     */
	 	public static final class USER{
			
			public static final Integer FREE_COUNT=10;
			
		}


		/******************************** 短信相关的配置 ***************************************/
		public static String CODE_SEND_PHONE_ERROR = "1001";
		public static String MSG_SEND_PHONE_ERROR = "手机号不正确";

		public static String CODE_SEND_LIMIT = "1002";
		public static String MSG_SEND_LIMIT_ERROR = "60s内不可多次访问";

		public static String CODE_SEND_MSG_ERROR = "1003";
		public static String MSG_SEND_MSG_ERROR = "验证码短信发送失败";

	public static String CODE_CHECK_MSG_ERROR = "1004";
	public static String MSG_CHECK_MSG_ERROR = "短信验证码错误";

	public static String CODE_CHECK_PASS_ERROR = "1005";
	public static String MSG_CHECK_PASS_ERROR = "短信验证码过期";

	public static String CODE_SMS_SEND_ERROR = "1006";
	public static String MSG_SMS_SEND_ERROR = "短信发送失败";

	/******************************** 登录 ***************************************/
	public static String CODE_LOGIN_FAIL_ERROE = "1007";
	public static String MSG_LOGIN_FAIL_ERROE = "登录失败";

}
