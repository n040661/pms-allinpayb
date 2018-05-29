package com.dominator.serviceImpl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TCompanyExt;
import com.dominator.service.*;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.dateutil.DateUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TCompanyExt tCompanyExt;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ModulesService modulesService;

    @Autowired
    private BillService billService;

    /**
     * 检查company_name是否重复
     *
     * @param dto ------新增
     *            gardenId
     *            companyName
     *            ------编辑
     *            gardenId
     *            companyName
     *            companyId
     * @return
     */
    @Override
    public int checkCompanyName(Dto dto) {
        String gardenId = dto.getString("gardenId");
        String companyName = dto.getString("companyName");
        String companyId = dto.getString("companyId");
        TCompanyExample companyExample = new TCompanyExample();
        companyExample.or().andGardenIdEqualTo(gardenId)
                .andCompanyNameEqualTo(companyName)
                .andIdNotEqualTo(companyId)
                .andIsValidEqualTo("1");
        return tCompanyMapper.countByExample(companyExample);
    }

    @Override
    public Dto getCompany(Dto dto) throws ApiException {
        String companyId = dto.getString("companyId");

        Dto dto1 = new HashDto();
        dto1.put("companyId", companyId);
        dto1.put("roleName", "企业管理员");
        Dto userRoleDto = roleService.getUserRoleId(dto1);
        String roleId = userRoleDto.getString("roleId");
        String userId = userRoleDto.getString("userId");
        dto.put("userId", userId);

        //查企业基本信息
        dto.put("roleId", roleId);
        Dto companyDto = tCompanyExt.getCompany(dto);

        //注册地址
        TAddressExample addressExample = new TAddressExample();
        addressExample.or().andOwnerIdEqualTo(companyId)
                .andTypeEqualTo("1")
                .andIsValidEqualTo("1");
        List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
        if (addressList != null && !addressList.isEmpty())
            companyDto.put("registerAddress", addressList.get(0));

        //联系地址
        addressExample = new TAddressExample();
        addressExample.or().andOwnerIdEqualTo(companyId)
                .andTypeEqualTo("2")
                .andIsValidEqualTo("1");
        addressList = tAddressMapper.selectByExample(addressExample);
        if (addressList != null && !addressList.isEmpty())
            companyDto.put("contactAddress", addressList.get(0));

        String modulesIds = companyDto.getString("modulesIds");
        modulesService.listModulesByIds(companyDto, modulesIds);

        Date date = loginService.getLastLoginTime(dto);
        companyDto.put("lastLoginTime", date);

        return companyDto;
    }

    @Override
    public ApiMessage listCompanyBill(Dto dto) {
//        String company_id = dto.getString("company_id");
//        String is_paid = dto.getString("is_paid");
//        String is_pushed = dto.getString("is_pushed");
//        List<String> userIds = new ArrayList<>();
//        int totalUserAmount = 0;
//
//        TCompanyUserExample companyUserExample = new TCompanyUserExample();
//        companyUserExample.or().andCompanyIdEqualTo(company_id)
//                .andIsValidEqualTo("1");
//        List<TCompanyUser> companyUserList = tCompanyUserMapper.selectByExample(companyUserExample);
//        if (companyUserList != null && !companyUserList.isEmpty()) {
//            for (TCompanyUser companyUser : companyUserList)
//                userIds.add(companyUser.getUserId());
//            TUserExample userExample = new TUserExample();
//            userExample.or().andIdIn(userIds).andIsValidEqualTo("1");
//            totalUserAmount = tUserMapper.countByExample(userExample);
//        }
//
//        BigDecimal unpaidFee = new BigDecimal(0);
//        BigDecimal paidFee = new BigDecimal(0);
//        VCompanyBillExample companyBillExample = new VCompanyBillExample();
//        companyBillExample.or().andCompanyIdEqualTo(company_id)
//                .andIsPaidEqualTo("0");
//        List<VCompanyBill> companyBillList = vCompanyBillMapper.selectByExample(companyBillExample);
//        if (companyBillList != null && !companyBillList.isEmpty()) {
//            VCompanyBill companyBill = companyBillList.get(0);
//            unpaidFee = companyBill.getPaidFee();
//        }
//
//        companyBillExample.clear();
//        companyBillExample.or().andCompanyIdEqualTo(company_id)
//                .andIsPaidEqualTo("1");
//        companyBillList = vCompanyBillMapper.selectByExample(companyBillExample);
//        if (companyBillList != null && !companyBillList.isEmpty()) {
//            VCompanyBill companyBill = companyBillList.get(0);
//            paidFee = companyBill.getPaidFee();
//        }
//
//        Dto dto3 = new HashDto();
//        dto3.put("company_id", company_id);
//        dto3.put("expiry_time_start", dto.getDate("expiry_time_start"));
//        dto3.put("expiry_time_end", dto.getDate("expiry_time_end"));
//        dto3.put("pay_time_start", dto.getDate("pay_time_start"));
//        dto3.put("pay_time_end", dto.getDate("pay_time_end"));
//        dto3.put("is_paid", is_paid);
//        dto3.put("is_pushed", is_pushed);
//        Page.startPage(dto);
//        List<Dto> bills = tCompanyExt.listBills(dto3);
//        PageInfo<Dto> pageInfo = new PageInfo<>(bills);
//
//        Dto resDto = new HashDto();
//        resDto.put("totalUserAmount", totalUserAmount);
//        resDto.put("unpaidFee", unpaidFee);
//        resDto.put("paidFee", paidFee);
//        resDto.put("bills", pageInfo.getList());
//        resDto.put("total", pageInfo.getTotal());
//        resDto.put("pageSize", pageInfo.getPageSize());
//        resDto.put("pageNum", pageInfo.getPageNum());
//
//        return ApiMessageUtil.success(resDto);
        return null;
    }

    @Override
    public Dto listCompany(Dto dto) {
        String gardenId = dto.getString("gardenId");
        String condition = dto.getString("condition");
        TCompanyExample companyExample = new TCompanyExample();
        TCompanyExample.Criteria criteria = companyExample.createCriteria();
        criteria.andGardenIdEqualTo(gardenId);
        criteria.andIsValidEqualTo("1");
        if (!SystemUtils.isEmpty(condition)) {
            criteria.andCompanyNameLike("%" + condition + "%");
            criteria.andContactNameLike("%" + condition + "%");
            criteria.andContactPhoneLike("%" + condition + "%");
        }
        companyExample.setOrderByClause(" create_time");
        Page.startPage(dto);
        List<TCompany> list = tCompanyMapper.selectByExample(companyExample);
        List<Dto> companyList = new ArrayList<>();
        for (TCompany company : list) {
            String companyId = company.getId();
            BigDecimal unPaidFee = new BigDecimal(0);
            Dto companyDto = JsonUtils.toDto(JsonUtils.toJson(company));
            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(companyId)
                    .andTypeEqualTo("2")
                    .andIsValidEqualTo("1");
            List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
            if (addressList != null && !addressList.isEmpty())
                companyDto.put("contactAddress", addressList.get(0));

            Dto dto1 = new HashDto();
            dto1.put("companyId", companyId);
            dto1.put("isPaid", "0");
            List<TCompanyBill> companyBillList = billService.listCompanyBill(dto1);
            if (companyBillList != null && !companyBillList.isEmpty()) {
                for (TCompanyBill companyBill : companyBillList)
                    unPaidFee = unPaidFee.add(companyBill.getTotalFee());
            }
            companyDto.put("unPaidFee", unPaidFee + "");
            companyList.add(companyDto);
        }
        PageInfo<TCompany> pageInfo = new PageInfo<>(list);
        Dto resDto = new HashDto();
        resDto.put("total", pageInfo.getTotal());
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("list", companyList);
        return resDto;
    }

    @Override
    @Transactional
    public void postCompany(Dto dto) throws ApiException {
        String user_id;
        String role_id = PrimaryGenerater.getInstance().uuid();
        String company_id = PrimaryGenerater.getInstance().uuid();
        String garden_id = dto.getString("garden_id");
        String company_name = dto.getString("company_name");
        String contact_name = dto.getString("contact_name");
        String contact_phone = dto.getString("contact_phone");
        String register_phone = dto.getString("register_phone");
        String bank_name = dto.getString("bank_name");
        String bank_num = dto.getString("bank_num");
        String credit_num = dto.getString("credit_num");
        String legal_person = dto.getString("legal_person");
        String licence_url = dto.getString("licence_url");
        String logo_url = dto.getString("logo_url");
        String status = dto.getString("status");
        String user_name = contact_phone;
        String nick_name = contact_name;

        String contact_province = dto.getString("contact_province");
        String contact_city = dto.getString("contact_city");
        String contact_area = dto.getString("contact_area");
        String contact_street = dto.getString("contact_street");
        String register_province = dto.getString("register_province");
        String register_city = dto.getString("register_city");
        String register_area = dto.getString("register_area");
        String register_street = dto.getString("register_street");
        int count = 0;
        try {
            Dto dto1 = new HashDto();
            dto1.put("garden_id", garden_id);
            dto1.put("company_name", company_name);
            count = checkCompanyName(dto1);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_COMPANY);

            count = userService.checkUserName(user_name);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);

            TCompany company = new TCompany();
            company.setId(company_id);
            company.setGardenId(garden_id);
            company.setCompanyName(company_name);
            company.setContactName(contact_name);
            company.setContactPhone(contact_phone);
            company.setRegisterPhone(register_phone);
            company.setBankName(bank_name);
            company.setBankNum(bank_num);
            company.setCreditNum(credit_num);
            company.setLegalPerson(legal_person);
            company.setLicenceUrl(licence_url);
            company.setLogoUrl(logo_url);
            company.setCreateTime(new Date());
            company.setStatus(status);
            company.setIsValid("1");
            count = tCompanyMapper.insertSelective(company);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company");

            TAddress address = new TAddress();
            address.setId(PrimaryGenerater.getInstance().uuid());
            address.setOwnerId(company_id);
            address.setType("1");
            address.setProvince(register_province);
            address.setCity(register_city);
            address.setArea(register_area);
            address.setStreet(register_street);
            address.setCreateTime(new Date());
            address.setIsValid("1");
            count = tAddressMapper.insertSelective(address);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "register_address");

            address = new TAddress();
            address.setId(PrimaryGenerater.getInstance().uuid());
            address.setOwnerId(company_id);
            address.setType("2");
            address.setProvince(contact_province);
            address.setCity(contact_city);
            address.setArea(contact_area);
            address.setStreet(contact_street);
            address.setCreateTime(new Date());
            address.setIsValid("1");
            count = tAddressMapper.insertSelective(address);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "contact_address");

            //创建企业管理员角色
            int sort = roleService.getRoleSort("4");
            TRole role = new TRole();
            role.setId(role_id);
            role.setUnitId(company_id);
            role.setRoleName("企业管理员");
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
                //关联企业和管理员
                // todo 加is_default
                TCompanyUserRole companyUserRole = new TCompanyUserRole();
                companyUserRole.setId(PrimaryGenerater.getInstance().uuid());
                companyUserRole.setCompanyId(company_id);
                companyUserRole.setUserId(user_id);
                companyUserRole.setNickName(nick_name);
                companyUserRole.setRoleId(role_id);
                companyUserRole.setCreateTime(new Date());
                companyUserRole.setIsValid("1");
                count = tCompanyUserRoleMapper.insertSelective(companyUserRole);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "companyUserRole");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void putCompany(Dto dto) throws ApiException {
        String user_id = "";
        String role_id = "";
        String garden_id = dto.getString("garden_id");
        String company_id = dto.getString("company_id");
        String modify_id = dto.getString("modify_id");
        String company_name = dto.getString("company_name");
        String contact_name = dto.getString("contact_name");
        String contact_phone = dto.getString("contact_phone");
        String register_phone = dto.getString("register_phone");
        String bank_name = dto.getString("bank_name");
        String bank_num = dto.getString("bank_num");
        String credit_num = dto.getString("credit_num");
        String legal_person = dto.getString("legal_person");
        String licence_url = dto.getString("licence_url");
        String logo_url = dto.getString("logo_url");
        String status = dto.getString("status");
        String user_name = contact_phone;
        String nick_name = contact_name;

        String contact_province = dto.getString("contact_province");
        String contact_city = dto.getString("contact_city");
        String contact_area = dto.getString("contact_area");
        String contact_street = dto.getString("contact_street");
        String register_province = dto.getString("register_province");
        String register_city = dto.getString("register_city");
        String register_area = dto.getString("register_area");
        String register_street = dto.getString("register_street");

        dto.put("nick_name", nick_name);
        int count = 0;
        try {
            Dto dto1 = new HashDto();
            dto1.put("garden_id", garden_id);
            dto1.put("company_name", company_name);
            count = checkCompanyName(dto1);
            if (count > 0)
                throw new ApiException(ReqEnums.REQ_REPEAT_COMPANY);

            count = userService.checkUserName(user_name);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);

            TCompany company = new TCompany();
            company.setId(company_id);
            company.setCompanyName(company_name);
            company.setContactName(contact_name);
            company.setContactPhone(contact_phone);
            company.setRegisterPhone(register_phone);
            company.setBankName(bank_name);
            company.setBankNum(bank_num);
            company.setCreditNum(credit_num);
            company.setLegalPerson(legal_person);
            company.setLicenceUrl(licence_url);
            company.setLogoUrl(logo_url);
            company.setModifyTime(new Date());
            company.setModifyId(modify_id);
            company.setStatus(status);
            count = tCompanyMapper.updateByPrimaryKeySelective(company);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company");

            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(company_id)
                    .andTypeEqualTo("1")
                    .andIsValidEqualTo("1");
            TAddress address = new TAddress();
            address.setProvince(register_province);
            address.setCity(register_city);
            address.setArea(register_area);
            address.setStreet(register_street);
            count = tAddressMapper.updateByExampleSelective(address, addressExample);
            if (count < 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "registerAddress");

            addressExample.clear();
            addressExample.or().andOwnerIdEqualTo(company_id)
                    .andTypeEqualTo("2")
                    .andIsValidEqualTo("1");
            address = new TAddress();
            address.setProvince(contact_province);
            address.setCity(contact_city);
            address.setArea(contact_area);
            address.setStreet(contact_street);
            count = tAddressMapper.updateByExampleSelective(address, addressExample);
            if (count < 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "contactAddress");

            //查该企业管理员role_id
            dto1.clear();
            dto1.put("companyId", company_id);
            dto1.put("roleName", "企业管理员");
            Dto userRoleDto = roleService.getUserRoleId(dto1);
            role_id = userRoleDto.getString("roleId");
            user_id = userRoleDto.getString("userId");

            TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
            companyUserRoleExample.or().andCompanyIdEqualTo(company_id)
                    .andRoleIdEqualTo(role_id)
                    .andUserIdEqualTo(user_id)
                    .andIsValidEqualTo("1");
            //查管理员信息
            TUserExample userExample = new TUserExample();
            userExample.or().andUserNameEqualTo(user_name)
                    .andIsValidEqualTo("1");
            List<TUser> userList = tUserMapper.selectByExample(userExample);
            if (userList != null && !userList.isEmpty()) {
                user_id = userList.get(0).getId();
                //更新企业和管理员的关联
                TCompanyUserRole companyUserRole = new TCompanyUserRole();
                companyUserRole.setUserId(user_id);
                companyUserRole.setNickName(nick_name);
                companyUserRole.setModifyTime(new Date());
                companyUserRole.setModifyId(modify_id);
                count = tCompanyUserRoleMapper.updateByExampleSelective(companyUserRole, companyUserRoleExample);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "companyUserRole");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    @Transactional
    public void delCompany(Dto dto) {
        String company_id = dto.getString("company_id");
        String modify_id = dto.getString("modify_id");
        String is_valid = dto.getString("is_valid");
        int count = 0;

        try {
            TCompany company = new TCompany();
            company.setId(company_id);
            company.setModifyId(modify_id);
            company.setModifyTime(new Date());
            company.setIsValid(is_valid);
            count = tCompanyMapper.updateByPrimaryKeySelective(company);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company");

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    /**
     * 园区端--企业管理--企业待缴费信息列表
     *
     * @param dto garden_id String 必传 园区id
     *            condition String 非必传 查询条件 企业名称/联系人/手机号
     *            pageNum int 非必传 第几页
     *            pageSize int 非必传 每页几条
     * @return
     */
    @Override
    public ApiMessage listCompanyUnpaidInfo(Dto dto) {
        Page.startPage(dto);
        List<Dto> list = tCompanyExt.listCompanyUnpaidInfoPage(dto);
        PageInfo<Dto> pageInfo = new PageInfo<>(list);
        Dto resDto = new HashDto();
        resDto.put("total", pageInfo.getTotal());
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("list", pageInfo.getList());
        return ApiMessageUtil.success(resDto);
    }


    //
//    /**
//     * 企业端--获取人事概览信息
//     *
//     * @param dto company_id String 必传 企业id
//     * @return
//     */
//    @Override
//    public ApiMessage getUserOverview(Dto dto) {
//        //离职人数
//        dto.put("company_admin_id", propertiesLoader.getProperty("company_admin_id"));
//        dto.put("status", "0");
//        int userAmount0 = tCompanyDaoExt.countCompanyUser(dto);
//        //在职人数
//        dto.put("status", "1");
//        int userAmount1 = tCompanyDaoExt.countCompanyUser(dto);
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
//     * 企业端--首页--近期账单
//     *
//     * @param dto company_id String 必传 企业id
//     * @return
//     */
//    @Override
//    public ApiMessage listRecentBills(Dto dto) {
//        dto.put("is_valid", "1");
//        List<T_company_billPO> list = tUserDaoExt.listBills(dto);
//        return ApiMessageUtil.success(list);
//    }
//
//    /**
//     * 企业端--用户名是否存在
//     *
//     * @param dto user_name String 必传
//     * @return
//     */
//    @Override
//    public ApiMessage checkUsername(Dto dto) {
//        String user_name = dto.getString("user_name");
//        if (SystemUtils.isEmpty(user_name)) {
//            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), ReqEnums.REQ_PARAM_ERROR.getMsg());
//        }
//
//        //判断用户名是否存在
//        Dto dto1 = new HashDto();
//        dto1.put("user_name", user_name);
//        dto1.put("is_valid", "1");
//        T_userPO t_userPO = t_userDao.selectOne(dto1);
//        if (t_userPO == null) {
//            //用户名不存在
//            throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT.getCode(), ReqEnums.REQ_USER_NOT_EXSIT.getMsg());
//        }
//        String user_id = t_userPO.getId();
//
//        //判断是否是企业用户
//        Dto dto2 = new HashDto();
//        dto2.put("user_id", t_userPO.getId());
//        dto2.put("status", "1");
//        dto2.put("is_valid", "1");
//        T_company_userPO t_company_userPO = t_company_userDao.selectOne(dto2);
//        if (t_company_userPO == null) {
//            //非企业用户
//            throw new ApiException(ReqEnums.REQ_NOT_COMPANY_USER.getCode(), ReqEnums.REQ_NOT_COMPANY_USER.getMsg());
//        }
//
//        //role_id为管理员，才能登录企业端
//        Dto dto3 = new HashDto();
//        dto3.put("company_id", t_company_userPO.getCompany_id());
//        dto3.put("role_id", propertiesLoader.getProperty("super_admin_id"));
//        dto3.put("user_id", t_company_userPO.getUser_id());
//        dto3.put("is_valid", "1");
//        T_company_user_rolePO t_company_user_rolePO = t_company_user_roleDao.selectOne(dto3);
//        if (t_company_user_rolePO == null) {
//            //非企业用户
//            throw new ApiException(ReqEnums.REQ_NOT_COMPANY_USER.getCode(), ReqEnums.REQ_NOT_COMPANY_USER.getMsg());
//        }
//
//        Dto resDto = new HashDto();
//        resDto.put("user_id", t_userPO.getId());
//        return ApiMessageUtil.success(resDto);
//    }
//
//    /**
//     * 企业端--重置密码
//     *
//     * @param dto user_name String 必传 用户名
//     *            password String 必传 新密码
//     * @return
//     */
//    @Override
//    public ApiMessage resetPassword(Dto dto) throws ApiException {
//        try {
//            String password = Des3Utils.encode(dto.getString("password"));
//            Dto dto1 = new HashDto();
//            dto1.put("user_name", dto.getString("user_name"));
//            dto1.put("is_valid", "1");
//            T_userPO user1 = t_userDao.selectOne(dto1);
//            if (user1 == null) {
//                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT.getCode(), ReqEnums.REQ_USER_NOT_EXSIT.getMsg());
//            }
//            String user_id = user1.getId();
//            T_userPO user = new T_userPO();
//            user.setId(user_id);
//            user.setPassword(password);
//            int count = 0;
//            count = t_userDao.updateByKey(user);
//            if (count != 1) {
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//            }
//
//            if (SystemUtils.isEmpty(user1.getPassword())) {
//                Dto dto2 = new HashDto();
//                dto2.put("user_id", user_id);
//                dto2.put("is_valid", "1");
//                T_company_userPO company_user = t_company_userDao.selectOne(dto2);
//                company_user.setIs_binding("1");
//                company_user.setModify_time(new Date());
//                count = t_company_userDao.updateByKey(company_user);
//                if (count != 1) {
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//                }
//            }
//
//            return ApiMessageUtil.success();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw new ApiException(e.getCode(), e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }
//
//
//    /**
//     * 企业端--根据条件查询企业员工列表
//     *
//     * @param dto company_id String 必传 企业id
//     *            status String 必传 在职状态：0离职，1在职， 2全部
//     *            condition String 非必传 查询条件 员工姓名/工号/手机号
//     *            pageNum int 非必传 第几页 默认1
//     *            pageSize int 非必传 每页几条 默认10
//     * @return
//     */
//    @Override
//    public ApiMessage listCompanyUsersPage(Dto dto) {
//        PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
//        List<Dto> list = tCompanyDaoExt.listCompanyUsersPage(dto);
//        Dto resDto = new HashDto();
//        resDto.put("total", dto.getPageTotal());
//        resDto.put("pageSize", dto.getString("pageSize"));
//        resDto.put("pageNum", dto.getString("pageNum"));
//        resDto.put("list", list);
//        return ApiMessageUtil.success(resDto);
//    }
//
//    /**
//     * 企业端--获取企业员工信息
//     *
//     * @param dto company_user_id String 企业员工关系表id
//     * @return
//     */
//    @Override
//    public ApiMessage getCompanyUser(Dto dto) {
//        T_company_userPO company_user = t_company_userDao.selectByKey(dto.getString("company_user_id"));
//        T_userPO user = new T_userPO();
//        if (company_user != null) {
//            user = t_userDao.selectByKey(company_user.getUser_id());
//        }
//        Dto resDto = new HashDto();
//        resDto.put("company_user", company_user);
//        resDto.put("user", user);
//        return ApiMessageUtil.success(resDto);
//    }
//

    @Override
    public List<TUser> listCompanyUserRole(Dto dto) throws ApiException {
        String roleId = dto.getString("roleId");
        String company_id = dto.getString("company_id");
        String addType = dto.getString("addType");
        List<String> idList = new ArrayList<String>();
        TCompanyUserRoleExample tCompanyUserRoleExample = new TCompanyUserRoleExample();
        TCompanyUserRoleExample.Criteria criteria = tCompanyUserRoleExample.createCriteria();
        switch (addType) {
            //未添加
            case "0":
                criteria.andRoleIdEqualTo(roleId).andCompanyIdEqualTo(company_id)
                        .andIsValidEqualTo("1").andStatusEqualTo("1");
                break;
            //已添加
            case "1":
                criteria.andRoleIdNotEqualTo(roleId).andCompanyIdEqualTo(company_id).
                        andIsValidEqualTo("1").andStatusEqualTo("1");
                break;
        }
        List<TCompanyUserRole> list = tCompanyUserRoleMapper.selectByExample(tCompanyUserRoleExample);
        tCompanyUserRoleExample.clear();
        if (list != null && list.size() > 0) {
            for (TCompanyUserRole tCompanyUserRole : list) {
                String user_id = tCompanyUserRole.getUserId();
                if (StringUtils.isNotEmpty(user_id))
                    idList.add(user_id);
            }
        }
        List<TUser> userList = new ArrayList<>();
        if (idList != null && idList.size() > 0) {
            TUserExample tUserExample = new TUserExample();
            TUserExample.Criteria criteria1 = tUserExample.createCriteria();
            criteria1.andIdIn(idList).andIsValidEqualTo("1");
            userList = tUserMapper.selectByExample(tUserExample);
        }

        return userList;
    }

//    @Override
//    @Transactional
//    public ApiMessage updateCompanyUser(Dto dto) throws ApiException {
//        String user_id = dto.getString("user_id");
//        String company_id = dto.getString("company_id");
//        String role_id = dto.getString("role_id");
//        String company_user_id = "";
//        String hire_date = dto.getString("hire_date")
//                .replace("年", "-")
//                .replace("月", "-")
//                .replace("日", "")
//                .replace(".", "-")
//                .replace("/", "-");
//        int count = 0;
//        try {
//
//            userService.updateUser(dto);
//
//            Dto dto1 = new HashDto();
//            dto1.put("company_id", company_id);
//            dto1.put("user_id", user_id);
//            dto1.put("is_valid", "1");
//            T_company_userPO company_user = t_company_userDao.selectOne(dto1);
//            if (company_user != null)
//                company_user_id = company_user.getId();
//            company_user.copyProperties(dto);
//            company_user.setId(company_user_id);
//            company_user.setHire_date(DateUtil.StringToDateFormat(hire_date, "yyyy-MM-dd"));
//            company_user.setModify_time(new Date());
//            count = t_company_userDao.updateByKey(company_user);
//            if (count != 1)
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company_user");
//
//            dto1.clear();
//            dto1.put("company_id", company_id);
//            dto1.put("user_id", user_id);
//            //dto1.put("role_id", role_id);
//            dto1.put("is_valid", "1");
//            T_company_user_rolePO company_user_role = t_company_user_roleDao.selectOne(dto1);
//            if (company_user_role != null) {
//                // company_user_role_id = company_user_role.getId();
//                company_user_role.copyProperties(dto);
//                company_user_role.setId(company_user_role.getId());
//                company_user_role.setRole_id(role_id);
//                company_user_role.setModify_time(new Date());
//                count = t_company_user_roleDao.updateByKey(company_user_role);
//                if (count != 1)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "update company_user_role");
//            } else {
//                company_user_role = new T_company_user_rolePO();
//                company_user_role.setId(PrimaryGenerater.getInstance().uuid());
//                company_user_role.setCompany_id(company_id);
//                company_user_role.setRole_id(role_id);
//                company_user_role.setUser_id(user_id);
//                company_user_role.setCreate_time(new Date());
//                company_user_role.setIs_valid("1");
//                count = t_company_user_roleDao.insert(company_user_role);
//                if (count != 1)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "insert company_user_role");
//            }
//            return ApiMessageUtil.success();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
////        int countMsg = 0;
////        try {
////            T_userPO user = new T_userPO();
////            user.copyProperties(dto);
////            user.setId(dto.getString("user_id"));
////            user.setUser_name(dto.getString("phone_num"));
////            user.setModify_time(new Date());
////            countMsg = t_userDao.updateByKey(user);
////            if (countMsg != 1) {
////                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "user");
////            }
////
////            T_company_userPO company_user = new T_company_userPO();
////            company_user.copyProperties(dto);
////            company_user.setId(dto.getString("company_user_id"));
////            company_user.setModify_time(new Date());
////            countMsg = t_company_userDao.updateByKey(company_user);
////            if (countMsg != 1) {
////                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company_user");
////            }
////
////            return ApiMessageUtil.success();
////        } catch (ApiException e) {
////            e.printStackTrace();
////            throw new ApiException(e.getCode(), e.getMessage());
////        } catch (Exception e) {
////            e.printStackTrace();
////            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
////        }
//    }
//
//    @Override
//    @Transactional
//    public ApiMessage delCompanyUser(Dto dto) throws ApiException {
//        String user_id = dto.getString("user_id");
//        String company_id = dto.getString("company_id");
//        int count = 0;
//
//        try {
//            Dto dto1 = new HashDto();
//            dto1.put("user_id", user_id);
//            dto1.put("company_id", company_id);
//            dto1.put("is_valid", "1");
//            T_company_user_rolePO company_user_role = t_company_user_roleDao.selectOne(dto1);
//            if (company_user_role != null) {
//                count = t_company_user_roleDao.deleteByKey(company_user_role.getId());
//                if (count == 0)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company_user_role");
//            }
//
//            T_company_userPO company_user = t_company_userDao.selectOne(dto1);
//            if (company_user != null) {
//                count = t_company_userDao.deleteByKey(company_user.getId());
//                if (count == 0)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company_user");
//            }
//
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }
//
//    @Override
//    @Transactional
//    public ApiMessage fireCompanyUser(Dto dto) throws ApiException {
//        String user_id = dto.getString("user_id");
//        String company_id = dto.getString("company_id");
//        int count = 0;
//
//        try {
//            Dto dto1 = new HashDto();
//            dto1.put("user_id", user_id);
//            dto1.put("company_id", company_id);
//            dto1.put("is_valid", "1");
//            T_company_userPO company_user = t_company_userDao.selectOne(dto1);
//            if (company_user != null) {
//                company_user.copyProperties(dto);
//                company_user.setId(company_user.getId());
//                company_user.setModify_time(new Date());
//                company_user.setStatus("0");//0 表示离职
//                count = t_company_userDao.updateByKey(company_user);
//                if (count == 0)
//                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "company_user");
//            }
//            t_company_userDao.updateByKey(company_user);
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }
//
//    /**
//     * 企业端--员工再入职
//     *
//     * @param dto company_user_id String 必传 企业员工关系表id
//     *            modify_id String 必传 当前登录用户id
//     *            hire_date String 必传 入职日期
//     * @return
//     */
//    @Override
//    @Transactional
//    public ApiMessage hireCompanyUser(Dto dto) throws ApiException {
//        try {
//            T_company_userPO company_user = new T_company_userPO();
//            company_user.copyProperties(dto);
//            company_user.setId(dto.getString("company_user_id"));
//            company_user.setModify_time(new Date());
//            company_user.setStatus("1");
//            t_company_userDao.updateByKey(company_user);
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }
//
//    /**
//     * 企业端--获取企业账单列表
//     *
//     * @param dto company_id String 必传 企业id
//     *            is_paid String 必传 缴费状态：0未交，1已交
//     *            pageNum int 非必传 当前页，默认1
//     *            pageSize int 非必传 每页条数，默认10
//     * @return
//     */
//    @Override
//    public ApiMessage listBills(Dto dto) {
//        dto.put("is_pushed", "1");
//        dto.put("is_valid", "1");
//        PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
//        List<Dto> bills = tGardenDaoExt.listBillsPage(dto);
//        BigDecimal companyTotalFee = null;
//        if ("1".equals(dto.getString("is_paid"))) {
//            companyTotalFee = tCompanyDaoExt.getFee(dto) != null ? tCompanyDaoExt.getFee(dto) : new BigDecimal("0");
//        } else {
//            companyTotalFee = tCompanyDaoExt.getCompanyFee(dto) != null ? tCompanyDaoExt.getCompanyFee(dto) : new BigDecimal("0");
//        }
//        Dto resDto = new HashDto();
//        resDto.put("bills", bills);
//        resDto.put("pageSize", dto.getString("pageSize"));
//        resDto.put("pageNum", dto.getString("pageNum"));
//        resDto.put("total", dto.getPageTotal());
//        resDto.put("companyTotalFee", companyTotalFee);
//
//        return ApiMessageUtil.success(resDto);
//    }
}