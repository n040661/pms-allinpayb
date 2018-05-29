package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.app.service.SocialActivityService;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangsuliang on 2018/4/24.
 */
@RestController("app.socialActivityBackendController")
@RequestMapping("/backend/v1/activity")
public class SocialActivityBackendController {
    private RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private SocialActivityService socialActivityService;

    @GetMapping
    public ApiMessage get(@RequestParam String paramStr){
        try {
            Dto dto = JsonUtils.toDto( paramStr );
            return this.socialActivityService.get( dto );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }


    @PostMapping("/adminList")
    public ApiMessage adminList(@RequestParam String paramStr){
        try {
            Dto dto = JsonUtils.toDto( paramStr );
            return socialActivityService.adminList( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 活动审核
     * @param paramStr
     * @return
     */
    @PostMapping("/check")
    public ApiMessage check(@RequestParam String paramStr){
        try {
            Dto dto = JsonUtils.toDto( paramStr );
            String token = dto.getString( "token" );
            Dto tokenDto =(Dto) ru.getObject( token );
            String userId = tokenDto.getString("userId");
            String propertyId = tokenDto.getString( "propertyId" );
            dto.put( "userId",userId );
            dto.put( "propertyId",propertyId );
            return socialActivityService.check( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }

    }

    /**
     * 活动审核
     * @param request
     * @return
     */
    @PostMapping("/checkList")
    public ApiMessage checkList(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            String propertyId = JwtToken.getString( token, "propertyId" );
            dto.put( "userId",userId );
            dto.put( "propertyId",propertyId );
            return socialActivityService.checkList( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }

    }
}
