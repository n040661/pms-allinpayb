package com.dominator.entity;

import java.util.Date;

public class TAddress {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.owner_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String ownerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.province
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String province;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String city;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.area
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String area;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.street
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String street;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_address.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String isValid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.id
     *
     * @return the value of t_address.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.id
     *
     * @param id the value for t_address.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.owner_id
     *
     * @return the value of t_address.owner_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.owner_id
     *
     * @param ownerId the value for t_address.owner_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId == null ? null : ownerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.type
     *
     * @return the value of t_address.type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.type
     *
     * @param type the value for t_address.type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.province
     *
     * @return the value of t_address.province
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.province
     *
     * @param province the value for t_address.province
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.city
     *
     * @return the value of t_address.city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.city
     *
     * @param city the value for t_address.city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.area
     *
     * @return the value of t_address.area
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getArea() {
        return area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.area
     *
     * @param area the value for t_address.area
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.street
     *
     * @return the value of t_address.street
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getStreet() {
        return street;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.street
     *
     * @param street the value for t_address.street
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.create_time
     *
     * @return the value of t_address.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.create_time
     *
     * @param createTime the value for t_address.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_address.is_valid
     *
     * @return the value of t_address.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_address.is_valid
     *
     * @param isValid the value for t_address.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }
}