package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface SpaceService {
    ApiMessage spaceDetail(Dto dto);
    ApiMessage listSpace(Dto dto);
}
