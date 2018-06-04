package com.example.asus.mydlnaapplicationone.MediaServer;

import android.util.Log;

import com.example.asus.mydlnaapplicationone.ContentItem;
import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class MediaServer extends NanoHTTPD {
    
    public static final String TAG = MediaServer.class.getSimpleName();
    private static final String REQUEST_ROOT = "/";
    private static final String REQUEST_IMAGE_LIST = "/image_list.html";
    private static final String REQUEST_Video_LIST = "/video_list.html";
    private static final String REQUEST_Audio_LIST = "/audio_list.html";

    private static final String REQUEST_STARTPAGE = "/startpage.html";

    List<ContentItem> imageList;
    List<ContentItem> videoList;
    List<ContentItem> audioList;

    public MediaServer(int port) {
        super(port);
        this.imageList = Globals.getInstance().getImageList();
        this.videoList = Globals.getInstance().getVideoList();
        this.audioList = Globals.getInstance().getAudioList();
    }
    
    @Override
    public Response serve(IHTTPSession session) {        
        Log.d(TAG,"OnRequest: "+session.getUri());
        if(REQUEST_ROOT.equals(session.getUri())) {
            return responseRootPage(session);
        }
        else  if(REQUEST_IMAGE_LIST.equals(session.getUri())){
            return responseMediaList(getStringBuilder(imageList));
        }
        else if(REQUEST_Video_LIST.equals(session.getUri()))
        {
            return responseMediaList(getStringBuilder(videoList));
        }
        else if(REQUEST_Audio_LIST.equals(session.getUri()))
        {
            return responseMediaList(getStringBuilder(audioList));
        }
        else if(REQUEST_STARTPAGE.equals(session.getUri()))
        {
            return responseRootPage(session);
        }
        else {
            for (ContentItem item: videoList
                 ) {
                if(item.getPath().equals(session.getUri()))
                {
                    Globals.getInstance().setCurrentvideoDuration(item.getDuration());
                    Globals.getInstance().setCurrentContentItem(item);
                    break;
                }
            }
            return responseMediaStream(session);
        }
    }

    public Response responseRootPage(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream("storage/emulated/0/startpage.html");
            String mimeType=getMimeType(session.getUri());
            return new Response(Status.OK, "text/html;charset=utf-8", fis);
        }
        catch (Exception e) {
            e.printStackTrace();
            return response404(session, "/startpage.html");
        }

    }

    public Response responseMediaList(StringBuilder builder)
    {
        return new Response(Status.OK,"text/html;charset=utf-8",String.valueOf(builder));
    }


    //TODO:添加image_list.html/video_list/audio_list.html的生成及请求相应
    private StringBuilder getStringBuilder(List<ContentItem> list)
    {
        /*
        * <html><body><dl>
   <dt></dt>
   <dd>
      <img src="/i/eg_mouse.jpg" width="24" height="24" />:
     <a href="http://www.w3school.com.cn/html/html_audio.asp">本文本</a>
   </dd>
</dl>
</body>
</html>
        * */
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPER html><html><body>");
        builder.append("<ol>");
        for(int i = 0;i<list.size();i++)
        {
            builder.append("<li><a href="+getQuotaStr(list.get(i).getPath())+">"+list.get(i).getName()+"</a></li>");
        }
        builder.append("</ol></body></html>\n");

        return builder;
    }

    
    public Response responseMediaStream(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream(session.getUri());
            String mimeType=getMimeType(session.getUri());
            return new Response(Status.OK, mimeType, fis);
        } 
        catch (FileNotFoundException e) {        
            e.printStackTrace();
            return response404(session,session.getUri());
        } 
    }

    private String getMimeType(String uri) {
        String mimeType="text/html;charset=utf-8";
        //TODO: set minetype according to .jpg/.png/.mp3/.mp4
        if(uri.contains(".mp4"))
        {
            mimeType="video/mp4";
        }else if(uri.contains(".mp3"))
        {
            mimeType = "audio/mp3";
        }else if(uri.contains(".jpg"))
        {
            mimeType="image/jpg";
        }else if(uri.contains(".png"))
        {
            mimeType="image/png";
        }
        return mimeType;
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
