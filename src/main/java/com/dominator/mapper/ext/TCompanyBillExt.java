package com.dominator.mapper.ext;

import com.dominFramework.core.annotation.Dao;
import com.dominFramework.core.typewrap.Dto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
@Dao
public interface TCompanyBillExt {

    BigDecimal payOverview(Dto dto);

    List<Dto> listBillsByPage(Dto dto);

    void push();

    List<Dto>  selectpush(Dto dto);

    void push(@Param("company_id") String company_id);

    List<Dto> selectpush(@Param("company_id") String company_id);

    Dto selectPushByBillId(Dto dto);

    Dto getBillDetail(@Param("billId") String billId);


    List<Dto> listpageByorder(Dto dto);

    List<Dto> billbymonth(Dto dto);

    String getNotPaidTotal(String company_id);
}
