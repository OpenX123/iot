package com.openx.mqtt_subsribe.controller;
import com.alibaba.fastjson.JSON;
import com.openx.mqtt_subsribe.entity.Message;
import com.openx.mqtt_subsribe.entity.Topic;
import com.openx.mqtt_subsribe.model.Result;
import com.openx.mqtt_subsribe.mqtt.MQTTSubsribe;
import com.openx.mqtt_subsribe.service.MessageService;
import com.openx.mqtt_subsribe.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.codehaus.jettison.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping(value = "/SubsribeController")
@Api(tags = "mqtt订阅相关接口")
public class SubsribeController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private MQTTSubsribe mqttSubsribe;
    @Autowired
    private MessageService messageService;
    /**
     * 订阅主题
     * @param payload JSON格式的请求体，包括topic和qos
     * @return JSON格式的响应
     */
    @PostMapping(value = "/subscribe")
    @ApiOperation(value="订阅主题")
    public Result<String> subscribTopic(@RequestBody Map<String, Object> payload) {
        String topic = (String) payload.get("topic");
        int qos = (int) payload.get("qos");
//        mqttSubsribe.init(topic, qos) ;
        Boolean b = topicService.subscribTopic(topic, qos);
        String data = String.format("订阅'%s'成功", topic);
        return Result.success(data);
    }

    /**
     * 退订主题
     * @param payload JSON格式的请求体，包括topic
     * @return JSON格式的响应
     */
    @PostMapping(value = "/unsubscribe")
    @ApiOperation("退订主题")
    public Result<String> testUnsvSubsribe(@RequestBody Map<String, Object> payload) {
        String topic = (String) payload.get("topic");

        mqttSubsribe.unionInit(topic);

        String data = String.format("取消订阅'%s'成功", topic);
        return Result.success(data);
    }

//    /**
//     * 发布消息
//     * @param payload JSON格式的请求体，包括topic, msg和qos
//     * @return JSON格式的响应
//     */
//    @PostMapping(value = "/publish")
//    @ApiOperation("发布消息")
//    public Result<String> testPublish(@RequestBody Map<String, Object> payload) {
//        String topic = (String) payload.get("topic");
//        String msg = (String) payload.get("msg");
//        int qos = (int) payload.get("qos");
//        Topic existingTopic = topicService.findByTopic(topic,qos);
//        if(existingTopic == null) {
//            return Result.error("主题不存在");
//        }
//        mqttSubsribe.sendMQTTMessage(topic, msg, qos);
//        // 创建并保存消息对象
//        Message mqttMessage = new Message();
//        mqttMessage.setTopicId(existingTopic.getId());
//        mqttMessage.setMsg(msg);
//        mqttMessage.setQos(qos);
//        if(!msg.contains("time")){
//            messageService.saveMessage(mqttMessage);
//        }
//        String data = String.format("发送了一条主题是‘%s’，内容是: %s，消息级别 %d", topic, msg, qos);
//        return Result.success(data);
//    }
    /**
     * 测试
     * @return JSON格式的响应
     */
    @GetMapping(value = "/hello")
    public Result<String> test() {
        return Result.success("hello");
    }
}