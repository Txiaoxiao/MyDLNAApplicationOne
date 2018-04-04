package com.example.asus.mydlnaapplicationone;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.asus.mydlnaapplicationone.Adapters.DeviceListViewAdapter;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpMessageType;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSearchMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/3/29.
 */

public class DeviceSearch {

    List<Device> newDeviceList;
    List<Device> deviceList;
    ListView listview;
    Activity activity;
    DeviceListViewAdapter adapter;


    private Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            deviceList.clear();
            for (Device device:newDeviceList
                 ) {
               // deviceList.add(device);
                deviceList.add(new Device("device name"));
            }
            adapter.notifyDataSetChanged();


        }
    };

    public void getDevicesByThread(Activity activity, DeviceListViewAdapter adapter,ListView listView,List<Device> deviceList)
    {
        this.activity = activity;
        this.listview = listView;
        this.adapter = adapter;
        this.deviceList = deviceList;
        this.newDeviceList = new ArrayList<>();

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {

                    searchRemoteDevices();

                } catch (IOException e) {
                    Log.e("error","searchRemoteDevices error in getDevicesByThread!");
                }
            }
        }.start();

    }

    private void searchRemoteDevices() throws IOException {
        sendSearchMessage();
        receiveResponseMessage();
    }

    private void receiveResponseMessage() throws IOException {

        DatagramSocket msr = new DatagramSocket(SsdpConstants.RECEIVE_PORT);//10002
        byte[] buffer = new byte[1024];

        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        msr.receive(dp);
        String s = new String(dp.getData(), 0, dp.getLength());

        // TODO: only choose NOTIFY and RESPONSE packet, get LOCATION form s,
        // and get DDD from the url.
        SsdpMessage ssdpMessage = SsdpMessage.toMessage(s);
        if (ssdpMessage.getType().equals(SsdpMessageType.RESPONSE)) {
            // URL url = new URL(ssdpMessage.getHeader("LOCATION"));
            newDeviceList.add(new Device(ssdpMessage.getHeader("USN")));
        }
    }

    private void sendSearchMessage() {

        SsdpSearchMessage searchContentDirectory = new SsdpSearchMessage(SsdpConstants.ST_RootDevice);

        SsdpSocket sock;

        try {
            sock = new SsdpSocket();
            sock.send(searchContentDirectory.toString(), activity);
        } catch (IOException e) {
            Log.e("error", "sendSeaerchMessage error!");
        }

    }


}
