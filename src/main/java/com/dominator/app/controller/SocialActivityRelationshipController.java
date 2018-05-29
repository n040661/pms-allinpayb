package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.app.service.SocialActivityRelationshipService;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangsuliang on 2018/4/22.
 */
@RestController("app.socialActivityRelationshipController")
@RequestMapping("/app/v1/activity/relationship")
public class SocialActivityRelationshipController {

    @Autowired
    private SocialActivityRelationshipService socialActivityRelationshipService;

    /**
     * 报名接口
     *     token TOKEN
     *     saId 活动ID
     * @param request
     * @return
     */
    @PostMapping("/signUp")
    public ApiMessage signUp(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put( "userId",userId );
            return socialActivityRelationshipService.signUp( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }


    /**
     * 报名接口
     *     token TOKEN
     *     saId 活动ID
     * @param request
     * @return
     */
    @PostMapping("/subscribers")
    public ApiMessage subscribers(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put( "userId",userId );
            return socialActivityRelationshipService.getSubscribers( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }


    /**
     * 报名接口
     *     token TOKEN
     *     saId 活动ID
     * @param request
     * @return
     */
    @PostMapping("/myActivities")
    public ApiMessage myActivities(HttpServletRequest request){
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put( "userId",userId );
            return socialActivityRelationshipService.getMyActivities( dto );
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage( ReqEnums.SYS_ERROR);
        }
    }

    @PostMapping("/isJoinActivity")
    public ApiMessage isJoinActivity(HttpServletRequest request){
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader( "token" );
            String userId = JwtToken.getString( token, "userId" );
            dto.put( "userId", userId );
            return socialActivityRelationshipService.isJoinActivity( dto );
        }catch (Exception e) {
                e.printStackTrace();
                return new ApiMessage( ReqEnums.SYS_ERROR);
            }
    }
}
