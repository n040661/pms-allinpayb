package com.dominator.mapper;

import com.dominator.entity.TMsg;
import com.dominator.entity.TMsgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TMsgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TMsg> selectByExample(TMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TMsg selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TMsg record, @Param("example") TMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TMsg record, @Param("example") TMsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TMsg record);
}