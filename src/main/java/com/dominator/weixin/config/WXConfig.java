package com.dominator.weixin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "wechat")
public class WXConfig {

    //开放消息校验Token和公开放平台消息加解密Key
    public static String WX_TOKEN;//("WX_TOKEN");
    public static String ENCODING_AESKEY;//("ENCODING_AESKEY");

    //授权回调域名
    public static String GRANT_DOMAIN_NAME;//("GRANT_DOMAIN_NAME");

    //三方授权
    public static String COMPONENT_TOKEN_URL;//("COMPONENT_TOKEN_URL");

    public static String WX_USER_INFO_URL;//("WX_USER_INFO_URL");   //微信用户
    public static String WEBPAGE_ACCESS_TOKEN_URL;//("WEBPAGE_ACCESS_TOKEN_URL");   //微信网页ACCESS_TOKEN

    //OTOSaaS支付回调
    public static String WEIXIN_PAY_NOTIFY_URL;//("WEIXIN_PAY_NOTIFY_URL");
    public static String ALL_IN_PAY_RESULT_NOTICE_URL;//("ALL_IN_PAY_RESULT_NOTICE_URL");
    public static String OTOSAAS_REFUND_NOTIFY_URL;//("OTOSAAS_REFUND_NOTIFY_URL");

    public static String COMP_PMSSAAS_TEST;//("COMP_PMSSAAS_TEST");  //开放平台（测试环境）
    public static String COMPSECRET_PMSSAAS_TEST;//("COMPSECRET_PMSSAAS_TEST");
    public static String COMP_PMSSAAS_PRO;//("COMP_PMSSAAS_PRO");   //开放平台（生产环境）
    public static String COMPSECRET_PMSSAAS_PRO;//("COMPSECRET_PMSSAAS_PRO");

    //OTOSSaaS对接公私钥
    public static String OTOSSAAS_PUBLIC_KEY;//("OTOSSAAS_PUBLIC_KEY");
    public static String OTOSSAAS_PRIVATE_KEY;//("OTOSSAAS_PRIVATE_KEY");

    //微信支付
    public static String WX_REFUND_URL; //退款
    public static String WX_PLACE_ORDER_URL;//("WX_PLACE_ORDER_URL");

    //联合登录
    public static String JOINT_LOGIN_APPKEY;//("JOINT_LOGIN_APPKEY");
    public static String JOINT_LOGIN_APPSECRET;//("JOINT_LOGIN_APPSECRET");
    public static String OTOSAAS_LOGIN_URL;//("OTOSAAS_LOGIN_URL");

    //微信相关
    public static String JSAPI_TOKEN_URL;//("JSAPI_TOKEN_URL");
    public static String JSAPI_TICKET_URL;//("JSAPI_TICKET_URL");

    public static String CREATE_PREAUTHCODE_URL;//("CREATE_PREAUTHCODE_URL");
    public static String GET_AUTHORIZER_OPTION_URL;//("GET_AUTHORIZER_OPTION_URL");
    public static String SET_AUTHORIZER_OPTION_URL;//("SET_AUTHORIZER_OPTION_URL");
    public static String GET_AUTHORIZER_INFO_URL;//("GET_AUTHORIZER_INFO_URL");
    public static String QUERY_AUTH_INFO_URL;//("QUERY_AUTH_INFO_URL");

    /**
     * 开放平台 AppID和AppSecret映射
     *
     * @param compApiId 开放平台appid
     * @return
     */
    public static String getApiSecret(String compApiId) {
        Map<String, String> map = new HashMap<>();
        map.put(COMP_PMSSAAS_TEST, COMPSECRET_PMSSAAS_TEST);
        map.put(COMP_PMSSAAS_PRO, COMPSECRET_PMSSAAS_PRO);
        return map.get(compApiId);
    }

    public void setWxToken(String wxToken) {
        WX_TOKEN = wxToken;
    }

    public void setEncodingAeskey(String encodingAeskey) {
        ENCODING_AESKEY = encodingAeskey;
    }

