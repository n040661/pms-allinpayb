package com.dominator.service.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TModules;
import com.dominator.utils.api.ApiMessage;

import java.util.List;

public interface HomePageServiceV3 {
    ApiMessage pageV3(Dto dto);

    ApiMessage getbanner(Dto dto);

    List<TModules> H5modules(Dto dto);
}
