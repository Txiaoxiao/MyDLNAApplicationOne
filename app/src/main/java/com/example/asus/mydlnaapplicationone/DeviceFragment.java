package com.example.asus.mydlnaapplicationone;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asus.mydlnaapplicationone.Adapters.DeviceListViewAdapter;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSearchMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSocket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import GlobalVariables.Globals;

;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

    ListView listView;
    DeviceListViewAdapter listAdapter;
    List<Device> deviceList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device,container,false);

        listView = view.findViewById(R.id.listview_availableDeviceList);
        deviceList = new ArrayList<>();

        new SearchRemoteDevicesAsyncTask().execute();
       /* listAdapter = new DeviceListViewAdapter(getActivity(),deviceList);
        listView.setAdapter(listAdapter);
*/
       // new DeviceSearch().getDevicesByThread(getActivity(),listAdapter,listView,deviceList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Globals.getInstance().setSelectedDevice(deviceList.get(position));
               SsdpConstants.RemoteDeviceName = deviceList.get(position).getName();

                listAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


  class SearchRemoteDevicesAsyncTask extends  AsyncTask<Void,Void,List<Device>>{

        @Override
      protected List<Device> doInBackground(Void... voids) {
            try {
                deviceList.clear();
                deviceList = getRemoteDevices();
            } catch (IOException e) {
                Log.e("error","getRemoteDevices");
            }
            return deviceList;
        }

      @Override
      protected void onPostExecute(List<Device> deviceList) {
          super.onPostExecute(deviceList);
           listAdapter = new DeviceListViewAdapter(getActivity(),deviceList);
          listView.setAdapter(listAdapter);
      }
  }

    private List<Device> getRemoteDevices() throws IOException {

        sendSearchMessage();
        try {
            return receiveResponseMessage();
        } catch (InterruptedException e) {
            Log.e("error","receiveResponsemessage");
        }
        return null;

    }

    private List<Device> receiveResponseMessage() throws IOException, InterruptedException {

        List<Device> devices = new ArrayList<>();

        DatagramSocket datagramSocket = new DatagramSocket(SsdpConstants.RECEIVE_PORT);//10002
        byte[] buffer = new byte[1024];

        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(datagramPacket);
        Log.i("receive","receive response message successful");
        String s = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        datagramSocket.close();
       /* ServerSocket socket = new ServerSocket(10002);

        final Socket clientSocket = socket.accept();
        InputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while((n=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,n);
        }
        String s = new String(outputStream.toByteArray());
        clientSocket.shutdownInput();
        outputStream.close();
*/


        // TODO: only choose NOTIFY and RESPONSE packet, get LOCATION form s,and get DDD from the url

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
            sock.send(searchContentDirectory.toString(), getActivity());
        } catch (IOException e) {
            Log.e("error", "sendSeaerchMessage error!");
        }

    }

}
