package com.example.asus.mydlnaapplicationone.SSDP;

/**
 * Created by Tang on 2018/3/10.
 */

public enum SsdpNotificationType {

    ALIVE("ssdp:alive"),
    BYEBYE("ssdp:byebye"),
    UPDATE("ssdp:update");

    private final String representation;

    SsdpNotificationType(String representation) {
        this.representation = representation;
    }

    public static SsdpNotificationType fromRepresentation(String representation) {
        if (representation == null) {
            return null;
        }

        for (SsdpNotificationType type : values()) {
            if (type.representation.equals(representation)) {
                return type;
            }
        }

        throw new IllegalArgumentException("representation=" + representation);
    }

    public String getRepresentation() {
        return representation;
    }

}
