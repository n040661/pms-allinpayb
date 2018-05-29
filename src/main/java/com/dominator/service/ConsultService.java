package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TConsult;

import java.util.List;

public interface ConsultService {
    String addConsult(Dto dto);

    String updateConsult(Dto dto);

    String deleteConsult(Dto dto);

    TConsult detailConsult(Dto dto);

    List<TConsult> list(Dto dto);
}
