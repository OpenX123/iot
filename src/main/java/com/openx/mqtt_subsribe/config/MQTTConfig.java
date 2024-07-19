package com.openx.mqtt_subsribe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(MQTTConfig.PREFIX)
public class MQTTConfig {
    //指定配置文件application-local.properties中的属性名前缀
    public static final String PREFIX = "publish.mqtt";
    private String host;
    private String clientid;
    private String username;
    private String password;
    private boolean cleansession;
    private String default_topic;
    private int timeout;
    private int keepalive;
    private int connectionTimeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefault_topic() {
        return default_topic;
    }

    public void setDefault_topic(String default_topic) {
        this.default_topic = default_topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public boolean isCleansession() {
        return cleansession;
    }

    public void setCleansession(boolean cleansession) {
        this.cleansession = cleansession;
    }
}
