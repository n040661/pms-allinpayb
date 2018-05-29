package com.dominator.mapper;

import com.dominator.entity.TblWxopenPreAuthCode;
import com.dominator.entity.TblWxopenPreAuthCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblWxopenPreAuthCodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int countByExample(TblWxopenPreAuthCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByExample(TblWxopenPreAuthCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insert(TblWxopenPreAuthCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insertSelective(TblWxopenPreAuthCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    List<TblWxopenPreAuthCode> selectByExample(TblWxopenPreAuthCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    TblWxopenPreAuthCode selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExampleSelective(@Param("record") TblWxopenPreAuthCode record, @Param("example") TblWxopenPreAuthCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExample(@Param("record") TblWxopenPreAuthCode record, @Param("example") TblWxopenPreAuthCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKeySelective(TblWxopenPreAuthCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_pre_auth_code
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKey(TblWxopenPreAuthCode record);
}