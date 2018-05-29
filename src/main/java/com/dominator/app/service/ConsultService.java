package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TConsult;
import com.dominator.utils.api.ApiMessage;

import java.util.List;

public interface ConsultService {
    Dto list(Dto dto);

    TConsult detail(Dto dto);

    ApiMessage appointment(Dto dto);
}
