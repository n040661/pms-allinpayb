package com.dominator.utils.api;

import com.dominator.enums.ReqEnums;
import com.dominator.utils.encode.Des3Utils;

/**
 * 返回结果集工具类
 */
public class ApiMessageUtil {

    //请求成功， 有返回值
    public static ApiMessage success(Object object){
        if (object == null){
            return new ApiMessage(ReqEnums.REQ_SUCCESS.getCode(), ReqEnums.REQ_SUCCESS.getMsg(), "");
        }
        return new ApiMessage(ReqEnums.REQ_SUCCESS.getCode(), ReqEnums.REQ_SUCCESS.getMsg(), Des3Utils.encResponse(object));
    }

    //请求成功， 有返回值
    public static ApiMessage success(){
        return new ApiMessage(ReqEnums.REQ_SUCCESS.getCode(), ReqEnums.REQ_SUCCESS.getMsg(), "");
    }

    //请求失败，非系统异常
    public static ApiMessage failure(String s1, String s2){
        return new ApiMessage(s1, s2, null);
    }

    //请求失败，非系统异常
    public static ApiMessage failure(ReqEnums enums){
        return new ApiMessage(enums.getCode(), enums.getMsg(), null);
    }

    //请求失败，系统异常
    public static ApiMessage failure(){
        return new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg(), null);
    }
}
