package com.dominator.weixin.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TblOtosaasOrder;
import com.dominator.entity.TblOtosaasOrderExample;
import com.dominator.mapper.TblOtosaasOrderMapper;
import com.dominator.mapper.TblPublicAccountMapper;
import com.dominator.utils.system.SeemseUtil;
import com.dominator.weixin.config.PayModeConfig;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.service.WXRefundService;
import com.dominator.weixin.util.BlmSignature;
import com.dominator.weixin.util.HttpUtils;
import com.dominator.weixin.util.WXPayConstants;
import com.dominator.weixin.util.WxUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.*;

@Service
@Slf4j
public class WXRefundServiceImpl implements WXRefundService {

    @Autowired
    private TblOtosaasOrderMapper tblOtosaasOrderMapper;

    @Autowired
    private TblPublicAccountMapper tblPublicAccountMapper;

    @Override
    public Map<String, Object> refund4Wechat(Dto dto,HttpServletRequest request) throws Exception{
        String out_refund_no = dto.getString("out_refund_no");
        String sign=dto.getString("sign");
        dto.remove("sign");
        Map<String,Object> map4OTOSaaS=new HashMap<>();

        boolean verify = BlmSignature.verify(WXConfig.OTOSSAAS_PUBLIC_KEY, BlmSignature.getText(dto), sign);
        if(!verify){
            map4OTOSaaS.put("return_code","failure");
            map4OTOSaaS.put("return_msg","解签失败");
            return map4OTOSaaS;
        }

        TblOtosaasOrderExample otosaasOrderExample = new TblOtosaasOrderExample();
        otosaasOrderExample.or().andTransactionIdEqualTo(dto.getString("transaction_id"));
        List<TblOtosaasOrder> otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
        TblOtosaasOrder payResultPO = new TblOtosaasOrder();
        if(otosaasOrderList!=null && !otosaasOrderList.isEmpty())
            payResultPO = otosaasOrderList.get(0);

        StringBuilder sb2 = new StringBuilder();
        SortedMap<String, Object> signMap = new TreeMap<>();
        signMap.put("appid",payResultPO.getAppid());
        signMap.put("mch_id",payResultPO.getMchId());
        signMap.put("nonce_str",payResultPO.getNonceStr());
        signMap.put("out_refund_no",out_refund_no);
        signMap.put("total_fee",payResultPO.getTotalFee());
        signMap.put("refund_fee",payResultPO.getTotalFee());
        signMap.put("transaction_id",payResultPO.getTransactionId());

        String signXML= WxUtils.generateSignedXml( signMap,
                tblPublicAccountMapper.selectByPrimaryKey(payResultPO.getAppid()).getApiKeystore(),
                WXPayConstants.SignType.MD5 );
        KeyStore keyStore = KeyStore.getInstance("PKCS12");   //指定读取证书格式为PKCS12

        String certPath = request.getSession().getServletContext().getRealPath("/WEB-INF/cert/") +payResultPO.getMchId()+"-apiclient_cert.p12";
        log.info("【证书地址："+certPath+"】");
        FileInputStream instream = new FileInputStream(new File(certPath));
        try {
            keyStore.load(instream, payResultPO.getMchId().toCharArray());  //指定PKCS12的密码(商户ID)
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,  payResultPO.getMchId().toCharArray()).build();  //ssl双向验证发送http请求报文
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        HttpPost httppost = new HttpPost(WXConfig.WX_REFUND_URL);
        StringEntity se = new StringEntity(signXML.toString(), "UTF-8");
        httppost.setEntity(se);
        CloseableHttpResponse responseEntry = null;
        String xmlStr2 = null;
        responseEntry = httpclient.execute(httppost);
        HttpEntity entity = responseEntry.getEntity();
        if (entity != null) {
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            while ((xmlStr2 = bufferedReader.readLine()) != null) {
                sb2.append(xmlStr2);
            }
        }
        Map<String, String> map = WxUtils.xmlToMap(sb2.toString());
        log.info("【微信退款接口返回的结果集：" + map+"】");
        String return_code = map.get("return_code");
        map4OTOSaaS.put("return_code",map.get("return_code"));
        map4OTOSaaS.put("transaction_id",map.get("transaction_id"));
        map4OTOSaaS.put("out_refund_no",map.get("out_refund_no"));
        map4OTOSaaS.put("refund_fee",map.get("refund_fee"));

        if (return_code.toString().equalsIgnoreCase("SUCCESS")
                && map.get("result_code").equalsIgnoreCase("SUCCESS")) {
            map4OTOSaaS.put("sign", BlmSignature.sign(WXConfig.OTOSSAAS_PRIVATE_KEY, BlmSignature.getText(map4OTOSaaS)));
            log.info("【****************退款申请成功！**********************】");

            otosaasOrderExample.clear();
            otosaasOrderExample.or().andOutTradeNoEqualTo(map.get("out_trade_no"));
            TblOtosaasOrder order = new TblOtosaasOrder();
            order.setPayStatus(2);
            order.setOutRefundNo(out_refund_no);
            order.setRefundDatetime(SeemseUtil.getCurrentTime());
            tblOtosaasOrderMapper.updateByExampleSelective(order, otosaasOrderExample);
            return map4OTOSaaS;
        } else {
            log.info("【*****************退款申请失败！*********************】");
            map4OTOSaaS.put("return_msg",map.get("return_msg"));
            map4OTOSaaS.put("sign", BlmSignature.sign(WXConfig.OTOSSAAS_PRIVATE_KEY, BlmSignature.getText(map4OTOSaaS)));
            return map4OTOSaaS;
        }
    }

