package com.openx.mqtt_subsribe.mqtt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openx.mqtt_subsribe.entity.Message;
import com.openx.mqtt_subsribe.entity.Sensor;
import com.openx.mqtt_subsribe.entity.Topic;
import com.openx.mqtt_subsribe.entity.User;
import com.openx.mqtt_subsribe.mapper.UserMapper;
import com.openx.mqtt_subsribe.service.MessageService;
import com.openx.mqtt_subsribe.service.SensorService;
import com.openx.mqtt_subsribe.service.TopicService;
import com.openx.mqtt_subsribe.service.UserService;
import com.openx.mqtt_subsribe.utils.UniqueUserNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class PushCallback implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushCallback.class);

    @Autowired
    private TopicService topicService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MQTTSubsribe mqttServer;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserService userService;

    @Autowired
    private MQTTSubsribe mqttSubsribe;

    @Autowired
    private UserMapper userMapper;

    public PushCallback() {
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.info("---------------------连接断开，可以做重连");
        mqttServer.subsribeConnect();
//        mqttSubsribe.init("local_info", 0) ;
//        log.info("----------local_info订阅成功");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String result = new String(message.getPayload(), "UTF-8");
        int qos = message.getQos();
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + qos);
        System.out.println("接收消息内容 : " + result);

        Topic existingTopic = topicService.findByTopic(topic,qos);
        if(existingTopic == null) {
            Topic topic1 = new Topic();
            topic1.setTopic(topic);
            topic1.setQos(qos);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            topic1.setCreatedAt(timestamp);
            topic1.setUpdatedAt(timestamp);
            topicService.saveTopic(topic1);
        }
        /**
         * 接收用户信息// "msg": "{\"UserName\": \"\", \"Password\": \"\", \"UserID\": 1231231111, \"money\": 0}",
         */
        if(result.contains("UserID")&&result.contains("UserName")&&result.contains("Password")&&result.contains("money")){
            Map<String, Object> map=null;
            String UserName="",Password="",UserID="",money="";

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                map = objectMapper.readValue(result, Map.class);
                UserID = (String) map.get("UserID");
                money = (String) map.get("money");
            } catch (Exception e) {
                e.printStackTrace();
            }
             UserName= UniqueUserNameGenerator.generateUserName();
             Password = "123456";
             User user = new User();
             user.setUserId(UserID);
             user.setUserName(UserName);
             user.setPassword(Password);
             user.setMoney(Double.valueOf(money));
             log.info("userId: {}, userName: {}, password: {}, money: {}", UserID, UserName, Password, money);
             if(userService.findByUserId(String.valueOf(UserID))==null){
                 userService.register(user);
             }
        }
        /**
         * 接收温度信息
         */
         if(result.contains("light")&&result.contains("temp1")&&result.contains("light2")&&result.contains("volt")){
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> map = objectMapper.readValue(result, Map.class);
          String light = (String) map.get("light");
          String temp1 = (String) map.get("temp1");
          String light2 = (String) map.get("light2");
          String volt = (String) map.get("volt");
          Sensor sensor = new Sensor();
          sensor.setLight(Integer.valueOf(light));
          sensor.setTemp1(Integer.valueOf(temp1));
          sensor.setLight2(Integer.valueOf(light2));
          sensor.setVolt(Double.valueOf(volt));
          log.info("light: {}, temp1: {}, light2: {}, volt: {}", light, temp1, light2, volt);
          sensorService.save(sensor);
        }
        /**
         * 接收用户充值信息
         */
        if(result.contains("Money_add")&&result.contains("UserID")){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(result, Map.class);
            String UserID = (String) map.get("UserID");
            String Money_add = (String) map.get("Money_add");
            userMapper.addMoney(UserID, Double.valueOf(Money_add));
        }
        /**
         * 接收用户扣款信息
         */
        if(result.contains("UserID")&&result.contains("moneyFlag")){
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.readValue(result, Map.class);
                String userID = (String) map.get("UserID");
                String moneyFlag = (String) map.get("moneyFlag");
                if (moneyFlag.equals("1")) {
                    userService.startCharging(userID);
                } else if (moneyFlag.equals("2")) {
                    userService.stopCharging(userID);
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("topic", "/post");
                    String formattedMoney = String.format("%.2f", userService.findByUserId(userID).getMoney());
                    payload.put("msg", "{\"Dmoney\":"+formattedMoney+"}");
                    //保留两位小数

                    payload.put("qos", 2);
                    messageService.publishMessage(payload);
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Message mqttMessage = new Message();
        mqttMessage.setTopicId(existingTopic.getId());
        mqttMessage.setMsg(result);
        mqttMessage.setQos(qos);
        messageService.saveMessage(mqttMessage);
    }
}
