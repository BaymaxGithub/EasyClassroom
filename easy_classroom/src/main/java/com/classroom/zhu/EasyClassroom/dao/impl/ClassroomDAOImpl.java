package com.classroom.zhu.EasyClassroom.dao.impl;

import com.classroom.zhu.EasyClassroom.dao.ClassroomDAO;
import com.classroom.zhu.EasyClassroom.util.ClassroomQueryGenrator;
import com.classroom.zhu.common.model.Classroom;
import com.classroom.zhu.common.service.MongoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static com.classroom.zhu.common.service.Collections.CLASSROOM;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by 12801 on 2017/12/10.
 */

@Component
public class ClassroomDAOImpl extends MongoService implements ClassroomDAO {
    private String collectionName = CLASSROOM;
    @Autowired
    private ClassroomQueryGenrator crQueryGenrator;

    //1

    @Override
    public void addClasstoom(ObjectId oId, Classroom classroom){
        MongoTemplate template = factory.getMongoTemplateByOid(oId);
        template.save(classroom,collectionName);
    }

    //2
    public void deleteClassroomById(ObjectId oid, ObjectId upsertId){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        template.remove(query(where("_id").is(upsertId)), collectionName);
    }

    //3
    public List<Classroom> getClassroomList (ObjectId oId, Classroom cr, int cursor, int limit){
        MongoTemplate template = factory.getMongoTemplateByOid(oId);
        Query query = new Query();
        crQueryGenrator.getQuery(query, cr);                 //设置查询条件
        query.limit(limit).skip(cursor);
        return template.find(query, Classroom.class, collectionName);
    }

    //4
    public long getClassroomCount(ObjectId oId, Classroom cr){
        Query query = new Query();
        crQueryGenrator.getQuery(query, cr);
        MongoTemplate template = factory.getMongoTemplateByOid(oId);
        return template.count(query, collectionName);
    }
}
