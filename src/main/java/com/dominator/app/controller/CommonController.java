package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.enums.ReqEnums;
import com.dominator.service.CommonService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.sms.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController("app.commonController")
@RequestMapping("/app/v1/common")
public class CommonController {

    @Autowired
    private CommonService commonService;
    /**
     * 发送验证码短信
     * <param>
     * phoneNum String 必传 手机号
     *
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/code")
    public ApiMessage postCode(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            String userName = dto.getString("phoneNum");
            SmsUtils.sendSms(userName);
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
     * 检查验证码
     * <param>
     * phoneNum String 必传 手机号即登录账号
     * userCode String 必传 验证码
     *
     * @return
     */
    @PostMapping("/verifyCode")
    public ApiMessage verifyCode(HttpServletRequest request) {
        try {
            Dto dto = ApiUtils.getParams(request);
            ApiUtils.checkParam(dto, "userCode,phoneNum");
            String phoneNum = dto.getString("phoneNum");
            String userCode = dto.getString("userCode");
            dto.put("userName", phoneNum);
            dto.put("userCode", userCode);
            SmsUtils.checkSms(dto);
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
     * 上传图片
     *
     * @param uploadPicture
     * @return
     */
    @PostMapping("/uploadPicture")
    public ApiMessage uploadPicture(@RequestParam(value = "uploadPicture") MultipartFile uploadPicture) {
        ApiMessage msg;
        try {
            msg = commonService.uploadPicture(uploadPicture);
            System.out.println(msg);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;
    }
}
