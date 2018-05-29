package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.entity.TAppointPublic;
import com.dominator.enums.ReqEnums;
import com.dominator.service.PublicService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController("app.publicController")
@RequestMapping("/app/v1/public")
public class PublicController {

    private static RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private PublicService publicService;

    /**
     * 公区服务列表
     * <header>
     * token
     * </header>
     * <p>
     * <body>
     * --可为空
     * title
     * isValid
     * type
     * pageSize
     * pageNum
     * </body>
     */
    @GetMapping("/list")
    public ApiMessage listPublic(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String propertyId = JwtToken.getString(token, "propertyId");
            dto.put("propertyId", propertyId);
            Dto resDto = publicService.listPublic(dto);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 获取单个公区服务详情
     * <header>
     * token
     * </header>
     * publicId
     */
    @GetMapping
    public ApiMessage getPublic(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            ApiUtils.checkParam(dto, "publicId");
            Dto resDto = publicService.getPublic(dto);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 获得会议室等占用时间
     * <header>
     * token
     * </header>
     * publicId
     * time    --可为空，默认当天
     */
    @GetMapping("/holdingTime")
    public ApiMessage getHoldingTime(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            ApiUtils.checkParam(dto, "publicId");
            List<Dto> list = publicService.getHoldingTime(dto);
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
     * 公区服务预约
     * <header>
     * token
     * </header>
     * <p>
     * <body>
     * publicId
     * startTime
     * endTime
     * status    --0:待支付, 1:已取消, 2:待使用, 3:已完成
     * price
     * </body>
     */
    @PostMapping("/order")
    public ApiMessage postOrder(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String propertyId = JwtToken.getString(token, "propertyId");
            String userId = JwtToken.getString(token, "userId");
            dto.put("propertyId", propertyId);
            dto.put("userId", userId);
            ApiUtils.checkParam(dto, "propertyId,publicId,startTime,endTime,status,price");
            String recordId = publicService.postOrder(dto);
            Dto resDto = new HashDto();
            resDto.put("recordId", recordId);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 公区服务修改订单状态
     * <header>
     * token
     * </header>
     * <p>
     * <body>
     * orderId
     * status    --0:待支付, 1:已取消, 2:待使用, 3:已完成
     * payType   --可为空 支付类型
     */
    @PutMapping("/order")
    public ApiMessage putOrder(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            ApiUtils.checkParam(dto, "orderId,status");
            publicService.putOrder(dto);
            return ApiMessageUtil.success();
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 我的预约列表
     * <header>
     * token
     * </header>
     * <p>
     * <body>
     * --可为空
     * type     -- 预约类型, 0:会议室名称, 1:SPA室名称, 2:总裁VIP室名称, 3:路演中心
     * status   -- 0:待支付, 1:已取消, 2:待使用, 3:已完成
     * pageNum
     * pageSize
     */
    @GetMapping("/orderList")
    public ApiMessage getOrderList(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String userId = JwtToken.getString(token, "userId");
            dto.put("userId", userId);
            Dto resDto = publicService.getOrderList(dto);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ApiMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiMessage(ReqEnums.SYS_ERROR);
        }
    }

    /**
     * 我的预约详情
     * <header>
     * token
     * </header>
     * <p>
     * <body>
     * orderId
     */
    @GetMapping("/order")
    public ApiMessage getOrder(HttpServletRequest request) {
        try {
            Dto dto = Dtos.newInDto(request);
            ApiUtils.checkParam(dto, "orderId");
            Dto resDto = publicService.getOrder(dto);
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
