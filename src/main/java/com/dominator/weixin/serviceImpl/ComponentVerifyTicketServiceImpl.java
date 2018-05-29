package com.dominator.weixin.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.TblWxopenAuthorizerAccountInfo;
import com.dominator.entity.TblWxopenAuthorizerAccountInfoExample;
import com.dominator.entity.TblWxopenComponentVerifyTicket;
import com.dominator.entity.TblWxopenComponentVerifyTicketExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TblWxopenAuthorizerAccountInfoMapper;
import com.dominator.mapper.TblWxopenComponentVerifyTicketMapper;
import com.dominator.utils.exception.ApiException;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.AuthorizationCodeService;
import com.dominator.weixin.service.ComponentAccessTokenService;
import com.dominator.weixin.service.ComponentVerifyTicketService;
import com.dominator.weixin.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ComponentVerifyTicketServiceImpl implements ComponentVerifyTicketService {

    @Autowired
    private TblWxopenComponentVerifyTicketMapper tblWxopenComponentVerifyTicketMapper;

    @Autowired
    private TblWxopenAuthorizerAccountInfoMapper tblWxopenAuthorizerAccountInfoMapper;

    @Autowired
    private AuthorizationCodeService authorizationCodeService;

    @Autowired
    private ComponentAccessTokenService componentAccessTokenService;

    @Override
    public TblWxopenComponentVerifyTicket getComponentVerifyTicket(String compAppid) throws PMSaaSException {
        TblWxopenComponentVerifyTicketExample componentVerifyTicketExample = new TblWxopenComponentVerifyTicketExample();
        componentVerifyTicketExample.or().andComponentIdEqualTo(compAppid);
        List<TblWxopenComponentVerifyTicket> componentVerifyTicketList =
                tblWxopenComponentVerifyTicketMapper.selectByExample(componentVerifyTicketExample);
        TblWxopenComponentVerifyTicket componentVerifyTicket = new TblWxopenComponentVerifyTicket();
        if (componentVerifyTicketList != null && !componentVerifyTicketList.isEmpty())
            componentVerifyTicket = componentVerifyTicketList.get(0);

        log.info("【查询开放平台component_appid(" + compAppid + ")信息：" + componentVerifyTicket + "】");
        if (componentVerifyTicket != null) {
            //验证component_verify_ticket是否过期
            long currentTime = System.currentTimeMillis();
            long createTime = componentVerifyTicket.getCreateTime();
            if ((((currentTime / 1000) - createTime) / 1000 / 60) > 10) {
                throw new PMSaaSException("component_verify_ticket已过期");
            }
        } else {
            return null;
        }
        return componentVerifyTicket;
    }

    //
//    @Override
//    public ComponentVerifyTicket getComponentVerifyTicket() throws PMSaaSException {
//        List<ComponentVerifyTicket> componentVerifyTickets = null;
//        ComponentVerifyTicket componentVerifyTicket=null;
//        if(CollectionUtils.isEmpty( componentVerifyTickets )){
//            throw new PMSaaSException( "component_verify_ticket值为空" );
//        }else{
//            componentVerifyTicket = componentVerifyTickets.get( 0 );
//            //验证component_verify_ticket是否过期
//            long currentTime=System.currentTimeMillis();
//            long createTime=componentVerifyTicket.getCreate_time();
//            if((((currentTime/1000)-createTime)/1000/60)>10) {
//                throw new PMSaaSException( PMSaaSErrorCodeType.COMPONENT_VERYFY_TICKET_EXPIRE );
//            }
//        }
//        return componentVerifyTicket;
//    }
//
    @Override
    public void saveWeiXinCallbackInfo(Map<String, String> decryptMsgMap) throws PMSaaSException {
        String createTime = decryptMsgMap.get("CreateTime");
        String infoType = decryptMsgMap.get("InfoType");
        if ("component_verify_ticket".equals(infoType)) {
            //获取component_verify_ticket并保存在数据库中
            TblWxopenComponentVerifyTicket ticket = new TblWxopenComponentVerifyTicket();
            ticket.setComponentVerifyTicket(decryptMsgMap.get("ComponentVerifyTicket"));
            ticket.setCreateTime(Long.parseLong(createTime));
            ticket.setUpdateTime(System.currentTimeMillis());
            ticket.setComponentId(decryptMsgMap.get("AppId"));

            TblWxopenComponentVerifyTicket ticket4DB = getComponentVerifyTicket(ticket.getComponentId());
            if (ticket4DB == null) {
                tblWxopenComponentVerifyTicketMapper.insertSelective(ticket);
            } else {
                ticket.setId(ticket4DB.getId());
                tblWxopenComponentVerifyTicketMapper.updateByPrimaryKeySelective(ticket);
            }
//            TblWxopenComponentVerifyTicketExample componentVerifyTicketExample = new TblWxopenComponentVerifyTicketExample();
//            componentVerifyTicketExample.or().andComponentIdEqualTo(decryptMsgMap.get("AppId"));
//            tblWxopenComponentVerifyTicketMapper.deleteByExample(componentVerifyTicketExample);
//            tblWxopenComponentVerifyTicketMapper.insertSelective(ticket);

        } else if ("authorized".equals(infoType) || "updateauthorized".equals(infoType)
                || "unauthorized".equals(infoType)) {
            //保存授权码
            authorizationCodeService.update(authorizationCodeService.getAuthorizationCodeByInfoType(decryptMsgMap, infoType));
            //保存授权方信息
            saveOrupdateAuthorizaitionInfo(decryptMsgMap);
        }
    }

    @Override
    public void saveOrupdateAuthorizaitionInfo(Map<String, String> decryptMsgMap) throws PMSaaSException {
        String mesURL = WXConfig.GET_AUTHORIZER_INFO_URL.replace("xxxx",
                componentAccessTokenService.getComponentAccessToken(decryptMsgMap.get("AppId")));
        JSONObject jsonObjectTemp = new JSONObject();
        jsonObjectTemp.put("component_appid", decryptMsgMap.get("AppId"));
        jsonObjectTemp.put("authorizer_appid", decryptMsgMap.get("AuthorizerAppid"));
        JSONObject msg = HttpUtils.doPost(mesURL, jsonObjectTemp);
        org.json.JSONObject jsonObject = new org.json.JSONObject(msg);
        org.json.JSONObject authorizerInfo = jsonObject.getJSONObject("authorizer_info");
        String principalName = authorizerInfo.getString("principal_name");
        org.json.JSONObject authorizationInfo = jsonObject.getJSONObject("authorization_info");
        org.json.JSONArray funcInfo = authorizationInfo.getJSONArray("func_info");
        //封装对象
        TblWxopenAuthorizerAccountInfo infoPO = new TblWxopenAuthorizerAccountInfo();
        infoPO.setAlias(authorizerInfo.getString("alias"));
        infoPO.setQrCodeUrl(authorizerInfo.getString("qrcode_url"));
        infoPO.setBusinessInfo(authorizerInfo.getJSONObject("business_info").toString());
        infoPO.setNickName(authorizerInfo.getString("nick_name"));
        infoPO.setHeadImg(authorizerInfo.getString("head_img"));
        infoPO.setUserName(authorizerInfo.getString("user_name"));
        infoPO.setServiceTypeInfo(authorizerInfo.getJSONObject("service_type_info").toString());
        infoPO.setVerifyTypeInfo(authorizerInfo.getJSONObject("verify_type_info").toString());
        infoPO.setIdc(authorizerInfo.getInt("idc") + "");
        infoPO.setPrincipalName(principalName);
        infoPO.setSignature(authorizerInfo.getString("signature"));
        infoPO.setAppId(authorizationInfo.getString("authorizer_appid"));
        infoPO.setRefreshToken(authorizationInfo.getString("authorizer_refresh_token"));
        infoPO.setFuncInfo(authorizationCodeService.jsonArrayToString(funcInfo).toString());

        if (decryptMsgMap.get("InfoType").equalsIgnoreCase("unauthorized")) {
            infoPO.setAuthed("0");
        } else {
            infoPO.setAuthed("1");
        }

        TblWxopenAuthorizerAccountInfoExample authorizerAccountInfoExample = new TblWxopenAuthorizerAccountInfoExample();
        authorizerAccountInfoExample.or().andAppIdEqualTo(infoPO.getAppId());
        List<TblWxopenAuthorizerAccountInfo> accountInfoList =
                tblWxopenAuthorizerAccountInfoMapper.selectByExample(authorizerAccountInfoExample);
        TblWxopenAuthorizerAccountInfo infoPO4DB;
        if (accountInfoList == null || accountInfoList.isEmpty()) {
            log.info("保存授权信息前：" + infoPO.toString());
            tblWxopenAuthorizerAccountInfoMapper.insertSelective(infoPO);
        } else {
            infoPO4DB = accountInfoList.get(0);
            infoPO.setId(infoPO4DB.getId());
            log.info("更新授权信息前：" + infoPO4DB);
            tblWxopenAuthorizerAccountInfoMapper.updateByPrimaryKeySelective(infoPO);
        }

    }
//
//    /**
//     * 根据条件查询第三方应用的配置信息
//     *
//     * @param appId      第三方appId
//     * @param authAppId  公众号ID
//     * @param optionName 选项名称
//     * @return
//     */
//    @Override
//    public JSONObject getetAuthorizerOption(String appId, String authAppId, String optionName) throws PMSaaSException {
//        String componentAccessToken = this.componentAccessTokenService.getComponentAccessToken(appId);
//        String authSettingURL = WXConfig.GET_AUTHORIZER_OPTION_URL.replace( "xxxx", componentAccessToken );
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put( "component_appid", appId );
//        jsonObject.put( "authorizer_appid", authAppId );
//        jsonObject.put( "option_name", optionName );
//        return HttpUtils.doPost( authSettingURL, jsonObject );
//    }
//
//    /**
//     * 配置选项信息
//     *
//     * @param appId       第三方appId
//     * @param authAppId   公众号Id
//     * @param optionName  选项名称
//     * @param optionValue 选项值
//     * @return
//     */
//    @Override
//    public JSONObject setAuthorizerOption(String appId, String authAppId, String optionName, String optionValue) throws PMSaaSException {
//        String authorizerOptionSetURL = WXConfig.SET_AUTHORIZER_OPTION_URL.replace( "xxxx",
//                this.componentAccessTokenService.getComponentAccessToken(appId) );
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put( "component_appid", appId );
//        jsonObject.put( "authorizer_appid", authAppId );
//        jsonObject.put( "option_name", optionName );
//        jsonObject.put( "option_value", optionValue );
//        return HttpUtils.doPost( authorizerOptionSetURL, jsonObject );
//    }
//
//    /**
//     * 获取authorizer_access_token
//     *
//     * 获取授权方的帐号基本信息
//     *
//     * @param appId     第三方appId
//     * @param authAppId 公众号Id
//     * @return
//     */
//    @Override
//    public JSONObject getAuthorizerInfo(String appId, String authAppId) throws Exception {
//        String getAuthorizerInfoURL = WXConfig.GET_AUTHORIZER_INFO_URL.replace( "xxxx",
//                this.componentAccessTokenService.getComponentAccessToken(appId) );
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put( "component_appid", appId );
//        jsonObject.put( "authorizer_appid", authAppId );
//        return HttpUtils.doPost( getAuthorizerInfoURL, jsonObject );
//    }
}
