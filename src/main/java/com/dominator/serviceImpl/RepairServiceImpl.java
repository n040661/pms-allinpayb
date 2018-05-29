package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.TRepair;
import com.dominator.entity.TRepairExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TRepairMapper;
import com.dominator.mapper.ext.TGardenExt;
import com.dominator.mapper.ext.TRepairExt;
import com.dominator.service.GardenService;
import com.dominator.service.RepairService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dominator.utils.dateutil.DateUtil.DatetoStringFormat;


@Service
public class RepairServiceImpl implements RepairService {

    private  static Logger log = LoggerFactory.getLogger(RepairServiceImpl.class);

    @Autowired
    private TGardenExt tGardenExt;

    @Autowired
    private TRepairExt tRepairExtMapper;

    @Autowired
    private MsgSendCenter msgSendCenter;

    @Autowired
    private TRepairMapper tRepairMapper;

    @Autowired
    private GardenService gardenService;

    private static RedisUtil ru = RedisUtil.getRu();

    @Override
    public ApiMessage listpage(Dto dto) {
        //园区名字
        String garden_name = "";
        //园区联系方式
        String garden_phone = "";
        //token
        String token = dto.getString("token");
        String status = dto.getString("repair_status");
        Dto tokenDto = (Dto) ru.getObject(token);
        //园区id
        String garden_id = tokenDto.getString("garden_id");
        //用户手机号
        String phone_num = tokenDto.getString("user_name");
        //用户id
        String user_id = tokenDto.getString("user_id");

        Dto dto1 = new HashDto();
        dto1.put("gardenId",garden_id);
        Dto gardenDto = gardenService.getGarden(dto1);
        if(gardenDto!=null){
            garden_name =  gardenDto.getString("garden_name");
            garden_phone = gardenDto.getString("telephone");
        }

        Dto userDto = (Dto) ru.getObject("user"+garden_id+phone_num);

        JSONArray jsonArray = new JSONArray();
        List<TRepair> list = new ArrayList<TRepair>();
        if(userDto!=null && StringUtils.isNotEmpty(userDto.getString("authe_type"))){
            Page.startPage(dto);
            TRepairExample tRepairExample = new TRepairExample();
            TRepairExample.Criteria criteria = tRepairExample.createCriteria();
            if (StringUtils.isNotEmpty(user_id))
                criteria.andRepairUserIdEqualTo(user_id);
            criteria.andIsValidEqualTo("1").andRepairStatusEqualTo(status).andGardenIdEqualTo(garden_id);
            tRepairExample.or().andFixUserIdEqualTo(user_id)
                .andIsValidEqualTo("1").andRepairStatusEqualTo(status).andGardenIdEqualTo(garden_id);
            tRepairExample.setOrderByClause(dto.getString("field")+" "+dto.getString("sort"));

            list = tRepairMapper.selectByExample(tRepairExample);
            tRepairExample.clear();
            if(list != null){
                    for(TRepair tRepair : list){
                    if(tRepair!=null){

                    Dto repair = new HashDto();
                    repair.put("id",tRepair.getId());
                    repair.put("repair_content",tRepair.getRepairContent());
                    repair.put("garden_name",garden_name);
                    repair.put("garden_phone",garden_phone);
                    repair.put("repair_num",tRepair.getRepairNum());
                    repair.put("repair_status",tRepair.getRepairStatus());
                    repair.put("type",tRepair.getType());
                    repair.put("spot",tRepair.getSpot());
                    Date repair_time= tRepair.getRepairTime();
                    Date date = new Date();
                    String s =  DatetoStringFormat(repair_time,"yyyy年MM月dd日 HH:mm");
                    repair.put("repair_time",s);
                    if(tRepair.getCompleteTime()!=null){
                        String complete_time =  DatetoStringFormat(tRepair.getCompleteTime(),"yyyy年MM月dd日 HH:mm");
                        repair.put("complete_time",complete_time);
                    }
                    String[] pics = null;
                    if(tRepair.getRepairPics()!=null){
                        String repair_pics = tRepair.getRepairPics();
                        pics = repair_pics.split(",");
                    }
                    repair.put("pics",pics);
                    jsonArray.add(repair);
                    }
                }
            }

        }
        return ApiMessageUtil.success(jsonArray);

    }

