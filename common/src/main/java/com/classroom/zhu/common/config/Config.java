package com.classroom.zhu.common.config;

import com.classroom.zhu.common.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.MongoClientOptions.builder;

/**
 * Created by 12801 on 2017/8/12.
 */

public class Config {
    private PublicConfigBean publicConfig;
    private Map<String,Object> projectConfig = new HashMap<>();
    private Object projectConfigBean;
    private MongoTemplate mongoTemplate;
    private String httpServerUri;
    private String projectName;
    private static final Logger LOG= LoggerFactory.getLogger(Config.class);


    /*我写来排错的
    public void setPublicConfigBean(PublicConfigBean publicConfig){ this.publicConfig =publicConfig;}
    public PublicConfigBean getPublicConfigBean(){ return publicConfig;}*/

    //构造函数
    public Config(MongoTemplate mongoTemplate, String collectionName)throws IOException{
        this(mongoTemplate,collectionName,null);
    }
    public Config(MongoTemplate mongoTemplate, String collectionName, String projectName){
        System.out.println("看看是不是为空：mongoTemplate："+mongoTemplate);
        System.out.println("看看是不是为空：collectionName："+collectionName);
        System.out.println("看看是不是为空：projectName："+projectName);
        this.mongoTemplate = mongoTemplate;
        //load public config
        Query query = Query.query(Criteria.where("name").is("public"));
        //这里从fanqy_config的V01表中检索出来设置到CommonConfigBean（配置文件这样指定的）
        CommonConfigBean pub = mongoTemplate.findOne(query,CommonConfigBean.class,collectionName);
        System.out.print("这里如果pub为null,后边就没法操作了--pub:"+pub);

        ObjectMapper mapper = new ObjectMapperFactory().getMapper();
        publicConfig = mapper.convertValue(pub.getConfig(),PublicConfigBean.class);//前者转换成后者

        //load project config
        if(projectName != null){  //projectName是从参数传过来的
            this.setProjectName(projectName);
            query = Query.query(Criteria.where("name").is(projectName));
            CommonConfigBean find = mongoTemplate.findOne(query,CommonConfigBean.class, collectionName);
            if (find != null){ //数据库中有记录（连接的数据库是哪个由传进来的mongoTemplate决定）
                LOG.info("!!!project-"+projectName+" 's config:"+find);//日志对象尚未生成，不能使用
                System.out.println("!!!project-"+projectName+" 's config:"+find);
                projectConfig = find.getConfig();
            }
        }
    }


    //mongoTemplate的get/set方法
    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }
    public void setMongoTemplate(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }
    public PublicConfigBean getPublic(){ return publicConfig;}

    @SuppressWarnings("unchecked")
    public <T> T getProject(Class<T> toValueType){
        if(projectConfigBean.getClass() == toValueType){//传进来的类型是projectConfigBean的类型
            return (T)projectConfigBean;
        }else{
            return new ObjectMapper().convertValue(projectConfig,toValueType);
        }
    }

    //
    public void setProjectName(String projectName){ this.projectName =projectName;}
    public String getProjectName(){ return projectName;}

    public Map<String,Object>getProject(){ return projectConfig;}

    public PublicConfigBean getPublicConfig(){ return publicConfig;}
    public void setPublicConfig(PublicConfigBean publicConfig){
        this.publicConfig = publicConfig;
    }

    /**
     * @return example "http://127.0.0.1:8080/"
     */
    public String getHttpServerUti(){
        if(this.httpServerUri == null){
            this.httpServerUri = "http://"+publicConfig.getApiServerIP()
                    +":"+publicConfig.getApiServerPort()+"/" ;
        }
        return this.httpServerUri;
    }

    public MongoClient createMongoClient()throws UnknownHostException{
        String host = getPublic().getMongodbHost();
        int port = getPublic().getMongodbPort();

        String adminDbName = "admin";
        String username = publicConfig.getMongodbUsername();
        String password = publicConfig.getMongodbPassword();

        ArrayList<MongoCredential> credentials = Lists.newArrayList();
        if(!Strings.isNullOrEmpty(username)){//用户名不为空
            MongoCredential credential = MongoCredential.createCredential(username, adminDbName, password.toCharArray());
            credentials.add(credential);
        }
        MongoClientOptions options = builder().connectionsPerHost(10).build();//.MongoClientOpations类。这个类包含了
        // MongoClient建立连接时，与连接相关的所有属性。该类提供了一Builder模式的方式创建并设置这些属性的值。

        return new MongoClient(new ServerAddress(host,port),credentials,options);
    }

}
