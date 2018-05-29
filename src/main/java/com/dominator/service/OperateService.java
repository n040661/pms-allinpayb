package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface OperateService {
    ApiMessage listModules(Dto dto);

    ApiMessage addPackage(Dto dto);
}
