package com.dominator.weixin.service;

import com.dominator.entity.TblWxopenAuthorizationCode;
import com.dominator.weixin.exception.PMSaaSException;
import org.json.JSONArray;

import java.util.Map;


public interface AuthorizationCodeService {

    StringBuffer jsonArrayToString(JSONArray jsonArray);

    TblWxopenAuthorizationCode getAuthorizationCodeByInfoType(Map<String, String> decryptMsgMap, String InfoType);

    TblWxopenAuthorizationCode getAuthorizedCodeByAppId(String authAppId, boolean refreshAuthCode) throws PMSaaSException;

//    void insert(TblWxopenAuthorizationCode authorizedResultInfo);

    void update(TblWxopenAuthorizationCode authorizationCode) throws PMSaaSException;

}
