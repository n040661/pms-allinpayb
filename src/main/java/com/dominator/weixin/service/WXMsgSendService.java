//package com.dominator.weixin.service;
//
//import com.dominFramework.core.typewrap.Dto;
//import com.dominator.mybatis.dao.Tbl_wxopen_templatePO;
//import com.dominator.weixin.domain.TemplateMsg;
//
//public interface WXMsgSendService {
//
//    /**
//     * 账单通知（待缴费）
//     */
//    public  void bill4NoPayNotice(TemplateMsg templateMsg);
//
//    /**
//     * 账单通知（已缴费）
//     */
//
//    public void bill4PayedNotice(TemplateMsg templateMsg);
//
//    /**
//     * 报修通知（新报修通知提醒）
//     */
//
//    public void repairNotice(TemplateMsg templateMsg);
//
//    /**
//     * 维修任务提醒
//     */
//
//    public void assignTaskNotice(TemplateMsg templateMsg);
//    /**
//     * 报修受理提醒（处理中）
//     */
//
//    public void taskReceiveNotice(TemplateMsg templateMsg);
//    /**
//     * 报修受理提醒(已处理)
//     */
//
//    public void taskDealedNotice(TemplateMsg templateMsg);
//
//    /**
//     * 访客到达通知
//     */
//
//    public void visitorArrivalNotice(TemplateMsg templateMsg);
//
//    /**
//     * #预约失效通知
//     */
//
//    public void dateExpireNotice(TemplateMsg templateMsg);
//
//
//    public Tbl_wxopen_templatePO getTemplateMsgPO(Dto dto);
//}
