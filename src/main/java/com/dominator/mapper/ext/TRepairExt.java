package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TRepairExt {
    List<Dto> listCompanyRepairPage(Dto dto);

    List<Dto> listGardenRepairPage(Dto dto);

    List<Dto> listGardenUser(Dto dto);

    /**
     * 获取分配后的报修信息
     * @param dto
     * @return
     */
    Dto repairToUser(Dto dto);

    /**
     * 获取报修详情
     * @param repair_id
     * @return
     */
    Dto repairDetail(String repair_id);
}
