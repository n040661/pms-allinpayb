package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.TUser;
import com.dominator.mapper.TUserMapper;
import com.dominator.redis.async.EventProducer;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.sms.SmsUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.dominator.utils.sms.ShortUrlUtil.HttpUtil.getShortUrl;

@Service
public class MsgSendCenter {

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private TUserMapper tUserMapper;

    private static Logger loggerFactory = LoggerFactory.getLogger(MsgSendCenter.class);
    /**
     * 未缴费账单
     * @param dto
     * @throws ApiException
     */
    public  void sendNBillNotice(Dto dto) throws ApiException {

        //修改成功后发送消息和微信模版信息
        Dto smsDto = new HashDto();
        String bill_year_month = dto.getString("bill_year_month");
        String[] s = bill_year_month.split("-");
        String str = s[1].startsWith("0") ? s[1].substring(1, 2) : s[1];
        smsDto.put("month", str);
        smsDto.put("type",dto.getString("bill_name"));
        smsDto.put("money", dto.getString("total_fee"));
        smsDto.put("phone_num", dto.getString("phone_num"));
        smsDto.put("stype","1");
        //http://www.pmssaas.com/h5/?appId=wx7dbda27aa5a445ed/#/app/business/bill-detail?billId=1
        String appid = dto.getString("app_id");
        String bill_id = dto.getString("id");
        String garden_id = dto.getString("garden_id");
        JSONObject json = new JSONObject();
        json.accumulate("bill_id",bill_id);
        json.accumulate("garden_id",garden_id);
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }
        String longUrl = SysConfig.BILL_DETAIL_URL+"?appId="+appid+"&masterplate="+detail;

        smsDto.put("url", getShortUrl(longUrl));
        SmsUtils.sendNoticeSms(smsDto);

