package com.dominator.redis.async;

import com.alibaba.fastjson.JSON;
import com.dominator.redis.util.RedisKeyUtil;
import com.dominator.utils.dao.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

	private static RedisUtil ru = RedisUtil.getRu();


	//public static Thread thread;
	//可扩容的线程池
	ExecutorService executorService = Executors.newCachedThreadPool();
	private ApplicationContext applicationContext;
	//存储各种type事件对应的Handler处理类
	private Map<EventType, List<EventHandler>> config = new HashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		//获取所有实现EventHandler的类
		Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
		if (eventHandlerMap != null){
			for (Map.Entry<String, EventHandler> entry : eventHandlerMap.entrySet()){
				//遍历每个EventHandler，将这个EventHandler放到集合对应的type中
				EventHandler eventHandler = entry.getValue();
				//获取每个EventHandler所关注的事件类型
				List<EventType> eventTypes = eventHandler.getSupportEventType();

				for (EventType type : eventTypes){
					//初始化的时候config为空
					if (!config.containsKey(type)){
						config.put(type, new ArrayList<EventHandler>());
					}
					//将Handler放入指定type中
					config.get(type).add(eventHandler);

				}


			}
		}

		//启动线程从工作队列中取出事件进行处理
		//thread = new Thread(new Runnable() {
		executorService.execute(new Runnable() {
		@Override
			public void run() {

				while (true){
					String key = RedisKeyUtil.getEventQueueKey();
					//获取存储的(经过序列化的)事件event
					List<String> events = ru.brpop(0, key);

					if (events != null && events.size() > 0 ) {
						for (String jsonEvent : events) {
							if (jsonEvent.equals(key)) {
								continue;
							}
							EventModel eventModel = JSON.parseObject(jsonEvent, EventModel.class);

							if (!config.containsKey(eventModel.getEventType())) {
								logger.error("不能识别的事件！");
								continue;
							}
							//获取关注过该事件eventModel的handler,进行处理
							for (EventHandler handler : config.get(eventModel.getEventType())) {
								handler.doHandler(eventModel);
							}

						}


					}



				}
			}
		});
		//thread.start();
		logger.info("异步线程启动成功");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
