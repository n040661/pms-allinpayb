package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TProperty;
import com.dominator.entity.TblWxopenAuthorizerAccountInfo;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.util.List;

public interface WechatService {

    ApiMessage checkWechat(Dto dto);

    ApiMessage addPropertyWechat(Dto dto) throws ApiException;

    ApiMessage listWechatsByName(Dto dto);

    List<TblWxopenAuthorizerAccountInfo> listWechats(String property_id);

    String getPropertyIdByAppId(String appId);
}
