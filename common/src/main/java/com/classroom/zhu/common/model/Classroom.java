package com.classroom.zhu.common.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by 12801 on 2017/12/10.
 * 假设管理员每星期更新一次空闲的教室
 * 假如10517 周二周三都是空的 那么就添加两条信息就好
 */
@Data
public class Classroom {
    @Id
    private ObjectId id;
    private String building_name;   //教学楼名称 如十教
    private String number;          //如十教 10517
    private Integer free_day;           //目前只简单情况-假设教室的空闲时间都是按天计算的

    private String state;  //教室状态 Appointment /Available
}
