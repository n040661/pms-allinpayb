package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.TUser;
import com.dominator.entity.TUserExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TUserMapper;
import com.dominator.service.RegisterService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Constants;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TUserMapper tUserMapperer;

    private static final String KEY = "CheckMessage";

    private static RedisUtil ru = RedisUtil.getRu();

    /**
     * h5注册
     *
     * @param dto user_name 用户名(即用户手机号)
     *            usercode  验证码
     *            password  密码
     * @return
     */
    @Transactional
    @Override
    public ApiMessage register(Dto dto) throws ApiException {
        try {
            String user_name = dto.getString("user_name");
            Dto dto1 = new HashDto();
            dto1.put("user_name",user_name);
            dto1.put("is_valid", "1");
            //查看用户名是否已经存在
            TUserExample tUserExample = new TUserExample();
            tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");

            TUser tUser = tUserMapperer.selectByExample(tUserExample).get(0);
            tUserExample.clear();
            if (tUser != null && StringUtils.isNotEmpty(tUser.getPassword())) {
                throw new ApiException(ReqEnums.REQ_USER_REPEAT.getCode(), ReqEnums.REQ_USER_REPEAT.getMsg());
            }
            //验证验证码
            String usercode = dto.getString("user_code");

            String code = ru.get(KEY + user_name);
            if (SystemUtils.isEmpty(code)) {
                throw new ApiException(Constants.CODE_CHECK_PASS_ERROR, Constants.MSG_CHECK_PASS_ERROR);
            }

            if (!usercode.equals(code)) {
                throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
            }

            TUser userPO = new TUser();
            //添加现有员工的密码
            if (tUser != null) {
                userPO.setId(tUser.getId());
                userPO.setPassword(Des3Utils.encode(dto.getString("password")));
                int i = tUserMapperer.updateByPrimaryKey(userPO);
                if (i != 1) {
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
                }
            } else {
                //新增注册员工
                String user_id = UUID.randomUUID().toString().replace("-", "");
                log.info(user_id);
                userPO.setId(user_id);
                userPO.setPassword(Des3Utils.encode(dto.getString("password")));
                userPO.setUserName(dto.getString("user_name"));
                userPO.setNickName("用户" + dto.getString("user_name").substring(dto.getString("user_name").length() - 4, dto.getString("user_name").length()));
                userPO.setIsValid("1");
                userPO.setCreateTime(new Date());
                userPO.setLoginType("0");//表示是注册的虚拟员工
                log.info(dto.getString("user_name") + "||" + userPO.getNickName() + "||" + userPO.getId());
                int i = tUserMapperer.insert(userPO);
                if (i != 1) {
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
                }
            }

            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ApiException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }

    /**
     * h5忘记密码
     *
     * @param dto user_name 用户名(即用户手机号)
     *            usercode  验证码
     *            password  密码
     * @return
     */
    @Override
    public ApiMessage updatepassword(Dto dto) throws ApiException {
        //忘记密码 修改密码
        String user_name = dto.getString("user_name");
        /*Dto dto1 = new HashDto();
        dto1.put("user_name", user_name);
        dto1.put("is_valid", "1");*/
        //查看用户名是否已经存在
        TUserExample tUserExample = new TUserExample();
        tUserExample.or().andUserNameEqualTo(user_name).andIsValidEqualTo("1");

        TUser tUser = tUserMapperer.selectByExample(tUserExample).get(0);
        //T_userPO t_userPO = t_userDao.selectOne(dto1);
        tUserExample.clear();
        if (tUser == null) {
            throw new ApiException(ReqEnums.REQ_USER_NOT_EXSIT.getCode(), ReqEnums.REQ_USER_NOT_EXSIT.getMsg());
        }
        //验证验证码
        String usercode = dto.getString("user_code");

        String code = ru.get(KEY + user_name);
        if (SystemUtils.isEmpty(code)) {
            throw new ApiException(Constants.CODE_CHECK_PASS_ERROR, Constants.MSG_CHECK_PASS_ERROR);
        }

        if (!usercode.equals(code)) {
            throw new ApiException(Constants.CODE_CHECK_MSG_ERROR, Constants.MSG_CHECK_MSG_ERROR);
        }
        //修改注册员工密码
        TUser userPO = new TUser();
        String user_id = userPO.getId();
        log.info(user_id);
        userPO.setId(user_id);
        userPO.setPassword(Des3Utils.encode(dto.getString("password")));
        userPO.setModifyTime(new Date());
        int i = tUserMapperer.updateByPrimaryKey(userPO);
        if (i != 1) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }

        return ApiMessageUtil.success();
    }
}
