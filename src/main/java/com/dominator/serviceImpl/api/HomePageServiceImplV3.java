/*
package com.dominator.serviceImpl.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.enums.ReqEnums;
import com.dominator.mybatis.dao.*;
import com.dominator.mybatis.ext.dao.ModulesDao;
import com.dominator.mybatis.ext.dao.TPropertyDao;
import com.dominator.service.ModulesService;
import com.dominator.service.api.HomePageServiceV3;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HomePageServiceImplV3 implements HomePageServiceV3 {

    @Autowired
    private T_property_merchantDao t_property_merchantDao;

    @Autowired
    private ModulesService modulesService;

    @Autowired
    private TPropertyDao tPropertyDao;

    @Autowired
    private ModulesDao modulesDao;

    private static RedisUtil ru = RedisUtil.getRu();

    */
/**
     * 首页可配置
     * @param dto
     * @return
     *//*

    @Override
    public ApiMessage pageV3(Dto dto) {
        ApiMessage msg = new ApiMessage();
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String app_id = dto.getString("app_id");
        String property_id = "";
        Dto resDto = Dtos.newDto();
        try {
            //获取首页的物业公司信息
            T_propertyPO t_propertyPO = tPropertyDao.getPropertyByAppId(app_id);
            if (t_propertyPO != null) {
                property_id = t_propertyPO.getId();
                resDto.put("property_id", property_id);
                resDto.put("property_name", t_propertyPO.getProperty_name());
            }

            //获取首页模块
            Dto moduledto = new HashDto();
            moduledto.put("classify", "0,0");
            //表示h5首页
            moduledto.put("sign","page");
            moduledto.putAll(tokenDto);
            List<T_modulesPO>  list = H5modules(moduledto);

            //替换物业商城信息
            Dto dto1 = new HashDto();
            dto1.put("property_id", property_id);
            dto1.put("is_auth", "1");  //已授权
            dto1.put("is_valid", "1");
            T_property_merchantPO property_merchant = t_property_merchantDao.selectOne(dto1);
            if (property_merchant != null) {
                String base_url = property_merchant.getBase_url();
                String icon_url = property_merchant.getIcon_url();
                if (list != null && list.size() > 2 && list.get(2).getModules_name().equals("园区商城")) {
                    list.get(2).setBase_url(base_url);
                    list.get(2).setIcon_url(icon_url);
                } else if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getModules_name().equals("园区商城")) {
                            list.get(i).setBase_url(base_url);
                            list.get(i).setIcon_url(icon_url);
                            break;
                        }
                    }
                }
            }
            log.info("********HomePage page modules list********" + list.size());
            log.info("---:{}", !ApiUtils.checkToken(token));
            JSONArray jsonArray = new JSONArray();
            if (!ApiUtils.checkToken(token)) {//表示用户未登录
                msg = new ApiMessage("200", "用户未登录");
                resDto.put("is_login", "0");
                jsonArray = getJsonArray(list, 3);
            } else {
                msg.setCode("200");
                msg.setMessage("用户已登陆");
                resDto.put("is_login", "1");//表示已经登陆
                //需要根据登陆用户获取跟他相关的信息
                if (tokenDto != null) {
                    //用户已经登陆但是未认证
                    if (StringUtils.isEmpty(tokenDto.getString("login_type"))) {
                        jsonArray = getJsonArray(list, 2);
                    } else {
                        jsonArray = getJsonArray(list, 0);
                    }
                }
            }

            resDto.put("button", jsonArray);
            msg.setData(Des3Utils.encResponse(resDto));
            return msg;
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ApiException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_REAULT_ERROR.getCode(), ReqEnums.REQ_REAULT_ERROR.getMsg());
        }
    }

    */
/**
     * 获取banner图
     * @param dto token
     * @return
     *//*

    @Override
    public ApiMessage getbanner(Dto dto) throws ApiException {
        String token = dto.getString("token");
        List<Dto> list = modulesDao.getBaseUrl(dto);
        if (list == null || list.size() == 0) {
            throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
        }
        //判断用户是否登陆
        //未登陆
        if(!ApiUtils.checkToken(token)){
            BannerList(list,3);
        }else{
            //登陆
            Dto tokenDto = (Dto)ru.getObject(token);

            //普通用户
            if(StringUtils.isEmpty(tokenDto.getString("login_type"))){
                BannerList(list,2);
            }else{
                //员工用户
                BannerList(list,0);
            }
        }


        Dto resDto = new HashDto();
        resDto.put("list", list);
        return ApiMessageUtil.success(resDto);
    }


    */
/**
     * 返回banner图是否可以应用列表
     * @return
     *//*

    private List<Dto> BannerList(List<Dto> list,int type){
        List<Dto> list1 = new ArrayList<Dto>();
        for(Dto dto1 : list) {
            int kind = Integer.valueOf(dto1.getString("modules_kind"));
            int i = kind&type;
            i  = i>>1;
            dto1.put("open_sign",String.valueOf(i));
            list1.add(dto1);
        }
        return list;

    }


    */
/**
     * 获取游客和用户登陆未认证模块列表
     *
     * @param list
     * @return
     *//*

    public JSONArray getJsonArray(List<T_modulesPO> list, int loginstatus) {
        JSONArray jsonArray = new JSONArray();
        for (T_modulesPO t : list) {
            if (t != null) {
                JSONObject json = new JSONObject();
                //访客通行和报事报修不可以访问
                json.accumulate("name", t.getModules_name());
                json.accumulate("pic", t.getIcon_url());
                json.accumulate("isurl", t.getIsurl());//是否跳转(0内网，1外网)
                int kind = Integer.valueOf(t.getModules_kind());
                int i = kind&loginstatus;
                i  = i>>1;
                json.accumulate("open_sign",String.valueOf(i));
                if (kind == 3){
                    json.accumulate("type", "2");
                    json.accumulate("desc", t.getRemark());
                    json.accumulate("font_color", t.getFont_color());
                }else{
                    json.accumulate("type", "1");
                }
                json.accumulate("url", t.getBase_url() == null ? "" : t.getBase_url());
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    */
/**
     * 获取h5对应的模块列表
     * @return
     *//*

    public  List<T_modulesPO>   H5modules(Dto dto){
        //获取所有模块信息
        ApiMessage msg = modulesService.listModules(dto);
        JSONObject jsonObject = JSONObject.fromObject(msg.getData());
        JSONArray jsonArray1  = jsonObject.getJSONArray("mainModules");
        List<T_modulesPO> list = JSONArray.toList(jsonArray1,T_modulesPO.class);
        return list;
    }

}
*/
