package com.dominator.service.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface ApiUserService {
    ApiMessage getuser(Dto dto);

    ApiMessage updateuser(Dto dto);
}
