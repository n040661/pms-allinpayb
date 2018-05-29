package com.dominator.enums;

public enum MessageEnums {

    PARAM_MISS("缺少参数"),
    USER_MISS("用户不存在"),
    GARDEN_USER_MISS("园区用户不存在"),
    GARDEN_USER_ROLE_MISS("园区用户角色不存在"),
    USER_NOT_VALID("用户已被禁用,请联系管理员"),
    VERIFY_ERROR("验证码错误"),
    VERIFY_EXPIRED("验证码已过期"),
    EXCEL_NULL("表格上传内容为空"),
    WRONG_DATEFORMAT("错误日期格式"),
    WRONG_ROLE_NAME("权限有误"),
    REPEAT_ID_CARD("身份证号已在其他公司注册"),
    COMPANY_NOT_EXIST("不存在"),
    BILL_PUSHED("账单已推送")
    ;


    /**
     * msg
     */
    private String msg;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private MessageEnums(String msg) {
        this.msg = msg;
    }
}
