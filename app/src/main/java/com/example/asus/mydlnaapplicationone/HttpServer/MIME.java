package com.example.asus.mydlnaapplicationone.HttpServer;

import java.util.HashMap;

public class MIME {
    HashMap<String, String> mime = new HashMap<String, String>();

    public MIME() {
        mime.put("css", "text/css");
        mime.put("gif", "image/gif");
        mime.put("html", "text/html;charset=utf-8");
        mime.put("ico", "image/x-icon");
        mime.put("jpeg", "image/jpeg");
        mime.put("jpg", "image/jpeg");
        mime.put("js", "text/javascript");
        mime.put("json", "application/json");
        mime.put("pdf", "application/pdf");
        mime.put("png", "image/png");
        mime.put("svg", "image/svg+xml");
        mime.put("swf", "application/x-shockwave-flash");
        mime.put("tiff", "image/tiff");
        mime.put("txt", "text/plain;charset=utf-8");
        mime.put("wav", "audio/x-wav");
        mime.put("wma", "audio/x-ms-wma");
        mime.put("wmv", "video/x-ms-wmv");
        mime.put("xml", "text/xml");
    }

    public HashMap<String, String> getMime() {
        return mime;
    }
}

