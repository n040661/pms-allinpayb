package com.dominator.app.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TSocialActivityRelationship;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

/**
 * Created by zhangsuliang on 2018/4/22.
 */
public interface SocialActivityRelationshipService {

    /**
     * 报名接口
     * @param dto
     * @return
     */
     ApiMessage signUp(Dto dto);

    /**
     * 获取报名列表
     * @param dto
     * @return
     */
     ApiMessage getSubscribers(Dto dto);


    /**
     * 我参加的活动
     * @param dto
     * @return
     */
     ApiMessage getMyActivities(Dto dto);

    /**
     * 保存或是更新活动关心
     * @param tSocialActivityRelationship
     * @return
     * @throws ApiException
     */
     ApiMessage merge(TSocialActivityRelationship tSocialActivityRelationship)throws ApiException;


    /**
     * 是否参加活动
     * @param dto
     * @return
     */
     ApiMessage isJoinActivity(Dto dto);

}
