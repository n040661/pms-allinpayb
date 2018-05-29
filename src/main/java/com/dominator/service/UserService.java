package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TUser;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    int countGardenUser(Dto dto);

    int countCompanyUser(Dto dto);

    ApiMessage resetPassword(Dto dto) throws ApiException;

    int checkUserName(String userName);

    void putUser(Dto dto) throws ApiException;

    void postUser(Dto dto) throws ApiException;

    void delUser(Dto dto) throws ApiException;

    ApiMessage userlist(Dto dto);

    ApiMessage updatePassword(Dto dto) throws ApiException;

    Dto getInfo(Dto dto);

    /**
     * 设置密码
     *
     * @param dto
     * @return
     * @throws ApiException
     */
    ApiMessage setPassword(Dto dto) throws ApiException;

    /**
     * 添加用户信息
     *
     * @param dto id
     *            user_name
     *            nick_name
     *            password
     *            phone_num
     * @return user_id
     * @throws ApiException
     */
    String addUser(Dto dto) throws ApiException;

    ApiMessage getUser(Dto dto);

    ApiMessage getRoleUserList(Dto dto);
}
