package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TblOtosaasOrder;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.util.Map;

/**
 * otosass相关接口
 */
public interface OtoSaasService {

    /**
     * otosaas管理后台登录接口
     * @param dto
     * @return
     * @throws ApiException
     */
    ApiMessage otosaasLogin(Dto dto)throws ApiException;



    /**
     * 保存在OtoSaaS平台下的订单
     * @param dto
     * @return
     * @throws ApiException
     */
    ApiMessage saveOrder(Dto dto) throws ApiException;

//    /**
//     * 获取订单详情
//     * @param dto
//     * @return
//     * @throws ApiException
//     */
    TblOtosaasOrder getOrderDetail(Dto dto) throws ApiException;
//
//     void update(TblOtosaasOrder tbl_otosaas_orderPO)  throws ApiException;
//
//     void merge(TblOtosaasOrder tbl_otosaas_orderPO) throws Exception;

     String jointParam(Dto dto);

     String getSign4OtoSaaS(Map<String, String> map);

    TblOtosaasOrder getOrderDetail2(Dto dto) throws ApiException;


    /**
     * 保存不同服务模块的订单
     * @param dto
     * @return
     * @throws Exception
     */
    ApiMessage saveServiceOrder(Dto dto) throws Exception;

}
