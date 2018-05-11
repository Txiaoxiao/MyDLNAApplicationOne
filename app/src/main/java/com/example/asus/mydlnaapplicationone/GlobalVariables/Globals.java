package com.example.asus.mydlnaapplicationone.GlobalVariables;

import com.example.asus.mydlnaapplicationone.Device;
import com.example.asus.mydlnaapplicationone.MediaServer.MediaServer;

/**
 * Created by asus on 2018/3/29.
 */

public class Globals {

    private  static Globals instance  = new Globals();

    public static Globals getInstance()
    {
        return instance;
    }

    public static void setInstance(Globals instance)
    {
        Globals.instance = instance;
    }

    private Device selectedDevice;

    private MediaServer mediaServer = null;

    public MediaServer getMediaServer() {
        return mediaServer;
    }

    public void setMediaServer(MediaServer mediaServer) {
        this.mediaServer = mediaServer;
    }

    private Globals()
    {}

    public Device getSelectedDevice()
    {
        return  selectedDevice;
    }

    public  void setSelectedDevice(Device selectedDevice)
    {
        this.selectedDevice= selectedDevice;
    }




}
