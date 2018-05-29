package com.dominator.serviceImpl;


import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.service.SystemManagerService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.PrimaryGenerater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class SystemManagerServiceImpl implements SystemManagerService {


    @Autowired
    private TPropertyMapper tPropertyMapper;

    @Autowired
    private TPropertyUserRoleMapper tPropertyUserRoleMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TblWxopenAuthorizerAccountInfoMapper tblWxopenAuthorizerAccountInfoMapper;

    @Autowired
    private TPropertyWeixinPublicConfigMapper tPropertyWeixinPublicConfigMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;


    @Override
    public ApiMessage addWechat(Dto dto) throws ApiException {
        List<String> name_list = Arrays.asList(dto.getString("names").split(","));
        if (name_list != null && name_list.size() > 0) {
            for (String name : name_list) {
                int count = 0;

                TblWxopenAuthorizerAccountInfo wechat = new TblWxopenAuthorizerAccountInfo();
                wechat.setAppId(PrimaryGenerater.getInstance().uuid());
                wechat.setNickName(name);
                wechat.setAuthed("1");
                count = tblWxopenAuthorizerAccountInfoMapper.insert(wechat);

//                Tbl_wxopen_authorizer_account_infoPO wechat = new Tbl_wxopen_authorizer_account_infoPO();
//                wechat.setApp_id(PrimaryGenerater.getInstance().uuid());
//                wechat.setNick_name(name);
//                wechat.setAuthed("1");
//                count = tbl_wxopen_authorizer_account_infoDao.insert(wechat);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        }
        return ApiMessageUtil.success();
    }

    @Override
    @Transactional
    public ApiMessage delProperty(Dto dto) throws ApiException {
//        String property_id = dto.getString("property_id");
//        //String role_id = propertiesLoader.getProperty("super_admin_id");
//        String role_id = SysConfig.SuperAdminId;
////        String role_id = sysConfig.getSuperAdminId();
//        try {
//            //删t_property_weixin_public_config
//            tPropertyWeixinPublicConfigMapper.deleteByPrimaryKey(property_id);
////            tCleanDBDaoExt.deletePropertyWechatConfig(property_id);
//            //删t_property
//
//            tPropertyMapper.deleteByPrimaryKey(property_id);
//            //删t_property_user
//            tPropertyUserMapper.deleteByPrimaryKey(property_id);
////            tCleanDBDaoExt.deletePropertyUser(property_id);
//            //删t_property_user_role
//            tPropertyUserRoleMapper.deleteByPrimaryKey(property_id);
////            tCleanDBDaoExt.deletePropertyUserRole(property_id);
//            //删t_property_role_modules
//            tPropertyRoleModulesMapper.deleteByPrimaryKey(property_id);
////            tCleanDBDaoExt.deletePropertyRoleModules(property_id);
//            //查garden_list
//            Dto dto1 = new HashDto();
//            dto1.put("property_id", property_id);
//
//            TGardenExample gardenExample = new TGardenExample();
//            gardenExample.or().andPropertyIdEqualTo(property_id)
//                    .andIsValidEqualTo("1");
//            List<TGarden> garden_list = tGardenMapper.selectByExample(gardenExample);
//
//
////            List<T_gardenPO> garden_list = t_gardenDao.list(dto1);
//            List<String> garden_id_list = new ArrayList<>();
//            if (garden_list != null && garden_list.size() > 0) {
//                for (TGarden garden : garden_list) {
//                    garden_id_list.add(garden.getId());
//                }
//                //删t_garden
//                dto1.clear();
//                dto1.put("ids", garden_id_list);
//                gardenExample.clear();
//                gardenExample.or().andIdIn(garden_id_list)
//                        .andIsValidEqualTo("1");
//
//                tGardenMapper.deleteByExample(gardenExample);
//                //tCleanDBDaoExt.deleteGarden(dto1);
//                //删t_garden_user
//                TGardenUserExample tGardenUserExample = new TGardenUserExample();
//                tGardenUserExample.or().andIdIn(garden_id_list).andIsValidEqualTo("1");
//                tGardenUserMapper.deleteByExample(tGardenUserExample);
//                //tCleanDBDaoExt.deleteGardenUser(dto1);
//                //删t_garden_user_role
//                TGardenUserRoleExample tGardenUserRoleExample = new TGardenUserRoleExample();
//                tGardenUserRoleExample.or().andIdIn(garden_id_list).andIsValidEqualTo("1");
//                tGardenUserRoleMapper.deleteByExample(tGardenUserRoleExample);
//                //tCleanDBDaoExt.deleteGardenUserRole(dto1);
//                //删t_garden_role_modules
//                TGardenRoleModulesExample tGardenRoleModulesExample = new TGardenRoleModulesExample();
//                tGardenRoleModulesExample.or().andIdIn(garden_id_list).andIsValidEqualTo("1");
//                tGardenRoleModulesMapper.deleteByExample(tGardenRoleModulesExample);
//                //tCleanDBDaoExt.deleteGardenRoleModules(dto1);
//
//                //查company_list
//                TCompanyExample tCompanyExample = new TCompanyExample();
//                tCompanyExample.or().andIdIn(garden_id_list).andIsValidEqualTo("1");
//                List<TCompany> companyList = tCompanyMapper.selectByExample(tCompanyExample);
//                List<String> company_id_list = new ArrayList<>();
//                if (companyList != null && !companyList.isEmpty()) {
//                    for (TCompany company : companyList) {
//                        company_id_list.add(company.getId());
//                    }
//                }
//                if (company_id_list != null && company_id_list.size() > 0) {
//                    //删t_company
//                    dto1.clear();
//                    dto1.put("ids", company_id_list);
//                    tCompanyExample.clear();
//                    tCompanyExample.or().andIdIn(company_id_list).andIsValidEqualTo("1");
//                    tCompanyMapper.deleteByExample(tCompanyExample);
//                    //tCleanDBDaoExt.deleteCompany(dto1);
//
//                    //删t_company_user
//                    TCompanyUserExample tCompanyUserExample = new TCompanyUserExample();
//                    tCompanyUserExample.or().andIdIn(company_id_list).andIsValidEqualTo("1");
//                    tCompanyUserMapper.deleteByExample(tCompanyUserExample);
//                    //tCleanDBDaoExt.deleteCompanyUser(dto1);
//
//                    //删t_company_user_role
//                    TCompanyUserRoleExample tCompanyUserRoleExample = new TCompanyUserRoleExample();
//                    tCompanyUserRoleExample.or().andIdIn(company_id_list).andIsValidEqualTo("1");
//                    tCompanyUserRoleMapper.deleteByExample(tCompanyUserRoleExample);
//                    //tCleanDBDaoExt.deleteCompanyUserRole(dto1);
//
//                    //删t_company_role_modules
//                    TCompanyRoleModulesExample tCompanyRoleModulesExample = new TCompanyRoleModulesExample();
//                    tCompanyRoleModulesExample.or().andIdIn(company_id_list).andIsValidEqualTo("1");
//                    tCompanyRoleModulesMapper.deleteByExample(tCompanyRoleModulesExample);
//                    //tCleanDBDaoExt.deleteCompanyRoleModules(dto1);
//                }
//            }
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
        return null;
    }
}
