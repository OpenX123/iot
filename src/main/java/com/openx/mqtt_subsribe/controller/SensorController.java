package com.openx.mqtt_subsribe.controller;

import com.openx.mqtt_subsribe.entity.Sensor;
import com.openx.mqtt_subsribe.entity.User;
import com.openx.mqtt_subsribe.entity.dto.LoginRequest;
import com.openx.mqtt_subsribe.entity.dto.RegisterRequest;
import com.openx.mqtt_subsribe.entity.dto.SensorRequest;
import com.openx.mqtt_subsribe.model.Result;
import com.openx.mqtt_subsribe.service.SensorService;
import com.openx.mqtt_subsribe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/sensor")
@Api(tags = "传感器相关接口")
public class SensorController {
    @Autowired
    private SensorService sensorService;

    @GetMapping("/getAll")
    @ApiOperation("获取所有传感器数据")
    public Result< List<Sensor>> getAll() {
        List<Sensor> sensorData = sensorService.getAll();
        if (sensorData != null) {
            return Result.success(sensorData);
        } else {
           return Result.error("getSensorData failed");
        }
    }
    @PostMapping("/save")
    @ApiOperation("添加传感器数据")
    public Result<String> save(@RequestBody SensorRequest sensorRequest) {
        try {
            Sensor sensor = new Sensor();
            BeanUtils.copyProperties(sensorRequest, sensor);
            sensorService.save(sensor);
            return Result.success("Sensor data added successfully");
        } catch (Exception e) {
            return Result.error("Add sensor data failed");
        }
    }
}

