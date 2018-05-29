package com.dominator.serviceImpl.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TAddressMapper;
import com.dominator.mapper.TGateAuthMapper;
import com.dominator.mapper.TModulesMapper;
import com.dominator.mapper.TUserMapper;
import com.dominator.mapper.ext.PageExt;
import com.dominator.mapper.ext.TCompanyBillExt;
import com.dominator.service.api.ApiUserService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApiUserServiceImpl implements ApiUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private PageExt pageExt;

    @Autowired
    private TCompanyBillExt tCompanyBillExt;

    @Autowired
    private TGateAuthMapper tGateAuthMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Autowired
    private TModulesMapper tModulesMapper;

    private static RedisUtil ru = RedisUtil.getRu();

    @Override
    public ApiMessage getuser(Dto dto) {
        //头像
       String head_img_url = SysConfig.HEAD_IMG;
        //是否是认证用户
        String authe = "0";
        //用户类型
        String authe_type = "";
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);

        Dto userdto = new HashDto();
        String user_name = tokenDto.getString("user_name");
        String garden_id = tokenDto.getString("garden_id");//园区id
        String user_id = tokenDto.getString("user_id");
        try {
            //获取园区信息
            dto.clear();
            dto.put("garden_id",garden_id);
            dto.put("is_valid", "1");
//            Dto gardenDto = commonService.getGarden(dto);
            Dto gardenDto = new HashDto();
            //todo
            if (gardenDto != null) {
                userdto.put("zj_park_id", gardenDto.getString("zj_park_id"));
                userdto.put("address", gardenDto.getString("contact_address"));
                userdto.put("company_name", gardenDto.getString("garden_name"));
                userdto.put("garden_name",gardenDto.getString("garden_name"));
            }
            userdto.put("garden_id", garden_id);
            userdto.put("user_id", tokenDto.getString("user_id"));//用户id
            String type = tokenDto.getString("type");
            log.info("******获取用户信息类型*****" + type);
            if (StringUtils.isNotEmpty(tokenDto.getString("type"))) {//表示是token里是认证用户，在实时查询数据库查询是否还是认证用户
                switch (type) {
                    case "2":
                        //表示企业用户
                        tokenDto.put("id", tokenDto.getString("company_id"));
                        authe = "1";
                        authe_type = "company";
                        Dto companyDto = pageExt.selectCompanyUser(tokenDto);
                        if (companyDto != null) {

                            String company_id = tokenDto.getString("company_id");
                            userdto.put("company_id", company_id);
                            userdto.put("company_name", companyDto.getString("company_name"));
                            /*Dto addressDto = new HashDto();
                            addressDto.put("owner_id", company_id);
                            addressDto.put("is_valid", "1");*/
                            TAddressExample tAddressExample = new TAddressExample();
                            tAddressExample.or().andOwnerIdEqualTo(company_id).andIsValidEqualTo("1");
                            TAddress tAddress = tAddressMapper.selectByExample(tAddressExample).get(0);
                            tAddressExample.clear();
                            //T_addressPO t_addressPO = t_addressDao.selectOne(addressDto);
                            if (tAddress != null) {
                                userdto.put("address", tAddress.getProvince() + tAddress.getCity() + tAddress.getArea() + tAddress.getStreet());
                            }
                            userdto.put("position", companyDto.getString("position"));
                            userdto.put("role_id", companyDto.getString("role_id"));
                            userdto.put("role_name", companyDto.getString("role_name"));
                            if (StringUtils.isNotEmpty(companyDto.getString("role_id"))) {
                                if (companyDto.getString("role_name").equals("企业管理员")) {//企业管理员即表示可以查看账单信息
                                    String totalpee = tCompanyBillExt.getNotPaidTotal(company_id);
                                    if (StringUtils.isEmpty(totalpee)) {
                                        userdto.put("total_pee", "0");
                                    } else {
                                        userdto.put("total_pee", totalpee);//待缴款总额
                                    }
                                }
                            }
                        }
                        break;
                    case "1":
                        //表示园区用户
                        authe = "1";
                        authe_type = "garden";
                        Dto gardenDto1 = pageExt.selectGardenUser(tokenDto);
                        if (gardenDto1 != null) {

                            userdto.put("position", gardenDto1.getString("position"));
                            userdto.put("role_id", gardenDto1.getString("role_id"));
                            userdto.put("role_name", gardenDto1.getString("role_name"));
                        }
                        break;
                    case "3":
                        //表示物业用户
                        authe = "1";
                        authe_type = "garden";
                        userdto.put("role_id", tokenDto.getString("role_id"));
                        userdto.put("role_name", tokenDto.getString("role_name"));
                        break;
                }

            }
            Dto extDto = new HashDto();
            extDto.put("authe", authe);
            extDto.put("authe_type", authe_type);
            ru.setObject("user" + garden_id + user_name, extDto);
            log.info("**************user has authe*******" + authe + "user authetype " + authe_type);
            //查询用户表，得到用户头像和用户名称
            TUser tUser = tUserMapper.selectByPrimaryKey(user_id);
            if (tUser == null) {
                throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
            }
            userdto.put("head_img_url", StringUtils.isNotEmpty(tUser.getHeadImg()) ? tUser.getHeadImg() : head_img_url);
            userdto.put("authe", authe);
            userdto.put("authe_type", authe_type);
            userdto.put("nick_name",tUser.getNickName());

            //查询该用户是不是闸机永久用户
            /*Dto oneDto = new HashDto();
            oneDto.put("park_id", userdto.getString("zj_park_id") == null ? "0" : userdto.getString("zj_park_id"));
            oneDto.put("phone", user_name);
            oneDto.put("garden_id", garden_id);
            oneDto.put("is_valid", "1");*/
            String park_id = userdto.getString("zj_park_id") == null ? "0" : userdto.getString("zj_park_id");
            TGateAuthExample tGateAuthExample = new TGateAuthExample();
            tGateAuthExample.or().andParkIdEqualTo(park_id).andPhoneEqualTo(user_name)
                    .andGardenIdEqualTo(garden_id).andIsValidEqualTo("1");
            List<TGateAuth> gateAuthList = tGateAuthMapper.selectByExample(tGateAuthExample);
            tGateAuthExample.clear();
            //T_gate_authPO t_gate_authPO = t_gate_authDao.selectOne(oneDto);
            if (gateAuthList != null && gateAuthList.size() == 1) {
                userdto.put("code_type", "0");//表示永久用户
            } else {
                userdto.put("code_type", "1");//表示访客
            }

            //是否包含通联钱包
            /*oneDto.clear();
            oneDto.put("modules_type","0");
            oneDto.put("modules_classify","3");
            oneDto.put("property_id",tokenDto.getString("property_id"));*/
            TModulesExample tModulesExample = new TModulesExample();
            tModulesExample.or().andModulesTypeEqualTo("0").andModulesClassifyEqualTo("3")
                    .andPropertyIdEqualTo(tokenDto.getString("property_id"));
            List<TModules> modulesList = tModulesMapper.selectByExample(tModulesExample);
            tModulesExample.clear();
            //T_modulesPO t_modulesPO = t_modulesDao.selectOne(oneDto);
            userdto.put("allinpay",0);
            if(modulesList !=null && modulesList.size() == 1){
                userdto.put("allinpay",1);
            }
            return ApiMessageUtil.success(userdto);
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ApiException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_REAULT_ERROR.getCode(), ReqEnums.REQ_REAULT_ERROR.getMsg());
        }
    }

    @Override
    public ApiMessage updateuser(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String user_id = tokenDto.getString("user_id");
        String nickname = dto.getString("nick_name");
        String head_img = dto.getString("head_img");
        if (StringUtils.isEmpty(nickname) && StringUtils.isEmpty(head_img)) {
            return ApiMessageUtil.success();
        }
        TUser tUser = tUserMapper.selectByPrimaryKey(user_id);
        if (tUser != null) {
            if (StringUtils.isNotEmpty(nickname))
                tUser.setNickName(nickname);
            if (StringUtils.isNotEmpty(head_img))
                tUser.setHeadImg(head_img);

        }
        int i = tUserMapper.updateByPrimaryKey(tUser);
        if (i != 1) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }

        return ApiMessageUtil.success();
    }
}
