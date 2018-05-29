package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TCompanyBill;
import com.dominator.utils.api.ApiMessage;

import java.math.BigDecimal;
import java.util.List;

public interface BillService {
    ApiMessage getBillAll(Dto dto);

    ApiMessage billbymonth(Dto dto);

    ApiMessage billdetail(Dto dto);

    ApiMessage billallbycompanyid(Dto dto);

    ApiMessage listBillsByPage(Dto dto);

    Dto payOverview(Dto dto);

    /**
     * 获取公司账单总额
     *
     * @param dto companyId
     *            isPaid
     */
    List<TCompanyBill> listCompanyBill(Dto dto);

    ApiMessage overview(Dto dto);
}
