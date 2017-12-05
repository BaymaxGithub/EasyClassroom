package com.classroom.zhu.EasyClassroom.javaMail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by 12801 on 2017/11/21.
 */

@RequestMapping("api")
@Controller
public class MailTestController {
    private static final Logger LOG= LoggerFactory.getLogger(MailTestController.class);
    @Autowired
    SendMailUtil sendMailUtil;

    /**
     * 测试能否发送邮件
     */
    @RequestMapping(value = "/mail/test",method = RequestMethod.GET)
    public void sendMail()throws Exception{
        LOG.info("send email");
        sendMailUtil.send("18108191791@163.com","1280157271@qq.com");
    }


}
