package com.dominator.utils.sms;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.utils.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证码工具类
 *
 * @author Administrator
 */
public class SmsUtils {


    private static Logger log = LoggerFactory.getLogger(SmsUtils.class);


    public static void sendSms(String phone) throws ApiException {

        log.info("--发送短信---");
        //LongJuSmsUtils.sendSms(dto);
        /**示远短信通道**/


        //LongJuSmsUtils.sendSYSms(dto);
        if (SysConfig.isSendMsg)
            YunPianSmsUtils.sendYPSms(phone);
        else
            log.info("跳过短信");
    }

    public static void checkSms(Dto dto) throws ApiException {
        log.info("--验证短信--");
        if (SysConfig.isSendMsg)
            YunPianSmsUtils.checkcode(dto);
        log.info("跳过短信");
    }

    public static void sendNoticeSms(Dto dto) throws ApiException {
        log.info("--发送通知短信--");
        YunPianSmsUtils.sendNoticeSms(dto);
    }

}
