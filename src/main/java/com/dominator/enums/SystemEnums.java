package com.dominator.enums;

public enum SystemEnums {


    // 利用构造函数传参
    MAIL_RESET_TITLE("找回密码");

    // 定义私有变量
    private String name;

    // 构造函数，枚举类型只能为私有
    private SystemEnums(String _name) {

        this.name = _name;

    }


}
