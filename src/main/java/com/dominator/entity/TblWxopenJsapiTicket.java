package com.dominator.entity;

public class TblWxopenJsapiTicket {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_jsapi_ticket.id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_jsapi_ticket.app_id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private String appId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_jsapi_ticket.access_token
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private String accessToken;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_jsapi_ticket.expires_in
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private Integer expiresIn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_jsapi_ticket.create_time
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private Long createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_jsapi_ticket.id
     *
     * @return the value of tbl_wxopen_jsapi_ticket.id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_jsapi_ticket.id
     *
     * @param id the value for tbl_wxopen_jsapi_ticket.id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_jsapi_ticket.app_id
     *
     * @return the value of tbl_wxopen_jsapi_ticket.app_id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public String getAppId() {
        return appId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_jsapi_ticket.app_id
     *
     * @param appId the value for tbl_wxopen_jsapi_ticket.app_id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_jsapi_ticket.access_token
     *
     * @return the value of tbl_wxopen_jsapi_ticket.access_token
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_jsapi_ticket.access_token
     *
     * @param accessToken the value for tbl_wxopen_jsapi_ticket.access_token
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_jsapi_ticket.expires_in
     *
     * @return the value of tbl_wxopen_jsapi_ticket.expires_in
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Integer getExpiresIn() {
        return expiresIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_jsapi_ticket.expires_in
     *
     * @param expiresIn the value for tbl_wxopen_jsapi_ticket.expires_in
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_jsapi_ticket.create_time
     *
     * @return the value of tbl_wxopen_jsapi_ticket.create_time
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_jsapi_ticket.create_time
     *
     * @param createTime the value for tbl_wxopen_jsapi_ticket.create_time
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}