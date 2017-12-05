package com.classroom.zhu.EasyClassroom.excel;

import cn.com.fanqy.myblog.dto.MongoTestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.OutputStream;

/**
 * Created by 12801 on 2017/11/12.
 */

@Controller
@RequestMapping("api")
public class PoIController {

    private static final Logger LOG= LoggerFactory.getLogger(PoIController.class);

    @RequestMapping(value = "/excel/crate",method = RequestMethod.GET)
    public
    @ResponseBody
    Object testMongo(@RequestBody MongoTestBean test)   {

        OutputStream out = null;
       /* try{

        }*/
        return  null;
    }
}
