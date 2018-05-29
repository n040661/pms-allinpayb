package com.dominator.app.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.enums.ReqEnums;
import com.dominator.service.BillService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("app.billController")
@RequestMapping("/app/v1/bill")
public class BillController {

        @Autowired
        private BillService billService;

        /**
         * 获取所有账单信息，分页查询
         * @param request token
         *                 page_num
         *
         *
         * @return
         */
        @GetMapping(value = "list")
        public ApiMessage getBillAll(HttpServletRequest request) {
            ApiMessage msg;
            try {
                Dto dto = Dtos.newInDto(request);
                String token = request.getHeader("token");
                String userName = JwtToken.getString(token, "userName");
                String companyId = JwtToken.getString(token, "companyId");
                dto.put("userName",userName);
                dto.put("companyId",companyId);
                msg = billService.getBillAll(dto);
            } catch (ApiException e) {
                msg = new ApiMessage(e.getCode(), e.getMessage());
            } catch (Exception e) {
                msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
                e.printStackTrace();
            }
            return msg;

        }


        /**
         *根据公司id查询所有月份账单有无
         * @param request token
         */
        @GetMapping(value = "billAll")
        public ApiMessage billAllByCompanyId(HttpServletRequest request) {
            ApiMessage msg;
            try {
                Dto dto = Dtos.newInDto(request);
                String token = request.getHeader("token");
                String companyId = JwtToken.getString(token, "companyId");
                dto.put("companyId", companyId);
                msg = billService.billallbycompanyid(dto);
            } catch (ApiException e) {
                msg = new ApiMessage(e.getCode(), e.getMessage());
            } catch (Exception e) {
                msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
                e.printStackTrace();
            }
            return msg;


        }


        /**
         * 根据月份条件查询
         * @param request
         * @return
         */
        @GetMapping(value = "billByMonth")
        public ApiMessage billByMonth(HttpServletRequest request) {
            ApiMessage msg;
            try {
                Dto dto = Dtos.newInDto(request);
                String token = request.getHeader("token");
                String companyId = JwtToken.getString(token, "companyId");
                String userName = JwtToken.getString(token,"userName");
                dto.put("companyId", companyId);
                dto.put("userName",userName);
                msg = billService.billbymonth(dto);
            } catch (ApiException e) {
                msg = new ApiMessage(e.getCode(), e.getMessage());
            } catch (Exception e) {
                msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
                e.printStackTrace();
            }
            return msg;


        }

        /**
         * 账单详情
         * @param request
         * @return
         */
        @GetMapping(value = "detail")
        public ApiMessage billDetail(HttpServletRequest request) {
            ApiMessage msg;
            try {
                Dto dto = Dtos.newInDto(request);
                String token = request.getHeader("token");
                String gardenId = JwtToken.getString(token, "gardenId");
                dto.put("gardenId", gardenId);
                msg = billService.billdetail(dto);
            } catch (ApiException e) {
                msg = new ApiMessage(e.getCode(), e.getMessage());
            } catch (Exception e) {
                msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
                e.printStackTrace();
            }
            return msg;


        }


    /**
     * 账单概览
     * @param request
     * @return
     */
    @GetMapping(value = "overview")
    public ApiMessage overview(HttpServletRequest request) {
        ApiMessage msg;
        try {
            Dto dto = Dtos.newInDto(request);
            String token = request.getHeader("token");
            String companyId = JwtToken.getString(token, "companyId");
            dto.put("companyId", companyId);
            msg = billService.overview(dto);
        } catch (ApiException e) {
            msg = new ApiMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            msg = new ApiMessage(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg());
            e.printStackTrace();
        }
        return msg;


    }
    }