    @Override
    public Map<String, Object> refund(Dto dto, HttpServletRequest request) throws Exception {
        String sign=dto.getString("sign");
        dto.remove("sign");
        Map<String,Object> map4OTOSaaS=new HashMap<String,Object>();
       boolean verify = BlmSignature.verify(WXConfig.OTOSSAAS_PUBLIC_KEY, BlmSignature.getText(dto), sign);
        if(!verify){
            map4OTOSaaS.put("return_code","failure");
            map4OTOSaaS.put("return_msg","解签失败");
            return map4OTOSaaS;
        }

        TblOtosaasOrderExample otosaasOrderExample = new TblOtosaasOrderExample();
        otosaasOrderExample.or().andTransactionIdEqualTo(dto.getString("transaction_id"));
        List<TblOtosaasOrder> otosaasOrderList = tblOtosaasOrderMapper.selectByExample(otosaasOrderExample);
        TblOtosaasOrder orderDetail = new TblOtosaasOrder();
        if(otosaasOrderList!=null && !otosaasOrderList.isEmpty())
            orderDetail = otosaasOrderList.get(0);
        if("0".equals(orderDetail.getPayType())){
            //微信公众号退款
          return refund4Wechat(dto,request);
        }else if("1".equals(orderDetail.getPayType())){
            //微信H5退款
            return null;
        }else if ("2".equals(orderDetail.getPayType())){
            //通联退款
            JSONObject json = new JSONObject();
            json.put("paramStr",dto);
            String result = HttpUtils.doPost1(PayModeConfig.ALLINPAY_REFUND_URL, json);
            JSONObject resultJson= JSONObject.fromObject(result);
            log.info("【通联退款接口：】");
            String code = resultJson.getString("code");
            JSONObject data = resultJson.getJSONObject("data");
            String refundOrderNo = data.getString("mchtRefundOrderNo");
            String refundAmount = data.getString("refundAmount");
            map4OTOSaaS.put("transaction_id",orderDetail.getTransactionId());
            map4OTOSaaS.put("out_refund_no",refundOrderNo);
            map4OTOSaaS.put("refund_fee",refundAmount);
            if("200".equals(code)) map4OTOSaaS.put("return_code","SUCCESS");
            else {
                map4OTOSaaS.put("return_code","FAIL");
                map4OTOSaaS.put("return_msg","退款失败！");
            }
           String sign4OTOSaaS = BlmSignature.sign(WXConfig.OTOSSAAS_PRIVATE_KEY,
                                                  BlmSignature.getText(map4OTOSaaS));
            map4OTOSaaS.put("sign",sign4OTOSaaS);
            return map4OTOSaaS;
        }else {
            return null;
        }

    }

}
