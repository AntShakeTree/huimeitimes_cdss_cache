package com.hm;

import java.io.Serializable;

/**
 * Created by ant_shake_tree on 15/10/26.
 */
public class Host implements Serializable{
    private  String address;
    private  Integer port;
    public static final int DEFAULT_PORT = 6379;
    public static final int DEFAULT_TIMEOUT = 2000;
    private int timeout;
    private Integer poolSize;
    private String username;
    private String password;

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Host(String address, Integer port, int timeout,int poolSize,String username,String password) {
        this.poolSize=poolSize;
        this.address = address;
        this.port = port;
        this.timeout = timeout;
        this.username=username;
        this.password=password;
    }

    public Host() {
    }

    public int getTimeout() {
        return timeout;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static final String CHARSET = "UTF-8";

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

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }
}
