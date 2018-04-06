package com.example.asus.mydlnaapplicationone;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by asus on 2018/3/20.
 */

public class ContentItem {
    private String name;
    private Bitmap thumbnail;
    private  Long id;
    private  String path;
    private ContentType type;

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
    public Long getId() {
        return id;
    }
    public Bitmap getThumbnail() {
        return thumbnail;
    }
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
    public  ContentItem(String contentName,Long id,Bitmap thumbnail,ContentType type,String path)
    {
        this(contentName,type);
        this.id = id;
        this.thumbnail = thumbnail;
        this.type = type;
        this.path = path;


    }

}
