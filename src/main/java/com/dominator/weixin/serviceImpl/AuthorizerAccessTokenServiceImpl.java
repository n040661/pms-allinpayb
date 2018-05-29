//package com.dominator.weixin.serviceImpl;
//
//import com.dominator.mybatis.dao.weixin.AuthorizerAccessToken;
//import com.dominator.mybatis.dao.weixin.AuthorizerAccessTokenDao;
//import com.dominator.weixin.service.AuthorizerAccessTokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthorizerAccessTokenServiceImpl implements AuthorizerAccessTokenService {
//
//    @Autowired
//    private AuthorizerAccessTokenDao authorizerAccessTokenDao;
//
//    public int insert(AuthorizerAccessToken AuthorizerAccessToken) {
//        if (AuthorizerAccessToken != null) {
//            this.authorizerAccessTokenDao.insert(AuthorizerAccessToken);
//        }
//        return 0;
//    }
//
//    public int update(AuthorizerAccessToken authorizerAccessToken) {
//        if(authorizerAccessToken.getId()>0){
//            this.authorizerAccessTokenDao.update(authorizerAccessToken);
//        }else{
//            this.authorizerAccessTokenDao.insert(authorizerAccessToken);
//        }
//        return 1;
//    }
//
//}
