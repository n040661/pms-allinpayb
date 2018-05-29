package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.app.service.RepairService;
import com.dominator.entity.TRepair;
import com.dominator.entity.TRepairExample;
import com.dominator.entity.TUser;
import com.dominator.entity.TVisitorExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TRepairMapper;
import com.dominator.mapper.TRoleMapper;
import com.dominator.mapper.TUserMapper;
import com.dominator.mapper.ext.TGardenExt;
import com.dominator.mapper.ext.TRepairExt;
import com.dominator.service.CommonService;
import com.dominator.service.GardenService;
import com.dominator.serviceImpl.MsgSendCenter;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dominator.utils.dateutil.DateUtil.DatetoStringFormat;

@Service("app.RepairServiceImpl")
public class RepairServicelmpl implements RepairService {
   @Autowired
   private GardenService gardenService;

    private  RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private TRepairMapper tRepairMapper;

    @Autowired
    private TGardenExt tGardenExt;

    @Autowired
    private MsgSendCenter msgSendCenter;

    @Autowired
    private TRepairExt tRepairExtMapper;

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public ApiMessage listpage(Dto dto) {
        //园区名字
        String garden_name = "";
        //园区联系方式
        String garden_phone = "";
        //报修人姓名
        String repairName = "";
        String status = dto.getString("repairStatus");
        //园区id
        String gardenId = dto.getString("gardenId");
        //用户id
        String userId = dto.getString("userId");
        String companyId = dto.getString("companyId");
        Dto dto1 = new HashDto();
        dto1.put("gardenId",gardenId);
        dto1.put("companyId",companyId);
        Dto gardenDto = gardenService.getGarden(dto1);
        if(gardenDto!=null){
            garden_name =  gardenDto.getString("garden_name");
            garden_phone = gardenDto.getString("telephone");
        }

        TUser tUser = tUserMapper.selectByPrimaryKey(userId);
        if (tUser!=null)
            repairName = tUser.getNickName();

        JSONArray jsonArray = new JSONArray();
        List<TRepair> list = new ArrayList<TRepair>();

        Page.startPage(dto);
        TRepairExample tRepairExample = new TRepairExample();
        TRepairExample.Criteria criteria = tRepairExample.createCriteria();
        if (StringUtils.isNotEmpty(userId))
            criteria.andRepairUserIdEqualTo(userId);
        if (StringUtils.isNotEmpty(companyId))
            criteria.andCompanyIdEqualTo(companyId);
        criteria.andIsValidEqualTo("1").andRepairStatusEqualTo(status).andGardenIdEqualTo(gardenId);
        tRepairExample.setOrderByClause(dto.getString("field")+" "+dto.getString("sort"));

        list = tRepairMapper.selectByExample(tRepairExample);
        tRepairExample.clear();
        if(list != null){
            for(TRepair tRepair : list){
                if(tRepair!=null){
                    Dto repair = new HashDto();
                    repair.put("id",tRepair.getId());
                    repair.put("repairName",repairName);
                    repair.put("repairContent",tRepair.getRepairContent());
                    repair.put("gardenName",garden_name);
                    repair.put("gardenPhone",garden_phone);
                    repair.put("repairNum",tRepair.getRepairNum());
                    repair.put("repairStatus",tRepair.getRepairStatus());
                    repair.put("type",tRepair.getType());
                    repair.put("spot",StringUtils.isNotEmpty(tRepair.getSpot())?tRepair.getSpot():"");
                    String s= String.valueOf(tRepair.getRepairTime().getTime());
                    repair.put("repairTime",s);
                    if(tRepair.getCompleteTime()!=null){
                        String complete_time =  DatetoStringFormat(tRepair.getCompleteTime(),"yyyy年MM月dd日 HH:mm");
                        repair.put("completeCime",complete_time);
                    }
                    List<String> picList = new ArrayList<String>();
                    String pics = tRepair.getRepairPics();
                    if(StringUtils.isNotEmpty(pics)){
                        String[] strs = pics.split(",");
                        for(String str : strs){
                            picList.add(str) ;
                        }
                    }
                    repair.put("pics",picList);
                    jsonArray.add(repair);
                }
            }
        }

        PageInfo pageInfo = new PageInfo(list);
        dto.clear();
        dto.put("pageSize",pageInfo.getPageSize());
        dto.put("pageNum",pageInfo.getPageNum());
        dto.put("total",pageInfo.getTotal());
        dto.put("list",jsonArray);

        //Dto result = Page.resultPage(list);
        return ApiMessageUtil.success(dto);

    }
    /**
     * 新增报修
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public ApiMessage addrepair(Dto dto) {
        //园区id
        String gardenId = dto.getString("gardenId");
        //报修用户id
        String userId = dto.getString("userId");
        //报修人手机号
        String phoneNum = dto.getString("phoneNum");
        //报修内容
        String repairContent = dto.getString("repairContent");
        //报修图片
        List<String> repairPics = (List<String>) dto.getList("repairPics");
        //报修时间
        Date date = new Date();
        //缓存用户信息
        //Dto userDto = (Dto) ru.getObject("user"+garden_id+phone_num);
        //登录用户类型
        //String authetype = userDto.getString("authe_type");
        //报修地点
        String spot = dto.getString("spot");
        //报修类型： 1故障，2报事
        String type = dto.getString("type");
        //id
        String uuid = PrimaryGenerater.getInstance().uuid();

        String s =  DatetoStringFormat(date,"yyyy年MM月dd日 HH:mm");
        TRepair t_repairPO = new TRepair();
        t_repairPO.setGardenId(gardenId);
        t_repairPO.setRepairContent(repairContent);
        StringBuffer sb = new StringBuffer();
        if(repairPics!=null && repairPics.size()>0){
            for (String pic : repairPics){
                sb.append(pic+",");
            }
            sb.delete(sb.length()-1,sb.length());
        }

        t_repairPO.setRepairPics(sb.toString());
        t_repairPO.setRepairTime(date);
        t_repairPO.setIsValid("1");
        t_repairPO.setSpot(spot);
        t_repairPO.setType(type);
        //if(authetype.equals("company")){
            t_repairPO.setCompanyId(dto.getString("companyId"));

        //}

       // }
        t_repairPO.setRepairStatus("0");//报是报修待处理状态
        t_repairPO.setId(uuid);
        t_repairPO.setRepairUserId(userId);
        t_repairPO.setRepairNum(PrimaryGenerater.getInstance().next());//工单号
        t_repairPO.setCreateTime(new Date());

        int i = tRepairMapper.insert(t_repairPO);
        if(i!=1) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        //发送短信和微信消息
        Dto dto1 = new HashDto();
        dto1.put("garden_id",gardenId);
        Dto gardenDto = tGardenExt.selectByGardenId(dto1);
        if(gardenDto!=null){
            gardenDto.put("repairId",uuid);
            gardenDto.put("repairContent",repairContent);
            gardenDto.put("repairTime",s);
            msgSendCenter.sendNRepairNotice(gardenDto);

        }

        return ApiMessageUtil.success();
    }
    /**
     * 报修详情
     * <param>
     * repair_id
     * @return
     */
    @Override
    public ApiMessage repairDetail(Dto dto) {
        String repair_id = dto.getString("repairId");
        Dto repair = tRepairExtMapper.repairDetail(repair_id);
        Dto repairDetail  = new HashDto();
        if(repair!=null){
            repairDetail.put("id",repair.getString("id"));
            repairDetail.put("companyUserPhone",repair.getString("phone_num"));
            repairDetail.put("gardenPhone",repair.getString("telephone"));
            repairDetail.put("repairName",repair.getString("repair_name"));
            repairDetail.put("nickName",repair.getString("nick_name"));
            repairDetail.put("spot",repair.getString("spot"));
            repairDetail.put("type",repair.getString("type"));
            repairDetail.put("repairStatus",repair.getString("repair_status"));
            String s = String.valueOf(repair.getDate("repair_time").getTime());
            if(repair.getString("complete_time")!=null){
                repairDetail.put("completeTime",repair.getString("complete_time"));
            }
            repairDetail.put("repairTime",s);
            repairDetail.put("repairContent",repair.getString("repair_content"));
            String pics = repair.getString("repair_pics");
            List<String> list = new ArrayList<String>();
            if(StringUtils.isNotEmpty(pics)){
                String[] strs = pics.split(",");
                for(String str : strs){
                    list.add(str) ;
                }
            }
            repairDetail.put("pics",list);
            repairDetail.put("repairNum",repair.getString("repair_num"));
            repairDetail.put("gardenName",repair.getString("garden_name"));
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
        /*//token
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        //园区id
        String garden_id = tokenDto.getString("garden_id");
        //用户手机号
        String phone_num = tokenDto.getString("user_name");
        //用户id
        String user_id = tokenDto.getString("user_id");
        //String user_id = dto.getString("user_id");*/
        String gardenId = dto.getString("gardenId");
        String userId = dto.getString("userId");
        Dto dto1 = new HashDto();
        dto1.put("gardenId",gardenId);
        Dto gardenDto = gardenService.getGarden(dto1);
        if(gardenDto!=null){
            garden_name =  gardenDto.getString("gardenName");
            garden_phone = gardenDto.getString("telephone");
        }
        JSONArray jsonArray = new JSONArray();
        List<TRepair> tRepairsList = new ArrayList<>();
        Page.startPage(dto);
        TRepairExample tRepairExample = new TRepairExample();
        TRepairExample.Criteria criteria = tRepairExample.createCriteria();
        criteria.andFixUserIdEqualTo(userId).andRepairStatusEqualTo(dto.getString("repairStatus"))
                .andGardenIdEqualTo(gardenId).andIsValidEqualTo("1");
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
                String s = String.valueOf(tRepair.getRepairTime().getTime());
                //String s = DatetoStringFormat(repair_time, "yyyy年MM月dd日 HH:mm");
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

        Dto result = Page.resultPage(tRepairsList);
        return ApiMessageUtil.success(result);
    }

    /**
     * 完成报事报修
     * @param dto repairId 报事报修id
     * @return
     */
    @Override
    public ApiMessage completeRepair(Dto dto) throws ApiException {
        String id = dto.getString("repairId");
        TRepair t_repairPO = new TRepair();
        t_repairPO.setRepairStatus("2");
        t_repairPO.setId(id);
        t_repairPO.setCompleteTime(new Date());
        int i = tRepairMapper.updateByPrimaryKeySelective(t_repairPO);
        if(i !=1) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        dto.put("repair_status","2");
        Dto repair=  tRepairExtMapper.repairToUser(dto);
        msgSendCenter.sendYRepairNotice(repair);
        return ApiMessageUtil.success();
    }
}
