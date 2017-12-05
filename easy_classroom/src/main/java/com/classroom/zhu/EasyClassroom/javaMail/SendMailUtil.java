package com.classroom.zhu.EasyClassroom.javaMail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Created by 12801 on 2017/11/21.
 */
@Component
public class SendMailUtil {

    @Value("#{config.project.senderName}")
    private String senderName;
    @Value("#{config.project.senderPassword}")
    private String senderPassword;
    @Value("#{config.project.smtpServer}")
    private String smtpServer;

    private static final Logger LOG= LoggerFactory.getLogger(SendMailUtil.class);

    /**
     * 发送邮件到指定邮箱
     */
    public void send(String fromAddress,String toAddress)throws Exception{
        /**
         * 第一步：创建session，包含邮件服务器网络连接信息
         */
        Properties props = new Properties();
        //指定邮件的传输协议，stmp；同时通过验证
        props.setProperty("mail.transport.protocol","stmp");
        props.setProperty("mail.stmp.auth","true");
        Session session = Session.getDefaultInstance(props);

        //！！！！需要登陆邮件服务器的方法
        /*
        SendMailAuthenticator au = new SendMailAuthenticator();
        au.check(user, password); // 认证 (用户名和密码)
        props.put("mail.smtp.host", smtpHost); // 设置smtp服务器
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, au);// 创建session
        */
        //开启调试模式
        //session.setDebug(true);

        /**
         * 第二步：编辑邮件内容
         */
        Message message = new MimeMessage(session);
        //设置邮件消息头
        message.setFrom(new InternetAddress(fromAddress));//发件人 如1280157277@qq.com
        message.setRecipients(RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject("JavaMail邮件测试");
        //设置邮件消息的内容、包含附件
        Multipart msgPart = new MimeMultipart("mixed");
        message.setContent(msgPart);

        MimeBodyPart body = new MimeBodyPart();     //正文
        MimeBodyPart attach = new MimeBodyPart();   //附件

        msgPart.addBodyPart(body);
        msgPart.addBodyPart(attach);

        //设置正文内容
        body.setContent("JavaMail功能测试-body","text/html;charset=utf-8");
        //设置附件内容
        attach.setDataHandler(new DataHandler(new FileDataSource("C:\\test\\export.xls")));
        //attach.setFileName("Export_Test数据.xls");
        //1.解决附件中中文显示的问题，使用工具类进行编码
        attach.setFileName(MimeUtility.encodeText("Export_Test数据.xls"));
        message.saveChanges();

        /**
         * 第三步：发送邮件
         */
        Transport trans = session.getTransport("smtp");
        LOG.info("!!!senderName:"+senderName);
        LOG.info("!!!senderPassword:"+senderPassword);
        LOG.info("!!!smtpServer:"+smtpServer);
        trans.connect(smtpServer,senderName,senderPassword);
        trans.sendMessage(message, message.getAllRecipients());

    }
}
