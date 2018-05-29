package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TConsult;
import com.dominator.entity.TConsultExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TConsultMapper;
import com.dominator.service.ConsultService;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class ConsultServiceImpl implements ConsultService{

    @Autowired
    private TConsultMapper tConsultMapper;

    @Override
    @Transactional
    public String addConsult(Dto dto) throws ApiException{
        String name = dto.getString("name");
        String subtitle = dto.getString("subtitle");
        String contactName = dto.getString("contactName");
        String phoneNum = dto.getString("phoneNum");
        String detail = dto.getString("detail");
        String logo = dto.getString("logo");
        String pics = dto.getString("pics");
        String propertyId = dto.getString("propertyId");
        String id = PrimaryGenerater.getInstance().uuid();


        TConsult tConsult = new TConsult();
        tConsult.setId(id);
        tConsult.setName(name);
        tConsult.setSubtitle(subtitle);
        tConsult.setContactName(contactName);
        tConsult.setPhoneNum(phoneNum);
        tConsult.setDetail(detail);
        tConsult.setLogo(logo);
        tConsult.setPics(pics);
        tConsult.setPropertyId(propertyId);
        tConsult.setStatus("1");
        tConsult.setCreateTime(new Date());
        tConsult.setIsValid("1");

        int i = tConsultMapper.insert(tConsult);
        if (i!=1)
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(),ReqEnums.REQ_UPDATE_ERROR.getMsg());

        return id;
    }

    @Override
    @Transactional
    public String updateConsult(Dto dto) {
        String name = dto.getString("name");
        String subtitle = dto.getString("subtitle");
        String contactName = dto.getString("contactName");
        String phoneNum = dto.getString("phoneNum");
        String detail = dto.getString("detail");
        String logo = dto.getString("logo");
        String pics = dto.getString("pics");
        String propertyId = dto.getString("propertyId");
        String id = dto.getString("id");
        String status = dto.getString("status");
        String userId = dto.getString("userId");

        TConsult tConsult = new TConsult();
        tConsult.setId(id);
        if (StringUtils.isNotEmpty(name))
            tConsult.setName(name);
        if (StringUtils.isNotEmpty(subtitle))
            tConsult.setSubtitle(subtitle);
        if (StringUtils.isNotEmpty(contactName))
            tConsult.setContactName(contactName);
        if (StringUtils.isNotEmpty(phoneNum))
            tConsult.setPhoneNum(phoneNum);
        if (StringUtils.isNotEmpty(detail))
            tConsult.setDetail(detail);
        if (StringUtils.isNotEmpty(logo))
            tConsult.setLogo(logo);
        if (StringUtils.isNotEmpty(pics))
            tConsult.setPics(pics);
        tConsult.setPropertyId(propertyId);
        tConsult.setStatus(status);
        tConsult.setModifyId(userId);
        tConsult.setModifyTime(new Date());

        int i = tConsultMapper.updateByPrimaryKeySelective(tConsult);
        if (true)
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(),ReqEnums.REQ_UPDATE_ERROR.getMsg());

        return id;
    }

    @Override
    public String deleteConsult(Dto dto) {
        String id = dto.getString("id");
        int i = tConsultMapper.deleteByPrimaryKey(id);
        if (i!=1)
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(),ReqEnums.REQ_UPDATE_ERROR.getMsg());
        return id;
    }

    @Override
    public TConsult detailConsult(Dto dto) {
        String id = dto.getString("id");
        TConsult tConsult = tConsultMapper.selectByPrimaryKey(id);
        return tConsult;
    }

    @Override
    public List<TConsult> list(Dto dto) {
        String propertyId = dto.getString("propertyId");
        String condition = dto.getString("condition");
        String field = dto.getString("field");
        String sort = dto.getString("sort");

        Page.startPage(dto);
        TConsultExample tConsultExample = new TConsultExample();
        TConsultExample.Criteria criteria = tConsultExample.createCriteria();
        criteria.andPropertyIdEqualTo(propertyId).andIsValidEqualTo("1");
        if (StringUtils.isNotEmpty(condition)){
            tConsultExample.or().andNameLike("%"+condition+"%");
        }
        if (StringUtils.isNotEmpty(field) && StringUtils.isNotEmpty(sort)) {
            tConsultExample.setOrderByClause(field + " " + sort);
        }else{
            tConsultExample.setOrderByClause("create_time desc");
        }

        List<TConsult> list = tConsultMapper.selectByExample(tConsultExample);
        PageInfo<TConsult> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();

        return list;
    }
}
