package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TGarden;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TCommonExt {
    List<Dto> list(Dto dto);

    List<Dto> listBackVisitor(Dto dto);

    List<Dto> listBills(Dto dto);
}
