package com.example.asus.mydlnaapplicationone;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by asus on 2018/3/20.
 */

public class ContentItem {
    private String name;
    private Bitmap thumbnail;
    private ContentType type;
    private  String path;

    public String getName(){
        return name;
    }
    public String getPath(){return  path;}
    public  ContentType getType(){return type;}
    public ContentItem(String contentName,ContentType type)
    {
        name = contentName;
        this.type = type;
    }
    public  ContentItem(String contentName,Bitmap thumbnail,ContentType type,String path)
    {
        this(contentName,type);
        this.thumbnail = thumbnail;
        this.type = type;
        this.path = path;


    }

}
