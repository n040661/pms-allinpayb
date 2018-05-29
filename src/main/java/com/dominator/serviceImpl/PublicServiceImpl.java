package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.app.service.AlipayService;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TAddressMapper;
import com.dominator.mapper.TAppointPublicMapper;
import com.dominator.mapper.TAppointPublicRecordMapper;
import com.dominator.mapper.TblOtosaasOrderMapper;
import com.dominator.mapper.ext.TPublicExt;
import com.dominator.service.PublicService;
import com.dominator.utils.dao.MsgRedisUtil;
import com.dominator.utils.dateutil.DateUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class PublicServiceImpl implements PublicService {

    private static MsgRedisUtil msgRu = MsgRedisUtil.getRu();

    @Autowired
    private TAppointPublicMapper tAppointPublicMapper;

    @Autowired
    private TPublicExt tPublicExt;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private TblOtosaasOrderMapper tblOtosaasOrderMapper;


    @Autowired
    private TAppointPublicRecordMapper tAppointPublicRecordMapper;

    @Override
    @Transactional
    public void postPublic(Dto dto) throws ApiException {
        String id = PrimaryGenerater.getInstance().uuid();
        String propertyId = dto.getString("propertyId");
        String gardenId = "";
        String title = dto.getString("title");
        String building = dto.getString("building");
        String floors = dto.getString("floors");
        String roomNumber = dto.getString("roomNumber");
        String peopleNumber = dto.getString("peopleNumber");
        String openMode = dto.getString("openMode");
        BigDecimal price = dto.getBigDecimal("price");
        String unitTime = dto.getString("unitTime");
        String description = dto.getString("description");
        String freeEquipment = dto.getString("freeEquipment");
        String type = dto.getString("type");
        String province = dto.getString("province");
        String city = dto.getString("city");
        String area = dto.getString("area");
        String street = dto.getString("street");
        String remarks = dto.getString("remarks");
        String picUrl = dto.getString("picUrl");
        String isValid = dto.getString("isValid");
        int count;

        try {
            TAppointPublic appointPublic = new TAppointPublic();
            appointPublic.setId(id);
            appointPublic.setGardenId(gardenId);
            appointPublic.setPropertyId(propertyId);
            appointPublic.setTitle(title);
            appointPublic.setBuilding(building);
            appointPublic.setFloors(floors);
            appointPublic.setRoomNumber(roomNumber);
            appointPublic.setPeopleNumber(peopleNumber);
            appointPublic.setOpenMode(openMode);
            appointPublic.setPrice(price);
            appointPublic.setUnitTime(unitTime);
            appointPublic.setDescription(description);
            appointPublic.setFreeEquipment(freeEquipment);
            appointPublic.setType(type);
            appointPublic.setRemarks(remarks);
            appointPublic.setPicUrl(picUrl);
            appointPublic.setCreateTime(new Date());
            appointPublic.setIsValid(isValid);
            count = tAppointPublicMapper.insertSelective(appointPublic);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "appointPublic");

            TAddress address = new TAddress();
            address.setId(PrimaryGenerater.getInstance().uuid());
            address.setOwnerId(id);
            address.setProvince(province);
            address.setCity(city);
            address.setArea(area);
            address.setStreet(street);
            address.setIsValid("1");
            count = tAddressMapper.insertSelective(address);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "address");
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void putPublic(Dto dto) throws ApiException {
        String id = dto.getString("publicId");
        String propertyId = dto.getString("propertyId");
        String gardenId = "";
        String titile = dto.getString("titile");
        String building = dto.getString("building");
        String floors = dto.getString("floors");
        String roomNumber = dto.getString("roomNumber");
        String peopleNumber = dto.getString("peopleNumber");
        String openMode = dto.getString("openMode");
        BigDecimal price = dto.getBigDecimal("price");
        String unitTime = dto.getString("unitTime");
        String description = dto.getString("description");
        String freeEquipment = dto.getString("freeEquipment");
        String type = dto.getString("type");
        String province = dto.getString("province");
        String city = dto.getString("city");
        String area = dto.getString("area");
        String street = dto.getString("street");
        String remarks = dto.getString("remarks");
        String picUrl = dto.getString("picUrl");
        String modifyId = dto.getString("modifyId");
        String isValid = dto.getString("isValid");
        int count;

        try {
            TAppointPublic appointPublic = new TAppointPublic();
            appointPublic.setId(id);
            appointPublic.setGardenId(gardenId);
            appointPublic.setPropertyId(propertyId);
            appointPublic.setTitle(titile);
            appointPublic.setBuilding(building);
            appointPublic.setFloors(floors);
            appointPublic.setRoomNumber(roomNumber);
            appointPublic.setPeopleNumber(peopleNumber);
            appointPublic.setOpenMode(openMode);
            appointPublic.setPrice(price);
            appointPublic.setUnitTime(unitTime);
            appointPublic.setDescription(description);
            appointPublic.setFreeEquipment(freeEquipment);
            appointPublic.setType(type);
            appointPublic.setRemarks(remarks);
            appointPublic.setPicUrl(picUrl);
            appointPublic.setModifyTime(new Date());
            appointPublic.setModifyId(modifyId);
            appointPublic.setIsValid(isValid);
            count = tAppointPublicMapper.updateByPrimaryKeySelective(appointPublic);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "appointPublic");

            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(id)
                    .andIsValidEqualTo("1");
            TAddress address = new TAddress();
            address.setProvince(province);
            address.setCity(city);
            address.setArea(area);
            address.setStreet(street);
            count = tAddressMapper.updateByExampleSelective(address, addressExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "address");
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public Dto listPublic(Dto dto) {
        String propertyId = dto.getString("propertyId");
        String title = dto.getString("title");
        String isValid = dto.getString("isValid");
        String type = dto.getString("type");
        List<Dto> list = new ArrayList<>();

        Page.startPage(dto);
        TAppointPublicExample appointPublicExample = new TAppointPublicExample();
        TAppointPublicExample.Criteria criteria = appointPublicExample.createCriteria();
        criteria.andPropertyIdEqualTo(propertyId);
        if (!SystemUtils.isEmpty(title))
            criteria.andTitleLike("%" + title + "%");
        if (!SystemUtils.isEmpty(isValid))
            criteria.andIsValidEqualTo(isValid);
        if (!SystemUtils.isEmpty(type))
            criteria.andTypeEqualTo(type);
        appointPublicExample.setOrderByClause(" create_time desc");
        List<TAppointPublic> publicList = tAppointPublicMapper.selectByExample(appointPublicExample);
        if (publicList != null && !SystemUtils.isEmpty(publicList)) {
            for (TAppointPublic appointPublic : publicList) {
                Dto dto1 = new HashDto();
                dto1.put("publicId", appointPublic.getId());
                dto1.put("time", System.currentTimeMillis());
                List<Dto> timeList = getHoldingTime(dto1);

                dto1 = JsonUtils.toDto(JsonUtils.toJson(appointPublic));
                dto1.put("holdingTime", timeList);

                list.add(dto1);
            }
        }

        PageInfo<TAppointPublic> pageInfo = new PageInfo<>(publicList);
        Dto resDto = new HashDto();
        resDto.put("list", list);
        resDto.put("total", pageInfo.getTotal());
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("pageNum", pageInfo.getPageNum());
        return resDto;
    }

    @Override
    public Dto getPublic(Dto dto) {
        String publicId = dto.getString("publicId");
        Dto resDto = new HashDto();

        TAppointPublic appointPublic = tAppointPublicMapper.selectByPrimaryKey(publicId);
        if (appointPublic != null) {
            resDto = JsonUtils.toDto(JsonUtils.toJson(appointPublic));
            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(publicId)
                    .andIsValidEqualTo("1");
            List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
            if (addressList != null && !addressList.isEmpty())
                resDto.put("address", addressList.get(0));

            Dto dto1 = new HashDto();
            dto1.put("publicId", publicId);
            dto1.put("time", System.currentTimeMillis());
            List<Dto> timeList = getHoldingTime(dto1);
            resDto.put("holdingTime", timeList);
        }
        return resDto;
    }

    @Override
    @Transactional
    public void delPublic(Dto dto) throws ApiException {
        String publicId = dto.getString("publicId");
        int count;

        try {
            TAppointPublicExample appointPublicExample = new TAppointPublicExample();
            appointPublicExample.or().andIdEqualTo(publicId)
                    .andIsValidEqualTo("0");
            count = tAppointPublicMapper.deleteByExample(appointPublicExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);

            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(publicId);
            tAddressMapper.deleteByExample(addressExample);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public List<Dto> getHoldingTime(Dto dto) {
        String publicId = dto.getString("publicId");
        Long time = dto.getLong("time") == null ? new Date().getTime() : dto.getLong("time");
        Date startTime = DateUtil.getStartTime(time);
        Date endTime = DateUtil.getEndTime(time);
        List<Dto> list = new ArrayList<>();

        TAppointPublicRecordExample appointPublicRecordExample = new TAppointPublicRecordExample();
        appointPublicRecordExample.or().andStartTimeGreaterThan(startTime)
                .andEndTimeLessThan(endTime)
                .andPublicIdEqualTo(publicId)
                .andStatusIn(Arrays.asList("0,2".split(",")))
                .andIsValidEqualTo("1");
        List<TAppointPublicRecord> recordList = tAppointPublicRecordMapper.selectByExample(appointPublicRecordExample);
        if (recordList != null && !SystemUtils.isEmpty(recordList)) {
            for (TAppointPublicRecord record : recordList) {
                Dto dto1 = new HashDto();
                dto1.put("startTime", record.getStartTime().getTime());
                dto1.put("endTime", record.getEndTime().getTime());
                list.add(dto1);
            }
        }
        return list;
    }

    @Override
    @Transactional
    public String postOrder(Dto dto) throws ApiException {
        String propertyId = dto.getString("propertyId");
        String userId = dto.getString("userId");
        String publicId = dto.getString("publicId");
        String status = dto.getString("status");
        Long startTime = dto.getLong("startTime");
        Long endTime = dto.getLong("endTime");
        BigDecimal price = dto.getBigDecimal("price");
        float hours = (endTime - startTime) / (1000 * 60 * 60);
        BigDecimal orderAmount = price.multiply(new BigDecimal(hours));
        String recordId = PrimaryGenerater.getInstance().uuid();
        Date now = new Date();
        int count;

        try {
            TAppointPublicRecord record = new TAppointPublicRecord();
            record.setId(recordId);
            record.setPropertyId(propertyId);
            record.setUserId(userId);
            record.setPublicId(publicId);
            record.setStartTime(new Date(startTime));
            record.setEndTime(new Date(endTime));
            record.setOrderAmount(orderAmount);
            record.setPrice(price);
            record.setHours(hours + "");
            record.setStatus(status);
            record.setCreateTime(now);
            record.setIsValid("1");
            count = tAppointPublicRecordMapper.insertSelective(record);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
            TAppointPublic appointPublic = tAppointPublicMapper.selectByPrimaryKey( publicId );
            String type = appointPublic.getType();
            String prodSubType="";
            String body ="";
            switch(type){
                case "0":
                    body="meeting";
                    prodSubType="meeting";
                    break;
                case "1":
                    body="SPA";
                    prodSubType="SPA";
                    break;
                case "2":
                    body="VIP";
                    prodSubType="VIP";
                    break;
                case "3":
                    body="roadShow";
                    prodSubType="roadShow";
                    break;
            }
            Dto orderDto = new HashDto();
            orderDto.put( "serviceId",recordId);
            orderDto.put( "prodType","PS" );
            orderDto.put( "body",body );
            orderDto.put( "prodSubType",prodSubType );
            BigDecimal multi = new BigDecimal("100");
            orderDto.put( "totalFee", orderAmount.multiply( multi ) );
            alipayService.save( orderDto );

            msgRu.zadd("cancelPublicOrder", now.getTime(), recordId);
            return recordId;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void putOrder(Dto dto) throws ApiException {
        String[] orderId = dto.getString("orderId").split(",");
        String status = dto.getString("status");
        String payType = dto.getString("payType");
        int count;

        try {
            if (orderId.length == 1) {
                TAppointPublicRecord record = new TAppointPublicRecord();
                record.setId(orderId[0]);
                record.setStatus(status);
                record.setPayType(payType);
                record.setModifyTime(new Date());
                count = tAppointPublicRecordMapper.updateByPrimaryKeySelective(record);
                if (count != 1)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);

                if (status.equals("2")) {
                    record = tAppointPublicRecordMapper.selectByPrimaryKey(orderId[0]);
                    Date endTime = record.getEndTime();
                    msgRu.zadd("completePublicOrder", endTime.getTime(), orderId[0]);
                }
            } else {
                TAppointPublicRecord record = new TAppointPublicRecord();
                record.setStatus(status);
                record.setPayType(payType);
                record.setModifyTime(new Date());
                TAppointPublicRecordExample appointPublicRecordExample = new TAppointPublicRecordExample();
                appointPublicRecordExample.or().andIdIn(Arrays.asList(orderId));
                count = tAppointPublicRecordMapper.updateByExampleSelective(record, appointPublicRecordExample);
                if (count == 0)
                    throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public Dto getOrderList(Dto dto) {
        Page.startPage(dto);
        List<Dto> list = tPublicExt.getOrderList(dto);
        if (list != null && !list.isEmpty()) {
            for (Dto dto1 : list) {
                dto1.put("startTime", dto1.getDate("startTime").getTime());
                dto1.put("endTime", dto1.getDate("endTime").getTime());
            }
        }
        PageInfo<Dto> pageInfo = new PageInfo<>(list);
        Dto resDto = new HashDto();
        resDto.put("list", list);
        resDto.put("total", pageInfo.getTotal());
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("pageNum", pageInfo.getPageNum());
        return resDto;
    }

    @Override
    public Dto getOrder(Dto dto) {
        Dto orderListDto = getOrderList(dto);
        Dto orderDto;
        Dto resDto = new HashDto();
        List<Dto> list = (List<Dto>) orderListDto.get("list");
        if (list != null && !list.isEmpty()) {
            orderDto = list.get(0);
            String publicId = orderDto.getString("publicId");
            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(publicId)
                    .andIsValidEqualTo("1");
            List<TAddress> addressList = tAddressMapper.selectByExample(addressExample);
            if (addressList != null && !SystemUtils.isEmpty(addressList))
                orderDto.put("address", addressList.get(0));

            Dto publicDto = getPublic(orderDto);
            publicDto.remove("holdingTime");
            resDto.put("order", orderDto);
            resDto.put("public", publicDto);
        }
        return resDto;
    }

    public static void main(String[] args) throws Exception {
        String s = "2018-05-09 14:00";
        String e = "2018-05-09 16:00";
        log.info("s:{}", DateUtil.StringToDateFormat(s, "yyyy-MM-dd hh:mm").getTime());
        log.info("e:{}", DateUtil.StringToDateFormat(e, "yyyy-MM-dd hh:mm").getTime());
    }
}
