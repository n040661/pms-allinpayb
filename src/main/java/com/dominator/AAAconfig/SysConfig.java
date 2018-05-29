package com.dominator.AAAconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sys")
public class SysConfig {

    public static int TokenExpiresTime;

    public static int RedisTokenExpiresTime;

    public static String CommonRole;

    public static String DoorGuardId;

    public static String GardenEmployeeId;

    public static String PayModuleId;

    public static String CompanyEmployeeId;

    public static String QYAccessKeyId;

    public static String QYAccessKeySecret;

    public static String QYZoneKey;

    public static String QYBucketName;

    public static String QYHttpUrl;

    public static String MailSmtpAuth;

    public static String MailSmtpHost;

    public static String MailUser;

    public static String MailPassword;

    public static String MailName;

    public static String ApiKey;

    public static String YZM;

    public static String ZDNOT;

    public static String ZDYES;

    public static String BXNOT;

    public static String BXRDOING;

    public static String BXSDOING;

    public static String BXYES;

    public static String OPEN;

    public static String FKGQ;

    public static String FKYES;

    public static String BEFK;

    public static String CONSULT;

    public static String HEAD_IMG;

    public static String HEAD_N_IMG;

    public static String PROPERTY_LOGO;

    public static String companyuser;

    public static String gardenuser;

    public static String company;

    public static String paidbills;

    public static String unpaidbills;

    public static String companyvisitors;

    public static String gardenvisitors;

//    以下为分环境的变量

    public static Boolean isSendMsg;

    public static String VerifyUrl;

    public static String OTOSAAS_BASE_URL;

    public static String OTOSAAS_BOSS_URL;

    public static String OTOSAAS_INDEX;

    public static String OTOSAAS_LOGIN;

    public static String OTOSAAS_LOGOUT;

    public static String JOINT_LOGIN_APPKEY;

    public static String JOINT_LOGIN_APPSECRET;

    public static String OTOSAAS_LOGIN_URL;

    public static String VISITOR_QR_URL;

    public static String BILL_DETAIL_URL;

    public static String REPAIR_DETAIL_URL;

    public static String VISITOR_DETAIL_URL;

    public void setCommonRole(String commonRole) {
        CommonRole = commonRole;
    }

    public void setDoorGuardId(String doorGuardId) {
        DoorGuardId = doorGuardId;
    }

    public void setGardenEmployeeId(String gardenEmployeeId) {
        GardenEmployeeId = gardenEmployeeId;
    }

    public void setCompanyEmployeeId(String companyEmployeeId) {
        CompanyEmployeeId = companyEmployeeId;
    }

    public void setQYAccessKeyId(String QYAccessKeyId) {
        SysConfig.QYAccessKeyId = QYAccessKeyId;
    }

    public void setQYAccessKeySecret(String QYAccessKeySecret) {
        SysConfig.QYAccessKeySecret = QYAccessKeySecret;
    }

    public void setQYZoneKey(String QYZoneKey) {
        SysConfig.QYZoneKey = QYZoneKey;
    }

    public void setQYBucketName(String QYBucketName) {
        SysConfig.QYBucketName = QYBucketName;
    }

    public void setQYHttpUrl(String QYHttpUrl) {
        SysConfig.QYHttpUrl = QYHttpUrl;
    }

    public void setMailSmtpAuth(String mailSmtpAuth) {
        MailSmtpAuth = mailSmtpAuth;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        MailSmtpHost = mailSmtpHost;
    }

    public void setMailUser(String mailUser) {
        MailUser = mailUser;
    }

    public void setMailPassword(String mailPassword) {
        MailPassword = mailPassword;
    }

    public void setMailName(String mailName) {
        MailName = mailName;
    }

    public void setApiKey(String apiKey) {
        ApiKey = apiKey;
    }

    public void setYZM(String YZM) {
        SysConfig.YZM = YZM;
    }

    public void setZDNOT(String ZDNOT) {
        SysConfig.ZDNOT = ZDNOT;
    }

    public void setZDYES(String ZDYES) {
        SysConfig.ZDYES = ZDYES;
    }

    public void setBXNOT(String BXNOT) {
        SysConfig.BXNOT = BXNOT;
    }

