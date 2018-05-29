//package com.dominator.weixin.serviceImpl;
//
//import com.dominator.mybatis.dao.weixin.AuthorizationInfo;
//import com.dominator.mybatis.dao.weixin.AuthorizationInfoDao;
//import com.dominator.weixin.service.AuthorizationInfoMapperService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * Created by zhangsuliang on 2017/8/25.
// */
//
//@Service
//public class AuthorizationInfoMapperServiceImpl implements AuthorizationInfoMapperService {
//
//    @Autowired
//    private AuthorizationInfoDao authorizationInfoDao;
//
//    @Override
//    public AuthorizationInfo getAuthorizationInfoByAppId(String appId){
//        return this.authorizationInfoDao.getAuthorizationInfoByAuthAppid(appId);
//    }
//
//    @Override
//    public int insert(AuthorizationInfo authorizationInfo) {
//        if(authorizationInfo!=null){
//            this.authorizationInfoDao.insert(authorizationInfo);
//        }
//        return 0;
//    }
//
//    @Override
//    public int update(AuthorizationInfo authorizationInfo) {
//        AuthorizationInfo authorizationInfoTemp = this.getAuthorizationInfoByAppId( authorizationInfo.getAuth_app_id() );
//        if(authorizationInfoTemp!=null){
//            authorizationInfo.setId( authorizationInfoTemp.getId() );
//            this.authorizationInfoDao.update( authorizationInfo );
//            return  1;
//        }else{
//            this.insert( authorizationInfo );
//            return 1;
//        }
//    }
//
//
//
//}
