package com.example.asus.mydlnaapplicationone.SSDP;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Tang on 2018/3/10.
 */

public class SsdpMessage {

    private static final String EOL = "\r\n";

    private final SsdpMessageType type;
    private final Map<String, String> headers = new LinkedHashMap<String, String>();

    public SsdpMessage(SsdpMessageType type) {
        this.type = type;
    }

    public SsdpMessageType getType() {
        return type;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public SsdpMessage setHeader(String name, String value) {
        headers.put(name.toUpperCase(), value);
        return this;
    }

    public SsdpNotificationType getNotificationType() {
        return SsdpNotificationType.fromRepresentation(headers.get(SsdpCommonHeaders.NTS.name()));
    }
    public static SsdpMessage toMessage(String raw) {
        if (!raw.endsWith(EOL + EOL)) {
           // throw new IllegalArgumentException("message is not complete, it should end with a blank line");
        }

        String[] lines = raw.split(EOL);
        SsdpMessage message = new SsdpMessage(SsdpMessageType.fromStartLine(lines[0]));

        for (int i = 1; i < lines.length; ++i) {
            int index = lines[i].indexOf(":");
            if (index < 1) {
                //throw new IllegalArgumentException(String.format("invalid header format, line=%s, header=%s", i, lines[i]));
            }

            message.setHeader(lines[i].substring(0, index).trim(), lines[i].substring(index + 1).trim());
        }

        return message;
    }

}
