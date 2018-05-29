package com.dominator.redis.async.handler;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.TVisitor;
import com.dominator.mapper.TVisitorMapper;
import com.dominator.redis.async.EventHandler;
import com.dominator.redis.async.EventModel;
import com.dominator.redis.async.EventType;
import com.dominator.redis.util.RedisKeyUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.sms.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ZAXMsgHandler implements EventHandler {
    @Autowired
    private TVisitorMapper tVisitorMapper;

    private static RedisUtil ru = RedisUtil.getRu();
    /**
     * 中安消 发送短信
     * @param
     * @throws ApiException
     */
    @Override
    @Transactional
    public void doHandler(EventModel eventModel) {

        String uuid = eventModel.getExt("ZAXMsg");
        Dto dto = (Dto) ru.getObject(uuid);
        ru.del(uuid);
        if(dto!=null) {
            Dto zaxDto = new HashDto();
            zaxDto.put("stype", 10);
            //访客手机号
            zaxDto.put("phone", dto.getString("username"));
            //邀请人手机号
            zaxDto.put("phone_num", dto.getString("invitePhone"));
            //访客姓名
            zaxDto.put("username", dto.getString("visitorName"));
            try {
                //修改访客状态
                TVisitor tVisitor = new TVisitor();
                tVisitor.setId(uuid);
                tVisitor.setStatus("1");
                tVisitor.setModifyTime(new Date());
                tVisitor.setModifyId("system");
                int i = tVisitorMapper.updateByPrimaryKey(tVisitor);
                //发送短信给邀请人告知被邀请人已到
                SmsUtils.sendNoticeSms(zaxDto);
            } catch (ApiException e) {
                e.printStackTrace();
                //序列化
                String json = com.alibaba.fastjson.JSONObject.toJSONString(eventModel);
                //产生key
                String key = RedisKeyUtil.getEventQueueKey();
                //重新放入队列中放入工作队列中
                ru.lpush(key, json);
            }
        }

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.ZAXMSG);
    }
}
