package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.PageExt;
import com.dominator.mapper.ext.TLoginExt;
import com.dominator.mapper.ext.TPropertyExt;
import com.dominator.service.GardenService;
import com.dominator.service.LoginService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Constants;
import com.dominator.utils.system.PrimaryGenerater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TLoginInfoMapper tLoginInfoMapper;

    @Autowired
    private TPropertyExt tPropertyExt;

    @Autowired
    private TLoginExt tLoginExt;

    @Autowired
    private PageExt pageExt;

    @Autowired
    private GardenService gardenService;

    private static final String KEY = "CheckMessage";

    //作为redis中token的key
    private static final String TOKENKEY = "TOKEN::KEY";

    private static RedisUtil ru = RedisUtil.getRu();

    @Override
    @Transactional
    public ApiMessage loginRecord(Dto dto) throws ApiException {
        int count;
        try {
            TLoginInfo loginInfo = new TLoginInfo();
            loginInfo.setId(PrimaryGenerater.getInstance().uuid());
            loginInfo.setLoginTime(new Date());
            loginInfo.setCompanyId(dto.getString("companyId"));
            loginInfo.setGardenId(dto.getString("gardenId"));
            loginInfo.setPropertyId(dto.getString("propertyId"));
            loginInfo.setUserId(dto.getString("userId"));
            loginInfo.setIsValid("1");
            count = tLoginInfoMapper.insertSelective(loginInfo);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    @Transactional
    public ApiMessage loginStep2(Dto dto) throws ApiException {
        String token = dto.getString("token");
        String companyId = dto.getString("companyId");
        String gardenId = "";
        String propertyId = "";
        Dto dto1 = new HashDto();
        try {
            if (!SystemUtils.isEmpty(companyId)) {
                TCompanyExample tCompanyExample = new TCompanyExample();
                tCompanyExample.or().andIdEqualTo(companyId)
                        .andIsValidEqualTo("1");
                List<TCompany> companyList = tCompanyMapper.selectByExample(tCompanyExample);

                if (companyList != null && !companyList.isEmpty()) {
                    TCompany company = companyList.get(0);
                    gardenId = company.getGardenId();
                    dto1.clear();
                    dto1.put("gardenId", gardenId);
                    Dto garden = gardenService.getGarden(dto1);
                    if (garden != null)
                        propertyId = garden.getString("propertyId");
                }
                dto.put("gardenId", gardenId);
                dto.put("propertyId", propertyId);
            }
            ApiUtils.updateToken(token, dto);
            loginRecord(dto);
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public ApiMessage loginV2(Dto dto) throws ApiException {
        String userName = dto.getString("userName");
        String password = Des3Utils.encode(dto.getString("password"));
        String loginType = dto.getString("loginType");

        try {
            //匹配账号密码
            TUserExample tUserExample = new TUserExample();
            tUserExample.or().andUserNameEqualTo(userName)
                    .andIsValidEqualTo("1");
            List<TUser> userList = tUserMapper.selectByExample(tUserExample);
            if (userList == null)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT.getCode(), ReqEnums.REQ_USER_NOT_EXSIT.getMsg());

            TUser user = userList.get(0);
            if (!password.equals(user.getPassword()))
                throw new ApiException(ReqEnums.REQ_WRONG_PASSWORD.getCode(), ReqEnums.REQ_WRONG_PASSWORD.getMsg());

            String userId = user.getId();
            String roleId = "";

            //token 生成
            Dto tokenDto = new HashDto();
            tokenDto.put("type", loginType);
            tokenDto.put("userName", userName);
            tokenDto.put("userId", userId);
            String token = ApiUtils.getToken(tokenDto);

            Dto userDto = new HashDto();
            userDto.put("token", token);
            userDto.put("userName", userName);
            userDto.put("userId", userId);
            userDto.put("nickName", user.getNickName());
            userDto.put("phoneNum", user.getPhoneNum());

            Dto resDto = new HashDto();
            resDto.put("userInfo", userDto);

            Dto dto1 = new HashDto();
            switch (loginType) {
                case "1":
                    List<Dto> propertyList = new ArrayList<>();
                    List<Dto> gardenList = new ArrayList<>();
                    List<String> roleIdList = new ArrayList<>();
                    List<String> propertyIdList = new ArrayList<>();
                    TRoleExample roleExample = new TRoleExample();
                    roleExample.or().andRoleNameEqualTo("物业管理员")
                            .andTypeEqualTo("4")
                            .andIsValidEqualTo("1");
                    List<TRole> roleList = tRoleMapper.selectByExample(roleExample);
                    if (roleList != null && !roleList.isEmpty()) {
                        for (TRole role : roleList)
                            roleIdList.add(role.getId());
                        dto1.put("userId", userId);
                        dto1.put("roleIdList", roleIdList);
                        propertyList = tLoginExt.listProperty(dto1);
                        if (propertyList != null && !propertyList.isEmpty()) {
                            for (Dto dto2 : propertyList)
                                propertyIdList.add(dto2.getString("id"));
                        }
                    }

                    roleExample.clear();
                    roleExample.or().andRoleNameEqualTo("园区管理员")
                            .andTypeEqualTo("4")
                            .andIsValidEqualTo("1");
                    roleList = tRoleMapper.selectByExample(roleExample);
                    if (roleList != null && !roleList.isEmpty()) {
                        for (TRole role : roleList)
                            roleIdList.add(role.getId());
                        dto1.clear();
                        dto1.put("userId", userId);
                        dto1.put("roleIdList", roleIdList);
                        dto1.put("propertyIdList", propertyIdList.isEmpty() ? null : propertyIdList);
                        gardenList = tLoginExt.listGarden(dto1);

                    }

                    resDto.put("propertyList", propertyList);
                    resDto.put("gardenList", gardenList);
                    boolean b1 = propertyList == null || propertyList.size() == 0;
                    boolean b2 = gardenList == null || gardenList.size() == 0;
                    if (b1 && b2)
                        throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR.getCode(), ReqEnums.REQ_OTO_LOGIN_ERROR.getMsg());
                    break;
                case "2":
                    dto1.put("userId", userId);
                    List<Dto> companyList = tLoginExt.listCompany(dto1);
                    resDto.put("companyList", companyList);
                    if (companyList == null || companyList.size() == 0)
                        throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR.getCode(), ReqEnums.REQ_OTO_LOGIN_ERROR.getMsg());
                    break;
                default:
                    throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR.getCode(), ReqEnums.REQ_OTO_LOGIN_ERROR.getMsg());
            }
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR.getCode(), ReqEnums.REQ_OTO_LOGIN_ERROR.getMsg());
        }
    }

    /**
     * 获取登陆用户的所属企业（园区)列表
     *
     * @param dto user_id 用户ID
     *            token 用户的token
     *            garden_id 园区ID 非必传
     */
    @Override
    public ApiMessage listusercompany(Dto dto) throws ApiException {
        List<Dto> dtoList = new ArrayList<Dto>();
        Dto resDto = new HashDto();
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String property_id = tokenDto.getString("property_id");
        Dto newTokenDto = new HashDto();
        newTokenDto.put("app_id", tokenDto.getString("app_id"));
        newTokenDto.put("user_name", tokenDto.getString("user_name"));
        newTokenDto.put("user_id", tokenDto.getString("user_id"));
        newTokenDto.put("time", tokenDto.getString("time"));
        newTokenDto.put("property_id", tokenDto.getString("property_id"));
        dto.put("property_id", property_id);
        //获取登陆后的物业管理员信息
        Dto propertyDto = tPropertyExt.getPropertyUser(dto);

        //查询登陆过后的用户ID对应所有企业／园区 员工信息
        List<Dto> list = pageExt.selectAllByUserId(dto);
        //组装成一个新的含有所有用户信息的列表
        List<Dto> dtoList1 = new ArrayList<Dto>();
        if (list != null) {
            dtoList1.addAll(list);
        }
        if (propertyDto != null) {
            dtoList1.add(propertyDto);
        }

        switch (dtoList1.size()) {
            case 0:
                //普通用户
                //获取园区列表的默认园区
                Dto reqDto = new HashDto();
                reqDto.put("is_valid", "1");
                reqDto.put("property_id", property_id);
//                List<T_gardenPO> gardenList = gardenDao.list(reqDto);
                TGardenExample tGardenExample = new TGardenExample();
                tGardenExample.or().andPropertyIdEqualTo(property_id).andIsValidEqualTo("1");
                List<TGarden> gardenList = tGardenMapper.selectByExample(tGardenExample);
                if (gardenList != null && gardenList.size() > 0) {
//                    T_gardenPO t_gardenPO = gardenList.get(0);
                    TGarden tGarden = gardenList.get(0);
                    if (tGarden != null) {
                        resDto.put("garden_id", tGarden.getId());
                        resDto.put("garden_name", tGarden.getGardenName());
                        resDto.put("province", tGarden.getProvince());
                        resDto.put("city", tGarden.getCity());
                        resDto.put("area", tGarden.getArea());
                        resDto.put("street", tGarden.getStreet());
                    }
                }
                resDto.put("identityType", "0");
                break;
            case 1:
                //单个身份返回园区信息
                Dto dto1 = dtoList1.get(0);
                String type1 = dto1.getString("type");
                String garden_id1 = dto1.getString("garden_id");
                newTokenDto.put("type", type1);
                newTokenDto.put("garden_id", garden_id1);
                switch (type1) {
                    case "1":
                        //查询园区用户信息
                        Dto gardenDto = pageExt.selectGardenUser(dto1);
                        if (gardenDto != null) {
                            newTokenDto.put("role_id", gardenDto.getString("role_id"));
                        }
                        break;
                    case "2":
                        //查询企业用户信息
                        Dto companyDto = pageExt.selectCompanyUser(dto1);
                        if (companyDto != null) {
                            newTokenDto.put("role_id", companyDto.getString("role_id"));
                            newTokenDto.put("company_id", dto1.getString("id"));
                        }
                        break;
                    case "3":
                        newTokenDto.put("property_id", dto1.getString("id"));
                        newTokenDto.put("role_id", dto1.getString("role_id"));


                }

                //获取园区信息
                resDto = getGardenInfo(dto1);
                resDto.put("property_id", property_id);
                resDto.put("identityType", "1");
                break;
            default:
                //多身份
                for (Dto dto11 : dtoList1) {
                    Dto dto2 = new HashDto();
                    dto2.put("property_id", property_id);
                    String type = dto11.getString("type");
                    //将多身份的类型放入到token中
                    dto2.put("type", type);
                    switch (type) {
                        case "1":
                            //查询园区用户信息
                            Dto gardenDto = pageExt.selectGardenUser(dto11);
                            if (gardenDto != null) {
                                dto2.put("role_name", gardenDto.getString("role_name"));
                                dto2.put("name", dto11.getString("name"));
                                dto2.put("role_id", gardenDto.getString("role_id"));
                            }
                            break;
                        case "2":
                            //查询企业用户信息
                            Dto companyDto = pageExt.selectCompanyUser(dto11);
                            if (companyDto != null) {
                                dto2.put("role_name", companyDto.getString("role_name"));
                                dto2.put("name", dto11.getString("name"));
                                dto2.put("role_id", companyDto.getString("role_id"));
                                dto2.put("company_id", dto11.getString("id"));
                            }
                            break;
                        default:
                            //查询物业用户信息
                            dto2.put("role_name", dto11.getString("role_name"));
                            dto2.put("name", dto11.getString("name"));
                            dto2.put("role_id", dto11.getString("role_id"));
                            break;
                    }
                    dto2.putAll(getGardenInfo(dto11));
                    dtoList.add(dto2);
                }
                resDto.put("identityType", "2");
                resDto.put("identityNum", dtoList1.size());
                resDto.put("companyList", dtoList);
                break;
        }
        //将token重新放入redis中
        ru.setObjectEx(token, newTokenDto, Constants.TOKEN_TIMES_INT);
        //查看该用户的企业列表
        return ApiMessageUtil.success(resDto);
    }

    //获取园区信息
    private Dto getGardenInfo(Dto dto) {
        Dto dto2 = new HashDto();

        Dto tGardenDto = gardenService.getGarden(dto);
        dto2.put("garden_id", dto.getString("garden_id"));
        if (tGardenDto != null) {
            dto2.put("garden_name", tGardenDto.getString("garden_name"));
            dto2.put("province", tGardenDto.getString("province"));
            dto2.put("city", tGardenDto.getString("city"));
            dto2.put("area", tGardenDto.getString("area"));
            dto2.put("street", tGardenDto.getString("street"));
        }
        return dto2;
    }


