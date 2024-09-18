package com.openx.mqtt_subsribe.service;

import com.openx.mqtt_subsribe.entity.ChargeRecord;
import com.openx.mqtt_subsribe.entity.User;

import java.util.List;

public interface UserService {

    User login(String account, String password);
    void register(User user);
    User findByUserId(String s);

    boolean updateInfo(User user);

    List<User> getAll();

//    void startCharging(String userID);

//    void stopCharging(String userID);

    List<ChargeRecord> getAllRecord();
}