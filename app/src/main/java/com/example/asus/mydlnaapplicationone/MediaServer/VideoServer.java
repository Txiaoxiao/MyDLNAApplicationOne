package com.example.asus.mydlnaapplicationone.MediaServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.util.Log;

import com.example.asus.mydlnaapplicationone.ContentType;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class VideoServer extends NanoHTTPD {
    
    public static final int DEFAULT_SERVER_PORT = 8080;
    public static final String TAG = VideoServer.class.getSimpleName();
    
    private static final String REQUEST_ROOT = "/";       
    
    private String mVideoFilePath;
    private int mVideoWidth  = 0;
    private int mVideoHeight = 0;
    private ContentType mContentType;

    public VideoServer(String filepath,int width,int height,int port,ContentType contentType) {
        super(port);
            mVideoWidth  = width;
            mVideoHeight = height;
        mVideoFilePath = filepath;
        mContentType= contentType;
    }
    
    @Override
    public Response serve(IHTTPSession session) {        
        Log.d(TAG,"OnRequest: "+session.getUri());
        if(REQUEST_ROOT.equals(session.getUri())) {
            return responseRootPage(session);
        }
        else if(mVideoFilePath.equals(session.getUri())) {
            return responseVideoStream(session);
        }
        return response404(session,session.getUri());
    }

    public Response responseRootPage(IHTTPSession session) {
        File file = new File(mVideoFilePath);
        if(!file.exists()) {
            return response404(session,mVideoFilePath);
        }

        if(mContentType.equals(ContentType.Video))
        {
            return  new Response(newVideoStringBuilder().toString());
        }else if (mContentType.equals(ContentType.Image))
        {
            return new Response(newImageStringBuilder().toString());
        }else
        {
            return new Response(newAudioStringBuilder().toString());
        }
    }

    private StringBuilder newAudioStringBuilder()
    {
       /* <!DOCTYPE HTML>
<html>
<body>

<audio controls="controls">
  <source src="/i/song.mp3" type="audio/mpeg" />
            Your browser does not support the audio element.
            </audio>

</body>
</html>*/
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append("<audio controls autoplay>");
        builder.append("<source src="+getQuotaStr(mVideoFilePath)+" ");
        builder.append("type="+getQuotaStr("audio/mpeg")+">");
        builder.append("Your browser doestn't support HTML5");
        builder.append("</audio>");
        builder.append("</body></html>\n");

        return builder;
    }

    private StringBuilder newVideoStringBuilder()
    {
        while(mVideoWidth>1500 && mVideoHeight>700)
        {
            mVideoWidth  = (int)(mVideoWidth*0.75);
            mVideoHeight = (int)(mVideoHeight*0.75);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("<video ");
        builder.append("width="+getQuotaStr(String.valueOf(mVideoWidth))+" ");
        builder.append("height="+getQuotaStr(String.valueOf(mVideoHeight))+" ");
        builder.append("controls autoplay>");
        builder.append("<source src="+getQuotaStr(mVideoFilePath)+" ");
        builder.append("type="+getQuotaStr("video/mp4")+">");
        builder.append("Your browser doestn't support HTML5");
        builder.append("</video>");
        builder.append("</body></html>\n");
        return builder;
    }

    private StringBuilder newImageStringBuilder()
    {
        while(mVideoWidth>1500 && mVideoHeight>700)
        {
            mVideoWidth  = (int)(mVideoWidth*0.75);
            mVideoHeight = (int)(mVideoHeight*0.75);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append("<img ");
        builder.append("width="+getQuotaStr(String.valueOf(mVideoWidth))+" ");
        builder.append("height="+getQuotaStr(String.valueOf(mVideoHeight))+" ");
        builder.append("src="+getQuotaStr(mVideoFilePath)+">");
        builder.append("</body></html>\n");

        return builder;
    }

    
    public Response responseVideoStream(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream(mVideoFilePath);
            return new NanoHTTPD.Response(Status.OK, "video/mp4", fis);
        } 
        catch (FileNotFoundException e) {        
            e.printStackTrace();
            return response404(session,mVideoFilePath);
        } 
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
