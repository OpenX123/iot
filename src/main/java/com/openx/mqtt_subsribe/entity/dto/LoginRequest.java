package com.openx.mqtt_subsribe.entity.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;
}
