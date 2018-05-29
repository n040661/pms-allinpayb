package com.dominator.redis.async;


import com.alibaba.fastjson.JSONObject;
import com.dominator.redis.util.RedisKeyUtil;
import com.dominator.utils.dao.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
	private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

	/*@Autowired
	JedisAdapter jedisAdapter;*/
	private static RedisUtil ru = RedisUtil.getRu();

	public boolean fireEvent(EventModel eventModel){
		try {
			//序列化
			String json = JSONObject.toJSONString(eventModel);
			//产生key
			String key = RedisKeyUtil.getEventQueueKey();
			//放入工作队列中
			ru.lpush(key, json);
			return true;
		}catch (Exception e){
			logger.error("EventProducer fireEvent 异常 ：" + e.getMessage());
			return false;
		}

	}
}
