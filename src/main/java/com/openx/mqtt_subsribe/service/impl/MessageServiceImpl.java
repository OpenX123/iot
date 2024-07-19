package com.openx.mqtt_subsribe.service.impl;

import com.openx.mqtt_subsribe.entity.Message;
import com.openx.mqtt_subsribe.entity.Topic;
import com.openx.mqtt_subsribe.mapper.MessageMapper;
import com.openx.mqtt_subsribe.model.Result;
import com.openx.mqtt_subsribe.mqtt.MQTTSubsribe;
import com.openx.mqtt_subsribe.service.MessageService;
import com.openx.mqtt_subsribe.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private MQTTSubsribe mqttSubsribe;

    @Override
    public void saveMessage(Message message) {
        messageMapper.insert(message);
    }

    @Override
    public String publishMessage(Map<String, Object> payload) {
        String topic = (String) payload.get("topic");
        String msg = (String) payload.get("msg");
        int qos = (int) payload.get("qos");
        Topic existingTopic = topicService.findByTopic(topic,qos);
        if(existingTopic == null) {
            return "主题不存在";
        }
        mqttSubsribe.sendMQTTMessage(topic, msg, qos);
        // 创建并保存消息对象
        Message mqttMessage = new Message();
        mqttMessage.setTopicId(existingTopic.getId());
        mqttMessage.setMsg(msg);
        mqttMessage.setQos(qos);
        if(!msg.contains("time")){//如果消息中不包含time字段，则保存消息
            saveMessage(mqttMessage);
        }
        String data = String.format("发送了一条主题是‘%s’，内容是: %s，消息级别 %d", topic, msg, qos);
        return data;
    }
}
