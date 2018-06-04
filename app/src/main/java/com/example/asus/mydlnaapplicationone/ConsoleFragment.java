package com.example.asus.mydlnaapplicationone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class ConsoleFragment extends Fragment implements View.OnClickListener {

    public static boolean isplay = false;

    private final String TAG = "VideoControl";

    private SeekBar mSeekBar = null;

    private TextView textviewNameTitle;

    private TextView textviewAuthorName;

    private TextView textviewCurrentDuration;
    private  TextView textviewTotalTime;

    private ImageView imageviewPlayBtn;

    private ImageView imageviewVoicePlus;

    private ImageView imageviewVoiceCut;

    private ImageView imageviewVoiceMute;

    private boolean isToMute = true;

    public String name;

    public ControlMessageType controlMessageTypeToSend;
    public String controlMessageParam;
    public int soundUp =2;
    public int soundDown=2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

          View view = inflater.inflate(R.layout.fragment_control, container, false);

          initView(view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mSeekBar.setMax(Integer.parseInt(Globals.getInstance().getCurrentvideoDuration())/1000);
        textviewTotalTime.setText(Utils.secToTime(Integer.parseInt(Globals.getInstance().getCurrentvideoDuration())/1000));
        textviewCurrentDuration.setText(Utils.secToTime(0));
        ContentItem item;
        if((item= Globals.getInstance().getCurrentContentItem())!=null)
        {
            textviewNameTitle.setText(item.getName());
        }
    }

    private void initView(View view) {
        textviewNameTitle = view.findViewById(R.id.media_tv_title);
        textviewAuthorName =view.findViewById(R.id.media_tv_author);

        textviewCurrentDuration=view.findViewById(R.id.media_tv_time);
        textviewTotalTime = view.findViewById(R.id.media_tv_total_time);

        imageviewPlayBtn = view.findViewById(R.id.media_iv_play);
        imageviewVoicePlus =view.findViewById(R.id.media_iv_voc_plus);
        imageviewVoiceCut = view.findViewById(R.id.media_iv_voc_cut);
        imageviewVoiceMute = view.findViewById(R.id.media_iv_voc_mute);
        imageviewPlayBtn.setOnClickListener(this);
        imageviewVoicePlus.setOnClickListener(this);
        imageviewVoiceCut.setOnClickListener(this);
        imageviewVoiceMute.setOnClickListener(this);
        imageviewPlayBtn.setBackgroundResource(R.drawable.icon_media_pause);
        imageviewVoiceMute.setBackgroundResource(R.drawable.icon_voc_mute);

        mSeekBar = view.findViewById(R.id.media_seekBar);

        mSeekBar.setOnSeekBarChangeListener(new PlaySeekBarListener());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.media_iv_play: {
                playPause();
                break;
            }
            case R.id.media_iv_voc_plus: {
                soundUp();
                break;
            }
            case R.id.media_iv_voc_cut: {
                soundDown();
                break;
            }
            case R.id.media_iv_voc_mute: {
                soundMute();
                break;
            }
            default:
                break;
        }
    }

    private void playPause() {
        if (isplay) {
            imageviewPlayBtn.setBackgroundResource(R.drawable.icon_media_pause);
            isplay = !isplay;
            sendControlMessageToPc(ControlMessageType.PLAY,null);
        } else {
            isplay = !isplay;
            imageviewPlayBtn.setBackgroundResource(R.drawable.icon_media_play);
            sendControlMessageToPc(ControlMessageType.PAUSE,null);
        }
    }

    private void soundUp() {
        sendControlMessageToPc(ControlMessageType.SOUND_UP,String.valueOf(soundUp));
    }

    private void soundDown() {
        sendControlMessageToPc(ControlMessageType.SOUND_DOWN,String.valueOf(soundDown));
    }

    private void soundMute() {
        if (!isToMute) {
            imageviewVoiceMute.setBackgroundResource(R.drawable.icon_voc_mute);
            isToMute=!isToMute;
        } else {
            imageviewVoiceMute.setBackgroundResource(R.drawable.icon_voc_mute_click);
            isToMute=!isToMute;
        }
        sendControlMessageToPc(ControlMessageType.MUTE,null);
    }


    class PlaySeekBarListener implements SeekBar.OnSeekBarChangeListener {
        PlaySeekBarListener() {}

        public void onProgressChanged(SeekBar paramSeekBar, int paramInt,
                                      boolean paramBoolean) {
            textviewCurrentDuration.setText(Utils.secToTime(paramInt));

        }

        public void onStartTrackingTouch(SeekBar paramSeekBar) {

        }

        public void onStopTrackingTouch(SeekBar paramSeekBar) {
                String str = Utils.secToTime(paramSeekBar.getProgress());
            sendControlMessageToPc(ControlMessageType.SEEKBAR,String.valueOf(paramSeekBar.getProgress()));
        }
    }

    public void sendControlMessageToPc(ControlMessageType type,String param) {
        controlMessageTypeToSend=type;
        controlMessageParam = param;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO:发送控制消息到pc
                InetAddress address = null;
                DatagramSocket socket =null;

                try {
                    address = SsdpConstants.selectedDevice.getDeviceInetAddress();
                    int port = 10003;

                    String str = controlMessageTypeToSend.getControlMessage();
                    if(controlMessageTypeToSend.equals(ControlMessageType.SEEKBAR))
                    {
                        str = str +";"+controlMessageParam;
                    }

                    byte[] data =str.getBytes("utf-8");
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                    socket = new DatagramSocket();
                    socket.send(packet);

                    socket.close();
                }catch (UnsupportedEncodingException e) {
                }catch (SocketException e) {
                }catch (IOException e) {
                }catch (Exception e){

                }
            }
        }).start();
    }
}
