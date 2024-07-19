package com.openx.mqtt_subsribe.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.openx.mqtt_subsribe.mapper")
public class MyBatisPlusConfig {
    // Additional configurations if necessary
}