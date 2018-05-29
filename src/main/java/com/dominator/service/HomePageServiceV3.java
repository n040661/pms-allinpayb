package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface HomePageServiceV3 {
    ApiMessage pageV3(Dto dto);

    ApiMessage getbanner(Dto dto);
}
