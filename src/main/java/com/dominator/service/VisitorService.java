package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;

public interface VisitorService {
    ApiMessage list(Dto dto);

    ApiMessage getOne(Dto dto);

    ApiMessage passcard(Dto dto);

    ApiMessage invitevisitor(Dto dto);

    ApiMessage checkpass(Dto dto);

    ApiMessage getvisitornum(Dto dto);

    ApiMessage getCompanyAdress(Dto dto);

    ApiMessage mgvisitorMessage(Dto dto);

    /**
     * 获取后台访客的数量
     * @param dto
     * @return
     */
    ApiMessage getBackVisitorNum(Dto dto);

    /**
     * 获取后台访客的列表
     * @param dto company_id or garden_id
     *                 status 状态 0 待到访 1已到访 2已过期
     *                 pageSize 每页数
     *                 pageNum 页数
     *                 create_time 开始时间
     *                 end_time 截止时间
     * @return
     */
    ApiMessage listVisitor(Dto dto);

    /**
     * 获取有访客的月份
     * @param dto
     * @return
     */
    ApiMessage VisitorMonth(Dto dto);

    /**
     * 获取访客月份数量列表
     * @param dto
     * @return
     */
    ApiMessage ListMonth(Dto dto);

    /**
     * 根据月份获取访客数据
     * @param dto
     * @return
     */
    ApiMessage GetVisitorByMonth(Dto dto);

    /**
     * 被邀请人列表
     * @param dto
     * @return
     */
    ApiMessage BeVisitedList(Dto dto);

    /**
     * 修改访客信息
     * @param dto
     * @return
     */
    ApiMessage updatevisitor(Dto dto);

    /**
     * 删除访客信息
     * @param dto
     * @return
     */
    ApiMessage deletevisitor(Dto dto);
}
