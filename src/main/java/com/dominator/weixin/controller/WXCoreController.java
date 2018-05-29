package com.dominator.weixin.controller;

import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.PreAuthCodeService;
import com.dominator.weixin.service.WXCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/api/v1/weixin")
@Slf4j
public class WXCoreController {

    @Autowired
    private PreAuthCodeService preAuthCodeService;

    @Autowired
    private WXCoreService wxCoreService;


    /**
     * 处理微信的回调信息
     * 开发者先验证消息体签名的正确性，验证通过后，
     * 再对消息体进行解密,返回解密后的原文，并保存微信的回调信息
     *
     * @param timeStamp    时间戳
     * @param nonce        随机串
     * @param msgSignature 消息体签名，用于验证消息体的正确性
     * @param encryptType  加密类型，为aes
     * @param postData     密文，对应POST请求的数据
     */
    @RequestMapping(value = "/ticket")
    public @ResponseBody
    String getComponentVerifyTicket(@RequestParam(value = "timestamp", required = false) String timeStamp,
                                    @RequestParam(value = "nonce", required = false) String nonce,
                                    @RequestParam(value = "msg_signature", required = false) String msgSignature,
                                    @RequestParam(value = "encrypt_type", required = false) String encryptType,
                                    @RequestBody String postData) throws Exception {
        try {
            return wxCoreService.getComponentVerifyTicket(timeStamp, nonce, msgSignature, encryptType, postData);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR).toString();
        }
    }

    /**
     * 微信回调
     *
     * @param $APPID$ 微信公众号
     */
    @RequestMapping("/callbackMsg/{$APPID$}")
    public String callbackMsg(@PathVariable String $APPID$) {
        log.info("【callbackMsg】" + $APPID$);
        return "SUCCESS";
    }

    /**
     * 调起微信第三方授权界面
     *
     * @param compAppId 第三方平台appid（微信开放平台）
     */
    @GetMapping("/grant")
    public ModelAndView grant(@RequestParam String compAppId) throws PMSaaSException {
        //获取预授权码
        String preAuthCode = preAuthCodeService.getPreAuthCode(compAppId);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("component_appid", compAppId);
        modelMap.put("pre_auth_code", preAuthCode);
        modelMap.put("redirect_uri", WXConfig.GRANT_DOMAIN_NAME);
        return new ModelAndView("grant", modelMap);
    }

}
