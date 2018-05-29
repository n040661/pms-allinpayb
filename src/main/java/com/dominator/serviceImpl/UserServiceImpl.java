package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TUserExt;
import com.dominator.service.CompanyService;
import com.dominator.service.GardenService;
import com.dominator.service.RoleService;
import com.dominator.service.UserService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TPropertyMapper tPropertyMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TUserExt tUserExt;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TGardenUserRoleMapper tGardenUserRoleMapper;

    @Autowired
    private TCompanyUserRoleMapper tCompanyUserRoleMapper;

    @Override
    public int checkUserName(String userName) {
        int count;
        TUserExample userExample = new TUserExample();
        userExample.or().andUserNameEqualTo(userName)
                .andIsValidEqualTo("1");
        count = tUserMapper.countByExample(userExample);
        return count;
    }

    @Override
    public Dto getInfo(Dto dto) {
        String propertyId = dto.getString("propertyId");
        String gardenId = dto.getString( "gardenId");
        String companyId = dto.getString( "companyId");
        String userId = dto.getString( "userId");
        Dto resDto = new HashDto();

        TUser user = tUserMapper.selectByPrimaryKey(userId);
        TProperty property = tPropertyMapper.selectByPrimaryKey(propertyId);
        if(!SystemUtils.isEmpty(gardenId)) {
            TGarden garden = tGardenMapper.selectByPrimaryKey(gardenId);
            resDto.put("garden", garden);
        }
        if(!SystemUtils.isEmpty(companyId)) {
            TCompany company = tCompanyMapper.selectByPrimaryKey(companyId);
            resDto.put("company", company);
        }
        resDto.put("user", user);
        resDto.put("property", property);
        return resDto;
    }

    /**
     * 重置密码
     *
     * @param dto userName String 必传 用户名
     *            passWprd String 必传 新密码
     * @return
     * @throws ApiException
     */
    @Override
    @Transactional
    public ApiMessage resetPassword(Dto dto) throws ApiException {
        String password = Des3Utils.encode(dto.getString("password"));
        String userName = dto.getString("userName");
        int count = 0;
        try {
            count = checkUserName(userName);
            if (count == 0)
                throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT);
            /*if (count!=1)
                throw new ApiException();*/

            TUserExample tUserExample = new TUserExample();
            tUserExample.or().andUserNameEqualTo(userName)
                    .andIsValidEqualTo("1");
            TUser user = new TUser();
            user.setPassword(password);
            count = tUserMapper.updateByExampleSelective(user, tUserExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ApiException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    @Transactional
    public void delUser(Dto dto) throws ApiException {
        String type = dto.getString("type");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String userId = dto.getString("userId");
        switch (type){
            case "1":
                TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                        .andUserIdEqualTo(userId);
                tGardenUserRoleMapper.deleteByExample(gardenUserRoleExample);
                break;
            case "2":
                TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                        .andUserIdEqualTo(userId);
                tCompanyUserRoleMapper.deleteByExample(companyUserRoleExample);
                break;
        }
    }

    @Override
    @Transactional
    public void putUser(Dto dto) throws ApiException {
        String userId = dto.getString("userId");
        String type = dto.getString("type");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String idCard = dto.getString("idCard");
        String nickName = dto.getString("nickName");
        Date hireDate = dto.getDate("hireDate");
        String workNum = dto.getString("workNum");
        String departmentName = dto.getString("departmentName");
        String position = dto.getString("position");
        String level = dto.getString("level");
        String email = dto.getString("email");
        String modifyId = dto.getString("modifyId");
        int count;
        try {
            TUser user = new TUser();
            user.setId(userId);
            user.setIdCard(idCard);
            user.setModifyTime(new Date());
            user.setModifyId(modifyId);
            count = tUserMapper.updateByPrimaryKeySelective(user);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "user");
            switch (type) {
                case "1":
                    TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
                    gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                            .andUserIdEqualTo(userId)
                            .andIsValidEqualTo("1");
                    TGardenUserRole gardenUserRole = new TGardenUserRole();
                    gardenUserRole.setWorkNum(workNum);
                    gardenUserRole.setNickName(nickName);
                    gardenUserRole.setDepartmentName(departmentName);
                    gardenUserRole.setHireDate(hireDate);
                    gardenUserRole.setPosition(position);
                    gardenUserRole.setLevel(level);
                    gardenUserRole.setEmail(email);
                    gardenUserRole.setModifyId(modifyId);
                    gardenUserRole.setModifyTime(new Date());
                    count = tGardenUserRoleMapper.updateByExampleSelective(gardenUserRole, gardenUserRoleExample);
                    if (count == 0)
                        throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "gardenUserRole");
                    break;
                case "2":
                    TCompanyUserRoleExample companyUserRoleExample = new TCompanyUserRoleExample();
                    companyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                            .andUserIdEqualTo(userId)
                            .andIsValidEqualTo("1");
                    TCompanyUserRole companyUserRole = new TCompanyUserRole();
                    companyUserRole.setWorkNum(workNum);
                    companyUserRole.setNickName(nickName);
                    companyUserRole.setDepartmentName(departmentName);
                    companyUserRole.setHireDate(hireDate);
                    companyUserRole.setPosition(position);
                    companyUserRole.setLevel(level);
                    companyUserRole.setEmail(email);
                    companyUserRole.setModifyId(modifyId);
                    companyUserRole.setModifyTime(new Date());
                    count = tCompanyUserRoleMapper.updateByExampleSelective(companyUserRole, companyUserRoleExample);
                    if (count == 0)
                        throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "companyUserRole");
                    break;
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    @Transactional
    public void postUser(Dto dto) throws ApiException {
        String type = dto.getString("type");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String phoneNum = dto.getString("phoneNum");
        Date hireDate = dto.getDate("hireDate");

        String nickName = dto.getString("nickName");
        if(!SystemUtils.isEmpty(nickName))
            nickName = phoneNum;
        String idCard = dto.getString("idCard");
        String workNum = dto.getString("workNum");
        String departmentName = dto.getString("departmentName");
        String position = dto.getString("position");
        String level = dto.getString("level");
        String email = dto.getString("email");
        TRoleExample roleExample = new TRoleExample();
        List<TRole> roleList;
        int count;

        try {
            TUserExample userExample = new TUserExample();
            userExample.or().andUserNameEqualTo(phoneNum)
                    .andIsValidEqualTo("1");
            List<TUser> userList = tUserMapper.selectByExample(userExample);
            if (userList == null || userList.isEmpty())
                throw new ApiException(ReqEnums.REQ_NO_USER);
            TUser user = userList.get(0);
            String userId = user.getId();

            if (!SystemUtils.isEmpty(idCard)) {
                user.setIdCard(idCard);
                count = tUserMapper.updateByPrimaryKey(user);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
            }

            switch (type) {
                case "1":
                    //园区员工,默认普通员工
                    roleExample.or().andUnitIdEqualTo("garden")
                            .andRoleNameEqualTo("普通员工")
                            .andIsValidEqualTo("1");
                    roleList = tRoleMapper.selectByExample(roleExample);
                    if (roleList != null && !roleList.isEmpty()) {
                        TRole role = roleList.get(0);
                        TGardenUserRole gardenUserRole = new TGardenUserRole();
                        gardenUserRole.setId(PrimaryGenerater.getInstance().uuid());
                        gardenUserRole.setGardenId(gardenId);
                        gardenUserRole.setUserId(userId);
                        gardenUserRole.setRoleId(role.getId());
                        gardenUserRole.setModulesIds(role.getModulesIds());
                        gardenUserRole.setNickName(nickName);
                        gardenUserRole.setHireDate(hireDate);
                        gardenUserRole.setWorkNum(workNum);
                        gardenUserRole.setDepartmentName(departmentName);
                        gardenUserRole.setPosition(position);
                        gardenUserRole.setLevel(level);
                        gardenUserRole.setEmail(email);
                        gardenUserRole.setStatus("1");
                        gardenUserRole.setIsValid("1");
                        count = tGardenUserRoleMapper.insertSelective(gardenUserRole);
                        if (count != 1)
                            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
                    }
                    break;
                case "2":
                    //企业员工，默认普通员工
                    roleExample.or().andUnitIdEqualTo("company")
                            .andRoleNameEqualTo("普通员工")
                            .andIsValidEqualTo("1");
                    roleList = tRoleMapper.selectByExample(roleExample);
                    if (roleList != null && !roleList.isEmpty()) {
                        TRole role = roleList.get(0);
                        TCompanyUserRole companyUserRole = new TCompanyUserRole();
                        companyUserRole.setId(PrimaryGenerater.getInstance().uuid());
                        companyUserRole.setCompanyId(companyId);
                        companyUserRole.setUserId(userId);
                        companyUserRole.setRoleId(role.getId());
                        companyUserRole.setModulesIds(role.getModulesIds());
                        companyUserRole.setNickName(nickName);
                        companyUserRole.setHireDate(hireDate);
                        companyUserRole.setWorkNum(workNum);
                        companyUserRole.setDepartmentName(departmentName);
                        companyUserRole.setPosition(position);
                        companyUserRole.setLevel(level);
                        companyUserRole.setEmail(email);
                        companyUserRole.setStatus("1");
                        companyUserRole.setIsValid("1");
                        count = tCompanyUserRoleMapper.insertSelective(companyUserRole);
                        if (count != 1)
                            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
                    }
                    break;
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public ApiMessage userlist(Dto dto) {
        String type = dto.getString("type");
        List<Dto> userList = new ArrayList<>();
        PageInfo<Dto> pageInfo;
        Page.startPage(dto);
        switch (type) {
            case "1":
                userList = tUserExt.gardenUserList(dto);
                break;
            case "2":
                userList = tUserExt.companyUserList(dto);
                break;
        }
        pageInfo = new PageInfo<>(userList);
        Dto resDto = new HashDto();
        resDto.put("list", pageInfo.getList());
        resDto.put("total", pageInfo.getTotal());
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("pageNum", pageInfo.getPageNum());
        return ApiMessageUtil.success(resDto);
    }

    @Override
    @Transactional
    public ApiMessage updatePassword(Dto dto) throws ApiException {
        String user_name = dto.getString("user_name");
        String password = dto.getString("password");
        String new_password = dto.getString("new_password");
        int count;
        try {
            TUserExample userExample = new TUserExample();
            userExample.or().andUserNameEqualTo(user_name)
                    .andPasswordEqualTo(Des3Utils.encode(password))
                    .andIsValidEqualTo("1");
            List<TUser> userList = tUserMapper.selectByExample(userExample);
            if (userList == null || userList.isEmpty())
                throw new ApiException(ReqEnums.REQ_WRONG_PASSWORD.getCode(), ReqEnums.REQ_WRONG_PASSWORD.getMsg());
            TUser user = userList.get(0);
            user.setPassword(Des3Utils.encode(new_password));
            user.setModifyTime(new Date());
            user.setModifyId(user.getId());
            count = tUserMapper.updateByPrimaryKeySelective(user);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            String token = ApiUtils.resetToken(dto);
            Dto resDto = new HashDto();
            resDto.put("token", token);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public int countGardenUser(Dto dto) {
        String gardenId = dto.getString("gardenId");
        Set<String> ids = new HashSet<>();
        int count = 0;

        TGardenUserRoleExample gardenUserRoleExample = new TGardenUserRoleExample();
        gardenUserRoleExample.or().andGardenIdEqualTo(gardenId)
                .andIsValidEqualTo("1");
        List<TGardenUserRole> gardenUserRoleList = tGardenUserRoleMapper.selectByExample(gardenUserRoleExample);
        if (gardenUserRoleList != null && !gardenUserRoleList.isEmpty()) {
            for (TGardenUserRole gardenUserRole : gardenUserRoleList) {
                ids.add(gardenUserRole.getUserId());
            }
            TUserExample userExample = new TUserExample();
            userExample.or().andIsValidEqualTo("1")
                    .andIdIn(new ArrayList<>(ids));
            count = tUserMapper.countByExample(userExample);
        }
        return count;
    }

    @Override
    public int countCompanyUser(Dto dto) {
        String companyId = dto.getString("companyId");
        Set<String> ids = new HashSet<>();
        int count = 0;

        TCompanyUserRoleExample tCompanyUserRoleExample = new TCompanyUserRoleExample();
        tCompanyUserRoleExample.or().andCompanyIdEqualTo(companyId)
                .andIsValidEqualTo("1");
        List<TCompanyUserRole> companyUserRoleList = tCompanyUserRoleMapper.selectByExample(tCompanyUserRoleExample);
        if (companyUserRoleList != null && !companyUserRoleList.isEmpty()) {
            for (TCompanyUserRole companyUserRole : companyUserRoleList) {
                ids.add(companyUserRole.getUserId());
            }
            TUserExample userExample = new TUserExample();
            userExample.or().andIsValidEqualTo("1")
                    .andIdIn(new ArrayList<>(ids));
            count = tUserMapper.countByExample(userExample);
        }
        return count;
    }

    @Override
    @Transactional
    public ApiMessage setPassword(Dto dto) throws ApiException {
        String user_name = dto.getString("user_name").trim();
        String password = Des3Utils.encode(dto.getString("password"));
        int count;
        try {
            TUserExample userExample = new TUserExample();
            userExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");
            TUser user = new TUser();
            user.setPassword(password);
            count = tUserMapper.updateByExampleSelective(user, userExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    /**
     * 添加用户信息
     *
     * @param dto user_id
     *            user_name
     *            nick_name
     *            password
     */
    @Override
    @Transactional
    public String addUser(Dto dto) throws ApiException {

        String user_id = dto.getString("user_id").trim();
        String user_name = dto.getString("user_name").trim();
        if (SystemUtils.isEmpty(user_name)) {
            user_name = dto.getString("phone_num");
        }
        String password = dto.getString("password").trim();
        String nick_name = dto.getString("nick_name").trim();
        String phone_num = user_name;
        String id_card = dto.getString("id_card");
        String email = dto.getString("email");
        String head_img = dto.getString("head_img");
        int count;
        try {

            TUser user = new TUser();
            user.setId(user_id);
            user.setUserName(user_name);
            user.setPassword(SystemUtils.isEmpty(password) ? null : Des3Utils.encode(password));
            user.setNickName(nick_name);
            user.setPhoneNum(phone_num);
            user.setIdCard(id_card);
            user.setEmail(email);
            user.setHeadImg(head_img);
            user.setCreateTime(new Date());
            user.setIsValid("1");
            count = tUserMapper.insert(user);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "insert user");

            return user_id;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

//    /**
//     * @param dto user_id
//     *            nick_name
//     *            password
//     */
//    @Override
//    @Transactional
//    public void updateUser(Dto dto) throws ApiException {
//        String user_id = dto.getString("user_id");
//        String password = dto.getString("password");
//        int count;
//        try {
//            TUser user = new TUser();
//            user.setId(user_id);
//            user.setPassword(SystemUtils.isEmpty(password) ? null : Des3Utils.encode(password));
//            user.setModifyTime(new Date());
//            count = tUserMapper.updateByPrimaryKeySelective(user);
//            if (count != 1)
//                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "update user");
//
//        } catch (ApiException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }

    @Override
    public ApiMessage getUser(Dto dto) {
        String type = dto.getString("type");
        Dto resDto = new HashDto();
        switch (type) {
            case "1":
                resDto = tUserExt.getGardenUser(dto);
                break;
            case "2":
                resDto = tUserExt.getCompanyUser(dto);
                break;
        }
        return ApiMessageUtil.success(resDto);
    }

    @Override
    public ApiMessage getRoleUserList(Dto dto) {
        String type = dto.getString("type");
        List<TUser> list = new ArrayList<>();
        switch (type) {
            case "1":
                list = gardenService.listGardenUserRole(dto);
                break;
            case "2":
                list = companyService.listCompanyUserRole(dto);
                break;
        }
        return ApiMessageUtil.success(list);
    }
}
