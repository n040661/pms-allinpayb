package com.dominator.weixin.service;

import com.dominFramework.core.typewrap.Dto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WXRefundService {
     /**
      * 微信公众号退款
      * @param dto
      * @param request
      * @return
      * @throws Exception
      */
     Map<String, Object> refund4Wechat(Dto dto, HttpServletRequest request) throws Exception;


     /**
      * 不同的支付渠道退款
      * @param dto
      * @param request
      * @return
      * @throws Exception
      */
     Map<String,Object> refund(Dto dto, HttpServletRequest request) throws Exception;
}