        /*//微信模版
        if(StringUtils.isNotEmpty(dto.getString("open_id"))){
            TemplateMsg templateMsg = new TemplateMsg();
            templateMsg.setAppid(dto.getString("app_id"));
            templateMsg.setTouser(dto.getString("open_id"));
            templateMsg.setFirst(str+"月"+dto.getString("bill_name")+"已出账");
            templateMsg.setKeyword1(dto.getString("contact_address"));
            templateMsg.setKeyword2(dto.getString("company_name"));
            templateMsg.setKeyword3(dto.getString("bill_name"));
            templateMsg.setKeyword4(dto.getString("bill_is_paid"));
            templateMsg.setKeyword5(dto.getString("total_fee"));
            templateMsg.setUrl(longUrl);
            //templateMsg.setUrl(propertiesLoader.getProperty("BILL_DETAIL_URL")+"?appId=wx7dbda27aa5a445ed&masterplate="+detail);
            templateMsg.setType("1");
            //msgSendService.bill4NoPayNotice(templateMsg);
        }*/
    }

    /**
     * 已缴费账单
     * @param dto
     * @throws ApiException
     */
    public  void sendYBillNotice(Dto dto) throws ApiException {
        //缴费过后的短信通知
        loggerFactory.info("**********账单发送开始*********");
        Dto smsDto = new HashDto();
        String bill_year_month = dto.getString("bill_year_month");
        String[] s = bill_year_month.split("-");
        String str = s[1].startsWith("0") ? s[1].substring(1, 2) : s[1];
        smsDto.put("month", str);
        smsDto.put("type",dto.getString("bill_name"));
        smsDto.put("money", dto.getString("total_fee"));
        smsDto.put("phone_num", dto.getString("phone_num"));
        smsDto.put("stype","2");
        String appid = dto.getString("app_id");
        String bill_id = dto.getString("id");
        String garden_id = dto.getString("garden_id");
        JSONObject json = new JSONObject();
        json.accumulate("bill_id",bill_id);
        json.accumulate("garden_id",garden_id);
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }
        String longUrl = SysConfig.BILL_DETAIL_URL+"?appId="+appid+"&masterplate="+detail;
        //smsDto.put("url",getShortUrl("url="+longUrl));
        String shorturl = getShortUrl(longUrl);
        smsDto.put("url",shorturl);
        loggerFactory.info("**********已缴费账单生成的短链接***********"+shorturl);
        SmsUtils.sendNoticeSms(smsDto);

       /* //微信模版
        if(StringUtils.isNotEmpty(dto.getString("open_id"))){
            TemplateMsg templateMsg = new TemplateMsg();
            templateMsg.setAppid(dto.getString("app_id"));
            templateMsg.setTouser(dto.getString("open_id"));
            templateMsg.setFirst(str+"月"+dto.getString("bill_name")+"已缴清");
            templateMsg.setKeyword1(dto.getString("contact_address"));
            templateMsg.setKeyword2(dto.getString("company_name"));
            templateMsg.setKeyword3(dto.getString("bill_name"));
            templateMsg.setKeyword4(dto.getString("bill_is_paid"));
            templateMsg.setKeyword5(dto.getString("total_fee"));
            templateMsg.setUrl(longUrl);
            templateMsg.setType("1");
            loggerFactory.info("****************templateMsg********");
           // msgSendService.bill4NoPayNotice(templateMsg);
        }*/
    }

    /**
     * 报修通知 申请
     * @param dto
     * @throws ApiException
     */
    public void sendNRepairNotice(Dto dto) throws ApiException {
        //发送消息给园区管理员
        String repairId = dto.getString("repair_id");
        Dto gardenDto = new HashDto();
        String appid = dto.getString("app_id");
        gardenDto.put("stype","3");
        gardenDto.put("phone_num",dto.getString("phone_num"));

        /*JSONObject json = new JSONObject();
        json.accumulate("repairId",dto.getString("repair_id"));
        json.accumulate("autheType","garden");
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }*/
        //https://test.pmssaas.com/h5/maintain/?appid=wxb17fbd493a3f9c8d/#/maintain/detail?repairId=f1c829f022f24b57a60b690f134df157&authetype=garden
        String longUrl = SysConfig.REPAIR_DETAIL_URL+"?appId="+appid+"/#/maintain/detail?&repairId="+repairId+"&authetype=garden";
        String shorturl = getShortUrl(longUrl);
        gardenDto.put("url",shorturl);
        loggerFactory.info("**********报修通知申请生成的短链接***********"+shorturl);
        SmsUtils.sendNoticeSms(gardenDto);

        /*//发送微信模版消息
        if(StringUtils.isNotEmpty(dto.getString("open_id"))) {
            TemplateMsg templateMsg = new TemplateMsg();
            templateMsg.setAppid(dto.getString("app_id"));
            templateMsg.setTouser(dto.getString("open_id"));
            templateMsg.setFirst("您收到新的报修申请，请到园区管理后台处理");
            templateMsg.setKeyword1(dto.getString("repair_content"));
            templateMsg.setKeyword2(dto.getString("company_name"));
            templateMsg.setKeyword3(dto.getString("repair_time"));
            templateMsg.setUrl(longUrl);
            templateMsg.setType("3");
           // msgSendService.repairNotice(templateMsg);
        }*/
    }

    /**
     * 报修通知 处理中
     * @param dto
     * @throws ApiException
     */
    public void sendRepairingNotice(Dto dto)throws ApiException {

        String appid = dto.getString("app_id");
        String repairId = dto.getString("id");
        //发送消息给到分配的园区管理员
        Dto gardenDto = new HashDto();
        gardenDto.put("stype","4");
        gardenDto.put("phone_num",dto.getString("garden_user_phone"));
        /*JSONObject json = new JSONObject();
        json.accumulate("repairId",dto.getString("id"));
        json.accumulate("autheType","garden");
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }*/
        String gardenlongUrl = SysConfig.REPAIR_DETAIL_URL+"?appId="+appid+"/#/maintain/detail?&repairId="+repairId+"&authetype=garden";
        String shorturl = getShortUrl(gardenlongUrl);
        loggerFactory.info("**********报修通知处理中(发送给园区管理员)生成的短链接***********"+shorturl);
        gardenDto.put("url",shorturl);
        SmsUtils.sendNoticeSms(gardenDto);

        //发送消息给到报修人
        Dto companyDto = new HashDto();
        companyDto.put("stype","5");
        companyDto.put("phone_num",dto.getString("company_user_phone"));

        /*JSONObject json1 = new JSONObject();
        json1.accumulate("repairId",dto.getString("repair_id"));
        json1.accumulate("authetype","garden");
        String companydetail = json1.toString();
        try {
            companydetail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }*/
        String companylongUrl = SysConfig.REPAIR_DETAIL_URL+"?appId="+appid+"/#/maintain/detail?&repairId="+repairId+"&authetype=company";
        String companyshorturl = getShortUrl(companylongUrl);
        loggerFactory.info("**********报修通知处理中(发送给报修人)生成的短链接***********"+companyshorturl);
        companyDto.put("url",companyshorturl);
        SmsUtils.sendNoticeSms(companyDto);


        //发送微信信息给到分配的园区员工
        /*if(StringUtils.isNotEmpty(dto.getString("garden_open_id"))) {
            TemplateMsg templateMsg = new TemplateMsg();
            templateMsg.setAppid(dto.getString("app_id"));
            templateMsg.setTouser(dto.getString("garden_open_id"));
            templateMsg.setFirst("您有新的维修任务，请及时处理");
            templateMsg.setKeyword1(dto.getString("nick_name"));
            templateMsg.setKeyword2(dto.getString("company_user_phone"));
            templateMsg.setKeyword3(DatetoStringFormat(dto.getDate("assign_time"), "yyyy年MM月dd日 HH:mm"));
            templateMsg.setKeyword4(dto.getString("repair_content"));
            templateMsg.setUrl(gardenlongUrl);
            templateMsg.setType("2");
            //msgSendService.assignTaskNotice(templateMsg);
        }*/

        /*//发送微信消息给到报修人
        if(StringUtils.isNotEmpty(dto.getString("company_open_id"))) {
            TemplateMsg templateMsg1 = new TemplateMsg();
            templateMsg1.setAppid(dto.getString("app_id"));
            templateMsg1.setTouser(dto.getString("company_open_id"));
            templateMsg1.setFirst("您好，园区正在处理您报修的情况");
            templateMsg1.setKeyword1(dto.getString("repair_content"));
            templateMsg1.setKeyword2(DatetoStringFormat(dto.getDate("repair_time"), "yyyy年MM月hh日 HH:mm"));
            templateMsg1.setKeyword3("处理中");
            templateMsg1.setUrl(companylongUrl);
            templateMsg1.setType("4");
           // msgSendService.taskReceiveNotice(templateMsg1);
        }*/
    }

    /**
     * 报修通知 已经处理
     * @param dto
     * @throws ApiException
     */
    public void sendYRepairNotice(Dto dto)throws ApiException {
        String appid = dto.getString("app_id");
        String repairId = dto.getString("repair_id");
        //发送消息给到报修人
        Dto companyDto = new HashDto();
        companyDto.put("stype","6");
        companyDto.put("phone_num",dto.getString("company_user_phone"));
       /* JSONObject json = new JSONObject();
        json.accumulate("repairId",dto.getString("repair_id"));
        json.accumulate("autheType","company");
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }*/
        String companylongUrl = SysConfig.REPAIR_DETAIL_URL+"?appId="+appid+"/#/maintain/detail?&repairId="+repairId+"&authetype=company";
        String companyshorturl = getShortUrl(companylongUrl);
        loggerFactory.info("**********报修通知已经处理生成的短链接***********"+companylongUrl);
        companyDto.put("url",companyshorturl);
        SmsUtils.sendNoticeSms(companyDto);

        /*//发送微信内容模版
        if(StringUtils.isNotEmpty(dto.getString("open_id"))) {
            TemplateMsg templateMsg1 = new TemplateMsg();
            templateMsg1.setAppid(dto.getString("app_id"));
            templateMsg1.setTouser(dto.getString("company_open_id"));
            templateMsg1.setFirst("您好，园区已经成功处理您报修的情况");
            templateMsg1.setKeyword1(dto.getString("repair_content"));
            templateMsg1.setKeyword2(DatetoStringFormat(dto.getDate("repair_time"), "yyyy年MM月hh日 HH:mm"));
            templateMsg1.setKeyword3("已处理");
            templateMsg1.setUrl(companylongUrl);
            templateMsg1.setType("4");
            //msgSendService.taskDealedNotice(templateMsg1);
        }*/
    }

    /**
     * 被邀请访客通知
     * @param dto
     * @throws ApiException
     */
    public void bevisitor(Dto dto)throws ApiException {

        //发送验证通过通知短信给邀请人
        Dto companyDto = new HashDto();
        companyDto.put("stype",9);
        companyDto.put("company",dto.getString("company_name"));
        companyDto.put("day",dto.getString("date"));
        companyDto.put("address",dto.getString("address"));
        TUser userPO = tUserMapper.selectByPrimaryKey(dto.getString("user_id"));
        if(userPO!=null){
            companyDto.put("username",userPO.getNickName());
        }
        companyDto.put("phone_num",dto.getString("visitor_phone"));
        JSONObject json = new JSONObject();
        json.accumulate("id",dto.getString("id"));
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }
//        String longUrl = "https://www.baidu.com";
        String longUrl = SysConfig.VISITOR_DETAIL_URL+"visitorId="+dto.getString("id")+"&appId="+dto.getString("app_id");
        String shorturl = getShortUrl(longUrl);
        loggerFactory.info("**********报修通知已经处理生成的短链接***********"+longUrl);
        companyDto.put("url",shorturl);
        SmsUtils.sendNoticeSms(companyDto);

    }


    /**
     * 访客通知
     * @param dto
     * @throws ApiException
     */
    public void visitor(Dto dto)throws ApiException {

        //发送验证通过通知短信给邀请人
        Dto companyDto = new HashDto();
        String type = dto.getString("type");
        if(type.equals("gq")){
            companyDto.put("stype","8");//过期发送的短信模版ID
        }else if(type.equals("tg")){
            companyDto.put("stype","7");//通过发送的短信模版ID
        }
        companyDto.put("username",dto.getString("visitor_name"));//访客姓名
        String sex =  "先生/女士";
        companyDto.put("xb",sex);
        companyDto.put("phone",dto.getString("visitor_phone"));//访客手机号
        //T_userPO userPO = t_userDao.selectByKey(t_visitorPO.getUser_id());
        companyDto.put("phone_num",dto.getString("user_phone_num"));
        JSONObject json = new JSONObject();
        json.accumulate("id",dto.getString("id"));
        String detail = json.toString();
        try {
            detail = URLEncoder.encode(detail, "UTF-8").replace("%2C",",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("104","转义错误");
        }
        String longUrl = SysConfig.VISITOR_DETAIL_URL+"visitorId="+dto.getString("id")+"&appId="+dto.getString("app_id");
        String shorturl = getShortUrl(longUrl);
        loggerFactory.info("**********报修通知已经处理生成的短链接***********"+longUrl);
        companyDto.put("url",shorturl);
        SmsUtils.sendNoticeSms(companyDto);


        //发送模版微信信息
        /*if(StringUtils.isNotEmpty(dto.getString("open_id"))) {
            if (type.equals("tg")) {
                TemplateMsg templateMsg1 = new TemplateMsg();
                templateMsg1.setAppid(dto.getString("app_id"));
                templateMsg1.setTouser(dto.getString("open_id"));
                templateMsg1.setFirst("您的访客已进入园区");
                templateMsg1.setKeyword1(dto.getString("visitor_name") + "(" + dto.getString("visitor_phone") + ")");
                templateMsg1.setKeyword2(DatetoStringFormat(dto.getDate("fact_time"), "yyyy年MM月hh日 HH:mm"));
                templateMsg1.setUrl(longUrl);
                templateMsg1.setType("5");
                //msgSendService.taskDealedNotice(templateMsg1);
            } else if (type.equals("gq")) {
                TemplateMsg templateMsg1 = new TemplateMsg();
                templateMsg1.setAppid(dto.getString("app_id"));
                templateMsg1.setTouser(dto.getString("open_id"));
                String name = dto.getString("visitor_name") + sex + "(" + dto.getString("visitor_phone") + ")";
                templateMsg1.setFirst("您好，" + name + "的通行证已过期");
                templateMsg1.setKeyword1("普通预约");
                templateMsg1.setKeyword2(dto.getString("expect_time"));
                templateMsg1.setKeyword3(dto.getString("company_address"));
                templateMsg1.setUrl(longUrl);
                templateMsg1.setType("6");
                //msgSendService.taskDealedNotice(templateMsg1);
            }
        }*/

    }

    /**
     * 企业开户通知
     * @param dto
     * @throws ApiException
     */
    public void businessopen(Dto dto)throws ApiException {

        //发送验证通过通知短信给企业开户的企业联系人
        Dto openDto = new HashDto();
        openDto.putAll(dto);
        openDto.put("stype",10);
        SmsUtils.sendNoticeSms(openDto);

    }
}
