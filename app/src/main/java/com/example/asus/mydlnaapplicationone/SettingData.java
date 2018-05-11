package com.example.asus.mydlnaapplicationone;

/**
 * Created by asus on 2018/5/7.
 */

public class SettingData {

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    private String title;

    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public SettingData(String title,String description)
    {
        this.title = title;
        this.description=description;
    }

}
