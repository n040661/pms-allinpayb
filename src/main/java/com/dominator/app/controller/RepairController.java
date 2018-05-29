package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.app.service.RepairService;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("app.RepairController")
@RequestMapping("/app/v1/repair")
public class RepairController {
    @Autowired
    private RepairService repairService;

    /**
     * 获取报事报修的列表
     * <param>
     * token
     * pageNum
     * pageSize
     * repairStatus
     * sort
     * field
     *
     * @return
     */
    @GetMapping("/list")
    public ApiMessage list(HttpServletRequest request) {
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");

            String user_id = JwtToken.getString(token, "userId");
            String garden_id = JwtToken.getString(token, "gardenId");
            //String garden_id = dto.getString("gardenId");
            String user_name = JwtToken.getString(token, "userName");
            dto.put("userId", user_id);
            dto.put("gardenId", garden_id);
            dto.put("userName", user_name);

            String userId = JwtToken.getString(token, "userId");
            String gardenId = JwtToken.getString(token, "gardenId");
            //String garden_id = dto.getString("gardenId");
            String userName = JwtToken.getString(token, "userName");
            String companyId = JwtToken.getString(token,"companyId");
            dto.put("userId", userId);
            dto.put("gardenId", gardenId);
            dto.put("userName", userName);
            dto.put("companyId",companyId);

            msg = repairService.listpage(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 增加报事报修
     *<param>
     * token
     * repairContent
     * repairPics
     * type
     * spot
     * @return
     */
    @PostMapping
    public ApiMessage addRepair(HttpServletRequest request) {
        ApiMessage msg;

        try {
            Dto dto = ApiUtils.getParams(request);
            String token = request.getHeader("token");
            String gardenId = JwtToken.getString(token, "gardenId");
            String userId = JwtToken.getString(token, "userId");
            String phoneNum = JwtToken.getString(token, "userName");
            String companyId = JwtToken.getString(token, "companyId");

            dto.put("userId", userId);
            dto.put("gardenId", gardenId);
            dto.put("phoneNum", phoneNum);
            dto.put("companyId", companyId);
            msg = repairService.addrepair(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
    /**
     * 报修详情
     * <param>
     * token
     * repairId
     * @return
     */
    @GetMapping("/repairDetail")
    @ResponseBody
    public ApiMessage repairDetail(HttpServletRequest request){
        ApiMessage msg;
        try{
            Dto dto = Dtos.newInDto(request);
            msg = repairService.repairDetail(dto);
        }catch(ApiException e){
            msg = new ApiMessage(e.getCode(), e.getMessage());
        }catch(Exception e){
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 我的维修
     * @param
     * @return
     */
    @GetMapping("/myRepair")
    @ResponseBody
    public ApiMessage myRepair(HttpServletRequest request) {
        ApiMessage msg;

        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String gardenId = JwtToken.getString(token, "gardenId");
            String userId = JwtToken.getString(token, "userId");
            String phoneNum = JwtToken.getString(token, "userName");
            String companyId = JwtToken.getString(token, "companyId");
            dto.put("gardenId",gardenId);
            dto.put("userId",userId);
            dto.put("phoneNum",phoneNum);
            dto.put("companyId",companyId);
            msg = repairService.myrepair(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }


    /**
     * 确认完成报事报修
     * @param request  id
     * @return
     */
    @PostMapping("/completeRepair")
    @ResponseBody
    public ApiMessage completeRepair(HttpServletRequest request) {
        ApiMessage msg;

        try {
            Dto dto = ApiUtils.getParams(request);
            msg = repairService.completeRepair(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
}
