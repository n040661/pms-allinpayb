package com.dominator.redis.async;

import java.util.List;

public interface EventHandler {
	//处理事件
	void doHandler(EventModel eventModel);
	//获取关注事件的类型
	List<EventType> getSupportEventType();

}
