package com.dominator.serviceImpl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TCompanyBillExt;
import com.dominator.mapper.ext.TGardenExt;
import com.dominator.redis.util.JSONUtil;
import com.dominator.service.*;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dateutil.DateUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class GardenServiceImpl implements GardenService {

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TGardenExt tGardenExt;

    @Autowired
    private TCompanyBillMapper tCompanyBillMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ModulesService modulesService;

    @Autowired
    private UserService userService;

    @Autowired
    private TCompanyBillExt tCompanyBillExt;

    @Autowired
    private MsgSendCenter msgSendCenter;

    /**
     * 检查garden_name是否重复
     *
     * @param dto ------新增
     *            propertyId
     *            gardenName
     *            ------编辑
     *            propertyId
     *            gardenName
     *            gardenId
     * @return
     */
    @Override
    public int checkGardenName(Dto dto) {
        String gardenId = dto.getString("gardenId");
        String propertyId = dto.getString("propertyId");
        String gardenName = dto.getString("gardenName");

        int count = 0;
        TGardenExample gardenExample = new TGardenExample();
        TGardenExample.Criteria criteria = gardenExample.createCriteria();
        criteria.andPropertyIdEqualTo(propertyId);
        criteria.andGardenNameEqualTo(gardenName);
        criteria.andIsValidEqualTo("1");
        if (!SystemUtils.isEmpty(gardenId))
            criteria.andIdNotEqualTo(gardenId);

        count = tGardenMapper.countByExample(gardenExample);
        return count;
    }

    @Override
    public Dto listGardens(Dto dto) {
        String propertyId = dto.getString("propertyId");
        String gardenName = dto.getString("gardenName");
        String isValid = dto.getString("isValid");

        TGardenExample gardenExample = new TGardenExample();
        TGardenExample.Criteria criteria = gardenExample.createCriteria();
        criteria.andPropertyIdEqualTo(propertyId);
        if (!SystemUtils.isEmpty(gardenName))
            criteria.andGardenNameLike("%" + gardenName + "%");
        if (!SystemUtils.isEmpty(isValid))
            criteria.andIsValidEqualTo(isValid);
        gardenExample.or(criteria);
        gardenExample.setOrderByClause("is_valid desc, create_time asc");
        List<TGarden> list = tGardenMapper.selectByExample(gardenExample);
        List<Dto> gardenList = new ArrayList<>();
        for (TGarden garden : list) {
            String gardenId = garden.getId();
            Dto dto1 = JsonUtils.toDto(JsonUtils.toJson(garden));
            dto1.remove("province");
            dto1.remove("city");
            dto1.remove("area");
            dto1.remove("street");
            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(gardenId)
                    .andTypeEqualTo("2")
                    .andIsValidEqualTo("1");
            List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
            if (addressList != null && !addressList.isEmpty()) {
                dto1.put("address", addressList.get(0));
            }
            gardenList.add(dto1);
        }

        Dto resDto = new HashDto();
        resDto.put("gardenAmount", list.size());
        resDto.put("gardens", gardenList);
        return resDto;
    }

    @Override
    public ApiMessage listWithoutManagerGarden(Dto dto) {
//        String user_id = dto.getString("manager_id");
//        TGardenUserExample gardenUserExample = new TGardenUserExample();
//        gardenUserExample.or().andUserIdEqualTo(user_id)
//                .andIsValidEqualTo("1");
//        List<TGardenUser> gardenUserList = tGardenUserMapper.selectByExample(gardenUserExample);
//        List<String> ids = new ArrayList<>();
//        if (gardenUserList != null && !gardenUserList.isEmpty()) {
//            for (TGardenUser gardenUser : gardenUserList)
//                ids.add(gardenUser.getGardenId());
//        }
//        TGardenExample gardenExample = new TGardenExample();
//        gardenExample.or().andIdIn(ids).andIsValidEqualTo("1");
//        gardenExample.setOrderByClause("create_time asc");
//        List<TGarden> gardenList = tGardenMapper.selectByExample(gardenExample);
//        List<TGarden> gardenList1 = tGardenExt.listWithoutManagerGarden(dto);
//        gardenList.addAll(gardenList1);
//        Dto resDto = new HashDto();
//        resDto.put("garden_list", gardenList);
//        return ApiMessageUtil.success(resDto);
        return null;
    }

    @Override
    public Dto getGarden(Dto dto) {
        String gardenId = dto.getString("gardenId");

        Dto dto1 = new HashDto();
        dto1.put("gardenId", gardenId);
        dto1.put("roleName", "园区管理员");
        Dto userRoleDto = roleService.getUserRoleId(dto1);
        String roleId = userRoleDto.getString("roleId");
        String userId = userRoleDto.getString("userId");
        dto.put("userId", userId);

        //查园区基本信息
        dto.put("roleId", roleId);
        Dto gardenDto = tGardenExt.getGarden(dto);
        gardenDto.remove("province");
        gardenDto.remove("city");
        gardenDto.remove("area");
        gardenDto.remove("street");

        //联系地址
        TAddressExample addressExample = new TAddressExample();
        addressExample.or().andOwnerIdEqualTo(gardenId)
                .andTypeEqualTo("2")
                .andIsValidEqualTo("1");
        List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
        if (addressList != null && !addressList.isEmpty())
            gardenDto.put("address", addressList.get(0));

        String modulesIds = gardenDto.getString("modulesIds");
        modulesService.listModulesByIds(gardenDto, modulesIds);

        Date date = loginService.getLastLoginTime(dto);
        gardenDto.put("lastLoginTime", date);

        return gardenDto;
    }

    @Override
    @Transactional
    public ApiMessage postGarden(Dto dto) throws ApiException {
        String property_id = dto.getString("property_id");
        String garden_id = PrimaryGenerater.getInstance().uuid();
        String role_id = PrimaryGenerater.getInstance().uuid();
        String user_id;
        String garden_name = dto.getString("garden_name");
        String user_name = dto.getString("user_name");
        String nick_name = dto.getString("nick_name");
        String province = dto.getString("province");
        String city = dto.getString("city");
        String area = dto.getString("area");
        String street = dto.getString("street");
        String telephone = dto.getString("telephone");
        String collection_unit = dto.getString("collection_unit");
        String bank_name = dto.getString("bank_name");
        String bank_num = dto.getString("bank_num");
        String zj_park_id = dto.getString("zj_park_id");
        String phoneNum = user_name;
        int count;
        try {
            Dto dto1 = new HashDto();
            dto1.put("gardenName", garden_name);
            dto1.put("propertyId", property_id);
            count = checkGardenName(dto1);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_GARDEN);

            count = userService.checkUserName(user_name);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);

            TGarden garden = new TGarden();
            garden.setId(garden_id);
            garden.setPropertyId(property_id);
            garden.setZjParkId(zj_park_id);
            garden.setGardenName(garden_name);
            garden.setPhoneNum(phoneNum);
            garden.setTelephone(telephone);
            garden.setBankName(bank_name);
            garden.setBankNum(bank_num);
            garden.setCollectionUnit(collection_unit);
            garden.setCreateTime(new Date());
            garden.setIsValid("1");
            count = tGardenMapper.insertSelective(garden);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden");

            //加园区联系地址
            TAddress address = new TAddress();
            address.setId(PrimaryGenerater.getInstance().uuid());
            address.setOwnerId(garden_id);
            address.setType("2");
            address.setProvince(province);
            address.setCity(city);
            address.setArea(area);
            address.setStreet(street);
            address.setCreateTime(new Date());
            address.setIsValid("1");
            count = tAddressMapper.insertSelective(address);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden_address");

            //创建园区管理员角色
            int sort = roleService.getRoleSort("4");
            TRole role = new TRole();
            role.setId(role_id);
            role.setUnitId(garden_id);
            role.setRoleName("园区管理员");
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
                //关联园区和管理员
                TGardenUserRole gardenUserRole = new TGardenUserRole();
                gardenUserRole.setId(PrimaryGenerater.getInstance().uuid());
                gardenUserRole.setGardenId(garden_id);
                gardenUserRole.setUserId(user_id);
                gardenUserRole.setNickName(nick_name);
                gardenUserRole.setRoleId(role_id);
                gardenUserRole.setCreateTime(new Date());
                gardenUserRole.setIsValid("1");
                count = tGardenUserRoleMapper.insertSelective(gardenUserRole);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "gardenUserRole");
            }

            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public ApiMessage putGarden(Dto dto) throws ApiException {
        String user_id;
        String role_id;
        String property_id = dto.getString("property_id");
        String garden_id = dto.getString("garden_id");
        String garden_name = dto.getString("province");
        String user_name = dto.getString("user_name");
        String province = dto.getString("province");
        String city = dto.getString("city");
        String area = dto.getString("area");
        String street = dto.getString("street");
        String telephone = dto.getString("telephone");
        String collection_unit = dto.getString("collection_unit");
        String bank_name = dto.getString("bank_name");
        String bank_num = dto.getString("bank_num");
        String zj_park_id = dto.getString("zj_park_id");
        String nick_name = dto.getString("nick_name");
        String modify_id = dto.getString("modify_id");
        String phoneNum = user_name;
        int count;
        try {
            Dto dto1 = new HashDto();
            dto1.put("gardenName", garden_name);
            dto1.put("gardenId", garden_id);
            dto1.put("propertyId", property_id);
            count = checkGardenName(dto1);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_GARDEN);

            count = userService.checkUserName(user_name);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);

            TGarden garden = new TGarden();
            garden.setId(garden_id);
            garden.setPropertyId(property_id);
            garden.setZjParkId(zj_park_id);
            garden.setGardenName(garden_name);
            garden.setPhoneNum(phoneNum);
            garden.setTelephone(telephone);
            garden.setBankName(bank_name);
            garden.setBankNum(bank_num);
            garden.setCollectionUnit(collection_unit);
            garden.setModifyId(modify_id);
            garden.setModifyTime(new Date());
            count = tGardenMapper.updateByPrimaryKeySelective(garden);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden");

            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(garden_id)
                    .andTypeEqualTo("2")
                    .andIsValidEqualTo("1");
            TAddress address = new TAddress();
            address.setOwnerId(garden_id);
            address.setType("2");
            address.setProvince(province);
            address.setCity(city);
            address.setArea(area);
            address.setStreet(street);
            count = tAddressMapper.updateByExampleSelective(address, addressExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden_address");

            //查该物业管理员role_id
            dto1.clear();
            dto1.put("gardenId", garden_id);
            dto1.put("roleName", "园区管理员");
            Dto userRoleDto = roleService.getUserRoleId(dto1);
            role_id = userRoleDto.getString("roleId");
            user_id = userRoleDto.getString("userId");

            //更新园区和管理员的关联
            TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
            gardenUserRoleExample.or().andGardenIdEqualTo(garden_id)
                    .andUserIdEqualTo(user_id)
                    .andRoleIdEqualTo(role_id)
                    .andIsValidEqualTo("1");
            TGardenUserRole gardenUserRole = new TGardenUserRole();
            gardenUserRole.setUserId(user_id);
            gardenUserRole.setNickName(nick_name);
            gardenUserRole.setModifyTime(new Date());
            gardenUserRole.setModifyId(modify_id);
            count = tGardenUserRoleMapper.updateByExampleSelective(gardenUserRole, gardenUserRoleExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "gardenUserRole");
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public ApiMessage delGarden(Dto dto) throws ApiException {
        String garden_id = dto.getString("garden_id");
        String modify_id = dto.getString("modify_id");
        String is_valid = dto.getString("is_valid");
        int count;
        try {
            TGarden garden = new TGarden();
            garden.setId(garden_id);
            garden.setModifyTime(new Date());
            garden.setModifyId(modify_id);
            garden.setIsValid(is_valid);
            count = tGardenMapper.updateByPrimaryKeySelective(garden);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "garden");

            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public Dto listByUser(Dto dto) {
        String propertyId = dto.getString("propertyId");
        String userId = dto.getString("userId");

        Dto dto1 = new HashDto();
        dto1.put("propertyId", propertyId);
        dto1.put("roleName", "物业管理员");
        Dto userRoleDto = roleService.getUserRoleId(dto1);
        if (userRoleDto.getString("userId").equals(userId)) {
            dto1.clear();
            dto1.put("propertyId", propertyId);
            dto1.put("isValid", "1");
            return listGardens(dto1);
        } else {
            List<Dto> gardens = tGardenExt.listByUser(dto);
            Dto resDto = new HashDto();
            resDto.put("gardens", gardens);
            return resDto;
        }
    }

    @Override
    public ApiMessage listGardenRoleModules(Dto dto) {
        List<Dto> list = tGardenExt.listGardenRole(dto);
        if (list != null && list.size() > 0) {
            List<TModules> module_list;
            for (Dto dto1 : list) {
                Dto dto2 = new HashDto();
                dto2.put("garden_id", dto.getString("garden_id"));
                dto2.put("role_id", dto1.getString("id"));
                module_list = tGardenExt.listRoleModules(dto2);
                if (module_list != null && module_list.size() > 0) {
                    dto1.put("module_list", module_list);
                }
            }
        }
        return ApiMessageUtil.success(list);
    }

    @Override
    public List<TUser> listGardenUserRole(Dto dto) {
        String garden_id = dto.getString("garden_id");
        String roleId = dto.getString("roleId");
        String addType = dto.getString("addType");
        TGardenUserRoleExample tGardenUserRoleExample = new TGardenUserRoleExample();
        TGardenUserRoleExample.Criteria criteria = tGardenUserRoleExample.createCriteria();
        switch (addType) {
            //未添加
            case "0":
                criteria.andGardenIdEqualTo(garden_id).andRoleIdEqualTo(roleId).
                        andIsValidEqualTo("1").andStatusEqualTo("1");
                break;
            //已添加
            case "1":
                criteria.andGardenIdEqualTo(garden_id).andRoleIdNotEqualTo(roleId).
                        andIsValidEqualTo("1").andStatusEqualTo("1");
                break;
        }
        List<TGardenUserRole> list = tGardenUserRoleMapper.selectByExample(tGardenUserRoleExample);
        tGardenUserRoleExample.clear();
        List<String> idList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (TGardenUserRole tGardenUserRole : list) {
                String user_id = tGardenUserRole.getUserId();
                if (StringUtils.isNotEmpty(user_id))
                    idList.add(user_id);
            }
        }
        List<TUser> userList = new ArrayList<>();
        if (idList.size() > 0) {
            TUserExample tUserExample = new TUserExample();
            TUserExample.Criteria criteria1 = tUserExample.createCriteria();
            criteria1.andIsValidEqualTo("1").andIdIn(idList);
            userList = tUserMapper.selectByExample(tUserExample);
        }
        return userList;
    }

    /**
     * 园区端--物业缴费-账单列表
     *
     * @param dto gardenId String 必传 园区id
     *            feeType String 必传  费用类型 0水 1电费 2煤气 3房租 4物业
     *            isPaid String 必传 0待缴费 1已缴费
     *            isPushed String 必传 0待推送，1已推送
     *            startTime datetime 非必传
     *            endTime datetime 非必传
     *            condition String 非必传 账单号/缴费单位
     *            pageNum int 非必传 第几页
     *            pageSize int 非必传 每页几条
     */
    @Override
    public ApiMessage listBills(Dto dto) {

        Page.startPage(dto);
        //PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
        /*TGardenExample tGardenExample = new TGardenExample();
        TGardenExample.Criteria criteria = tGardenExample.createCriteria();
        criteria.*/
        List<Dto> bills = tGardenExt.listBillsPage(dto);

        PageInfo<Dto> pageInfo;
        pageInfo = new PageInfo<>(bills);
        Dto resDto = new HashDto();
        resDto.put("bills", bills);
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("pageNum", pageInfo.getPageNum());
        resDto.put("total", pageInfo.getTotal());
        return ApiMessageUtil.success(resDto);
    }

//    /**
//     * 园区端--获取该园区企业列表
//     *
//     * @param dto garden_id String 必传 园区id
//     *            pageNum int 非必传 当前页，默认1
//     *            pageSize int 非必传 每页条数，默认10
//     *            condition String 非必传 查询条件
//     * @return
//     */
//    @Override
//    public ApiMessage listCompanies(Dto dto) {
//        dto.put("is_paid", "0");
//        dto.put("condition", dto.getString("condition"));
//        dto.put("garden_id", dto.getString("garden_id"));
//        PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
//        List<Dto> list = tCompanyDaoExt.listPage(dto);
//        return ApiMessageUtil.success(list);
//    }
//
//
//    /**
//     * 园区端--获取人事概览信息
//     *
//     * @param dto garden_id String 必传 企业id
//     * @return
//     */
//    @Override
//    public ApiMessage getUserOverview(Dto dto) {
//        //离职人数
//        dto.put("status", "0");
//        dto.put("super_admin_id", propertiesLoader.getProperty("super_admin_id"));
//        dto.put("admin_id", propertiesLoader.getProperty("admin_id"));
//        int userAmount0 = tGardenDaoExt.countGardenUser(dto);
//        //在职人数
//        dto.put("status", "1");
//        int userAmount1 = tGardenDaoExt.countGardenUser(dto);
//        //总人数
//        int totalUserAmount = userAmount0 + userAmount1;
//        Dto resDto = new HashDto();
//        resDto.put("userAmount0", userAmount0);
//        resDto.put("userAmount1", userAmount1);
//        resDto.put("totalUserAmount", totalUserAmount);
//        return ApiMessageUtil.success(resDto);
//    }
//
//    /**
//     * 园区端--园区概览--缴费概览
//     *
//     * @param dto garden_id String 必传 园区id
//     *            startTime String 非必传 开始时间 默认最近七天
//     *            endTime String 非必传 结束时间
//     *            is_paid String 非必传 0待缴费 ，1已缴费 默认待缴费
//     * @return
//     */
//    @Override
//    public ApiMessage getGardenPayOverview(Dto dto) {
//        List<Dto> payOverview = payOverview(dto);
//        Dto resDto = new HashDto();
//        resDto.put("payOverview", payOverview);
//        return ApiMessageUtil.success(resDto);
//    }
//
//    /**
//     * 物业管理-缴费概览
//     *
//     * @param dto garden_id String 必传 园区id
//     *            bill_year_month String 非必传 年月，例： 2017年10月
//     *            startTime String 非必传 开始时间 默认7天前开始
//     *            endTime String 非必传 结束时间
//     *            is_paid String 非必传 0待缴费，1已缴费 默认待缴费
//     * @return
//     */
//    @Override
//    public ApiMessage getPayOverview(Dto dto) throws ParseException {
//
//        //按种类统计，overviewByType
//        //获取年月
//        String bill_year_month = "";
//        Date date = null;
//        if (bill_year_month == null || "".equals(bill_year_month)) {
//            date = new Date();
//            date = DateUtil.minusMonth(date, 1);
//        } else {
//            date = DateUtil.StringToDateFormat(bill_year_month, "yyyy-MM");
//            date = DateUtil.minusMonth(date, 1);
//        }
//        bill_year_month = DateUtil.DatetoStringFormat(date, "yyyy-MM");
//        log.info("bill_year_month:{}", bill_year_month);
//        List<Dto> overviewByType = new ArrayList<>();
//        BigDecimal totalUnpaidFee = new BigDecimal(0);
//        for (int i = 0; i < 4; i++) {
//            Dto dto1 = new HashDto();
//            dto1.put("garden_id", dto.getString("garden_id"));
//            dto1.put("bill_year_month", bill_year_month);
//            dto1.put("fee_type", i);
//            dto1.put("is_paid", "0");
//            BigDecimal unpaid = tGardenDaoExt.overviewByType(dto1);
//            totalUnpaidFee = totalUnpaidFee.add(unpaid);
//            dto1.put("is_paid", "1");
//            BigDecimal paid = tGardenDaoExt.overviewByType(dto1);
//
//            Dto dto2 = new HashDto();
//            dto2.put("unpaid", unpaid);
//            dto2.put("paid", paid);
//            String type = "";
//            if (i == 0) {
//                type = "水费";
//            }
//            if (i == 1) {
//                type = "电费";
//            }
//            if (i == 2) {
//                type = "燃气费";
//            }
//            if (i == 3) {
//                type = "房租物业";
//            }
//            dto2.put("type", type);
//
//            overviewByType.add(dto2);
//        }
//
//
//        Dto resDto = new HashDto();
//        resDto.put("overviewByType", overviewByType);
//        resDto.put("totalUnpaidFee", totalUnpaidFee);
//        return ApiMessageUtil.success(resDto);
//    }
//
//    /**
//     * 园区端--物业缴费-账单列表
//     *
//     * @param dto garden_id String 必传 园区id
//     *            fee_type String 必传  费用类型 0水 1电费 2煤气 3房租 4物业
//     *            is_paid String 必传 0待缴费 1已缴费
//     *            is_pushed String 必传 0待推送，1已推送 ,2全部
//     *            startTime datetime 非必传
//     *            endTime datetime 非必传
//     *            condition String 非必传 账单号/缴费单位
//     *            pageNum int 非必传 第几页
//     *            pageSize int 非必传 每页几条
//     * @return
//     */
//    @Override
//    public ApiMessage listBills(Dto dto) {
//        if ("2".equals(dto.getString("is_pushed"))) {
//            dto.put("is_pushed", "2");
//        }
//        if ("1".equals(dto.getString("is_pushed"))) {
//            dto.put("is_pushed", "1");
//        }
//        PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
//        List<Dto> bills = tGardenDaoExt.listBillsPage(dto);
//        Dto resDto = new HashDto();
//        resDto.put("bills", bills);
//        resDto.put("pageSize", dto.getString("pageSize"));
//        resDto.put("pageNum", dto.getString("pageNum"));
//        resDto.put("total", dto.getPageTotal());
//        return ApiMessageUtil.success(resDto);
//    }
//

    /**
     * 园区端--查询账单详情
     *
     * @param dto BillId String 必传 账单id
     */
    @Override
    public ApiMessage getBillDetail(Dto dto) {

        // T_company_billPO company_bill = t_company_billDao.selectByKey(dto.getString("company_bill_id"));
        TCompanyBill company_bill = tCompanyBillMapper.selectByPrimaryKey(dto.getString("billId"));
        //T_companyPO company = t_companyDao.selectByKey(company_bill.getCompanyId());
        TCompany company = tCompanyMapper.selectByPrimaryKey(company_bill.getCompanyId());
        //T_gardenPO garden = t_gardenDao.selectByKey(company.getGarden_id());
        TGarden garden = tGardenMapper.selectByPrimaryKey(company.getGardenId());
        Dto resDto = new HashDto();
        resDto.put("companyBill", company_bill);
        resDto.put("company", company);
        resDto.put("garden", garden);
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 园区端--添加账单
     *
     * @param dto token
     *            feeType String 必传 费用类型 0水 1电费 2煤气 3房租物业
     *            companyId String 必传 企业id
     *            billYearMonth String 必传 账单年月 例：2017-02
     *            otherFee double 必传 其他费用
     *            totalFee double 必传 总费用
     *            expiryTime datetime 必传 应缴日期
     *            pushTime datetime 必传 推送时间
     *            mainFee double 非必传 主要费用
     *            feeUnit double 非必传 单位费用
     *            feeDgree double 非必传 用量
     */
    @Override
    @Transactional
    public ApiMessage addBill(Dto dto) throws ApiException {

        try {
            TCompanyBill company_bill = new TCompanyBill();
            company_bill.setFeeType(dto.getString("feeType"));
            company_bill.setCompanyId(dto.getString("companyId"));
            company_bill.setBillYearMonth(dto.getString("billYearMonth"));
            company_bill.setOtherFee(dto.getBigDecimal("otherFee"));
            company_bill.setTotalFee(dto.getBigDecimal("totalFee"));
            company_bill.setExpiryTime(dto.getDate("expiryTime"));
            company_bill.setPushTime(dto.getDate("pushTime"));
            company_bill.setMainFee(dto.getBigDecimal("mainFee"));
            company_bill.setId(PrimaryGenerater.getInstance().uuid());
            company_bill.setBillNum(PrimaryGenerater.getInstance().next());
            company_bill.setGardenId(dto.getString("gardenId"));
            company_bill.setFeeUnit(dto.getBigDecimal("feeUnit"));
            company_bill.setFeeDgree(dto.getBigDecimal("feeDgree"));
            company_bill.setIsPushed("0");
            company_bill.setIsPaid("0");
            company_bill.setCreateTime(new Date());
            company_bill.setIsValid("1");

            if (dto.getInteger("feeType") == 1 || dto.getInteger("feeType") == 2) {
                BigDecimal main_fee = dto.getBigDecimal("feeUnit").multiply(dto.getBigDecimal("feeDgree"));

                company_bill.setMainFee(main_fee);
            }

            int count;
            count = tCompanyBillMapper.insert(company_bill);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }

        return ApiMessageUtil.success();
    }

    /**
     * 园区端--删除账单
     *
     * @param dto BillId String 必传 账单id
     *            modifyId String 必传 当前登录用户id
     */
    @Override
    @Transactional
    public ApiMessage delBill(Dto dto) throws ApiException {
        try {
            TCompanyBill company_bill = new TCompanyBill();
            company_bill.setId(dto.getString("billId"));
            company_bill.setModifyTime(new Date());
            company_bill.setModifyId(dto.getString("modifyId"));
            company_bill.setIsValid("0");
            tCompanyBillMapper.updateByPrimaryKeySelective(company_bill);
            return ApiMessageUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    /**
     * 完成缴费
     *
     * @param dto billId String 必传 账单id
     */
    @Override
    public ApiMessage pay(Dto dto) throws ApiException {
        try {
            TCompanyBill company_bill = new TCompanyBill();
            company_bill.setGardenId(dto.getString("gardenId"));
            company_bill.setModifyTime(new Date());
            company_bill.setModifyId(dto.getString("modifyId"));
            company_bill.setId(dto.getString("billId"));
            company_bill.setIsPaid("1");
            company_bill.setPayTime(new Date());
            company_bill.setModifyTime(new Date());

            //发送短信和微信消息
            Dto dto2 = new HashDto();

            dto2.put("billId", dto.getString("billId"));
            Dto dto1 = tCompanyBillExt.selectPushByBillId(dto2);

            int count;
            count = tCompanyBillMapper.updateByPrimaryKeySelective(company_bill);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }

            //发送短信和微信消息
            if (dto1 != null) {
                msgSendCenter.sendYBillNotice(dto1);
                log.info("**********发送模版成功*********");
            }

            return ApiMessageUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    /**
     * 园区端--根据园区id,企业名字查询
     *
     * @param dto garden_id String 必传 园区id
     *            company_name String 非必传 企业名
     */
    @Override
    public ApiMessage listGardenCompanies(Dto dto) {
        List<Dto> companies = tGardenExt.listGardenCompanies(dto);
        return ApiMessageUtil.success(companies);
    }

}
