# IoT物联网平台

一个基于MQTT协议的物联网平台，用于连接、管理和监控智能设备，实现数据采集、分析和可视化。

2024年第八届湖南省大学生物联网应用创新设计竞赛（技能赛）一等奖作品。

## 项目结构

本项目由三个主要部分组成：

- **mqtt_subsribe**：基于Spring Boot开发的后端服务，负责MQTT消息订阅、数据处理和API服务
- **iot_uniapp**：基于uni-app开发的前端应用，支持多端部署（H5、小程序、App）
- **sql**：数据库脚本，包含表结构和初始数据

## 技术栈

### 后端技术栈
- Spring Boot 2.5.6
- MyBatis-Plus 3.4.2
- Spring Integration MQTT
- MySQL 8.0
- Redis
- Swagger 3.0
- JWT认证

### 前端技术栈
- uni-app
- uCharts (图表组件)
- ECharts (数据可视化)

## 功能特性

- MQTT消息订阅与处理
- 设备连接与管理
- 传感器数据采集与存储
- 实时数据监控与告警
- 用户认证与授权管理
- 数据可视化和分析

## 环境要求

- Java 17
- Node.js 14+
- MySQL 8.0+
- Redis 6.0+
- MQTT Broker (如Mosquitto、EMQX等)

## 部署指南

### 数据库配置

1. 创建MySQL数据库并执行SQL脚本：

```bash
# 连接到MySQL
mysql -u your_username -p

# 创建数据库
source sql/create_database.sql

# 执行表结构和初始数据脚本
use iot;
source sql/mqtt_user.sql
source sql/mqtt_topic.sql
source sql/mqtt_sensor.sql
source sql/mqtt_msg.sql
source sql/charge_record.sql
```

### 后端服务部署

1. 配置后端服务：

在 `mqtt_subsribe/src/main/resources/application.yml` 中修改数据库、Redis和MQTT broker连接信息。

2. 构建并启动后端服务：

```bash
cd mqtt_subsribe
mvn clean package
java -jar target/mqtt_subsribe-0.0.1-SNAPSHOT.jar
```

或者使用Maven直接运行：

```bash
cd mqtt_subsribe
mvn spring-boot:run
```

### 前端应用部署

1. 安装依赖：

```bash
cd iot_uniapp
npm install
```

2. 开发模式运行：

```bash
# HBuilderX中运行
# 或通过命令行
npm run dev:h5      # H5版本
npm run dev:mp-weixin  # 微信小程序版本
npm run dev:app     # App版本
```

3. 构建生产环境版本：

```bash
npm run build:h5    # 构建H5版本
npm run build:mp-weixin  # 构建微信小程序版本
npm run build:app   # 构建App版本
```

## MQTT配置说明

本项目使用MQTT协议进行设备通信，需要配置MQTT broker：

1. 安装MQTT broker（如Mosquitto或EMQX）
2. 配置后端服务连接到MQTT broker(需自行修改yml文件的mqtt连接配置)
3. 设备需按照规定的Topic格式发布消息

## API文档

API文档采用Swagger自动生成，部署后可通过以下地址访问：

```
http://your-server-ip:8080/swagger-ui/index.html
```

## 常见问题

- 连接MQTT broker失败：检查broker地址、端口和认证信息
- 数据库连接异常：检查数据库配置和网络连通性
- 前端应用无法连接后端：检查API地址配置和跨域设置
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182133811.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182121563.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182127606.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182126995.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182128811.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182128947.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182128917.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182129604.png)
![image.png](https://obsidian-oss-openx.oss-cn-guangzhou.aliyuncs.com/img/202409182124502.png)


