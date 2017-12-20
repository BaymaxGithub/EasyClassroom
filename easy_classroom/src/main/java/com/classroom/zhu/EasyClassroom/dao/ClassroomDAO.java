package com.classroom.zhu.EasyClassroom.dao;

import com.classroom.zhu.common.model.Classroom;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by 12801 on 2017/12/10.
 */
public interface ClassroomDAO {
    //1、添加教室信息
    public void addClasstoom(ObjectId oId, Classroom classroom);
    //2、删除教室信息
    public void deleteClassroomById(ObjectId oid, ObjectId upsertId);
    //3
    public List<Classroom> getClassroomList (ObjectId oId, Classroom cr, int cursor, int limit);
    //4
    public long getClassroomCount(ObjectId oId, Classroom cr);
}
