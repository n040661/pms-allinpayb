package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface RepairService {
    ApiMessage listpage(Dto dto);
    ApiMessage addrepair(Dto dto);
    /**
     * 报修详情
     * @param dto
     * @return
     */
    ApiMessage repairDetail(Dto dto);

    /**
     * 我的维修
     * @param
     * @return
     */
    ApiMessage myrepair(Dto dto);

    ApiMessage completeRepair(Dto dto);
}
