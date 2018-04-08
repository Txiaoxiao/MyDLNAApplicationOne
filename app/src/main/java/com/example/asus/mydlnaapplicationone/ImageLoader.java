package com.example.asus.mydlnaapplicationone;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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
                handleMessage(contentItem,thumbnail);
            }
        }.start();
    }

    public void getVideoThumbnail(final String path,final ImageView imageView,final Long videoId,final ContentItem contentItem)//
    {
        mImageView = imageView;
        mImageId = videoId;

        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
                handleMessage(contentItem,bitmap);

            }
        }.start();

    }

    public void handleMessage(ContentItem contentItem,Bitmap bitmap)
    {
        contentItem.setThumbnail(bitmap);
        Message message = Message.obtain();
        message.obj = bitmap;
        handler.sendMessage(message);
    }

}
