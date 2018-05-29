package com.dominator.app.service.impl;

import com.alipay.api.internal.util.AlipaySignature;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.AAAconfig.AlipayConfig2;
import com.dominator.app.service.AlipayService;
import com.dominator.app.utils.AlipayUtil;
import com.dominator.entity.TAppointPublic;
import com.dominator.entity.TAppointPublicRecord;
import com.dominator.entity.TblOtosaasOrder;
import com.dominator.entity.TblOtosaasOrderExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TAppointPublicMapper;
import com.dominator.mapper.TAppointPublicRecordMapper;
import com.dominator.mapper.TblOtosaasOrderMapper;
import com.dominator.service.PublicService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.PrimaryGenerater;
import com.dominator.utils.system.SeemseUtil;
import com.dominator.weixin.util.WxUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangsuliang on 2018/4/26.
 */
@Slf4j
@Service("app.alipayServiceImpl")
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    private PublicService publicService;

    @Autowired
    private TAppointPublicRecordMapper tAppointPublicRecordMapper;

    @Autowired
    private TblOtosaasOrderMapper tblOtosaasOrderMapper;

    private static String SUCCESS ="success";

    @Override
    public ApiMessage save(Dto dto) throws Exception {
        String serviceId = dto.getString( "serviceId" ); //作为orderId
        String prodType = dto.getString( "prodType" );
        String prodSubType = dto.getString( "prodSubType" );
        String body = dto.getString( "body" );
        String totalFeeStr = dto.getString( "totalFee" );
        String detail = dto.getString( "detail" );
        TblOtosaasOrder otosaasOrder =new TblOtosaasOrder();
        String id = PrimaryGenerater.getInstance().uuid();
        otosaasOrder.setId( id );
        otosaasOrder.setBody( body );
        otosaasOrder.setDetail( detail );
        otosaasOrder.setOrderId( serviceId );
        otosaasOrder.setServiceId( serviceId );
        otosaasOrder.setProdType( prodType );
        otosaasOrder.setProdSubType( prodSubType );
        int totalFee = Integer.parseInt( totalFeeStr );
        otosaasOrder.setTotalFee( totalFee);
        String outTradeNo = PrimaryGenerater.getInstance().uuid();
        otosaasOrder.setOutTradeNo( outTradeNo );
        tblOtosaasOrderMapper.insert( otosaasOrder );
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage placeOrder(Dto dto) throws Exception {
        //OtosaasServiceImpl.java
        String orderId = dto.getString( "orderId" );
        TblOtosaasOrderExample example = new TblOtosaasOrderExample();
        TblOtosaasOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo( orderId );
        List<TblOtosaasOrder> orders = tblOtosaasOrderMapper.selectByExample( example );
        TblOtosaasOrder order = orders.get( 0 );
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
        payParam.put("body", order.getDetail());
        payParam.put("subject", order.getBody());
        payParam.put("out_trade_no", order.getOutTradeNo() );
        payParam.put("timeout_express", "15m");
        int totalFeeI = order.getTotalFee();
        double totalFee =(double) totalFeeI;
        totalFee = totalFee/100;
        //payParam.put("total_amount",totalFee+"");
        payParam.put( "total_amount","0.01" );
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
        codeParam.put("biz_content", URLEncoder.encode(bizContentJson.toString(), "UTF-8"));
        String data = AlipaySignature.getSignContent(codeParam);                  //拼接后的字符串
        data = data + "&sign=" + URLEncoder.encode(rsaSign, "UTF-8");
        System.out.println("支付宝支付参数=="+data);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("data", data);
        System.out.println(data);
        return ApiMessageUtil.success( dataMap );
    }

    @Override
    public String alipayCallback(HttpServletRequest request) {
        try{
            BufferedReader reader = request.getReader();
            String line;
            StringBuilder inputString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            reader.close();
            log.info("【支付宝支付回调返回值，回调明文：" + inputString.toString() + "】");
            Map<String, String> alipayResult = WxUtils.xmlToMap(inputString.toString());
            String notifyId = alipayResult.get( "notify_id" );
            String response = AlipayUtil.verifyResponse( notifyId );  //判断是否是支付宝返回的值
            if("false".equals( response )){
                throw new ApiException( ReqEnums.NOT_ALIPAY_CALLBACK.getCode(),ReqEnums.NOT_ALIPAY_CALLBACK.getMsg() );
            }
            boolean deSign = AlipayUtil.rsaCheck( alipayResult );
            if(!deSign){
                throw new ApiException( ReqEnums.VERIFY_SIGN_FAIL.getCode(),ReqEnums.VERIFY_SIGN_FAIL.getMsg() );
            }
            String appId = alipayResult.get( "app_id" );
            String outTradeNo = alipayResult.get( "out_trade_no" );
            String totalAmount = alipayResult.get( "total_amount" );
            String tradeNo = alipayResult.get( "trade_no" );
            String signType = alipayResult.get( "sign_type" );
            String buyerId = alipayResult.get( "buyer_id" );
            TblOtosaasOrderExample example = new TblOtosaasOrderExample();
            TblOtosaasOrderExample.Criteria criteria = example.createCriteria();
            criteria.andOutTradeNoEqualTo( outTradeNo );
            List<TblOtosaasOrder> orders = tblOtosaasOrderMapper.selectByExample( example );
            TblOtosaasOrder otosaasOrder = orders.get( 0 );
            otosaasOrder.setPayType( "4 ");
            String tradeStatus = alipayResult.get("trade_status");
            int transCode = getTransCode( tradeStatus );
            otosaasOrder.setSignType( signType );
            otosaasOrder.setPayStatus( transCode );
            otosaasOrder.setTransactionId( tradeNo );
            otosaasOrder.setOpenid(buyerId );
            otosaasOrder.setAppid( appId);
            otosaasOrder.setTotalFee( Integer.parseInt( totalAmount ));
            otosaasOrder.setPayStatus( transCode );
            tblOtosaasOrderMapper.updateByPrimaryKeySelective( otosaasOrder );
            String orderId = otosaasOrder.getOrderId();
            TAppointPublicRecord record = tAppointPublicRecordMapper.selectByPrimaryKey( orderId );
            String status="";
            if(0==transCode){
                status=transCode+"";
            }else if(1==transCode||3==transCode){
                status="3";
            }else if(4==transCode){
                status="0";
            }
            record.setStatus( status );
            Dto recordDto =new HashDto();
            recordDto.put( "orderId",orderId);
            recordDto.put( "status",status );
            recordDto.put( "payType","4" );      //支付类型 0:微信公众号支付 1:微信H5支付 2:通联支付 3  微信APP支付 4 支付宝APP支付
            publicService.putOrder( recordDto );
            String prodType = otosaasOrder.getProdType();
            if("oto".equals( prodType )){

            }
        }catch (Exception e){
          e.printStackTrace();
        }
        return SUCCESS;
    }

    //支付状态:0:已下单，未支付1:支付成功 2:已退款 3 支付成功不可退款 4 支付失败
    public static int getTransCode(String result){
     switch (result){
         case "WAIT_BUYER_PAY":    return 0;    //交易创建，等待买家付款

         case "TRADE_CLOSED":      return 4;     // 未付款交易超时关闭，或支付完成后全额退款

         case "TRADE_SUCCESS":     return 1;    //交易支付成功

         case "TRADE_FINISHED":    return 3;   //交易结束，不可退款

             default:              return -1;
     }
    }

}
