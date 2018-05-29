package com.dominator.redis.async.handler;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.TUser;
import com.dominator.mapper.TUserMapper;
import com.dominator.redis.async.EventHandler;
import com.dominator.redis.async.EventModel;
import com.dominator.redis.async.EventType;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.sms.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.dominator.utils.sms.ShortUrlUtil.HttpUtil.getShortUrl;

@Component
@Slf4j
public class MsgHandler implements EventHandler {
    @Autowired
    private TUserMapper tUserMapper;

    /*@Autowired
    private SysConfig sysConfig;*/

    private static RedisUtil ru = RedisUtil.getRu();
    /**
     * 被邀请访客通知
     * @param
     * @throws ApiException
     */
    @Override
    public void doHandler(EventModel eventModel) throws ApiException {

        String uuid = eventModel.getExt("msg");
        Dto dto = (Dto) ru.getObject(uuid);
        ru.del(uuid);
        //发送验证通过通知短信给邀请人
        Dto companyDto = new HashDto();
        companyDto.put("stype","9");
        companyDto.put("company",dto.getString("company"));
        companyDto.put("day",dto.getString("date"));
        companyDto.put("address",dto.getString("address"));
        TUser userPO = tUserMapper.selectByPrimaryKey(dto.getString("user_id"));
        if(userPO!=null){
            companyDto.put("username",userPO.getNickName());
        }
        companyDto.put("phone_num",dto.getString("visitor_phone"));
        /*JSONObject json = new JSONObject();
        json.accumulate("id",dto.getString("id"));
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }*/
//        String longUrl = "https://www.baidu.com";
        String longUrl = SysConfig.VISITOR_DETAIL_URL+"visitorId="+dto.getString("id")+"&appId="+dto.getString("app_id");
        String shorturl = getShortUrl(longUrl);
        log.info("**********报修通知已经处理生成的短链接***********"+longUrl);
        companyDto.put("url",shorturl);
        SmsUtils.sendNoticeSms(companyDto);

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.MSG);
    }
}
