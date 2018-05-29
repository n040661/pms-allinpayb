package com.dominator.AAAconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations={"classpath:provider.xml"})
@Slf4j
public class DubboProviderConfig {

    DubboProviderConfig(){
        log.info("pms-core provider start");
    }
}