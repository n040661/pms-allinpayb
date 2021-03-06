package com.dominator.mapper;

import com.dominator.entity.TSms;
import com.dominator.entity.TSmsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TSmsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TSmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TSmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TSms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TSms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TSms> selectByExample(TSmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TSms selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TSms record, @Param("example") TSmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TSms record, @Param("example") TSmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TSms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sms
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TSms record);
}