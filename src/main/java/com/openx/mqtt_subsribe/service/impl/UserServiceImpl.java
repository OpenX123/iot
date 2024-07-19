package com.openx.mqtt_subsribe.service.impl;

import com.openx.mqtt_subsribe.entity.ChargeRecord;
import com.openx.mqtt_subsribe.entity.User;
import com.openx.mqtt_subsribe.mapper.ChargeRecordMapper;
import com.openx.mqtt_subsribe.mapper.UserMapper;
import com.openx.mqtt_subsribe.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;

    //    //在高并发环境下，使用 ConcurrentHashMap 会更有效率
    //    private Map<String, Integer> userBalances = new ConcurrentHashMap<>();
    //    private Map<String, Boolean> userChargingStatus = new ConcurrentHashMap<>();
    //可以使用 ScheduledExecutorService 来替代手动创建和管理线程，这样可以更好地控制线程的生命周期和资源管理。
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);// 定时任务执行器，线程池大小为10
    // 存储每个用户的定时任务
    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Override
    public User login(String account, String password) {
        log.info("User login: account={}, password={}", account, password);
        return userMapper.login(account, password);
    }

    @Override
    public void register(User user) {
        user.setTimestamp(new Timestamp(System.currentTimeMillis()));
        userMapper.register(user);
    }

    @Override
    public User findByUserId(String UserId) {
        User user = userMapper.findByUserId(UserId);
        if (user!=null)
            return user;
        else
            return null;
    }

    @Override
    public boolean updateInfo(User user) {
        int i = userMapper.updateInfo(user);
        if(i>0)
            return true;
        else
            return false;
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    /**
     * 开始扣费
     * @param userID 用户ID
     */
    public void startCharging(String userID) {
        User user = userMapper.selectById(userID);
        if (user != null) {
            // 创建一个定时任务，每秒钟执行一次
            ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
                User updatedUser = userMapper.selectById(userID);
                if (updatedUser != null && updatedUser.getTimestamp() != null) {
                    reduceMoney(userID);
                }
            }, 0, 1, TimeUnit.SECONDS);
            // 将定时任务存储到Map中
            scheduledTasks.put(userID, scheduledFuture);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * 停止扣费
     * @param userID 用户ID
     */
    public void stopCharging(String userID) {
        User user = userMapper.selectById(userID);
        if (user != null) {
            // 设置用户的timestamp为null表示停止扣费
            user.setTimestamp(null);
            userMapper.updateById(user);
            ScheduledFuture<?> scheduledFuture = scheduledTasks.get(userID);
            // 获取并取消对应的定时任务
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledTasks.remove(userID);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public List<ChargeRecord> getAllRecord() {
        List<ChargeRecord> records = userMapper.getAllRecord();
        return records;
    }

    /**
     * 扣款逻辑
     * @param userID 用户ID
     */
    private void reduceMoney(String userID) {
        User user = userMapper.selectById(userID);
        if (user != null) {
            double deductionAmount = 0.1; // 假设每次扣1单位的金额
            if (user.getMoney() >= deductionAmount) {
                user.setMoney(user.getMoney() - deductionAmount);
                userMapper.updateById(user);
                recordCharge(userID, deductionAmount);
                System.out.println("User " + userID + " has been charged. New balance: " + user.getMoney());
            } else {
                System.out.println("User " + userID + " has insufficient balance.");
                stopCharging(userID);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
    /**
     * 记录每次扣费
     * @param userID 用户ID
     * @param amount 扣款金额
     */
    private void recordCharge(String userID, double amount) {
        ChargeRecord record = new ChargeRecord();
        record.setUserId(userID);
        record.setAmount(amount);
        record.setTimestamp(new Timestamp(System.currentTimeMillis()));
        chargeRecordMapper.insert(record);
    }
}