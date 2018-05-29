package com.dominator.AAAconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
@Slf4j
public class RedisConfig {

    public static String host;

    public static int port;

    public static int database;

    public static int database_msg;

    public static String password;

    public static int timeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        RedisConfig.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase_msg() {
        return database_msg;
    }

    public void setDatabase_msg(int database_msg) {
        RedisConfig.database_msg = database_msg;
    }
}
