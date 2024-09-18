package com.openx.mqtt_subsribe.service;


import com.openx.mqtt_subsribe.entity.Sensor;

import java.time.LocalDate;
import java.util.List;

public interface SensorService {

    List<Sensor> getAll();

    void save(Sensor sensor);

    List<Sensor> getDataByDate(LocalDate date);
}
