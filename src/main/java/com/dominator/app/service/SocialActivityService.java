package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TSocialActivity;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

/**
 * Created by zhangsuliang on 2018/4/19.
 */
public interface SocialActivityService {
    /**
     * 创建活动-逻辑
     * @return
     * @throws ApiException
     */
    ApiMessage create(Dto dto) throws Exception;
    /**
     * 保存或更新活动
     * @param tSocialActivity
     * @return
     * @throws ApiException
     */
     ApiMessage merge(TSocialActivity tSocialActivity) throws ApiException;

    /**
     * 删除活动
     * @param dto
     * @return
     */
     ApiMessage delete(Dto dto);

    /**
     * 普通用户活动列表
     * @param dto
     * @return
     */
     ApiMessage list(Dto dto);

    /**
     * 管理员活动列表
     * @param dto
     * @return
     */
     ApiMessage adminList(Dto dto);
    /**
     * 获取活动详情
     * @param dto
     * @return
     */
     ApiMessage get(Dto dto);


    /**
     * 审核活动
     * @param dto
     * @return
     * @throws ApiException
     */
     ApiMessage update(Dto dto) throws ApiException;

    /**
     * 查询活动状态
     * @param dto
     * @return
     */
     ApiMessage checkStatus(Dto dto);


    /**
     * 取消活动
     * @param dto
     * @return
     * @throws Exception
     */
     ApiMessage cancel(Dto dto) throws Exception;

    /**
     * 单个审核
     * @param dto
     * @return
     * @throws Exception
     */
     ApiMessage check(Dto dto);

    /**
     * 多个审核
     * @param dto
     * @return
     * @throws Exception
     */
     ApiMessage checkList(Dto dto);

    /**
     * 获取所有的活动
     * @param dto
     * @return
     * @throws Exception
     */
     ApiMessage allActivities(Dto dto);
}
