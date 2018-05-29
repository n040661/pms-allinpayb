package com.dominator.mapper;

import com.dominator.entity.THfProvinceCity;
import com.dominator.entity.THfProvinceCityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface THfProvinceCityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(THfProvinceCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(THfProvinceCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(THfProvinceCity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(THfProvinceCity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<THfProvinceCity> selectByExample(THfProvinceCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    THfProvinceCity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") THfProvinceCity record, @Param("example") THfProvinceCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") THfProvinceCity record, @Param("example") THfProvinceCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(THfProvinceCity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_hf_province_city
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(THfProvinceCity record);
}