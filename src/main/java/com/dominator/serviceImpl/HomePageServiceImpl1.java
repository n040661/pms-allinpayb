package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TPropertyExt;
import com.dominator.service.HomePageService1;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.exception.ApiException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class HomePageServiceImpl1 implements HomePageService1 {
    private static Logger logger = LoggerFactory.getLogger(HomePageServiceImpl1.class);

    @Autowired
    private TPropertyExt tPropertyExt;

    @Autowired
    private TModulesMapper tModulesMapper;

    @Autowired
    private TPropertyMerchantMapper tPropertyMerchantMapper;

    private static RedisUtil ru = RedisUtil.getRu();


    /**
     * 获取首页信息
     *
     * @param dto
     * @return
     */
    @Override
    public ApiMessage page(Dto dto) throws ApiException {
        String token = dto.getString("token");
        String appid = dto.getString("app_id");
        ApiMessage msg = new ApiMessage();
        Dto resDto = Dtos.newDto();
        try {
            //获取首页的物业公司信息
            String property_id = "";
            TProperty tProperty = tPropertyExt.getPropertyByAppId(appid);
            if (tProperty != null) {
                property_id = tProperty.getId();
                resDto.put("property_id", property_id);
                resDto.put("property_name", tProperty.getPropertyName());
            }

            JSONArray jsonArray = new JSONArray();
            //获取所有模块信息
            /*Dto moduledto = new HashDto();
            moduledto.put("modules_type", "0");//表示是h5部分
            moduledto.put("modules_classify", "0");*/
            TModulesExample tModulesExample = new TModulesExample();
            tModulesExample.setOrderByClause("sort asc");
            tModulesExample.or().andModulesTypeEqualTo("0").andModulesClassifyEqualTo("0")
                    .andIsValidEqualTo("1");
            List<TModules> list= tModulesMapper.selectByExample(tModulesExample);
            tModulesExample.clear();
            //List<T_modulesPO> list = modulesDao.listsort(moduledto);
            //替换物业商城信息
            /*Dto dto1 = new HashDto();
            dto1.put("property_id", property_id);
            dto1.put("is_auth", "1");  //已授权
            dto1.put("is_valid", "1");*/
            TPropertyMerchantExample tPropertyMerchantExample = new TPropertyMerchantExample();
            tPropertyMerchantExample.or().andPropertyIdEqualTo(property_id).andIsAuthEqualTo("1")
                    .andIsValidEqualTo("1");
            List<TPropertyMerchant> merchantList = tPropertyMerchantMapper.selectByExample(tPropertyMerchantExample);
            tPropertyMerchantExample.clear();
           // T_property_merchantPO property_merchant = t_property_merchantDao.selectOne(dto1);
            if (merchantList != null && merchantList.size()==1) {
                TPropertyMerchant property_merchant = merchantList.get(0);
                String base_url = property_merchant.getBaseUrl();
                String icon_url = property_merchant.getIconUrl();
                if (list != null && list.size() > 2 && list.get(2).getModulesName().equals("园区商城")) {
                    list.get(2).setBaseUrl(base_url);
                    list.get(2).setIconUrl(icon_url);
                } else if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getModulesName().equals("园区商城")) {
                            list.get(i).setBaseUrl(base_url);
                            list.get(i).setIconUrl(icon_url);
                            break;
                        }
                    }
                }
            }
            logger.info("******************HomePage page modules list********" + list.size());
            logger.info("---:{}", !ApiUtils.checkToken(token));
            if (!ApiUtils.checkToken(token)) {//表示用户未登录
                msg = new ApiMessage("200", "用户未登录");
                resDto.put("is_login", "0");
                jsonArray = getJsonArray(list, "outlogin");
            } else {
                msg.setCode("200");
                msg.setMessage("用户已登陆");
                resDto.put("is_login", "1");//表示已经登陆
                //需要根据登陆用户获取跟他相关的信息
                Dto tokenDto = (Dto) ru.getObject(token);
                //resDto.put("user", userdto);
                if (tokenDto != null) {
                    //用户已经登陆但是未认证
                    if (StringUtils.isEmpty(tokenDto.getString("type"))) {
                        jsonArray = getJsonArray(list, "is_login");
                    } else {
                        for (TModules t : list) {
                            if (t != null) {
                                JSONObject json = new JSONObject();
                                json.accumulate("module_classify", t.getModulesClassify());//模块类型
                                json.accumulate("name", t.getModulesName());
                                json.accumulate("pic", t.getIconUrl());
                                json.accumulate("open_sign", "1");
                                json.accumulate("isurl", t.getIsurl());

                                switch (t.getId()) {
                                    case "42":
                                    case "43":
                                    case "44":
                                        json.accumulate("type", "2");
                                        json.accumulate("desc", t.getRemark());
                                        json.accumulate("font_color", t.getFontColor());
                                        break;
                                    case "8":
                                        json.accumulate("type", "1");
                                        break;
                                    default:
                                        json.accumulate("type", "1");
                                        break;

                                }
                                json.accumulate("url", t.getBaseUrl() == null ? "" : t.getBaseUrl());
                                jsonArray.add(json);
                            }
                        }
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

    @Override
    public ApiMessage show(Dto dto) {
        return null;
    }

    @Override
    public ApiMessage update(Dto dto) {
        return null;
    }

    @Override
    public ApiMessage user(Dto dto) {
        return null;
    }

    @Override
    public ApiMessage getuser(Dto dto) {
        return null;
    }

    /**
     * 获取游客和用户登陆未认证模块列表
     *
     * @param list
     * @return
     */
    public JSONArray getJsonArray(List<TModules> list, String loginstatus) {
        JSONArray jsonArray = new JSONArray();
        for (TModules t : list) {
            if (t != null) {
                JSONObject json = new JSONObject();
                //访客通行和报事报修不可以访问
                json.accumulate("module_classify", t.getModulesClassify());//模块类型
                json.accumulate("name", t.getModulesName());
                json.accumulate("pic", t.getIconUrl());
                json.accumulate("isurl", t.getIsurl());//是否跳转(0内网，1外网)
                switch (t.getId()) {
                    case "1":
                    case "2":
                    case "3":
                        json.accumulate("open_sign", "0");//是否可以使用(0不可使用该模块)
                        json.accumulate("type", "1");
                        break;
                    case "8":
                        json.accumulate("open_sign", "1");
                        json.accumulate("type", "1");
                        break;
                    case "42":
                    case "43":
                    case "44":
                        if (loginstatus.equals("outlogin")) {
                            json.accumulate("open_sign", "0");
                        } else {
                            json.accumulate("open_sign", "1");
                        }
                        json.accumulate("type", "2");
                        json.accumulate("desc", t.getRemark());
                        json.accumulate("font_color", t.getFontColor());
                        break;
                    default:
                        json.accumulate("open_sign", "1");
                        json.accumulate("type", "1");
                        break;

                }
                json.accumulate("url", t.getBaseUrl() == null ? "" : t.getBaseUrl());
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }


    /**
     * 获取banner图
     * @param dto token
     * @return
     */
    @Override
    public ApiMessage getbanner(Dto dto) throws ApiException {
        String token = dto.getString("token");
        TModulesExample tModulesExample = new TModulesExample();
        tModulesExample.or().andModulesTypeEqualTo(dto.getString("modules_type"))
                .andModulesClassifyEqualTo(dto.getString("modules_classify"))
                .andIsValidEqualTo("1");
        tModulesExample.setOrderByClause("sort asc");
        List<TModules> list = tModulesMapper.selectByExample(tModulesExample);
        tModulesExample.clear();
       // List<Dto> list = modulesDao.getBaseUrl(dto);
        if (list == null || list.size() == 0) {
            throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
        }
        //判断用户是否登陆
        List<Dto> bannerList = new ArrayList<Dto>();
        //未登陆
        if(!ApiUtils.checkToken(token)){
            bannerList = BannerList(list,"1");
        }else{
            //登陆
            Dto tokenDto = (Dto)ru.getObject(token);

            //普通用户
            if(StringUtils.isEmpty(tokenDto.getString("type"))){
                bannerList = BannerList(list,"2");
            }else{
                //员工用户
                bannerList = BannerList(list,"3");
            }
        }


        Dto resDto = new HashDto();
        resDto.put("list", bannerList);
        return ApiMessageUtil.success(resDto);
    }

    @Override
    public ApiMessage getproperty(Dto dto) {
        return null;
    }

    @Override
    public ApiMessage updateuser(Dto dto) {
        return null;
    }

    /**
     * 返回banner图是否可以应用列表
     * @return
     */
    private List<Dto> BannerList(List<TModules> list,String type){
        List<Dto> list1 = new ArrayList<Dto>();
        for(TModules tModules : list) {
            Dto dto1 = new HashDto();
            dto1.put("base_url",tModules.getBaseUrl());
            dto1.put("icon_url",tModules.getIconUrl());
            dto1.put("is_url",tModules.getIsurl());
            if(type.equals("3")){
                dto1.put("open_sign","0");
            }else {
                switch (tModules.getBaseId()) {
                    case "1":
                    case "2":
                    case "3":
                        dto1.put("open_sign", "0");//是否可以使用(0不可使用该模块)
                        break;
                    case "8":
                        dto1.put("open_sign", "1");
                        break;
                    case "42":
                    case "43":
                    case "44":
                        if (type.equals("1")) {
                            dto1.put("open_sign", "0");
                        } else if (type.equals("2")) {
                            dto1.put("open_sign", "1");
                        }
                        break;
                    default:
                        dto1.put("open_sign", "1");
                        break;

                }
            }
            list1.add(dto1);
        }
        return list1;

    }

}
