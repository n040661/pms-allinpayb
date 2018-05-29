package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.app.service.UserService;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("app.userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;

    @Autowired
    private TSocialActivityMapper tSocialActivityMapper;

    @Autowired
    private TSocialActivityRelationshipMapper tSocialActivityRelationshipMapper;

    @Autowired
    private TRepairMapper tRepairMapper;

    @Autowired
    private TAppointPublicRecordMapper tAppointPublicRecordMapper;

    @Autowired
    private TPropertyUserRoleMapper tPropertyUserRoleMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TPropertyMapper tPropertyMapper;

    @Override
    public void putUser(String userId, HttpServletRequest request) throws ApiException {
        try {
            Dto dto = ApiUtils.getParams(request);
            String nickName = dto.get("nickName") == null ? null : dto.getString("nickName");
            String headImg = dto.get("headImg") == null ? null : dto.getString("headImg");
            int count;

            TUser user = new TUser();
            user.setId(userId);
            user.setHeadImg(headImg);
            user.setNickName(nickName);
            user.setModifyTime(new Date());
            user.setModifyId(userId);
            count = tUserMapper.updateByPrimaryKeySelective(user);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public Dto listCompanyAndGarden(Dto dto) {
        String gardenId = dto.getString("gardenId");
        String userId = dto.getString("userId");
        int count;
        Dto resDto = new HashDto();

        TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
        gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                .andUserIdEqualTo(userId)
                .andIsValidEqualTo("1");
        count = tGardenUserRoleMapper.countByExample(gardenUserRoleExample);
        if (count > 1)
            resDto.put("garden", tGardenMapper.selectByPrimaryKey(gardenId));


        List<String> companyIdList = new ArrayList<>();
        List<String> userCompanyIdList = new ArrayList<>();
        Set<String> userCompanyIdSet = new HashSet<>();

        TCompanyExample companyExample = new TCompanyExample();
        companyExample.or().andGardenIdEqualTo(gardenId)
                .andIsValidEqualTo("1");
        List<TCompany> companyList = tCompanyMapper.selectByExample(companyExample);
        for (TCompany company : companyList)
            companyIdList.add(company.getId());

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

        if (!userCompanyIdSet.isEmpty()) {
            companyExample.clear();
            companyExample.or().andIdIn(new ArrayList<>(userCompanyIdSet));
            companyExample.setOrderByClause(" create_time");
            companyList = tCompanyMapper.selectByExample(companyExample);
            resDto.put("companyList", companyList);
        }

        return resDto;
    }

    @Override
    public Dto msgamount(Dto dto) {
        String userId = dto.getString("userId");
        String pos = dto.getString("pos");
        Dto resDto = new HashDto();
        //社区活动数
        TSocialActivityExample tSocialActivityExample = new TSocialActivityExample();
        tSocialActivityExample.or().andPublisherIdEqualTo(userId).andIsValidEqualTo("1");
        int count1 = tSocialActivityMapper.countByExample(tSocialActivityExample);
        TSocialActivityRelationshipExample tSocialActivityRelationshipExample = new TSocialActivityRelationshipExample();
        tSocialActivityRelationshipExample.or().andSubscriberIdEqualTo(userId).andIsValidEqualTo("1");
        int count2 = tSocialActivityRelationshipMapper.countByExample(tSocialActivityRelationshipExample);
        int activNum = count1 + count2;

        //报事报修数
        TRepairExample tRepairExample = new TRepairExample();
        tRepairExample.or().andRepairUserIdEqualTo(userId).andIsValidEqualTo("1");
        int repairNum = tRepairMapper.countByExample(tRepairExample);

        //预约数
        TAppointPublicRecordExample tAppointPublicRecordExample = new TAppointPublicRecordExample();
        tAppointPublicRecordExample.or().andUserIdEqualTo(userId).andIsValidEqualTo("1");
        int appointNum = tAppointPublicRecordMapper.countByExample(tAppointPublicRecordExample);

        switch (pos) {
            case "pos1":
                resDto.put("pos1", activNum);
                break;
            case "pos2":
                resDto.put("pos2", repairNum);
                break;
            case "pos3":
                resDto.put("pos3", appointNum);
                break;
            default:
                resDto.put("pos1", activNum);
                resDto.put("pos2", repairNum);
                resDto.put("pos3", appointNum);
                break;
        }
        return resDto;
    }

    @Override
    public Dto getCompanyAndRole(Dto dto) {
        String userId = dto.getString("userId");
        String companyId = dto.getString("companyId");
        String propertyId = dto.getString("propertyId");
        String gardenId = dto.getString("gardenId");

        String name = "";
        List<TRole> roleList = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        if (!SystemUtils.isEmpty(companyId)) {
            TCompany company = tCompanyMapper.selectByPrimaryKey(companyId);
            if (company != null)
                name = company.getCompanyName();
            TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
            companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                    .andUserIdEqualTo(userId)
                    .andIsValidEqualTo("1");
            List<TCompanyUserRole> companyUserRoleList = tCompanyUserRoleMapper.selectByExample(companyUserRoleExample);
            if (companyUserRoleList != null && !companyUserRoleList.isEmpty()) {
                for (TCompanyUserRole companyUserRole : companyUserRoleList)
                    ids.add(companyUserRole.getRoleId());
            }
        } else {
            TProperty property = tPropertyMapper.selectByPrimaryKey(propertyId);
            if (property != null)
                name = property.getPropertyName();
            if (!SystemUtils.isEmpty(gardenId)) {
                TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                        .andUserIdEqualTo(userId)
                        .andIsValidEqualTo("1");
                List<TGardenUserRole> gardenUserRoleList = tGardenUserRoleMapper.selectByExample(gardenUserRoleExample);
                if (gardenUserRoleList != null && !gardenUserRoleList.isEmpty()) {
                    for (TGardenUserRole gardenUserRole : gardenUserRoleList)
                        ids.add(gardenUserRole.getRoleId());
                }
            } else {
                TPropertyUserRoleExample propertyUserRoleExample = new TPropertyUserRoleExample();
                propertyUserRoleExample.or().andPropertyIdEqualTo(propertyId)
                        .andUserIdEqualTo(userId)
                        .andIsValidEqualTo("1");
                List<TPropertyUserRole> propertyUserRoleList = tPropertyUserRoleMapper.selectByExample(propertyUserRoleExample);
                if (propertyUserRoleList != null && !propertyUserRoleList.isEmpty()) {
                    for (TPropertyUserRole propertyUserRole : propertyUserRoleList)
                        ids.add(propertyUserRole.getRoleId());
                }
            }
        }
        if (!ids.isEmpty()) {
            TRoleExample roleExample = new TRoleExample();
            roleExample.or().andIdIn(ids)
                    .andIsValidEqualTo("1");
            roleExample.setOrderByClause(" sort");
            roleList = tRoleMapper.selectByExample(roleExample);
        }
        Dto resDto = new HashDto();
        resDto.put("name", name);
        resDto.put("roleList", roleList);
        return resDto;
    }

    @Override
    public Dto showBill(Dto dto) {
        String userId = dto.getString("userId");
        String companyId = dto.getString("companyId");
        String payModuleId = SysConfig.PayModuleId;
        int count = 0;
        if (!SystemUtils.isEmpty(companyId)) {
            TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
            companyUserRoleExample.or().andUserIdEqualTo(userId)
                    .andCompanyIdEqualTo(companyId)
                    .andIsValidEqualTo("1");
            List<TCompanyUserRole> list = tCompanyUserRoleMapper.selectByExample(companyUserRoleExample);
            if (list != null && !list.isEmpty()) {
                for (TCompanyUserRole companyUserRole : list) {
                    String moduleIds = companyUserRole.getModulesIds();
                    if (!SystemUtils.isEmpty(moduleIds)) {
                        List<String> moduleIdList = Arrays.asList(moduleIds.split(","));
                        if (moduleIdList.contains(payModuleId))
                            count = 1;
                    }
                }
            }
        }
        Dto resDto = new HashDto();
        resDto.put("count", count);
        return resDto;
    }
}
