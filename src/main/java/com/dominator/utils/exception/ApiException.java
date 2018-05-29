package com.dominator.utils.exception;

import com.dominator.enums.ReqEnums;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	protected String iErrCode = "";
	protected String iErrMessage = "";

	/**
	 * 直接返回异常信息
	 *
	 * @param aMessage
	 *            异常信息
	 */
	public ApiException(String aMessage) {
		super(aMessage);
		this.iErrMessage = aMessage.trim();
	}

	/**
	 * 异常信息和编号
	 * 
	 * @param aCode
	 * @param aMessage
	 */
	public ApiException(String aCode, String aMessage) {
		super(aMessage);
		this.iErrCode = aCode.trim();
		this.iErrMessage = aMessage.trim();
	}

	public ApiException(String aCode, String aMessage, Throwable throwable) {
		super(throwable);
		this.iErrCode = aCode;
		this.iErrMessage = aMessage;
	}

	public String getCode() {
		return this.iErrCode;
	}

	/**
	 * 业务异常提示信息
	 */
	@Override
	public String getMessage() {
		return this.iErrMessage;
	}

	/**
	 * 系统级异常信息
	 * 
	 * @return
	 */
	public String getSysMessage() {
		return this.getMessage();
	}

	/**
	 * 枚举异常
	 * @param reqEnums
	 */
	public ApiException(ReqEnums reqEnums) {
		super(reqEnums.getMsg());
		this.iErrCode = reqEnums.getCode().trim();
		this.iErrMessage = reqEnums.getMsg().trim();
	}
}
