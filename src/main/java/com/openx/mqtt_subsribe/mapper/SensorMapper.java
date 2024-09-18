package com.openx.mqtt_subsribe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openx.mqtt_subsribe.entity.Sensor;
import com.openx.mqtt_subsribe.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SensorMapper extends BaseMapper<Sensor> {
    void insert(int light, int temp);

    @Select("SELECT * FROM mqtt_sensor WHERE timestamp = #{date}")
    List<Sensor> findByDate(@Param("date") LocalDate date);
}
