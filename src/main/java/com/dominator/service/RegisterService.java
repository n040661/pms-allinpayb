package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface RegisterService {
    ApiMessage register(Dto dto);

    ApiMessage updatepassword(Dto dto);
}
