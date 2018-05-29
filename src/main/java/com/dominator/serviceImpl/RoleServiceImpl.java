package com.dominator.serviceImpl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TCompanyUserRoleExt;
import com.dominator.mapper.ext.TGardenUserRoleExt;
import com.dominator.mapper.ext.TRoleExt;
import com.dominator.service.RoleService;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.PrimaryGenerater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private TRoleExt tRoleExt;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TPropertyUserRoleMapper tPropertyUserRoleMapper;

    @Autowired
    private TCompanyUserRoleExt tCompanyUserRoleExt;

    @Autowired
    private TGardenUserRoleExt tGardenUserRoleExt;

    @Autowired
    private RoleService roleService;

    @Override
    public int checkRoleName(Dto dto) {
        String companyId = dto.getString("companyId");
        String gardenId = dto.getString("gardenId");
        String roleName = dto.getString("roleName");
        String roleId = dto.getString("roleId");
        List<String> unitIdList = new ArrayList<>();

        if (roleName.equals("物业管理员") || roleName.equals("园区管理员") || roleName.equals("企业管理员"))
            return 1;

        if (!SystemUtils.isEmpty(companyId)) {
            unitIdList.add("company");
            unitIdList.add(companyId);
        } else {
            unitIdList.add("garden");
            unitIdList.add(gardenId);
        }

        TRoleExample roleExample = new TRoleExample();
        roleExample.or().andUnitIdIn(unitIdList)
                .andRoleNameEqualTo(roleName)
                .andIdNotEqualTo(roleId)
                .andIsValidEqualTo("1");
        return tRoleMapper.countByExample(roleExample);
    }

    @Override
    public List<TRole> listRole(String unitId, String roleName) {
        TRoleExample roleExample = new TRoleExample();
        TRoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andUnitIdEqualTo(unitId);
        criteria.andIsValidEqualTo("1");
        if (!SystemUtils.isEmpty(roleName))
            criteria.andRoleNameLike("%" + roleName + "%");
        roleExample.setOrderByClause(" sort");
        return tRoleMapper.selectByExample(roleExample);
    }

    @Override
    @Transactional
    public void postUsers(Dto dto) throws ApiException {
        String userIds = dto.getString("userIds");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String modifyId = dto.getString("modifyId");
        String type = dto.getString("type");
        String roleId = dto.getString("roleId");
        String modulesIds = "";

        try {
            TRole role = tRoleMapper.selectByPrimaryKey(roleId);
            if (role != null)
                modulesIds = role.getModulesIds();
            switch (type) {
                //园区
                case "1":
                    Dto dto1 = new HashDto();
                    dto1.put("gardenId", gardenId);
                    dto1.put("roleName", "普通员工");
                    Dto userRoleDto = roleService.getUserRoleId(dto1);
                    String roleId1 = userRoleDto.getString("roleId");

                    List<TGardenUserRole> insertList = new ArrayList<>();
                    TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();

                    for (String userId : userIds.split(",")) {
                        gardenUserRoleExample.clear();
                        gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                                .andUserIdEqualTo(userId)
                                .andRoleIdEqualTo(roleId1)
                                .andIsValidEqualTo("1");
                        List<TGardenUserRole> gardenUserRoleList = tGardenUserRoleMapper.selectByExample(gardenUserRoleExample);
                        if (gardenUserRoleList != null && !gardenUserRoleList.isEmpty()) {
                            TGardenUserRole gardenUserRole = gardenUserRoleList.get(0);
                            gardenUserRole.setId(PrimaryGenerater.getInstance().uuid());
                            gardenUserRole.setRoleId(roleId);
                            gardenUserRole.setModulesIds(modulesIds);
                            gardenUserRole.setModifyTime(new Date());
                            gardenUserRole.setModifyId(modifyId);

                            insertList.add(gardenUserRole);
                        }
                    }

                    gardenUserRoleExample.clear();
                    gardenUserRoleExample.or().andRoleIdEqualTo(roleId)
                            .andGardenIdEqualTo(gardenId);
                    tGardenUserRoleMapper.deleteByExample(gardenUserRoleExample);

                    dto1.clear();
                    dto1.put("list", insertList);
                    if (insertList.size() > 0)
                        tGardenUserRoleExt.insertBatch(dto1);


                    //                List<TGardenUserRole> insertList = new ArrayList<>();
                    //                TGardenUserRoleExample tGardenUserRoleExample = new TGardenUserRoleExample();
                    //                TGardenUserRoleExample.Criteria criteria = tGardenUserRoleExample.createCriteria();
                    //                criteria.andGardenIdEqualTo(garden_id).andUserIdIn(idList).andIsValidEqualTo("1");
                    //                List<TGardenUserRole> list = tGardenUserRoleMapper.selectByExample(tGardenUserRoleExample);
                    //                tGardenUserRoleExample.clear();
                    //                for (TGardenUserRole tGardenUserRole : list) {
                    //                    String user_role_id = tGardenUserRole.getRoleId();
                    //                    if (StringUtils.isNotEmpty(user_role_id)) {
                    //                        //新增
                    //                        tGardenUserRole.setId(PrimaryGenerater.getInstance().uuid());
                    //                        tGardenUserRole.setRoleId(roleId);
                    //                        tGardenUserRole.setCreateTime(new Date());
                    //                        insertList.add(tGardenUserRole);
                    //                    } else {
                    //                        //修改
                    //                        updateList.add(tGardenUserRole.getId());
                    //                    }
                    //                }
                    //                if (insertList.size() > 0)
                    //                    tGardenUserRoleExt.insertBatch(insertList);
                    //                if (updateList.size() > 0) {
                    //                    dto.clear();
                    //                    dto.put("roleId", roleId);
                    //                    dto.put("updateList", updateList);
                    //                    tGardenUserRoleExt.updateRoleIdBatch(dto);
                    //                }

                    break;
                //企业
                case "2":
                    Dto dto2 = new HashDto();
                    dto2.put("companyId", companyId);
                    dto2.put("roleName", "普通员工");
                    Dto userRoleDto1 = roleService.getUserRoleId(dto2);
                    String roleId2 = userRoleDto1.getString("roleId");

                    List<TCompanyUserRole> insertList1 = new ArrayList<>();
                    TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();

                    for (String userId : userIds.split(",")) {
                        companyUserRoleExample.clear();
                        companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                                .andUserIdEqualTo(userId)
                                .andRoleIdEqualTo(roleId2)
                                .andIsValidEqualTo("1");
                        List<TCompanyUserRole> companyUserRoleList = tCompanyUserRoleMapper.selectByExample(companyUserRoleExample);
                        if (companyUserRoleList != null && !companyUserRoleList.isEmpty()) {
                            TCompanyUserRole companyUserRole = companyUserRoleList.get(0);
                            companyUserRole.setRoleId(roleId);
                            companyUserRole.setModulesIds(modulesIds);
                            companyUserRole.setModifyTime(new Date());
                            companyUserRole.setModifyId(modifyId);

                            insertList1.add(companyUserRole);
                        }
                    }

                    companyUserRoleExample.clear();
                    companyUserRoleExample.or().andRoleIdEqualTo(roleId)
                            .andCompanyIdEqualTo(companyId);
                    tCompanyUserRoleMapper.deleteByExample(companyUserRoleExample);

                    dto2.clear();
                    dto2.put("list", insertList1);
                    if (insertList1.size() > 0)
                        tCompanyUserRoleExt.insertBatch(dto2);


                    //                List<TCompanyUserRole> insertList1 = new ArrayList<>();
                    //                TCompanyUserRoleExample tCompanyUserRoleExample = new TCompanyUserRoleExample();
                    //                TCompanyUserRoleExample.Criteria criteria1 = tCompanyUserRoleExample.createCriteria();
                    //                criteria1.andCompanyIdEqualTo(company_id).andRoleIdNotEqualTo(roleId).andUserIdIn(idList).andIsValidEqualTo("1");
                    //                List<TCompanyUserRole> list1 = tCompanyUserRoleMapper.selectByExample(tCompanyUserRoleExample);
                    //                tCompanyUserRoleExample.clear();
                    //                for (TCompanyUserRole tCompanyUserRole : list1) {
                    //                    String user_role_id = tCompanyUserRole.getRoleId();
                    //                    if (StringUtils.isNotEmpty(user_role_id)) {
                    //                        //新增
                    //                        tCompanyUserRole.setId(PrimaryGenerater.getInstance().uuid());
                    //                        tCompanyUserRole.setCreateTime(new Date());
                    //                        tCompanyUserRole.setRoleId(roleId);
                    //                        insertList1.add(tCompanyUserRole);
                    //                    } else {
                    //                        //修改
                    //                        updateList.add(tCompanyUserRole.getId());
                    //                    }
                    //                }
                    //                if (insertList1.size() > 0)
                    //                    tCompanyUserRoleExt.insertBatch(insertList1);
                    //                if (updateList.size() > 0) {
                    //                    dto.clear();
                    //                    dto.put("updateList", updateList);
                    //                    dto.put("roleId", roleId);
                    //                    tCompanyUserRoleExt.updateRoleIdBatch(dto);
                    //                }
                    break;
            }
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public Dto listUsers(Dto dto) {
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String type = dto.getString("type");
        String roleId = dto.getString("roleId");
        Dto resDto = new HashDto();

        switch (type) {
            case "1":
                TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                gardenUserRoleExample.or().andRoleIdEqualTo(roleId)
                        .andGardenIdEqualTo(gardenId)
                        .andStatusEqualTo("1")
                        .andIsValidEqualTo("1");
                gardenUserRoleExample.setOrderByClause(" create_time");
                List<TGardenUserRole> checkedGardenUsers = tGardenUserRoleMapper.selectByExample(gardenUserRoleExample);

                List<Dto> userList= tGardenUserRoleExt.uncheckedGardenUsers(dto);
                resDto.put("checked", checkedGardenUsers);
                resDto.put("userList", userList);
                break;
            case "2":
                TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                companyUserRoleExample.or().andRoleIdEqualTo(roleId)
                        .andCompanyIdEqualTo(companyId)
                        .andStatusEqualTo("1")
                        .andIsValidEqualTo("1");
                companyUserRoleExample.setOrderByClause(" create_time");
                List<TCompanyUserRole> checkedCompanyUsers = tCompanyUserRoleMapper.selectByExample(companyUserRoleExample);

                List<Dto> userList1= tCompanyUserRoleExt.uncheckedGardenUsers(dto);
                resDto.put("checked", checkedCompanyUsers);
                resDto.put("userList", userList1);
                break;
        }
        return resDto;
    }

    //    @Override
//    @Transactional
//    public void deleteUserToRole(Dto dto) throws ApiException {
//        //选中需要删除的员工进行物理删除
//        String type = dto.getString("type");
//
//        switch (type) {
//            //园区
//            case "1":
//                //批量删除
//                tGardenUserRoleExt.delBatch(dto);
//                break;
//            //企业
//            case "2":
//                tCompanyUserRoleExt.delBatch(dto);
//                break;
//        }
//
//    }


    @Override
    public int getRoleSort(String type) {
        TRoleExample roleExample = new TRoleExample();
        roleExample.or().andTypeEqualTo(type)
                .andIsValidEqualTo("1");
        roleExample.setOrderByClause(" sort desc limit 1");
        List<TRole> roleList = tRoleMapper.selectByExample(roleExample);
        if (roleList == null || roleList.isEmpty())
            return 1;
        else {
            return roleList.get(0).getSort() + 1;
        }
    }

    @Override
    @Transactional
    public void postRole(Dto dto) throws ApiException {
        String roleName = dto.getString("roleName");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String roleId = PrimaryGenerater.getInstance().uuid();
        int count;

        try {
            count = checkRoleName(dto);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_ROLE);

            int sort = getRoleSort("0");
            TRole role = new TRole();
            role.setId(roleId);
            role.setUnitId(SystemUtils.isEmpty(companyId) ? gardenId : companyId);
            role.setRoleName(roleName);
            role.setType("0");
            role.setSort(sort);
            role.setCreateTime(new Date());
            role.setIsValid("1");
            count = tRoleMapper.insertSelective(role);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void putRole(Dto dto) throws ApiException {
        String roleName = dto.getString("roleName");
        String roleId = dto.getString("roleId");
        String modifyId = dto.getString("modifyId");
        int count;

        try {
            count = checkRoleName(dto);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_ROLE);

            TRole role = new TRole();
            role.setId(roleId);
            role.setRoleName(roleName);
            role.setModifyTime(new Date());
            role.setModifyId(modifyId);
            count = tRoleMapper.updateByPrimaryKeySelective(role);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "role");

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void delRole(Dto dto) throws ApiException {
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String roleId = dto.getString("roleId");
        int count;

        try {
            TRoleExample roleExample = new TRoleExample();
            roleExample.or().andIdEqualTo(roleId)
                    .andTypeEqualTo("0");
            count = tRoleMapper.deleteByExample(roleExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "role");

            if (!SystemUtils.isEmpty(companyId)) {
                TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                        .andRoleIdEqualTo(roleId);
                tCompanyUserRoleMapper.deleteByExample(companyUserRoleExample);
            } else {
                TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                        .andRoleIdEqualTo(roleId);
                tGardenUserRoleMapper.deleteByExample(gardenUserRoleExample);
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    //    private void getRoleList(List<Dto> role_list) {
//        if (role_list != null && role_list.size() > 0) {
//            String ids = "";
//            Dto dto1 = new HashDto();
//            for (Dto dto2 : role_list) {
//                int h5_module = 0;
//                int backend_module = 0;
//                ids = dto2.getString("modules_id");
//                dto1.put("modules_type", "0,1,2".split(","));
//                dto1.put("ids", ids.split(","));
//
//                String[] strs = "0,1,2".split(",");
//                String[] strs_ids = ids.split(",");
//                List<String> typeIds = new ArrayList<String>();
//                List<String> idList = new ArrayList<String>();
//                for (String str : strs) {
//                    typeIds.add(str);
//                }
//                for (String str : strs_ids) {
//                    idList.add(str);
//                }
//
//                TModulesExample tModulesExample = new TModulesExample();
//                tModulesExample.or().andModulesTypeIn(typeIds)
//                        .andIdIn(idList)
//                        .andIsValidEqualTo("1");
//
//                List<TModules> module_list = tModulesMapper.selectByExample(tModulesExample);
//                if (module_list != null && module_list.size() > 0) {
//                    for (TModules module : module_list) {
//                        if (module.getModulesType().equals("0"))
//                            h5_module++;
//                        else
//                            backend_module++;
//                    }
//                }
//                dto2.put("h5_module", h5_module);
//                dto2.put("backend_module", backend_module);
//            }
//        }
//    }

    /**
     * 获取userId , roleId
     *
     * @param dto propertyId 非必传
     *            gardenId  非必传
     *            companyId
     *            roleName
     */
    @Override
    public Dto getUserRoleId(Dto dto) {
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String propertyId = dto.getString("propertyId");
        String roleName = dto.getString("roleName");
        String userId = "";
        String roleId;
        Dto resDto = new HashDto();
        if (SystemUtils.isEmpty(roleName)) {
            return resDto;
        }
        if (!SystemUtils.isEmpty(gardenId)) {
            resDto = tRoleExt.getGardenRole(dto);
        } else if (!SystemUtils.isEmpty(companyId)) {
            resDto = tRoleExt.getCompanyRole(dto);
        } else {
            TRoleExample roleExample = new TRoleExample();
            roleExample.or().andUnitIdEqualTo(propertyId)
                    .andRoleNameEqualTo(roleName)
                    .andIsValidEqualTo("1");
            List<TRole> roleList = tRoleMapper.selectByExample(roleExample);
            if (roleList != null && !roleList.isEmpty()) {
                roleId = roleList.get(0).getId();
                resDto.put("roleId", roleId);
                TPropertyUserRoleExample propertyUserRoleExample = new TPropertyUserRoleExample();
                propertyUserRoleExample.or().andPropertyIdEqualTo(propertyId)
                        .andRoleIdEqualTo(roleId)
                        .andIsValidEqualTo("1");
                List<TPropertyUserRole> propertyUserRoleList = tPropertyUserRoleMapper.selectByExample(propertyUserRoleExample);
                if (propertyUserRoleList != null && !propertyUserRoleList.isEmpty())
                    userId = propertyUserRoleList.get(0).getUserId();
                resDto.put("userId", userId);
            }
        }
        return resDto;
    }
}

