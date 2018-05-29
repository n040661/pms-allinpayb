package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TUserMapper;
import com.dominator.mapper.TVisitorMapper;
import com.dominator.mapper.ext.TVisitorExt;
import com.dominator.redis.async.EventModel;
import com.dominator.redis.async.EventProducer;
import com.dominator.redis.async.EventType;
import com.dominator.service.RoleService;
import com.dominator.service.VisitorService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.dateutil.DateUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

import static com.dominator.enums.ReqEnums.REQ_UPDATE_ERROR;
import static com.dominator.utils.api.ApiUtils.CreateRandom;
import static com.dominator.utils.dateutil.DateUtil.DatetoStringFormat;
import static com.dominator.utils.dateutil.DateUtil.StringToDateFormat;
import static com.dominator.utils.dateutil.DateUtil.getWeekOfDate;

@Service
@Slf4j
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private TVisitorMapper tVisitorMapper;

    @Autowired
    private MsgSendCenter msgSendCenter;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TVisitorExt tVisitorExt;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RoleService roleService;

    private static RedisUtil ru = RedisUtil.getRu();

    /**
     * 访客信息列表
     *
     * @param dto
     * @return
     */

    @Override
    public ApiMessage list(Dto dto) {
        String companyId = dto.getString("companyId");
        String userId = dto.getString("userId");//用户id
        String gardenId = dto.getString("gardenId");
        String status = dto.getString("status");
        String sort = dto.getString("sort");
        String field = dto.getString("field");

        Page.startPage(dto);
        TVisitorExample visitorExample = new TVisitorExample();
        TVisitorExample.Criteria criteria = visitorExample.createCriteria();
        criteria.andGardenIdEqualTo(gardenId);
        criteria.andStatusEqualTo(status);
        criteria.andIsValidEqualTo("1");

        Dto dto1 = new HashDto();
        if (!SystemUtils.isEmpty(companyId)) {
            criteria.andCompanyIdEqualTo(companyId);
        }else {
            dto1.put("roleName", "扫码门岗");
            dto1.put("gardenId", gardenId);
            Dto userRoleDto = roleService.getUserRoleId(dto1);
            if(!userId.equals(userRoleDto.getString("userId"))){
//                criteria.and
            }
        }
        visitorExample.setOrderByClause(" " + field + " " + sort);
        List<TVisitor> visitorList = tVisitorMapper.selectByExample(visitorExample);
////        dto.put("user_id", user_id);
////        dto.put("company_id", company_id);
////        dto.put("garden_id", garden_id);
//
//        JSONArray jsonArray = new JSONArray();
//
//        if (role_id.equals(sysConfig.DoorGuardId))
//
//        {//表示门禁
//            dto1.put("garden_id", garden_id);
//            dto1.put("status", status);
//            dto1.put("start", dto.getPageStart());
//            dto1.put("limit", dto.getPageLimit());
//            dto1.put("is_valid", "1");
//            dto1.put("field", dto.getString("field"));
//            dto1.put("sort", dto.getString("sort"));
//
//        } else
//
//        {
//            dto.put("is_valid", "1");
//            dto1.putAll(dto);
//        }
//
//        //获取园区的访客列
//        TVisitorExample tVisitorExample = new TVisitorExample();
//        TVisitorExample.Criteria criteria = tVisitorExample.createCriteria();
//        criteria.andIsValidEqualTo("1").
//
//                andGardenIdEqualTo(garden_id);
//        if (StringUtils.isNotEmpty(user_id))
//            criteria.andUserIdEqualTo(user_id);
//        if (StringUtils.isNotEmpty(status))
//            criteria.andStatusEqualTo(status);
//        if (StringUtils.isNotEmpty(company_id))
//            criteria.andCompanyIdEqualTo(company_id);
//        tVisitorExample.setOrderByClause(dto.getString("field") + ",create_time " + dto.getString("sort"));
//        List<TVisitor> list = tVisitorMapper.selectByExample(tVisitorExample);
//
//        tVisitorExample.clear();
//        //将园区访客列表按照实际到访时间倒序
//        if (list != null && list.size() > 0)
//
//        {
//            if (list.size() < endpage) {
//                endpage = list.size();
//            }
//            jsonArray = getvisitorarray(list, startpage, endpage, "fact_time");
//        }


        return ApiMessageUtil.success(visitorList);
    }

    /**
     * 获取访客信息
     *
     * @param dto id 访客id
     * @return
     */
    @Override
    public ApiMessage getOne(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String garden_id = tokenDto.getString("garden_id");
        dto.put("is_valid", "1");
        TVisitor visitorPO = tVisitorMapper.selectByPrimaryKey(dto.getString("id"));
        JSONObject json = new JSONObject();
        if (visitorPO != null) {
            String user_id = visitorPO.getUserId();
            if (StringUtils.isNotEmpty(user_id)) {
                TUser tUser = tUserMapper.selectByPrimaryKey(user_id);
                if (tUser != null) {
                    json.accumulate("invite_name", tUser.getNickName());
                    json.accumulate("invite_phone", tUser.getPhoneNum());
                }
            }
            json.accumulate("status", visitorPO.getStatus());
            json.accumulate("visitor_name", visitorPO.getVisitorName());//访客名称
            json.accumulate("visitor_phone", visitorPO.getVisitorPhone());//访客手机号
            json.accumulate("visitor_sex", visitorPO.getVisitorSex());//访客性别
            json.accumulate("visitor_car_num", visitorPO.getVisitorCarNum() == null ? "" : visitorPO.getVisitorCarNum());//访客车牌
            Date date = null;
            String week = "";
            try {
                date = StringToDateFormat(visitorPO.getExpectTime(), "yyyy年MM月dd日");
                week = getWeekOfDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            json.accumulate("expect_time", visitorPO.getExpectTime() + "(" + week + ")");//预约到访时间
            String device_ids = visitorPO.getDeviceIds();
            if (StringUtils.isNotEmpty(device_ids)) {
                /*String[] strs = device_ids.split(",");
                List<String> stringList = new ArrayList<String>();
                for (String s : strs) {
                    *//*Dto dto1 = new HashDto();
                    dto1.put("device_id", s);
                    dto1.put("garden_id", garden_id);
                    dto1.put("is_valid", "1");*//*
                    stringList.add(s);

                }*/
                /*TVisitorZjExample tVisitorZjExample = new TVisitorZjExample();
                tVisitorZjExample.or().andDeviceIdIn(stringList).andGardenIdEqualTo(garden_id)
                        .andIsValidEqualTo("1");

                List<TVisitorZj> tVisitorZjList = tVisitorZjMapper.selectByExample(tVisitorZjExample);*/
                String[] strs = device_ids.split(",");
                Dto dto1 = new HashDto();
                dto1.put("garden_id", garden_id);
                dto1.put("ids", strs);
                List<Dto> list = tVisitorExt.deviceList(dto1);
                json.accumulate("device_ids", list);
            }
            Date date1 = visitorPO.getFactTime();
            String fact_time = "";
            if (date1 != null) {
                fact_time = DatetoStringFormat(date1, "yyyy年MM月dd日 HH:mm");
            }
            json.accumulate("fact_time", fact_time);//实际到访时间
            json.accumulate("company_address", visitorPO.getCompanyAddress() == null ? "" : visitorPO.getCompanyAddress());//公司地址
            json.accumulate("company_name", visitorPO.getCompanyName() == null ? "" : visitorPO.getCompanyName());

        }
        return ApiMessageUtil.success(json);
    }

    /**
     * 获取通行证
     *
     * @param dto
     * @return
     */

    @Override
    public ApiMessage passcard(Dto dto) {
        TVisitor visitorPO = tVisitorMapper.selectByPrimaryKey(dto.getString("id"));

        Dto resDto = new HashDto();
        if (visitorPO != null && visitorPO.getIsValid().equals("1")) {
            String address = visitorPO.getCompanyAddress();//公司地址\
            String visitor_name = visitorPO.getVisitorName();//访客名称
            String sex = visitorPO.getVisitorSex();//访客性别
            String qr_code = visitorPO.getVisitorQrCode();//访客生成的二维码
            String pass_code = visitorPO.getVisitorPassCode();//访客通行码
            StringBuffer sb = new StringBuffer(pass_code);
            pass_code = sb.insert(3, "-").insert(7, "-").toString();
            String expect_time = visitorPO.getExpectTime();
            Date date = null;
            String week = "";
            try {
                date = StringToDateFormat(expect_time, "yyyy年MM月dd日");
                week = getWeekOfDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TUser tUser = tUserMapper.selectByPrimaryKey(visitorPO.getUserId());
            if (tUser != null) {
                resDto.put("invite_name", tUser.getNickName());
            }
            //被邀请时间，有修改过就填修改过的邀请时间。没有就填创建时间
            if (visitorPO.getModifyTime() != null) {
                resDto.put("invite_time", visitorPO.getModifyTime());
            } else {
                resDto.put("invite_time", visitorPO.getCreateTime());
            }
            resDto.put("address", address);
            resDto.put("company_name", visitorPO.getCompanyName());
            resDto.put("visitor_name", visitor_name);
            resDto.put("visitor_sex", sex);
            resDto.put("qr_code", qr_code);
            resDto.put("pass_code", pass_code);
            resDto.put("expect_time", expect_time + "(" + week + ")");

        }
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 邀请访客,生成通行证
     *
     * @param dto
     * @return
     */

    @Override
    @Transactional
    public ApiMessage invitevisitor(Dto dto) throws ApiException {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String garden_id = tokenDto.getString("garden_id");
        String user_id = tokenDto.getString("user_id");
        dto.put("garden_id", garden_id);
        dto.put("user_id", user_id);


        Dto dto1 = new HashDto();
        dto1.putAll(dto);
        Dto visitorDto = new HashDto();
        TVisitor visitorPO = new TVisitor();

        String uuid = PrimaryGenerater.getInstance().uuid();

        String visitor_phone = dto.getString("visitor_phone");

        String visitor_pass_code = CreateRandom(9);//通行码
        dto.put("visitor_pass_code", visitor_pass_code);
        //获取二维码
        Dto reqDto = getqrpasscode(dto);
        visitorPO.setVisitorQrCode(reqDto.getString("visitor_qr_code"));
        //将访客信息落地
        visitorPO.setVisitorPassCode(visitor_pass_code);//通行码

        visitorPO.setId(uuid);//主键
        StringBuffer deviceIds = new StringBuffer(reqDto.getString("deviceIds"));
        visitorPO.setDeviceIds(deviceIds.length() == 0 ? "" : deviceIds.deleteCharAt(deviceIds.length() - 1).toString());
        visitorPO.setVisitorName(dto.getString("visitor_name"));//访客名称
        visitorPO.setVisitorSex(dto.getString("visitor_sex")); //访客性别
        visitorPO.setVisitorPhone(visitor_phone);//访客手机号
        visitorPO.setVisitorCarNum(dto.getString("visitor_car_num"));//访客车牌号

        Date expect_time = dto.getDate("expect_time");
        String str = DateUtil.DatetoStringFormat(expect_time, "yyyy年MM月dd日");
        visitorPO.setExpectTime(str);//预约时间
        visitorPO.setUserId(dto.getString("user_id"));//用户id
        visitorPO.setGardenId(dto.getString("garden_id"));//园区id
        visitorPO.setCreateTime(new Date());
        visitorPO.setIsValid("1");//启用
        visitorPO.setStatus("0");//拜访状态,未拜访
        visitorPO.setCompanyName(dto.getString("company_name"));
        visitorPO.setCompanyAddress(dto.getString("address"));
        int i = tVisitorMapper.insert(visitorPO);
        if (i != 1) {
            throw new ApiException(REQ_UPDATE_ERROR.getCode(), REQ_UPDATE_ERROR.getMsg());
        }
        visitorDto.put("id", uuid);
        //发送短信给到被邀请人，给到链接
        dto1.put("date", str);
        dto1.put("id", uuid);
        dto1.put("app_id", tokenDto.getString("app_id"));
        //dto1.put("app_id","wxb17fbd493a3f9c8d");
        ru.setObject(uuid, dto1);
        eventProducer.fireEvent(new EventModel(EventType.MSG).setExt("msg", uuid));

        return ApiMessageUtil.success(visitorDto);
    }


    /**
     * 验证通行码
     *
     * @param dto
     * @return
     */

    @Override
    public ApiMessage checkpass(Dto dto) throws ApiException {
        log.info("*************验证通行码开始**************");
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);

        Dto visitorDto = new HashDto();
        String garden_id = "";
        String CodeType = "";
        String visitor_pass_code = dto.getString("visitor_pass_code");//通行码验证
        //从二维码解析出来的数据获取需要的数据
        if (visitor_pass_code.contains("|")) {
            String[] strs = visitor_pass_code.split("\\|");
            String pass_code = strs[0].split("\\.")[1];
            CodeType = pass_code.substring(0, 1);
            visitor_pass_code = pass_code.substring(1, 10);
            garden_id = pass_code.substring(10, pass_code.length());
        }
        log.info("*********获取到的通行码**********" + visitor_pass_code);
        //  表示是固定用户
        if (CodeType.equals("0")) {
            String code = ru.get(garden_id + visitor_pass_code);
            if (StringUtils.isEmpty(code)) {
                throw new ApiException(ReqEnums.REQ_LAST_PASS_CODE.getCode(), ReqEnums.REQ_LAST_PASS_CODE.getMsg());
            }
            if (!code.equals("PASS")) {
                throw new ApiException(ReqEnums.REQ_NOT_PASS_CODE.getCode(), ReqEnums.REQ_NOT_PASS_CODE.getMsg());
            }
            visitorDto.put("msg", "验证通过");
            visitorDto.put("CodeType", CodeType);
            return ApiMessageUtil.success(visitorDto);
        }

        //表示游客验证验证码
        /*Dto passdto = new HashDto();
        passdto.put("visitor_pass_code", visitor_pass_code);
        passdto.put("garden_id", dto.getString("garden_id"));*/
        TVisitorExample tVisitorExample = new TVisitorExample();
        tVisitorExample.or().andVisitorPassCodeEqualTo(visitor_pass_code)
                .andGardenIdEqualTo(tokenDto.getString("garden_id"));
        TVisitor tVisitor = tVisitorMapper.selectByExample(tVisitorExample).get(0);
        if (tVisitor == null) {//表示通行码不存在
            throw new ApiException(ReqEnums.REQ_NOT_PASS_CODE.getCode(), ReqEnums.REQ_NOT_PASS_CODE.getMsg());
        } else {
            String status = tVisitor.getStatus();//访客状态
            if (status.equals("2")) {//过期
                //dto1.put("type", "gq");

                //发送验证通过通知短信给邀请人
                //msgSendCenter.visitor(dto1);
                throw new ApiException(ReqEnums.REQ_LAST_PASS_CODE.getCode(), ReqEnums.REQ_LAST_PASS_CODE.getMsg());
            } else {
                String date = tVisitor.getExpectTime();
                Date now = new Date();
                String nowdate = DatetoStringFormat(now, "yyyy年MM月dd日");
                if (nowdate.equals(date)) {
                    TVisitor visitorPO = new TVisitor();
                    visitorPO.setFactTime(now);
                    visitorPO.setStatus("1");
                    visitorPO.setId(tVisitor.getId());
                    tVisitorMapper.updateByPrimaryKey(visitorPO);
                    //获取redis中通行证对应的使用次数，当为0的时候表示这个通行证不可用
                    int count = Integer.parseInt(ru.get("page_view" + tVisitor.getVisitorPhone()));
                    if (count == 0) {
                        throw new ApiException(ReqEnums.REQ_TIMES_PASS_CODE.getCode(), ReqEnums.REQ_TIMES_PASS_CODE.getMsg());
                    } else {
                        ru.decr("page_view" + tVisitor.getVisitorPhone());
                        visitorDto.put("type", "1");
                        visitorDto.put("msg", "验证通过");
                        visitorDto.put("status", "1");//验证通过
                        visitorDto.put("visitor_id", tVisitor.getId());
                        visitorDto.put("visitor_name", tVisitor.getVisitorName());//访客名称
                        visitorDto.put("visitor_phone", tVisitor.getVisitorPhone());//访客手机号
                        visitorDto.put("visitor_sex", tVisitor.getVisitorSex());//访客性别
                        visitorDto.put("visitor_car_num", tVisitor.getVisitorCarNum());//访客车牌
                        visitorDto.put("expect_time", tVisitor.getExpectTime());//预约到访时间
                        String fact_time = "";
                        fact_time = DatetoStringFormat(now, "yyyy年MM月dd日 HH:mm");
                        visitorDto.put("fact_time", fact_time);//实际到访时间
                        visitorDto.put("company_name", tVisitor.getCompanyName());//获取公司名称
                        visitorDto.put("company_address", tVisitor.getCompanyAddress());//公司地址

                        //发送通知短信
                        Dto msgDto = new HashDto();
                        msgDto.put("visitor_name", tVisitor.getVisitorName());
                        msgDto.put("visitor_phone", tVisitor.getVisitorPhone());
                        msgDto.put("user_phone_num", tokenDto.getString("user_name"));
                        msgDto.put("id", tVisitor.getId());
                        msgDto.put("type", "tg");

                        msgSendCenter.visitor(msgDto);
                    }

                } else {
                    throw new ApiException(ReqEnums.REQ_NOTFOR_PASS_CODE.getCode(), ReqEnums.REQ_NOTFOR_PASS_CODE.getMsg());
                }
            }
        }


        return ApiMessageUtil.success(visitorDto);
    }

    /**
     * 访客数量
     *
     * @param dto
     * @return
     */

    @Override
    public ApiMessage getvisitornum(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String role_id = tokenDto.getString("role_id");//用户的权限id
        String garden_id = tokenDto.getString("garden_id");
        String company_id = tokenDto.getString("company_id");
        Dto visitordto = new HashDto();
        if (role_id.equals(SysConfig.DoorGuardId)) {//表示是门禁
            if (StringUtils.isNotEmpty(garden_id)) {
                Date date = new Date();
                /*Dto dto1 = new HashDto();*/
                String str = DatetoStringFormat(date, "yyyy年MM月dd日");
                /*dto1.put("garden_id", garden_id);
                dto1.put("now", str);*/
                TVisitorExample tVisitorExample = new TVisitorExample();
                TVisitorExample.Criteria criteria = tVisitorExample.createCriteria();
                //今日已到访
                criteria.andExpectTimeEqualTo(str).andGardenIdEqualTo(garden_id)
                        .andIsValidEqualTo("1").andStatusEqualTo("1");
                int i = tVisitorMapper.countByExample(tVisitorExample);
                //今日未到访
                criteria.andStatusEqualTo("0");
                int j = tVisitorMapper.countByExample(tVisitorExample);
                tVisitorExample.clear();
                visitordto.put("user_today", i);
                visitordto.put("user_all", j);
            }
        } else {
            String user_id = tokenDto.getString("user_id");//用户id
            if (StringUtils.isNotEmpty(user_id)) {
                Date date = new Date();
                /*Dto dto1 = new HashDto();*/
                String str = DatetoStringFormat(date, "yyyy年MM月dd日");
                /*dto1.put("user_id", user_id);
                dto1.put("now", str);*/
                TVisitorExample tVisitorExample = new TVisitorExample();
                TVisitorExample.Criteria criteria = tVisitorExample.createCriteria();
                criteria.andUserIdEqualTo(user_id).andIsValidEqualTo("1")
                        .andGardenIdEqualTo(garden_id);
                if (StringUtils.isNotEmpty(company_id))
                    criteria.andCompanyIdEqualTo(company_id);
                int j = tVisitorMapper.countByExample(tVisitorExample);//访客总人数
                criteria.andExpectTimeEqualTo(str);
                int i = tVisitorMapper.countByExample(tVisitorExample);//当天访客数
                /*int i = tVisitorDao.countByToday(dto1);//当天访客数
                int j = tVisitorDao.countAll(user_id);//访客总人数*/
                tVisitorExample.clear();
                visitordto.put("user_today", i);
                visitordto.put("user_all", j);
            }
        }
        return ApiMessageUtil.success(visitordto);
    }

    /**
     * 获取公司所有地址
     *
     * @param dto
     * @return
     */

    @Override
    public ApiMessage getCompanyAdress(Dto dto) {
        /*//获取公司信息
        T_companyPO t_companyPO = tCompanyDao.getCompanyByUserId(dto);
        Dto addressdto = new HashDto();
        String address = "";
        if (t_companyPO != null) {
            Dto dto2 = new HashDto();
            dto2.put("owner_id", t_companyPO.getId());
            dto2.put("is_valid", "1");
            T_addressPO t_addressPO = t_addressDao.selectOne(dto2);
            if (t_addressPO != null) {
                address = t_addressPO.getProvince() + t_addressPO.getCity() + t_addressPO.getArea() + t_addressPO.getStreet();//公司地址
            }
            String[] list = address.split(",");
            if (list.length > 0 && list != null) {
                if (list.length == 1) {
                    addressdto.put("one_address", "1");//表示是单个地址
                } else {
                    addressdto.put("one_address", "0");//表示是多个地址
                }
                JSONArray jsonArray = new JSONArray();
                for (String s : list) {
                    JSONObject json = new JSONObject();
                    json.accumulate("address", s);
                    jsonArray.add(json);
                }
                addressdto.put("company_name", t_companyPO.getCompany_name());
                addressdto.put("addresslist", jsonArray);
            }
        }
        return ApiMessageUtil.success(addressdto);*/
        return null;
    }

    /**
     * 获取门岗访客信息
     *
     * @param dto
     * @return
     */

    @Override
    public ApiMessage mgvisitorMessage(Dto dto) {

        return null;
    }

    /**
     * 获取后台访客数量
     *
     * @param dto status 访客状态
     *            company_id 公司id
     * @return
     */
    @Override
    public ApiMessage getBackVisitorNum(Dto dto) {
        Dto resDto = new HashDto();
        String str = DatetoStringFormat(new Date(), "yyyy年MM月dd日");
        dto.put("now", str);
        String today_count = tVisitorExt.selectVisitorNum(dto);
        String total_count = tVisitorExt.selectVisitorAll(dto);
        resDto.put("today_count", today_count);
        resDto.put("total_count", total_count);

        return ApiMessageUtil.success(resDto);
    }

    /**
     * 获取后台访客的列表
     *
     * @param dto company_id or garden_id
     *            status 状态 0 待到访 1已到访 2已过期
     *            pageSize 每页数
     *            pageNum 页数
     *            create_time 开始时间
     *            end_time 截止时间
     * @return
     */
    @Override
    public ApiMessage listVisitor(Dto dto) {
        if (SystemUtils.isEmpty(dto.getString("create_time")) && SystemUtils.isEmpty(dto.getString("end_time"))) {
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.YEAR, -1);
            Date create_time = c.getTime();
            String Creatime = DatetoStringFormat(create_time, "yyyy年MM月dd日");
            c.add(Calendar.YEAR, 2);
            Date end_time = c.getTime();
            String Now = DatetoStringFormat(end_time, "yyyy年MM月dd日");
            dto.put("end_time", Now);
            dto.put("create_time", Creatime);
        } else {
            dto.put("end_time", DatetoStringFormat(dto.getDate("end_time"), "yyyy年MM月dd日"));
            dto.put("create_time", DatetoStringFormat(dto.getDate("create_time"), "yyyy年MM月dd日"));
        }
        dto.put("is_valid", "1");
        // PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
        Page.startPage(dto);
        List<Dto> list = tVisitorExt.listBackVisitorPage(dto);

        Dto resDto = new HashDto();
        String str = DatetoStringFormat(new Date(), "yyyy年MM月dd日");
        dto.put("now", str);
        String today_count = tVisitorExt.selectVisitorNum(dto);
        String total_count = tVisitorExt.selectVisitorAll(dto);
        Dto dto1 = new HashDto();
        dto1.put("today_count", today_count);
        dto1.put("total_count", total_count);
        resDto.put("count", dto1);
        resDto.put("total", dto.getPageTotal());
        resDto.put("list", list);
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 获取有访客的月份
     *
     * @param dto
     * @return
     */
    @Override
    public ApiMessage VisitorMonth(Dto dto) {

        return null;
    }

    /**
     * 获取各个月份访客数据
     *
     * @param dto
     * @return
     */
    @Override
    public ApiMessage ListMonth(Dto dto) {
        /*//页码
        Integer pageNum = dto.getInteger("pageNum");
        //每页多少数
        Integer pageSize = dto.getInteger("pageSize");
        //用户id
        String user_name = dto.getString("user_id");
        //访客状态
        String status = dto.getString("status");
        //获取公司id
        Dto dtocompany = t_companyDao.getCompanyId(user_name);
        dto.put("company_id",dtocompany.getString("company_id"));

        //默认是第一页
        if(pageNum == null){
            pageNum = 1;
        }
        //默认显示六个月的账单信息
        if(pageSize==null){
            pageSize = 6;
        }

        int startpage = 0;

        dto.put("startpage",startpage+(pageNum-1)*pageSize);
        dto.put("pageSize",pageSize);

        Dto billdto = new HashDto();
        List<Dto> list =  t_company_billDao.listpageByorder(dto);
        logger.info("get bill by page size"+list.size());
        //JSONArray jsonArray = new JSONArray();
        Dto  dto1 = new HashDto();
        if(list!=null &&list.size()>0) {
            for (int j = 0; j < list.size(); j++) {
                String month = list.get(j).getString("bill_year_month");
                month = month.substring(0, 4) + "年" + month.substring(5, 7) + "月";
                String s = dto1.getString(month);
                if (StringUtils.isEmpty(s)) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject json = getbilljson(list.get(j));
                    jsonArray.add(json);
                    dto1.put(month, jsonArray);

                } else {
                    JSONObject json = getbilljson(list.get(j));
                    JSONArray jsonArray = JSONArray.fromObject(dto1.getString(month));
                    jsonArray.add(json);
                    dto1.put(month, jsonArray);
                }

            }
        }
        JSONArray jsonArray = new JSONArray();
        for(String key : dto1.keySet()){
            JSONObject json = new JSONObject();
            json.accumulate("month",key);
            json.accumulate("array",dto1.get(key));
            jsonArray.add(json);
        }
        //jsonArray.add(billdto);
        // billdto.put("billjsonarray",jsonArray);
        return ApiMessageUtil.success(jsonArray);*/
        return null;
    }

    /**
     * 根据月份获取访客数据
     *
     * @param dto
     * @return
     */
    @Override
    public ApiMessage GetVisitorByMonth(Dto dto) {
        return null;
    }

    /**
     * 被邀请人列表
     *
     * @param dto page_num 页码
     *            page_size 页数
     *            token
     *            is_asc 是否正序
     *            field 字段名
     * @return
     */
    @Override
    public ApiMessage BeVisitedList(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);

        Integer pageNum = dto.getInteger("page_num");//第几页
        Integer pageSize = dto.getInteger("page_size");//每页显示条数
        //PagerUtil.getPager(dto, pageNum, pageSize);
        int startpage = (pageNum - 1) * pageSize;//开始的数据
        int endpage = pageNum * pageSize;//结束的数据
        Page.startPage(dto);
        /*dto.put("is_valid", "1");
        dto.put("start", dto.getPageStart());
        dto.put("limit", dto.getPageLimit());
        dto.put("status", "0");
        dto.put("user_name",tokenDto.getString("user_name"));
        dto.put("garden_id",tokenDto.getString("garden_id"));*/
        String user_id = tokenDto.getString("user_id");
        String user_name = tokenDto.getString("user_name");
        String status = dto.getString("status");
        //获取园区的访客列表
        //获取园区的访客列
        TVisitorExample tVisitorExample = new TVisitorExample();
        TVisitorExample.Criteria criteria = tVisitorExample.createCriteria();
        criteria.andIsValidEqualTo("1").andGardenIdEqualTo(tokenDto.getString("garden_id"));
        if (StringUtils.isNotEmpty(user_id))
            criteria.andUserIdEqualTo(user_id);
        if (StringUtils.isNotEmpty(status))
            criteria.andStatusEqualTo(status);
        if (StringUtils.isNotEmpty(user_name))
            criteria.andVisitorPhoneEqualTo(user_name);
        tVisitorExample.setOrderByClause(dto.getString("field") + dto.getString("sort") + "create_time asc");
        List<TVisitor> list = tVisitorMapper.selectByExample(tVisitorExample);

        JSONArray jsonArray = new JSONArray();
        //将园区访客列表按照实际到访时间倒序
        if (list != null && list.size() > 0) {
            if (list.size() < endpage) {
                endpage = list.size();
            }
            jsonArray = getvisitorarray(list, startpage, endpage, "expect_time");
        }
        return ApiMessageUtil.success(jsonArray);
    }

    /**
     * 修改访客信息
     *
     * @param dto id 必填
     *            visitor_name 访客名称 必填
     *            visitor_phone 邀请访客手机号 必填
     *            device_ids 通行授权ID 必填
     *            expect_time 预约时间  必填
     *            visitor_car_num 预约车牌号 选填
     *            park_id
     *            token
     * @return
     */
    @Transactional
    @Override
    public ApiMessage updatevisitor(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String garden_id = tokenDto.getString("garden_id");
        dto.put("garden_id", garden_id);
        String user_id = tokenDto.getString("user_id");

        TVisitor t_visitorPO = new TVisitor();
        String visitor_pass_code = CreateRandom(9);//通行码
        dto.put("visitor_pass_code", visitor_pass_code);
        String expect_time = DatetoStringFormat(dto.getDate("expect_time"), "yyyy年MM月dd日");
        Dto reqDto = getqrpasscode(dto);
        t_visitorPO.setVisitorQrCode(reqDto.getString("visitor_qr_code"));
        t_visitorPO.setVisitorPassCode(visitor_pass_code);
        t_visitorPO.setId(dto.getString("id"));
        t_visitorPO.setVisitorSex(dto.getString("visitor_sex"));
        t_visitorPO.setVisitorName(dto.getString("visitor_name"));
        t_visitorPO.setVisitorPhone(dto.getString("visitor_phone"));
        StringBuffer deviceIds = new StringBuffer();
        List<Map> list = (List<Map>) dto.getList("device_ids");
        if (list != null && list.size() > 0) {
            for (Map map : list) {
                if (map != null) {
                    deviceIds.append(map.get("device_id").toString() + ",");
                }
            }

        }
        t_visitorPO.setDeviceIds(deviceIds.length() == 0 ? "" : deviceIds.deleteCharAt(deviceIds.length() - 1).toString());
        //t_visitorPO.setDevice_ids(dto.getString("device_ids"));
        t_visitorPO.setExpectTime(expect_time);
        t_visitorPO.setVisitorCarNum(dto.getString("visitor_car_num"));
        int i = tVisitorMapper.updateByPrimaryKey(t_visitorPO);
        if (i != 1) {
            throw new ApiException(REQ_UPDATE_ERROR.getCode(), REQ_UPDATE_ERROR.getMsg());
        }
        //发送短信给到被邀请人，给到链接
        Dto msgDto = new HashDto();
        msgDto.put("user_id", user_id);
        msgDto.put("visitor_phone", dto.getString("visitor_phone"));
        msgDto.put("address", dto.getString("address"));
        msgDto.put("company_name", dto.getString("company_name"));
        msgDto.put("date", expect_time);
        msgDto.put("id", dto.getString("id"));
        msgDto.put("app_id", tokenDto.getString("app_id"));
        //dto1.put("app_id","wxb17fbd493a3f9c8d");
        msgSendCenter.bevisitor(msgDto);
        return ApiMessageUtil.success();
    }

    /**
     * 删除访客信息
     *
     * @param dto id 访客ID
     * @return
     */
    @Transactional
    @Override
    public ApiMessage deletevisitor(Dto dto) {
        TVisitor t_visitorPO = new TVisitor();
        t_visitorPO.setIsValid("0");
        t_visitorPO.setId(dto.getString("id"));
        int i = tVisitorMapper.updateByPrimaryKey(t_visitorPO);
        if (i != 1) {
            throw new ApiException(REQ_UPDATE_ERROR.getCode(), REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success();
    }


    public JSONArray getvisitorarray(List<TVisitor> list, int startpage, int endpage, String field) {
        JSONArray jsonArray = new JSONArray();

        for (TVisitor visitorPO : list) {
            JSONObject json = new JSONObject();
            if (visitorPO != null) {
                json.accumulate("id", visitorPO.getId());//访客id
                json.accumulate("status", visitorPO.getStatus());
                json.accumulate("visitor_name", visitorPO.getVisitorName());//访客名称
                json.accumulate("visitor_phone", visitorPO.getVisitorPhone());//访客手机号
                json.accumulate("visitor_sex", visitorPO.getVisitorSex());//访客性别
                json.accumulate("visitor_car_num", visitorPO.getVisitorCarNum() == null ? "" : visitorPO.getVisitorCarNum());//访客车牌
                json.accumulate("expect_time", visitorPO.getExpectTime());//预约到访时间
                Date date1 = visitorPO.getFactTime();
                String fact_time = "";
                if (date1 != null) {
                    fact_time = DatetoStringFormat(date1, "yyyy年MM月dd日 HH:mm");
                }
                json.accumulate("fact_time", fact_time);//实际到访时间
                json.accumulate("company_name", visitorPO.getCompanyName() == null ? "" : visitorPO.getCompanyName());//获取公司名称
                json.accumulate("company_address", visitorPO.getCompanyAddress() == null ? "" : visitorPO.getCompanyAddress());//公司地址
                json.accumulate("visitor_pass_code", visitorPO.getVisitorPassCode());//通行码
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    /**
     * 获取二维码
     *
     * @param dto
     * @return
     */
    public Dto getqrpasscode(Dto dto) {
        Dto resDto = new HashDto();
        String visitor_qr_code = "";
        String visitor_pass_code = dto.getString("visitor_pass_code");
        if (StringUtils.isNotEmpty(visitor_pass_code)) {

            ru.set("page_view" + dto.getString("visitor_phone"), "2");//设置访问通行证可以使用5次
            PrimaryGenerater.getInstance().next();
            //通过中安消生成二维码
            Dto dto1 = new HashDto();
            dto1.put("STATUS", "0");
            dto1.put("PASSWORD", "123456");
            dto1.put("PHONE", dto.getString("visitor_phone"));
            dto1.put("USERNAME", dto.getString("visitor_phone"));
            dto1.put("EMAIL", "");
            dto1.put("NAME", "");
            StringBuffer deviceIds = new StringBuffer();
            List<String> listid = new ArrayList<String>();
            List<Map> list = (List<Map>) dto.getList("device_ids");
            if (list != null && list.size() > 0) {
                for (Map map : list) {
                    if (map != null) {
                        listid.add(map.get("device_id").toString());
                        deviceIds.append(map.get("device_id").toString() + ",");
                    }
                }

            }
            dto1.put("deviceIds", listid);
            dto1.put("parkId", dto.getString("parkId"));
            dto1.put("businessId", "1" + visitor_pass_code + dto.getString("garden_id"));
            Timestamp t = dto.getTimestamp("expect_time");
            String AccessDate = DateUtil.DatetoStringFormat(t, "yyyy-MM-dd HH:mm:ss");
            dto1.put("AccessDate", AccessDate);
            dto1.put("CodeType", "1");//游客
//            visitor_qr_code = gateService.getPass(dto1);
//            todo
            if (StringUtils.isEmpty(visitor_qr_code)) {
                throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
            }
            resDto.put("visitor_qr_code", visitor_qr_code);
            resDto.put("deviceIds", deviceIds);
        }
        return resDto;
    }
}
