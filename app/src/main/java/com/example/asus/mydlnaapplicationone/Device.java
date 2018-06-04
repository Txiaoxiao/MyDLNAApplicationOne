package com.example.asus.mydlnaapplicationone;

import java.net.InetAddress;

/**
 * Created by Tang on 2018/3/10.
 */

public class Device{

    private  String name;

    private InetAddress deviceInetAddress;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InetAddress getDeviceInetAddress() {
        return deviceInetAddress;
    }

    public void setDeviceInetAddress(InetAddress deviceInetAddress) {
        this.deviceInetAddress = deviceInetAddress;
    }

    public Device(String deviceName)
    {
        name = deviceName;
    }

    public String getName(){
        return  name;
    }

}
