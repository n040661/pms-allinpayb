package com.dominator.enums;


public enum ReqEnums {

    SYS_ERROR("400", "系统异常"),
    REQ_SUCCESS("200", "请求成功"),
    REQ_INCORRECT_PHONE_NUM("101", "手机号不正确"),
    REQ_PARAM_MISS("102", "请求参数缺少"),
    REQ_TOKEN_FALSE("103", "请求过期"),
    REQ_INCORRECT_CODE("104", "短信验证码错误"),
    REQ_PARAM_ERROR("105", "请求参数错误"),
    REQ_RESULT_NULL("106", "请求结果为空"),
    REQ_ORDER_ERROR("107", "订单生成失败"),
    REQ_TRADE_ERROR("109", "交易失败"),
    REQ_UPDATE_ERROR("110", "保存或更新失败"),
    REQ_EMAIL_ERROR("111", "发送邮件失败"),
    REQ_MONTH_BILL_ERROR("112", "该月份暂无账单"),
    REQ_USER_NOT_EXSIT("113", "用户名不存在，请先注册"),
    REQ_WRONG_PASSWORD("114", "密码错误"),
    REQ_NOT_COMPANY_USER("115", "非企业用户"),
    REQ_NOT_PASS_CODE("116", "该通行码不存在"),
    REQ_NOT_QR_CODE("117", "二维码解析错误"),
    REQ_NOT_GARDEN_USER("118", "非园区用户"),
    REQ_USER_REPEAT("119", "用户已存在"),
    REQ_NOT_GARDEN_MANAGER("120", "非园区管理员"),
    REQ_NO_EMAIL("121", "缺少邮箱地址"),
    REQ_NO_PASSWORD("122", "缺少密码"),
    REQ_OTO_LOGIN_ERROR("123", "暂无相关权限，请联系上级管理员"),
    REQ_LAST_PASS_CODE("124", "通行证已过期,无法使用"),
    REQ_TIMES_PASS_CODE("125", "使用次数已达上线"),
    REQ_NOTFOR_PASS_CODE("126", "未到预约日期,通行证暂时无法使用"),
    REQ_OTOSAAS_ORDER_LIMITED("127", "服务器被挤爆啦，请明日再来"),
    REQ_HF_USERPAY_ERROR("128", "汇付开户异常"),
    REQ_HF_ADD_SIGN("129", "汇付加密异常"),
    REQ_POST_ERROR("130", "httpclient请求异常"),
    REQ_REAULT_ERROR("131", "请求结果异常"),
    REQ_DEVICE_REPEAT("132", "设备已存在"),
    REQ_DEVICE_UPDATE_FAILED("133", "修改设备状态失败"),
    REQ_COMPANY_NOT_EXIST_IN_GARDEN("134", "不在当前园区中"),
    REQ_PHONE_OR_PASSWORD_ERROR("135", "号码或密码错误,请重新输入"),
    REQ_ERORR_MSG_LIST("136", "错误提示："),
    REQ_DEVICE_NOT_EXIST_REPEAT("137", "该园区不存在该设备号"),
    REQ_DEVICE_NOT_HAVE_REPEAT("138", "该园区无设备"),
    REQ_NO_USER("139", "用户不存在,请先注册"),
    REQ_USER_INFO_NOT_MATCH("140", "姓名和手机号不匹配"),
    REQ_IMPORT_ERROR("141", "导入失败"),
    REQ_MSG_EXIST_INVALID("142", "已存在,且已失效"),
    REQ_RESOURCE_NOT_INVALID("143","请求资源不在"),
    REQ_REPEAT_COMPANY("144","企业名称已存在"),
    REQ_REPEAT_GARDEN("145","园区名称已存在"),
    REQ_REPEAT_PROPERTY("146","物业名称已存在"),
    REQ_REPEAT_ROLE("147","角色名称已存在"),
    REQ_TOKEN_DUO("148","账号在其他设备登录"),
    REQ_NO_RIGHT_FOR_MANAGER("149","管理员不能被删除"),
    VERIFY_SIGN_FAIL("150","验签失败"),
    NOT_ALIPAY_CALLBACK("151","非支付宝回调信息")
    ;

    /**
     * code
     */
    private String code;
    /**
     * msg
     */
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ReqEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
