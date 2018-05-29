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

@RestController("app.SpaceController")
@RequestMapping("/app/v1/space")
public class SpaceController {
    @Autowired
    private com.dominator.app.service.SpaceService spaceService;

    /**
     * 获取空间详情
     * <param>
     * token
     * spaceId
     * @return
     */
    @GetMapping("/detail")
    public ApiMessage spaceDetail(HttpServletRequest request) {
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            msg = spaceService.spaceDetail(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
    /**
     * 获取空间列表
     * <param>
     *  excellent --是否为优质,0:非优质 , 1:优质
     *  pageNum  非必传 第几页
     *  pageSize  非必传 每页几条
     * @return
     */
    @GetMapping("/list")
    public ApiMessage listSpace(HttpServletRequest request){
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            //String token = request.getHeader("token");
           // String propertyId = JwtToken.getString(token, "propertyId");
            //String propertyId = dto.getString("propertyId");
            //String gardenId = JwtToken.getString(token, "gardenId");
            //dto.put("gardenId",gardenId);
            //dto.put("propertyId",propertyId);
            msg = spaceService.listSpace(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
}
