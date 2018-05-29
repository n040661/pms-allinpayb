package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface ProjectService {
    ApiMessage projectDetail(Dto dto);
    ApiMessage listProject(Dto dto);
}
