//package com.dominator.weixin.serviceImpl;
//
//import com.dominator.entity.TblWxopenAuthorizerAccountInfo;
//import com.dominator.mapper.TblWxopenAuthorizerAccountInfoMapper;
//import com.dominator.weixin.service.AuthorizerAccountService;
//import com.dominator.weixin.service.ComponentAccessTokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthorizerAccountServiceImpl implements AuthorizerAccountService{
//
//    @Autowired
//    private ComponentAccessTokenService componentAccessTokenService;
//
//    @Autowired
//    private TblWxopenAuthorizerAccountInfoMapper tblWxopenAuthorizerAccountInfoMapper;
//
//
//    @Override
//    public JSONObject getAuthorizerAccountInfo(String componentAppid, String authorizerAppId) throws PMSaaSException {
//        String  AUTHORIZER_ACOUNT_INFO_URL="https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_acess_token="
//                +this.componentAccessTokenService.getComponentAccessToken(componentAppid);
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("component_appid",componentAppid);
//        jsonObject.put("authorizer_appid",authorizerAppId);
//        return HttpUtils.doPost( AUTHORIZER_ACOUNT_INFO_URL, jsonObject );
//    }
//
//
//    @Override
//    public void insert(TblWxopenAuthorizerAccountInfo tbl_wxopen_authorizer_account_infoPO) {
//        this.tbl_wxopen_authorizer_account_infoDao.insert(tbl_wxopen_authorizer_account_infoPO);
//    }
//
//    @Override
//    public Tbl_wxopen_authorizer_account_infoPO getAuthorizerAccountInfoPO(Dto dto) {
//        return this.tbl_wxopen_authorizer_account_infoDao.selectOne(dto);
//    }
//
//    @Override
//    public void update(TblWxopenAuthorizerAccountInfo tbl_wxopen_authorizer_account_infoPO) {
//      this.tbl_wxopen_authorizer_account_infoDao.updateByKey(tbl_wxopen_authorizer_account_infoPO);
//    }
//}
