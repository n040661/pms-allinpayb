package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface ProjectService {
    ApiMessage addProject(Dto dto);
    ApiMessage listProject(Dto dto);
    ApiMessage getPjDetails(Dto dto);
    ApiMessage editProject(Dto dto);
    ApiMessage releaseProject(Dto dto);
    ApiMessage deleteProject(Dto dto);
    ApiMessage exNum(Dto dto);
}
