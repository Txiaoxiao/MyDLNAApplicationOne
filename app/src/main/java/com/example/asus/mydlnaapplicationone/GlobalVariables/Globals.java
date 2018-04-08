package com.example.asus.mydlnaapplicationone.GlobalVariables;

import com.example.asus.mydlnaapplicationone.Device;
import com.example.asus.mydlnaapplicationone.MediaServer.VideoServer;

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

    private VideoServer mediaServer = null;

    public VideoServer getMediaServer() {
        return mediaServer;
    }

    public void setMediaServer(VideoServer mediaServer) {
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