    /**
     * 新增报修
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public ApiMessage addrepair(Dto dto) {
        //token
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        //园区id
        String garden_id = tokenDto.getString("garden_id");
        //报修用户id
        String user_id = tokenDto.getString("user_id");
        //报修人手机号
        String phone_num = tokenDto.getString("user_name");
        //报修内容
        String repair_content = dto.getString("repair_content");
        //报修图片
        List<String> repair_pics = (List<String>) dto.getList("repair_pics");
        //报修时间
        Date date = new Date();
        //报修类型
        String type = dto.getString("type");
        //报修地点
        String spot = dto.getString("spot");
        //缓存用户信息
        Dto userDto = (Dto) ru.getObject("user"+garden_id+phone_num);
        //登录用户类型
        String authetype = userDto.getString("authe_type");
        //id
        String uuid = PrimaryGenerater.getInstance().uuid();

        String s =  DatetoStringFormat(date,"yyyy年MM月dd日 HH:mm");
        TRepair t_repairPO = new TRepair();
        t_repairPO.setGardenId(garden_id);
        t_repairPO.setRepairContent(repair_content);
        StringBuffer sb = new StringBuffer();
        if(repair_pics!=null && repair_pics.size()>0){
            for (String pic : repair_pics){
                sb.append(pic+",");
            }
            sb.delete(sb.length()-1,sb.length());
            t_repairPO.setRepairPics(sb.toString());
        }



        t_repairPO.setRepairTime(date);
        t_repairPO.setIsValid("1");
        if(authetype.equals("company")){
            t_repairPO.setCompanyId(tokenDto.getString("company_id"));
        }
        t_repairPO.setRepairStatus("0");//报是报修待处理状态
        t_repairPO.setId(uuid);
        t_repairPO.setRepairUserId(user_id);
        t_repairPO.setRepairNum(PrimaryGenerater.getInstance().next());//工单号
        t_repairPO.setType(type);
        t_repairPO.setSpot(spot);
        int i = tRepairMapper.insert(t_repairPO);
        if(i!=1) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
            //发送短信和微信消息
            Dto dto1 = new HashDto();
            dto1.put("garden_id",garden_id);
            Dto gardendto = tGardenExt.selectByGardenId(dto1);
            if(gardendto!=null){
                gardendto.put("repair_id",uuid);
                gardendto.put("repair_content",repair_content);
                gardendto.put("repair_time",s);
                msgSendCenter.sendNRepairNotice(gardendto);

        }

        return ApiMessageUtil.success();
    }

    /**
     * 报修处理成功
     * @return
     *//*
    @Override
    public ApiMessage updaterepair(Dto dto) {
        String id = dto.getString("id");
        Date date = new Date();
        T_repairPO t_repairPO = new T_repairPO();
        t_repairPO.setId(id);
        t_repairPO.setComplete_time(date);
        t_repairPO.setRepair_status("2");//完成报事报修
        int i = t_repairDao.updateByKey(t_repairPO);
        if(i>0){
            //发送消息给报修人
            Dto companyDto = new HashDto();
            companyDto.put("stype","5");
            companyDto.put("mobile",dto.getString("phone_num"));
            companyDto.put("url",dto.getString("url")+"?"+id);
            SmsUtils.sendNoticeSms(companyDto);
        }
        return ApiMessageUtil.success();
    }*/


    @Override
    public ApiMessage addCompanyRepair(Dto dto) {

        return null;
    }

