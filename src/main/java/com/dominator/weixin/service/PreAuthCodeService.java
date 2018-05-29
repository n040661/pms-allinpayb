package com.dominator.weixin.service;

import com.dominator.weixin.exception.PMSaaSException;

import java.util.List;

/**
 * Created by zhangsuliang on 2017/8/21.
 */
public interface PreAuthCodeService {

     String getPreAuthCode(String compId)  throws PMSaaSException ;
//     List<PreAuthCode> getAllPreAuthCode() ;
//     int insert(PreAuthCode PreAuthCode) ;
//     int update(PreAuthCode PreAuthCode) ;

}
