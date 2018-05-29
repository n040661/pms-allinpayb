//package com.dominator.weixin.serviceImpl;
//
//import com.dominFramework.core.typewrap.Dto;
//import com.dominFramework.core.typewrap.impl.HashDto;
//import com.dominator.enums.ReqEnums;
//import com.dominator.mybatis.dao.Tbl_public_accountDao;
//import com.dominator.mybatis.dao.Tbl_public_accountPO;
//import com.dominator.utils.api.ApiMessage;
//import com.dominator.utils.api.ApiMessageUtil;
//import com.dominator.utils.exception.ApiException;
//import com.dominator.weixin.service.PublicAccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PublicAccountServiceImpl implements PublicAccountService {
//
//    @Autowired
//    private Tbl_public_accountDao tbl_public_accountDao;
//    @Override
//    public ApiMessage saveOrUpdat(Tbl_public_accountPO tbl_public_accountPO) {
//        String mch_id = tbl_public_accountPO.getMch_id();
//        Dto dto=new HashDto();
//        dto.put("mch_id",mch_id);
//        Tbl_public_accountPO accountPO = this.tbl_public_accountDao.selectOne(dto);
//        try {
//            if(accountPO==null){
//                this.tbl_public_accountDao.insert(tbl_public_accountPO);
//            }else{
//                accountPO.setAdmin(tbl_public_accountPO.getAdmin());
//                accountPO.setApi_cert_name(tbl_public_accountPO.getApi_cert_name());
//                accountPO.setApp_secret(tbl_public_accountPO.getApp_secret());
//                accountPO.setAppi_id(tbl_public_accountPO.getAppi_id());
//                accountPO.setMch_id(tbl_public_accountPO.getMch_id());
//                accountPO.setApi_keystore(tbl_public_accountPO.getApi_keystore());
//                this.tbl_public_accountDao.updateByKey(tbl_public_accountPO);
//            }
//        }catch (Exception e){
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//        return ApiMessageUtil.success();
//    }
//
//    @Override
//    public Tbl_public_accountPO find(String appId) {
//        return  this.tbl_public_accountDao.selectByKey(appId);
//    }
//}
