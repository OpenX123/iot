package com.openx.mqtt_subsribe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("mqtt_topic")
public class Topic {
    @TableId
    private Long id;
    private String topic;
    private Integer qos;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
