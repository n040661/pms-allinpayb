package com.dominator.weixin.domain;

public class TemplateMsg {

    public final static String APPID="appi_id";

    public final static String TYPE="type";

    public final static String TOUSER="touser";

    public final static String TEMPLATE_ID="template_id";

    public final static String VALUE="value";

    public final static String COLOR="color";

    public final static String COLOR_VALUE="#173177";

    public final static String URL="url";

    public final static String FIRST="first";

    public final static String KEYWORD1="keyword1";

    public final static String KEYWORD2="keyword2";

    public final static String KEYWORD3="keyword3";

    public final static String KEYWORD4="keyword4";

    public final static String KEYWORD5="keyword5";

    public final static String KEYWORD6="keyword6";

    public final static String KEYWORD7="keyword7";

    public final static String DATA="data";

    /**
     * 公众号ID
     */
    private String appid;

    /**
     *  用户标识openid
     */
    private String touser;

    /**
     * 模版类型
     */
    private String type;

    /**
     * 模版消息头部信息
     */
    private String first;

    /**
     * 自定义字段
     */
    private String keyword1;

    /**
     * 自定义字段
     */
    private String keyword2;

    /**
     * 自定义字段
     */
    private String keyword3;

    private String keyword4;

    private String keyword5;

    private String keyword6;

    private String keyword7;

    private String url;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    public String getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }

    public String getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(String keyword4) {
        this.keyword4 = keyword4;
    }

    public String getKeyword5() {
        return keyword5;
    }

    public void setKeyword5(String keyword5) {
        this.keyword5 = keyword5;
    }

    public String getKeyword6() {
        return keyword6;
    }

    public void setKeyword6(String keyword6) {
        this.keyword6 = keyword6;
    }

    public String getKeyword7() {
        return keyword7;
    }

    public void setKeyword7(String keyword7) {
        this.keyword7 = keyword7;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
