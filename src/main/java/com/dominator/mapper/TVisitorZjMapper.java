package com.dominator.mapper;

import com.dominator.entity.TVisitorZj;
import com.dominator.entity.TVisitorZjExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TVisitorZjMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TVisitorZjExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TVisitorZjExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TVisitorZj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TVisitorZj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TVisitorZj> selectByExample(TVisitorZjExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TVisitorZj selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TVisitorZj record, @Param("example") TVisitorZjExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TVisitorZj record, @Param("example") TVisitorZjExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TVisitorZj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_visitor_zj
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TVisitorZj record);
}