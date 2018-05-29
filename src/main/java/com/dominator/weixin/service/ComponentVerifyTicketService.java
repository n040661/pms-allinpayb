package com.dominator.weixin.service;

import com.dominator.entity.TblWxopenComponentVerifyTicket;
import com.dominator.weixin.exception.PMSaaSException;

import java.util.Map;

/**
 * Created by zhangsuliang on 2017/8/17.
 */

public interface ComponentVerifyTicketService {

     TblWxopenComponentVerifyTicket getComponentVerifyTicket(String compAppid) throws PMSaaSException;

//     ComponentVerifyTicket getComponentVerifyTicket() throws PMSaaSException;

     void saveWeiXinCallbackInfo(Map<String, String> decryptMsgMap) throws PMSaaSException;

     void saveOrupdateAuthorizaitionInfo(Map<String, String> decryptMsgMap) throws PMSaaSException;
//
//     JSONObject getetAuthorizerOption(String appId, String authAppId, String optionName) throws PMSaaSException;
//
//     JSONObject setAuthorizerOption(String appId, String authAppId, String optionName, String optionValue) throws PMSaaSException;
//
//     JSONObject getAuthorizerInfo(String appId, String authAppId) throws Exception;
}