package com.dominator.weixin.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.weixin.exception.PMSaaSException;

public interface WebPageAuthService {
     ApiMessage getWeiXinUserInfo(Dto dto) throws PMSaaSException;
     ApiMessage getWeiXinUserInfo2(Dto dto) throws PMSaaSException;
}
