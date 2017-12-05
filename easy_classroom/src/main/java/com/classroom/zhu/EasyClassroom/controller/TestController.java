package com.classroom.zhu.EasyClassroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by 12801 on 2017/8/10.
 */

@RestController
@RequestMapping("api")
public class TestController {

    //日志
    private static final Logger LOG= LoggerFactory.getLogger(TestController.class);

    /**
     * @param demo
     * 项目第一个接口，测试能不能跑起来
     */
    @RequestMapping(value = "/demo",method = RequestMethod.GET)
    public Object get(
            @RequestParam(required = true,name = "demo") String demo
    ){
        System.out.println("请求进来了:demo:"+demo);
        return demo;
       /* LOG.info("打一个日志瞧瞧,这是传过来的请求参数：{}",demo);
        BasicResultDTO resultDTo = new BasicResultDTO();
        Map resultMap = new HashMap();
        resultMap.put("number",1);
        resultMap.put("status","successful");
        resultMap.put("send content",demo);

        resultDTo.setTotal(0);
        resultDTo.setLimit(0);
        resultDTo.setCursor(0);
        resultDTo.setResult(resultMap);
        return resultDTo;*/
    }
}
