package com.dominator.mapper.ext;

import com.dominFramework.core.annotation.Dao;
import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TCompany;
import com.dominator.entity.TGarden;
import com.dominator.entity.TModules;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Dao
public interface TCompanyExt {
    Dto getCompany(Dto dto);

    List<Dto> listBills(Dto dto);
    List<Dto> listCompanyUnpaidInfoPage(Dto dto);

    List<Dto> listPage(Dto dto);

    List<Dto> list(Dto dto);

    BigDecimal getFee(Dto dto);

    Dto getCompanyId(@Param(value = "user_name") String user_name);

    List<Dto> listCompanyUsersPage(Dto dto);

    List<Dto> listCompanyRepairPage(Dto dto);

//    T_companyPO getCompanyByUserId(Dto dto);
    TCompany getCompanyByUserId(Dto dto);

    Integer countCompanyUser(Dto dto);

    BigDecimal getCompanyFee(Dto dto);

    Integer deleteAddress(Dto dto);

    Integer deleteCompanyUser(String company_id);

}

