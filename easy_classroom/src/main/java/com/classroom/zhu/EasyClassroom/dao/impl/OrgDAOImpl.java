package com.classroom.zhu.EasyClassroom.dao.impl;

import cn.com.fanqy.common.model.UserDb;
import cn.com.fanqy.common.service.Collections;
import cn.com.fanqy.common.service.MongoService;
import cn.com.fanqy.myblog.dao.OrgDAO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class OrgDAOImpl extends MongoService implements OrgDAO {
    private final ObjectId systemId = new ObjectId("0000000000000000000ABCDE");

    private static final Logger LOG= LoggerFactory.getLogger(OrgDAOImpl.class);
    //1
    @Override
    public ObjectId getOidByEmail(String email) {
        /*MongoTemplate db = factory.getMongoTemplateByDbName(DBNames.SYSTEM);*/
        MongoTemplate db = factory.getMongoTemplateByDbName("ABCDE_db");
        Query query = query(where("username").is(email));//这条数据是手动插入数据库的，如下

        UserDb result = db.findOne(query, UserDb.class, Collections.USER_DBS);
        LOG.info("!!!UserDb result:"+result);
        return result == null ? null : result.getOid();
    }

    //2
    /**
     * 从ABCDE数据库的organizations表中获取所有及公用的oid
     */
    /*@Override
    public List<ObjectId> getOidList(){
        MongoTemplate template = factory.getMongoTemplateByOId(systemId);
        DBCollection dbCollection = template.getCollection("organizations");
        List<ObjectId> idList = dbCollection.distinct("_id");
        idList.remove(systemId);  //去除管理员机构
        return idList;
    }*/
}
