package com.dominator.utils.api;

import com.dominFramework.core.utils.SystemUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.exception.ApiException;

import java.io.Serializable;

/**
 * 响应json数据用的公共类
 * 
 * @author Administrator
 * 
 */
public class ApiMessage implements Serializable{
	String code;
	String message;
	String data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ApiMessage() {
	}

	public ApiMessage(String _code, String _message) {
		this.code = _code;
		this.message = _message;
	}

	public ApiMessage(String _code, String _message, String _data) {
		this.code = _code;
		this.message = _message;
		this.data = _data;
	}

	public ApiMessage(ReqEnums reqEnums){
		this.code=reqEnums.getCode();;
		this.message=reqEnums.getMsg();
	}

	public ApiMessage(ApiException e){
		this.code=e.getCode();
		this.message=e.getMessage();
	}


	@Override
	public String toString() {

		StringBuilder sb =new StringBuilder();
		sb.append("{ code='" + code + "'"+", message='" + message + "'");
		if (SystemUtils.isNotEmpty(data)){
			sb.append(", data='" + data + "'");
		}
		sb.append("}");
		return sb.toString();
	}
}
