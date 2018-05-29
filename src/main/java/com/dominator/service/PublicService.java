package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.exception.ApiException;

import java.util.List;

public interface PublicService {

    void postPublic(Dto dto) throws ApiException;

    void putPublic(Dto dto) throws ApiException;

    void delPublic(Dto dto) throws ApiException;

    String postOrder(Dto dto) throws ApiException;

    void putOrder(Dto dto) throws ApiException;

    Dto listPublic(Dto dto);

    Dto getPublic(Dto dto);

    Dto getOrderList(Dto dto);

    Dto getOrder(Dto dto);

    List<Dto> getHoldingTime(Dto dto);
}
