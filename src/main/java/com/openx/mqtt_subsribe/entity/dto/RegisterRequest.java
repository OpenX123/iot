package com.openx.mqtt_subsribe.entity.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class RegisterRequest {
    private String userId;
    private String userName;
    private String password;
    private Double money;
}
