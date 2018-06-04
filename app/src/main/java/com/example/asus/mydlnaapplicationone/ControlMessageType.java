package com.example.asus.mydlnaapplicationone;

/**
 * Created by asus on 2018/5/13.
 */

public enum  ControlMessageType {
    PLAY("play"),
    PAUSE("pause"),
    SEEKBAR("seekbar"),
    MUTE("mute"),
    SOUND_UP("sound up"),
    SOUND_DOWN("sound down"),
    LIST("list");



    private final String controlMessage;

     ControlMessageType(String controlMessage)
    {
        this.controlMessage=controlMessage;
    }

    public String getControlMessage() {
        return controlMessage;
    }

}
