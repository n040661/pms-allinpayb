package com.dominator.mapper;

import com.dominator.entity.VCompanyBill;
import com.dominator.entity.VCompanyBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VCompanyBillMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int countByExample(VCompanyBillExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByExample(VCompanyBillExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insert(VCompanyBill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insertSelective(VCompanyBill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    List<VCompanyBill> selectByExample(VCompanyBillExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExampleSelective(@Param("record") VCompanyBill record, @Param("example") VCompanyBillExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_company_bill
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExample(@Param("record") VCompanyBill record, @Param("example") VCompanyBillExample example);
}