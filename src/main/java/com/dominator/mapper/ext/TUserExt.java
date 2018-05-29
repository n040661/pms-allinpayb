package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TUserExt {

    List<Dto> gardenUserList(Dto dto);

    List<Dto> companyUserList(Dto dto);

    Dto getCompanyUser(Dto dto);

    Dto getGardenUser(Dto dto);
}
