package com.openx.mqtt_subsribe.service.impl;

import com.openx.mqtt_subsribe.entity.Sensor;
import com.openx.mqtt_subsribe.mapper.SensorMapper;
import com.openx.mqtt_subsribe.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

@Service
public class SensorServiceImpl implements SensorService {
    @Autowired
    private SensorMapper sensorMapper;

    @Override
    public List<Sensor> getAll() {
        List<Sensor> sensors = sensorMapper.selectList(null);
        return sensors;
    }

    @Override
    public void save(Sensor sensor) {
        sensor.setTimestamp(new Timestamp(System.currentTimeMillis()));
        sensorMapper.insert(sensor);
    }
}
