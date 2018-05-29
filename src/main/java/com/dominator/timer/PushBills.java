//package com.dominator.timer;//package com.myutil.timer;
//
//import com.dominFramework.core.typewrap.Dto;
//import com.dominFramework.core.typewrap.impl.HashDto;
//import com.dominator.mybatis.ext.dao.TCompanyBillDao;
//import com.dominator.serviceImpl.MsgSendCenter;
//import com.dominator.utils.system.PropertiesLoader;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//
///**
// * 基于注解的定时器
// */
//@Component
//public class PushBills {
//
//    private static Logger logger = LoggerFactory.getLogger(PushBills.class);
//
//    private static final PropertiesLoader propertiesLoader = new PropertiesLoader("sysconfig.properties");
//
//    @Autowired
//    private TCompanyBillDao tCompanyBillDaoExt;
//
//    @Autowired
//    private MsgSendCenter msgSendCenter;
//    /**
//     * 定时计算。每天凌晨 01:00 执行一次
//     */
////    @Scheduled(cron = "0 0 1 * * *")
////    public void show(){
////        System.out.println("Annotation：is show run");
////    }
//
//    /**
//     * 心跳更新。启动时执行一次，之后每隔2秒执行一次
//     */
////    @Scheduled(fixedRate = 1000*2)
////    public void print(){
////        System.out.println("Annotation：print run");
//    //   */5 * * * * ?
////    }
//
//
//    /**
//     * 定时计算。每天凌晨 07:00 执行一次 推送账单
//     */
//   //@Scheduled(cron = "*/5 * * * * ?")
//    @Scheduled(fixedRate = 1000*60*5)
//    @Transactional
//    public void push() {
//        try {
//            logger.info("push：start");
//            //获取需要推送的公司账单
//            Dto dto1 = new HashDto();
//            dto1.put("company_admin_id",propertiesLoader.getProperty("company_admin_id"));
//            List<Dto> list = tCompanyBillDaoExt.selectpush(dto1);
//
//            tCompanyBillDaoExt.push();//更新数据
//            for (Dto dto : list) {
//                msgSendCenter.sendNBillNotice(dto);
//            }
//
//
//        } catch (Exception e) {
//            logger.error("push companybill have error is " + e);
//        }
//
//        logger.info("push：end");
//    }
//}
