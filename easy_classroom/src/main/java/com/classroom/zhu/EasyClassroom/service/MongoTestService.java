package com.classroom.zhu.EasyClassroom.service;

import cn.com.fanqy.myblog.dao.MongoTestDao;
import cn.com.fanqy.myblog.dto.MongoTestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 12801 on 2017/8/13.
 */

@Service
public class MongoTestService {
    @Autowired
    MongoTestDao mongoTestDao;

    public void testAddToMongoDb(MongoTestBean testBean){
        mongoTestDao.testMongo_addDoc(testBean);
    }

    public MongoTestBean getTestBeanByName(String name){
        return mongoTestDao.getTestBeanByName(name);
    }

}
