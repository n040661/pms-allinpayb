package com.dominator.weixin.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TblOtosaasOrder;
import com.dominator.entity.TblOtosaasOrderExample;
import com.dominator.entity.TblPublicAccount;
import com.dominator.enums.OToSaaSOrderEnum;
import com.dominator.mapper.TblOtosaasOrderMapper;
import com.dominator.mapper.TblPublicAccountMapper;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.SeemseUtil;
import com.dominator.weixin.config.PayModeConfig;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.service.WXPayService;
import com.dominator.weixin.util.*;
import com.dominator.weixin.util.aes.XMLParse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class WXPayServiceImpl implements WXPayService {

    @Autowired
    private TblOtosaasOrderMapper tblOtosaasOrderMapper;

    @Autowired
    private TblPublicAccountMapper tblPublicAccountMapper;

    @Override
    @Transactional
    public void payAfter4AllInPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("==========进入通联支付回调接口===========参数如下：");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            log.info(paramName + "=");
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    log.info(paramValue);
                }
            }
        }

        String paymentOrderId = request.getParameter("paymentOrderId");
        if (paymentOrderId != null) {
            TblOtosaasOrderExample otosaasOrderExample = new TblOtosaasOrderExample();
            otosaasOrderExample.or().andOrderIdEqualTo(request.getParameter("orderNo"));
            List<TblOtosaasOrder> otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
            TblOtosaasOrder orderDetail = new TblOtosaasOrder();
            if (otosaasOrderList != null && !otosaasOrderList.isEmpty())
                orderDetail = otosaasOrderList.get(0);

            String merchantId = request.getParameter("merchantId");
            String orderDatetime = request.getParameter("orderDatetime");
            String orderAmount = request.getParameter("orderAmount");
            String payAmount = request.getParameter("payAmount");
            // String signMsg = request.getParameter("signMsg");
            orderDetail.setMchId(merchantId);
            orderDetail.setPayStatus(1);
            orderDetail.setTransactionId(paymentOrderId);
            orderDetail.setIsValid("1");
            orderDetail.setOrderDatetime(orderDatetime);
            orderDetail.setPayType("2");
            orderDetail.setTotalFee(Integer.parseInt(orderAmount));

            //封装发送OTOSaaS的参数封装
            Map<String, Object> map2 = new TreeMap<>();
            map2.put("return_code", "SUCCESS");
            map2.put("total_fee", payAmount);
            map2.put("transaction_id", paymentOrderId);
            map2.put("order_id", orderDetail.getOrderId());
            map2.put("out_trade_no", orderDetail.getOutTradeNo());

            //私钥加密
            String text = BlmSignature.getText(map2);
            String sign = BlmSignature.sign(WXConfig.OTOSSAAS_PRIVATE_KEY, text);

            JSONObject json = new JSONObject();
            json.put("return_code", "SUCCESS");
            json.put("total_fee", payAmount);
            json.put("transaction_id", paymentOrderId);
            json.put("order_id", orderDetail.getOrderId());
            json.put("out_trade_no", orderDetail.getOutTradeNo());
            json.put("sign", sign);
            log.info("【通联支付结果返回OTOSaaS平台：" + json.toString() + "】");
            JSONObject sb = com.dominator.weixin.util.HttpUtils.doPost(WXConfig.OTOSAAS_REFUND_NOTIFY_URL, json);
            log.info("【OTOSaaS平台接收通联支付支付结果：" + sb + "】");
            tblOtosaasOrderMapper.updateByPrimaryKeySelective(orderDetail);
        }
    }

    @Override
    public ApiMessage placeOrder(Dto dto) throws Exception {
        String payType = dto.getString("pay_type");
        if ("0".equals(payType)) {
            return placeOrder4Public(dto);
        } else if ("1".equals(payType)) {
            return placeOrder4H5(dto);
        } else if ("2".equals(payType)) {
            return placeOrder4AllInPay(dto);
        } else {
            return null;
        }
    }

    @Override
    public ApiMessage placeOrder4AllInPay(Dto dto) throws Exception {
        JSONObject json = new JSONObject();
        dto.put("receive_url", WXConfig.ALL_IN_PAY_RESULT_NOTICE_URL);
        json.put("paramStr", dto);
        String result = HttpUtils.doPost1(PayModeConfig.ALLINPAY_PLACE_ORDER, json);
        log.info("【placeOrder4AllInPay：" + result + "】");
        JSONObject jsonObject = JSONObject.fromObject(result);
        String code = jsonObject.getString("code");
        if ("200".equals(code)) {
            JSONObject data = jsonObject.getJSONObject("data");
            log.info(data + "");
            return ApiMessageUtil.success(data);
        }
        return ApiMessageUtil.failure();
    }

    @Override
    public String payAfter4Wechat(HttpServletRequest request) throws Exception {

        //验证签名、基础参数是否是微信发来的，一般情况下无需修改
        try {
            BufferedReader reader = request.getReader();
            log.info("request.getReader():" + reader);
            String line;
            StringBuilder inputString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            reader.close();

            log.info("【微信支付回调进入支付回调接口，回调明文：" + inputString.toString() + "】");
            Map<String, String> result4PayMap = WxUtils.xmlToMap(inputString.toString());

            TblOtosaasOrderExample otosaasOrderExample = new TblOtosaasOrderExample();
            otosaasOrderExample.or().andOutTradeNoEqualTo(result4PayMap.get("out_trade_no"));
            List<TblOtosaasOrder> otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
            TblOtosaasOrder orderDetail = new TblOtosaasOrder();
            if (otosaasOrderList != null && !otosaasOrderList.isEmpty())
                orderDetail = otosaasOrderList.get(0);

            // if(result4PayMap.containsKey("return_code")){
            if (result4PayMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
                orderDetail.setTransactionId(result4PayMap.get("transaction_id"));
                orderDetail.setPayStatus(1);
                orderDetail.setIsValid("1");
                tblOtosaasOrderMapper.updateByPrimaryKeySelective(orderDetail);
            }

//            otosaasOrderExample.clear();
//            otosaasOrderExample.or().andOutTradeNoEqualTo(result4PayMap.get("out_trade_no"));
//            otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
//            TblOtosaasOrder order = new TblOtosaasOrder();
//            if (otosaasOrderList != null && !otosaasOrderList.isEmpty())
//                order = otosaasOrderList.get(0);

            Map<String, Object> map = new TreeMap<>();
            map.put("return_code", result4PayMap.get("return_code"));
            map.put("total_fee", result4PayMap.get("total_fee"));
            map.put("time_end", result4PayMap.get("time_end"));
            map.put("transaction_id", result4PayMap.get("transaction_id"));
            map.put("order_id", orderDetail.getOrderId());
            map.put("out_trade_no", orderDetail.getOutTradeNo());

            //私钥加密
            String text = BlmSignature.getText(map);
            String sign = BlmSignature.sign(WXConfig.OTOSSAAS_PRIVATE_KEY, text);
            JSONObject json = new JSONObject();
            json.put("return_code", result4PayMap.get("return_code"));
            json.put("total_fee", result4PayMap.get("total_fee"));
            json.put("time_end", result4PayMap.get("time_end"));
            json.put("transaction_id", result4PayMap.get("transaction_id"));
            json.put("order_id", orderDetail.getOrderId());
            json.put("out_trade_no", result4PayMap.get("out_trade_no"));
            json.put("sign", sign);
            log.info("【支付结果返回OTOSaaS平台：" + json.toString() + "】");
            JSONObject sb = com.dominator.weixin.util.HttpUtils.doPost(WXConfig.OTOSAAS_REFUND_NOTIFY_URL, json);
            log.info("【OTOSaaS平台接收支付结果：" + sb + "】");
            //商户接受到微信的回调信息正确校验后，需返回给微信参数
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>\n";
        } catch (Exception e) {
            log.info("【微信异步回调出错，错误为：" + e.getMessage() + "】");
        }
        return null;
    }


    @Override
    public ApiMessage getConfig(Dto dto) throws Exception {
        log.info("【进入jsdkConfig接口，携带参数为：" + dto + "】");
        String appid = dto.getString("appid");
        String url = dto.getString("url");
        TblPublicAccount account = tblPublicAccountMapper.selectByPrimaryKey(appid);
        String params;
        String jsApiTicketUrl = WXConfig.JSAPI_TOKEN_URL + appid;
        params = "&secret=" + account.getAppSecret();
        JSONObject jsonObject4accessToken = com.dominator.weixin.util.HttpUtils.doPost(jsApiTicketUrl + params, new JSONObject());
        Object access_token = jsonObject4accessToken.get("access_token");

        String jsapiTicketURL = WXConfig.JSAPI_TICKET_URL + access_token.toString() + "&type=jsapi";
        JSONObject jsapiTicket4Json = com.dominator.weixin.util.HttpUtils.doPost(jsapiTicketURL, new JSONObject());

        Long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = UUID.randomUUID().toString();

        //生成signature
//        Map<String, Object> valueMap = new HashMap<>();
//        valueMap.put("noncestr", noncestr);
//        valueMap.put("timestamp", timestamp);
//        valueMap.put("url", url);
//        valueMap.put("jsapi_ticket", jsapiTicket4Json.get("ticket"));
        String signbefore = "jsapi_ticket=" + jsapiTicket4Json.get("ticket") + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        log.info("【参与JSSDK配置签名参数：" + signbefore + "】");
        String signAfter = SHA1.SHA14Str(signbefore);
        Map<String, Object> map = new HashMap<>();
        map.put("jsapi_ticket", jsapiTicket4Json.get("ticket"));
        map.put("appid", account.getAppiId());
        map.put("signature", signAfter.toLowerCase());
        map.put("timestamp", timestamp.toString());
        map.put("noncestr", String.valueOf(noncestr));
        log.info("【jsdkConfig接口返回结果：" + map + "】");
        return ApiMessageUtil.success(map);
    }

    @Override
    public ApiMessage placeOrder4Public(Dto dto) throws Exception {
        log.info("【进入placeOrder4Public接口，携带参数：" + dto + "】");
        String openid = dto.getString("openid");
        String orderId = dto.getString("order_id");

        TblOtosaasOrderExample otosaasOrderExample = new TblOtosaasOrderExample();
        otosaasOrderExample.or().andOrderIdEqualTo(orderId);
        List<TblOtosaasOrder> otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
        TblOtosaasOrder order = new TblOtosaasOrder();
        if (otosaasOrderList != null && !otosaasOrderList.isEmpty())
            order = otosaasOrderList.get(0);

        if (dto.containsKey("pay_type")) {
            String payType = dto.getString("pay_type");
            order.setPayType(payType);
        }

        log.info("【获取订单信息】" + order);
        if (order == null) {
            throw new ApiException(OToSaaSOrderEnum.ORDER_NO_EXIST.getMsg());
        }
        SortedMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("appid", order.getAppid());
        parameterMap.put("mch_id", order.getMchId());

        String nonce_str = SeemseUtil.randoms().toUpperCase();
        parameterMap.put("nonce_str", nonce_str);
        order.setNonceStr(nonce_str);

        parameterMap.put("body", order.getBody());
        parameterMap.put("openid", openid);
        parameterMap.put("out_trade_no", order.getOutTradeNo());
        parameterMap.put("total_fee", order.getTotalFee());
        parameterMap.put("sign_type", order.getSignType());
        parameterMap.put("spbill_create_ip", order.getSpbillCreateIp());
        parameterMap.put("notify_url", WXConfig.WEIXIN_PAY_NOTIFY_URL);
        order.setNotifyUrl(WXConfig.WEIXIN_PAY_NOTIFY_URL);
        parameterMap.put("trade_type", order.getTradeType());   //支付类型  只有一个参数JSAPI
        log.info("【参加一次签名参数：" + parameterMap + "】");
        TblPublicAccount account = tblPublicAccountMapper.selectByPrimaryKey(order.getAppid());
        //生成签名
        String signXML = WxUtils.generateSignedXml(parameterMap, account.getApiKeystore(), WXPayConstants.SignType.MD5);
        String postResult = com.dominator.weixin.util.HttpUtils.doPost(new URL(WXConfig.WX_PLACE_ORDER_URL), signXML);
        log.info("【微信返回下单结果】" + postResult);
        SortedMap<String, Object> sortedMap = jsonToMap(XMLParse.xml2json(postResult));
        order.setSign(sortedMap.get("sign").toString());
        tblOtosaasOrderMapper.updateByPrimaryKeySelective(order);

        if ("SUCCESS".equals(sortedMap.get("return_code"))) {
            //二次签名
            SortedMap<String, Object> map = new TreeMap<>();
            map.put("appId", order.getAppid());
            map.put("nonceStr", WxUtils.getRandomString(20));
            map.put("package", "prepay_id=" + sortedMap.get("prepay_id"));
            map.put("signType", order.getSignType());
            map.put("timeStamp", (System.currentTimeMillis() / 1000));
            String secondSign = WxUtils.generateSignature2(map, account.getApiKeystore(), WXPayConstants.SignType.MD5);
            map.put("paySign", secondSign);
            log.info("【二次签名后返回值：" + map + "】");
            return ApiMessageUtil.success(map);
        } else {
            log.info("错误原因为：" + sortedMap.get("return_msg"));
            return null;
        }
    }

    @Override
    public ApiMessage placeOrder4H5(Dto dto) throws Exception {
        String mchId = "1344365201";
        String appid = "wx9e9ffc8d064dd58c";
        String apiKeystore = "LEmwA2hYOOhSnTWJsKLdHJ9BEvMArP0R";
        String tradeType = "MWEB";
        String orderId = dto.getString("order_id");

        TblOtosaasOrderExample otosaasOrderExample = new TblOtosaasOrderExample();
        otosaasOrderExample.or().andOrderIdEqualTo(orderId);
        List<TblOtosaasOrder> otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
        TblOtosaasOrder order;
        if (otosaasOrderList != null && !otosaasOrderList.isEmpty())
            order = otosaasOrderList.get(0);
        else
            throw new ApiException(OToSaaSOrderEnum.ORDER_NO_EXIST.getMsg());

        SortedMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("appid", appid);
        parameterMap.put("mch_id", mchId);

        String nonce_str = SeemseUtil.randoms().toUpperCase();
        parameterMap.put("nonce_str", nonce_str);
        order.setNonceStr(nonce_str);

        parameterMap.put("body", order.getBody());
        parameterMap.put("out_trade_no", order.getOutTradeNo());
        parameterMap.put("total_fee", order.getTotalFee());
        parameterMap.put("sign_type", order.getSignType());
        parameterMap.put("spbill_create_ip", dto.getString("client_ip"));
        order.setSpbillCreateIp(dto.getString("client_ip"));

        parameterMap.put("trade_type", tradeType);
        parameterMap.put("notify_url", WXConfig.WEIXIN_PAY_NOTIFY_URL);
        order.setNotifyUrl(WXConfig.WEIXIN_PAY_NOTIFY_URL);
        parameterMap.put("trade_type", tradeType);

        //生成签名
        String sign = WxUtils.generateSignedXml(parameterMap, apiKeystore, WXPayConstants.SignType.MD5);
        String postResult = com.dominator.weixin.util.HttpUtils.doPost(new URL(WXConfig.WX_PLACE_ORDER_URL), sign);
        log.info("postResult:" + postResult);
        SortedMap<String, Object> sortedMap = jsonToMap(XMLParse.xml2json(postResult));
        order.setSign(sortedMap.get("sign").toString());
        tblOtosaasOrderMapper.updateByPrimaryKeySelective(order);

        if ("SUCCESS".equals(sortedMap.get("return_code"))) {
            //二次签名
            SortedMap<String, Object> map = new TreeMap<>();
            map.put("appId", appid);
            map.put("nonceStr", WxUtils.getRandomString(20));
            map.put("package", "prepay_id=" + sortedMap.get("prepay_id"));
            map.put("signType", order.getSignType());
            map.put("timeStamp", (System.currentTimeMillis() / 1000));
            map.put("mweb_url", sortedMap.get("mweb_url"));
            String secondSign = WxUtils.generateSignature2(map, apiKeystore, WXPayConstants.SignType.MD5);
            map.put("paySign", secondSign);
            log.info("【二次签名后返回值：" + map + "】");
            return ApiMessageUtil.success(map);
        } else {
            log.info("【错误原因为：" + sortedMap.get("return_msg") + "】");
            return null;
        }
    }

    private SortedMap<String, Object> jsonToMap(org.json.JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        SortedMap<String, Object> signMap = new TreeMap<>();
        org.json.JSONObject json4Xml = jsonObject.getJSONObject("xml");
        signMap.put("appid", json4Xml.get("appid"));
        signMap.put("nonce_str", json4Xml.get("nonce_str"));
        signMap.put("package", json4Xml.get("prepay_id"));
        signMap.put("sign", json4Xml.get("sign"));
        signMap.put("trade_type", json4Xml.get("trade_type"));
        signMap.put("return_msg", json4Xml.get("return_msg"));
        signMap.put("result_code", json4Xml.get("result_code"));
        signMap.put("mch_id", json4Xml.get("mch_id"));
        signMap.put("return_code", json4Xml.get("return_code"));
        signMap.put("prepay_id", json4Xml.get("prepay_id"));
        if (json4Xml.toString().contains("mweb_url"))
            signMap.put("mweb_url", json4Xml.get("mweb_url"));
        return signMap;
    }

}
