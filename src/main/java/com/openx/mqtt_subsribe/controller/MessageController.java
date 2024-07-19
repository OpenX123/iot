package com.openx.mqtt_subsribe.controller;
import com.openx.mqtt_subsribe.entity.Message;
import com.openx.mqtt_subsribe.entity.Topic;
import com.openx.mqtt_subsribe.model.Result;
import com.openx.mqtt_subsribe.mqtt.MQTTSubsribe;
import com.openx.mqtt_subsribe.service.MessageService;
import com.openx.mqtt_subsribe.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/publish")
@Api(tags = "mqtt发布相关接口")
public class MessageController {

    @Autowired
    private MessageService messageService;
    /**
     * 发布消息
     * @param payload JSON格式的请求体，包括topic, msg和qos
     * @return JSON格式的响应
     */
    @PostMapping(value = "")
    @ApiOperation("发布消息")
    public Result<String> testPublish(@RequestBody Map<String, Object> payload) {
        String result = messageService.publishMessage(payload);
        if(result.equals("主题不存在")){
            return Result.error(result);
        }
        return Result.success(result);
    }
}