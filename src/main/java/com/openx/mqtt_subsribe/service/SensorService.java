package com.openx.mqtt_subsribe.service;


import com.openx.mqtt_subsribe.entity.Sensor;

import java.util.List;

public interface SensorService {

    List<Sensor> getAll();

    void save(Sensor sensor);
}
