package com.dominator.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TUserMapper;
import com.dominator.mapper.TblOtosaasOrderMapper;
import com.dominator.mapper.TblPublicAccountMapper;
import com.dominator.service.OtoSaasService;
import com.dominator.service.RoleService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.network.HttpPoster;
import com.dominator.utils.system.PrimaryGenerater;
import com.dominator.utils.system.SeemseUtil;
import com.dominator.weixin.config.WXConfig;
import com.dominator.weixin.util.BlmSignature;
import com.dominator.weixin.util.OTOOrderReflection;
import com.dominator.weixin.util.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Slf4j
public class OtosaasServiceImpl implements OtoSaasService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TblOtosaasOrderMapper tblOtosaasOrderMapper;

    @Autowired
    private TblPublicAccountMapper tblPublicAccountMapper;

    @Override
    public ApiMessage saveServiceOrder(Dto dto) throws Exception {
        String serviceId = dto.getString( "service_id" );
        String prodType = dto.getString( "prod_type" );
        String prodSubType = dto.getString( "prod_sub_type" );
        TblOtosaasOrder otosaasOrder = new TblOtosaasOrder();
        String id = PrimaryGenerater.getInstance().uuid();
        otosaasOrder.setId( id );
        otosaasOrder.setServiceId( serviceId );
        otosaasOrder.setProdType( prodType );
        otosaasOrder.setProdSubType( prodSubType);
        tblOtosaasOrderMapper.insert( otosaasOrder );
        return ApiMessageUtil.success(  );
    }

    /**
     * otosaas管理后台登录接口
     *
     * @param dto user_name
     */
    @Override
    public ApiMessage otosaasLogin(Dto dto) throws ApiException {
        String loginUrl = SysConfig.OTOSAAS_BASE_URL + SysConfig.OTOSAAS_LOGIN;
        String userName = dto.getString("userName");
        String propertyId = dto.getString("propertyId");
        String userId;
        Dto dto1 = new HashDto();
        try {
            TUserExample userExample = new TUserExample();
            userExample.or().andIsValidEqualTo("1");
            List<TUser> userList = tUserMapper.selectByExample(userExample);
            if (userList == null || userList.isEmpty()) {
                throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), ReqEnums.REQ_PARAM_ERROR.getMsg());
            } else {
                if (SystemUtils.isEmpty(userList.get(0).getPassword()))
                    throw new ApiException(ReqEnums.REQ_NO_PASSWORD.getCode(), ReqEnums.REQ_NO_PASSWORD.getMsg());
            }

            if (!SystemUtils.isEmpty(propertyId)) {
                dto1.put("propertyId", propertyId);
                dto1.put("roleName", "物业管理员");
                Dto userRoleDto = roleService.getUserRoleId(dto1);
                userId = userRoleDto.getString("userId");
                TUser user = tUserMapper.selectByPrimaryKey(userId);
                if (user != null)
                    userName = user.getUserName();
            }
            Dto qDto = Dtos.newDto();
            qDto.put("loginName", userName);
            qDto.put("password", "12345678");
            String resp = HttpPoster.postByJson(qDto, loginUrl);
            log.info("后台登录返回：" + resp);
            JSONObject json = JSON.parseObject(resp);
            //获取otosaastoken
            String otosaastoken;
            if ("0".equals(json.getString("code"))) {
                otosaastoken = json.getJSONObject("data").getString("token");
            } else {
                log.error("otosaastoken error");
                throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR.getCode(), ReqEnums.REQ_OTO_LOGIN_ERROR.getMsg());
            }
            Dto resDto = Dtos.newDto();
            resDto.put("otosaasToken", otosaastoken);
            return ApiMessageUtil.success(resDto);
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_OTO_LOGIN_ERROR.getCode(), ReqEnums.REQ_OTO_LOGIN_ERROR.getMsg());
        }
    }


    /**
     * 保存订单
     */
    @Transactional
    @Override
    public ApiMessage saveOrder(Dto dto) throws ApiException {
        log.info("OTOSaaA保存订单参数：" + dto);
        if (dto != null) {
            String sign = dto.getString("sign");
            dto.remove("sign");
            //公钥匙解签
            boolean verify = BlmSignature.verify(WXConfig.OTOSSAAS_PUBLIC_KEY, BlmSignature.getText(dto), sign);
            log.info("【解签结果】：" + verify);
            try {
                if (verify) {
                    String appid = dto.getString("appid");
                    String outTradeNo = UUID.randomUUID().toString().replace("-", "");
                    String header = OTOOrderReflection.orderReflection(appid);
                    if (header == null) {
                        header = "PMS";
                    }
                    String outTradeNo4OTOSSaaS = header + '-' + outTradeNo.substring(10);
                    dto.put("out_trade_no", outTradeNo4OTOSSaaS);
                    TblOtosaasOrder otosaasOrder = dtoConvert(dto);
                    log.info("【保存来自OTOSaaS的订单】" + otosaasOrder);
                    tblOtosaasOrderMapper.insertSelective(otosaasOrder);
                    Map<String, Object> map4OTOSSaaS = new TreeMap<>();
                    map4OTOSSaaS.put("out_trade_no", outTradeNo4OTOSSaaS);
                    String text = BlmSignature.getText(map4OTOSSaaS);
                    String sign4OTOSSaaS = BlmSignature.sign(WXConfig.OTOSSAAS_PRIVATE_KEY, text);
                    net.sf.json.JSONObject json = new net.sf.json.JSONObject();
                    json.put("out_trade_no", outTradeNo4OTOSSaaS);
                    json.put("sign", sign4OTOSSaaS);
                    log.info("【保存订单后返回OTOSaaS结果：" + ApiMessageUtil.success(json) + "】");
                    return ApiMessageUtil.success(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        }
        return ApiMessageUtil.failure();
    }

    @Override
    public TblOtosaasOrder getOrderDetail(Dto dto) throws ApiException {
        String orderId = dto.getString( "order_id" );
        TblOtosaasOrder tbl_otosaas_orderPO = tblOtosaasOrderMapper.selectByPrimaryKey(orderId);
        if (tbl_otosaas_orderPO == null) {
            throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
        }
        return tbl_otosaas_orderPO;
    }


    private TblOtosaasOrder dtoConvert(Dto dto) {
        String body = dto.getString( "body" );
        String appid = dto.getString("appid");
        String detail = dto.getString( "detail" );
        String attach = dto.getString( "attach" );
        String openid = dto.getString( "openid" );
        String orderId = dto.getString( "order_id" );
        String nonceStr = dto.getString( "nonce_str" );
        String signType = dto.getString( "sign_type" );
        String goodsTag = dto.getString( "goods_tag" );
        String prodTpey = dto.getString( "prod_type" );
        String notifyUrl = dto.getString( "notify_url" );
        String productId = dto.getString( "product_id" );
        Integer totalFee = dto.getInteger( "total_fee" );
        String sceneInfo = dto.getString( "scene_info" );
        String deviceInfo = dto.getString( "device_info" );
        String timeExpire = dto.getString( "time_expire" );
        String outTradeNo = dto.getString( "out_trade_no" );
        String prodSubType = dto.getString( "prod_sub_type" );
        String spbillCreateIp = dto.getString( "spbill_create_ip" );

        //Dto转变Tbl_otosaas_orderPO
        TblOtosaasOrder otosaasOrder = new TblOtosaasOrder();
        String id = PrimaryGenerater.getInstance().uuid();
        otosaasOrder.setId(id);
        otosaasOrder.setBody(body);
        otosaasOrder.setAppid(appid);
        TblPublicAccount account = tblPublicAccountMapper.selectByPrimaryKey( appid );
        if(account!=null){
            otosaasOrder.setMchId(account.getMchId());
        }
        otosaasOrder.setPayStatus(0);
        otosaasOrder.setDetail(detail);
        otosaasOrder.setAttach(attach);
        otosaasOrder.setSignType("MD5");
        otosaasOrder.setOpenid( openid );
        otosaasOrder.setOrderId( orderId );
        otosaasOrder.setNonceStr(nonceStr);
        otosaasOrder.setSignType(signType);
        otosaasOrder.setTradeType("JSAPI");
        otosaasOrder.setGoodsTag( goodsTag );
        otosaasOrder.setTotalFee( totalFee );
        otosaasOrder.setProdType( prodTpey );
        otosaasOrder.setLimitPay("no_credit");
        otosaasOrder.setProductId( productId );
        otosaasOrder.setSceneInfo( sceneInfo );
        otosaasOrder.setOutTradeNo(outTradeNo);
        otosaasOrder.setDeviceInfo(deviceInfo);
        otosaasOrder.setNotifyUrl( notifyUrl );
        otosaasOrder.setTimeExpire( timeExpire );
        otosaasOrder.setProdSubType( prodSubType );
        otosaasOrder.setSpbillCreateIp( spbillCreateIp );
        otosaasOrder.setTimeStart(System.currentTimeMillis() + "");
        otosaasOrder.setOrderDatetime(SeemseUtil.getCurrentTime());
        return otosaasOrder;
    }

//    /**
//     * 获取订单详情
//     *
//     * @param dto
//     * @return
//     * @throws ApiException
//     */
//    @Override
//    public TblOtosaasOrder getOrderDetail(Dto dto) throws ApiException {
//        TblOtosaasOrder otosaasOrder = tbl_otosaas_orderDao.selectOne(dto);
//        if (otosaasOrder == null) {
//            throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
//        }
//        return otosaasOrder;
//    }

//    @Override
//    public void update(Tbl_otosaas_orderPO tbl_otosaas_orderPO) throws ApiException {
//        try {
//            this.tbl_otosaas_orderDao.updateByKey(tbl_otosaas_orderPO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
//    }

//    @Override
//    public void merge(Tbl_otosaas_orderPO tbl_otosaas_orderPO) throws Exception {
//        Dto dto = new HashDto();
//        dto.put("order_id", tbl_otosaas_orderPO.getOrder_id());
//        Tbl_otosaas_orderPO orderPO = this.tbl_otosaas_orderDao.selectOne(dto);
//        if (orderPO == null) {
//            this.tbl_otosaas_orderDao.insert(tbl_otosaas_orderPO);
//        } else {
//            tbl_otosaas_orderPO.setId(orderPO.getId());
//            this.tbl_otosaas_orderDao.updateByKey(tbl_otosaas_orderPO);
//        }
//    }

    @Override
    public String jointParam(Dto dto) {
        String OTOSAAS_LOGIN_URL = WXConfig.OTOSAAS_LOGIN_URL;
        String customerUserId = dto.getString("customerUserId");
        String customerUserPhone = dto.getString("customerUserPhone");
        String customerPayLoad = dto.getString("customerPayLoad");
        String timeStamp = (System.currentTimeMillis() / 1000) + "";

        //获取签名(参与签名的参数为：appkey,customerUserId, customerUserPhone, timestamp)
        Map<String, String> map = new HashMap<>();
        map.put("appKey", WXConfig.JOINT_LOGIN_APPKEY);
        map.put("customerUserId", customerUserId);
        map.put("customerUserPhone", customerUserPhone);
        map.put("customerPayLoad", customerPayLoad);
        map.put("timeStamp", timeStamp);
        map.put("appSecret", WXConfig.JOINT_LOGIN_APPSECRET);
        String sign = getSign4OtoSaaS(map);
        OTOSAAS_LOGIN_URL = OTOSAAS_LOGIN_URL.replace("APPKEY", WXConfig.JOINT_LOGIN_APPKEY)
                .replace("CUSTOMERUSERID", customerUserId).replace("CUSTOMERUSERPHONE", customerUserPhone)
                .replace("TIMESTAMP", timeStamp).replace("SIGN", sign)
                .replace("CUSTOMERPAYLOAD", customerPayLoad);
        return OTOSAAS_LOGIN_URL;
    }


    /**
     * 加签
     * 第一步获取字符串A(customerUserId + customerUserPhone + timeStamp)
     * 第二步获取字符串B(appKey + 字符串A + appSecret)
     * 第三步获取字符串C（对字符串B进行md5哈希）
     */
    @Override
    public String getSign4OtoSaaS(Map<String, String> map) {
        String strC = null;
        try {
            String strA = map.get("customerUserId")
                    + map.get("customerUserPhone")
                    + map.get("timeStamp");
            String strB = map.get("appKey") + strA + map.get("appSecret");
            strC = WxUtils.MD5(strB);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strC;
    }

    @Override
    public TblOtosaasOrder getOrderDetail2(Dto dto) {
        String orderId = dto.getString( "orderId" );
        TblOtosaasOrderExample example=new TblOtosaasOrderExample();
        TblOtosaasOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo( orderId );
        List<TblOtosaasOrder> orders = tblOtosaasOrderMapper.selectByExample( example );
        if(orders.size()>0){
            return orders.get( 0 );
        }else {
            return new TblOtosaasOrder();
        }
    }

}
