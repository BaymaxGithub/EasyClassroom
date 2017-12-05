package com.classroom.zhu.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Map;
import java.util.Set;

/**
 * Created by 12801 on 2017/8/11.
 */
public abstract class MongoService {
    @Autowired
    @Qualifier("mongo")
    protected Mongo mongo;

    @Autowired
    protected ObjectMapper mapper;

   @Autowired
    protected MongoFactory factory;

    private String defaultSortField = "createTime";//默认的排序字段
    private Direction defaultSortDirection =  Direction.DESC;

    public DBObject getSortObject(){//直接把普通的java对象保存到mongodb,就需要先转换成DBObject对象
        return new BasicDBObject(this.defaultSortField,this.defaultSortDirection);
    }
    public DBObject getSortObject(String field, Direction direction){
        return new BasicDBObject(field,direction);
    }
    //mongo的get/set方法
    public Mongo getMongo(){
        return mongo;
    }
    public void setMongo(Mongo mongo){
        this.mongo = mongo;
    }

    //defaultSortField的get/set方法
    public String getDefaultSortField(){
        return defaultSortField;
    }
    public void setDefaultSortField(String defaultSortField){
        this.defaultSortField = defaultSortField;
    }

    //direction的get/set方法
    public Direction getDefaultSortDirection(){
        return defaultSortDirection;
    }
    public void setDefaultSortDirection(Direction defaultSortDirection){
        this.defaultSortDirection = defaultSortDirection;
    }

    ////////
    @SuppressWarnings("unchecked")  //告诉编译器忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的警告信息。
    public DBObject getVerbose(String verboseJson, int verbose){
        DBObject obj = new BasicDBObject();
        if(verbose <1 ){
            verbose = 1;
        }
        Map<String,Integer>maps = null;
        try{
            maps = this.mapper.readValue(verboseJson,Map.class);
            Set<String> key = maps.keySet();//所有的key组成的集合
            for(String temp:key){
                if(maps.get(temp)<=verbose){
                    obj.put(temp,maps.get(temp));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    public boolean exist(ObjectId oid, String key, Object value, String collection){
        Query query = Query.query(Criteria.where(key).is(value));
        return exist(oid,query,collection);
    }
    private boolean exist(ObjectId oid, Query query, String collection){
        return this.factory.getMongoTemplateByOid(oid).count(query,collection)>0;
    }
    public boolean exist(String dbName, Query query, String collection){
        return this.factory.getMongoTemplateByDbName(dbName).count(query,collection)>0;
    }


}
