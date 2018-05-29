package com.dominator.weixin.serviceImpl;

import com.dominator.entity.TblWxopenPreAuthCode;
import com.dominator.entity.TblWxopenPreAuthCodeExample;
import com.dominator.mapper.TblWxopenPreAuthCodeMapper;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.ComponentAccessTokenService;
import com.dominator.weixin.service.PreAuthCodeService;
import com.dominator.weixin.util.HttpUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreAuthCodeServiceImpl implements PreAuthCodeService {

    @Autowired
    private TblWxopenPreAuthCodeMapper tblWxopenPreAuthCodeMapper;

    @Autowired
    private ComponentAccessTokenService componentAccessTokenService;

    /**
     * 获取PreAuthCode的值
     */
    @Override
    public String getPreAuthCode(String compId) throws PMSaaSException {

        //判断数据库中是否存在预授权码，若是存在，判断过期，若是不存在或是过期，重新获取预授权码
        // List<PreAuthCode> preAuthCodes = this.getAllPreAuthCode();
//        boolean expire4Bol = false;

        //若数据库中无预授权码或是授权码已经过期，则重新请求新的授权码
        // if (CollectionUtils.isEmpty( preAuthCodes )|| expire4Bol) {
        //获取component_access_token
        String componentAccessToken = this.componentAccessTokenService.getComponentAccessToken(compId);
        String preAuthCodeURL = WXConfig.CREATE_PREAUTHCODE_URL.replace("xxx", componentAccessToken);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("component_appid", compId);

        JSONObject result = HttpUtils.doPost(preAuthCodeURL, jsonObject);
        if (result.get("pre_auth_code") == null) {
            throw new PMSaaSException(result.toString());
        }
        //更新数据库中的预授权码信息
        TblWxopenPreAuthCode preAuthCode = new TblWxopenPreAuthCode();
        preAuthCode.setCreateTime(System.currentTimeMillis());
        preAuthCode.setPreAuthCode(result.get("pre_auth_code").toString());
        preAuthCode.setExpiresIn(Integer.parseInt(result.getString("expires_in")));

        List<TblWxopenPreAuthCode> preAuthCodeList = tblWxopenPreAuthCodeMapper.selectByExample(new TblWxopenPreAuthCodeExample());
        if (preAuthCodeList != null && preAuthCodeList.size() > 0) {
            TblWxopenPreAuthCode preAuthCode1 = preAuthCodeList.get(0);
            preAuthCode.setId(preAuthCode1.getId());
            tblWxopenPreAuthCodeMapper.updateByPrimaryKeySelective(preAuthCode);
        } else {
            tblWxopenPreAuthCodeMapper.insertSelective(preAuthCode);
        }
        return result.getString("pre_auth_code");
    }

//    @Override
//    public List<PreAuthCode> getAllPreAuthCode(){
//        return this.preAuthCodeDao.getAllPreAuthCode();
//    }
//
//    @Override
//    public int insert(PreAuthCode PreAuthCode) {
//        if(PreAuthCode!=null){
//            this.preAuthCodeDao.insert(PreAuthCode);
//        }
//        return 0;
//    }
//
//    @Override
//    public int update(PreAuthCode PreAuthCode) {
//        List<PreAuthCode> PreAuthCodes=this.getAllPreAuthCode();
//        if(PreAuthCode!=null){
//            if(PreAuthCodes.size()>0){
//                PreAuthCode preAuthCode4List=PreAuthCodes.get(0);
//                PreAuthCode.setId(preAuthCode4List.getId());
//                this.preAuthCodeDao.update(preAuthCode4List);
//            }else {
//                this.preAuthCodeDao.insert(PreAuthCode);
//            }
//            return 1;
//        }
//        return 0;
//    }

}
