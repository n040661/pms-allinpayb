package com.dominator.enums;

public enum OToSaaSOrderEnum {


    ORDER_NO_EXIST( "订单不存在");



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

    private OToSaaSOrderEnum( String msg) {
        this.msg = msg;
    }
}
