package com.openx.mqtt_subsribe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("mqtt_msg")
public class Message {
    @TableId
    private Long id;
    private Long topicId;
    private String msg;
    private Integer qos;
    private Timestamp createdAt;
}
