package com.dominator.AAAconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsuliang on 2018/4/28.
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

    public static String APP_ID;

    public static String URL;

    public static String APP_PRIVATE_KEY;

    public static String ALIPAY_PUBLIC_KEY;

}
