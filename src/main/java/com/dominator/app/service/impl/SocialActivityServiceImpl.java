package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.app.service.SocialActivityService;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TCompanyMapper;
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
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangsuliang on 2018/4/19.
 */
@Service("app.socialActivityServiceImpl")
public class SocialActivityServiceImpl implements SocialActivityService {

    private RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TSocialActivityMapper tSocialActivityMapper;

    @Override
    public ApiMessage create(Dto dto) throws Exception {
        System.out.println("dto:"+dto);
        String desc = dto.getString( "activityDesc" );
        String title = dto.getString( "title" );
        String province = dto.getString( "province" );
        String city = dto.getString( "city" );
        String district = dto.getString( "district" );
        String address = dto.getString( "address" );
        String userId= dto.getString("userId");
        String beginTimeStr = dto.getString( "beginTime" );
        String endTimeStr = dto.getString( "endTime" );
        String pictureUrl = dto.getString( "pictureUrl" );
        String propertyId = dto.getString( "propertyId" );
        String companyId = dto.getString( "companyId" );
        Date currentTime = new Date( System.currentTimeMillis() );
        Date beginTime = new Date( Long.parseLong( beginTimeStr ) );
        Date endTime = new Date( Long.parseLong( endTimeStr ) );

        TSocialActivity socialActivity = new TSocialActivity();
        socialActivity.setActivityDesc( desc );
        socialActivity.setTitle( title );
        socialActivity.setAddress( address );
        socialActivity.setIsValid( "1" );
        socialActivity.setIsChecked( "0" );
        socialActivity.setIsHearted( "0" );
        socialActivity.setStatus( "0" );
        socialActivity.setProvince( province );
        socialActivity.setCity( city );
        socialActivity.setDistrict( district );
        socialActivity.setPublisherId( userId );
        socialActivity.setPictureUrl( pictureUrl );
        socialActivity.setBeginTime( beginTime );
        socialActivity.setEndTime( endTime );
        socialActivity.setCompanyId( companyId );
        socialActivity.setPropertyId( propertyId );
        socialActivity.setCreateTime( currentTime );
        socialActivity.setPublishTime( currentTime );
        return merge( socialActivity );
    }

