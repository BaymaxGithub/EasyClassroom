package com.classroom.zhu.common.service;

import com.classroom.zhu.common.converter.LongToDateConverter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.classroom.zhu.common.service.DbNames.SYSTEM;


/**
 * Created by 12801 on 2017/8/11.
 */
public class MongoFactory implements Serializable {
    private MongoClient mongo;
    private ConcurrentMap<String, MongoTemplate> templates = new ConcurrentHashMap<>();
    private ConcurrentMap<String,GridFsTemplate> gridFsTemplates = new ConcurrentHashMap<>();

    private final ObjectId systemId  = new ObjectId("0000000000000000000ABCDE");

    public MongoFactory(MongoClient mongo){
        this.mongo = mongo;
    }

    //根据oid获取数据库名称
     public String getDatabaseName(ObjectId oid){
        if(systemId.equals(oid)){
            return "ABCDE_db";
        }else{
            return oid.toString().toUpperCase()+"_db";//规定数据库名称的格式
        }
     }

    //根据oid获取数据库名称
    public String getDbNameByOid(ObjectId oid){
        return getDatabaseName(oid);
    }

    //根据db名字返回操作数据库的句柄
    public MongoDatabase getDbByName(String dbName){
        return this.mongo.getDatabase(dbName);
    }

    //根据oid获取操作数据库的句柄
    public MongoDatabase getDbByOid(ObjectId oid){
        return getDbByName(getDbNameByOid(oid));
    }

    //获取指定oid的指定collection
    public MongoCollection<Document> getCollectionByOid(ObjectId oid, String collection){
        return this.getDbByOid(oid).getCollection(collection);
    }

    //根据指定数据库名称获取指定的collection
    public MongoCollection<Document> getCollectionByDbName(String dbName, String collection){
        return this.getDbByName(dbName).getCollection(collection);
    }

    public Mongo getMongo(){
        return this.mongo;
    }

    private MongoMappingContext mongoMappingContext(){
        return new MongoMappingContext();
    }

    private CustomConversions customConversions(){
        List<Converter<?,?>> converterList = new ArrayList<>();
        converterList.add(new LongToDateConverter());
        return new CustomConversions(converterList);
    }

    private MappingMongoConverter mappingMongoConverter(MongoDbFactory factory){
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver,mongoMappingContext());

         converter.setCustomConversions(customConversions());
         converter.setTypeMapper(new DefaultMongoTypeMapper(null));
         converter.afterPropertiesSet();

         return converter;
    }

    //这个函数默认返回的是系统数据看ABCDE_db
    public MongoTemplate getMongoTemplate(){
        return getMongoTemplateByDbName(SYSTEM);
    }

    //根据oid返回
    public MongoTemplate getMongoTemplateByOid(ObjectId oid){
        return getMongoTemplateByDbName(getDatabaseName(oid));
    }

    //根据数据库名称返回MongoTemplate
    public MongoTemplate getMongoTemplateByDbName(String database){
        return getMongoTemplate(database);
    }

    //
    public MongoTemplate getMongoTemplate(String database){
        MongoTemplate template = templates.get(database);
        if(template != null){  //concurrentHashmap里面有该database值
            return template;
        }else{
            SimpleMongoDbFactory factory = new SimpleMongoDbFactory(mongo,database);
            MappingMongoConverter converter = mappingMongoConverter(factory);
            template = new MongoTemplate(factory,converter);
            MongoTemplate temp  = templates.putIfAbsent(database,template);//如果集合中不存在就存进去
            if(temp == null){
                return template;
            }else{
                return temp;
            }
        }
    }

    //根据oid返回MongoTemplate(实实质还是获取到数据库名称再去获取模板方法的 )
    public MongoTemplate getMongoTemplate(ObjectId oid){
        return getMongoTemplate(getDbNameByOid(oid));
    }

    public GridFsTemplate getGridFsTemplateByDbName(String dbName){
        GridFsTemplate template = gridFsTemplates.get(dbName);
        if (template != null){
            return template;
        }else{
            SimpleMongoDbFactory factory = new SimpleMongoDbFactory(mongo,dbName);
            MappingMongoConverter converter = mappingMongoConverter(factory);

            template = new GridFsTemplate(factory,converter);
            GridFsTemplate temp = gridFsTemplates.putIfAbsent(dbName,template);
            if(temp == null){
                return template;
            }else{
                return temp;
            }
        }
    }

    public GridFsTemplate getGridFsTemplateByOid(ObjectId oid){
        return getGridFsTemplateByDbName(getDbNameByOid(oid));
    }

}