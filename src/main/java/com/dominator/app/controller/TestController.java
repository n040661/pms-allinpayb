package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController("app.TestController")
@RequestMapping("/app/v1/test")
public class TestController {

    @GetMapping
    public ApiMessage testget(HttpServletRequest request) {
        Dto dto = Dtos.newInDto(request);
        return ApiMessageUtil.success(dto);
    }

    @PostMapping
    public ApiMessage testpost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Dto dto = ApiUtils.getParams(request);
            response.setHeader("dto", dto.toString());
            return ApiMessageUtil.success(dto);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    @PutMapping
    public ApiMessage testput(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            return ApiMessageUtil.success(dto);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    @DeleteMapping
    public ApiMessage testdel(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            return ApiMessageUtil.success(dto);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    @GetMapping("user")
    public ApiMessage getUser(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String user_id = JwtToken.getString(token, "user_id");
            dto.put("user_id", user_id);
            return ApiMessageUtil.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }
}
