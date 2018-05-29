package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TUser;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.util.List;


public interface CompanyService {

    Dto listCompany(Dto dto);

    void delCompany(Dto dto) throws ApiException;

    void postCompany(Dto dto) throws ApiException;

    void putCompany(Dto dto) throws ApiException;

//    ApiMessage getCompanyBill(Dto dto);

    ApiMessage listCompanyBill(Dto dto);

    ApiMessage listCompanyUnpaidInfo(Dto dto);

    int checkCompanyName(Dto dto);

    Dto getCompany(Dto dto);

//    /**
//     * 企业端--获取人事概览信息
//     *
//     * @param dto company_id String 必传 企业id
//     * @return
//     */
//    ApiMessage getUserOverview(Dto dto);
//
//    /**
//     * 企业端--首页--近期账单
//     *
//     * @param dto company_id String 必传 企业id
//     * @return
//     */
//    ApiMessage listRecentBills(Dto dto);
//
//    /**
//     * 企业端--用户名是否存在
//     *
//     * @param dto user_name String 必传
//     * @return
//     */
//    ApiMessage checkUsername(Dto dto);
//
//    /**
//     * 企业端--重置密码
//     *
//     * @param dto user_name String 必传 用户名
//     *            password String 必传 新密码
//     * @return
//     */
//    ApiMessage resetPassword(Dto dto) throws ApiException;
//
//
//    /**
//     * 企业端--完善企业信息
//     *
//     * @param dto company_id String 必传 企业id
//     *            modify_id String 必传 当前登录用户id
//     *            register_address String 必传 注册地址
//     *            register_phone String 必传 注册电话
//     *            bank_name String 必传 开户银行
//     *            bank_num String 必传 银行账号
//     *            credit_num String 必传 统一社会信用代码
//     *            legal_person String 必传 法定代表人
//     *            licence_url String 必传 营业执照url
//     * @return
//     */
////    ApiMessage updateCompany(Dto dto) throws ApiException;
//
//    /**
//     * 企业端--根据条件查询企业员工列表
//     *
//     * @param dto company_id String 必传 企业id
//     *            status String 必传 在职状态：0离职，1在职， 2全部
//     *            condition String 非必传 查询条件 员工姓名/工号/手机号
//     *            pageNum int 非必传 第几页 默认1
//     *            pageSize int 非必传 每页几条 默认10
//     * @return
//     */
//    ApiMessage listCompanyUsersPage(Dto dto);
//
//    /**
//     * 企业端--获取企业员工信息
//     *
//     * @param dto company_user_id String 企业员工关系表id
//     * @return
//     */
//    ApiMessage getCompanyUser(Dto dto);
//
    /**
     * 企业端--根据角色获取员工列表
     *
     * @return role_id 必传 角色id
     *          company_id 必传 企业id
     */
    List<TUser> listCompanyUserRole(Dto dto) throws ApiException;
//
//    /**
//     * 企业端--编辑员工信息
//     *
//     * @param dto user_id String 必传 需要修改员工的id
//     *            company_user_id String 必传 公司员工关系表id
//     *            modify_id String 必传 当前登录员工id
//     *            nick_name String 必传 姓名
//     *            phone_num String 必传 手机号
//     *            hire_date String 必传 入职时间
//     *            status String 必传 在职状态：0离职，1在职
//     *            work_num String 必传 工号 （可为空）
//     *            department_name String 必传 部门（可为空）
//     *            position String 必传 岗位（可为空）
//     *            level String 必传 职级（可为空）
//     *            email String 必传 邮箱 （可为空）
//     * @return
//     */
//    ApiMessage updateCompanyUser(Dto dto) throws ApiException;
//
//    /**
//     * 企业端--员工离职
//     *
//     * @param dto company_user_id String 必传 公司员工关系表id
//     *            modify_id String 必传 当前登录员工id
//     *            fire_date String 必传 离职时间
//     * @return
//     */
//    ApiMessage fireCompanyUser(Dto dto) throws ApiException;
//
//    /**
//     * 企业端--员工再入职
//     *
//     * @param dto company_user_id String 必传 企业员工关系表id
//     *            modify_id String 必传 当前登录用户id
//     *            hire_date String 必传 入职日期
//     * @return
//     */
//    ApiMessage hireCompanyUser(Dto dto) throws ApiException;
//
//    /**
//     * 企业端--删除员工
//     *
//     * @param dto user_id 必传 用户id
//     *            company_user_id String 必传 企业员工关系表id
//     *            modify_id String 必传 当前登录员工id
//     * @return
//     */
//    ApiMessage delCompanyUser(Dto dto) throws ApiException;
//
//    /**
//     * 企业端--获取企业账单列表
//     *
//     * @param dto company_id String 必传 企业id
//     *            is_paid String 必传 缴费状态：0未交，1已交
//     *            pageNum int 非必传 当前页，默认1
//     *            pageSize int 非必传 每页条数，默认10
//     * @return
//     */
//    ApiMessage listBills(Dto dto);
}
