package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

public interface TimerService {

    /**
     * 手动推送账单
     * @param dto company_name String 非必传 企业名称
     * @return
     */
    ApiMessage push(Dto dto) throws ApiException;
}
