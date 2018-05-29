package com.dominator.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.app.service.ReservationHouseService;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("app.ReservationHouseController")
@RequestMapping("/app/v1/house")
public class ReservationHouseController {
    @Autowired
    private com.dominator.app.service.ReservationHouseService reservationHouseService;
    /**
     * 添加预约看房记录
     * <param>
     *     token
     *     visitTime 必传 参观时间
     *     contactName 必传 String 联系人
     *     contactPhone 必传 String 联系电话
     *     remarks 必传 String 备注
     * @return
     */
    @PostMapping
    public ApiMessage addHouse(HttpServletRequest request){
        ApiMessage msg;

        try {
            //Dto dto = Dtos.newInDto(request);
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String gardenId = JwtToken.getString(token, "gardenId");
            String userId = JwtToken.getString(token,"userId");
            dto.put("gardenId",gardenId);
            dto.put("userId",userId);
            msg = reservationHouseService.addHouse(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
    /**
     * 预约看房记录
     * <param>
     *     token
     *  pageNum  非必传 第几页
     *  pageSize  非必传 每页几条
     * @return
     *
     */
    @GetMapping
    public ApiMessage listHouse(HttpServletRequest request){
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String gardenId = JwtToken.getString(token, "gardenId");
            String userId = JwtToken.getString(token,"userId");
            dto.put("gardenId",gardenId);
            dto.put("userId",userId);
            msg = reservationHouseService.listHouse(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
    /**
     * 预约看房详情
     * <param>
     *     token
     *     id   预约看房id
     * @return
     */
    @GetMapping("details")
    public ApiMessage detailsHouse(HttpServletRequest request){
        ApiMessage msg;
        try {
            Dto dto =Dtos.newInDto(request);
            String token = request.getHeader("token");
            String gardenId = JwtToken.getString(token,"gardenId");
            dto.put("gardenId",gardenId);
            msg=reservationHouseService.detailsHouse(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }

}
