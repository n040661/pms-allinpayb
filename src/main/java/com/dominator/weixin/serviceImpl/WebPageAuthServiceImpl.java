package com.dominator.weixin.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TblPublicAccount;
import com.dominator.entity.TblWeixinUser;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TblPublicAccountMapper;
import com.dominator.mapper.TblWeixinUserMapper;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.ComponentAccessTokenService;
import com.dominator.weixin.service.WebPageAuthService;
import com.dominator.weixin.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WebPageAuthServiceImpl implements WebPageAuthService {

    @Autowired
    private TblWeixinUserMapper tblWeixinUserMapper;

    @Autowired
    private TblPublicAccountMapper tblPublicAccountMapper;

    @Autowired
    private ComponentAccessTokenService componentAccessTokenService;

    @Override
    @Transactional
    public ApiMessage getWeiXinUserInfo(Dto dto) throws PMSaaSException {
        String appid = dto.getString("appid");
        String code = dto.getString("code");
        String compAppid = dto.getString("component_appid");
        Map<String, String> token = getWebpaeAccessToken(appid, code, compAppid);
        if(token == null)
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR);
        //拼接URL
        String url = WXConfig.WX_USER_INFO_URL + token.get("webpage_access_token") + "&openid=" + token.get("openid") + "&lang=zh_CN";
        JSONObject jsonObject = HttpUtils.doPost(url, new JSONObject());
        //保存用户信息
        jsonObject.put("appid", appid);

        TblWeixinUser weixinUser = new TblWeixinUser();
        weixinUser.setOpenid(jsonObject.get("openid").toString());
        weixinUser.setNickname("");
        weixinUser.setSex(Integer.parseInt(jsonObject.get("sex").toString()));
        weixinUser.setLanguage(jsonObject.get("language").toString());
        weixinUser.setCity(jsonObject.get("city").toString());
        weixinUser.setProvince(jsonObject.get("province").toString());
        weixinUser.setCountry(jsonObject.get("country").toString());
        weixinUser.setHeadimgurl(jsonObject.get("headimgurl").toString());
        weixinUser.setPrivilege(jsonObject.get("privilege").toString());
        weixinUser.setAppid(jsonObject.getString("appid"));
        tblWeixinUserMapper.updateByPrimaryKeySelective(weixinUser);

        log.info("【getWeiXinUserInfo接口返回结果为：" + jsonObject + "】");
        return ApiMessageUtil.success(jsonObject);
    }

    @Override
    public ApiMessage getWeiXinUserInfo2(Dto dto) throws PMSaaSException {
        log.info("【getWeiXinUserInfo2公众号获取微信用户信息，携带参数：" + dto + "】");
        String appid = dto.getString("appid");
        String code = dto.getString("code");
        Map<String, String> token = getWebpaeAccessToken(appid, code);
        if(token == null)
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR);
        //拼接URL
        String url = WXConfig.WX_USER_INFO_URL + token.get("webpage_access_token") + "&openid=" + token.get("openid") + "&lang=zh_CN";
        JSONObject jsonObject = HttpUtils.doPost(url, new JSONObject());
        //保存用户信息
        jsonObject.put("appid", appid);

        TblWeixinUser weixinUser = new TblWeixinUser();
        weixinUser.setOpenid(jsonObject.get("openid").toString());
        weixinUser.setNickname("");
        weixinUser.setSex(Integer.parseInt(jsonObject.get("sex").toString()));
        weixinUser.setLanguage(jsonObject.get("language").toString());
        weixinUser.setCity(jsonObject.get("city").toString());
        weixinUser.setProvince(jsonObject.get("province").toString());
        weixinUser.setCountry(jsonObject.get("country").toString());
        weixinUser.setHeadimgurl(jsonObject.get("headimgurl").toString());
        weixinUser.setPrivilege(jsonObject.get("privilege").toString());
        weixinUser.setAppid(jsonObject.getString("appid"));
        tblWeixinUserMapper.updateByPrimaryKeySelective(weixinUser);

        log.info("【getWeiXinUserInfo接口返回结果为：" + jsonObject + "】");
        return ApiMessageUtil.success(jsonObject);
    }

//    public Tbl_weixin_userPO jsonConvertToEntity(JSONObject jsonObject){
//        if(jsonObject.isEmpty()){
//            return  null;
//        }
//        Tbl_weixin_userPO tbl_weixin_userPO=new Tbl_weixin_userPO();
//        tbl_weixin_userPO.setOpenid(jsonObject.get("openid").toString());
//        tbl_weixin_userPO.setNickname("");
//        tbl_weixin_userPO.setSex(Integer.parseInt(jsonObject.get("sex").toString()));
//        tbl_weixin_userPO.setLanguage(jsonObject.get("language").toString());
//        tbl_weixin_userPO.setCity(jsonObject.get("city").toString());
//        tbl_weixin_userPO.setProvince(jsonObject.get("province").toString());
//        tbl_weixin_userPO.setCountry(jsonObject.get("country").toString());
//        tbl_weixin_userPO.setHeadimgurl(jsonObject.get("headimgurl").toString());
//        tbl_weixin_userPO.setPrivilege(jsonObject.get("privilege").toString());
//        tbl_weixin_userPO.setAppid(jsonObject.getString("appid"));
//        return tbl_weixin_userPO;
//    }

    private Map<String, String> getWebpaeAccessToken(String appid, String code, String comAppid) throws PMSaaSException {
        Map<String, String> map = new HashMap<>();
        //拼接URL
        String url = WXConfig.WEBPAGE_ACCESS_TOKEN_URL + "appid=" + appid + "&code=" + code +
                "&grant_type=authorization_code" +
                "&component_appid=" + comAppid +
                "&component_access_token=" +
                componentAccessTokenService.getComponentAccessToken(comAppid);
        JSONObject jsonObject = HttpUtils.doPost(url, new JSONObject());
        String accessToken = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        if (accessToken != null && openid != null) {
            map.put("webpage_access_token", accessToken);
            map.put("openid", openid);
            return map;
        }
        return null;
    }


    private Map<String, String> getWebpaeAccessToken(String appid, String code) throws PMSaaSException {
        Map<String, String> map = new HashMap<>();
        TblPublicAccount account = tblPublicAccountMapper.selectByPrimaryKey(appid);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + account.getAppSecret() + "&code=" + code + "&grant_type=authorization_code";
        JSONObject jsonObject = HttpUtils.doPost(url, new JSONObject());
        log.info("[获取access_token接口结果]" + jsonObject);
        String accessToken = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        if (accessToken != null && openid != null) {
            map.put("webpage_access_token", accessToken);
            map.put("openid", openid);
            return map;
        }
        return null;
    }
}
