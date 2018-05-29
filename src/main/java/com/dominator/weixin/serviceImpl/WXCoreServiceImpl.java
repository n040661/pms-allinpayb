package com.dominator.weixin.serviceImpl;

import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.service.ComponentVerifyTicketService;
import com.dominator.weixin.service.WXCoreService;
import com.dominator.weixin.util.WxUtils;
import com.dominator.weixin.util.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class WXCoreServiceImpl implements WXCoreService {

    @Autowired
    private ComponentVerifyTicketService componentVerifyTicketService;

    @Override
    public String getComponentVerifyTicket(String timeStamp, String nonce, String msgSignature, String encryptType, String postData) throws Exception {
        log.info("【微信回调密文（每隔十分钟发一次）：timestamp:" + timeStamp + "msg_signature:" + msgSignature + "nonce:" + nonce + "encrypt_type:" + encryptType + "postData:" + postData+"】");
        Map<String, String> map = WxUtils.xmlToMap(postData);
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(WXConfig.WX_TOKEN, WXConfig.ENCODING_AESKEY, map.get("AppId"));
        String decryptMsg = wxBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, postData);
        log.info("【微信回调明文 ："+decryptMsg+"】");
        componentVerifyTicketService.saveWeiXinCallbackInfo(WxUtils.xmlToMap(decryptMsg));
        return "success";
    }
}
