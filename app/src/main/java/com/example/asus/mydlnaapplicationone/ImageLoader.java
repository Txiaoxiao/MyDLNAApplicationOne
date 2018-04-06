package com.example.asus.mydlnaapplicationone;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ImageView;

/**
 * Created by asus on 2018/3/23.
 */

public class ImageLoader {

    private ImageView mImageView;
    private Long mImageId;

    private Handler handler = new Handler(){//提交数据到UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView.getTag().equals(mImageId))
            mImageView.setImageBitmap((Bitmap) msg.obj);
        }
    };


    public void getThumbnailByThread(final ContentResolver contentResolver, final ImageView imageView, final Long imageId,final ContentItem contentItem){

        mImageView=imageView;
        mImageId = imageId;

        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver,imageId, MediaStore.Images.Thumbnails.MINI_KIND,null);
                contentItem.setThumbnail(thumbnail);
               Message message = Message.obtain();
                message.obj = thumbnail;
                handler.sendMessage(message);
            }
        }.start();
    }

    public void loadImage()//
    {

    }

}
