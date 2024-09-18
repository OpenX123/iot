package com.openx.mqtt_subsribe.controller;

import com.openx.mqtt_subsribe.entity.ChargeRecord;
import com.openx.mqtt_subsribe.entity.User;
import com.openx.mqtt_subsribe.entity.dto.LoginRequest;
import com.openx.mqtt_subsribe.entity.dto.RegisterRequest;
import com.openx.mqtt_subsribe.mapper.UserMapper;
import com.openx.mqtt_subsribe.model.Result;
import com.openx.mqtt_subsribe.service.MessageService;
import com.openx.mqtt_subsribe.service.UserService;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        User loggedInUser = userService.login(loginRequest.getAccount(), loginRequest.getPassword());
        if (loggedInUser != null) {
            return Result.success(loggedInUser);
        } else {
           return Result.error("Login failed");
        }
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User();
            BeanUtils.copyProperties(registerRequest, user);
            user.setTimestamp(new Timestamp(System.currentTimeMillis()));
            userService.register(user);
            return Result.success("User registered successfully");
        } catch (Exception e) {
            return Result.error("Registration failed");
        }
    }
    @PostMapping("/add/{C}")
    public Result<String> addC(@PathVariable Double C) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("topic", "/post");
        payload.put("msg", "{\"C\":}"+C.intValue());
        payload.put("qos", 2);
        String s = messageService.publishMessage(payload);
        if(!s.equals("主题不存在"))
            return Result.success("Add C :"+C);
        else
            return Result.error("Add C failed");
    }
    @GetMapping("/find/{userId}")
    public Result<User> findByUserId(@PathVariable String userId) {
        User user = userService.findByUserId(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("User not found");
        }
    }
    @PutMapping("/update")
    public Result<String> update(@RequestBody User user) {
        if(userService.updateInfo(user))
            return Result.success("Update user successfully");
        else
            return Result.error("Update user failed");
    }
    @GetMapping("/getAll")
    public Result<List<User>> getAll() {
        List<User> users = userService.getAll();
        if (users != null) {
            return Result.success(users);
        } else {
            return Result.error("User not found");
        }
    }
    @GetMapping("/getAllRecord")
    public Result<List<ChargeRecord>> getAllRecord() {
        List<ChargeRecord> chargeRecords = userService.getAllRecord();
        if (chargeRecords != null) {
            return Result.success(chargeRecords);
        } else {
            return Result.error("User not found");
        }
    }
    @GetMapping("/getRecordByUserId/{userId}")
    public Result<List<ChargeRecord>> getRecordByUserId(@PathVariable String userId) {
        List<ChargeRecord> records = userMapper.getRecordByUserId(userId);
        if (records != null) {
            return Result.success(records);
        } else {
            return Result.error("User not found");
        }
    }
}

