package com.example.asus.mydlnaapplicationone;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by asus on 2018/3/20.
 */

public class Folder {
    private String name;
/*    private Bitmap folderImage;
    private Bitmap arrow;

    public Bitmap getFolderImage() {
        return folderImage;
    }

    public Bitmap getArrow() {
        return arrow;
    }*/

    public String getName(){
        return name;
    }

    public Folder(String folderName)
    {
        name = folderName;

    }

}
