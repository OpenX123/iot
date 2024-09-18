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
    private Integer light2;//光照
    private Integer temp;//温度
    private Integer temp1;//温度
    private Integer temp2;//温度
    private Double volt;//电压
    private Double volt1;//电压
    private Double volt2;//电压
    private Double C1;//电量
    private Double C2;//电量
    private Double C3;//电量
    private Timestamp timestamp;//时间戳
}