    public void setGrantDomainName(String grantDomainName) {
        GRANT_DOMAIN_NAME = grantDomainName;
    }

    public void setComponentTokenUrl(String componentTokenUrl) {
        COMPONENT_TOKEN_URL = componentTokenUrl;
    }

    public void setWxUserInfoUrl(String wxUserInfoUrl) {
        WX_USER_INFO_URL = wxUserInfoUrl;
    }

    public void setWebpageAccessTokenUrl(String webpageAccessTokenUrl) {
        WEBPAGE_ACCESS_TOKEN_URL = webpageAccessTokenUrl;
    }

    public void setWeixinPayNotifyUrl(String weixinPayNotifyUrl) {
        WEIXIN_PAY_NOTIFY_URL = weixinPayNotifyUrl;
    }

    public void setAllInPayResultNoticeUrl(String allInPayResultNoticeUrl) {
        ALL_IN_PAY_RESULT_NOTICE_URL = allInPayResultNoticeUrl;
    }

    public void setOtosaasRefundNotifyUrl(String otosaasRefundNotifyUrl) {
        OTOSAAS_REFUND_NOTIFY_URL = otosaasRefundNotifyUrl;
    }

    public void setCompPmssaasTest(String compPmssaasTest) {
        COMP_PMSSAAS_TEST = compPmssaasTest;
    }

    public void setCompsecretPmssaasTest(String compsecretPmssaasTest) {
        COMPSECRET_PMSSAAS_TEST = compsecretPmssaasTest;
    }

    public void setCompPmssaasPro(String compPmssaasPro) {
        COMP_PMSSAAS_PRO = compPmssaasPro;
    }

    public void setCompsecretPmssaasPro(String compsecretPmssaasPro) {
        COMPSECRET_PMSSAAS_PRO = compsecretPmssaasPro;
    }

    public void setOtossaasPublicKey(String otossaasPublicKey) {
        OTOSSAAS_PUBLIC_KEY = otossaasPublicKey;
    }

    public void setOtossaasPrivateKey(String otossaasPrivateKey) {
        OTOSSAAS_PRIVATE_KEY = otossaasPrivateKey;
    }

    public void setWxRefundUrl(String wxRefundUrl) {
        WX_REFUND_URL = wxRefundUrl;
    }

    public void setWxPlaceOrderUrl(String wxPlaceOrderUrl) {
        WX_PLACE_ORDER_URL = wxPlaceOrderUrl;
    }

    public void setJointLoginAppkey(String jointLoginAppkey) {
        JOINT_LOGIN_APPKEY = jointLoginAppkey;
    }

    public void setJointLoginAppsecret(String jointLoginAppsecret) {
        JOINT_LOGIN_APPSECRET = jointLoginAppsecret;
    }

    public void setOtosaasLoginUrl(String otosaasLoginUrl) {
        OTOSAAS_LOGIN_URL = otosaasLoginUrl;
    }

    public void setJsapiTokenUrl(String jsapiTokenUrl) {
        JSAPI_TOKEN_URL = jsapiTokenUrl;
    }

    public void setJsapiTicketUrl(String jsapiTicketUrl) {
        JSAPI_TICKET_URL = jsapiTicketUrl;
    }

    public void setCreatePreauthcodeUrl(String createPreauthcodeUrl) {
        CREATE_PREAUTHCODE_URL = createPreauthcodeUrl;
    }

    public void setGetAuthorizerOptionUrl(String getAuthorizerOptionUrl) {
        GET_AUTHORIZER_OPTION_URL = getAuthorizerOptionUrl;
    }

    public void setSetAuthorizerOptionUrl(String setAuthorizerOptionUrl) {
        SET_AUTHORIZER_OPTION_URL = setAuthorizerOptionUrl;
    }

    public void setGetAuthorizerInfoUrl(String getAuthorizerInfoUrl) {
        GET_AUTHORIZER_INFO_URL = getAuthorizerInfoUrl;
    }

    public void setQueryAuthInfoUrl(String queryAuthInfoUrl) {
        QUERY_AUTH_INFO_URL = queryAuthInfoUrl;
    }
}
