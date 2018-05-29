package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.TUser;
import com.dominator.enums.ReqEnums;
import com.dominator.service.UserService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("app.UserController")
@RequestMapping("/app/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private com.dominator.app.service.UserService userServiceApp;

    /**
     * 检查账户是否存在
     * <param>
     * userName
     *
     * @return 0不存在，其他存在
     */
    @GetMapping("/checkUserName")
    public ApiMessage checkUserName(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String userName = dto.getString("userName");
            int count = userService.checkUserName(userName);
            Dto resDto = new HashDto();
            resDto.put("count", count);
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
     * 根据手机验证码，重置密码
     * <p>
     * <param>
     * userName String 必传 用户名
     * password String 必传 新密码
     *
     * @return
     */
    @PostMapping("/resetPassword")
    public ApiMessage resetPassword(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            ApiUtils.checkParam(dto, "userName,password");
            return userService.resetPassword(dto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 获取当前用户信息及公司园区物业信息
     * <header>
     * token
     * </header>
     */
    @GetMapping
    public ApiMessage getInfo(HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            String propertyId = JwtToken.getString(token, "propertyId");
            String gardenId = JwtToken.getString(token, "gardenId");
            String companyId = JwtToken.getString(token, "companyId");
            String userId = JwtToken.getString(token, "userId");
            Dto dto = new HashDto();
            dto.put("propertyId", propertyId);
            dto.put("gardenId", gardenId);
            dto.put("companyId", companyId);
            dto.put("userId", userId);
            ApiUtils.checkParam(dto, "propertyId,userId");
            Dto resDto = userService.getInfo(dto);
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
     * 修改用户头像信息
     * <header>
     * token
     * </header>
     * <p>
     * headImg
     * nickName
     */
    @PutMapping
    public ApiMessage putUser(HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            userServiceApp.putUser(userId, request);
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 获取我的页面信息数量
     * <header>
     * token
     * </header>
     * pos   --pos1,pos2,pos3 三选一
     */
    @GetMapping("/msgamount")
    public ApiMessage getMsgAmount(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            //Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put("userId", userId);
            Dto resDto = userServiceApp.msgamount(dto);
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
     * 获取用户指定园区的企业列表，是否是园区员工
     * <p>
     * <header>
     * token
     * </header>
     * gardenId
     */
    @GetMapping("/listCompanyAndGarden")
    public ApiMessage listCompanyAndGarden(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put("userId", userId);
            Dto resDto = userServiceApp.listCompanyAndGarden(dto);
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
     * 获取当前用户的公司名称及角色
     * token
     */
    @GetMapping("/companyAndRole")
    public ApiMessage getCompanyAndRole(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            String companyId = JwtToken.getString(token, "companyId");
            String propertyId = JwtToken.getString(token, "propertyId");
            String gardenId = JwtToken.getString(token, "gardenId");
            dto.put("userId", userId);
            dto.put("companyId", companyId);
            dto.put("gardenId", gardenId);
            dto.put("propertyId", propertyId);
            Dto resDto = userServiceApp.getCompanyAndRole(dto);
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
     * 当前用户是否显示账单
     * token
     */
    @GetMapping("/showBill")
    public ApiMessage showBill(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            String companyId = JwtToken.getString(token, "companyId");
            dto.put("userId", userId);
            dto.put("companyId", companyId);
            Dto resDto = userServiceApp.showBill(dto);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }
}
