package com.dominator.weixin.service;

import com.dominator.weixin.exception.PMSaaSException;

public interface ComponentAccessTokenService {
    String getComponentAccessToken(String compAppId) throws PMSaaSException;
}
