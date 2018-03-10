package com.example.asus.mydlnaapplicationone.SSDP;

import android.app.AlertDialog;
import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;

/**
 * Created by asus on 2018/3/10.
 */

public class SsdpSocket {


    SocketAddress mSSDPMulticastGroup = null;

    MulticastSocket mSSDPSocket = null;

    InetAddress broadcastgroup = null;

    public SsdpSocket() throws IOException {

        mSSDPSocket = new MulticastSocket(SsdpConstants.SEND_PORT); // 1900端口

        broadcastgroup = InetAddress.getByName(SsdpConstants.ADDRESS);// 239.255.255.250

        mSSDPSocket.joinGroup(broadcastgroup);

    }

	/* Used to send SSDP packet */

    public void send(String data, Context context) throws IOException {

        if (broadcastgroup != null && mSSDPSocket!=null) {

            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(),

                    broadcastgroup, SsdpConstants.SEND_PORT);
            try {

                mSSDPSocket.send(dp);

                new AlertDialog.Builder(context).setTitle("Send").setMessage("send successul!")
                        .setPositiveButton("return", null).show();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                new AlertDialog.Builder(context).setTitle("send failed").setMessage(e.getMessage().toString())
                        .setPositiveButton("return", null).show();
            }
        } else {

        }
    }

	/* Used to receive SSDP packet */

    public DatagramPacket receive() throws IOException {

        byte[] buf = new byte[1024];

        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        mSSDPSocket.receive(dp);

        return dp;

    }

    public void close() {

        if (mSSDPSocket != null) {

            mSSDPSocket.close();

        }

    }

}
