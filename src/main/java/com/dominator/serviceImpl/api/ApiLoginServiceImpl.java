
package com.dominator.serviceImpl.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.app.service.LoginService;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.PageExt;
import com.dominator.mapper.ext.TPropertyExt;
import com.dominator.service.GardenService;
import com.dominator.service.PropertyService;
import com.dominator.service.WechatService;
import com.dominator.service.api.ApiLoginService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dominator.utils.sms.SmsUtils.checkSms;


@Service
@Slf4j
public class ApiLoginServiceImpl implements ApiLoginService {

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
    private TPropertyExt tPropertyExt;

    @Autowired
    private PageExt pageExt;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private LoginService loginService;

    private static final String KEY = "CheckMessage";

    private static RedisUtil ru = RedisUtil.getRu();


    /**
     * 获取登陆用户的所属企业（园区)列表
     *
     * @param dto token 用户的token
     * @return
     */
    @Override
    public ApiMessage identityList(Dto dto) throws ApiException {
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
        dto.putAll(newTokenDto);
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
                TGardenExample tGardenExample = new TGardenExample();
                tGardenExample.or().andPropertyIdEqualTo(property_id).andIsValidEqualTo("1");
                List<TGarden> gardenList = tGardenMapper.selectByExample(tGardenExample);
                tGardenExample.clear();
                //List<TGarden> gardenList = gardenDao.list(reqDto);
                if (gardenList != null && gardenList.size() > 0) {
                    TGarden t_gardenPO = gardenList.get(0);
                    if (t_gardenPO != null) {
                        resDto.put("garden_id", t_gardenPO.getId());
                        resDto.put("garden_name", t_gardenPO.getGardenName());
                        resDto.put("province", t_gardenPO.getProvince());
                        resDto.put("city", t_gardenPO.getCity());
                        resDto.put("area", t_gardenPO.getArea());
                        resDto.put("street", t_gardenPO.getStreet());
                    }
                }
                resDto.put("property_id", property_id);
                resDto.put("identity_type", "0");
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
                resDto.put("identity_type", "1");
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
                resDto.put("identity_type", "2");
                resDto.put("identity_num", dtoList1.size());
                resDto.put("identityList", dtoList);
                break;
        }
        //将token重新放入redis中
        ru.setObjectEx(token, newTokenDto, Constants.TOKEN_TIMES_INT);
        //查看该用户的企业列表
        return ApiMessageUtil.success(resDto);
    }

    //获取园区信息
    public Dto getGardenInfo(Dto dto) {
        Dto dto2 = new HashDto();

        Dto gardenDto = gardenService.getGarden(dto);
        dto2.put("gardend", dto.getString("garden_id"));
        if (gardenDto != null) {
            dto2.put("garden_name", gardenDto.getString("garden_name"));
            dto2.put("contact_address", gardenDto.getString("contact_address"));
        }
        return dto2;
    }

    @Override
    public Dto login(Dto dto) throws ApiException {
        String appId = dto.getString("appId");
        String propertyId = wechatService.getPropertyIdByAppId(appId);
        String code = dto.getString("code");
        String userName = dto.getString("userName");
        String password = dto.getString("password");
        TUser user = new TUser();
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
        }

        Dto tokenDto = new HashDto();
        Dto resDto = loginService.getLoginList(propertyId, user, tokenDto);
        String token = ApiUtils.getToken(tokenDto);
        resDto.put("token", token);
        return resDto;


//        String type = dto.getString("type");
//        String user_name = dto.getString("user_name");
//        String appid = dto.getString("app_id");
//        Dto resDto = Dtos.newDto();
//        ApiMessage msg = new ApiMessage(Constants.REQ_SUCCESS, Constants.REQ_LOGIN_SUCCESS);
//        String user_id = "";
//        //获取token，存redis
//        Dto tokenDto = new HashDto();
//        TUserExample tUserExample = new TUserExample();
//        //验证码登陆
//        if (type.equals("0")) {
//            String usercode = dto.getString("user_code");
//
//            long remain_time = ru.ttl(KEY + user_name);
//            //表示Redis中没有值
//            if (remain_time == -2) {
//                throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
//            }
//            if (remain_time == -1) {
//                String code = ru.get(KEY + user_name);
//                if (StringUtils.isNotEmpty(code)) {
//                    if (!usercode.equals(code)) {//是否用户填入的验证码和缓存中的验证码一致
//                        throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
//                    }
//                }
//            }
//
//            boolean flag = Day - remain_time - HalfHour > 0;
//            String code = ru.get(KEY + user_name);
//            if (flag && remain_time != -1) {
//                if (StringUtils.isNotEmpty(code)) {
//                    if (usercode.equals(code)) {//是否用户填入的验证码和缓存中的验证码一致
//                        //表示短信验证码过期
//                        throw new ApiException(Constants.CODE_CHECK_PASS_ERROR, Constants.MSG_CHECK_PASS_ERROR);
//                    } else {
//                        throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
//                    }
//                }
//            } else {
//                if (StringUtils.isNotEmpty(code)) {
//                    if (!usercode.equals(code)) {//是否用户填入的验证码和缓存中的验证码一致
//                        throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
//                    }
//                }
//            }
//
//
//            dto.put("is_valid", "1");
//
//            tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");
//            TUser tUser = tUserMapper.selectByExample(tUserExample).get(0);
//            //T_userPO userPO = t_userDao.selectOne(dto);
//            if (SystemUtils.isEmpty(tUser)) {
//                throw new ApiException(ReqEnums.REQ_NO_USER.getCode(), ReqEnums.REQ_NO_USER.getMsg());
//            }
//            user_id = tUser.getId();
//
//
//        } else if (type.equals("1")) {
//            String password = Des3Utils.encode(dto.getString("password"));
//            //匹配账号密码
//            /*Dto dto1 = new HashDto();
//            dto1.put("user_name", user_name);
//            dto1.put("is_valid", "1");
//            dto1.put("password", password);*/
//            tUserExample.or().andPasswordEqualTo(password).andUserNameEqualTo(user_name)
//                    .andIsValidEqualTo("1");
//            TUser tUser = tUserMapper.selectByExample(tUserExample).get(0);
//            tUserExample.clear();
//            if (tUser == null) {
//                //用户名或密码错误
//                throw new ApiException(ReqEnums.REQ_PHONE_OR_PASSWORD_ERROR.getCode(), ReqEnums.REQ_PHONE_OR_PASSWORD_ERROR.getMsg());
//            }
//
//            user_id = tUser.getId();
//        }
//        //通过appId获取物业ID
//        String property_id = tPropertyExt.getPropertyIdByAppId(appid);
//        tokenDto.put("app_id", appid);
//        tokenDto.put("user_name", user_name);
//        tokenDto.put("user_id", user_id);
//        tokenDto.put("property_id", property_id);
//        String token = ApiUtils.getToken(tokenDto);
//
//        resDto.put("user_name", user_name);
//        resDto.put("user_id", user_id);
//        resDto.put("token", token);
//        resDto.put("is_login", "1");
//        msg.setData(Des3Utils.encResponse(resDto));
//        //验证成功后需要将redis中保存的验证码清除
//        //ru.del(KEY+user_name);
//        return msg;

    }


}



