package com.openx.mqtt_subsribe.service;

import com.openx.mqtt_subsribe.entity.Message;

import java.util.Map;

public interface MessageService {
    void saveMessage(Message message);

    String publishMessage(Map<String, Object> payload);
}
