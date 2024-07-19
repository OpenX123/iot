package com.openx.mqtt_subsribe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("charge_record")
public class ChargeRecord {
    @TableId
    private Long id;
    private String userId;
    private Double amount;
    private Timestamp timestamp;
}
