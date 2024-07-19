package com.openx.mqtt_subsribe.entity.dto;

import lombok.Data;

@Data
public class SensorRequest {
    private Integer light;
    private Integer temp1;
    private Integer light2;
    private Double volt;
}
