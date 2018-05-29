package com.dominator.mapper;

import com.dominator.entity.TblOtosaasOrder;
import com.dominator.entity.TblOtosaasOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblOtosaasOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int countByExample(TblOtosaasOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int deleteByExample(TblOtosaasOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int insert(TblOtosaasOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int insertSelective(TblOtosaasOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    List<TblOtosaasOrder> selectByExample(TblOtosaasOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    TblOtosaasOrder selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int updateByExampleSelective(@Param("record") TblOtosaasOrder record, @Param("example") TblOtosaasOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int updateByExample(@Param("record") TblOtosaasOrder record, @Param("example") TblOtosaasOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int updateByPrimaryKeySelective(TblOtosaasOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_otosaas_order
     *
     * @mbggenerated Fri May 04 18:16:34 CST 2018
     */
    int updateByPrimaryKey(TblOtosaasOrder record);
}