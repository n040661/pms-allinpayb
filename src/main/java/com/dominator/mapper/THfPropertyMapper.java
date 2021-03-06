package com.dominator.mapper;

import com.dominator.entity.THfProperty;
import com.dominator.entity.THfPropertyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface THfPropertyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(THfPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(THfPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(THfProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(THfProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<THfProperty> selectByExample(THfPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    THfProperty selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") THfProperty record, @Param("example") THfPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") THfProperty record, @Param("example") THfPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(THfProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(THfProperty record);
}