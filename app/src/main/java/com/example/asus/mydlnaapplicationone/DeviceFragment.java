package com.example.asus.mydlnaapplicationone;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.mydlnaapplicationone.Adapters.DeviceListViewAdapter;
import com.example.asus.mydlnaapplicationone.Dmax.dialog.SpotsDialog;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSearchMessage;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;

;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

    ListView listView;
    FloatingActionButton refershButton;
    DeviceListViewAdapter listAdapter;
    List<Device> deviceList;
    int clickPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device,container,false);

        listView = view.findViewById(R.id.listview_availableDeviceList);
        refershButton = view.findViewById(R.id.floatingactionbutton_refresh);
        deviceList = new ArrayList<>();

        ObjectAnimator animator= ObjectAnimator.ofFloat(refershButton,"rotation",0,360);
        animator.setRepeatCount(2);
        animator.setDuration(800);
        animator.start();

        if(!Utils.isWiFi(getContext()))
        {
            Toast.makeText(getContext(),"The wireless network is not connected, please set up the network first",Toast.LENGTH_LONG).show();
        }
        else
        {
            searchRemoteDevices();
        }

        refershButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(deviceList==null))
                {
                    deviceList.clear();
                    listAdapter.notifyDataSetChanged();
                }

                ObjectAnimator animator= ObjectAnimator.ofFloat(view,"rotation",0,360);
                animator.setRepeatCount(2);
                animator.setDuration(800);
                animator.start();

                searchRemoteDevices();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 

                if(SsdpConstants.selectedDevice == (deviceList.get(position)))
                {
                    SsdpConstants.selectedDevice = null;
                    SsdpConstants.RemoteDeviceName=null;
                    Globals.getInstance().setSelectedDevice(null);
                    listAdapter.notifyDataSetChanged();
                    return;
                }
                //身份验证
                clickPosition =position;

                IdentityVerificationDialog.OnConnectButtonListener connectListener = new IdentityVerificationDialog.OnConnectButtonListener() {
                    @Override
                    public void getPassword(String password) {
                        if(password.equals(SsdpConstants.IDENTIFYVERIFICATION_CANCEL))
                        {
                           return;
                        }
                        else if(password.equals(null))
                        {
                            Toast.makeText(getContext(),"please input the pairing code!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String[] params = new String[2];
                            params[0]=String.valueOf(clickPosition);
                            params[1]=password;
                            new IdentityVerificationAsyncTask().execute(params);

                            //tcp连接



                           /* Globals.getInstance().setSelectedDevice(deviceList.get(position));
                            SsdpConstants.RemoteDeviceName = deviceList.get(position).getName();
                            SsdpConstants.selectedDevice = deviceList.get(position);
                            listAdapter.notifyDataSetChanged();*/
                        }
                    }
                };

                new IdentityVerificationDialog(getActivity(),connectListener).show();

            }
        });


        return view;
    }

    private void searchRemoteDevices() {
        new SearchRemoteDevicesAsyncTask().execute();
    }

    class SearchRemoteDevicesAsyncTask extends  AsyncTask<Void,Void,List<Device>>{

        @Override
      protected List<Device> doInBackground(Void... voids) {
            try {
                List<Device> devices = getRemoteDevices();
                deviceList.clear();
                deviceList.addAll(devices);
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
          if(deviceList.size()==0)
          {
              Toast.makeText(getContext()," No Devices can be found near.",Toast.LENGTH_LONG).show();
          }
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
        datagramSocket.setSoTimeout(1000);
        byte[] buffer = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

        Date before = new Date();
        Date now = new Date();
        long  t=(now.getTime()-before.getTime());
        while(t<3000)
        {
            try {
                datagramSocket.receive(datagramPacket);
            }catch (Exception e)
            {
                now = new Date();
                t=(now.getTime()-before.getTime());
                continue;
            }
            Log.i("receive","receive response message successful");
            String s = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            InetAddress remoteAddress =datagramPacket.getAddress();

            // TODO: only choose NOTIFY and RESPONSE packet, get LOCATION form s,and get DDD from the url
            SsdpMessage ssdpMessage = SsdpMessage.toMessage(s);
            Device device = new Device(ssdpMessage.getHeader("NAME"));
            device.setDeviceInetAddress(remoteAddress);
            device.setDescription(ssdpMessage.getHeader("SERVER"));
            if(devices.contains(device))
            {
                continue;
            }
            else {
                devices.add(device);
            }
            now = new Date();
            t=(now.getTime()-before.getTime());
        }

        datagramSocket.close();
        return devices;
    }

    private void sendSearchMessage() {

        SsdpSearchMessage searchContentDirectory = new SsdpSearchMessage(SsdpConstants.ST_RootDevice);
        SsdpSocket sock;

        try {
            sock = new SsdpSocket();
            sock.send(searchContentDirectory.toString());
            sock.close();
        } catch (IOException e) {
            Log.e("error", "sendSeaerchMessage error!");
        }

    }

    private class IdentityVerificationAsyncTask extends AsyncTask<String,Void,Integer> {

        AlertDialog spotsDialog;

        @Override
        protected Integer doInBackground(String... strings) {
            int result =Integer.parseInt(strings[0]); // -1 means failed to connected.
                                                        // -2 means error password.
            String password = strings[1];
            int position = result;

            //TODO:与远端设备建立tcp连接，验证密码
            InetAddress serverAddress = deviceList.get(position).getDeviceInetAddress();

            try {
                Socket socket = new Socket(serverAddress,SsdpConstants.CONNECTING_PORT);//port:10002
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                out.print(password+"\n");
                out.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String connectingResult = reader.readLine();// 0 for error password. 1 for connect successfully.
                if(Integer.parseInt(connectingResult) == 0)
                {
                    result = -2;//error password
                    out.close();
                    reader.close();
                    socket.close();

                }
                else if(Integer.parseInt(connectingResult) == 1)
                {

                }
                else
                {
                    result =-1;//failed to connect.
                    out.close();
                    reader.close();
                    socket.close();
                }

            } catch (IOException e) {
               result = -1;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spotsDialog = new SpotsDialog(getActivity());
            spotsDialog.show();
        }

        @Override
        protected void onPostExecute(Integer position) {
            super.onPostExecute(position);
            spotsDialog.dismiss();
            if(position == -1)
            {
                Toast.makeText(getActivity(),"Connected to Device Failed.",Toast.LENGTH_LONG).show();
            }else if(position == -2)
            {
                Toast.makeText(getActivity(),"Error Pairing Code.",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(),"Connect successfully.",Toast.LENGTH_LONG).show();

                Globals.getInstance().setSelectedDevice(deviceList.get(position));
                SsdpConstants.RemoteDeviceName = deviceList.get(position).getName();
                SsdpConstants.selectedDevice = deviceList.get(position);
                listAdapter.notifyDataSetChanged();
            }

        }

    }
}
