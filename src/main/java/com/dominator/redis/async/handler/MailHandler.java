package com.dominator.redis.async.handler;

import com.dominator.redis.async.EventHandler;
import com.dominator.redis.async.EventModel;
import com.dominator.redis.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//import com.dominator.redis.util.MailSender;

@Component
public class MailHandler implements EventHandler {
	private static final Logger logger = LoggerFactory.getLogger(MailHandler.class);

	/*@Autowired
	MailSender mailSender;*/

	@Override
	public void doHandler(EventModel eventModel) {
		try {
			//mailSender.sendMail(eventModel.getExt("email"));
		}catch (Exception e){
			logger.error("邮件发送异常：" + e.getMessage());
		}
	}

	@Override
	public List<EventType> getSupportEventType() {
		return Arrays.asList(EventType.MAIL);
	}
}
