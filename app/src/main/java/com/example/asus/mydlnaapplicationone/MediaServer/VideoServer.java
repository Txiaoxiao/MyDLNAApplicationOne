package com.example.asus.mydlnaapplicationone.MediaServer;

import android.util.Log;

import com.example.asus.mydlnaapplicationone.ContentItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class VideoServer extends NanoHTTPD {
    
    public static final String TAG = VideoServer.class.getSimpleName();
    private static final String REQUEST_ROOT = "/";
    List<ContentItem> imageList;
    List<ContentItem> videoList;
    List<ContentItem> audioList;

    public VideoServer(int port, List<ContentItem> imageList,List<ContentItem> videoList,List<ContentItem> audioList) {
        super(port);
        this.imageList = imageList;
        this.videoList = videoList;
        this.audioList = audioList;
    }
    
    @Override
    public Response serve(IHTTPSession session) {        
        Log.d(TAG,"OnRequest: "+session.getUri());
        if(REQUEST_ROOT.equals(session.getUri())) {
            return responseRootPage(session);
        }
        else  {
            return responseVideoStream(session);
        }
    }

    public Response responseRootPage(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream("storage/emulated/0/test.html");
            String mimeType=getMineType(session.getUri());
            return new NanoHTTPD.Response(Status.OK, "text/html", fis);
        }
        catch (Exception e) {
            e.printStackTrace();
            return response404(session, "storage/emulated/0/test.html");
        }

    }

    private StringBuilder newAudioStringBuilder()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append("<audio controls autoplay>");
        builder.append("type="+getQuotaStr("audio/mpeg")+">");
        builder.append("Your browser doestn't support HTML5");
        builder.append("</audio>");
        builder.append("</body></html>\n");

        return builder;
    }

    
    public Response responseVideoStream(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream(session.getUri());
            String mimeType=getMineType(session.getUri());
            /*if(mContentType.equals(ContentType.Video)){mimeType="video/mp4";}
            else if(mContentType.equals(ContentType.Image)){mimeType="image/jpg";}
            else {mimeType="audio/mp3";}*/
            return new NanoHTTPD.Response(Status.OK, mimeType, fis);
        } 
        catch (FileNotFoundException e) {        
            e.printStackTrace();
            return response404(session,session.getUri());
        } 
    }

    private String getMineType(String uri) {
        String miniType="video/mp4";
        //TODO: set minetype according to .jpg/.png/.mp3/.mp4
        return miniType;
    }

    public Response response404(IHTTPSession session,String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");        
        builder.append("Sorry, Can't Found "+url + " !");        
        builder.append("</body></html>\n");
        return new Response(builder.toString());
    }
    
    
    protected String getQuotaStr(String text) {
        return "\"" + text + "\"";
    }
    
}
