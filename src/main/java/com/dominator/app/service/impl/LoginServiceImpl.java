package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.app.service.LoginService;
import com.dominator.service.CompanyService;
import com.dominator.service.GardenService;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import com.dominator.utils.system.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dominator.utils.sms.SmsUtils.checkSms;

@Service("app.loginServiceImpl")
public class LoginServiceImpl implements LoginService {

    private static RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TPropertyUserRoleMapper tPropertyUserRoleMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private CompanyService companyService;

    @Override
    public Dto login(Dto dto) throws ApiException {
        String propertyId = dto.getString("propertyId");
        String code = dto.getString("code");
        String userName = dto.getString("userName");
        String password = dto.getString("password");
        String userId = dto.getString("userId");
        String token = dto.getString("token");
        TUser user;
        //验证码登录
        if (StringUtils.isNotEmpty(code)) {
            //调用检查接口
            checkSms(dto);
            //获取用户信息
            TUserExample tUserExample = new TUserExample();
            tUserExample.or().andUserNameEqualTo(userName).andIsValidEqualTo("1");
            List<TUser> list = tUserMapper.selectByExample(tUserExample);
            if (list.size() != 1) {
                throw new ApiException(Constants.CODE_LOGIN_FAIL_ERROE, Constants.MSG_LOGIN_FAIL_ERROE);
            }
            user = list.get(0);
            if (SystemUtils.isEmpty(user)) {
                throw new ApiException(ReqEnums.REQ_NO_USER.getCode(), ReqEnums.REQ_NO_USER.getMsg());
            }
        }
        //密码登录
        else if (StringUtils.isNotEmpty(password)) {
            TUserExample tUserExample = new TUserExample();
            tUserExample.or().andUserNameEqualTo(userName)
                    .andPasswordEqualTo(Des3Utils.encode(password))
                    .andIsValidEqualTo("1");
            List<TUser> list = tUserMapper.selectByExample(tUserExample);
            if (list == null || list.isEmpty())
                //用户名或密码错误
                throw new ApiException(ReqEnums.REQ_PHONE_OR_PASSWORD_ERROR.getCode(), ReqEnums.REQ_PHONE_OR_PASSWORD_ERROR.getMsg());
            user = list.get(0);
        } else {
            user = tUserMapper.selectByPrimaryKey(userId);
        }

        Dto tokenDto = new HashDto();
        Dto resDto = getLoginList(propertyId, user, tokenDto);
        if (SystemUtils.isEmpty(token)) {
            try {
                token = JwtToken.createToken(tokenDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resDto.put("token", token);
            ru.setex("jwt_" + userName, token, SysConfig.RedisTokenExpiresTime);
        }
        return resDto;
    }

    @Override
    public Dto getLoginList(String propertyId, TUser user, Dto tokenDto) {
        if (user == null)
            throw new ApiException(ReqEnums.REQ_NO_USER);
        String userName = user.getUserName();
        String userId = user.getId();
        List<String> gardenIdList = new ArrayList<>();
        List<String> userGardenIdList = new ArrayList<>();
        List<String> companyIdList = new ArrayList<>();
        List<String> userCompanyIdList = new ArrayList<>();
        Set<String> userGardenIdSet = new HashSet<>();
        TCompanyExample companyExample = new TCompanyExample();
        Set<String> userCompanyIdSet = new HashSet<>();

        TPropertyUserRoleExample propertyUserRoleExample = new TPropertyUserRoleExample();
        propertyUserRoleExample.or().andUserIdEqualTo(userId)
                .andPropertyIdEqualTo(propertyId)
                .andIsValidEqualTo("1");
        List<TPropertyUserRole> propertyUserRoleList = tPropertyUserRoleMapper.selectByExample(propertyUserRoleExample);

        //所有园区
        TGardenExample gardenExample = new TGardenExample();
        gardenExample.or().andPropertyIdEqualTo(propertyId)
                .andIsValidEqualTo("1");
        gardenExample.setOrderByClause(" create_time");
        List<TGarden> gardenList = tGardenMapper.selectByExample(gardenExample);
        for (TGarden garden : gardenList)
            gardenIdList.add(garden.getId());

        if (gardenIdList.size() > 0) {
            TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
            gardenUserRoleExample.or().andUserIdEqualTo(userId)
                    .andGardenIdIn(gardenIdList)
                    .andIsValidEqualTo("1");
            List<TGardenUserRole> gardenUserRoleList = tGardenUserRoleMapper.selectByExample(gardenUserRoleExample);
            for (TGardenUserRole gardenUserRole : gardenUserRoleList)
                userGardenIdList.add(gardenUserRole.getGardenId());
            userGardenIdSet = new HashSet<>(userGardenIdList);

            companyExample.or().andGardenIdIn(gardenIdList)
                    .andIsValidEqualTo("1");
            List<TCompany> companyList = tCompanyMapper.selectByExample(companyExample);
            for (TCompany company : companyList)
                companyIdList.add(company.getId());
        }

        if (companyIdList.size() > 0) {
            TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
            companyUserRoleExample.or().andUserIdEqualTo(userId)
                    .andCompanyIdIn(companyIdList)
                    .andIsValidEqualTo("1");
            List<TCompanyUserRole> companyUserRoleList = tCompanyUserRoleMapper.selectByExample(companyUserRoleExample);
            for (TCompanyUserRole companyUserRole : companyUserRoleList)
                userCompanyIdList.add(companyUserRole.getCompanyId());
            userCompanyIdSet = new HashSet<>(userCompanyIdList);
        }

        int count1 = propertyUserRoleList.size();
        int count2 = userGardenIdSet.size();
        int count3 = userCompanyIdSet.size();
        int type; //登录类型 1选所在园区和所在企业  2选所有园区和所在企业  3直接进园区 4直接进企业
        Dto resDto = Dtos.newDto();

        if (gardenList.size() == 0)
            throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR);

        if (count1 == 0) {//不在物业中
            if (count2 == 0 && count3 == 0) {//不在园区
                type = 2;
            } else {//在多个园区
                type = 1;
            }
        } else {//在物业中 需要选
            type = 2;
        }

        //所在园区
        List<TGarden> gardenList1 = new ArrayList<>();
        if (!userGardenIdSet.isEmpty()) {
            gardenExample.clear();
            gardenExample.or().andIdIn(new ArrayList<>(userGardenIdSet));
            gardenExample.setOrderByClause(" create_time");
            gardenList1 = tGardenMapper.selectByExample(gardenExample);
        }
        //所在企业
        List<TCompany> companyList1 = new ArrayList<>();
        if (!userCompanyIdSet.isEmpty()) {
            companyExample.clear();
            companyExample.or().andIdIn(new ArrayList<>(userCompanyIdSet));
            companyExample.setOrderByClause(" create_time");
            companyList1 = tCompanyMapper.selectByExample(companyExample);
        }
        switch (type) {
            case 1:
                resDto.put("gardenList", gardenList1);
                resDto.put("companyList", companyList1);
                break;
            case 2:
                resDto.put("gardenList", gardenList);
                resDto.put("companyList", companyList1);
                break;
        }

        tokenDto.put("propertyId", propertyId);
        tokenDto.put("userId", userId);
        tokenDto.put("userName", userName);
        tokenDto.put("loginType", "4");
        //返回token和用户信息
        resDto.put("user", user);
        return resDto;
    }

    @Override
    public Dto select(Dto dto) {
        String userName = dto.getString("userName");
        String userId = dto.getString("userId");
        String companyId = dto.getString("companyId");
        String gardenId = dto.getString("gardenId");
        String token = dto.getString("token");
        int count;
        String isTourist = "1";
        Dto resDto = new HashDto();
        if (!SystemUtils.isEmpty(companyId)) {
            TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
            companyUserRoleExample.or().andUserIdEqualTo(userId)
                    .andCompanyIdEqualTo(companyId)
                    .andIsValidEqualTo("1");
            count = tCompanyUserRoleMapper.countByExample(companyUserRoleExample);
            if (count > 0)
                isTourist = "0";
        } else {
            TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
            gardenUserRoleExample.or().andUserIdEqualTo(userId)
                    .andGardenIdEqualTo(gardenId)
                    .andIsValidEqualTo("1");
            count = tGardenUserRoleMapper.countByExample(gardenUserRoleExample);
            if (count > 0)
                isTourist = "0";
        }

        Dto gardenDto = gardenService.getGarden(dto);
        resDto.put("garden", gardenDto);
        if (!SystemUtils.isEmpty(companyId)) {
            Dto companyDto = companyService.getCompany(dto);
            resDto.put("company", companyDto);
        }
        resDto.put("isTourist", isTourist);

        Dto dto1 = new HashDto();
        dto1.put("gardenId", gardenId);
        dto1.put("companyId", companyId);
        dto1.put("isTourist", isTourist);

        try {
            token = JwtToken.updateToken(token, dto1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        resDto.put("token", token);
        ru.setex("jwt_" + userName, token, SysConfig.RedisTokenExpiresTime);
        return resDto;
    }
}
