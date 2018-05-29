//package com.dominator.weixin.serviceImpl;
//
//import com.dominFramework.core.typewrap.Dto;
//import com.dominFramework.core.typewrap.impl.HashDto;
//import com.dominator.mybatis.dao.Tbl_public_accountPO;
//import com.dominator.mybatis.dao.Tbl_wxopen_templateDao;
//import com.dominator.mybatis.dao.Tbl_wxopen_templatePO;
//import com.dominator.utils.system.PropertiesLoader;
//import com.dominator.weixin.domain.TemplateMsg;
//import com.dominator.weixin.service.PublicAccountService;
//import com.dominator.weixin.service.WXMsgSendService;
//import com.dominator.weixin.util.HttpUtils;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class WXMsgSendServiceImpl implements WXMsgSendService {
//
//    public final static String URL = "http://pmsaas.otosaas.com/pmsaas/";
//
//    @Autowired
//    private Tbl_wxopen_templateDao tbl_wxopen_templateDao;
//
//    @Autowired
//    private PublicAccountService publicAccountService;
//
//    private PropertiesLoader propertiesLoader = new PropertiesLoader("wechatconfig.properties");
//
//
//    /**
//     * 账单通知（待缴费）
//     */
//    @Override
//    public  void bill4NoPayNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser =  templateMsg.getTouser();
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID,templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote4 = new JSONObject();
//        jsonObject4Keynote4.put(TemplateMsg.VALUE,   templateMsg.getKeyword4());
//        jsonObject4Keynote4.put(TemplateMsg.COLOR, "#ff0000");
//
//        JSONObject jsonObject4Keynote5 = new JSONObject();
//        jsonObject4Keynote5.put(TemplateMsg.VALUE,   templateMsg.getKeyword5());
//        jsonObject4Keynote5.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4remark = new JSONObject();
//        jsonObject4remark.put(TemplateMsg.VALUE, "点击查看账单详情");
//        jsonObject4remark.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//        subJsonObject2.put(TemplateMsg.KEYWORD4, jsonObject4Keynote4);
//        subJsonObject2.put(TemplateMsg.KEYWORD5, jsonObject4Keynote5);
//        //subJsonObject2.put("remark",jsonObject4remark);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//    }
//
//
//    /**
//     * 账单通知（已缴费）
//     */
//    @Override
//    public void bill4PayedNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID, templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote4 = new JSONObject();
//        jsonObject4Keynote4.put(TemplateMsg.VALUE,   templateMsg.getKeyword4());
//        jsonObject4Keynote4.put(TemplateMsg.COLOR, "#ff0000");
//
//
//        JSONObject jsonObject4Keynote5 = new JSONObject();
//        jsonObject4Keynote5.put(TemplateMsg.VALUE,   templateMsg.getKeyword5());
//        jsonObject4Keynote5.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4remark = new JSONObject();
//        jsonObject4remark.put(TemplateMsg.VALUE, "点击查看账单详情");
//        jsonObject4remark.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//        subJsonObject2.put(TemplateMsg.KEYWORD4, jsonObject4Keynote4);
//        subJsonObject2.put(TemplateMsg.KEYWORD5, jsonObject4Keynote5);
//        //subJsonObject2.put("remark",jsonObject4remark);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//    }
//
//    /**
//     * 报修通知（新报修通知提醒）
//     */
//    @Override
//    public void repairNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID, templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//    }
//
//    /**
//     * 维修任务提醒
//     */
//    @Override
//    public void assignTaskNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID, templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE, TemplateMsg.KEYWORD1);
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote4 = new JSONObject();
//        jsonObject4Keynote4.put(TemplateMsg.VALUE, templateMsg.getKeyword4());
//        jsonObject4Keynote4.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//        subJsonObject2.put(TemplateMsg.KEYWORD4, jsonObject4Keynote4);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//
//    }
//
//    /**
//     * 报修受理提醒（处理中）
//     */
//    @Override
//    public void taskReceiveNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID,templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, "#ff0000");
//
//
//        JSONObject jsonObject4remark = new JSONObject();
//        jsonObject4remark.put(TemplateMsg.VALUE, "点击查看报修详情！");
//        jsonObject4remark.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//        //subJsonObject2.put("remark",jsonObject4remark);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//
//    }
//
//    /**
//     * 报修受理提醒(已处理)
//     */
//    @Override
//    public void taskDealedNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID,templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, "#ff0000");
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//        //subJsonObject2.put("remark",jsonObject4remark);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//    }
//
//    /**
//     * 访客到达通知
//     */
//    @Override
//    public void visitorArrivalNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID,templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        //jsonObject4First.put(TemplateMsg.VALUE,"访客到达提醒");
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//    }
//
//
//    /**
//     * #预约失效通知
//     */
//    @Override
//    public void dateExpireNotice(TemplateMsg templateMsg) {
//        String appid = templateMsg.getAppid();
//        String touser = templateMsg.getTouser();
//        String SEND_MSG_TEMPLATE_URL = propertiesLoader.getProperty("MSG_TEMPLATE_URL")
//                + this.getAccessToken(appid);
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,templateMsg.getAppid());
//        dto.put(TemplateMsg.TYPE,templateMsg.getType());
//        Tbl_wxopen_templatePO templateMsgPO = this.getTemplateMsgPO(dto);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(TemplateMsg.TOUSER, touser);
//        jsonObject.put(TemplateMsg.TEMPLATE_ID,templateMsgPO.getTemplate_id());
//        jsonObject.put(TemplateMsg.URL, templateMsg.getUrl());
//
//        JSONObject subJsonObject2 = new JSONObject();
//        JSONObject jsonObject4First = new JSONObject();
//        //jsonObject4First.put(TemplateMsg.VALUE,"访客到达提醒");
//        jsonObject4First.put(TemplateMsg.VALUE, templateMsg.getFirst());
//        jsonObject4First.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote1 = new JSONObject();
//        jsonObject4Keynote1.put(TemplateMsg.VALUE,   templateMsg.getKeyword1());
//        jsonObject4Keynote1.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote2 = new JSONObject();
//        jsonObject4Keynote2.put(TemplateMsg.VALUE,   templateMsg.getKeyword2());
//        jsonObject4Keynote2.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4Keynote3 = new JSONObject();
//        jsonObject4Keynote3.put(TemplateMsg.VALUE,   templateMsg.getKeyword3());
//        jsonObject4Keynote3.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        JSONObject jsonObject4remark = new JSONObject();
//        jsonObject4remark.put(TemplateMsg.VALUE, " 点击查看访客信息");
//        jsonObject4remark.put(TemplateMsg.COLOR, TemplateMsg.COLOR_VALUE);
//
//        subJsonObject2.put( TemplateMsg.FIRST, jsonObject4First);
//        subJsonObject2.put(TemplateMsg.KEYWORD1, jsonObject4Keynote1);
//        subJsonObject2.put(TemplateMsg.KEYWORD2, jsonObject4Keynote2);
//        subJsonObject2.put(TemplateMsg.KEYWORD3, jsonObject4Keynote3);
//        //subJsonObject2.put("remark",jsonObject4remark);
//
//        jsonObject.put(TemplateMsg.DATA, subJsonObject2);
//
//        HttpUtils.doPost(SEND_MSG_TEMPLATE_URL, jsonObject);
//    }
//
//    public  String getAccessToken(String appid) {
//        Dto dto=new HashDto();
//        dto.put(TemplateMsg.APPID,appid);
//        Tbl_public_accountPO accountPO = this.publicAccountService.find(appid);
//        //根据appid获取appSecret
//        String MENU_ACCESS_TOKEN_URL = propertiesLoader.getProperty("MENU_ACCESS_TOKEN_URL")
//                + appid + "&secret=" + accountPO.getApp_secret();
//        JSONObject jsonObject = HttpUtils.doPost(MENU_ACCESS_TOKEN_URL, new JSONObject());
//        return (String) jsonObject.get("access_token");
//    }
//
//    @Override
//    public Tbl_wxopen_templatePO getTemplateMsgPO(Dto dto){
//        return this.tbl_wxopen_templateDao.selectOne(dto);
//    }
//
//}
