package com.dominator.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TCompanyBill {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.company_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String companyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.garden_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String gardenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.bill_num
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String billNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.bill_year_month
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String billYearMonth;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.total_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private BigDecimal totalFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.main_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private BigDecimal mainFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.other_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private BigDecimal otherFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.fee_unit
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private BigDecimal feeUnit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.fee_dgree
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private BigDecimal feeDgree;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.fee_type
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String feeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.is_pushed
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String isPushed;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.push_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private Date pushTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.expiry_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private Date expiryTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.is_paid
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String isPaid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.pay_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private Date payTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.modify_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.create_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.modify_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String modifyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_bill.is_valid
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    private String isValid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.id
     *
     * @return the value of t_company_bill.id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.id
     *
     * @param id the value for t_company_bill.id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.company_id
     *
     * @return the value of t_company_bill.company_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.company_id
     *
     * @param companyId the value for t_company_bill.company_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.garden_id
     *
     * @return the value of t_company_bill.garden_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getGardenId() {
        return gardenId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.garden_id
     *
     * @param gardenId the value for t_company_bill.garden_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setGardenId(String gardenId) {
        this.gardenId = gardenId == null ? null : gardenId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.bill_num
     *
     * @return the value of t_company_bill.bill_num
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getBillNum() {
        return billNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.bill_num
     *
     * @param billNum the value for t_company_bill.bill_num
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setBillNum(String billNum) {
        this.billNum = billNum == null ? null : billNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.bill_year_month
     *
     * @return the value of t_company_bill.bill_year_month
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getBillYearMonth() {
        return billYearMonth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.bill_year_month
     *
     * @param billYearMonth the value for t_company_bill.bill_year_month
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setBillYearMonth(String billYearMonth) {
        this.billYearMonth = billYearMonth == null ? null : billYearMonth.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.total_fee
     *
     * @return the value of t_company_bill.total_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.total_fee
     *
     * @param totalFee the value for t_company_bill.total_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.main_fee
     *
     * @return the value of t_company_bill.main_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public BigDecimal getMainFee() {
        return mainFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.main_fee
     *
     * @param mainFee the value for t_company_bill.main_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setMainFee(BigDecimal mainFee) {
        this.mainFee = mainFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.other_fee
     *
     * @return the value of t_company_bill.other_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public BigDecimal getOtherFee() {
        return otherFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.other_fee
     *
     * @param otherFee the value for t_company_bill.other_fee
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.fee_unit
     *
     * @return the value of t_company_bill.fee_unit
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public BigDecimal getFeeUnit() {
        return feeUnit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.fee_unit
     *
     * @param feeUnit the value for t_company_bill.fee_unit
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setFeeUnit(BigDecimal feeUnit) {
        this.feeUnit = feeUnit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.fee_dgree
     *
     * @return the value of t_company_bill.fee_dgree
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public BigDecimal getFeeDgree() {
        return feeDgree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.fee_dgree
     *
     * @param feeDgree the value for t_company_bill.fee_dgree
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setFeeDgree(BigDecimal feeDgree) {
        this.feeDgree = feeDgree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.fee_type
     *
     * @return the value of t_company_bill.fee_type
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.fee_type
     *
     * @param feeType the value for t_company_bill.fee_type
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.is_pushed
     *
     * @return the value of t_company_bill.is_pushed
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getIsPushed() {
        return isPushed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.is_pushed
     *
     * @param isPushed the value for t_company_bill.is_pushed
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setIsPushed(String isPushed) {
        this.isPushed = isPushed == null ? null : isPushed.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.push_time
     *
     * @return the value of t_company_bill.push_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public Date getPushTime() {
        return pushTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.push_time
     *
     * @param pushTime the value for t_company_bill.push_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.expiry_time
     *
     * @return the value of t_company_bill.expiry_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public Date getExpiryTime() {
        return expiryTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.expiry_time
     *
     * @param expiryTime the value for t_company_bill.expiry_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.is_paid
     *
     * @return the value of t_company_bill.is_paid
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getIsPaid() {
        return isPaid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.is_paid
     *
     * @param isPaid the value for t_company_bill.is_paid
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid == null ? null : isPaid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.pay_time
     *
     * @return the value of t_company_bill.pay_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.pay_time
     *
     * @param payTime the value for t_company_bill.pay_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.modify_time
     *
     * @return the value of t_company_bill.modify_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.modify_time
     *
     * @param modifyTime the value for t_company_bill.modify_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.create_time
     *
     * @return the value of t_company_bill.create_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.create_time
     *
     * @param createTime the value for t_company_bill.create_time
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.modify_id
     *
     * @return the value of t_company_bill.modify_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getModifyId() {
        return modifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.modify_id
     *
     * @param modifyId the value for t_company_bill.modify_id
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_bill.is_valid
     *
     * @return the value of t_company_bill.is_valid
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_bill.is_valid
     *
     * @param isValid the value for t_company_bill.is_valid
     *
     * @mbggenerated Tue Apr 10 16:28:42 CST 2018
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }
}