package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TRole;
import com.dominator.utils.exception.ApiException;

import java.util.List;

public interface RoleService {
    int checkRoleName(Dto dto);

    int getRoleSort(String type);

    void postRole(Dto dto) throws ApiException;

    void putRole(Dto dto) throws ApiException;

    void delRole(Dto dto) throws ApiException;

    Dto getUserRoleId(Dto dto);

    List<TRole> listRole(String unit_id, String roleName);

    void postUsers(Dto dto) throws ApiException;

    Dto listUsers(Dto dto);

//    void deleteUserToRole(Dto dto);
}
