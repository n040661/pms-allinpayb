package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.service.GardenService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("app.gardenController")
@RequestMapping("/app/v1/garden")
@Slf4j
public class GardenController {

    @Autowired
    private GardenService gardenService;

    /**
     * 获取当前物业的园区列表
     * <header>
     * token
     * </header>
     * <param>
     * propertyId
     *
     * --可为空
     * isValid
     * gardenName
     *
     * token和propertyId必须传一个
     */
    @GetMapping("/list")
    public ApiMessage gardenList(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String propertyId = dto.getString("propertyId");
            if(SystemUtils.isEmpty(propertyId)){
                String token = request.getHeader("token");
                propertyId = JwtToken.getString(token, "propertyId");
                dto.put("propertyId", propertyId);
            }
            ApiUtils.checkParam(dto, "propertyId");
            Dto resDto = gardenService.listGardens(dto);
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


