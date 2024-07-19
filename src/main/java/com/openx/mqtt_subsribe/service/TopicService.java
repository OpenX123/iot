package com.openx.mqtt_subsribe.service;

import com.openx.mqtt_subsribe.entity.Topic;

public interface TopicService {
    void saveTopic(Topic topic);
    Topic findByTopic(String topic,int qos);

    Boolean subscribTopic(String topic, int qos);
}
