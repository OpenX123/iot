package com.openx.mqtt_subsribe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("mqtt_sensor")
public class Sensor {
    private Integer sensorId;
    private Integer light;//光照
    private Integer temp1;//温度
    private Integer light2;//温度
    private Double volt;//温度
    private Timestamp timestamp;//时间戳
}
