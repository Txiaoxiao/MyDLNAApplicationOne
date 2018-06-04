package com.example.asus.mydlnaapplicationone.GlobalVariables;

import com.example.asus.mydlnaapplicationone.ContentItem;
import com.example.asus.mydlnaapplicationone.Device;
import com.example.asus.mydlnaapplicationone.MediaServer.MediaServer;

import java.util.List;

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


    private Device selectedDevice=null;

    private MediaServer mediaServer = null;

    private String currentvideoDuration="0";

    private ContentItem currentContentItem=null;

    private List<ContentItem> imageList;
    private List<ContentItem> videoList;
    private List<ContentItem> audioList;

    public List<ContentItem> getImageList() {
        return imageList;
    }

    public void setImageList(List<ContentItem> imageList) {
        this.imageList = imageList;
    }

    public List<ContentItem> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<ContentItem> videoList) {
        this.videoList = videoList;
    }

    public List<ContentItem> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<ContentItem> audioList) {
        this.audioList = audioList;
    }


    public ContentItem getCurrentContentItem() {
        return currentContentItem;
    }

    public void setCurrentContentItem(ContentItem currentContentItem) {
        this.currentContentItem = currentContentItem;
    }

    public String getCurrentvideoDuration() {
        return currentvideoDuration;
    }

    public void setCurrentvideoDuration(String currentvideoDuration) {
        this.currentvideoDuration = currentvideoDuration;
    }


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
