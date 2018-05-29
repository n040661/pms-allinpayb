package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TPropertyExt;
import com.dominator.service.*;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private static RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TPropertyExt tPropertyExt;

    @Autowired
    private TPropertyMapper tPropertyMapper;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TPropertyUserRoleMapper tPropertyUserRoleMapper;

    @Autowired
    private TGardenWeixinPublicConfigMapper tGardenWeixinPublicConfigMapper;

    @Autowired
    private TPropertyWeixinPublicConfigMapper tPropertyWeixinPublicConfigMapper;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private TPayTypeMapper tPayTypeMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModulesService modulesService;

    @Autowired
    private UserService userService;


    @Override
    public int checkPropertyName(String propertyName, String propertyId) {
        TPropertyExample propertyExample = new TPropertyExample();
        TPropertyExample.Criteria criteria = propertyExample.createCriteria();
        criteria.andPropertyNameEqualTo(propertyName);
        criteria.andIsValidEqualTo("1");
        if (!SystemUtils.isEmpty(propertyId)) {
            criteria.andIdNotEqualTo(propertyId);
        }
        return tPropertyMapper.countByExample(propertyExample);
    }

    @Override
    @Transactional
    public void postProperty(Dto dto) throws ApiException {
        String user_id;
        String role_id = PrimaryGenerater.getInstance().uuid();
        String property_id = PrimaryGenerater.getInstance().uuid();
        String property_name = dto.getString("property_name");
        String logo_url = dto.getString("logo_url");
        String logo_color = dto.getString("logo_color");
        String telephone = dto.getString("telephone");
        String phone_num = dto.getString("user_name");
        String email = dto.getString("email");
//        String bank_name = dto.getString("bank_name");
//        String bank_num = dto.getString("bank_num");
//        String bank_account_name = dto.getString("bank_account_name");
        String nick_name = dto.getString("nick_name");
        String province = dto.getString("province");
        String city = dto.getString("city");
        String area = dto.getString("area");
        String street = dto.getString("street");

        String user_name = dto.getString("user_name");

        int count;
        try {
            count = checkPropertyName(property_name, null);
            if (count != 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_PROPERTY);

            count = userService.checkUserName(user_name);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);

            //添加物业信息
            TProperty property = new TProperty();
            property.setId(property_id);
            property.setPropertyName(property_name);
            property.setLogoUrl(logo_url);
            property.setLogoColor(logo_color);
            property.setTelephone(telephone);
            property.setPhoneNum(phone_num);
            property.setEmail(email);
//        property.setBankName(bank_name);
//        property.setBankNum(bank_num);
//        property.setBankAccountName(bank_account_name);
            property.setCreateTime(new Date());
            property.setIsValid("1");
            count = tPropertyMapper.insertSelective(property);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property");

            //加物业联系地址
            TAddress address = new TAddress();
            address.setId(PrimaryGenerater.getInstance().uuid());
            address.setOwnerId(property_id);
            address.setType("2");
            address.setProvince(province);
            address.setCity(city);
            address.setArea(area);
            address.setStreet(street);
            address.setCreateTime(new Date());
            address.setIsValid("1");
            count = tAddressMapper.insertSelective(address);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "address");

            //创建物业管理员角色
            int sort = roleService.getRoleSort("4");
            TRole role = new TRole();
            role.setId(role_id);
            role.setUnitId(property_id);
            role.setRoleName("物业管理员");
            role.setType("4");
            role.setSort(sort);
            role.setCreateTime(new Date());
            role.setIsValid("1");
            count = tRoleMapper.insertSelective(role);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "role");

            //查管理员信息
            TUserExample userExample = new TUserExample();
            userExample.or().andUserNameEqualTo(user_name)
                    .andIsValidEqualTo("1");
            List<TUser> userList = tUserMapper.selectByExample(userExample);
            if (userList != null && !userList.isEmpty()) {
                user_id = userList.get(0).getId();
                //关联物业和管理员
                TPropertyUserRole propertyUserRole = new TPropertyUserRole();
                propertyUserRole.setId(PrimaryGenerater.getInstance().uuid());
                propertyUserRole.setPropertyId(property_id);
                propertyUserRole.setUserId(user_id);
                propertyUserRole.setNickName(nick_name);
                propertyUserRole.setRoleId(role_id);
                propertyUserRole.setCreateTime(new Date());
                propertyUserRole.setIsValid("1");
                count = tPropertyUserRoleMapper.insertSelective(propertyUserRole);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "propertyUserRole");

                String[] wechat_ids = dto.getString("wechat_ids").split(",");
                Dto dto1 = new HashDto();
                for (String s : wechat_ids) {
                    dto1.clear();
                    dto1.put("property_id", property_id);
                    dto1.put("wechat_id", s);
                    wechatService.addPropertyWechat(dto1);
                }
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void putProperty(Dto dto) throws ApiException {
        String user_id;
        String role_id;
        String property_id = dto.getString("property_id");
        String property_name = dto.getString("property_name");
        String logo_url = dto.getString("logo_url");
        String logo_color = dto.getString("logo_color");
        String telephone = dto.getString("telephone");
        String phone_num = dto.getString("user_name");
        String email = dto.getString("email");
//        String bank_name = dto.getString("bank_name");
//        String bank_num = dto.getString("bank_num");
//        String bank_account_name = dto.getString("bank_account_name");
        String nick_name = dto.getString("nick_name");
        String province = dto.getString("province");
        String city = dto.getString("city");
        String area = dto.getString("area");
        String street = dto.getString("street");
        String modify_id = dto.getString("modify_id");

        String user_name = dto.getString("user_name");

        int count;
        try {
            count = checkPropertyName(property_name, property_id);
            if (count != 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_PROPERTY);

            count = userService.checkUserName(user_name);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);

            //修改物业信息
            TProperty property = new TProperty();
            property.setId(property_id);
            property.setPropertyName(property_name);
            property.setLogoUrl(logo_url);
            property.setLogoColor(logo_color);
            property.setTelephone(telephone);
            property.setPhoneNum(phone_num);
            property.setEmail(email);
//        property.setBankName(bank_name);
//        property.setBankNum(bank_num);
//        property.setBankAccountName(bank_account_name);
            property.setModifyTime(new Date());
            property.setModifyId(modify_id);
            count = tPropertyMapper.updateByPrimaryKeySelective(property);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property");

            //修改物业联系地址
            TAddress address = new TAddress();
            address.setProvince(province);
            address.setCity(city);
            address.setArea(area);
            address.setStreet(street);
            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(property_id)
                    .andTypeEqualTo("2")
                    .andIsValidEqualTo("1");
            count = tAddressMapper.updateByExampleSelective(address, addressExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "address");

            //查该物业管理员role_id,user_id
            Dto dto1 = new HashDto();
            dto1.put("propertyId", property_id);
            dto1.put("roleName", "物业管理员");
            Dto userRoleDto = roleService.getUserRoleId(dto1);
            role_id = userRoleDto.getString("roleId");
            user_id = userRoleDto.getString("userId");

            TPropertyUserRoleExample propertyUserRoleExample = new TPropertyUserRoleExample();
            propertyUserRoleExample.or().andPropertyIdEqualTo(property_id)
                    .andRoleIdEqualTo(role_id)
                    .andUserIdEqualTo(user_id)
                    .andIsValidEqualTo("1");
            //更新物业和管理员的关联
            TPropertyUserRole propertyUserRole = new TPropertyUserRole();
            propertyUserRole.setUserId(user_id);
            propertyUserRole.setNickName(nick_name);
            propertyUserRole.setModifyTime(new Date());
            propertyUserRole.setModifyId(modify_id);
            count = tPropertyUserRoleMapper.updateByExampleSelective(propertyUserRole, propertyUserRoleExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "propertyUserRole");

            TPropertyWeixinPublicConfigExample tPropertyWeixinPublicConfigExample = new TPropertyWeixinPublicConfigExample();
            tPropertyWeixinPublicConfigExample.or()
                    .andPropertyIdEqualTo(property_id)
                    .andIsValidEqualTo("1");
            tPropertyWeixinPublicConfigMapper.deleteByExample(tPropertyWeixinPublicConfigExample);
            String[] wechat_ids = dto.getString("wechat_ids").split(",");
            for (String s : wechat_ids) {
                dto1.clear();
                dto1.put("property_id", property_id);
                dto1.put("wechat_id", s);
                wechatService.addPropertyWechat(dto1);
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public ApiMessage getGardenToManager(Dto dto) {
        String user_id = dto.getString("manager_id");
//        List<T_gardenPO> garden_list = tPropertyDaoExt.getGardenToManager(user_id);
        List<TProperty> garden_list = tPropertyExt.getGardenToManager(user_id);
        Dto resDto = new HashDto();
        resDto.put("garden_list", garden_list);
        return ApiMessageUtil.success(resDto);
    }

    @Override
    @Transactional
    public ApiMessage addGardenToManager(Dto dto) throws ApiException {
//        int count = 0;
//        String property_id = dto.getString("property_id");
//        String user_id = dto.getString("manager_id");
//        String garden_ids = dto.getString("garden_ids");
//        String role_id = "";
//        String modules_ids = "";
//        try {
//            Dto dto1 = new HashDto();
//            dto1.put("property_id", property_id);
//            dto1.put("user_id", user_id);
//            dto1.put("is_valid", "1");
////            T_property_user_rolePO property_user_role = t_property_user_roleDao.selectOne(dto1);
//            TPropertyUserRoleExample tPropertyUserRoleExample = new TPropertyUserRoleExample();
//            tPropertyUserRoleExample.or().andPropertyIdEqualTo(property_id).andUserIdEqualTo(user_id).andIsValidEqualTo("1");
//            List<TPropertyUserRole> tPropertyUserRoleList = tPropertyUserRoleMapper.selectByExample(tPropertyUserRoleExample);
////            if (property_user_role != null)
////                role_id = property_user_role.getRole_id();
//            if(tPropertyUserRoleList!=null || !tPropertyUserRoleList.isEmpty()){
//                TPropertyUserRole tPropertyUserRole = tPropertyUserRoleList.get(0);
//                role_id = tPropertyUserRole.getRoleId();
//            }
//
//            else
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property_user_role");
//
//            //查是否配过菜单
//            dto1.clear();
//            dto1.put("property_id", property_id);
//            dto1.put("role_id", role_id);
//            dto1.put("is_valid", "1");
////            T_property_role_modulesPO property_role_modules = t_property_role_modulesDao.selectOne(dto1);
//            TPropertyRoleModulesExample tPropertyRoleModulesExample = new TPropertyRoleModulesExample();
//            tPropertyRoleModulesExample.or().andPropertyIdEqualTo(property_id).andRoleIdEqualTo(role_id).andIsValidEqualTo("1");
//            List<TPropertyRoleModules> tPropertyRoleModulesList = tPropertyRoleModulesMapper.selectByExample(tPropertyRoleModulesExample);
//            TPropertyRoleModules tPropertyRoleModules=null;
////            if (tPropertyRoleModules != null)
////                modules_ids = property_role_modules.getModules_id();
//            if(tPropertyRoleModulesList!=null || !tPropertyRoleModulesList.isEmpty()){
//                tPropertyRoleModules = tPropertyRoleModulesList.get(0);
//                modules_ids =  tPropertyRoleModules.getModulesId();
//            }
//
//
//            //删除之前的garden_user，garden_user_role，garden_role_modules
////            tPropertyDaoExt.deleteGardenUser(user_id);
//
//            dto1.clear();
//            dto1.put("user_id", user_id);
//            dto1.put("role_id", role_id);
////            tPropertyDaoExt.deleteGardenUserRole(dto1);
//            TGardenUserRoleExample tGardenUserRoleExample = new TGardenUserRoleExample();
//            tGardenUserRoleExample.or().andUserIdEqualTo(user_id);
//            tGardenUserRoleMapper.deleteByExample(tGardenUserRoleExample);
//            if (tPropertyRoleModules != null) {
//                TGardenRoleModulesExample tGardenRoleModulesExample = new TGardenRoleModulesExample();
//                tGardenRoleModulesExample.or().andRoleIdEqualTo(role_id);
//                tGardenRoleModulesMapper.deleteByExample(tGardenRoleModulesExample);
////                tPropertyDaoExt.deleteGardenRoleModules(dto1);
//
//            }
//
//
//            if (!SystemUtils.isEmpty(garden_ids)) {
////                T_garden_userPO garden_user = new T_garden_userPO();
////                T_garden_user_rolePO garden_user_role = new T_garden_user_rolePO();
////                T_garden_role_modulesPO garden_role_modules = new T_garden_role_modulesPO();
//                TGardenUser garden_user = new TGardenUser();
//                TGardenUserRole garden_user_role = new TGardenUserRole();
//                TGardenRoleModules garden_role_modules =new TGardenRoleModules();
//                for (int i = 0; i < garden_ids.split(",").length; i++) {
//                    //关联园区用户
//                    garden_user.setId(PrimaryGenerater.getInstance().uuid());
////                    garden_user.setGarden_id(garden_ids.split(",")[i]);
//                    garden_user.setGardenId(garden_ids.split(",")[i]);
//
////                    garden_user.setUser_id(user_id);
//                    garden_user.setUserId(user_id);
////                    garden_user.setCreate_time(new Date());
//                    garden_user.setCreateTime(new Date());
//
//                    garden_user.setStatus("1");
////                    garden_user.setIs_valid("1");
//                    garden_user.setIsValid("1");
////                    count = t_garden_userDao.insert(garden_user);
//                    count = tGardenUserMapper.insert(garden_user);
//                    if (count != 1)
//                        throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden_user_" + i);
//
//                    //关联用户与角色
////                    garden_user_role.clear();
//                    garden_user_role = new TGardenUserRole();
//                    garden_user_role.setId(PrimaryGenerater.getInstance().uuid());
////                    garden_user_role.setGarden_id(garden_ids.split(",")[i]);
//                    garden_user_role.setGardenId(garden_ids.split(",")[i]);
////                    garden_user_role.setRole_id(role_id);
//                    garden_user_role.setRoleId(role_id);
////                    garden_user_role.setUser_id(user_id);
//                    garden_user_role.setUserId(user_id);
////                    garden_user_role.setCreate_time(new Date());
//                    garden_user_role.setCreateTime(new Date());
////                    garden_user_role.setIs_valid("1");
//                    garden_user_role.setIsValid("1");
////                    count = t_garden_user_roleDao.insert(garden_user_role);
//                    count = tGardenUserRoleMapper.insert(garden_user_role);
//                    if (count != 1)
//                        throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden_user_role" + i);
//
//                    if (/*property_role_modules*/ tPropertyRoleModules != null) {
//                        garden_role_modules= new TGardenRoleModules();
//                        garden_role_modules.setId(PrimaryGenerater.getInstance().uuid());
////                        garden_role_modules.setGarden_id(garden_ids.split(",")[i]);
//                        garden_role_modules.setGardenId(garden_ids.split(",")[i]);
////                        garden_role_modules.setRole_id(role_id);
//                        garden_role_modules.setRoleId(role_id);
////                        garden_role_modules.setModules_id(modules_ids);
//                        garden_role_modules.setModulesId(modules_ids);
////                        garden_role_modules.setCreate_time(new Date());
//                        garden_role_modules.setCreateTime(new Date());
////                        garden_role_modules.setIs_valid("1");
//                        garden_role_modules.setIsValid("1");
////                        count = t_garden_role_modulesDao.insert(garden_role_modules);
//                        count = tGardenRoleModulesMapper.insert(garden_role_modules);
//                        if (count != 1)
//                            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden_role_modules" + i);
//
//                    }
//                }
//            }
//            return ApiMessageUtil.success();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
        return null;
    }

    /**
     * 园区端--获取园区基本信息，微信公众号列表，管理员
     *
     * @param dto garden_id String 必传 园区id
     *            flag String 必传 列表状态：0只传ID，1详细信息
     */
    @Override
    public ApiMessage getGardenBaseInfo(Dto dto) {
        String garden_id = dto.getString("garden_id");
        String flag = dto.getString("flag");
        Dto resDto = new HashDto();
        //园区基本信息
        Object object = ru.getObject("garden_" + dto.getString("garden_id"));
        TGarden garden;
        if (object == null) {
            garden = tGardenMapper.selectByPrimaryKey(dto.getString("garden_id"));
            if (garden != null) {
                ru.setObject("garden_" + garden.getId(), garden);
            }
        } else {
            garden = (TGarden) object;
        }

        resDto.put("garden", garden);

        //园区管理员
        Dto dto2 = new HashDto();
        dto2.put("gardenId", garden_id);
        dto2.put("roleName", "园区管理员");
        Dto manager = roleService.getUserRoleId(dto2);

        resDto.put("manager", manager);

        if ("0".equals(flag)) {
            //园区微信公众号id列表
//            List<String> wechats = tPropertyDaoExt.listGardenWechatIds(garden_id);
            TGardenWeixinPublicConfigExample tGardenWeixinPublicConfigExample = new TGardenWeixinPublicConfigExample();
            tGardenWeixinPublicConfigExample.or().andGardenIdEqualTo(garden_id).andIsValidEqualTo("1");
            tGardenWeixinPublicConfigExample.setOrderByClause("create_time");
            List<TGardenWeixinPublicConfig> weixin_List = tGardenWeixinPublicConfigMapper.selectByExample(tGardenWeixinPublicConfigExample);
            List<String> wechats = new ArrayList<>();
            for (TGardenWeixinPublicConfig weixin_id_list : weixin_List) {
                wechats.add(weixin_id_list.getWeixinConfigId());
            }
            resDto.put("wechats", wechats);
        } else {
            //园区微信公众号列表
            List<Dto> wechats = tPropertyExt.listGardenWechats(garden_id);
            resDto.put("wechats", wechats);
        }
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 获取园区管理员列表
     *
     * @param dto property_id String 必传
     */
//    @Override
//    public ApiMessage listManagers(Dto dto) {
//        dto.put("super_admin_id", propertiesLoader.getProperty("super_admin_id"));
//        dto.put("role_name", "园区管理员");
//        List<Dto> managers = tPropertyDaoExt.listManagers(dto);
//        Dto resDto = new HashDto();
//        resDto.put("managers", managers);
//        return ApiMessageUtil.success(resDto);
//    }
    @Override
    public ApiMessage listManagers(Dto dto) {
//        String property_id = dto.getString("property_id");
//        dto.put("role_name", "园区管理员");
//        List<Dto> managerlist = tPropertyExt.listManagers(dto);
//        if (managerlist != null && managerlist.size() > 0) {
//            String user_id = "";
//            String garden_id = "";
//            String role_id = "";
//            String module_status = "0";
//            int count = 0;
//            StringBuffer sb = new StringBuffer();
//            Dto dto1 = new HashDto();
//            for (Dto managerDto : managerlist) {
//                user_id = managerDto.getString("id");
//                module_status = "0";
//                //菜单配置状态
//                dto1.clear();
//                dto1.put("property_id", property_id);
//                dto1.put("user_id", user_id);
//                dto1.put("is_valid", "1");
//                TPropertyUserRoleExample tPropertyUserRoleExample = new TPropertyUserRoleExample();
//                tPropertyUserRoleExample.or().andPropertyIdEqualTo(property_id).andUserIdEqualTo(user_id).andIsValidEqualTo("1");
//                TPropertyUserRole property_user_role = (TPropertyUserRole) tPropertyUserRoleMapper.selectByExample(tPropertyUserRoleExample);
////                T_property_user_rolePO property_user_role = t_property_user_roleDao.selectOne(dto1);
//                if (property_user_role != null) {
////                    role_id = property_user_role.getRole_id();
//                    role_id = property_user_role.getRoleId();
//                    dto1.clear();
//                    dto1.put("property_id", property_id);
//                    dto1.put("role_id", role_id);
//                    dto1.put("is_valid", "1");
//                    TPropertyRoleModulesExample tPropertyRoleModulesExample = new TPropertyRoleModulesExample();
//                    tPropertyRoleModulesExample.or().andPropertyIdEqualTo(property_id).andRoleIdEqualTo(role_id).andIsValidEqualTo("1");
//                    TPropertyRoleModules property_role_modules = (TPropertyRoleModules) tPropertyRoleModulesMapper.selectByExample(tPropertyRoleModulesExample);
////                    T_property_role_modulesPO property_role_modules = t_property_role_modulesDao.selectOne(dto1);
//                    if (property_role_modules != null) {
////                        String ids = property_role_modules.getModules_id();
//                        String ids = property_role_modules.getModulesId();
//                        List<String> idList = new ArrayList<>();
//                        if (!SystemUtils.isEmpty(ids)) {
//                            dto1.clear();
//                            dto1.put("ids", ids.split(","));
//                            String[] ides = ids.split(",");
//                            for(String str : ides){
//                                idList.add(str);
//                            }
//                            TModulesExample tModulesExample = new TModulesExample();
//                            tModulesExample.or().andIdIn(idList).andIsValidEqualTo("1").andModulesTypeEqualTo("1");
//                            count=tModulesMapper.countByExample(tModulesExample);
////                            count = tPropertyDaoExt.checkGardenModule(dto1);
//
//                            if (count > 0)
//                                module_status = "1";
//                        }
//                    }
//                }
//                managerDto.put("module_status", module_status);
//                //列表加入管理园区名称
////                List<T_gardenPO> garden_list = tPropertyDaoExt.listGardens(user_id);
////                sb.delete(0, sb.length());
////                if (garden_list != null && garden_list.size() > 0) {
////                    garden_id = garden_list.get(0).getId();
////                    for (int i = 0; i < garden_list.size(); i++) {
////                        if (i == 0) {
////                            sb.append(garden_list.get(i).getGarden_name());
////                        } else {
////                            sb.append("<br/>" + garden_list.get(i).getGarden_name());
////                        }
////                    }
////                }
////                managerDto.put("garden_names", sb.toString());
//            }
//        }
//        Dto resDto = new HashDto();
//        resDto.put("managerlist", managerlist);
//        return ApiMessageUtil.success(resDto);
        return null;
    }

    @Override
    @Transactional
    public ApiMessage addManager(Dto dto) throws ApiException {
//        String user_id = dto.getString("user_id");
//        String property_id = dto.getString("property_id");
//        String user_name = dto.getString("phone_num");
//        dto.put("user_name", user_name);
//        String nick_name = dto.getString("nick_name");
//        int count = 0;
//        try {
//
//            if (SystemUtils.isEmpty(user_id)) {
//                //插t_user,t_property_user,t_property_user_role 3个表
//                //根据user_name查t_user,看用户是否存在
//                //用户不存在，直接添加
//                //用户存在
//                //  是游客--修改用户信息
//                //  不是--查t_property_user
//                //      是--该园区用户，抛异常
//                //      不是--检查nick_name ,user_name 是否匹配
//                //          匹配--修改用户信息
//                //          不匹配--抛异常
//
//                //根据user_name查t_user,看用户是否存在
//                Dto dto1 = new HashDto();
//                dto1.put("user_name", user_name);
//                dto1.put("is_valid", "1");
//                TUserExample tUserExample = new TUserExample();
//                tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");
////                T_userPO user = t_userDao.selectOne(dto1);
//                TUser user = null;
//                List<TUser> userList = tUserMapper.selectByExample(tUserExample);
//                if (userList == null) {
//                    //不存在，直接添加
//                    user_id = PrimaryGenerater.getInstance().uuid();
//                    dto.put("user_id", user_id);
//                    userService.addUser(dto);
//                } else {
//                    user = userList.get(0);
//                    user_id = user.getId();
//                    dto.put("user_id", user_id);
//                    //存在，看是不是游客
//                    //是游客，修改用户信息
//                    boolean b = nick_name.length() > 1 && nick_name.substring(0, 2).equals("游客");
//                    if (b)
//                        userService.updateUser(dto);
//                    else {
//                        user_id = user.getId();
//                        dto1.clear();
//                        dto1.put("property_id", property_id);
//                        dto1.put("user_id", user_id);
//                        dto1.put("is_valid", "1");
////                        count = t_property_userDao.rows(dto1);
//                        TPropertyUserExample tPropertyUserExample = new TPropertyUserExample();
//                        tPropertyUserExample.or().andPropertyIdEqualTo(property_id).andUserIdEqualTo(user_id).andIsValidEqualTo("1");
//                        count = tPropertyUserMapper.countByExample(tPropertyUserExample);
//                        if (count > 0)
//                            //      是该物业用户--抛异常
//                            throw new ApiException(ReqEnums.REQ_USER_REPEAT.getCode(), ReqEnums.REQ_USER_REPEAT.getMsg());
//
//                        //      不是该物业用户--检查nick_name ,user_name 是否匹配
//                        if (!user.getNickName().equals(nick_name))
//                            //          不匹配--抛异常
//                            throw new ApiException(ReqEnums.REQ_USER_INFO_NOT_MATCH.getCode(), ReqEnums.REQ_USER_INFO_NOT_MATCH.getMsg());
//                        //          匹配--修改用户信息
//                        userService.updateUser(dto);
//                    }
//                }
//
//                //检查用户是否存在
////                Dto dto1 = new HashDto();
////                dto1.put("user_name", user_name);
////                dto1.put("is_valid", "1");
////                T_userPO user1 = t_userDao.selectOne(dto1);
////                T_userPO user = new T_userPO();
////                if (user1 != null) {
////                    user_id = user1.getId();
////                    countMsg = tPropertyDaoExt.countUnitUser(user_id);
////                    String nick_name = user1.getNick_name();
////                    boolean b1 = countMsg == 0 && nick_name.equals(dto.getString("nick_name"));
////                    boolean b2 = nick_name.length() > 1 && nick_name.substring(0, 2).equals("游客");
////                    if (b1 || b2) {
////                        user.copyProperties(dto);
////                        user.setId(user_id);
////                        user.setUser_name(user_name);
////                        user.setPassword(Des3Utils.encode(password));
////                        user.setModify_time(new Date());
////                        countMsg = t_userDao.updateByKey(user);
////                        if (countMsg != 1) {
////                            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "update user");
////                        }
////                    } else {
////                        throw new ApiException(ReqEnums.REQ_USER_REPEAT.getCode(), ReqEnums.REQ_USER_REPEAT.getMsg());
////                    }
////                } else {
////                    //1 t_user 创建用户
////                    user_id = PrimaryGenerater.getInstance().uuid();
////                    user.copyProperties(dto);
////                    user.setId(user_id);
////                    user.setUser_name(user_name);
////                    user.setPassword(Des3Utils.encode(password));
////                    user.setCreate_time(new Date());
////                    user.setIs_valid("1");
////                    countMsg = t_userDao.insert(user);
////                    if (countMsg != 1) {
////                        throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "insert user");
////                    }
////                }
//
//                //2 t_property_user 关联物业用户
////                T_property_userPO property_user = new T_property_userPO();
//                TPropertyUser property_user = new TPropertyUser();
//                property_user.setId(PrimaryGenerater.getInstance().uuid());
////                property_user.setProperty_id(property_id);
////                property_user.setUser_id(user_id);
////                property_user.setHire_date(new Date());
////                property_user.setCreate_time(new Date());
//                property_user.setStatus("1");
////                property_user.setIs_valid("1");
//
//                property_user.setPropertyId(property_id);
//                property_user.setUserId(user_id);
//                property_user.setHireDate(new Date());
//                property_user.setCreateTime(new Date());
//                property_user.setIsValid("1");
//
////                count = t_property_userDao.insert(property_user);
//                count = tPropertyUserMapper.insert(property_user);
//                if (count != 1) {
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property_user");
//                }
//
//                //3 t_role 加角色名，默认存在的不用加
//                String role_id = PrimaryGenerater.getInstance().uuid();
//                Dto dto2 = new HashDto();
//                dto2.put("role_id", role_id);
//                dto2.put("role_name", "园区管理员");
//                roleService.addRole(dto2);
//
//                //4 t_property_user_role 关联用户与角色
////                T_property_user_rolePO property_user_role = new T_property_user_rolePO();
//                TPropertyUserRole property_user_role = new TPropertyUserRole();
//                property_user_role.setId(PrimaryGenerater.getInstance().uuid());
////                property_user_role.setProperty_id(property_id);
////                property_user_role.setRole_id(role_id);
////                property_user_role.setUser_id(user_id);
////                property_user_role.setCreate_time(new Date());
////                property_user_role.setIs_valid("1");
//
//                property_user_role.setPropertyId(property_id);
//                property_user_role.setRoleId(role_id);
//                property_user_role.setUserId(user_id);
//                property_user_role.setCreateTime(new Date());
//                property_user_role.setIsValid("1");
////                count = t_property_user_roleDao.insert(property_user_role);
//                count = tPropertyUserRoleMapper.insert(property_user_role);
//                if (count != 1) {
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property_user_role");
//                }
//
//                Dto resDto = new HashDto();
//                resDto.put("manager", user);
//                return ApiMessageUtil.success(resDto);
//            } else {
//                dto.put("user_name", user_name);
//                userService.updateUser(dto);
//                return ApiMessageUtil.success();
//            }
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
        return null;
    }

    //
    @Override
    @Transactional
    public ApiMessage delManager(Dto dto) {
//        try {
//            String property_id = dto.getString("property_id");
//            String user_id = dto.getString("user_id");
//            String role_id = "";
//            int count = 0;
//            Dto dto1 = new HashDto();
//            dto1.put("user_id", user_id);
//            dto1.put("is_valid", "1");
//            TGardenUserExample tGardenUserExample = new TGardenUserExample();
//            tGardenUserExample.or().andUserIdEqualTo(user_id).andIsValidEqualTo("1");
////            count = t_garden_userDao.rows(dto1);
//            count = tGardenUserMapper.countByExample(tGardenUserExample);
//            if (count > 0)
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "can not delete this manager");
//
//            dto1.clear();
//            dto1.put("user_id", user_id);
//            dto1.put("property_id", property_id);
//            dto1.put("is_valid", "1");
////            T_property_userPO property_user = t_property_userDao.selectOne(dto1);
//            TPropertyUserExample tPropertyUserExample = new TPropertyUserExample();
//            tPropertyUserExample.or().andUserIdEqualTo(user_id).andPropertyIdEqualTo(property_id).andIsValidEqualTo("1");
//            List<TPropertyUser> property_user_list = tPropertyUserMapper.selectByExample(tPropertyUserExample);
//            if (property_user_list != null || !property_user_list.isEmpty()) {
//                TPropertyUser property_user = property_user_list.get(0);
////                count = t_property_userDao.deleteByKey(property_user.getId());
//                count = tPropertyUserMapper.deleteByPrimaryKey(property_user.getId());
//                if (count != 1)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property_user");
//            }
////            T_property_user_rolePO property_user_role = t_property_user_roleDao.selectOne(dto1);
//            TPropertyUserRoleExample tPropertyUserRoleExample = new TPropertyUserRoleExample();
//            tPropertyUserRoleExample.or().andUserIdEqualTo(user_id).andPropertyIdEqualTo(property_id).andIsValidEqualTo("1");
//            TPropertyUserRole property_user_role = (TPropertyUserRole) tPropertyUserRoleMapper.selectByExample(tPropertyUserRoleExample);
//            if (property_user_role != null) {
////                role_id = property_user_role.getRole_id();
//                role_id = property_user_role.getRoleId();
////                count = t_property_user_roleDao.deleteByKey(property_user_role.getId());
//                count = tPropertyUserRoleMapper.deleteByPrimaryKey(property_user_role.getId());
//                if (count != 1)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "property_user_role");
//            }
//            dto1.put("role_id", role_id);
////            tRoleDaoExt.deletePropertyRoleModules(dto1);
//
//            TPropertyRoleModulesExample tPropertyRoleModulesExample = new TPropertyRoleModulesExample();
//            tPropertyRoleModulesExample.or().andPropertyIdEqualTo(property_id).andRoleIdEqualTo(role_id);
//            tPropertyRoleModulesMapper.deleteByExample(tPropertyRoleModulesExample);
//
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
        return null;
    }

    @Override
    @Transactional
    public ApiMessage saveGardenInfo(Dto dto) throws ApiException {
//
//        try {
//            int count = 0;
//            //检查园区名
//            Dto dto1 = new HashDto();
//            dto1.put("garden_name", dto.getString("garden_name"));
//            dto1.put("is_valid", "1");
////            count = t_gardenDao.rows(dto1);
//            TGardenExample tGardenExample = new TGardenExample();
//            tGardenExample.or().andGardenNameEqualTo(dto.getString("garden_name")).andIsValidEqualTo("1");
//            count = tGardenMapper.countByExample(tGardenExample);
//            if (count >= 1) {
//                throw new ApiException(ReqEnums.REQ_USER_REPEAT.getCode(), ReqEnums.REQ_USER_REPEAT.getMsg());
//            }
//
//            //新增操作
//            String garden_id = PrimaryGenerater.getInstance().uuid();
////            T_gardenPO garden = new T_gardenPO();
//            TGarden garden = new TGarden();
////            garden.copyProperties(dto);
//            garden.setPropertyId(dto.getString("property_id"));
//            garden.setZjParkId(dto.getString("zj_park_id"));
//            garden.setGardenName(dto.getString("garden_name"));
//            garden.setTelephone(dto.getString("telephone"));
//            garden.setPhoneNum(dto.getString("phone_num"));
//            garden.setBankName(dto.getString("bank_name"));
//            garden.setBankNum(dto.getString("bank_num"));
//            garden.setCollectionUnit(dto.getString("collection_unit"));
//            garden.setProvince(dto.getString("province"));
//            garden.setCity(dto.getString("city"));
//            garden.setArea(dto.getString("area"));
//            garden.setStreet(dto.getString("street"));
//            garden.setModifyId(dto.getString("modify_id"));
//            garden.setId(garden_id);
////            garden.setCreate_time(new Date());
////            garden.setIs_valid("1");
//
//            garden.setCreateTime(new Date());
//            garden.setIsValid("1");
////            count = t_gardenDao.insert(garden);
//            count = tGardenMapper.insert(garden);
//            if (count != 1) {
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//            }
//
//            saveWechatsAndAdmin(dto, garden_id);
//
//            return ApiMessageUtil.success();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw new ApiException(e.getCode(), e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
        return null;
    }

    @Override
    @Transactional
    public ApiMessage updateGardenInfo(Dto dto) throws ApiException {
//
//        try {
//            int count = 0;
//            String garden_id = dto.getString("garden_id");
////            T_gardenPO garden = new T_gardenPO();
//            TGarden garden = new TGarden();
////            garden.copyProperties(dto);
//            garden.setPropertyId(dto.getString("property_id"));
//            garden.setZjParkId(dto.getString("zj_park_id"));
//            garden.setGardenName(dto.getString("garden_name"));
//            garden.setTelephone(dto.getString("telephone"));
//            garden.setPhoneNum(dto.getString("phone_num"));
//            garden.setBankName(dto.getString("bank_name"));
//            garden.setBankNum(dto.getString("bank_num"));
//            garden.setCollectionUnit(dto.getString("collection_unit"));
//            garden.setProvince(dto.getString("province"));
//            garden.setCity(dto.getString("city"));
//            garden.setArea(dto.getString("area"));
//            garden.setStreet(dto.getString("street"));
//            garden.setModifyId(dto.getString("modify_id"));
//            garden.setId(garden_id);
////            garden.setModify_time(new Date());
//            garden.setModifyTime(new Date());
////            count = t_gardenDao.updateByKey(garden);
//            tGardenMapper.updateByPrimaryKey(garden);
//            if (count != 1) {
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_gardenDao");
//            }
//            //更新redis
////            ru.setObject("garden_" + garden_id, t_gardenDao.selectByKey(garden_id));
//            ru.setObject("garden_" + garden_id, tGardenMapper.selectByPrimaryKey(garden_id));
//
//
//            //1 删t_garden_role_modules
//            //2 删t_garden_user_role
//            //3 删t_garden_user
//
//            //原管理员id,role_id
//            String del_user_id = "";
//            String del_role_id = "";
//            Dto dto5 = new HashDto();
//            dto5.put("garden_id", garden_id);
//            dto5.put("role_name", "园区管理员");
//            Dto managerDto = roleService.getUserRoleId(dto5);
//            if (managerDto != null) {
//                del_user_id = managerDto.getString("user_id");
//                del_role_id = managerDto.getString("role_id");
//            }
//
//            //1 删t_garden_role_modules
//            if (!SystemUtils.isEmpty(del_user_id) && !SystemUtils.isEmpty(del_role_id)) {
//                Dto dto4 = new HashDto();
//                dto4.put("garden_id", garden_id);
//                dto4.put("role_id", del_role_id);
////                tRoleDaoExt.deleteGardenRoleModules(dto4);
//
//                TGardenRoleModulesExample tGardenRoleModulesExample = new TGardenRoleModulesExample();
//                tGardenRoleModulesExample.or().andGardenIdEqualTo(garden_id).andRoleIdEqualTo(del_role_id);
//                tGardenRoleModulesMapper.deleteByExample(tGardenRoleModulesExample);
//
//                //2 删t_garden_user_role
//                Dto dto2 = new HashDto();
//                dto2.put("garden_id", garden_id);
//                dto2.put("user_id", del_user_id);
//                dto2.put("role_id", del_role_id);
//                dto2.put("is_valid", "1");
////                T_garden_user_rolePO garden_user_role = t_garden_user_roleDao.selectOne(dto2);
//                TGardenUserRoleExample tGardenUserRoleExample = new TGardenUserRoleExample();
//                tGardenUserRoleExample.or().andGardenIdEqualTo(garden_id).andUserIdEqualTo(del_user_id).andRoleIdEqualTo(del_role_id)
//                        .andIsValidEqualTo("1");
//                List<TGardenUserRole> garden_user_role_list = tGardenUserRoleMapper.selectByExample(tGardenUserRoleExample);
//                if (garden_user_role_list != null || !garden_user_role_list.isEmpty()) {
//                    TGardenUserRole garden_user_role = garden_user_role_list.get(0);
////                    t_garden_user_roleDao.deleteByKey(garden_user_role.getId());
//                    tGardenUserRoleMapper.deleteByPrimaryKey(garden_user_role.getId());
//                }
//
//                //3 删t_garden_user
//                Dto dto1 = new HashDto();
//                dto1.put("garden_id", garden_id);
//                dto1.put("user_id", del_user_id);
//                dto1.put("is_valid", "1");
////                T_garden_userPO garden_user = t_garden_userDao.selectOne(dto1);
//                TGardenUserExample tGardenUserExample = new TGardenUserExample();
//                tGardenUserExample.or().andGardenIdEqualTo(garden_id).andUserIdEqualTo(del_user_id);
//                List<TGardenUser> garden_user_list =  tGardenUserMapper.selectByExample(tGardenUserExample);
//                if (garden_user_list != null || !garden_user_list.isEmpty()) {
//                    TGardenUser garden_user = garden_user_list.get(0);
////                    t_garden_userDao.deleteByKey(garden_user.getId());
//                    tGardenUserMapper.deleteByPrimaryKey(garden_user.getId());
//                }
//            }
//
//            Dto dto3 = new HashDto();
//            dto3.put("garden_id", garden_id);
//            dto3.put("is_valid", "1");
////            List<T_garden_weixin_public_configPO> wechats = t_garden_weixin_public_configDao.list(dto3);
//            TGardenWeixinPublicConfigExample tGardenWeixinPublicConfigExample = new TGardenWeixinPublicConfigExample();
//            tGardenWeixinPublicConfigExample.or().andGardenIdEqualTo(garden_id).andIsValidEqualTo("1");
//            List<TGardenWeixinPublicConfig> wechats = tGardenWeixinPublicConfigMapper.selectByExample(tGardenWeixinPublicConfigExample);
//            if (!SystemUtils.isEmpty(wechats)) {
//                for (TGardenWeixinPublicConfig wechat : wechats) {
////                    count = t_garden_weixin_public_configDao.deleteByKey(wechat.getId());
//                    count = tGardenWeixinPublicConfigMapper.deleteByPrimaryKey(wechat.getId());
//                    if (count != 1) {
//                        throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "wechat");
//                    }
//                }
//            }
//
//            saveWechatsAndAdmin(dto, garden_id);
//
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }
//
//    private void saveWechatsAndAdmin(Dto dto, String garden_id) {
//        String user_id = dto.getString("user_id");
//        String wechatIds = dto.getString("wechat_ids");
////        T_garden_userPO garden_user;
//        TGardenUser garden_user;
////        T_garden_user_rolePO garden_user_role;
//        TGardenUserRole garden_user_role;
//        if (!SystemUtils.isEmpty(user_id)) {
//            //创建用户角色权限流程
//            //1 t_user 创建用户
//            //2 t_garden_user 关联园区用户
//            //3 t_role 加角色名，默认存在的不用加
//            //4 t_garden_role_modules 给角色加菜单
//            //5 t_garden_user_role 关联用户与角色
//
//            String property_id = dto.getString("property_id");
//            String role_id = "";
//
//            Dto dto6 = new HashDto();
//            dto6.put("property_id", property_id);
//            dto6.put("user_id", user_id);
//            dto6.put("is_valid", "1");
////            T_property_user_rolePO property_user_role = t_property_user_roleDao.selectOne(dto6);
//            TPropertyUserRoleExample tPropertyUserRoleExample = new TPropertyUserRoleExample();
//            tPropertyUserRoleExample.or().andPropertyIdEqualTo(property_id).andUserIdEqualTo(user_id).andIsValidEqualTo("1");
//            List<TPropertyUserRole> property_user_role_list = tPropertyUserRoleMapper.selectByExample(tPropertyUserRoleExample);
//            if (property_user_role_list != null || !property_user_role_list.isEmpty()) {
//                TPropertyUserRole property_user_role = property_user_role_list.get(0);
////                role_id = property_user_role.getRole_id();
//                role_id = property_user_role.getRoleId();
//            }
//
////            T_userPO user = t_userDao.selectByKey(user_id);
//            TUser user = tUserMapper.selectByPrimaryKey(user_id);
//            //2 t_garden_user 关联物业用户
////            garden_user = new T_garden_userPO();
//            garden_user = new TGardenUser();
//            garden_user.setId(PrimaryGenerater.getInstance().uuid());
////            garden_user.setGarden_id(garden_id);
////            garden_user.setUser_id(dto.getString("user_id"));
////            garden_user.setHire_date(new Date());
////            garden_user.setCreate_time(new Date());
//            garden_user.setEmail(user.getEmail());
//            garden_user.setStatus("1"); //在职
////            garden_user.setIs_valid("1");
//
//            garden_user.setGardenId(garden_id);
//            garden_user.setUserId(dto.getString("user_id"));
//            garden_user.setHireDate(new Date());
//            garden_user.setCreateTime(new Date());
//            garden_user.setIsValid("1");
//
//            //4 t_garden_role_modules 给角色加菜单
//            Dto dto1 = new HashDto();
//            dto1.put("property_id", property_id);
//            dto1.put("role_id", role_id);
//            dto1.put("is_valid", "1");
////            List<T_property_role_modulesPO> property_role_modules = t_property_role_modulesDao.like(dto1);
//            TPropertyRoleModulesExample tPropertyRoleModulesExample = new TPropertyRoleModulesExample();
//            tPropertyRoleModulesExample.or().andPropertyIdLike(property_id).andRoleIdLike(role_id).andIsValidEqualTo("1");
//            List<TPropertyRoleModules> property_role_modules = tPropertyRoleModulesMapper.selectByExample(tPropertyRoleModulesExample);
//            String modules_ids = "";
//            if (property_role_modules != null && property_role_modules.size() > 0) {
//                for (int i = 0; i < property_role_modules.size(); i++) {
////                    modules_ids += property_role_modules.get(i).getModules_id() + ",";
//                    modules_ids += property_role_modules.get(i).getModulesId() + ",";
//
//                }
//            }
//            Dto dto2 = new HashDto();
//            dto2.put("garden_id", garden_id);
//            dto2.put("role_id", role_id);
//            dto2.put("modules_ids", modules_ids);
//            roleService.setGardenRoleModules(dto2);
//            //给园区加财务角色
//            Dto dto3 = new HashDto();
//            dto3.put("garden_id", garden_id);
//            dto3.put("role_id", "55555555"); //财务角色id
//            dto3.put("modules_ids", "16");
//            roleService.setGardenRoleModules(dto3);
//
//            //5 t_garden_user_role 关联用户与角色
////            garden_user_role = new T_garden_user_rolePO();
//            garden_user_role = new TGardenUserRole();
//            garden_user_role.setId(PrimaryGenerater.getInstance().uuid());
////            garden_user_role.setGarden_id(garden_id);
////            garden_user_role.setRole_id(role_id);
////            garden_user_role.setUser_id(user_id);
////            garden_user_role.setCreate_time(new Date());
////            garden_user_role.setIs_valid("1");
//
//            garden_user_role.setGardenId(garden_id);
//            garden_user_role.setRoleId(role_id);
//            garden_user_role.setUserId(user_id);
//            garden_user_role.setCreateTime(new Date());
//            garden_user_role.setIsValid("1");
//
////            t_garden_userDao.insert(garden_user);
//            tGardenUserMapper.insert(garden_user);
////            t_garden_user_roleDao.insert(garden_user_role);
//            tGardenUserRoleMapper.insert(garden_user_role);
//        }
//
//        List<String> wechat_ids = Arrays.asList(wechatIds.split(","));
//        for (String wechat_id : wechat_ids) {
////            T_garden_weixin_public_configPO garden_weixin = new T_garden_weixin_public_configPO();
//            TGardenWeixinPublicConfig garden_weixin = new TGardenWeixinPublicConfig();
//            garden_weixin.setId(PrimaryGenerater.getInstance().uuid());
////            garden_weixin.setGarden_id(garden_id);
////            garden_weixin.setWeixin_config_id(wechat_id);
////            garden_weixin.setCreate_time(new Date());
////            garden_weixin.setIs_valid("1");
//            garden_weixin.setGardenId(garden_id);
//            garden_weixin.setWeixinConfigId(wechat_id);
//            garden_weixin.setCreateTime(new Date());
//            garden_weixin.setIsValid("1");
////            t_garden_weixin_public_configDao.insert(garden_weixin);
//            tGardenWeixinPublicConfigMapper.insert(garden_weixin);
//        }
        return null;
    }

    /**
     * 园区端--删除园区
     *
     * @param dto garden_id String 必传
     *            modify_id String 必传 当前登录用户id
     */
    @Override
    public ApiMessage delGarden(Dto dto) throws ApiException {
        try {
//            T_gardenPO garden = new T_gardenPO();
            TGarden garden = new TGarden();
//            garden.copyProperties(dto);
            garden.setPropertyId(dto.getString("property_id"));
            garden.setZjParkId(dto.getString("zj_park_id"));
            garden.setGardenName(dto.getString("garden_name"));
            garden.setTelephone(dto.getString("telephone"));
            garden.setPhoneNum(dto.getString("phone_num"));
            garden.setBankName(dto.getString("bank_name"));
            garden.setBankNum(dto.getString("bank_num"));
            garden.setCollectionUnit(dto.getString("collection_unit"));
            garden.setProvince(dto.getString("province"));
            garden.setCity(dto.getString("city"));
            garden.setArea(dto.getString("area"));
            garden.setStreet(dto.getString("street"));
            garden.setModifyId(dto.getString("modify_id"));
            garden.setId(dto.getString("garden_id"));
//            garden.setModify_time(new Date());
            garden.setModifyTime(new Date());
//            garden.setIs_valid("0");
            garden.setIsValid("0");
            int count;
            count = tGardenMapper.updateByPrimaryKey(garden);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
            return ApiMessageUtil.success(count);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    /**
     * 园区端--恢复园区
     *
     * @param dto garden_id String 必传
     *            modify_id String 必传 当前登录用户id
     */
    @Override
    public ApiMessage recoverGarden(Dto dto) throws ApiException {
        try {
//            T_gardenPO garden = new T_gardenPO();
            TGarden garden = new TGarden();
//            garden.copyProperties(dto);
            garden.setPropertyId(dto.getString("property_id"));
            garden.setZjParkId(dto.getString("zj_park_id"));
            garden.setGardenName(dto.getString("garden_name"));
            garden.setTelephone(dto.getString("telephone"));
            garden.setPhoneNum(dto.getString("phone_num"));
            garden.setBankName(dto.getString("bank_name"));
            garden.setBankNum(dto.getString("bank_num"));
            garden.setCollectionUnit(dto.getString("collection_unit"));
            garden.setProvince(dto.getString("province"));
            garden.setCity(dto.getString("city"));
            garden.setArea(dto.getString("area"));
            garden.setStreet(dto.getString("street"));
            garden.setModifyId(dto.getString("modify_id"));
            garden.setId(dto.getString("garden_id"));
//            garden.setModify_time(new Date());
            garden.setModifyTime(new Date());
//            garden.setIs_valid("1");
            garden.setIsValid("1");
            int count = tGardenMapper.updateByPrimaryKey(garden);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
            return ApiMessageUtil.success(count);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    @Transactional
    public ApiMessage listProperties(Dto dto) {

        PageInfo<Dto> pageInfo;
        Page.startPage(dto);
        List<Dto> list = tPropertyExt.listProperties(dto);
//        for (Dto dto1 : list) {
//            String propertyId = dto1.getString("propertyId");
//            TAddressExample addressExample = new TAddressExample();
//            addressExample.or().andOwnerIdEqualTo(propertyId)
//                    .andTypeEqualTo("2")
//                    .andIsValidEqualTo("1");
//            List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
//            if (addressList != null && !addressList.isEmpty())
//                dto1.put("address", addressList.get(0));
//        }
        pageInfo = new PageInfo<>(list);
        Dto resDto = new HashDto();
        resDto.put("list", pageInfo.getList());
        resDto.put("total", pageInfo.getTotal());
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("pageNum", pageInfo.getPageNum());
        return ApiMessageUtil.success(resDto);
    }


    @Override
    public Dto getProperty(Dto dto) {
        String propertyId = dto.getString("propertyId");

        Dto dto1 = new HashDto();
        dto1.put("propertyId", propertyId);
        dto1.put("roleName", "物业管理员");
        Dto userRoleDto = roleService.getUserRoleId(dto1);
        String roleId = userRoleDto.getString("roleId");
//        String userId = userRoleDto.getString("userId");

        //查物业基本信息
        dto.put("roleId", roleId);
        Dto propertyDto = tPropertyExt.getProperty(dto);

        //联系地址
        TAddressExample addressExample = new TAddressExample();
        addressExample.or().andOwnerIdEqualTo(propertyId)
                .andTypeEqualTo("2")
                .andIsValidEqualTo("1");
        List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
        if (addressList != null && !addressList.isEmpty())
            propertyDto.put("address", addressList.get(0));

        String modulesIds = propertyDto.getString("modulesIds");
        modulesService.listModulesByIds(propertyDto, modulesIds);
        //查微信列表
        List<TblWxopenAuthorizerAccountInfo> wechatList = wechatService.listWechats(propertyId);
        propertyDto.put("wechatList", wechatList);

        return propertyDto;
    }

    @Override
    @Transactional
    public void delProperty(Dto dto) throws ApiException {
        String property_id = dto.getString("property_id");
        String modify_id = dto.getString("modify_id");
        String is_valid = dto.getString("is_valid");
        int count;
        try {
            TProperty property = new TProperty();
            property.setId(property_id);
            property.setModifyId(modify_id);
            property.setModifyTime(new Date());
            property.setIsValid(is_valid);
            count = tPropertyMapper.updateByPrimaryKeySelective(property);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public ApiMessage getManager(Dto dto) {
//        String property_id = dto.getString("property_id");
//        String user_id = dto.getString("manager_id");
//        Dto dto1 = new HashDto();
//        dto1.put("id", user_id);
//        dto1.put("is_valid", "1");
////        T_userPO user = t_userDao.selectOne(dto1);
//        TUserExample tUserExample = new TUserExample();
//        tUserExample.or().andIdCardEqualTo(user_id).andIsValidEqualTo("1");
//        List<TUser> userList = tUserMapper.selectByExample(tUserExample);
//        Dto managerDto = new HashDto();
//        if (userList != null || !userList.isEmpty()) {
//            TUser user = userList.get(0);
//            managerDto.put("id", user.getId());
//            managerDto.put("nick_name", user.getNickName());
//            managerDto.put("phone_num", user.getPhoneNum());
//            managerDto.put("user_name", user.getUserName());
//        }
//
//        dto1.clear();
//        dto1.put("property_id", property_id);
//        dto1.put("user_id", user_id);
//        dto1.put("is_valid", "1");
////        T_property_user_rolePO property_user_role = t_property_user_roleDao.selectOne(dto1);
//        TPropertyUserRoleExample tPropertyUserRoleExample = new TPropertyUserRoleExample();
//        tPropertyUserRoleExample.or().andPropertyIdEqualTo(property_id).andUserIdEqualTo(user_id).andIsValidEqualTo("1");
//        List<TPropertyUserRole> property_user_role_list = tPropertyUserRoleMapper.selectByExample(tPropertyUserRoleExample);
//
//        String role_id = "";
//        if (property_user_role_list != null || !property_user_role_list.isEmpty()){
//            TPropertyUserRole property_user_role = property_user_role_list.get(0);
////            role_id = property_user_role.getRole_id();
//            role_id = property_user_role.getRoleId();
//        }
//        dto1.clear();
//        dto1.put("property_id", dto.getString("property_id"));
//        dto1.put("role_id", role_id);
//        dto1.put("is_valid", "1");
////        T_property_role_modulesPO property_role_modules = t_property_role_modulesDao.selectOne(dto1);
//        TPropertyRoleModulesExample tPropertyRoleModulesExample = new TPropertyRoleModulesExample();
//        tPropertyRoleModulesExample.or().andPropertyIdEqualTo(dto.getString("property_id"))
//                .andRoleIdEqualTo(role_id).andIsValidEqualTo("1");
//        List<TPropertyRoleModules> property_role_modules_list = tPropertyRoleModulesMapper.selectByExample(tPropertyRoleModulesExample);
//        if(property_role_modules_list!=null || !property_role_modules_list.isEmpty()){
//            TPropertyRoleModules property_role_modules = property_role_modules_list.get(0);
//            if (property_role_modules != null && property_role_modules.getModulesId()!=null) {
//                String[] modules_ids = property_role_modules.getModulesId().split(",");
//                dto1.clear();
//                dto1.put("ids", modules_ids);
//                List<String> modules_ids_list = new ArrayList<>();
//                for(String str : modules_ids){
//                    modules_ids_list.add(str);
//                }
//
////                List<T_modulesPO> module_list = tModulesDaoExt.listModulesByIds(dto1);
//                TModulesExample tModulesExample = new TModulesExample();
//                tModulesExample.or().andIdIn(modules_ids_list).andIsValidEqualTo("1");
//                List<TModules> module_list = tModulesMapper.selectByExample(tModulesExample);
//                managerDto.put("module_list", module_list);
//            }
//        }
//
//
//        return ApiMessageUtil.success(managerDto);
        return null;
    }

    @Override
    public ApiMessage getPayType(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        TPayTypeExample tPayTypeExample = new TPayTypeExample();
        tPayTypeExample.or().andPropertyIdLike(tokenDto.getString("property_id"));
        List<TPayType> typePOs = tPayTypeMapper.selectByExample(tPayTypeExample);
        List<String> list = new ArrayList<>();
        if (typePOs.size() > 0) {
            for (TPayType po : typePOs) {
                list.add(po.getPayType());
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("types", list);
        return ApiMessageUtil.success(map);
    }

}
