package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

public interface PayService {



    /**
     * 获取openid
     * @param dto
     * @return
     */
    ApiMessage getOpenId(Dto dto) throws ApiException;


    /**
     * 创建订单
     * @param dto
     * @return
     * @throws ApiException
     */
    ApiMessage createOrder(Dto dto) throws ApiException;

}
