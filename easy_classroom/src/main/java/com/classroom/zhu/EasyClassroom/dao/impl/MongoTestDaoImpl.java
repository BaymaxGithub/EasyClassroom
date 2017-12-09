package com.classroom.zhu.EasyClassroom.dao.impl;

import com.classroom.zhu.EasyClassroom.dao.MongoTestDao;
import com.classroom.zhu.EasyClassroom.dto.MongoTestBean;
import com.classroom.zhu.common.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static com.classroom.zhu.common.service.Collections.MONGO_TEST;

/**
 * Created by 12801 on 2017/8/13.
 */
@Component
public class MongoTestDaoImpl extends MongoService implements MongoTestDao {
    private static final Logger LOG = LoggerFactory.getLogger(MongoTestDaoImpl.class);
    private String collectionName = MONGO_TEST;

    public void testMongo_addDoc(MongoTestBean testBean){
        MongoTemplate template = factory.getMongoTemplateByDbName("test");
        template.save(testBean,MONGO_TEST);
        LOG.info("测试往test数据库的mongo.test表中插入数据,"+testBean.getName());
        System.out.println("testBean.getDesc"+testBean.getDesc());
    }

    public MongoTestBean getTestBeanByName(String name){
        MongoTemplate template = factory.getMongoTemplateByDbName("test");

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        MongoTestBean mongoTestBean = template.findOne(query, MongoTestBean.class, MONGO_TEST);

        return mongoTestBean;
    }
}
