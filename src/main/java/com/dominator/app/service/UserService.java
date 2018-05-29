package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.exception.ApiException;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void putUser(String userId, HttpServletRequest request) throws ApiException;

    Dto listCompanyAndGarden(Dto dto);
    
    Dto msgamount(Dto dto);

    Dto getCompanyAndRole(Dto dto);

    Dto showBill(Dto dto);
}
