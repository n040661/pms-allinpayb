package com.dominator.mapper;

import com.dominator.entity.TCompanyUserRole;
import com.dominator.entity.TCompanyUserRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TCompanyUserRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int countByExample(TCompanyUserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int deleteByExample(TCompanyUserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int insert(TCompanyUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int insertSelective(TCompanyUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    List<TCompanyUserRole> selectByExample(TCompanyUserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    TCompanyUserRole selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int updateByExampleSelective(@Param("record") TCompanyUserRole record, @Param("example") TCompanyUserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int updateByExample(@Param("record") TCompanyUserRole record, @Param("example") TCompanyUserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int updateByPrimaryKeySelective(TCompanyUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user_role
     *
     * @mbggenerated Fri Apr 20 18:04:29 CST 2018
     */
    int updateByPrimaryKey(TCompanyUserRole record);
}