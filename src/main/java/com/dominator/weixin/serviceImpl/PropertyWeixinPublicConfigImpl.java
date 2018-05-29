//package com.dominator.weixin.serviceImpl;
//
//import com.dominator.enums.ReqEnums;
//import com.dominator.mybatis.dao.T_property_weixin_public_configDao;
//import com.dominator.utils.exception.ApiException;
//import com.dominator.weixin.service.PropertyWeixinPublicConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PropertyWeixinPublicConfigImpl implements PropertyWeixinPublicConfig {
//
//    @Autowired
//    private T_property_weixin_public_configDao t_property_weixin_public_configDao;
//    @Override
//    public void delete(String id) {
//       try{
//           this.t_property_weixin_public_configDao.deleteByKey(id);
//       }catch (Exception e){
//           e.printStackTrace();
//           throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//       }
//    }
//}
