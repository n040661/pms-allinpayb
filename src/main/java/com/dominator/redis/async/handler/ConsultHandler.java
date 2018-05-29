package com.dominator.redis.async.handler;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.TVisitor;
import com.dominator.redis.async.EventHandler;
import com.dominator.redis.async.EventModel;
import com.dominator.redis.async.EventType;
import com.dominator.redis.util.RedisKeyUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.sms.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class ConsultHandler implements EventHandler {
	private static final Logger logger = LoggerFactory.getLogger(ConsultHandler.class);

	private RedisUtil ru = RedisUtil.getRu();

	@Override
	public void doHandler(EventModel eventModel) {
		try {

			String uuid = eventModel.getExt("consult");
			Dto dto = (Dto) ru.getObject(uuid);
			ru.del(uuid);
			//发送企业咨询通知短信给到企业咨询的联系人
			Dto consultDto = new HashDto();
			consultDto.put("stype", 11);
			consultDto.put("name", dto.getString("name"));
			consultDto.put("userName", dto.getString("userName"));
			consultDto.put("phone_num", dto.getString("phoneNum"));
			SmsUtils.sendNoticeSms(consultDto);

		}catch (Exception e){
			logger.error("邮件发送异常：" + e.getMessage());
			String json = com.alibaba.fastjson.JSONObject.toJSONString(eventModel);
			//产生key
			String key = RedisKeyUtil.getEventQueueKey();
			//重新放入队列中放入工作队列中
			ru.lpush(key, json);
		}
	}

	@Override
	public List<EventType> getSupportEventType() {
		return Arrays.asList(EventType.CONSULT);
	}
}
