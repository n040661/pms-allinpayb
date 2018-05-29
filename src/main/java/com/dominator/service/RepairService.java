package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.utils.api.ApiMessage;
import org.springframework.web.multipart.MultipartFile;

public interface RepairService {
    ApiMessage listpage(Dto dto);

    ApiMessage addrepair(Dto dto);

    //ApiMessage updaterepair(Dto dto);



    ApiMessage addCompanyRepair(Dto dto);

    /**
     * 获取企业报事报修列表
     * @param dto  garden_id 园区id
     *                  company_user_id 公司用户id
     *                  pageNum 当前页
     *                  pageSize 每页条数
     * @return
     */
    ApiMessage companyListRepair(Dto dto);

    /**
     * 获取园区报事报修列表
     * @param dto garden_id 园区id
     *                  garden_user_id 园区员工id
     *                  pageNum 当前页
     *                  pageSize 每页条数
     *                  repair_status 报修状态(0待处理,1正在处理,2处理完成)
     *             create_time 起始时间
     *             end_time 截止时间
     * @return
     */
    ApiMessage gardenListRepair(Dto dto);

    /**
     * 获取园区员工列表
     * @param dto garden_id 园区id
     * @return
     */
    ApiMessage getGardenUser(Dto dto);

    /**
     * 指派员工处理报事报修
     * @param dto garden_user_id 园区员工id
     *            repair_id 报事报修id
     * @return
     */
    ApiMessage gardenUserToRepair(Dto dto);

    /**
     * 完成报事报修
     * @param dto repair_id 报事报修id
     *              company_user_phone 公司员工手机号
     * @return
     */
    ApiMessage CompleteRepair(Dto dto);

    /**
     * 报修详情
     * @param dto
     * @return
     */
    ApiMessage repairDetail(Dto dto);
    /**
     * 我的维修
     * @param
     * @return
     */
    ApiMessage myrepair(Dto dto);
}
