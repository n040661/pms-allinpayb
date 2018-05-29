package com.dominator.mapper;

import com.dominator.entity.TblOtosaasOrderPayResult;
import com.dominator.entity.TblOtosaasOrderPayResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblOtosaasOrderPayResultMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int countByExample(TblOtosaasOrderPayResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByExample(TblOtosaasOrderPayResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insert(TblOtosaasOrderPayResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insertSelective(TblOtosaasOrderPayResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    List<TblOtosaasOrderPayResult> selectByExample(TblOtosaasOrderPayResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    TblOtosaasOrderPayResult selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExampleSelective(@Param("record") TblOtosaasOrderPayResult record, @Param("example") TblOtosaasOrderPayResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExample(@Param("record") TblOtosaasOrderPayResult record, @Param("example") TblOtosaasOrderPayResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKeySelective(TblOtosaasOrderPayResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_pay_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKey(TblOtosaasOrderPayResult record);
}