package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

public interface HomePageService {
    ApiMessage page(Dto dto) throws ApiException;

    ApiMessage show(Dto dto);

    ApiMessage update(Dto dto);

    ApiMessage user(Dto dto);

}
