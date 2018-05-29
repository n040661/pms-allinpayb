package com.dominator.mapper;

import com.dominator.entity.TGarden;
import com.dominator.entity.TGardenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TGardenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int countByExample(TGardenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByExample(TGardenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insert(TGarden record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int insertSelective(TGarden record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    List<TGarden> selectByExample(TGardenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    TGarden selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExampleSelective(@Param("record") TGarden record, @Param("example") TGardenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByExample(@Param("record") TGarden record, @Param("example") TGardenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKeySelective(TGarden record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garden
     *
     * @mbggenerated Thu Mar 29 14:47:43 CST 2018
     */
    int updateByPrimaryKey(TGarden record);
}