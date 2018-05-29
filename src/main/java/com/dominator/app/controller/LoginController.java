package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.enums.ReqEnums;
import com.dominator.app.service.LoginService;
import com.dominator.mapper.TUserMapper;
import com.dominator.service.CompanyService;
import com.dominator.service.GardenService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController("app.loginController")
@RequestMapping("/app/v1/login")
@Slf4j
public class LoginController {

    private static RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private LoginService loginService;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private CompanyService companyService;

    /**
     * 登录生成token（验证码、密码）
     * <param>
     * propertyId
     * userName 用户名（手机号）
     * code     验证码
     * password 密码
     */
    @PostMapping
    public ApiMessage login(HttpServletRequest request, HttpServletResponse response) {
        try {
            Dto dto = ApiUtils.getParams(request);
            ApiUtils.checkParam(dto, "propertyId,userName");
            Dto resDto = loginService.login(dto);
            String token = resDto.getString("token");
            response.setHeader("token", token);
            resDto.remove("token");
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 获取登录后的选择列表
     *
     * @param request
     * @return
     */
    @GetMapping
    public ApiMessage getLogin(HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            String propertyId = JwtToken.getString(token, "propertyId");
            String userId = JwtToken.getString(token, "userId");
            Dto dto = new HashDto();
            dto.put("token", token);
            dto.put("propertyId", propertyId);
            dto.put("userId", userId);
            Dto resDto = loginService.login(dto);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 选择园区/企业进入
     * <header>
     * token
     * </header>
     * <param>
     * gardenId
     * companyId  --可为空
     */
    @PutMapping
    public ApiMessage select(HttpServletRequest request, HttpServletResponse response) {
        try {
            Dto dto = ApiUtils.getParams(request);
            ApiUtils.checkParam(dto, "gardenId");
            String token = request.getHeader("token");
            String userName = JwtToken.getString(token, "userName");
            String userId = JwtToken.getString(token, "userId");
            dto.put("userName", userName);
            dto.put("userId", userId);
            dto.put("token", token);
            Dto resDto = loginService.select(dto);

            token = resDto.getString("token");
            response.setHeader("token", token);
            resDto.remove("token");
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

//    /**
//     * 获取可切换的列表
//     * <header>
//     * token
//     * </header>
//     */
//    @GetMapping("/list")
//    public ApiMessage getLoginList(HttpServletRequest request) {
//        try {
//            String token = request.getHeader("token");
//            String userId = JwtToken.getString(token, "userId");
//            String propertyId = JwtToken.getString(token, "propertyId");
//            Dto resDto = loginService.getLoginList(propertyId, tUserMapper.selectByPrimaryKey(userId), new HashDto());
//            return ApiMessageUtil.success(resDto);
//        } catch (ApiException e) {
//            e.printStackTrace();
//            return new ApiMessage(e);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ApiMessage(ReqEnums.SYS_ERROR);
//        }
//    }

    /**
     * 登出
     * <header>
     * token
     * </header>
     */
    @DeleteMapping
    public ApiMessage logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            String userName = JwtToken.getString(token, "userName");
            ru.del("jwt_" + userName);
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


