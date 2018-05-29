package com.dominator.weixin.controller;

/**
 * Created by zhangsuliang on 2017/9/13.
 */

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import com.dominator.weixin.service.WXPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信支付相关的接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/weixin/pay")
@Slf4j
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    /**
     * 微信支付结果回调
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/callback")
    public String payAfter(HttpServletRequest request) throws Exception {
        try {
            return wxPayService.payAfter4Wechat(request);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR).toString();
        }
    }

    @RequestMapping("/callback4AllInPay")
    public void payAfter4AllInPay(HttpServletRequest request, HttpServletResponse response) {
        try {
            wxPayService.payAfter4AllInPay(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * jsdk配置接口
     *
     * @param paramStr appid 微信公众号appid
     *                 url   地址
     */
    @PostMapping("/jsdkConfig")
    public ApiMessage getConfig(@RequestParam String paramStr){
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            return wxPayService.getConfig(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 微信公众号支付
     *
     * @param paramStr 必填    appid   公众账号ID
     *                 mch_id   商户号
     *                 nonce_str  随机字符串
     *                 sign   签名
     *                 body  商品描述
     *                 out_trade_no  商户订单号
     *                 total_fee  标价金额
     *                 spbill_create_ip  终端IP
     *                 trade_type  交易类型（公众号支付值为JSAPI）
     *                 openid  用户标识，公众号支付必传字段
     *                 notify_url  通知地址，支付结果回调地址
     *                 非必填  device_info  设备号
     *                 sign_type   签名类型
     *                 detail  商品详情
     *                 attach  附加数据
     *                 fee_type  标价币种
     *                 time_start  交易起始时间
     *                 time_expire  交易结束时间
     *                 goods_tag  订单优惠标记
     *                 limit_pay  指定支付方式
     *                 scene_info 场景信息
     */
    @PostMapping(value = "/placeOrder0")
    public ApiMessage placeOrder4Public(@RequestParam String paramStr){
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            return wxPayService.placeOrder4Public(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    @PostMapping("/placeOrder")
    public ApiMessage placeOrder(@RequestParam String paramStr) {
        log.info("[下单接口，携带参数：" + paramStr + "]");
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            return wxPayService.placeOrder(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 微信H5支付
     *
     * @param paramStr 下单参数除以下情况，其他与微信公众号一致
     * @param request  trade_type 传 MWEB
     *                 openid为非必要字段
     */
    @PostMapping(value = "/placeOrder4H5")
    public ApiMessage commonPlaceOrder4H5(@RequestParam String paramStr, HttpServletRequest request){
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            dto.put("client_ip", request.getRemoteAddr());
            return wxPayService.placeOrder4H5(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }


}