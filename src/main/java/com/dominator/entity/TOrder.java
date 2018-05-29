package com.dominator.entity;

import java.util.Date;

public class TOrder {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.order_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.bill_num
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String billNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.out_bill_cust
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String outBillCust;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.in_bill_cust
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String inBillCust;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.pee
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String pee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String payType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.pay_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private Date payTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.status
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.modify_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.modify_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String modifyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    private String isValid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.id
     *
     * @return the value of t_order.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.id
     *
     * @param id the value for t_order.id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.order_id
     *
     * @return the value of t_order.order_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.order_id
     *
     * @param orderId the value for t_order.order_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.bill_num
     *
     * @return the value of t_order.bill_num
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getBillNum() {
        return billNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.bill_num
     *
     * @param billNum the value for t_order.bill_num
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setBillNum(String billNum) {
        this.billNum = billNum == null ? null : billNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.out_bill_cust
     *
     * @return the value of t_order.out_bill_cust
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getOutBillCust() {
        return outBillCust;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.out_bill_cust
     *
     * @param outBillCust the value for t_order.out_bill_cust
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setOutBillCust(String outBillCust) {
        this.outBillCust = outBillCust == null ? null : outBillCust.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.in_bill_cust
     *
     * @return the value of t_order.in_bill_cust
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getInBillCust() {
        return inBillCust;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.in_bill_cust
     *
     * @param inBillCust the value for t_order.in_bill_cust
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setInBillCust(String inBillCust) {
        this.inBillCust = inBillCust == null ? null : inBillCust.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.pee
     *
     * @return the value of t_order.pee
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getPee() {
        return pee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.pee
     *
     * @param pee the value for t_order.pee
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setPee(String pee) {
        this.pee = pee == null ? null : pee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.pay_type
     *
     * @return the value of t_order.pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getPayType() {
        return payType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.pay_type
     *
     * @param payType the value for t_order.pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.pay_time
     *
     * @return the value of t_order.pay_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.pay_time
     *
     * @param payTime the value for t_order.pay_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.status
     *
     * @return the value of t_order.status
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.status
     *
     * @param status the value for t_order.status
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.modify_time
     *
     * @return the value of t_order.modify_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.modify_time
     *
     * @param modifyTime the value for t_order.modify_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.create_time
     *
     * @return the value of t_order.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.create_time
     *
     * @param createTime the value for t_order.create_time
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.modify_id
     *
     * @return the value of t_order.modify_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getModifyId() {
        return modifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.modify_id
     *
     * @param modifyId the value for t_order.modify_id
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order.is_valid
     *
     * @return the value of t_order.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order.is_valid
     *
     * @param isValid the value for t_order.is_valid
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }
}