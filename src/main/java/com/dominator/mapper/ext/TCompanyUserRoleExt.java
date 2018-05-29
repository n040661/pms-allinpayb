package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TCompanyUserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TCompanyUserRoleExt {
    //批量新增
    void  insertBatch(Dto dto);

    //批量修改
    void updateRoleIdBatch(Dto dto);

    //批量删除
    void  delBatch(Dto dto);

    List<Dto> uncheckedGardenUsers(Dto dto);
}
