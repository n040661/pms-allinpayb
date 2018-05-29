package com.dominator.service.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface ApiGardenService {
    ApiMessage getgardensByProId(Dto dto);
}
