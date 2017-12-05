package com.classroom.zhu.EasyClassroom.dao;

import cn.com.fanqy.myblog.dto.MongoTestBean;

/**
 * Created by 12801 on 2017/8/13.
 */
public interface MongoTestDao {
    public void testMongo_addDoc(MongoTestBean testBean);

    public MongoTestBean getTestBeanByName(String name);
}
