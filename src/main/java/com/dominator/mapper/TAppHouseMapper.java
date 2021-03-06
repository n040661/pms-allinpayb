package com.dominator.mapper;

import com.dominator.entity.TAppHouse;
import com.dominator.entity.TAppHouseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TAppHouseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int countByExample(TAppHouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int deleteByExample(TAppHouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int insert(TAppHouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int insertSelective(TAppHouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    List<TAppHouse> selectByExample(TAppHouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    TAppHouse selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int updateByExampleSelective(@Param("record") TAppHouse record, @Param("example") TAppHouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int updateByExample(@Param("record") TAppHouse record, @Param("example") TAppHouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int updateByPrimaryKeySelective(TAppHouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_app_house
     *
     * @mbggenerated Fri May 04 18:15:13 CST 2018
     */
    int updateByPrimaryKey(TAppHouse record);
}