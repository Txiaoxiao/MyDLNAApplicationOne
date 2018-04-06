package com.example.asus.mydlnaapplicationone;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.example.asus.mydlnaapplicationone.Adapters.DeviceListViewAdapter;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSearchMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;

/**
 * Created by asus on 2018/3/29.
 */

public class DeviceSearch {

    List<Device> newDeviceList;
    List<Device> deviceList;
    ListView listview;
    Activity activity;
    DeviceListViewAdapter adapter;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            /*DeviceListViewAdapter adapter = new DeviceListViewAdapter(activity,(List<Device>)msg.obj);
            listview.setAdapter(adapter);*/

            adapter.getDeviceList().clear();
            adapter.getDeviceList().add(new Device("device name"));

            /*List<Device> devices = (List<Device>)msg.obj;

            for (Device device:devices
                 ) {
                adapter.getDeviceList().add(new Device(device.getName()));
            }*/

            adapter.notifyDataSetChanged();


        }
    };

    public void getDevicesByThread(Activity activity, DeviceListViewAdapter adapter, ListView listView, final List<Device> deviceList)
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

                    newDeviceList = searchRemoteDevices();
                    Message message =Message.obtain();
                    message.obj = newDeviceList;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    Log.e("error","searchRemoteDevices error in getDevicesByThread!");
                }
            }
        }.start();

    }

    private List<Device> searchRemoteDevices() throws IOException {
        List<Device> devices;

        sendSearchMessage();
       // devices = receiveResponseMessage();
        //return  devices;
        return new ArrayList<>();
    }

    private List<Device> receiveResponseMessage() throws IOException {

        List<Device> devices = new ArrayList<>();
        DatagramSocket msr = new DatagramSocket(SsdpConstants.RECEIVE_PORT);//10002
        byte[] buffer = new byte[1024];

        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

        msr.receive(dp);
        String s = new String(dp.getData(), 0, dp.getLength());

        // TODO: only choose NOTIFY and RESPONSE packet, get LOCATION form s,
        // and get DDD from the url.
        SsdpMessage ssdpMessage = SsdpMessage.toMessage(s);
       // if (ssdpMessage.getType().equals(SsdpMessageType.RESPONSE)) {
            // URL url = new URL(ssdpMessage.getHeader("LOCATION"));
            devices.add(new Device(ssdpMessage.getHeader("USN")));
       // }
        return devices;
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
