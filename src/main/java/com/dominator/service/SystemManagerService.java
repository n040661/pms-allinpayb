package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

public interface SystemManagerService {

    ApiMessage delProperty(Dto dto) throws ApiException;

    ApiMessage addWechat(Dto dto) throws ApiException;
}
