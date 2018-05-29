package com.dominator.weixin.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.WebPageAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/weixin/webpage")
@Slf4j
public class WebPageAuthController {

    @Autowired
    private WebPageAuthService webPageAuthService;

    /**
     * 获取微信用户信息（第三方）
     *
     * @param paramStr code 微信回调code
     *                 appid 公众号appid
     *                 component_appid  第三方appid
     * @return subscribe, openid, nickname, sex, language, city,
     * province,country,headimgurl,subscribe_time,
     * unionid,remark,groupid,tagid\_list
     */
    @PostMapping(value = "/getWeiXinUserInfo")
    public ApiMessage getWeiXinUserInfo(@RequestParam String paramStr){
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            log.info("getWeiXinUserInfo获取微信用户信息，携带参数为：{}", dto);
            return webPageAuthService.getWeiXinUserInfo(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 获取微信用户信息（公众号）
     *
     * @param paramStr code 微信回调code
     *                 appid 公众号appid
     */
    @PostMapping(value = "/getWXUser4Public")
    public ApiMessage getWeiXinUserInfo2(@RequestParam String paramStr) throws PMSaaSException {
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            log.info("getWeiXinUserInfo获取微信用户信息，携带参数为：{}", dto);
            return webPageAuthService.getWeiXinUserInfo2(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }


}