package com.classroom.zhu.common.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

/**
 * Created by 12801 on 2017/8/11.
 */
public class ObjectMapperFactory {
    private ObjectMapper mapper;

    public ObjectMapperFactory(){
        this.mapper = new ObjectMapper();
        this.mapper.writerWithDefaultPrettyPrinter();

        SimpleModule module = new SimpleModule("ObjectIdModule");
        module.addSerializer(ObjectId.class,new ToStringSerializer());
        this.mapper.registerModule(module);
        this.mapper.findAndRegisterModules();

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.findAndRegisterModules();
    }

    public ObjectMapper getMapper(){    return mapper;}
    public void setMapper(ObjectMapper mapper){ this.mapper = mapper;}
}
