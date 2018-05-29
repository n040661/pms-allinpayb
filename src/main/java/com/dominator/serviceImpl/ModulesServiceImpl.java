package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.service.ModulesService;
import com.dominator.service.RoleService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ModulesServiceImpl implements ModulesService {

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TModulesMapper tModulesMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TPropertyUserRoleMapper tPropertyUserRoleMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;

    @Autowired
    private RoleService roleService;

    @Override
    @Transactional
    public void setRoleModules(Dto dto) throws ApiException {
        String companyId = dto.getString("companyId");
        String gardenId = dto.getString("gardenId");
        String roleId = dto.getString("roleId");
        String modifyId = dto.getString("modifyId");
        String modulesIds = dto.getString("modulesIds");

        try {
            TRole role = new TRole();
            role.setId(roleId);
            role.setModulesIds(modulesIds);
            role.setModifyTime(new Date());
            role.setModifyId(modifyId);
            int count = tRoleMapper.updateByPrimaryKeySelective(role);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);

            if (!SystemUtils.isEmpty(companyId)) {
                TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                        .andRoleIdEqualTo(roleId)
                        .andIsValidEqualTo("1");
                TCompanyUserRole companyUserRole = new TCompanyUserRole();
                companyUserRole.setModulesIds(modulesIds);
                companyUserRole.setModifyId(modifyId);
                companyUserRole.setModifyTime(new Date());
                tCompanyUserRoleMapper.updateByExampleSelective(companyUserRole, companyUserRoleExample);
            } else {
                TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                        .andRoleIdEqualTo(roleId)
                        .andIsValidEqualTo("1");
                TGardenUserRole gardenUserRole = new TGardenUserRole();
                gardenUserRole.setModulesIds(modulesIds);
                gardenUserRole.setModifyId(modifyId);
                gardenUserRole.setModifyTime(new Date());
                tGardenUserRoleMapper.updateByExampleSelective(gardenUserRole, gardenUserRoleExample);
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public void listModulesByIds(Dto gardenDto, String modulesIds) {
        if (SystemUtils.isNotEmpty(modulesIds)) {
            String[] strIds = modulesIds.split(",");
            TModulesExample tModulesExample = new TModulesExample();
            tModulesExample.or().andIdIn(Arrays.asList(strIds)).andIsValidEqualTo("1");
            tModulesExample.setOrderByClause(" modules_type,sort");
            List<TModules> moduleList = tModulesMapper.selectByExample(tModulesExample);
            gardenDto.put("moduleList", moduleList);
        }
    }

    @Override
    public List<TModules> listCheckedModulesByRole(Dto dto) {
        String roleId = dto.getString("roleId");
        List<TModules> modulesList = new ArrayList<>();
        String modulesIds;

        TRole role = tRoleMapper.selectByPrimaryKey(roleId);
        if (role != null) {
            modulesIds = role.getModulesIds();
            if (!SystemUtils.isEmpty(modulesIds) && modulesIds.split(",").length > 0) {
                TModulesExample modulesExample = new TModulesExample();
                modulesExample.or().andIdIn(Arrays.asList(modulesIds.split(",")))
                        .andIsValidEqualTo("1");
                modulesList = tModulesMapper.selectByExample(modulesExample);
            }
        }
        return modulesList;
//
//        if ("1".equals(type)) {
//        } else {
//            dto1.put("role_id", role_id);
//            dto1.put("is_valid", "1");
////            T_company_role_modulesPO company_role_modules = t_company_role_modulesDao.selectOne(dto1);
//            TCompanyRoleModulesExample tCompanyRoleModules = new TCompanyRoleModulesExample();
//            tCompanyRoleModules.or().andRoleIdEqualTo(role_id).andIsValidEqualTo("1");
//            List<TCompanyRoleModules> tCompanyRoleModulesList = tCompanyRoleModulesMapper.selectByExample(tCompanyRoleModules);
//            if (tCompanyRoleModulesList != null || !tCompanyRoleModulesList.isEmpty()) {
//                TCompanyRoleModules company_role_modules = tCompanyRoleModulesList.get(0);
//                modules_ids = company_role_modules.getModulesId();
//            }
//        }
////        List<T_modulesPO> module_list = new ArrayList<>();
//        List<TModules> module_list = new ArrayList<>();
//        if (!SystemUtils.isEmpty(modules_ids)) {
//            dto1.clear();
//            dto1.put("ids", modules_ids.split(","));
//            dto1.put("modules_type", SystemUtils.isEmpty(modules_type) ? null : modules_type.split(","));
//            String[] strs = modules_ids.split(",");
//            List<String> types = new ArrayList<>();
//            List<String> idList = new ArrayList<String>();
//            for (String str : strs) {
//                idList.add(str);
//            }
//            if (!SystemUtils.isEmpty(modules_type)) {
//                String[] str_types = modules_type.split(",");
//                for (String str : str_types) {
//                    types.add(str);
//                }
////                module_list = tModulesDaoExt.listModulesByIds(dto1);
//                TModulesExample tModulesExample = new TModulesExample();
//                tModulesExample.or().andIdIn(idList).andModulesTypeIn(types).andIsValidEqualTo("1");
//                tModulesExample.setOrderByClause("modules_type,sort");
//                module_list = tModulesMapper.selectByExample(tModulesExample);
//            }
//
//        }
//        return ApiMessageUtil.success(module_list);
//        return null;
    }

//    @Override
//    @Transactional
//    public ApiMessage addCompanyModules(Dto dto) throws ApiException {
//        String company_id = dto.getString("company_id");
//        String modules_ids = dto.getString("modules_ids");
//
//        try {
//            Dto dto1 = new HashDto();
//            dto1.put("company_id", company_id);
//            dto1.put("role_name", "企业管理员");
//            Dto userRoleDto = roleService.getUserRoleId(dto1);
//            String role_id = userRoleDto.getString("role_id");
//
//            dto1.clear();
//            dto1.put("company_id", company_id);
//            dto1.put("role_id", role_id);
//            dto1.put("modules_ids", modules_ids);
//            roleService.setCompanyRoleModules(dto1);
//            return ApiMessageUtil.success();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }

    @Override
    @Transactional
    public ApiMessage addModules(Dto dto) throws ApiException {
        String type = dto.getString("type");
        String propertyId = dto.getString("propertyId");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String modulesIds = dto.getString("modulesIds");
        String modifyId = dto.getString("modifyId");
        String roleId;
        String userId;
        int count = 0;

        try {
            switch (type) {
                case "1":
                    if (SystemUtils.isEmpty(companyId)) {
                        Dto dto1 = new HashDto();
                        dto1.put("gardenId", gardenId);
                        dto1.put("roleName", "园区管理员");
                        Dto userRoleDto = roleService.getUserRoleId(dto1);
                        roleId = userRoleDto.getString("roleId");
                        userId = userRoleDto.getString("userId");
                        TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                        gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                                .andUserIdEqualTo(userId)
                                .andRoleIdEqualTo(roleId)
                                .andIsValidEqualTo("1");
                        TGardenUserRole gardenUserRole = new TGardenUserRole();
                        gardenUserRole.setModifyId(modulesIds);
                        gardenUserRole.setModifyTime(new Date());
                        gardenUserRole.setModifyId(modifyId);
                        count = tGardenUserRoleMapper.updateByExampleSelective(gardenUserRole, gardenUserRoleExample);

                    } else {
                        Dto dto1 = new HashDto();
                        dto1.put("companyId", companyId);
                        dto1.put("roleName", "企业管理员");
                        Dto userRoleDto = roleService.getUserRoleId(dto1);
                        roleId = userRoleDto.getString("roleId");
                        userId = userRoleDto.getString("userd");
                        TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                        companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                                .andUserIdEqualTo(userId)
                                .andRoleIdEqualTo(roleId)
                                .andIsValidEqualTo("1");
                        TCompanyUserRole companyUserRole = new TCompanyUserRole();
                        companyUserRole.setModulesIds(modulesIds);
                        companyUserRole.setModifyTime(new Date());
                        companyUserRole.setModifyId(modifyId);
                        count = tCompanyUserRoleMapper.updateByExampleSelective(companyUserRole, companyUserRoleExample);
                    }
                    break;
                case "3":
                    Dto dto1 = new HashDto();
                    dto1.put("propertyId", propertyId);
                    dto1.put("roleName", "物业管理员");
                    Dto userRoleDto = roleService.getUserRoleId(dto1);
                    roleId = userRoleDto.getString("roleId");
                    userId = userRoleDto.getString("userId");
                    TPropertyUserRoleExample propertyUserRoleExample = new TPropertyUserRoleExample();
                    propertyUserRoleExample.or().andPropertyIdEqualTo(propertyId)
                            .andUserIdEqualTo(userId)
                            .andRoleIdEqualTo(roleId)
                            .andIsValidEqualTo("1");
                    TPropertyUserRole propertyUserRole = new TPropertyUserRole();
                    propertyUserRole.setModulesIds(modulesIds);
                    propertyUserRole.setModifyTime(new Date());
                    propertyUserRole.setModifyId(modifyId);
                    count = tPropertyUserRoleMapper.updateByExampleSelective(propertyUserRole, propertyUserRoleExample);
                    break;
            }
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public ApiMessage listModules(Dto dto) {
        String type = dto.getString("type");
        String propertyId = dto.getString("propertyId");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String userId = dto.getString("userId");

        List<TModules> modules = new ArrayList<>();
        List<TModules> mainModules = new ArrayList<>();
        List<TModules> subModules = new ArrayList<>();
        Set<String> moduleIdSet = new HashSet<>();
        String[] modulesIds;
        TModulesExample tModulesExample = new TModulesExample();
        Dto dto1 = new HashDto();
        switch (type) {
            case "1":
                dto1.put("propertyId", propertyId);
                dto1.put("roleName", "物业管理员");
                Dto userRoleDto = roleService.getUserRoleId(dto1);
                if (userRoleDto.getString("userId").equals(userId)) {
                    TPropertyUserRoleExample propertyUserRoleExample = new TPropertyUserRoleExample();
                    propertyUserRoleExample.or().andPropertyIdEqualTo(propertyId)
                            .andUserIdEqualTo(userId)
                            .andIsValidEqualTo("1");
                    List<TPropertyUserRole> propertyUserRoleList = tPropertyUserRoleMapper.selectByExample(propertyUserRoleExample);
                    if (propertyUserRoleList != null && !propertyUserRoleList.isEmpty()) {
                        modulesIds = propertyUserRoleList.get(0).getModulesIds().split(",");
                        tModulesExample.clear();
                        tModulesExample.or().andModulesTypeEqualTo(type)
                                .andIdIn(Arrays.asList(modulesIds))
                                .andIsValidEqualTo("1");
                        tModulesExample.setOrderByClause(" modules_type,sort");
                        modules = tModulesMapper.selectByExample(tModulesExample);
                    }
                } else {
                    TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                    gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                            .andUserIdEqualTo(userId)
                            .andIsValidEqualTo("1");
                    List<TGardenUserRole> gardenUserRoleList = tGardenUserRoleMapper.selectByExample(gardenUserRoleExample);
                    if (gardenUserRoleList != null && !gardenUserRoleList.isEmpty()) {
                        for (TGardenUserRole gardenUserRole : gardenUserRoleList) {
                            modulesIds = gardenUserRole.getModulesIds().split(",");
                            moduleIdSet.addAll(Arrays.asList(modulesIds));
                        }
                        tModulesExample.clear();
                        tModulesExample.or().andModulesTypeEqualTo(type)
                                .andIdIn(new ArrayList<>(moduleIdSet))
                                .andIsValidEqualTo("1");
                        tModulesExample.setOrderByClause(" modules_type,sort");
                        modules = tModulesMapper.selectByExample(tModulesExample);
                    }
                }
                break;
            case "2":
                TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                        .andUserIdEqualTo(userId)
                        .andIsValidEqualTo("1");
                List<TCompanyUserRole> companyUserRoleList = tCompanyUserRoleMapper.selectByExample(companyUserRoleExample);
                if (companyUserRoleList != null && !companyUserRoleList.isEmpty()) {
                    for (TCompanyUserRole companyUserRole : companyUserRoleList) {
                        modulesIds = companyUserRole.getModulesIds().split(",");
                        moduleIdSet.addAll(Arrays.asList(modulesIds));
                    }
                    tModulesExample.clear();
                    tModulesExample.or().andModulesTypeEqualTo(type)
                            .andIdIn(new ArrayList<>(moduleIdSet))
                            .andIsValidEqualTo("1");
                    tModulesExample.setOrderByClause(" modules_type,sort");
                    modules = tModulesMapper.selectByExample(tModulesExample);
                }
                break;
            case "3":
                tModulesExample.clear();
                tModulesExample.or().andModulesTypeEqualTo(type).andIsValidEqualTo("1");
                tModulesExample.setOrderByClause(" modules_type,sort");
                modules = tModulesMapper.selectByExample(tModulesExample);
                break;
        }
        for (
                TModules module : modules)

        {
            if (module.getParentId().equals("root")) {
                mainModules.add(module);
            } else {
                subModules.add(module);
            }
        }

        Dto resDto = new HashDto();
        resDto.put("mainModules", mainModules);
        resDto.put("subModules", subModules);
        return ApiMessageUtil.success(resDto);
    }

    @Override
    public ApiMessage listModulesByRole(Dto dto) {
        String modulesType = dto.getString("modulesType");
        List<TModules> modules;
        String[] types = modulesType.split(",");
        TModulesExample tModulesExample = new TModulesExample();
        tModulesExample.or().andModulesTypeIn(Arrays.asList(types)).andIsValidEqualTo("1");
        tModulesExample.setOrderByClause("modules_type,sort");
        modules = tModulesMapper.selectByExample(tModulesExample);
        return ApiMessageUtil.success(modules);
    }

//    private void listSubModules(List<String> parent_ids, List<T_modulesPO> modules) {
//        if (parent_ids == null || parent_ids.size() == 0)
//            return;
//        List<String> temp = new ArrayList<>();
//        List<T_modulesPO> temp1 = new ArrayList<>();
//        Dto dto = new HashDto();
//        dto.put("parent_ids", parent_ids);
//        temp1 = tModulesDaoExt.listSubModules(dto);
//        modules.addAll(temp1);
//        if (modules != null && modules.size() > 0) {
//            for (T_modulesPO module : temp1) {
//                if (module.getIs_leaf().equals("0"))
//                    temp.add(module.getId());
//            }
//            listSubModules(temp, modules);
//        }
//    }

//    @Override
//    @Transactional
//    public ApiMessage addRoleModules(Dto dto) throws ApiException {
//        String garden_id = dto.getString("garden_id");
//        String modules_ids = dto.getString("modules_ids");
//        String role_name = dto.getString("role_name");
//        String role_id = dto.getString("role_id");
//        try {
//            if (SystemUtils.isEmpty(role_id)) {
//                role_id = PrimaryGenerater.getInstance().uuid();
//                Dto dto1 = new HashDto();
//                dto1.put("garden_id", garden_id);
//                dto1.put("role_id", role_id);
//                dto1.put("role_name", role_name);
//                roleService.addRole(dto1);
//            }
//            Dto dto2 = new HashDto();
//            dto2.put("garden_id", garden_id);
//            dto2.put("role_id", role_id);
//            dto2.put("modules_ids", modules_ids);
//            roleService.setGardenRoleModules(dto2);
//
//            return ApiMessageUtil.success();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw new ApiException(e.getCode(), e.getMessage());
//        }
//    }
}
