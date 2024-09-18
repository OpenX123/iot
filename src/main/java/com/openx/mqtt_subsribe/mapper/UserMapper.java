package com.openx.mqtt_subsribe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openx.mqtt_subsribe.entity.ChargeRecord;
import com.openx.mqtt_subsribe.entity.Topic;
import com.openx.mqtt_subsribe.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM mqtt_user WHERE (user_name = #{account} OR user_id = #{account}) AND password = #{password}")
    User login(@Param("account") String account, @Param("password") String password);

    @Select("INSERT INTO mqtt_user (user_id, user_name, password, C) VALUES (#{userId}, #{userName}, #{password}, #{C})")
    void register(User user);

    @Select("select * from mqtt_user where user_id=#{userId}")
    User findByUserId(String UserId);

//    @Insert("update mqtt_user set  where user_id=#{userId}")
//    void addC(String userId,Double C);

    @Update("update mqtt_user set user_name=#{userName}, password=#{password} where user_id=#{userId}")
    int updateInfo(User user);

//    @Update("update mqtt_user set C = C - #{reduceC} where user_id=#{userID}")
//    void reduceC(String userID, Integer reduceC);

    @Select("select * from charge_record order by timestamp desc")
    List<ChargeRecord> getAllRecord();

    @Select("select * from charge_record where user_id=#{userId}")
    List<ChargeRecord> getRecordByUserId(String userId);
}
