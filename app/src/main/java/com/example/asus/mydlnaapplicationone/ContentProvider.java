package com.example.asus.mydlnaapplicationone;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asus on 2018/5/13.
 */

public class ContentProvider implements Runnable {

    private Activity activity;


    public ContentProvider(Activity activity)
    {
        this.activity=activity;
    }


    @Override
    public void run() {
        LoadingImageList();
        LoadingVideoList();
        LoadingAudioList();


    }

    private void LoadingAudioList() {
        String[] audioProjection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER};
        String audioOrderBy = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Globals.getInstance().setAudioList(searchContents(audioUri, audioProjection, audioOrderBy, ContentType.Audio));
    }

    private void LoadingVideoList() {
        String[] videoProjection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DURATION};
        String videoOrderBy = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Globals.getInstance().setVideoList(searchContents(videoUri, videoProjection, videoOrderBy, ContentType.Video));

    }

    private void LoadingImageList() {
        String[] imageProjection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT};
        String imageOrderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Globals.getInstance().setImageList(searchContents(uri, imageProjection, imageOrderBy, ContentType.Image));
    }


    public List<ContentItem> searchContents(Uri uri, String[] projection, String orderBy, ContentType contentType) {

        List<ContentItem> listContentItem = new ArrayList<>();

        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy);

        ContentItem contentItem;
        if (null == cursor) {
            return null;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();  // imageProjection = {MediaStore.Images.Media._ID,
            // MediaStore.Images.Media.DISPLAY_NAME,
            // MediaStore.Images.Media.DATA,
            // MediaStore.Images.Media.DEFAULT_SORT_ORDER
            // MediaStore.Images.Media.WIDTH,
            // MediaStore.Images.Media.HEIGHT};
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
                Log.i("content_item", projection[i] + ":::::::" + cursor.getString(i) + "\n");
            }

            String displayName = map.get(projection[1]);
            contentItem = new ContentItem(displayName.substring(displayName.lastIndexOf("/") + 1, displayName.length()), Long.parseLong(map.get(projection[0])), null, contentType, map.get(projection[2]));

            if (contentType.equals(ContentType.Audio)) {
                listContentItem.add(contentItem);

            } else {
                Log.i("display_name", displayName);
                Log.i("display_name_substring", displayName.substring(displayName.lastIndexOf("/") + 1, displayName.length()));
                String width = map.get(projection[4]);
                String height = map.get(projection[5]);
                if (width != null && height != null) {
                    contentItem.setWidth(Integer.parseInt(width));
                    contentItem.setHeight(Integer.parseInt(height));
                }
                if(contentType.equals(ContentType.Video))
                {
                    contentItem.setDuration(map.get(projection[6]));
                }
                listContentItem.add(contentItem);
            }
        }
        cursor.close();
        return listContentItem;
    }
}
