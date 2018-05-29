package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.TBanner;
import com.dominator.enums.ReqEnums;
import com.dominator.service.BannerService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController("app.BannerController")
@RequestMapping("/app/v1/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取banner
     * <body>
     * propertyId
     * sorts
     * </body>
     */
    @GetMapping
    public ApiMessage getBanner(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String propertyId = dto.getString("propertyId");
            if(SystemUtils.isEmpty(propertyId)){
                String token = request.getHeader("token");
                propertyId = JwtToken.getString(token, "propertyId");
                dto.put("propertyId", propertyId);
            }
            ApiUtils.checkParam(dto, "propertyId,sorts");
            List<TBanner> list = bannerService.getBanner(dto);
            Dto resDto = new HashDto();
            resDto.put("list", list);
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
