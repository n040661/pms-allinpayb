package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TLoginInfo;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.util.Date;

public interface LoginService {
//    ApiMessage login(Dto dto);

    /**
     * 企业用户登录
     *
     * @param dto user_name String 必传 账号
     *            password String 必传 密码
     * @return
     *//*
    ApiMessage login4EnterpriseUser(Dto dto) throws ApiException;

    */

    /**
     * 园区端--邮箱登录
     *
     * @param dto user_name String 必传 邮箱账号
     *            password String 必传 密码
     * @return
     *//*
    ApiMessage loginByEmail(Dto dto) throws ApiException;

    /**
     * 登录记录
     *
     * @param dto user_id 必传
     *            property_id
     *            garden_id
     *            company_id
     * @return
     */
    ApiMessage loginInfo(Dto dto) throws ApiException;

    Date getLastLoginTime(Dto dto);

    /**
     * 登录记录
     *
     * @param dto token
     * @return
     * @throws ApiException
     */
    ApiMessage loginRecord(Dto dto) throws ApiException;

    ApiMessage loginByAdmin(Dto dto) throws ApiException;

    ApiMessage loginV2(Dto dto) throws ApiException;

    ApiMessage loginStep2(Dto dto) throws ApiException;

    ApiMessage listusercompany(Dto dto) throws ApiException;
}
