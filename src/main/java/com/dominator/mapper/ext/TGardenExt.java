package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TGarden;
import com.dominator.entity.TModules;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface TGardenExt {

    Dto getGarden(Dto dto);

    List<TGarden> listWithoutManagerGarden(Dto dto);

    List<Dto> listGardenRole(Dto dto);

    List<TModules> listRoleModules(Dto dto);

    Dto selectByGardenId(Dto dto);

    List<Dto> listGarden(Dto dto);

    BigDecimal overviewByType(Dto dto);

    List<Dto> listBillsPage(Dto dto);

    List<Dto> listGardenCompanies(Dto dto);

    List<Dto> listGardenRepairPage(Dto dto);

    List<Dto> listGardenUser(Dto dto);

    List<Dto> listCompanyUnpaidInfoPage(Dto dto);

    int delCompanyBills(Dto dto);

    int delCompanyUser(Dto dto);

    int delCompanyUserRole(Dto dto);

    Integer countGardenUser(Dto dto);

    List<Dto> listPark(String appId);

    Dto getGardenById(String garden_id);

    List<Dto> listBills(Dto dto);

    List<Dto> listByUser(Dto dto);
}

