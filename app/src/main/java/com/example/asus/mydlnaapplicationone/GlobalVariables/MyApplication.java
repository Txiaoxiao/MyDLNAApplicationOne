package com.example.asus.mydlnaapplicationone.GlobalVariables;

import android.app.Application;

import com.example.asus.mydlnaapplicationone.Device;

import java.util.List;

/**
 * Created by asus on 2018/3/29.
 */

public class MyApplication extends Application {

    public static Device selectedDevice;
    public static List<Device> deviceList;

    @Override
    public void onCreate() {
        super.onCreate();
        selectedDevice = new Device("haha");

    }
}
