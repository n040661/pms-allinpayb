package com.dominator.app.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.dominFramework.core.typewrap.Dto;
import com.dominator.AAAconfig.AlipayConfig2;
import com.dominator.app.service.AlipayService;
import com.dominator.app.utils.AlipayUtil;
import com.dominator.entity.TblOtosaasOrder;
import com.dominator.enums.ReqEnums;
import com.dominator.service.OtoSaasService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.PrimaryGenerater;
import com.dominator.utils.system.SeemseUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsuliang on 2018/4/24.
 * 支付宝
 */
@RestController("app.alipayController")
@RequestMapping("/app/v1/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private OtoSaasService otoSaasService;

    /**
     * 下单
     * @param request
     * @return
     */
    @PostMapping("/order/place")
    public ApiMessage placeOrder(HttpServletRequest request){
        ApiMessage msg;
        try {
            Dto dto = ApiUtils.getParams(request);
            msg =alipayService.placeOrder( dto );
        }catch  (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
        return msg;
    }


    /**
     * 下单回调
     * @param request
     * @return
     */
    @PostMapping("/order/callback")
    public String alipayCallback(HttpServletRequest request) {
        String success;
        try {
            success =alipayService.alipayCallback( request );
        }catch  (Exception e) {
            e.printStackTrace();
            return null;
        }
        return success;
    }


    /**
     * 订单保存
     * @param request
     * @return
     */
    @PostMapping("/order/save")
    public ApiMessage save(HttpServletRequest request){
        ApiMessage msg;
        try {
            Dto dto = ApiUtils.getParams(request);
            msg =alipayService.save( dto );
        }catch  (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
        return msg;
    }

    @PostMapping("/pay")
    public ApiMessage pay() throws Exception{
        String timeStamp = SeemseUtil.getCurrentTimeyyyyMMddHHmmss();
        //公共参数
        Map<String, String> publicParam = new HashMap<String, String>();
        publicParam.put("app_id", AlipayConfig2.APP_ID);
        publicParam.put("method", AlipayConfig2.APP_INT_NAME);
        publicParam.put("format", "JSON");
        publicParam.put("charset", AlipayConfig2.CHARSET);
        publicParam.put("sign_type", AlipayConfig2.SIGN_TYPE);
        publicParam.put("timestamp", timeStamp);
        publicParam.put("version", "1.0");
        publicParam.put("notify_url", AlipayConfig2.NOTIFY_URL);

        //业务参数
        Map<String, String> payParam = new HashMap<>();
        payParam.put("body", "Iphone6 16G");
        payParam.put("subject", "大乐透");
        payParam.put("out_trade_no", PrimaryGenerater.getInstance().uuid());
        payParam.put("timeout_express", "30m");
        payParam.put("total_amount","0.01" );
        payParam.put("product_code", AlipayConfig2.PRODUCT_CODE);
        JSONObject bizContentJson= JSONObject.fromObject(payParam);
        System.out.println("支付宝业务参数=="+bizContentJson);

        publicParam.put("biz_content", bizContentJson.toString());
        System.out.println("支付宝请求参数=="+publicParam);

        //RSA签名
        String rsaSign = AlipayUtil.rsaSign(publicParam);
        System.out.println("支付宝签名参数=="+rsaSign);

        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("app_id", AlipayConfig2.APP_ID);
        codeParam.put("method",  AlipayConfig2.APP_INT_NAME);
        codeParam.put("format", "json");
        codeParam.put("charset", AlipayConfig2.CHARSET);
        codeParam.put("sign_type", AlipayConfig2.SIGN_TYPE);
        codeParam.put("timestamp", URLEncoder.encode(timeStamp,"UTF-8"));
        codeParam.put("version", "1.0");
        codeParam.put("notify_url",  URLEncoder.encode(AlipayConfig2.NOTIFY_URL,"UTF-8"));

        //最后对请求字符串的所有一级value（biz_content作为一个value）进行encode，编码格式按请求串中的charset为准，没传charset按UTF-8处理
        codeParam.put("biz_content", URLEncoder.encode(bizContentJson.toString(), "UTF-8"));
        String data = AlipaySignature.getSignContent(codeParam);                  //拼接后的字符串
        data = data + "&sign=" + URLEncoder.encode(rsaSign, "UTF-8");
        System.out.println("支付宝支付参数=="+data);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("data", data);
        System.out.println(data);
        return ApiMessageUtil.success( dataMap );
    }


    @PostMapping("/getOrderDetail")
    public ApiMessage getOrderDetail(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            TblOtosaasOrder detail = otoSaasService.getOrderDetail2( dto );
            return ApiMessageUtil.success(detail);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }
    public static void main(String[] args) {
        System.out.println(SeemseUtil.getCurrentTimeyyyyMMddHHmmss());
        System.out.println(PrimaryGenerater.getInstance().uuid().substring( 1,20 ));
    }
}