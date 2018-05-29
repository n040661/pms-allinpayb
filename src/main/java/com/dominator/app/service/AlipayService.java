package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangsuliang on 2018/4/26.
 */
public interface AlipayService {

    ApiMessage save(Dto dto) throws Exception;
    /**
     * 下单接口
     * @param dto
     * @return
     * @throws ApiException
     */
    ApiMessage placeOrder(Dto dto) throws Exception;

    /**
     * 支付宝支付回调
     * @param request
     * @return
     */
    String alipayCallback(HttpServletRequest request);
}
