package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TBanner;
import com.dominator.utils.exception.ApiException;

import java.util.List;

public interface BannerService {

    void postBanner(Dto dto) throws ApiException;

    List<TBanner> getBanner(Dto dto);
}