    /**
     * 获取企业报事报修列表
     * @param dto  garden_id 园区id
     *                  company_user_id 公司用户id
     *                  page_num 当前页
     *                  page_size 每页条数
     *             repair_status 报修状态(0待处理,1正在处理,2处理完成)
     *             create_time 起始时间
     *             end_time 截止时间
     * @return
     */
    @Override
    public ApiMessage companyListRepair(Dto dto) {
        if(SystemUtils.isEmpty(dto.getString("create_time"))&&SystemUtils.isEmpty(dto.getString("end_time"))){
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.YEAR,-1);
            Date create_time = c.getTime();
            dto.put("end_time",now);
            dto.put("create_time",create_time);
        }
        dto.put("is_valid", "1");
        Page.startPage(dto);
        List<Dto> list = tRepairExtMapper.listCompanyRepairPage(dto);
        List<Dto> repairList = new ArrayList<Dto>();
        for(Dto d : list){
            String pics =  d.getString("repair_pics");
            String[] repair_pics = pics.split(",");
            d.put("repair_pics",repair_pics);
            repairList.add(d);
        }
        Dto resDto = new HashDto();
        resDto.put("total", dto.getPageTotal());
        resDto.put("list", repairList);
        return ApiMessageUtil.success(resDto);
    }


    /**
     * 获取园区报事报修列表
     * @param dto  garden_id 园区id
     *                  page_num 当前页
     *                  page_size 每页条数
     *                  repair_status 报修状态(0待处理,1正在处理,2处理完成)
     *             create_time 起始时间
     *             end_time 截止时间
     * @return
     */
    @Override
    public ApiMessage gardenListRepair(Dto dto) {
        if(SystemUtils.isEmpty(dto.getString("create_time"))&&SystemUtils.isEmpty(dto.getString("end_time"))){
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.YEAR,-1);
            Date create_time = c.getTime();
            dto.put("end_time",now);
            dto.put("create_time",create_time);
        }
        dto.put("is_valid", "1");
        //PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
        Page.startPage(dto);
        List<Dto> listDto = tRepairExtMapper.listGardenRepairPage(dto);
        log.info("=========pc garden listrepair======"+listDto.size());
        List<Dto> repairGardenList = new ArrayList<Dto>();
        if(listDto!=null &&listDto.size()>0){

            for(Dto dto1 : listDto){
                String pic =  dto1.getString("repair_pics");
                if(SystemUtils.isNotEmpty(pic)){
                    String[] pics = pic.split(",");
                    dto1.put("repair_pics",pics);
                }
                repairGardenList.add(dto1);
            }
        }
        Dto resDto = new HashDto();
        resDto.put("total", dto.getPageTotal());
        resDto.put("list", repairGardenList);
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 获取园区员工列表
     * @param dto garden_id 园区id  选填
     *            token 选填
     * @return
     */
    @Override
    public ApiMessage getGardenUser(Dto dto) {
        String token = dto.getString("token");
        if (StringUtils.isNotEmpty(token)) {
            Dto tokenDto = (Dto) ru.getObject(token);
            dto.clear();
            dto.put("garden_id", tokenDto.getString("garden_id"));
        }
        List<Dto> list = tRepairExtMapper.listGardenUser(dto);
        Dto resDto = new HashDto();
        resDto.put("list",list);
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 指派员工处理报事报修
     * @param dto garden_user_id 园区员工id
     *            repair_id 报事报修id
     *            garden_user_phone 园区员工手机号
     *            company_user_phone 公司员工手机号
     * @return
     */
    @Transactional
    @Override
    public ApiMessage gardenUserToRepair(Dto dto) throws ApiException {
        String garden_user_id =  dto.getString("garden_user_id");
        String garden_user_phone  = dto.getString("garden_user_phone");
        TRepair t_repairPO = new TRepair();
        t_repairPO.setId(dto.getString("repair_id"));
        t_repairPO.setFixUserId(garden_user_id);
        t_repairPO.setAssignTime(new Date());//处理时间
        t_repairPO.setRepairStatus("1");//处理中
        int i = tRepairMapper.updateByPrimaryKey(t_repairPO);
        if(i!=1) {//表示修改失败
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        dto.put("repair_status","1");
        Dto repair =  tRepairExtMapper.repairToUser(dto);
        repair.put("garden_user_phone",garden_user_phone);
        msgSendCenter.sendRepairingNotice(repair);

        return ApiMessageUtil.success();
    }

    /**
     * 完成报事报修
     * @param dto repairId 报事报修id
     * @return
     */
    @Override
    public ApiMessage CompleteRepair(Dto dto) throws ApiException {
        TRepair t_repairPO = new TRepair();
        t_repairPO.setRepairStatus("2");
        t_repairPO.setId(dto.getString("repairId"));
        t_repairPO.setCompleteTime(new Date());
        int i = tRepairMapper.updateByPrimaryKey(t_repairPO);
        if(i !=1) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        dto.put("repair_status","2");
        Dto repair=  tRepairExtMapper.repairToUser(dto);
        msgSendCenter.sendYRepairNotice(repair);
        return ApiMessageUtil.success();
    }

    /**
     * 报修详情
     * @param dto
     * @return
     */
    @Override
    public ApiMessage repairDetail(Dto dto) {
        String repair_id = dto.getString("repair_id");
        Dto repair = tRepairExtMapper.repairDetail(repair_id);
        Dto repairDetail  = new HashDto();
        if(repair!=null){
            repairDetail.put("company_user_phone",repair.getString("phone_num"));
            repairDetail.put("garden_phone",repair.getString("telephone"));
            repairDetail.put("repair_name",repair.getString("repair_name"));
            repairDetail.put("nick_name",repair.getString("nick_name"));
            repairDetail.put("type",repair.getString("type"));
            repairDetail.put("spot",repair.getString("spot"));
            String s =  DatetoStringFormat(repair.getDate("repair_time"),"yyyy-MM-dd HH:mm");
            if(repair.getString("complete_time")!=null){
                repairDetail.put("complete_time",repair.getString("complete_time"));
            }
            repairDetail.put("repair_time",s);
            repairDetail.put("repair_content",repair.getString("repair_content"));
            String pics = repair.getString("repair_pics");
            List<String> list = new ArrayList<String>();
            if(StringUtils.isNotEmpty(pics)){
                String[] strs = pics.split(",");
                for(String str : strs){
                   list.add(str) ;
                }
            }
            repairDetail.put("repair_pics",list);
            repairDetail.put("repair_num",repair.getString("repair_num"));
            repairDetail.put("garden_name",repair.getString("garden_name"));
        }

        return ApiMessageUtil.success(repairDetail);
    }

    /**
     * 我的维修
     * @param dto
     * @return
     */
    @Override
    public ApiMessage myrepair(Dto dto) {
        //园区名字
        String garden_name = "";
        //园区联系方式
        String garden_phone = "";
        //token
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        //园区id
        String garden_id = tokenDto.getString("garden_id");
        //用户手机号
        String phone_num = tokenDto.getString("user_name");
        //用户id
        String user_id = tokenDto.getString("user_id");
        //String user_id = dto.getString("user_id");
        Dto dto1 = new HashDto();
        dto1.put("gardenId",garden_id);
        Dto gardenDto = gardenService.getGarden(dto1);
        if(gardenDto!=null){
            garden_name =  gardenDto.getString("gardenName");
            garden_phone = gardenDto.getString("telephone");
        }
        JSONArray jsonArray = new JSONArray();
        List<TRepair> tRepairsList = new ArrayList<>();
        Page.startPage(dto);
        TRepairExample tRepairExample = new TRepairExample();
        tRepairExample.or().andFixUserIdEqualTo(user_id).andIsValidEqualTo("1");
        tRepairExample.setOrderByClause(dto.getString("field")+" "+dto.getString("sort"));
        tRepairsList = tRepairMapper.selectByExample(tRepairExample);
        if(tRepairsList!=null || !tRepairsList.isEmpty()){
            for (TRepair tRepair : tRepairsList) {
                Dto repair = new HashDto();
                repair.put("id", tRepair.getId());
                repair.put("repairUserId", tRepair.getRepairUserId());
                repair.put("repairContent", tRepair.getRepairContent());
                repair.put("gardenName", garden_name);
                repair.put("gardenPhone", garden_phone);
                repair.put("repairNum", tRepair.getRepairNum());
                repair.put("repairStatus", tRepair.getRepairStatus());
                repair.put("type",tRepair.getType());
                repair.put("spot",tRepair.getSpot());
                Date repair_time = tRepair.getRepairTime();
                Date date = new Date();
                String s = DatetoStringFormat(repair_time, "yyyy年MM月dd日 HH:mm");
                repair.put("repairTime", s);
                if (tRepair.getCompleteTime() != null) {
                    String complete_time = DatetoStringFormat(tRepair.getCompleteTime(), "yyyy年MM月dd日 HH:mm");
                    repair.put("completeTime", complete_time);
                }
                String[] pics = null;
                if (tRepair.getRepairPics() != null) {
                    String repair_pics = tRepair.getRepairPics();
                    pics = repair_pics.split(",");
                }
                repair.put("pics", pics);
                jsonArray.add(repair);
            }
        }
        return ApiMessageUtil.success(jsonArray);
    }


}
