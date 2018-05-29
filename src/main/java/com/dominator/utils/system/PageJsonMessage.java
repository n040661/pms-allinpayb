package com.dominator.utils.system;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应json数据用的公共类
 * 
 * @author Administrator
 * 
 */
public class PageJsonMessage {
	String code;
	String message;
	Map<String, Object> data = new HashMap<String, Object>();

	public PageJsonMessage() {
	}

	public PageJsonMessage(String _code, String _message) {
		this.code = _code;
		this.message = _message;
	}

	public PageJsonMessage(String _code, String _message, Map<String, Object> _data) {
		this.code = _code;
		this.message = _message;
		this.data = _data;
	}

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

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void putData(String key, Object value) {
		this.data.put(key, value);
	}
}
