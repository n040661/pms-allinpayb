package com.dominator.weixin.service;


import com.dominator.entity.TblWxopenAuthorizationInfo;

interface AuthorizationInfoMapperService {
    TblWxopenAuthorizationInfo getAuthorizationInfoByAppId(String appId);

    int insert(TblWxopenAuthorizationInfo authorizationInfo);

    int update(TblWxopenAuthorizationInfo authorizationInfo);

}
