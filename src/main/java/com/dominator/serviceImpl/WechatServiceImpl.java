package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TPropertyWeixinPublicConfigMapper;
import com.dominator.mapper.TblWxopenAuthorizerAccountInfoMapper;
import com.dominator.service.WechatService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.PrimaryGenerater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private TblWxopenAuthorizerAccountInfoMapper tblWxopenAuthorizerAccountInfoMapper;

    @Autowired
    private TPropertyWeixinPublicConfigMapper tPropertyWeixinPublicConfigMapper;

    @Override
    public String getPropertyIdByAppId(String appId) {
        String propertyId = "";
        TblWxopenAuthorizerAccountInfoExample accountInfoExample = new TblWxopenAuthorizerAccountInfoExample();
        accountInfoExample.or().andAppIdEqualTo(appId);
        List<TblWxopenAuthorizerAccountInfo> accountInfoList = tblWxopenAuthorizerAccountInfoMapper.selectByExample(accountInfoExample);
        if (accountInfoList != null && !accountInfoList.isEmpty()) {
            Integer weixinConfigId = accountInfoList.get(0).getId();
            TPropertyWeixinPublicConfigExample propertyWeixinPublicConfigExample = new TPropertyWeixinPublicConfigExample();
            propertyWeixinPublicConfigExample.or().andWeixinConfigIdEqualTo(String.valueOf(weixinConfigId))
                    .andIsValidEqualTo("1");
            List<TPropertyWeixinPublicConfig> propertyWeixinPublicConfigList = tPropertyWeixinPublicConfigMapper.selectByExample(propertyWeixinPublicConfigExample);
            if (propertyWeixinPublicConfigList != null && !propertyWeixinPublicConfigList.isEmpty())
                propertyId = propertyWeixinPublicConfigList.get(0).getPropertyId();
        }
        return propertyId;
    }

    @Override
    public ApiMessage checkWechat(Dto dto) {
        dto.put("nick_name", dto.getString("wechat_name"));
        dto.put("authed", "1");
        TblWxopenAuthorizerAccountInfoExample tblWxopenAuthorizerAccountInfoExample = new TblWxopenAuthorizerAccountInfoExample();
        tblWxopenAuthorizerAccountInfoExample.or().andNickNameEqualTo(dto.getString("wechat_name")).andAuthedEqualTo("1");
        int count = tblWxopenAuthorizerAccountInfoMapper.countByExample(tblWxopenAuthorizerAccountInfoExample);
        return ApiMessageUtil.success(count);
    }

    /**
     * 添加物业微信公众号映射
     *
     * @param dto property_id
     *            wechat_id
     */
    @Override
    @Transactional
    public ApiMessage addPropertyWechat(Dto dto) throws ApiException {
        int count;
        try {


            TPropertyWeixinPublicConfigExample tPropertyWeixinPublicConfigExample = new TPropertyWeixinPublicConfigExample();
            tPropertyWeixinPublicConfigExample.or()
                    .andWeixinConfigIdEqualTo(dto.getString("wechat_id"))
                    .andIsValidEqualTo("1");
            count = tPropertyWeixinPublicConfigMapper.countByExample(tPropertyWeixinPublicConfigExample);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), ReqEnums.REQ_PARAM_ERROR.getMsg() + "repeat wechat_ids");

            TPropertyWeixinPublicConfig wechat_config = new TPropertyWeixinPublicConfig();
            wechat_config.setId(PrimaryGenerater.getInstance().uuid());
            wechat_config.setPropertyId(dto.getString("property_id"));
            wechat_config.setWeixinConfigId(dto.getString("wechat_id"));
            wechat_config.setCreateTime(new Date());
            wechat_config.setIsValid("1");
            count = tPropertyWeixinPublicConfigMapper.insertSelective(wechat_config);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "wechat");
            }
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public ApiMessage listWechatsByName(Dto dto) {
        String wechatName = dto.getString("wechatName");
        String propertyId = dto.getString("propertyId");
        List<Integer> ids = new ArrayList<>();
        List<TblWxopenAuthorizerAccountInfo> list = new ArrayList<>();

        TPropertyWeixinPublicConfigExample propertyWeixinPublicConfigExample = new TPropertyWeixinPublicConfigExample();
        propertyWeixinPublicConfigExample.or().andPropertyIdNotEqualTo(propertyId)
                .andIsValidEqualTo("1");
        List<TPropertyWeixinPublicConfig> propertyWeixinPublicConfigList = tPropertyWeixinPublicConfigMapper.selectByExample(propertyWeixinPublicConfigExample);
        if (propertyWeixinPublicConfigList != null && !propertyWeixinPublicConfigList.isEmpty()) {
            for (TPropertyWeixinPublicConfig propertyWeixinPublicConfig : propertyWeixinPublicConfigList)
                ids.add(Integer.valueOf(propertyWeixinPublicConfig.getWeixinConfigId()));

            TblWxopenAuthorizerAccountInfoExample wxopenAuthorizerAccountInfoExample = new TblWxopenAuthorizerAccountInfoExample();
            TblWxopenAuthorizerAccountInfoExample.Criteria criteria = wxopenAuthorizerAccountInfoExample.createCriteria();
            if (!SystemUtils.isEmpty(wechatName))
                criteria.andNickNameLike("%" + wechatName + "%");
            criteria.andIdNotIn(ids);
            criteria.andAuthedEqualTo("1");
            wxopenAuthorizerAccountInfoExample.setOrderByClause(" nick_name");
            list = tblWxopenAuthorizerAccountInfoMapper.selectByExample(wxopenAuthorizerAccountInfoExample);
        }
        return ApiMessageUtil.success(list);
    }

    @Override
    public List<TblWxopenAuthorizerAccountInfo> listWechats(String property_id) {
        List<TblWxopenAuthorizerAccountInfo> wechat_list = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        TPropertyWeixinPublicConfigExample propertyWeixinPublicConfigExample = new TPropertyWeixinPublicConfigExample();
        propertyWeixinPublicConfigExample.or().andPropertyIdEqualTo(property_id)
                .andIsValidEqualTo("1");

        List<TPropertyWeixinPublicConfig> propertyWeixinPublicConfigList = tPropertyWeixinPublicConfigMapper.selectByExample(propertyWeixinPublicConfigExample);
        if (propertyWeixinPublicConfigList != null && !propertyWeixinPublicConfigList.isEmpty()) {
            for (TPropertyWeixinPublicConfig propertyWeixinPublicConfig : propertyWeixinPublicConfigList)
                ids.add(Integer.valueOf(propertyWeixinPublicConfig.getWeixinConfigId()));

            TblWxopenAuthorizerAccountInfoExample wxopenAuthorizerAccountInfoExample = new TblWxopenAuthorizerAccountInfoExample();
            wxopenAuthorizerAccountInfoExample.or().andIdIn(ids);
            wechat_list = tblWxopenAuthorizerAccountInfoMapper.selectByExample(wxopenAuthorizerAccountInfoExample);
        }
        return wechat_list;
    }
}