    public void setBXRDOING(String BXRDOING) {
        SysConfig.BXRDOING = BXRDOING;
    }

    public void setBXSDOING(String BXSDOING) {
        SysConfig.BXSDOING = BXSDOING;
    }

    public void setBXYES(String BXYES) {
        SysConfig.BXYES = BXYES;
    }

    public void setOPEN(String OPEN) {
        SysConfig.OPEN = OPEN;
    }

    public void setFKGQ(String FKGQ) {
        SysConfig.FKGQ = FKGQ;
    }

    public void setBEFK(String BEFK) {
        SysConfig.BEFK = BEFK;
    }

    public void setHeadImg(String headImg) {
        HEAD_IMG = headImg;
    }

    public void setHeadNImg(String headNImg) {
        HEAD_N_IMG = headNImg;
    }

    public void setPropertyLogo(String propertyLogo) {
        PROPERTY_LOGO = propertyLogo;
        this.BEFK = BEFK;
    }

    public String getVerifyUrl() {
        return VerifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        VerifyUrl = verifyUrl;
    }

    public void setOtosaasBaseUrl(String otosaasBaseUrl) {
        OTOSAAS_BASE_URL = otosaasBaseUrl;
    }

    public void setOtosaasBossUrl(String otosaasBossUrl) {
        OTOSAAS_BOSS_URL = otosaasBossUrl;
    }

    public void setOtosaasIndex(String otosaasIndex) {
        OTOSAAS_INDEX = otosaasIndex;
    }

    public void setOtosaasLogin(String otosaasLogin) {
        OTOSAAS_LOGIN = otosaasLogin;
    }

    public void setOtosaasLogout(String otosaasLogout) {
        OTOSAAS_LOGOUT = otosaasLogout;
    }

    public void setJointLoginAppkey(String jointLoginAppkey) {
        JOINT_LOGIN_APPKEY = jointLoginAppkey;
    }

    public void setJointLoginAppsecret(String jointLoginAppsecret) {
        JOINT_LOGIN_APPSECRET = jointLoginAppsecret;
    }

    public void setOtosaasLoginUrl(String otosaasLoginUrl) {
        OTOSAAS_LOGIN_URL = otosaasLoginUrl;
    }

    public void setVisitorQrUrl(String visitorQrUrl) {
        VISITOR_QR_URL = visitorQrUrl;
    }

    public void setBillDetailUrl(String billDetailUrl) {
        BILL_DETAIL_URL = billDetailUrl;
    }

    public void setRepairDetailUrl(String repairDetailUrl) {
        REPAIR_DETAIL_URL = repairDetailUrl;
    }

    public void setVisitorDetailUrl(String visitorDetailUrl) {
        VISITOR_DETAIL_URL = visitorDetailUrl;
    }

    public void setCompanyuser(String companyuser) {
        SysConfig.companyuser = companyuser;
    }

    public void setGardenuser(String gardenuser) {
        SysConfig.gardenuser = gardenuser;
    }

    public void setCompany(String company) {
        SysConfig.company = company;
    }

    public void setPaidbills(String paidbills) {
        SysConfig.paidbills = paidbills;
    }

    public void setUnpaidbills(String unpaidbills) {
        SysConfig.unpaidbills = unpaidbills;
    }

    public void setCompanyvisitors(String companyvisitors) {
        SysConfig.companyvisitors = companyvisitors;
    }

    public void setGardenvisitors(String gardenvisitors) {
        SysConfig.gardenvisitors = gardenvisitors;
    }

    public void setFKYES(String FKYES) {
        SysConfig.FKYES = FKYES;
    }

    public void setTokenExpiresTime(int tokenExpiresTime) {
        TokenExpiresTime = tokenExpiresTime;
    }

    public void setRedisTokenExpiresTime(int redisTokenExpiresTime) {
        RedisTokenExpiresTime = redisTokenExpiresTime;
    }

    public void setIsSendMsg(Boolean isSendMsg) {
        SysConfig.isSendMsg = isSendMsg;
    }

    public void setCONSULT(String CONSULT) {
        SysConfig.CONSULT = CONSULT;
    }

    public void setPayModuleId(String payModuleId) {
        PayModuleId = payModuleId;
    }
}
