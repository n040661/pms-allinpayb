package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.app.service.ReservationHouseService;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TAppHouseMapper;
import com.dominator.mapper.TGardenMapper;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dominator.utils.dateutil.DateUtil.StringToDateFormat;
import static com.dominator.utils.dateutil.DateUtil.timeStampToString;

@Service("app.ReservationHouseImpl")
public class ReservationHouseImpl implements ReservationHouseService{
    @Autowired
    private TAppHouseMapper tAppHouseMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Override
    public ApiMessage addHouse(Dto dto) {
        try{
            //园区id
            String gardenId = dto.getString("gardenId");
            //用户id
            String userId = dto.getString("userId");
            //联系人
            String contactName = dto.getString("contactName");
            //联系人电话
            String contactPhone = dto.getString("contactPhone");
            //性别
            String sex = dto.getString("sex");
            //参观时间
           Long  visitTime = dto.getLong("visitTime");
//            String time = timeStampToString(visitDate,"yyyy年MM月dd日 HH:mm");
//            Date visitTime = StringToDateFormat(time,"yyyy-MM-dd HH:mm:ss");
            //备注
            String remarks = dto.getString("remarks");

            TAppHouse tAppHouse = new TAppHouse();
            tAppHouse.setId(PrimaryGenerater.getInstance().uuid());
            tAppHouse.setGardenId(gardenId);
            tAppHouse.setAppId(userId);
            tAppHouse.setContactName(contactName);
            tAppHouse.setContactPhone(contactPhone);
            tAppHouse.setSex(sex);
            tAppHouse.setStartTime(new Date(visitTime));
            tAppHouse.setRemarks(remarks);
            tAppHouse.setCreateTime(new Date());
            tAppHouse.setIsValid("1");

            int i = tAppHouseMapper.insertSelective(tAppHouse);
            if(i!=1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        }catch (Exception e){
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }

            return ApiMessageUtil.success();
    }

    @Override
    public ApiMessage listHouse(Dto dto) {
        TGarden tGarden = tGardenMapper.selectByPrimaryKey(dto.getString("gardenId"));
        String gardenName = tGarden.getGardenName();
        TAppHouseExample tAppHouseExample = new TAppHouseExample();
        tAppHouseExample.or().andAppIdEqualTo(dto.getString("userId")).andIsValidEqualTo("1");
        Dto resDto = new HashDto();
        Page.startPage(dto);
        List<TAppHouse> houseList = tAppHouseMapper.selectByExample(tAppHouseExample);
        PageInfo<TAppHouse> pageInfo;
        pageInfo = new PageInfo<>(houseList);
        List<Dto> list = new ArrayList<>();
        if (houseList != null && !houseList.isEmpty()){
            for(TAppHouse house : houseList){
                if(house !=null){
                    Dto thouse= new HashDto();
                    thouse.put("visitTime",(house.getStartTime().getTime()));
                    thouse.put("id",house.getId());
                    thouse.put("gardenName",gardenName);
                    list.add(thouse);
                }

            }

        }
        resDto.put("houses",list);
        resDto.put("pageSize",pageInfo.getPageSize());
        resDto.put("pageNum",pageInfo.getPageNum());
        resDto.put("total", pageInfo.getTotal());
        return ApiMessageUtil.success(resDto);
    }

    @Override
    public ApiMessage detailsHouse(Dto dto) {
        String houseId = dto.getString("id");
        TAppHouse tAppHouse = tAppHouseMapper.selectByPrimaryKey(houseId);
        TGarden tGarden = tGardenMapper.selectByPrimaryKey(dto.getString("gardenId"));
        String gardenName = tGarden.getGardenName();
        Dto tHouse = new HashDto();
        if(tAppHouse!=null){
            tHouse.put("gardenName",gardenName);//园区名称
            tHouse.put("contactName",tAppHouse.getContactName());//联系人
            tHouse.put("contactPhone",tAppHouse.getContactPhone());//联系电话
            tHouse.put("sex",tAppHouse.getSex());//性别,0:男, 1:女
            tHouse.put("visitTime",tAppHouse.getStartTime().getTime());//参观时间
            tHouse.put("remarks",tAppHouse.getRemarks());//备注
        }
        return ApiMessageUtil.success(tHouse);
    }
}
