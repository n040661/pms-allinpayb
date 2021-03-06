package com.dominator.mapper;

import com.dominator.entity.TMsgSend;
import com.dominator.entity.TMsgSendExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TMsgSendMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TMsgSendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TMsgSendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TMsgSend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TMsgSend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TMsgSend> selectByExample(TMsgSendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TMsgSend selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TMsgSend record, @Param("example") TMsgSendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TMsgSend record, @Param("example") TMsgSendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TMsgSend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_msg_send
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TMsgSend record);
}