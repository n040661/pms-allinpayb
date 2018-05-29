package com.dominator.weixin.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payMode")
public class PayModeConfig {

    public static String ALLINPAY_PLACE_ORDER;

    public static String ALLINPAY_REFUND_URL;

    public void setAllinpayPlaceOrder(String allinpayPlaceOrder) {
        ALLINPAY_PLACE_ORDER = allinpayPlaceOrder;
    }

    public void setAllinpayRefundUrl(String allinpayRefundUrl) {
        ALLINPAY_REFUND_URL = allinpayRefundUrl;
    }
}
