package com.openx.mqtt_subsribe.mqtt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openx.mqtt_subsribe.entity.*;
import com.openx.mqtt_subsribe.mapper.ChargeRecordMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Map;

@Component
@Slf4j
public class PushCallback implements MqttCallback {
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
    private UserMapper userMapper;
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;

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
        // 消息发送完成时的回调方法
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // 消息到达时的回调方法
        String result = new String(message.getPayload(), "UTF-8");
        int qos = message.getQos();
//        if(!(result.contains("light")&&result.contains("temp1")&&result.contains("light2")&&result.contains("volt"))){
            System.out.println("接收消息主题 : " + topic);
            System.out.println("接收消息Qos : " + qos);
            System.out.println("接收消息内容 : " + result);
//        }

        // 处理接收到的主题
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
        // 处理接收到的用户信息
        if(result.contains("UserID")&&result.contains("UserName")&&result.contains("Password")){
            Map<String, Object> map=null;
            String UserName="",Password="",UserID="",C="";

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                map = objectMapper.readValue(result, Map.class);
                UserID = (String) map.get("UserID");
                C = (String) map.get("C");
            } catch (Exception e) {
                e.printStackTrace();
            }
             UserName= UniqueUserNameGenerator.generateUserName();
             Password = "123456";
             User user = new User();
             user.setUserId(UserID);
             user.setUserName(UserName);
             user.setPassword(Password);
             log.info("userId: {}, userName: {}, password: {}, C: {}", UserID, UserName, Password);
             if(userService.findByUserId(String.valueOf(UserID))==null){
                 userService.register(user);
             }
        }
        // 处理接收到的用户信息
         if(result.contains("light")&&result.contains("temp1")&&result.contains("light2")||result.contains("volt")){
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> map = objectMapper.readValue(result, Map.class);
             Sensor sensor = new Sensor();
             if(result.contains("light")&&result.contains("temp1")&&result.contains("temp")&&result.contains("temp2")&&result.contains("light2")){
                String light = (String) map.get("light");
                 String light2 = (String) map.get("light2");
                 String temp = (String) map.get("temp");
                 String temp1 = (String) map.get("temp1");
                 String temp2 = (String) map.get("temp2");
                 sensor.setLight(Integer.valueOf(light));
                 sensor.setLight2(Integer.valueOf(light2));
                 sensor.setTemp(Integer.valueOf(temp));
                 sensor.setTemp1(Integer.valueOf(temp1));
                 sensor.setTemp2(Integer.valueOf(temp2));
             }else{
                 String volt = (String) map.get("volt");
                 String volt1 = (String) map.get("volt1");
                 String volt2 = (String) map.get("volt2");
                 String C1 = (String) map.get("C1");
                 String C2 = (String) map.get("C2");
                 String C3 = (String) map.get("C3");
                 sensor.setVolt(Double.valueOf(volt));
                 sensor.setVolt1(Double.valueOf(volt1));
                 sensor.setVolt2(Double.valueOf(volt2));
                 sensor.setC1(Double.valueOf(C1));
                 sensor.setC2(Double.valueOf(C2));
                 sensor.setC3(Double.valueOf(C3));
             }
          sensorService.save(sensor);
        }
        /**
         * 接收用户充值信息
         */
        if(result.contains("C")&&result.contains("UserID")){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(result, Map.class);
            String UserID = (String) map.get("UserID");
            Double C = (Double) map.get("C");
            try {
//                userMapper.addC(UserID,C);
                ChargeRecord record = new ChargeRecord();
                record.setUserId(UserID);
                record.setAmount(C);
                record.setTimestamp(new Timestamp(System.currentTimeMillis()));
                chargeRecordMapper.insert(record);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//        /**
//         * 接收用户扣款信息
//         */
//        if(result.contains("UserID")&&result.contains("CFlag")){
//            try{
//                ObjectMapper objectMapper = new ObjectMapper();
//                Map<String, Object> map = objectMapper.readValue(result, Map.class);
//                String userID = (String) map.get("UserID");
//                String CFlag = (String) map.get("CFlag");
//                if (CFlag.equals("1")) {
//                    userService.startCharging(userID);
//                } else if (CFlag.equals("2")) {
//                    userService.stopCharging(userID);
//                    Map<String, Object> payload = new HashMap<>();
//                    payload.put("topic", "/post");
//                    String formattedC = String.format("%.2f", userService.findByUserId(userID).getC());
//                    payload.put("msg", "{\"DC\":"+formattedC+"}");
//                    //保留两位小数
//
//                    payload.put("qos", 2);
//                    messageService.publishMessage(payload);
//            }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
        if(result.contains("UserID")&&result.contains("temp_warning")&&result.contains("C_warining")){
            Map<String, Object> map=null;
            String UserName="",Password="",UserID="";
            Integer tempWaring=0,CWaring=0;

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                map = objectMapper.readValue(result, Map.class);
                UserID = (String) map.get("UserID");
                tempWaring = (Integer) map.get("temp_warning");
                CWaring = (Integer) map.get("C_warining");
            } catch (Exception e) {
                e.printStackTrace();
            }
            User user = userService.findByUserId(UserID);
//            user.setUserId("52211255109");
            user.setTempWaring(tempWaring);
            user.setCWaring(CWaring);
            log.info("userId: {}, userName: {}, password: {}, C: {}", UserID, UserName, Password);
            userMapper.updateById(user);
        }
        Message mqttMessage = new Message();
        mqttMessage.setTopicId(existingTopic.getId());
        mqttMessage.setMsg(result);
        mqttMessage.setQos(qos);
        messageService.saveMessage(mqttMessage);
    }
}
