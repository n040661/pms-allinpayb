package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TGarden;
import com.dominator.entity.TUser;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.text.ParseException;
import java.util.List;

public interface GardenService {

    Dto listByUser(Dto dto);

    ApiMessage postGarden(Dto dto) throws ApiException;

    ApiMessage putGarden(Dto dto) throws ApiException;

    ApiMessage delGarden(Dto dto) throws ApiException;

    Dto listGardens(Dto dto);

    int checkGardenName(Dto dto);

    ApiMessage listWithoutManagerGarden(Dto dto);

    Dto getGarden(Dto dto);

    ApiMessage listGardenRoleModules(Dto dto);

    List<TUser> listGardenUserRole(Dto dto);

//    ApiMessage getGardenPayOverview(Dto dto);
//
//    ApiMessage getPayOverview(Dto dto) throws ParseException;
//
    /**
     * 园区端--物业缴费-账单列表
     *
     * @param dto garden_id String 必传 园区id
     *            fee_type String 必传  费用类型 0水 1电费 2煤气 3房租物业
     *            is_paid String 必传 0待缴费 1已缴费
     *            is_pushed String 非必传 0待推送，1已推送
     *            startTime datetime 非必传
     *            endTime datetime 非必传
     *            condition String 非必传 账单号/缴费单位
     *            pageNum int 非必传 第几页
     *            pageSize int 非必传 每页几条
     * @return
     */
    ApiMessage listBills(Dto dto);

    /**
     * 园区端--查询账单详情
     *
     * @param dto company_bill_id String 必传 账单id
     * @return
     */
    ApiMessage getBillDetail(Dto dto);
    /**
     * 园区端--添加账单
     *
     * @param dto fee_type String 必传 费用类型 0水 1电费 2煤气 3房租物业
     *            company_id String 必传 企业id
     *            bill_year_month String 必传 账单年月 例：2017-02
     *            other_fee double 必传 其他费用
     *            total_fee double 必传 总费用
     *            expiry_time datetime 必传 应缴日期
     *            push_time datetime 必传 推送时间
     *            main_fee double 非必传 主要费用
     *            fee_unit double 非必传 单位费用
     *            fee_dgree double 非必传 用量
     * @return
     */
    ApiMessage addBill(Dto dto) throws ApiException;
//
    /**
     * 园区端--删除账单
     *
     * @param dto company_bill_id String 必传 账单id
     *            modify_id String 必传 当前登录用户id
     * @return
     */
    ApiMessage delBill(Dto dto) throws ApiException;
    /**
     * 完成缴费
     *
     * @param dto company_bill_id String 必传 账单id
     *            modify_id String 必传 当前登录id
     * @return
     */
    ApiMessage pay(Dto dto) throws ApiException;

    /**
     * 园区端--根据园区id,企业名字查询
     *
     * @param dto garden_id String 必传 园区id
     *            company_name String 非必传 企业名
     * @return
     */
    ApiMessage listGardenCompanies(Dto dto);
}
