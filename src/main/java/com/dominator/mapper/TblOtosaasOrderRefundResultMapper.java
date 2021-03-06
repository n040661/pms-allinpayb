package com.dominator.mapper;

import com.dominator.entity.TblOtosaasOrderRefundResult;
import com.dominator.entity.TblOtosaasOrderRefundResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblOtosaasOrderRefundResultMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int countByExample(TblOtosaasOrderRefundResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByExample(TblOtosaasOrderRefundResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insert(TblOtosaasOrderRefundResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insertSelective(TblOtosaasOrderRefundResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    List<TblOtosaasOrderRefundResult> selectByExample(TblOtosaasOrderRefundResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    TblOtosaasOrderRefundResult selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExampleSelective(@Param("record") TblOtosaasOrderRefundResult record, @Param("example") TblOtosaasOrderRefundResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExample(@Param("record") TblOtosaasOrderRefundResult record, @Param("example") TblOtosaasOrderRefundResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKeySelective(TblOtosaasOrderRefundResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order_refund_result
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKey(TblOtosaasOrderRefundResult record);
}