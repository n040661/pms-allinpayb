package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PageExt {
    /**
     * 通过用户id查询该用户所在园区信息
     */
    Dto selectGardenByUserId(Dto dto);

    /**
     * 通过用户id查询该用户所在公司信息
     */
    Dto selectCompanyByUserId(Dto dto);

    /**
     * 通过企业id和用户ID获取角色ID
     * @param companydto
     * @return
     */
    String selectRoleId(Dto companydto);

    List<Dto> selectAllByUserId(Dto dto);

    /**
     * 根据园区ID和用户ID获取园区用户信息
     * @param dto1 garden_id 园区ID
     *             user_id   用户ID
     * @return
     */
    Dto selectGardenUser(Dto dto1);

    /**
     * 根据企业ID和用户ID获取企业用户信息
     * @param dto1
     * @return
     */
    Dto selectCompanyUser(Dto dto1);
}
