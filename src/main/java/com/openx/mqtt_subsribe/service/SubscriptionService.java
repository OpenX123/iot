package com.openx.mqtt_subsribe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubscriptionService implements ApplicationRunner {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 当Spring Boot应用启动后，会调用此方法。
     * 该方法通过发送POST请求订阅指定的主题。
     */
    @Override
    public void run(ApplicationArguments args) {
            subscribeToTopic();
//            performAnotherTask();
    }
    private void subscribeToTopic() {
        String url = "http://localhost:8088/SubsribeController/subscribe";

        // 设置HTTP头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // POST请求的JSON主体
        String requestJson = "{\"topic\": \"/local_info\", \"qos\": 0}";

        // 创建HttpEntity对象以包含头和JSON主体
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        // 发送POST请求并接收响应
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 检查响应状态
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("成功订阅主题。");
        } else {
            System.out.println("订阅主题失败。");
        }
    }
    private void performAnotherTask() {
    }
}
