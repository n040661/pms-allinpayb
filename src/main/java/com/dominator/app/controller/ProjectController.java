package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("app.ProjectController")
@RequestMapping("/app/v1/project")
public class ProjectController {
    @Autowired
    private com.dominator.app.service.ProjectService projectService;

    /**
     * 获取项目详情
     * <param>
     * projectId
     *
     * @return
     */
    @GetMapping("/detail")
    public ApiMessage projectDetail(HttpServletRequest request) {
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            msg = projectService.projectDetail(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
    /**
     * 获取项目列表
     * <param>
     * propertyId
     * pageNum  非必传 第几页
     * pageSize  非必传 每页几条
     *
     */
    @GetMapping("/list")
    public ApiMessage listProject(HttpServletRequest request){
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            //String token = request.getHeader("token");
            //String propertyId = JwtToken.getString(token, "propertyId");
            String propertyId = dto.getString("propertyId");
            dto.put("propertyId",propertyId);
            msg = projectService.listProject(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
}
