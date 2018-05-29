package com.dominator.weixin.util;

import java.util.HashMap;
import java.util.Map;

public class OTOOrderReflection {

    public static String orderReflection(String appid){
        //map中的value值为字符串（10位以内）
        Map<String,String> map=new HashMap<>();
        map.put("wx7dbda27aa5a445ed","PMS");
        map.put("wx40658779aea637b8","PMS-BXR");
        map.put("","");
        map.put("","");
        map.put("","");
        map.put("","");
        return map.get(appid);
    }
}
