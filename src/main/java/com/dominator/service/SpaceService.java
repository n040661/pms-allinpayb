package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface SpaceService {
    ApiMessage addSpace(Dto dto);
    ApiMessage listSpace(Dto dto);
    ApiMessage getSpDetails(Dto dto);
    ApiMessage editSpace(Dto dto);
    ApiMessage releaseSpace(Dto dto);
    ApiMessage deleteSpace(Dto dto);
    ApiMessage exNum(Dto dto);
}
