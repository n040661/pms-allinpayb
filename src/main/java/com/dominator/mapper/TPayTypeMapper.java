package com.dominator.mapper;

import com.dominator.entity.TPayType;
import com.dominator.entity.TPayTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TPayTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TPayTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TPayTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TPayType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TPayType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TPayType> selectByExample(TPayTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TPayType selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TPayType record, @Param("example") TPayTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TPayType record, @Param("example") TPayTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TPayType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_type
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TPayType record);
}