package com.openx.mqtt_subsribe.utils;

import java.util.UUID;

public class UniqueUserNameGenerator {

    public static String generateUserName() {
        // 生成UUID
        UUID uuid = UUID.randomUUID();

        // 从UUID中提取一部分作为UserName
        String uniqueUserName = "User_" + uuid.toString().substring(0, 8);

        return uniqueUserName;
    }

    public static void main(String[] args) {
        // 测试生成随机唯一的UserName
        String userName1 = generateUserName();
        String userName2 = generateUserName();

        System.out.println("Generated UserName 1: " + userName1);
        System.out.println("Generated UserName 2: " + userName2);
    }
}
