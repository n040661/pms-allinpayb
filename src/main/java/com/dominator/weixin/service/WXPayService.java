package com.dominator.weixin.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WXPayService {

     /**
      * 微信支付回调
      * @param request
      * @return
      * @throws Exception
      */
     String payAfter4Wechat(HttpServletRequest request) throws Exception;

     /**
      * 通联支付回调
      * @param request
      * @throws Exception
      */
     void payAfter4AllInPay(HttpServletRequest request, HttpServletResponse response) throws Exception;

     /**
      * 获取配置
      * @param dto
      * @return
      * @throws Exception
      */
     ApiMessage getConfig(Dto dto) throws Exception;

     /**
      * 公众号下单
      * @param dto
      * @return
      * @throws Exception
      */
     ApiMessage placeOrder4Public(Dto dto) throws Exception;

     /**
      * H5下单
      * @param dto
      * @return
      * @throws Exception
      */
     ApiMessage placeOrder4H5(Dto dto) throws Exception;

     /**
      * 通联下单
      * @param dto
      * @return
      * @throws Exception
      */
     ApiMessage placeOrder4AllInPay(Dto dto) throws Exception;

     /**
      * 根据payType类型判断使用
      * @param dto payType 0:微信公众号支付 1：微信H5支付 2：通联支付
      * @return
      * @throws Exception
      */
     ApiMessage placeOrder(Dto dto) throws Exception;

}
