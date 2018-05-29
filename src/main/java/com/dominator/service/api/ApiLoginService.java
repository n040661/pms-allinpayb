package com.dominator.service.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

public interface ApiLoginService {

    Dto login(Dto dto);

    ApiMessage identityList(Dto dto)throws ApiException;
}
