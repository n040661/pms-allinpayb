package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TUser;

public interface LoginService {
    Dto login(Dto dto);

    Dto select(Dto dto);

    Dto getLoginList(String propertyId, TUser user, Dto tokenDto);
}
