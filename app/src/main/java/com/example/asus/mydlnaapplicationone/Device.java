package com.example.asus.mydlnaapplicationone;

import android.graphics.drawable.Drawable;

import java.net.InetAddress;

/**
 * Created by Tang on 2018/3/10.
 */

public class Device{

    private  String name;

    private InetAddress deviceInetAddress;

    private String server;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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
