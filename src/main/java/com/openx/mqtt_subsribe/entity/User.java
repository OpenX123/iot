package com.openx.mqtt_subsribe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;

@Data
@TableName("mqtt_user")
public class User {
    @TableId
    private String userId;
    private String userName;
    private String password;
    private String cId;
    private Integer tempWaring;
    private Integer CWaring;
    private Timestamp timestamp;//时间戳
}