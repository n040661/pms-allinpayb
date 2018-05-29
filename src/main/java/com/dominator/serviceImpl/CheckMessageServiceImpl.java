package com.dominator.serviceImpl;

import com.dominator.AAAconfig.SysConfig;
import com.dominator.service.CheckMessageService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.system.Constants;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dominator.utils.system.RedisTimeConstant.HalfHour;
import static com.dominator.utils.system.RedisTimeConstant.Minute;

@Service
public class CheckMessageServiceImpl implements CheckMessageService {

    @Autowired
    private GetTplContent tplContent;

    private static final String KEY= "CheckMessage";

    private static final String TimeKey= "CheckMessage:Time";

    private static Logger log = LoggerFactory.getLogger( CheckMessageServiceImpl.class);

    @Override
    public ApiMessage checkcode(String usercode, String phone) {
        Map<String,String> map = new HashMap<String,String>();
        ApiMessage msg = new ApiMessage(Constants.REQ_SUCCESS, Constants.MSG_SUCCESS);
       // Dto resDto = Dtos.newDto();
        //resDto.put("resp","测试返回内容"+new Date());
        /*try {*/
            /*String result = "1";
            String message = "短信验证码错误";*/
           // resDto.put("message","短信验证码错误");
        msg.setData("短信验证码错误");
            //
            RedisUtil jedis = RedisUtil.getRu();
            //Jedis jedis = jedispool.getJedis();
            String code = jedis.get(KEY + phone);
            if(jedis.exists(KEY + phone)){//redis中有值ֵ
                if (StringUtils.isNotEmpty(code) ) {
                    if (usercode.equals(code)) {//是否用户填入的验证码和缓存中的验证码一致
                        /*result = "0";
                        message = "验证码正确，请继续";*/
                        //resDto.put("message","验证码正确，请继续");
                        msg.setData("验证码正确，请继续");
                    }
                }
            }else{//表示redis中没有值，即已经过期
                /*result = "2";
                message = "短信验证码已过期";*/
                //resDto.put("message","短信验证码已过期");
                msg.setData("短信验证码已过期");
            }
            /*map.put("code", result);
            map.put("message", message);*/
           // msg.setData(Des3Utils.encResponse(resDto));
       // msg.setData(resDto);
        /*} catch (Exception e) {
            // TODO: handle exception
            log.error("checkmobile have error is "+e);
            *//*map.put("code", InterfaceConstants.ERROR_CODE_001);
            map.put("message", InterfaceConstants.ERROR_CODE_MESSAGE);*//*
        }*/
        return msg;
    }

    @Override
    public ApiMessage sendmobile(String mobile) {
        Map<String, String> map = new HashMap<String, String>();
        ApiMessage msg = new ApiMessage(Constants.REQ_SUCCESS, Constants.MSG_SUCCESS);

            String phone = mobile;
            //检查手机号是否符合格式
            if (checkphone(phone).equals("1")) {//表示手机号码不匹配
                /*map.put("code", "1");
                map.put("message", "手机号不正确");*/
                msg.setData("手机号不正确");
                return msg;
            }
            //获取缓存中的上次访问时间
            RedisUtil jedis = RedisUtil.getRu();
            //Jedis jedis = jedispool.getJedis();
            String time0 = jedis.get(TimeKey);
            long now = System.currentTimeMillis();
            if (time0 != null && !time0.equals("nil")) {//表示redis中有值
                long lasttime = Long.parseLong(time0);
                if (now - lasttime < 60000) {//上次访问时间跟这次访问时间要大于60s
                    /*map.put("code", "2");
                    map.put("message", "60s内不可多次访问");*/
                    msg.setMessage("60s内不可多次访问");
                    return msg;
                }
            }
            String text = CreateRandom(phone);
            //初始化client,apikey作为所有请求的默认值(可以为空)
            YunpianClient clnt = new YunpianClient(
                    SysConfig.ApiKey).init();
            //String sendSms = sendSms(ApiKey);
            String value = tplContent.getcontent(SysConfig.ApiKey, text);
            //修改账户信息API
            Map<String, String> param = clnt.newParam(2);
            param.put(YunpianClient.MOBILE, mobile);
            param.put(YunpianClient.TEXT, value);
            Result<SmsSingleSend> r = clnt.sms().single_send(param);
            //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
            //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
            log.info("返回码描述:" + r.getMsg());
            String time = String.valueOf(System.currentTimeMillis());
            String code = r.getCode().toString();
            if (code.equals("0")) {
                jedis.setex(TimeKey, time,Minute);
                //jedis.expire(TimeKey, Minute);
            }
            //最后释放client
            clnt.close();
            if (code.equals("0")) {//表示验证码短信发送成功
                /*map.put("code", "0");
                map.put("message", "验证码短信发送成功");*/
                msg.setData("验证码短信发送成功");
            } else {
                /*map.put("code", "3");
                map.put("message", "验证码短信发送失败");*/
                msg.setData("验证码短信发送失败");
            }

        return msg;
    }



    //检查手机号是否正确
    public String checkphone(String phone){
        String Message = "0";
        String re = "^1[3,4,5,7,8]\\d{9}$";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {//表示手机号码不匹配
            Message =  "1";
        }
        return Message;


    }

    //设置6个随机组成的数
    public  String CreateRandom(String phone){
        //随机数
        Random r = new Random();
        String R = "";
        for (int i = 0; i < 6; i++) {
            R=R+r.nextInt(10);
        }
        //将随机生成的6个数放入到redis中
        RedisUtil jedis = RedisUtil.getRu();

        //Jedis jedis = jedispool.getJedis();
        jedis.setex(KEY+phone, R,HalfHour);
        // jedis.expire(KEY+phone, HalfHour);
        return R;
    }
}
