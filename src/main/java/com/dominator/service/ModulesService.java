package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TModules;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.util.List;

public interface ModulesService {

    void listModulesByIds(Dto gardenDto, String modules_ids);

    ApiMessage listModules(Dto dto);

    ApiMessage listModulesByRole(Dto dto);

    List<TModules> listCheckedModulesByRole(Dto dto);

    ApiMessage addModules(Dto dto) throws ApiException;

    void setRoleModules(Dto dto) throws ApiException;
}
