package com.dominator.app.controller;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.app.service.SocialActivityService;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangsuliang on 2018/4/19.
 */
@RestController("app.socialActivityController")
@RequestMapping("/app/v1/activity")
public class SocialActivityController {

    @Autowired
    private SocialActivityService socialActivityService;

    /**
     * 创建活动
     * @param request
     * @return
     */
    @PostMapping
    public ApiMessage create(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            String propertyId = JwtToken.getString( token, "propertyId" );
            String companyId = JwtToken.getString( token,"companyId" );
            dto.put( "userId",userId );
            dto.put( "companyId",companyId );
            dto.put( "propertyId",propertyId );
           return socialActivityService.create( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }

    @GetMapping
    public ApiMessage get(HttpServletRequest request){
        try {
             Dto dto = Dtos.newInDto(request);
            return socialActivityService.get( dto );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }

    @PutMapping
    public ApiMessage update(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put( "userId",userId );
            return socialActivityService.update( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }

    @PostMapping("/list")
    public ApiMessage activities(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put( "userId",userId );
           return socialActivityService.list( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }


    @PostMapping("/all")
    public ApiMessage allActivities(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            return socialActivityService.allActivities( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }

    @PostMapping("/checkStatus")
    public ApiMessage checkStatus(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            return socialActivityService.checkStatus( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }


    /**
     * 取消活动
     * @param request
     * @return
     */
    @PostMapping("/cancel")
    public ApiMessage cancel(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            String propertyId = JwtToken.getString( token, "propertyId" );
            dto.put( "userId",userId );
            dto.put( "propertyId",propertyId );
            return socialActivityService.cancel( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }

    }

}
