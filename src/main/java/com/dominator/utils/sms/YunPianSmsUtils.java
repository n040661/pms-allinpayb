package com.dominator.utils.sms;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.network.HttpPoster;
import com.dominator.utils.system.Constants;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dominator.utils.system.RedisTimeConstant.*;

@Service
@Slf4j
public class YunPianSmsUtils {

    private static final String KEY = "CheckMessage";

    private static final String TimeKey = "CheckMessage:Time";

    private static RedisUtil jedis = RedisUtil.getRu();
    /**
     * 验证短信验证码
     */
    public static void checkcode(Dto dto) throws ApiException{
        String userName = dto.getString("userName");
        String usercode = dto.getString("code");
        Map<String, String> map = new HashMap<String, String>();
        //获取redis中的用户名对应的数据
        String cacheCode = jedis.get(KEY+userName);
        long remain_time = jedis.ttl(KEY+userName);
        //表示key不存在
        if(remain_time==-2 || StringUtils.isEmpty(usercode) || remain_time == -1)
            throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);

        boolean flag = Day - remain_time - HalfHour >0;
        if(flag){
            if (cacheCode.equals(usercode)) {//是否用户填入的验证码和缓存中的验证码一致
                //表示短信验证码过期
                throw new ApiException(Constants.CODE_CHECK_PASS_ERROR, Constants.MSG_CHECK_PASS_ERROR);
            }else{
                throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
            }
        }else{
            if (!cacheCode.equals(usercode)) {//是否用户填入的验证码和缓存中的验证码一致
                throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
            }
        }
    }


    /**
     * 发送短信验证码
     *
     * @throws ApiException
     */

    public static void sendYPSms(String phone) throws ApiException {

        //检查手机号是否符合格式
        if (!ApiUtils.isMobile(phone))
            throw new ApiException(Constants.CODE_SEND_PHONE_ERROR, Constants.MSG_SEND_PHONE_ERROR);

        //获取缓存中的上次访问时间
        String time0 = jedis.get(TimeKey+phone);

        long now = System.currentTimeMillis();
        if (time0 != null && !time0.equals("nil")){//表示redis中有值
            long lasttime = Long.parseLong(time0);
            if (now - lasttime < 60000) {//上次访问时间跟这次访问时间要大于60s
                throw new ApiException(Constants.CODE_SEND_LIMIT, Constants.MSG_SEND_LIMIT_ERROR);
            }
        }

        String text = ApiUtils.CreateRandom(4);
        //String text = "1111";
        //将随机生成的6个数放入到redis中
        jedis.setex(KEY + phone, text, Day);
        //初始化client,apikey作为所有请求的默认值(可以为空)
        YunpianClient clnt = new YunpianClient(
                SysConfig.ApiKey).init();
        /*Dto dto1 = new HashDto();
        dto1.put("code",text);*/
        String tpl_content = SysConfig.YZM;
        String value = tpl_content.replace("#code#",text);
        //String value = getcontent(dto1, tpl_id);
        //修改账户信息API
        Map<String, String> param = clnt.newParam(2);
        param.put(YunpianClient.MOBILE, phone);
        param.put(YunpianClient.TEXT, value);
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
        log.info("返回码描述:" + r.getMsg());
        String time = String.valueOf(System.currentTimeMillis());
        String code = r.getCode().toString();
        if (code.equals("0")){
            jedis.setex(TimeKey+phone, time, Minute);
        }
        //最后释放client
        clnt.close();
        if (code.equals("0")){//表示验证码短信发送成功
            log.info("验证码短信发送成功");
        } else{
            throw new ApiException(Constants.CODE_SEND_MSG_ERROR, Constants.MSG_SEND_MSG_ERROR);
        }
    }

    /**
     * 发送通知短信
     *
     * @throws ApiException
     */

    public static void sendNoticeSms(Dto dto) throws ApiException {
        System.out.println("YUNPIAN发送短信开始");
        String mobile = dto.getString("phone_num");
        byte[] bytes = null;
        switch (dto.getInteger("stype")) {
            case 1:
                bytes = SysConfig.ZDNOT.getBytes();
                break;
            case 2:
                bytes = SysConfig.ZDYES.getBytes();
                break;
            case 3:
                bytes = SysConfig.BXNOT.getBytes();
                break;
            case 4:
                bytes = SysConfig.BXRDOING.getBytes();
                break;
            case 5:
                bytes = SysConfig.BXSDOING.getBytes();
                break;
            case 6:
                bytes = SysConfig.BXYES.getBytes();
                break;
            case 7:
                bytes = SysConfig.FKYES.getBytes();
                break;
            case 8:
                bytes = SysConfig.FKGQ.getBytes();
                break;
            case 9:
                bytes = SysConfig.BEFK.getBytes();
                break;
            case 10:
                bytes = SysConfig.OPEN.getBytes();
                break;
            case 11:
                bytes = SysConfig.CONSULT.getBytes();
                break;
        }
        YunpianClient clnt = new YunpianClient(
                SysConfig.ApiKey).init();
        //String sendSms = sendSms(ApiKey);
        String value = getcontent(dto,bytes);
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
        //最后释放client
        clnt.close();
    }

    /**
     * 根据传入的参数生成不同的短信信息
     * @param dto
     * @param
     * @return
     */
    public static String getcontent(Dto dto, byte[] bytes)throws ApiException {
        String value = "";
        String str = "";
        try {
            if (StringUtils.isNotEmpty(SysConfig.ApiKey)) {
                str =  new String(bytes, "utf-8");
                /*if(tpl_id.equals("ZDNOT") || tpl_id.equals("ZDYES")){
                }else{
                    String sendSms = getTpl(ApiKey,tpl_id);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(sendSms);
                    JsonNode readTree = mapper.readTree(node.get("template").toString());
                    str = readTree.get("tpl_content").toString();
                }*/
                for(String s : dto.keySet()){
                    String s0 = "#"+s+"#";
                    String s1 =      dto.getString(s);
                    str = str.replace("#"+s+"#",dto.getString(s));
                }
               // value = str.replace("#month#", month).replace("#money#",money);
                /*if(tpl_id.equals("ZDNOT") || tpl_id.equals("ZDYES")){
                    value = str;
                }else{
                    value = str.substring(1, str.length() - 1);
                }*/
                value = str;
            }
        } catch (Exception e) {
            throw new ApiException(Constants.CODE_SMS_SEND_ERROR, Constants.MSG_SMS_SEND_ERROR);
        }
        return value;
    }

    /**
     * 根据用户标识和用户唯一id来获取短信模版
     * @param apikey
     * @return
     * @throws IOException
     */
    public static String getTpl(String apikey,String tpl_id) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("tpl_id", tpl_id);
        String rel = "";
        try {
             rel = HttpPoster.post("https://sms.yunpian.com/v1/tpl/get.json", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  rel;
    }


   public static void main(String[] args) {
       jedis.setex(KEY + "17621501628", "123456", Month);

   }
   
}
