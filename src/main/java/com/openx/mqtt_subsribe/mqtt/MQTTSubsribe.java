package com.openx.mqtt_subsribe.mqtt;

import com.openx.mqtt_subsribe.config.MQTTConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQTTSubsribe {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTSubsribe.class);

    @Autowired
    private MQTTConfig mqttConfig;

    @Autowired
    private MqttConnect mqttConnect;

    @Autowired
    private PushCallback pushCallback;

    @Autowired
    private MQTTSubsribe mqttSubsribe;

    @Autowired
    private MQTTSubsribe mqttServer;

    private MqttClient subsribeClient;
    private MqttClient publishClient;
    public MqttTopic topic;
    public MqttMessage message;

    public MQTTSubsribe() {
        LOGGER.info("8088上线了");
    }

    /**
     * 发布者客户端和服务端建立连接
     * @return
     */
    public MqttClient publishConnect() {
        try {
            if (publishClient == null) {
                publishClient = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientid(), new MemoryPersistence());
            }
            MqttConnectOptions options = mqttConnect.getOptions();
            if (!publishClient.isConnected()) {
                publishClient.connect(options);
            } else {
                publishClient.disconnect();
                publishClient.connect(mqttConnect.getOptions(options));
            }
            LOGGER.info("-----回调-----客户端连接成功");
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
        return publishClient;
    }

    /**
     * 订阅者客户端和服务端建立连接
     */
    public void subsribeConnect() {
        try {
            if (subsribeClient == null) {
                subsribeClient = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientid(), new MemoryPersistence());
                subsribeClient.setCallback(pushCallback);
            }
            MqttConnectOptions options = mqttConnect.getOptions();
            if (!subsribeClient.isConnected()) {
                subsribeClient.connect(options);
                mqttServer.subsribeConnect();
                subscribe("local_info", 0);
                log.info("----------local_info订阅成功");
            } else {
                subsribeClient.disconnect();
                subsribeClient.connect(mqttConnect.getOptions(options));
            }
//            LOGGER.info("----------客户端连接成功");
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    public void init(String topic, int qos) {
        subsribeConnect();
        subscribe(topic, qos);
    }

    public void unionInit(String topic) {
        subsribeConnect();
        unsuSubscribe(topic);
    }

    public void subscribe(String topic, int qos) {
        try {
            subsribeClient.subscribe(topic, qos);
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    public void unsuSubscribe(String topic) {
        try {
            subsribeClient.unsubscribe(topic);
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    /**
     * 向特定主题发布消息
     * @param topic
     * @param message
     * @return
     */
    public boolean publish(MqttTopic topic, MqttMessage message) {
        MqttDeliveryToken token = null;
        try {
            token = topic.publish(message);
            token.waitForCompletion();
            boolean flag = token.isComplete();
            StringBuffer sbf = new StringBuffer(200);
            sbf.append("给主题为'" + topic.getName());
            sbf.append("'发布消息：");
            if (flag) {
                sbf.append("成功！消息内容是：" + new String(message.getPayload()));
            } else {
                sbf.append("失败！");
            }
            LOGGER.info(sbf.toString());
        } catch (MqttException e) {
            LOGGER.info(e.toString());
        }
        if (token != null) {
            return token.isComplete();
        }
        return false;
    }

    /**
     * 发送消息
     * @param topic
     * @param data
     * @param qos
     */
    public void sendMQTTMessage(String topic, String data, int qos) {
        try {
            this.publishClient = this.publishConnect();
            this.topic = this.publishClient.getTopic(topic);
            this.message = new MqttMessage();
            message.setQos(qos);
            message.setRetained(false);
            message.setPayload(data.getBytes());
            publish(this.topic, message);
        } catch (Exception e) {
            LOGGER.info(e.toString());
            e.printStackTrace();
        }
    }
}
