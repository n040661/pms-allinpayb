package com.dominator.weixin.serviceImpl;

import com.dominator.entity.TblWxopenCompAccessToken;
import com.dominator.entity.TblWxopenCompAccessTokenExample;
import com.dominator.entity.TblWxopenComponentVerifyTicket;
import com.dominator.mapper.TblWxopenCompAccessTokenMapper;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.exception.PMSaaSException;
import com.dominator.weixin.service.ComponentAccessTokenService;
import com.dominator.weixin.service.ComponentVerifyTicketService;
import com.dominator.weixin.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ComponentAccessTokenServiceImpl implements ComponentAccessTokenService {

    @Autowired
    private TblWxopenCompAccessTokenMapper tblWxopenCompAccessTokenMapper;

    @Autowired
    private ComponentVerifyTicketService componentVerifyTicketService;

    public String getComponentAccessToken(String compAppId) throws PMSaaSException {
        //获取component_verify_ticket值
        TblWxopenComponentVerifyTicket componentVerifyTicket = componentVerifyTicketService.getComponentVerifyTicket(compAppId);
        //获取componentAccessToken的值
        // ComponentAccessToken componentAccessToken = null;
        //List<ComponentAccessToken> componentAccessTokens = this.getAllComponentAccessToken();
        //boolean bol4token = true;
        /*if (!CollectionUtils.isEmpty( componentAccessTokens ) || componentAccessTokens.size() > 0) {
         componentAccessToken = componentAccessTokens.get( 0 );
         long createTime4Token = componentAccessToken.getCreateTime();
         long expiresIn = componentAccessToken.getExpiresIn();
         long currentTiem = System.currentTimeMillis();
         if (((currentTiem - createTime4Token) / 1000) >= (expiresIn - 300)) {
         bol4token = true;
         } else {
         return componentAccessToken.getComponetAccessToken();
         }
         }**/
        //若数据库中无预componentAccessToken或是授权码已经过期，则重新请求新的componentAccessToken
        // if (CollectionUtils.isEmpty( componentAccessTokens ) ) {
        //拼装待发送的Json
        JSONObject json = new JSONObject();
        json.accumulate("component_appid", compAppId);
        json.accumulate("component_appsecret", WXConfig.getApiSecret(compAppId));
        json.accumulate("component_verify_ticket", componentVerifyTicket.getComponentVerifyTicket());
        //发送Https请求到微信
        JSONObject jsonObject = HttpUtils.doPost(WXConfig.COMPONENT_TOKEN_URL, json);
        log.info("====获取微信access_token====" + jsonObject);
        if (jsonObject.get("component_access_token") == null) {
            throw new PMSaaSException(jsonObject.toString());
        }
        TblWxopenCompAccessToken componentAccessToken4DB = new TblWxopenCompAccessToken();
        componentAccessToken4DB.setCreateTime(System.currentTimeMillis());
        componentAccessToken4DB.setComponetAccessToken(jsonObject.get("component_access_token").toString());
        componentAccessToken4DB.setExpiresIn(Long.valueOf(jsonObject.get("expires_in").toString()));
        log.info("=" + componentAccessToken4DB);

        List<TblWxopenCompAccessToken> compAccessTokenList = tblWxopenCompAccessTokenMapper.selectByExample(new TblWxopenCompAccessTokenExample());
        if (compAccessTokenList != null) {
            if (!compAccessTokenList.isEmpty()) {
                TblWxopenCompAccessToken compAccessToken = compAccessTokenList.get(0);
                componentAccessToken4DB.setId(compAccessToken.getId());
                tblWxopenCompAccessTokenMapper.updateByPrimaryKeySelective(componentAccessToken4DB);
            } else {
                tblWxopenCompAccessTokenMapper.insertSelective(componentAccessToken4DB);
            }
        }
        return jsonObject.getString("component_access_token");
    }
}
