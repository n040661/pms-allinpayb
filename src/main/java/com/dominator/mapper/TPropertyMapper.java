package com.dominator.mapper;

import com.dominator.entity.TProperty;
import com.dominator.entity.TPropertyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TPropertyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TProperty> selectByExample(TPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TProperty selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TProperty record, @Param("example") TPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TProperty record, @Param("example") TPropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_property
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TProperty record);
}