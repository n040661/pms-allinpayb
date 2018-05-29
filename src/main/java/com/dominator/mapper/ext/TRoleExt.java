package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TRoleExt {

    Dto getGardenRole(Dto dto);

    Dto getCompanyRole(Dto dto);

    Dto getPropertyRole(Dto dto);

}
