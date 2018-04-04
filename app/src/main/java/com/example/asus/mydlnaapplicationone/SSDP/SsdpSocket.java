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

        mSSDPSocket = new MulticastSocket(1900); // 1900端口

        broadcastgroup = InetAddress.getByName(SsdpConstants.ADDRESS);// 239.255.255.250

        mSSDPSocket.joinGroup(broadcastgroup);

    }

	/* send SSDP packet */

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
                throw  new IOException("send dp failed!");
                /*new AlertDialog.Builder(context).setTitle("send failed").setMessage(e.getMessage().toString())
                        .setPositiveButton("return", null).show();*/
            }
        } else {
            throw new NullPointerException("null broadcastgroup or mssdpsocket");

        }
    }

	/*  receive SSDP packet */

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
