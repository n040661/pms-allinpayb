//package com.dominator.weixin.serviceImpl;
//
//import com.dominFramework.core.typewrap.Dto;
//import com.dominFramework.core.typewrap.impl.HashDto;
//import com.dominator.enums.ReqEnums;
//import com.dominator.mybatis.dao.Tbl_weixin_userDao;
//import com.dominator.mybatis.dao.Tbl_weixin_userPO;
//import com.dominator.utils.api.ApiMessage;
//import com.dominator.utils.api.ApiMessageUtil;
//import com.dominator.utils.exception.ApiException;
//import com.dominator.utils.system.PrimaryGenerater;
//import com.dominator.weixin.service.WeixinUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.logging.Logger;
//
//@Service
//public class WeixinUserServiceImpl implements WeixinUserService {
//
//    private static Logger logger = Logger.getLogger( WeixinUserServiceImpl.class.getName() );
//
//    @Autowired
//    private Tbl_weixin_userDao tbl_weixin_userDao;
//
//
//    @Override
//    public void insert(Tbl_weixin_userPO tbl_weixin_userPO) {
//        try {
//            this.tbl_weixin_userDao.insert(tbl_weixin_userPO);
//        }catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }
//
//    @Override
//    public Tbl_weixin_userPO getWeixinUserByOpenid(Dto dto) {
//        return this.tbl_weixin_userDao.selectOne(dto);
//    }
//
//    @Override
//    public ApiMessage update(Tbl_weixin_userPO tbl_weixin_userPO) {
//        try {
//            Dto dto=new HashDto();
//            dto.put("openid",tbl_weixin_userPO.getOpenid());
//            dto.put("appid",tbl_weixin_userPO.getAppid());
//            Tbl_weixin_userPO weixinUser = this.getWeixinUserByOpenid(dto);
//
//            if (weixinUser == null) {
//                tbl_weixin_userPO.setId(PrimaryGenerater.getInstance().uuid());
//                this.tbl_weixin_userDao.insert(tbl_weixin_userPO);
//            } else {
//                tbl_weixin_userPO.setId(weixinUser.getId());
//                tbl_weixin_userPO.setUser_id(weixinUser.getUser_id());
//                this.tbl_weixin_userDao.updateByKey(tbl_weixin_userPO);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//        return ApiMessageUtil.success(tbl_weixin_userPO);
//    }
//
//}