//   /**
//     * 企业用户登录
//     *
//     * @param dto user_name String 必传 账号
//     *            password String 必传 密码
//     */
   /*
    @Override
    @Transactional
    public ApiMessage login4EnterpriseUser(Dto dto) throws ApiException {
        String user_name = dto.getString("user_name");
        String password = Des3Utils.encode(dto.getString("password"));

        //匹配账号密码
        Dto dto1 = new HashDto();
        dto1.put("user_name", user_name);
        dto1.put("is_valid", "1");
//        T_userPO t_userPO = t_userDao.selectOne(dto1);
        TUserExample tUserExample = new TUserExample();
        tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");
        List<TUser> list = tUserMapper.selectByExample(tUserExample);
        if (list == null || list.isEmpty()) {
            //用户名不存在
            throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT.getCode(), ReqEnums.REQ_USER_NOT_EXSIT.getMsg());
        }

        dto1.put("password", password);
        log.info("password:{}", password);
//        T_userPO t_userPO1 = t_userDao.selectOne(dto1);
        tUserExample.clear();
        tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1").andPasswordEqualTo(password);
        List<TUser> list1 = tUserMapper.selectByExample(tUserExample);
        if (list1 == null || list1.isEmpty()) {
            //用户名密码不匹配
            throw new ApiException(ReqEnums.REQ_WRONG_PASSWORD.getCode(), ReqEnums.REQ_WRONG_PASSWORD.getMsg());
        }

        //判断是否是企业用户
//        String user_id = t_userPO1.getId();
        TUser tuser = list1.get(0);
        String user_id = tuser.getId();
        Dto dto2 = new HashDto();
        dto2.put("user_id", user_id);
        dto2.put("status", "1");
        dto2.put("is_valid", "1");
//        T_company_userPO t_company_userPO = t_company_userDao.selectOne(dto2);
        TCompanyUserExample tCompanyUserExample = new TCompanyUserExample();
        tCompanyUserExample.or().andUserIdEqualTo(user_id).andStatusEqualTo("1").andIsValidEqualTo("1");
        List<TCompanyUser> tCompanyUserList = tCompanyUserMapper.selectByExample(tCompanyUserExample);
        if (tCompanyUserList == null || tCompanyUserList.isEmpty()) {
            //非企业用户
            throw new ApiException(ReqEnums.REQ_NOT_COMPANY_USER.getCode(), ReqEnums.REQ_NOT_COMPANY_USER.getMsg());
        }

        //role_id为管理员，才能登录企业端
//        String company_id = t_company_userPO.getCompany_id();
        TCompanyUser tCompanyUser = tCompanyUserList.get(0);
        String company_id = tCompanyUser.getCompanyId();
        Dto dto3 = new HashDto();
        dto3.put("company_id", company_id);
        dto3.put("role_id", propertiesLoader.getProperty("company_admin_id"));
        log.info("role_id:{}", propertiesLoader.getProperty("company_admin_id"));
        dto3.put("user_id", tCompanyUser.getUserId());
        dto3.put("is_valid", "1");
//        T_company_user_rolePO t_company_user_rolePO = t_company_user_roleDao.selectOne(dto3);
        TCompanyUserRoleExample tCompanyUserRoleExample = new TCompanyUserRoleExample();
        tCompanyUserRoleExample.or().andCompanyIdEqualTo(company_id).andRoleIdEqualTo(propertiesLoader.getProperty("company_admin_id"))
                .andUserIdEqualTo(tCompanyUser.getUserId()).andIsValidEqualTo("1");
        List<TCompanyUserRole> tCompanyUserRolesList = tCompanyUserRoleMapper.selectByExample(tCompanyUserRoleExample);
        if (tCompanyUserRolesList == null || tCompanyUserRolesList.isEmpty()) {
            //非企业用户
            throw new ApiException(ReqEnums.REQ_NOT_COMPANY_USER.getCode(), ReqEnums.REQ_NOT_COMPANY_USER.getMsg());
        }
        TCompanyUserRole tCompanyUserRole = tCompanyUserRolesList.get(0);
        //获取token，存redis
        Dto tokenDto = new HashDto();
        tokenDto.put("type", "2");
        tokenDto.put("user_name", user_name);
        tokenDto.put("user_id", user_id);
        tokenDto.put("company_id", company_id);
        tokenDto.put("role_id", tCompanyUserRole.getRoleId());
        String token = ApiUtils.getToken(tokenDto);

        Dto userInfo = new HashDto();
        userInfo.put("user_id", user_id);
        userInfo.put("token", token);
        userInfo.put("nick_name", tuser.getNickName());
        userInfo.put("phone_num", tuser.getPhoneNum());
        userInfo.put("role_id", "77777777");

        try {
//            T_companyPO company1 = new T_companyPO();
            TCompany company1 = new TCompany();
            company1.setId(tCompanyUser.getCompanyId());
            company1.setModifyTime(new Date());
            company1.setModifyId(user_id);
//            t_companyDao.updateByKey(company1);
            tCompanyMapper.updateByPrimaryKey(company1);

            //存登录记录
            Dto dto6 = new HashDto();
            dto6.put("company_id", company_id);
            dto6.put("user_id", user_id);
            loginInfo(dto6);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //封装公司信息
        Dto dto4CompanyInfo = new HashDto();
        dto4CompanyInfo.put("id", tCompanyUser.getCompanyId());
        dto4CompanyInfo.put("is_valid", "1");
//        T_companyPO company = t_companyDao.selectOne(dto4CompanyInfo);
        TCompanyExample tCompanyExample = new TCompanyExample();
        tCompanyExample.or().andIdEqualTo(tCompanyUser.getCompanyId()).andIsValidEqualTo("1");
        List<TCompany> companyList = tCompanyMapper.selectByExample(tCompanyExample);
        TCompany tCompany = companyList.get(0);
        ru.setObject("company_" + tCompanyUser.getCompanyId(), tCompany);

        Dto resDto = new HashDto();
        resDto.put("company_info", tCompany);
        resDto.put("user_info", userInfo);

        return ApiMessageUtil.success(resDto);
    }
*/

   /* *//**
     * 园区端--用户登录
     *
     * @param dto user_name String 必传 邮箱账号
     *            password String 必传 密码
     * @return
     * @throws ApiException
     *//*
    @Override
    @Transactional
    public ApiMessage loginByEmail(Dto dto) throws ApiException {
        String user_name = dto.getString("user_name");
        String password = Des3Utils.encode(dto.getString("password"));

        //匹配账号密码
        Dto dto1 = new HashDto();
        dto1.put("user_name", user_name);
        dto1.put("is_valid", "1");
//        T_userPO t_userPO = t_userDao.selectOne(dto1);
        TUserExample tUserExample = new TUserExample();
        tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");
        List<TUser> tUserList = tUserMapper.selectByExample(tUserExample);
        TUser tUser = tUserList.get(0);
        if (tUser == null) {
            //用户名不存在
            throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT.getCode(), ReqEnums.REQ_USER_NOT_EXSIT.getMsg());
        }

        dto1.put("password", password);
        log.info("password:{}", password);
//        T_userPO t_userPO1 = t_userDao.selectOne(dto1);
        tUserExample.clear();
        tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1").andPasswordEqualTo(password);
        List<TUser> tUserList1 = tUserMapper.selectByExample(tUserExample);
        if (tUserList1 == null || tUserList1.isEmpty()) {
            //用户名密码不匹配
            throw new ApiException(ReqEnums.REQ_WRONG_PASSWORD.getCode(), ReqEnums.REQ_WRONG_PASSWORD.getMsg());
        }

        //判断用户是否是超级管理员
        TUser tUser1 = tUserList.get(0);
        String user_id = tUser1.getId();
        String super_admin = "0";
        String property_id = "";
        String role_id = "";
        List<Dto> gardenList = new ArrayList<>();
        Dto dto4 = new HashDto();
        dto4.put("user_id", user_id);
        dto4.put("is_valid", "1");
        dto4.put("role_id", propertiesLoader.getProperty("super_admin_id"));
//        T_property_user_rolePO property_user_role = t_property_user_roleDao.selectOne(dto4);
        TPropertyUserRoleExample tPropertyUserRoleExample = new TPropertyUserRoleExample();
        tPropertyUserRoleExample.or().andUserIdEqualTo(user_id).andIsValidEqualTo("1").andRoleIdEqualTo(propertiesLoader.getProperty("super_admin_id"));
        List<TPropertyUserRole> tPropertyUserRoleList = tPropertyUserRoleMapper.selectByExample(tPropertyUserRoleExample);
        TPropertyUserRole property_user_role;
        if (tPropertyUserRoleList == null || tPropertyUserRoleList.isEmpty()) {
            //判断是否是园区管理员
            Dto dto2 = new HashDto();
            dto2.put("user_id", user_id);
            dto2.put("role_name", "园区管理员");
            gardenList = tLoginExt.listGardens(dto2);
            if (gardenList != null && gardenList.size() > 0) {
                property_id = gardenList.get(0).getString("property_id");
                role_id = gardenList.get(0).getString("role_id");
            }
        } else {
            property_user_role = tPropertyUserRoleList.get(0);
            super_admin = "1";
            property_id = property_user_role.getPropertyId();
            role_id = property_user_role.getRoleId();
        }

        //存登录记录
        try {
            Dto dto6 = new HashDto();
            dto6.put("property_id", property_id);
            dto6.put("user_id", user_id);
            loginInfo(dto6);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg() + "login_info");
        }

        Dto dto5 = new HashDto();
        dto5.put("id", property_id);
        dto5.put("is_valid", "1");
//        T_propertyPO property = t_propertyDao.selectOne(dto5);
        TPropertyExample tPropertyExample = new TPropertyExample();
        tPropertyExample.or().andIdEqualTo(property_id).andIsValidEqualTo("1");
        List<TProperty> propertyList = tPropertyMapper.selectByExample(tPropertyExample);
        if(propertyList==null || propertyList.isEmpty()){

        }
        //token 生成
        TProperty tProperty = propertyList.get(0);
        Dto tokenDto = new HashDto();
        tokenDto.put("type", "1");
        tokenDto.put("user_name", user_name);
        tokenDto.put("user_id", user_id);
        tokenDto.put("property_id", tProperty.getId());
        tokenDto.put("role_id", role_id);
        String token = ApiUtils.getToken(tokenDto);

        Dto user_info = new HashDto();
        user_info.put("token", token);
        user_info.put("user_id", user_id);
        user_info.put("nick_name", tUser1.getNickName());
        user_info.put("phone_num", tUser1.getPhoneNum());
        user_info.put("role_id", role_id);

        Dto resDto = Dtos.newDto();
        resDto.put("property_info", tProperty);
        resDto.put("super_admin", super_admin);
        resDto.put("user_info", user_info);
        resDto.put("gardenList", gardenList);
        return ApiMessageUtil.success(resDto);
    }
*/

    /**
     * 登录记录
     *
     * @param dto user_id 必传
     *            property_id
     *            garden_id
     *            company_id
     * @return
     */
    @Override
    @Transactional
    public ApiMessage loginInfo(Dto dto) throws ApiException {
        try {
            TLoginInfo login_info = new TLoginInfo();
            login_info.setCompanyId(dto.getString("company_id"));
            login_info.setGardenId(dto.getString("garden_id"));
            login_info.setPropertyId(dto.getString("property_id"));
            login_info.setUserId(dto.getString("user_id"));
            login_info.setId(PrimaryGenerater.getInstance().uuid());
            login_info.setLoginTime(new Date());
            login_info.setIsValid("1");
            int count = 0;
            count = tLoginInfoMapper.insert(login_info);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
            }
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public Date getLastLoginTime(Dto dto) {
        String propertyId = dto.getString("propertyId");
        if (SystemUtils.isEmpty(propertyId))
            propertyId = dto.getString("property_id");

        String gardenId = dto.getString("gardenId");
        if (SystemUtils.isEmpty(gardenId))
            gardenId = dto.getString("garden_id");

        String companyId = dto.getString("companyId");
        if (SystemUtils.isEmpty(companyId))
            companyId = dto.getString("company_id");
        String userId = dto.getString("userId");
        if (SystemUtils.isEmpty(userId))
            userId = dto.getString("user_id");
        Date date = null;

        TLoginInfoExample loginInfoExample = new TLoginInfoExample();
        TLoginInfoExample.Criteria criteria = loginInfoExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        if (!SystemUtils.isEmpty(propertyId))
            criteria.andPropertyIdEqualTo(propertyId);
        if (!SystemUtils.isEmpty(gardenId))
            criteria.andGardenIdEqualTo(gardenId);
        if (!SystemUtils.isEmpty(companyId))
            criteria.andCompanyIdEqualTo(companyId);
        loginInfoExample.setOrderByClause(" login_time desc limit 1");
        List<TLoginInfo> loginInfoList = tLoginInfoMapper.selectByExample(loginInfoExample);
        if (loginInfoList != null && !loginInfoList.isEmpty())
            date = loginInfoList.get(0).getLoginTime();
        return date;
    }

    @Override
    public ApiMessage loginByAdmin(Dto dto) throws ApiException {
        String user_name = dto.getString("user_name");
        String password = Des3Utils.encode(dto.getString("password"));

        dto.put("password", password);
        dto.put("is_valid", "1");
        TUserExample tUserExample = new TUserExample();
        tUserExample.or().andUserNameEqualTo(user_name).andPasswordEqualTo(password).andIsValidEqualTo("1");
        List<TUser> tUserList = tUserMapper.selectByExample(tUserExample);

        if (tUserList == null || tUserList.isEmpty()) {
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), ReqEnums.REQ_PARAM_ERROR.getMsg());
        }
        TUser user = tUserList.get(0);
        String user_id = user.getId();
        //token 生成
        Dto tokenDto = new HashDto();
        tokenDto.put("type", "3");
        tokenDto.put("user_name", user_name);
        tokenDto.put("user_id", user_id);
        String token = ApiUtils.getToken(tokenDto);

        try {
            Dto dto2 = new HashDto();
            dto2.put("property_id", "admin");
            dto2.put("user_id", user_id);
            loginInfo(dto2);
        } catch (Exception e) {
            throw new ApiException(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg() + "login_info");
        }

        Dto userDto = new HashDto();
        userDto.put("token", token);
        userDto.put("user_name", user_name);
        userDto.put("user_id", user_id);
        userDto.put("nick_name", user.getNickName());
        userDto.put("phone_num", user.getPhoneNum());

        Dto resDto = new HashDto();
        resDto.put("type", "3");
        resDto.put("user_info", userDto);
        return ApiMessageUtil.success(resDto);
    }
}
