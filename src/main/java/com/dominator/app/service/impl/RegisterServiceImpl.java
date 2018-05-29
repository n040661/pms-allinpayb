package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.service.UserService;
import com.dominator.entity.TUser;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TUserMapper;
import com.dominator.app.service.RegisterService;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import com.dominator.utils.system.PrimaryGenerater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("app.RegisterServiceImpl")
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public String register(Dto dto) throws ApiException {
        String userName = dto.getString("userName");
        String fullName = dto.getString("fullName");
        if(SystemUtils.isEmpty(fullName))
            fullName = userName;
        String password = dto.getString("password");
        String nickName = fullName;
        int count;
        try {
            count = userService.checkUserName(userName);
            if(count > 0)
                throw new ApiException(ReqEnums.REQ_USER_REPEAT);

            TUser user = new TUser();
            user.setId(PrimaryGenerater.getInstance().uuid());
            user.setUserName(userName);
            user.setFullName(fullName);
            user.setNickName(nickName);
            user.setPassword(Des3Utils.encode(password));
            user.setCreateTime(new Date());
            user.setIsValid("1");
            count = tUserMapper.insertSelective(user);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);

            return JwtToken.createToken(dto);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.SYS_ERROR);
        }
    }
}
