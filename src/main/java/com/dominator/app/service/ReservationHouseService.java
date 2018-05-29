package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface ReservationHouseService {
    /**
     * 添加预约看房记录
     * @param dto
     * @return
     */
    ApiMessage addHouse(Dto dto);
    /**预约看房记录列表
     * @param dto
     * @return
     */
    ApiMessage listHouse(Dto dto);
    /**预约看房记录详情
     * @param dto
     * @return
     */
    ApiMessage detailsHouse(Dto dto);
}
