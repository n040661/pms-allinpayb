//package com.dominator.weixin.serviceImpl;
//
//import com.dominator.mybatis.dao.weixin.WebpageAccessToken;
//import com.dominator.mybatis.dao.weixin.WebpageAccessTokenDao;
//import com.dominator.weixin.service.WebpageAccessTokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class WebpageAccessTokenServiceImpl
//
//        implements WebpageAccessTokenService{
//
//    @Autowired
//    private WebpageAccessTokenDao webpageAccessTokenDao;
//
//    @Override
//    public void update(WebpageAccessToken webpageAccessToken){
//        WebpageAccessToken accessToken = this.webpageAccessTokenDao.getWebpageAccessTokenByOpenid( webpageAccessToken.getOpenid() );
//        if(accessToken!=null){
//            webpageAccessToken.setId( accessToken.getId() );
//            this.webpageAccessTokenDao.update( webpageAccessToken );
//        }else{
//            this.webpageAccessTokenDao.insert( webpageAccessToken );
//        }
//    }
//
//}
