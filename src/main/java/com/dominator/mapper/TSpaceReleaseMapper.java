package com.dominator.mapper;

import com.dominator.entity.TSpaceRelease;
import com.dominator.entity.TSpaceReleaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TSpaceReleaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int countByExample(TSpaceReleaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int deleteByExample(TSpaceReleaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int insert(TSpaceRelease record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int insertSelective(TSpaceRelease record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    List<TSpaceRelease> selectByExample(TSpaceReleaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    TSpaceRelease selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int updateByExampleSelective(@Param("record") TSpaceRelease record, @Param("example") TSpaceReleaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int updateByExample(@Param("record") TSpaceRelease record, @Param("example") TSpaceReleaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int updateByPrimaryKeySelective(TSpaceRelease record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_space_release
     *
     * @mbggenerated Wed May 16 11:55:57 CST 2018
     */
    int updateByPrimaryKey(TSpaceRelease record);
}