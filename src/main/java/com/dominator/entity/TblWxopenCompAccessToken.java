package com.dominator.entity;

public class TblWxopenCompAccessToken {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_comp_access_token.id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_comp_access_token.componet_access_token
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private String componetAccessToken;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_comp_access_token.create_time
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private Long createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_wxopen_comp_access_token.expires_in
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    private Long expiresIn;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_comp_access_token.id
     *
     * @return the value of tbl_wxopen_comp_access_token.id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_comp_access_token.id
     *
     * @param id the value for tbl_wxopen_comp_access_token.id
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_comp_access_token.componet_access_token
     *
     * @return the value of tbl_wxopen_comp_access_token.componet_access_token
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public String getComponetAccessToken() {
        return componetAccessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_comp_access_token.componet_access_token
     *
     * @param componetAccessToken the value for tbl_wxopen_comp_access_token.componet_access_token
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setComponetAccessToken(String componetAccessToken) {
        this.componetAccessToken = componetAccessToken == null ? null : componetAccessToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_comp_access_token.create_time
     *
     * @return the value of tbl_wxopen_comp_access_token.create_time
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_comp_access_token.create_time
     *
     * @param createTime the value for tbl_wxopen_comp_access_token.create_time
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_wxopen_comp_access_token.expires_in
     *
     * @return the value of tbl_wxopen_comp_access_token.expires_in
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_wxopen_comp_access_token.expires_in
     *
     * @param expiresIn the value for tbl_wxopen_comp_access_token.expires_in
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}