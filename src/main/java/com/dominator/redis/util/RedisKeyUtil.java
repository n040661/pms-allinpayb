package com.dominator.redis.util;

public class RedisKeyUtil {

	private static String EVENT_KEY = "ASYNC_EVENT_KEY";

	public static String getEventQueueKey(){
		return EVENT_KEY;
	}
}
