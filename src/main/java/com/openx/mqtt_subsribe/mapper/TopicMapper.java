package com.openx.mqtt_subsribe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openx.mqtt_subsribe.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}
