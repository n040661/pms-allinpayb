package com.dominator.entity;

import java.util.Date;

public class TApiLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.property_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String propertyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.garden_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String gardenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.garden_admin_user_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String gardenAdminUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.user_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.api_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String apiId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.params
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String params;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.is_success
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String isSuccess;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.modify_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.modify_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String modifyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_api_log.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String isValid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.id
     *
     * @return the value of t_api_log.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.id
     *
     * @param id the value for t_api_log.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.property_id
     *
     * @return the value of t_api_log.property_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.property_id
     *
     * @param propertyId the value for t_api_log.property_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId == null ? null : propertyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.garden_id
     *
     * @return the value of t_api_log.garden_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getGardenId() {
        return gardenId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.garden_id
     *
     * @param gardenId the value for t_api_log.garden_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setGardenId(String gardenId) {
        this.gardenId = gardenId == null ? null : gardenId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.garden_admin_user_id
     *
     * @return the value of t_api_log.garden_admin_user_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getGardenAdminUserId() {
        return gardenAdminUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.garden_admin_user_id
     *
     * @param gardenAdminUserId the value for t_api_log.garden_admin_user_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setGardenAdminUserId(String gardenAdminUserId) {
        this.gardenAdminUserId = gardenAdminUserId == null ? null : gardenAdminUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.user_id
     *
     * @return the value of t_api_log.user_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.user_id
     *
     * @param userId the value for t_api_log.user_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.api_id
     *
     * @return the value of t_api_log.api_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getApiId() {
        return apiId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.api_id
     *
     * @param apiId the value for t_api_log.api_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setApiId(String apiId) {
        this.apiId = apiId == null ? null : apiId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.params
     *
     * @return the value of t_api_log.params
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getParams() {
        return params;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.params
     *
     * @param params the value for t_api_log.params
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.is_success
     *
     * @return the value of t_api_log.is_success
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getIsSuccess() {
        return isSuccess;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.is_success
     *
     * @param isSuccess the value for t_api_log.is_success
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess == null ? null : isSuccess.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.modify_time
     *
     * @return the value of t_api_log.modify_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.modify_time
     *
     * @param modifyTime the value for t_api_log.modify_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.create_time
     *
     * @return the value of t_api_log.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.create_time
     *
     * @param createTime the value for t_api_log.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.modify_id
     *
     * @return the value of t_api_log.modify_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getModifyId() {
        return modifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.modify_id
     *
     * @param modifyId the value for t_api_log.modify_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_api_log.is_valid
     *
     * @return the value of t_api_log.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_api_log.is_valid
     *
     * @param isValid the value for t_api_log.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }
}