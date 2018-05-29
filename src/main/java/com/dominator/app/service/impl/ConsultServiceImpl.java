package com.dominator.app.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dominFramework.core.typewrap.Dto;
import com.dominator.app.service.ConsultService;
import com.dominator.entity.TConsult;
import com.dominator.entity.TConsultAppointment;
import com.dominator.entity.TConsultExample;
import com.dominator.mapper.TConsultAppointmentMapper;
import com.dominator.mapper.TConsultMapper;
import com.dominator.redis.async.EventModel;
import com.dominator.redis.async.EventProducer;
import com.dominator.redis.async.EventType;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.dominator.enums.ReqEnums.REQ_UPDATE_ERROR;

@Service("app.consultServiceImpl")
public class ConsultServiceImpl implements ConsultService {

    @Autowired
    private TConsultMapper tConsultMapper;

    @Autowired
    private TConsultAppointmentMapper tConsultAppointmentMapper;

    @Autowired
    private EventProducer eventProducer;

    private RedisUtil ru = RedisUtil.getRu();

    @Override
    public Dto list(Dto dto) {
        String propertyId = dto.getString("propertyId");
        String sort = dto.getString("sort");
        String field = dto.getString("field");
        Page.startPage(dto);
        TConsultExample tConsultExample = new TConsultExample();
        TConsultExample.Criteria criteria = tConsultExample.createCriteria();

        criteria.andPropertyIdEqualTo(propertyId).andIsValidEqualTo("1").andStatusEqualTo("1");
        if (StringUtils.isNotEmpty(sort)&&StringUtils.isNotEmpty(field)){
            tConsultExample.setOrderByClause(field+" "+sort);
        }else{
            tConsultExample.setOrderByClause("create_time desc");
        }

        List<TConsult> list = tConsultMapper.selectByExample(tConsultExample);

        Dto result = Page.resultPage(list);
        return result;
    }

    @Override
    public TConsult detail(Dto dto) {
        String id = dto.getString("id");
        TConsult tConsult = tConsultMapper.selectByPrimaryKey(id);
        return tConsult;
    }

    @Override
    @Transactional
    public ApiMessage appointment(Dto dto) throws ApiException{
        String phoneNum = dto.getString("phoneNum");
        String id = dto.getString("id");
        String name = dto.getString("name");

        //落地到咨询预约表中
        TConsultAppointment tConsultAppointment = new TConsultAppointment();
        String uuid = PrimaryGenerater.getInstance().uuid();
        tConsultAppointment.setId(uuid);
        tConsultAppointment.setConsultId(id);
        tConsultAppointment.setPhoneNum(phoneNum);
        tConsultAppointment.setName(name);
        tConsultAppointment.setCreateTime(new Date());
        tConsultAppointment.setIsValid("1");

        int i = tConsultAppointmentMapper.insertSelective(tConsultAppointment);
        if (i!=1)
            throw new ApiException(REQ_UPDATE_ERROR.getCode(),REQ_UPDATE_ERROR.getMsg());
        //发送异步短信
        String consultId = PrimaryGenerater.getInstance().uuid();
        ru.setObject(consultId,dto);
        eventProducer.fireEvent(new EventModel(EventType.CONSULT).setExt("consult", consultId));

        return ApiMessageUtil.success();
    }

}
