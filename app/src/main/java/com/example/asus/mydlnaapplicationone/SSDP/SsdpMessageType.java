package com.example.asus.mydlnaapplicationone.SSDP;

/**
 * Created by asus on 2018/3/10.
 */

public enum SsdpMessageType {


    MSEARCH("M-SEARCH * HTTP/1.1"),
    NOTIFY("NOTIFY * HTTP/1.1"),
    RESPONSE("HTTP/1.1 200 OK");

    private final String httpHeader;

    SsdpMessageType(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public static SsdpMessageType fromStartLine(String startLine) {
        for (SsdpMessageType type : values()) {
            if (type.getHttpHeader().equals(startLine)) {
                return type;
            }
        }

        throw new IllegalArgumentException("startLine=" + startLine);
    }

    public String getHttpHeader() {
        return httpHeader;
    }

}
