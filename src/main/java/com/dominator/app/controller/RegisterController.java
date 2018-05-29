package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.enums.ReqEnums;
import com.dominator.app.service.RegisterService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注册
 * 1.传手机号，发验证码
 * 2.验证手机与验证码是否匹配
 * 3.设置密码，名称
 * 4.检查账号，已存用户修改密码，调login接口，新用户调register接口
 * 5.进入首页
 */
@RestController("app.registerController")
@RequestMapping("/app/v1/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 注册，保存用户信息
     * <param>
     * userName
     * fullName
     * password
     *
     * @return
     */
    @PostMapping
    public ApiMessage register(HttpServletRequest request, HttpServletResponse response) {
        try {
            Dto dto = ApiUtils.getParams(request);
            ApiUtils.checkParam(dto, "userName,password");
            String token = registerService.register(dto);
            response.setHeader("token", token);
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }
}