    @Override
    public ApiMessage merge(TSocialActivity tSocialActivity) throws ApiException {
        String id = tSocialActivity.getId();
         try {
            if(id==null){
                tSocialActivity.setId( PrimaryGenerater.getInstance().uuid() );
                tSocialActivityMapper.insert( tSocialActivity );
            }else {
                tSocialActivityMapper.updateByPrimaryKey( tSocialActivity );
            }
        }catch (Exception ex){
            throw new ApiException( ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage delete(Dto dto) {
        return null;
    }

    @Autowired
    private TSocialActivityRelationshipMapper tSocialActivityRelationshipMapper;

    @Override
    public ApiMessage allActivities(Dto dto) {
        TSocialActivityExample socialActivityExample = new TSocialActivityExample();
        socialActivityExample.setOrderByClause( "create_time DESC");
        //分页
        Page.startPage(dto);
        TSocialActivityExample.Criteria criteria = socialActivityExample.createCriteria();
        criteria.andIsValidEqualTo( "1" ).andStatusEqualTo( "1" );
        List<TSocialActivity> activities4All = tSocialActivityMapper.selectByExample( socialActivityExample );
        PageInfo<TSocialActivity> pageInfo = new PageInfo<>( activities4All );
        List<JSONObject> jsons = new ArrayList<>(  );
        for (TSocialActivity activity:activities4All){
            JSONObject json = JSONObject.fromObject( activity );
            String pictureUrl = activity.getPictureUrl();
            pictureUrl= pictureUrl.replace( "[","" ).replace(  "]","");
            String[] splits = pictureUrl.split( "," );
            List<String> list = new ArrayList<>(  );
            for (String split:splits){
                list.add( split );
            }
            json.put( "pictureUrl",list );

            json.put( "publishTime",activity.getPublishTime().getTime() );
            json.put( "beginTime",activity.getBeginTime().getTime() );
            json.put( "endTime",activity.getEndTime().getTime() );

            String publisherId = activity.getPublisherId();
            TUser user = tUserMapper.selectByPrimaryKey( publisherId );
            String publisherUserName = user.getUserName();
            String publisherNickName = user.getNickName();
            String publisherPhoneNum = user.getPhoneNum();
            json.put( "publisherUserName",publisherUserName );
            json.put( "publisherNickName",publisherNickName );
            json.put( "publisherPhoneNum",publisherPhoneNum );
            //获取活动报名总数
            TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
            TSocialActivityRelationshipExample.Criteria criteria2 = example.createCriteria();
            criteria2.andSaIdEqualTo( activity.getId() ).andIsValidEqualTo( "1" );
            List<TSocialActivityRelationship> relationships = tSocialActivityRelationshipMapper.selectByExample( example );
            json.put( "subscriber_count",relationships.size() +"");
            jsons.add( json );
        }
        Dto result = new HashDto();
        result.put( "list",jsons );
        result.put( "total",pageInfo.getTotal() );
        result.put( "pageSize",pageInfo.getPageSize() );
        result.put( "pageNum",pageInfo.getPageNum() );
        return ApiMessageUtil.success( result );
    }


    @Override
    @Transactional
    public ApiMessage list(Dto dto) {
        String userId = dto.getString( "userId" );
        String serviceCode = dto.getString( "serviceCode" );
        if("PERSON".equals( serviceCode )){
            TUser user = tUserMapper.selectByPrimaryKey( userId );
            //分页操作
            Page.startPage(dto);
            TSocialActivityExample socialActivityExample = new TSocialActivityExample();
            TSocialActivityExample.Criteria criteria = socialActivityExample.createCriteria();
            criteria.andPublisherIdEqualTo( userId ).andStatusEqualTo( "1" );
            List<TSocialActivity> activities4Person = tSocialActivityMapper.selectByExample( socialActivityExample );
            PageInfo<TSocialActivity> pageInfo = new PageInfo<>( activities4Person );
            List<JSONObject> jsons = new ArrayList<>(  );
            for (TSocialActivity activity:activities4Person){
                String status = activity.getStatus();
                String publisherId = activity.getPublisherId();
                if(!"1".equals( status )) {
                    continue;
                }
                if (!userId.equals( publisherId )){
                    continue;
                }
                JSONObject json = JSONObject.fromObject( activity );
                String pictureUrl = activity.getPictureUrl();
                pictureUrl= pictureUrl.replace( "[","" ).replace(  "]","");
                String[] splits = pictureUrl.split( "," );
                List<String> list = new ArrayList<>(  );
                for (String split:splits){
                    list.add( split );
                }
                json.put( "pictureUrl",list );
                json.put( "createTime",activity.getCreateTime().getTime() );
                json.put( "publishTime",activity.getPublishTime().getTime() );
                json.put( "beginTime",activity.getBeginTime().getTime() );
                json.put( "endTime",activity.getEndTime().getTime() );
                //String publisherId = activity.getPublisherId();
                //TUser user = tUserMapper.selectByPrimaryKey( publisherId );
                String publisherUserName = user.getUserName();
                String publisherNickName = user.getNickName();
                String publisherPhoneNum = user.getPhoneNum();
                json.put( "publisherUserName",publisherUserName );
                json.put( "publisherNickName",publisherNickName );
                json.put( "publisherPhoneNum",publisherPhoneNum );
                //获取活动报名总数
                TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
                TSocialActivityRelationshipExample.Criteria criteria2 = example.createCriteria();
                criteria2.andSaIdEqualTo( activity.getId() ).andIsValidEqualTo( "1" );
                List<TSocialActivityRelationship> relationships = this.tSocialActivityRelationshipMapper.selectByExample( example );
                json.put( "subscriber_count",relationships.size() +"");
                jsons.add( json );
            }
            Dto activityDto = new HashDto();
            activityDto.put( "list",jsons );
            activityDto.put( "total",pageInfo.getTotal() );
            activityDto.put( "pageSize",pageInfo.getPageSize() );
            activityDto.put( "pageNum",pageInfo.getPageNum() );
            return ApiMessageUtil.success( activityDto );
        }else if("ALL".equals( serviceCode )) {
            TSocialActivityExample socialActivityExample = new TSocialActivityExample();
            socialActivityExample.setOrderByClause( "create_time DESC");
            //分页
            Page.startPage(dto);
            TSocialActivityExample.Criteria criteria = socialActivityExample.createCriteria();
            criteria.andIsValidEqualTo( "1" ).andStatusEqualTo( "1" );
            List<TSocialActivity> activities4All = tSocialActivityMapper.selectByExample( socialActivityExample );
            PageInfo<TSocialActivity> pageInfo = new PageInfo<>( activities4All );
            List<JSONObject> jsons = new ArrayList<>(  );
            for (TSocialActivity activity:activities4All){
                JSONObject json = JSONObject.fromObject( activity );
                String pictureUrl = activity.getPictureUrl();
                pictureUrl= pictureUrl.replace( "[","" ).replace(  "]","");
                String[] splits = pictureUrl.split( "," );
                List<String> list = new ArrayList<>(  );
                for (String split:splits){
                    list.add( split );
                }
                json.put( "pictureUrl",list );
                json.put( "createTime",activity.getCreateTime().getTime() );
                json.put( "publishTime",activity.getPublishTime().getTime() );
                json.put( "beginTime",activity.getBeginTime().getTime() );
                json.put( "endTime",activity.getEndTime().getTime() );
                String publisherId = activity.getPublisherId();
                TUser user = tUserMapper.selectByPrimaryKey( publisherId );
                String publisherUserName = user.getUserName();
                String publisherNickName = user.getNickName();
                String publisherPhoneNum = user.getPhoneNum();
                json.put( "publisherUserName",publisherUserName );
                json.put( "publisherNickName",publisherNickName );
                json.put( "publisherPhoneNum",publisherPhoneNum );
                //获取活动报名总数
                TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
                TSocialActivityRelationshipExample.Criteria criteria2 = example.createCriteria();
                criteria2.andSaIdEqualTo( activity.getId() ).andIsValidEqualTo( "1" );
                List<TSocialActivityRelationship> relationships = tSocialActivityRelationshipMapper.selectByExample( example );
                json.put( "subscriber_count",relationships.size() +"");
                jsons.add( json );
            }
            Dto result = new HashDto();
            result.put( "list",jsons );
            result.put( "total",pageInfo.getTotal() );
            result.put( "pageSize",pageInfo.getPageSize() );
            result.put( "pageNum",pageInfo.getPageNum() );
            return ApiMessageUtil.success( result );
        }else if("PERSON_ALL".equals( serviceCode )){
            TUser user = tUserMapper.selectByPrimaryKey( userId );
            //分页操作
            Page.startPage(dto);
            TSocialActivityExample socialActivityExample = new TSocialActivityExample();
            TSocialActivityExample.Criteria criteria = socialActivityExample.createCriteria();
            criteria.andPublisherIdEqualTo( userId );
            List<TSocialActivity> activities4Person = tSocialActivityMapper.selectByExample( socialActivityExample );
            PageInfo<TSocialActivity> pageInfo = new PageInfo<>( activities4Person );
            List<JSONObject> jsons = new ArrayList<>(  );
            for (TSocialActivity activity:activities4Person){
                String status = activity.getStatus();
                String publisherId = activity.getPublisherId();
//                if(!"1".equals( status )) {
//                    continue;
//                }
                if (!userId.equals( publisherId )){
                    continue;
                }
                JSONObject json = JSONObject.fromObject( activity );
                String pictureUrl = activity.getPictureUrl();
                pictureUrl= pictureUrl.replace( "[","" ).replace(  "]","");
                String[] splits = pictureUrl.split( "," );
                List<String> list = new ArrayList<>(  );
                for (String split:splits){
                    list.add( split );
                }
                json.put( "pictureUrl",list );
                json.put( "createTime",activity.getCreateTime().getTime() );
                json.put( "publishTime",activity.getPublishTime().getTime() );
                json.put( "beginTime",activity.getBeginTime().getTime() );
                json.put( "endTime",activity.getEndTime().getTime() );
                 //String publisherId = activity.getPublisherId();
                //TUser user = tUserMapper.selectByPrimaryKey( publisherId );
                String publisherUserName = user.getUserName();
                String publisherNickName = user.getNickName();
                String publisherPhoneNum = user.getPhoneNum();
                json.put( "publisherUserName",publisherUserName );
                json.put( "publisherNickName",publisherNickName );
                json.put( "publisherPhoneNum",publisherPhoneNum );
                //获取活动报名总数
                TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
                TSocialActivityRelationshipExample.Criteria criteria2 = example.createCriteria();
                criteria2.andSaIdEqualTo( activity.getId() ).andIsValidEqualTo( "1" );
                List<TSocialActivityRelationship> relationships = tSocialActivityRelationshipMapper.selectByExample( example );
                json.put( "subscriber_count",relationships.size() +"");
                jsons.add( json );
            }
            Dto activityDto = new HashDto();
            activityDto.put( "list",jsons );
            activityDto.put( "total",pageInfo.getTotal() );
            activityDto.put( "pageSize",pageInfo.getPageSize() );
            activityDto.put( "pageNum",pageInfo.getPageNum() );
            return ApiMessageUtil.success( activityDto );
        }
     return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage adminList(Dto dto) {
        String token = dto.getString( "token" );
        String status = dto.getString( "status" );
        Dto tokenDto = (Dto) ru.getObject(token);
        String propertyId = tokenDto.getString( "propertyId" );
        TSocialActivityExample socialActivityExample = new TSocialActivityExample();
        socialActivityExample.setOrderByClause( "create_time DESC");
        //分页
        Page.startPage(dto);
        TSocialActivityExample.Criteria criteria = socialActivityExample.createCriteria();
        criteria.andIsValidEqualTo( "1" ).andPropertyIdEqualTo( propertyId ).andStatusEqualTo( status );
        List<TSocialActivity> activities4All = tSocialActivityMapper.selectByExample( socialActivityExample );
        PageInfo<TSocialActivity> pageInfo = new PageInfo<>( activities4All );
        List<JSONObject> jsons = new ArrayList<>(  );
        for (TSocialActivity activity:activities4All){
            JSONObject json = JSONObject.fromObject( activity );
            json.put( "createTime",activity.getCreateTime().getTime() );
            json.put( "publishTime",activity.getPublishTime().getTime() );
            json.put( "beginTime",activity.getBeginTime().getTime() );
            json.put( "endTime",activity.getEndTime().getTime() );
            String publisherId = activity.getPublisherId();
            TUser user = this.tUserMapper.selectByPrimaryKey( publisherId );
            String publisherUserName = user.getUserName();
            String publisherNickName = user.getNickName();
            String publisherPhoneNum = user.getPhoneNum();
            json.put( "publisherUserName",publisherUserName );
            json.put( "publisherNickName",publisherNickName );
            json.put( "publisherPhoneNum",publisherPhoneNum );
            jsons.add( json );
        }
        Dto result = new HashDto();
        result.put( "list",jsons );
        result.put( "total",  pageInfo.getTotal() );
        result.put( "pageSize",pageInfo.getPageSize() );
        result.put( "pageNum",pageInfo.getPageNum() );
        return ApiMessageUtil.success( result );
    }

    @Override
    public ApiMessage get(Dto dto) {
        Dto resDto = new HashDto();
        String id = dto.getString( "id" );
        TSocialActivity socialActivity = tSocialActivityMapper.selectByPrimaryKey( id );
        resDto.put( "id",socialActivity.getId() );
        resDto.put( "title",socialActivity.getTitle() );
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
            resDto.put( "pictureUrl","" );
        }else {
            resDto.put( "pictureUrl",JSONArray.fromObject( list )  );
        }
        resDto.put( "publisher",socialActivity.getPublisherId());
        resDto.put( "publishTime",socialActivity.getPublishTime().getTime() );
        resDto.put( "province",socialActivity.getProvince() );
        resDto.put( "city",socialActivity.getCity() );
        resDto.put( "district",socialActivity.getDistrict() );
        resDto.put( "address",socialActivity.getAddress() );
        resDto.put( "activityDesc",socialActivity.getActivityDesc() );
        resDto.put( "isHearted",socialActivity.getIsHearted() );
        resDto.put( "isChecked",socialActivity.getIsChecked() );
        resDto.put( "checkDesc",socialActivity.getCheckDesc() );
        resDto.put( "status",socialActivity.getStatus() );
        resDto.put( "beginTime",socialActivity.getBeginTime().getTime() );
        resDto.put( "endTime",socialActivity.getEndTime().getTime()  );
        String publisherId = socialActivity.getPublisherId();
        TUser publisher = tUserMapper.selectByPrimaryKey( publisherId );
        resDto.put( "publisherUserName",publisher.getUserName() );
        resDto.put( "publisherHeadImg",publisher.getHeadImg() );
        resDto.put( "publisherPhoneNum",publisher.getPhoneNum());
        resDto.put( "publisherNickName",publisher.getNickName() );
        TSocialActivityRelationshipExample example = new TSocialActivityRelationshipExample();
        TSocialActivityRelationshipExample.Criteria criteria2 = example.createCriteria();
        criteria2.andSaIdEqualTo( socialActivity.getId() ).andIsValidEqualTo( "1" );
        List<TSocialActivityRelationship> relationships = tSocialActivityRelationshipMapper.selectByExample( example );
        resDto.put( "subscriber_count",relationships.size() +"");
        if("1".equals( socialActivity.getIsChecked() )){
            String checkerId = socialActivity.getChecker();
            TUser checker=tUserMapper.selectByPrimaryKey( checkerId );
            resDto.put( "checkerUserName",checker.getUserName() );
            resDto.put( "checkerPhoneNum",checker.getPhoneNum());
            resDto.put( "checkerNickName",checker.getNickName() );
        }
        TCompany company = tCompanyMapper.selectByPrimaryKey( socialActivity.getId() );
        String companyName = "";
        if(company!=null){
           companyName = company.getCompanyName();
        }
         resDto.put( "companyName",companyName);
        return ApiMessageUtil.success( resDto );
    }

    @Override
    public ApiMessage update(Dto dto) throws ApiException{
        String id = dto.getString( "id" );
        String status = dto.getString( "status" );
        String checkDesc = dto.getString( "checkDesc" );
        String userId = dto.getString( "userId" );
        String serviceCode = dto.getString( "serviceCode" );
        TSocialActivity socialActivity = tSocialActivityMapper.selectByPrimaryKey( id );
        if ("CANCEL".equals( serviceCode )){
            socialActivity.setStatus( status );
            tSocialActivityMapper.updateByPrimaryKey( socialActivity );
        }else if("CHECK".equals( serviceCode )){
            socialActivity.setIsChecked( "1" );
            socialActivity.setStatus( status );
            socialActivity.setChecker( userId );
            socialActivity.setCheckDesc( checkDesc );
            socialActivity.setCheckTime( new Date( System.currentTimeMillis() ) );
            tSocialActivityMapper.updateByPrimaryKey( socialActivity );
        }
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage checkStatus(Dto dto) {
        String said = dto.getString( "saId" );
        TSocialActivity tSocialActivity = tSocialActivityMapper.selectByPrimaryKey( said );
        Dto result = new HashDto();
        result.put( "status",tSocialActivity.getStatus() );
        return ApiMessageUtil.success( result );
    }

    @Override
    public ApiMessage cancel(Dto dto) throws Exception {
        String id = dto.getString( "id" );
        TSocialActivity socialActivity = tSocialActivityMapper.selectByPrimaryKey( id );
        socialActivity.setStatus( "3" );
        tSocialActivityMapper.updateByPrimaryKey( socialActivity );
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage check(Dto dto) {
        String id = dto.getString( "id" );
        String status = dto.getString( "status" );
        String checkDesc = dto.getString( "checkDesc" );
        String userId = dto.getString( "userId" );
        TSocialActivity socialActivity = tSocialActivityMapper.selectByPrimaryKey( id );
        socialActivity.setIsChecked( "1" );
        socialActivity.setStatus( status );
        socialActivity.setChecker( userId );
        socialActivity.setCheckDesc( checkDesc );
        socialActivity.setCheckTime( new Date( System.currentTimeMillis() ) );
        tSocialActivityMapper.updateByPrimaryKey( socialActivity );
        return ApiMessageUtil.success(  );
    }

    @Override
    public ApiMessage checkList(Dto dto) {
        JSONObject jsonObject = JSONObject.fromObject( dto );
        String status = jsonObject.getString( "status" );
        String checkDesc = jsonObject.getString( "checkDesc" );
        String userId = jsonObject.getString( "userId" );
        JSONArray ids = jsonObject.getJSONArray( "ids" );
        if(ids.size()>0){
            for(int i=0;i<ids.size();i++){
                String id = ids.getString(i);
                Dto dto4DB = new HashDto();
                dto4DB.put( "id",id );
                dto4DB.put( "status",status );
                dto4DB.put( "userId",userId );
                dto4DB.put( "checkDesc",checkDesc );
                this.check(dto4DB);
            }
        }
        return ApiMessageUtil.success(  );
    }


}
