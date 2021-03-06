package com.dominator.redis.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class JSONUtil {

	public static String getJSONString(int code){

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		return jsonObject.toString();
	}

	public static String getJSONString(int code, String msg){

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		jsonObject.put("msg", msg);
		return jsonObject.toString();
	}

	public static String getJSONString(int code, Map<String, Object> map){

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		for(Map.Entry<String, Object> m : map.entrySet()){

			jsonObject.put(m.getKey(), m.getValue());
		}
		return jsonObject.toString();
	}
}
