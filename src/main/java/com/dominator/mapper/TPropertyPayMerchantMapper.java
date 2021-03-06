package com.dominator.mapper;

import com.dominator.entity.TPropertyPayMerchant;
import com.dominator.entity.TPropertyPayMerchantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TPropertyPayMerchantMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TPropertyPayMerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TPropertyPayMerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TPropertyPayMerchant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TPropertyPayMerchant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TPropertyPayMerchant> selectByExample(TPropertyPayMerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TPropertyPayMerchant selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TPropertyPayMerchant record, @Param("example") TPropertyPayMerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TPropertyPayMerchant record, @Param("example") TPropertyPayMerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TPropertyPayMerchant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property_pay_merchant
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TPropertyPayMerchant record);
}