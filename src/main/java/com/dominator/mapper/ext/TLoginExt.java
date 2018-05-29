package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TLoginExt {
    List<Dto> listProperty(Dto dto);

    List<Dto> listGarden(Dto dto);

    List<Dto> listCompany(Dto dto);
}
