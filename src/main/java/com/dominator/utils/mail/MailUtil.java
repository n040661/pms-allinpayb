package com.dominator.utils.mail;


import com.dominator.AAAconfig.SysConfig;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.exception.ApiException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailUtil {

    public static final Properties props = new Properties();

    public static void sendEmail(String toUser, String title, String content) {
        try {
            // 配置发送邮件的环境属性
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
            props.put("mail.smtp.auth", SysConfig.MailSmtpAuth);
            props.put("mail.smtp.host", SysConfig.MailSmtpHost);
            // 发件人的账号
            props.put("mail.user", SysConfig.MailUser);
            // 访问SMTP服务时需要提供的密码
            props.put("mail.password", SysConfig.MailPassword);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            System.out.println("======");
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(
                    props.getProperty("mail.user"), SysConfig.MailUser, "UTF-8");
            message.setFrom(form);

            // 设置收件人
            InternetAddress to = new InternetAddress(toUser);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            // 设置抄送
//        InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
//        message.setRecipient(MimeMessage.RecipientType.CC, cc);

            // 设置密送，其他的收件人不能看到密送的邮件地址
//        InternetAddress bcc = new InternetAddress("aaaaa@163.com");
//        message.setRecipient(MimeMessage.RecipientType.CC, bcc);

            // 设置邮件标题
            message.setSubject(title);

            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 发送邮件
            Transport.send(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_EMAIL_ERROR.getCode(),ReqEnums.REQ_EMAIL_ERROR.getMsg());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_EMAIL_ERROR.getCode(),ReqEnums.REQ_EMAIL_ERROR.getMsg());
        }
    }


    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
        MailUtil.sendEmail("zhangwei@pms-saas.onaliyun.com","test","<div><font color='red'>TEST</font><a href='http://www.baidu.com'>测试的HTML邮件</a></div>");
    }
}
