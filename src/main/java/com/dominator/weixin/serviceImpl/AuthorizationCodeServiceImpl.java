package com.dominator.weixin.serviceImpl;

import com.dominator.entity.TblWxopenAuthorizationCode;
import com.dominator.entity.TblWxopenAuthorizationCodeExample;
import com.dominator.mapper.TblWxopenAuthorizationCodeMapper;
import com.dominator.weixin.errorCode.PMSaaSErrorCodeType;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.AuthorizationCodeService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zhangsuliang on 2017/8/28.
 */

@Service
public class AuthorizationCodeServiceImpl implements AuthorizationCodeService {

    @Autowired
    private TblWxopenAuthorizationCodeMapper tblWxopenAuthorizationCodeMapper;

    @Override
    public TblWxopenAuthorizationCode getAuthorizationCodeByInfoType(Map<String, String> decryptMsgMap, String InfoType) {
        String appId = decryptMsgMap.get("AppId");
        String createTime = decryptMsgMap.get("CreateTime");
        String authorizerAppId = decryptMsgMap.get("AuthorizerAppid");
        TblWxopenAuthorizationCode authorizationCodeTemp = new TblWxopenAuthorizationCode();
        authorizationCodeTemp.setAppId(appId);
        authorizationCodeTemp.setCreateTime(Long.parseLong(createTime));
        authorizationCodeTemp.setInfoType(InfoType);
        authorizationCodeTemp.setAuthAppId(authorizerAppId);
        authorizationCodeTemp.setUpdateTime(System.currentTimeMillis() + "");
        if ("authorized".equals(InfoType)
                || "updateauthorized".equals(InfoType)) {
            authorizationCodeTemp.setAuthCode(decryptMsgMap.get("AuthorizationCode"));
            authorizationCodeTemp.setPreAuthCode(decryptMsgMap.get("PreAuthCode"));
            authorizationCodeTemp.setExpiresIn(Integer.parseInt(decryptMsgMap.get("AuthorizationCodeExpiredTime")));
        }
        return authorizationCodeTemp;
    }

    @Override
    public TblWxopenAuthorizationCode getAuthorizedCodeByAppId(String authAppId, boolean refreshAuthCode) throws PMSaaSException {
        TblWxopenAuthorizationCode authorizationCode = new TblWxopenAuthorizationCode();
        TblWxopenAuthorizationCodeExample authorizationCodeExample = new TblWxopenAuthorizationCodeExample();
        authorizationCodeExample.or().andAuthAppIdEqualTo(authAppId);
        List<TblWxopenAuthorizationCode> authorizationCodeList =
                tblWxopenAuthorizationCodeMapper.selectByExample(authorizationCodeExample);
        if(authorizationCodeList!=null && !authorizationCodeList.isEmpty())
            authorizationCode= authorizationCodeList.get(0);

        if (refreshAuthCode) {
            long currentTime = System.currentTimeMillis();
            long expiresIn = authorizationCode.getExpiresIn();
            if (authorizationCode == null) {
                throw new PMSaaSException(PMSaaSErrorCodeType.UNAUTHORIZED);
            }
            if ((expiresIn - (currentTime / 1000)) < 60) {
                throw new PMSaaSException(PMSaaSErrorCodeType.AUTHORIZATION_CODE_EXPIRED);
            }
        }
        return authorizationCode;
    }

//    @Override
//    public void insert(AuthorizationCode authorizedResultInfo) {
//        if (authorizedResultInfo != null) {
//            this.authorizationCodeDao.insert( authorizedResultInfo );
//        }
//    }

    @Override
    public void update(TblWxopenAuthorizationCode authorizationCode) throws PMSaaSException {
        TblWxopenAuthorizationCode authorizeResultInfoTemp = getAuthorizedCodeByAppId(authorizationCode.getAuthAppId(), false);
        if (authorizeResultInfoTemp != null) {
            authorizationCode.setId(authorizeResultInfoTemp.getId());
            tblWxopenAuthorizationCodeMapper.updateByPrimaryKeySelective(authorizationCode);
        } else {
            tblWxopenAuthorizationCodeMapper.insertSelective(authorizationCode);
        }
    }

    public StringBuffer jsonArrayToString(JSONArray jsonArray) {
        Iterator<Object> iterator = jsonArray.iterator();
        Set<Integer> integers = new HashSet();
        while (iterator.hasNext()) {
            org.json.JSONObject next = (org.json.JSONObject) iterator.next();
            org.json.JSONObject funcScopeCategory = next.getJSONObject("funcscope_category");
            if ((funcScopeCategory.getInt("id")) != 0) {
                integers.add(funcScopeCategory.getInt("id"));
            }
        }
        return dataConvert(integers);
    }

    private StringBuffer dataConvert(Set<Integer> integers) {
        if (CollectionUtils.isEmpty(integers)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Integer inte : integers) {
            sb.append(inte);
            sb.append(",");
        }
        return sb;
    }

}
