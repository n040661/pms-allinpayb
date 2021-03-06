package com.dominator.entity;

import java.util.Date;

public class TConsultAppointment {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.consult_id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private String consultId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.phone_num
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private String phoneNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.name
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.create_time
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.modify_time
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.modify_id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private String modifyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_consult_appointment.is_valid
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    private String isValid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.id
     *
     * @return the value of t_consult_appointment.id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.id
     *
     * @param id the value for t_consult_appointment.id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.consult_id
     *
     * @return the value of t_consult_appointment.consult_id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public String getConsultId() {
        return consultId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.consult_id
     *
     * @param consultId the value for t_consult_appointment.consult_id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setConsultId(String consultId) {
        this.consultId = consultId == null ? null : consultId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.phone_num
     *
     * @return the value of t_consult_appointment.phone_num
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.phone_num
     *
     * @param phoneNum the value for t_consult_appointment.phone_num
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.name
     *
     * @return the value of t_consult_appointment.name
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.name
     *
     * @param name the value for t_consult_appointment.name
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.create_time
     *
     * @return the value of t_consult_appointment.create_time
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.create_time
     *
     * @param createTime the value for t_consult_appointment.create_time
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.modify_time
     *
     * @return the value of t_consult_appointment.modify_time
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.modify_time
     *
     * @param modifyTime the value for t_consult_appointment.modify_time
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.modify_id
     *
     * @return the value of t_consult_appointment.modify_id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public String getModifyId() {
        return modifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.modify_id
     *
     * @param modifyId the value for t_consult_appointment.modify_id
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_consult_appointment.is_valid
     *
     * @return the value of t_consult_appointment.is_valid
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_consult_appointment.is_valid
     *
     * @param isValid the value for t_consult_appointment.is_valid
     *
     * @mbggenerated Mon May 07 15:17:48 CST 2018
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }
}