package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TCompanyUserRole;
import com.dominator.entity.TGardenUserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TGardenUserRoleExt {
    //批量新增
    void  insertBatch(Dto dto);

    //批量修改
    void updateRoleIdBatch(Dto dto);

    void delBatch(Dto dto);

    List<Dto> uncheckedGardenUsers(Dto dto);
}
