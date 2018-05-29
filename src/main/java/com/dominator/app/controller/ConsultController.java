package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.app.service.ConsultService;
import com.dominator.entity.TConsult;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController("app.businessConsultController")
@RequestMapping("/app/v1/consult")
public class ConsultController {

    @Autowired
    private ConsultService consultService;
    /**
     * 企业咨询列表接口
     * <param>
     *  token
     *  pageSize
     *  pageNum
     *  field
     *  sort
     *
     * @return
     * @throws
     */
    @GetMapping(value = "/list")
    public ApiMessage List(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String propertyId = JwtToken.getString(token,"propertyId");
            dto.put("propertyId",propertyId);
            Dto list = consultService.list(dto);
            return ApiMessageUtil.success(list);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 企业咨询详情
     * <param>
     *  token
     *  id
     * @return
     */
    @GetMapping(value = "/detail")
    public ApiMessage Detail(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            TConsult tConsult = consultService.detail(dto);
            return ApiMessageUtil.success(tConsult);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }


    /**
     * 企业咨询预约
     * <param>
     *  token
     *  id      咨询企业的id
     *  phoneNum 企业咨询人的联系手机号
     *  name     预约人名称
     * @return
     */
    @PostMapping (value = "/appointment")
    public ApiMessage Appointment(HttpServletRequest request) {
        try {
            //Dto dto = Dtos.newInDto(request);
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String propertyId = JwtToken.getString(token,"propertyId");
            String userName = JwtToken.getString(token,"userName");
            dto.put("propertyId",propertyId);
            dto.put("userName",userName);
            consultService.appointment(dto);
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
