package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.app.service.SocialActivityRelationshipService;
import com.dominator.entity.TSocialActivity;
import com.dominator.entity.TSocialActivityRelationship;
import com.dominator.entity.TSocialActivityRelationshipExample;
import com.dominator.entity.TUser;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TSocialActivityMapper;
import com.dominator.mapper.TSocialActivityRelationshipMapper;
import com.dominator.mapper.TUserMapper;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangsuliang on 2018/4/22.
 */

@Service("app.SocialActivityRelationshipServiceImpl")
public class SocialActivityRelationshipServiceImpl implements SocialActivityRelationshipService{

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TSocialActivityMapper tSocialActivityMapper;

    @Autowired
    private TSocialActivityRelationshipMapper tSocialActivityRelationshipMapper;

    @Override
    public ApiMessage signUp(Dto dto) {
        String saId = dto.getString( "saId" );
        String userId = dto.getString( "userId" );
        TUser user = tUserMapper.selectByPrimaryKey( userId );
        String subscriberPhoneNum = dto.getString( "subscriberPhoneNum" );
        String subscriberRealName = dto.getString( "subscriberRealName" );
        TSocialActivityRelationship socialActivityRelationship = new TSocialActivityRelationship();
        socialActivityRelationship.setSaId( saId );
        socialActivityRelationship.setSubscriberId( userId );
        socialActivityRelationship.setIsValid( "1" );
        if(user.getUserName().equals( subscriberPhoneNum )){
            socialActivityRelationship.setSubscriberPhoneNum( subscriberPhoneNum );
            socialActivityRelationship.setSubscriberRealName( subscriberRealName );
        }
        Date createTime = new Date( System.currentTimeMillis() );
        socialActivityRelationship.setCreateTime(createTime);
        merge( socialActivityRelationship );
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage getSubscribers(Dto dto) {
        String saId = dto.getString( "saId" );
        String serviceCode = dto.getString( "serviceCode" );

        Page.startPage( dto );
        TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
        TSocialActivityRelationshipExample.Criteria criteria = example.createCriteria();
        criteria.andSaIdEqualTo( saId ).andIsValidEqualTo( "1" );
        List<Dto> dtos = new ArrayList<>(  );
        List<TSocialActivityRelationship> relationships = tSocialActivityRelationshipMapper.selectByExample( example );
        PageInfo<TSocialActivityRelationship> pageInfo = new PageInfo<>( relationships );
        if ("COUNT".equals( serviceCode )){
            Dto dto4Count = new HashDto();
            dto4Count.put( "count",pageInfo.getTotal());
            return ApiMessageUtil.success( dto4Count );
        }
        List<TSocialActivityRelationship> list = pageInfo.getList();
        if (list.size()>0){
            for (TSocialActivityRelationship relationship:list){
                Dto dto4Temp = new HashDto();
                String subscriberId = relationship.getSubscriberId();
                String saId4DB = relationship.getSaId();
                TSocialActivity socialActivity = tSocialActivityMapper.selectByPrimaryKey( saId4DB );
                TUser publishUser = tUserMapper.selectByPrimaryKey( socialActivity.getPublisherId() );
                TUser user = tUserMapper.selectByPrimaryKey( subscriberId );
                Date createTime = relationship.getCreateTime();
                dto4Temp.put( "createTime",createTime.getTime());
                dto4Temp.put( "nickName",user.getNickName() );
                dto4Temp.put( "userName",user.getUserName() );
                dto4Temp.put( "phoneNum",user.getPhoneNum() );
                dto4Temp.put( "headImg",user.getHeadImg() );
                dto4Temp.put( "publisherUserName",publishUser.getUserName() );
                dto4Temp.put( "publisherUserPhone",publishUser.getPhoneNum() );
                dtos.add(dto4Temp);
            }
        }
        Dto result = new HashDto();
        result.put( "list", dtos );
        result.put( "total",pageInfo.getTotal() );
        result.put( "pageSize",pageInfo.getPageSize() );
        result.put( "pageNum",pageInfo.getPageNum() );
        return ApiMessageUtil.success( result );
    }

    @Override
    @Transactional
    public ApiMessage getMyActivities(Dto dto) {
        String id = dto.getString( "userId" );
        TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
        //分页处理
        Page.startPage(dto);
        TSocialActivityRelationshipExample.Criteria criteria = example.createCriteria();
        criteria.andSubscriberRealNameIsNull().andSubscriberPhoneNumIsNull().andSubscriberIdEqualTo( id );
        List<TSocialActivityRelationship> relationships = tSocialActivityRelationshipMapper.selectByExample( example );
        PageInfo<TSocialActivityRelationship> pageInfo = new PageInfo<>( relationships );
        List<Dto> dtos = new ArrayList<>(  );
        if(relationships.size()>0){
           for (TSocialActivityRelationship relationship:relationships){
               Dto dto4Temp = new HashDto();
               String saId = relationship.getSaId();
               TSocialActivity socialActivity = tSocialActivityMapper.selectByPrimaryKey( saId );
               String publisherId = socialActivity.getPublisherId();
               TUser user = tUserMapper.selectByPrimaryKey( publisherId );
               dto4Temp.put( "publisherUserName",user.getUserName() );
               dto4Temp.put( "publisherPhoneNum",user.getPhoneNum() );
               dto4Temp.put( "publisherNickName",user.getNickName() );
               dto4Temp.put( "title",socialActivity.getTitle() );
               String pictureUrl = socialActivity.getPictureUrl();
               pictureUrl = pictureUrl.replace( "[", "" ).replace( "]", "" );
               String[] splits = pictureUrl.split( "," );
               List<String> list = new ArrayList<>(  );
               if(splits.length>0){
                   for (String split:splits){
                       list.add( split );
                   }
               }
               if("".equals( list.get( 0 ) )){
                   dto4Temp.put( "pictureUrl","" );
               }else {
                   dto4Temp.put( "pictureUrl", JSONArray.fromObject( list ) );
               }
               dto4Temp.put( "address",socialActivity.getAddress() );
               dto4Temp.put( "activityDesc",socialActivity.getActivityDesc() );
               dto4Temp.put( "publishTime",socialActivity.getPublishTime().getTime());
               dto4Temp.put( "beginTime",socialActivity.getBeginTime().getTime() );
               dto4Temp.put( "id",socialActivity.getId() );
               //获取活动报名总数
               TSocialActivityRelationshipExample example2 = new TSocialActivityRelationshipExample();
               TSocialActivityRelationshipExample.Criteria criteria2 = example2.createCriteria();
               criteria2.andSaIdEqualTo( socialActivity.getId() ).andIsValidEqualTo( "1" );
               List<TSocialActivityRelationship> relationships2 = this.tSocialActivityRelationshipMapper.selectByExample( example2 );
               dto4Temp.put( "subscriber_count",relationships2.size() +"");
               dtos.add( dto4Temp );
           }
        }
        Dto result= new HashDto();
        result.put( "list",dtos );
        result.put( "total",pageInfo.getTotal() );
        result.put( "pageSize",pageInfo.getPageSize() );
        result.put( "pageNum",pageInfo.getPageNum() );
        return ApiMessageUtil.success( result );
    }

    @Override
    public ApiMessage merge(TSocialActivityRelationship tSocialActivityRelationship) throws ApiException {
        String socialActivityRelationshipId = tSocialActivityRelationship.getId();
        try {
            if(socialActivityRelationshipId==null){
                tSocialActivityRelationship.setId( PrimaryGenerater.getInstance().uuid() );
                this.tSocialActivityRelationshipMapper.insert( tSocialActivityRelationship );
            }else {
                this.tSocialActivityRelationshipMapper.updateByPrimaryKey( tSocialActivityRelationship );
            }
        }catch (Exception ex){
            throw new ApiException( ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage isJoinActivity(Dto dto) {
        String userId = dto.getString( "userId" );
        String saId = dto.getString( "saId" );
        Dto dto4DB = new HashDto();
        dto4DB.put( "subscriber_id",userId );
        dto4DB.put( "sa_id",saId );
        TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
        TSocialActivityRelationshipExample.Criteria criteria = example.createCriteria();
        criteria.andSubscriberRealNameIsNull().andSubscriberPhoneNumIsNull().andSubscriberIdEqualTo( userId ).andSaIdEqualTo( saId );
        List<TSocialActivityRelationship> list = tSocialActivityRelationshipMapper.selectByExample( example );
        Dto result = new HashDto();
        result.put( "isJoinActivity",list.size()>0?1:0 );
        return ApiMessageUtil.success( result );
    }
}


