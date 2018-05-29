package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.exception.ApiException;

public interface RegisterService {
    String register(Dto dto) throws ApiException;
}
