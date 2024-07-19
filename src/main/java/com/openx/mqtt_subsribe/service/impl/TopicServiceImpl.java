package com.openx.mqtt_subsribe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.openx.mqtt_subsribe.entity.Topic;
import com.openx.mqtt_subsribe.mapper.TopicMapper;
import com.openx.mqtt_subsribe.mqtt.MQTTSubsribe;
import com.openx.mqtt_subsribe.service.MessageService;
import com.openx.mqtt_subsribe.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private MQTTSubsribe mqttSubsribe;
    @Override
    public void saveTopic(Topic topic) {
        topicMapper.insert(topic);
    }

    @Override
    public Topic findByTopic(String topic, int qos) {
        return topicMapper.selectOne(new QueryWrapper<Topic>().eq("topic", topic).eq("qos", qos));
    }

    @Override
    public Boolean subscribTopic(String topic, int qos) {
       try {
           mqttSubsribe.init(topic, qos);
           Topic existingTopic = findByTopic(topic,qos);
           if (existingTopic == null) {
               existingTopic = new Topic();
               existingTopic.setTopic(topic);
               existingTopic.setQos(qos);
               Timestamp timestamp = new Timestamp(System.currentTimeMillis());
               existingTopic.setCreatedAt(timestamp);
               existingTopic.setUpdatedAt(timestamp);
               saveTopic(existingTopic);
           }
       }catch (Exception e){
           return false;
       }
        return true;
    }
}