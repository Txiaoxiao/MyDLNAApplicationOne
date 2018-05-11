package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.mydlnaapplicationone.MediaServer.MediaServer;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    ListView listview;
    TextView textviewItemTitle;
    TextView textViewItemDetails;
    List<SettingData> listSettingData;
    boolean sharingFileList = false;
    MediaServer videoServer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_setting, container, false);

         listview = view.findViewById(R.id.listview_setting);
         textviewItemTitle=view.findViewById(R.id.textview_setting_title);
         textViewItemDetails=view.findViewById(R.id.textview_setting_detail);
         initData();

         final SettingAdapter adapter = new SettingAdapter(getActivity(),listSettingData);
         listview.setAdapter(adapter);

         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 if(listSettingData.get(position).getTitle().equals("Network Setting"))
                 {
                     Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                     startActivity(intent);
                 }
                 if(listSettingData.get(position).getTitle().equals("Sharing Media File"))
                 {
                     if(!sharingFileList)
                     {
                         Toast.makeText(getContext(),"Sharing Media File List.",Toast.LENGTH_LONG).show();
                         listSettingData.get(position).setDescription("click to not sharing media file list to pc.");
                         //try {
                          // videoServer=  new MediaServer(8080);
                          // videoServer.start();
                        // } catch (IOException e) {
                         //}
                         sharingFileListToPc();
                     }else {
                         Toast.makeText(getContext(),"Close Sharing Media File List.",Toast.LENGTH_LONG).show();
                         listSettingData.get(position).setDescription("click to sharing media file list to pc.");
                         if (videoServer!=null)
                         {
                             //videoServer.stop();
                         }
                     }
                     sharingFileList=!sharingFileList;
                     adapter.notifyDataSetChanged();

                 }
             }
         });
        return view;
    }

    private void sharingFileListToPc() {
       new SharingFileListToPcAsyncTask().execute();
    }

    private void initData() {
        listSettingData = new ArrayList<>();
        SettingData dataMediaSharing = new SettingData("Sharing Media File","click to sharing media file list on pc.");
        listSettingData.add(dataMediaSharing);
        SettingData dataWifiManager = new SettingData("Network Setting","manager wireless network.");
        listSettingData.add(dataWifiManager);
    }

    private class SettingAdapter extends BaseAdapter{

        Activity context;
        List<SettingData> settingDataList;

        public SettingAdapter(Activity context,List<SettingData> objects){
            super();
            this.context = context;
            settingDataList = objects;
        }

        @Override
        public Object getItem(int position) {
            return settingDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return settingDataList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                LayoutInflater inflater =context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.setting_item,null);
                holder.settingDataTitle = convertView.findViewById(R.id.textview_setting_title);
                holder.settingDataDescription=convertView.findViewById(R.id.textview_setting_detail);
                holder.image = convertView.findViewById(R.id.imageview_setting_item);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            SettingData data = settingDataList.get(position);
            holder.settingDataTitle.setText(data.getTitle());
            holder.settingDataDescription.setText(data.getDescription());
            if(data.getTitle().equals("Sharing Media File"))
            {
                holder.image.setImageBitmap(Utils.getBitmap(getActivity(),R.drawable.ic_sharing));
            }
            else{
                holder.image.setImageBitmap(Utils.getBitmap(getActivity(),R.drawable.ic_wifi));
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView image;
            TextView settingDataTitle;
            TextView settingDataDescription;
        }
    }

    private class SharingFileListToPcAsyncTask extends AsyncTask{

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            InetAddress address = null;
            DatagramSocket socket =null;

            try {
                address = SsdpConstants.selectedDevice.getDeviceInetAddress();
                int port = 10003;
                byte data[] = Utils.pathToUrl(getActivity(),"/startpage.html",8080).getBytes("utf-8");
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket = new DatagramSocket();
                socket.send(packet);
                socket.close();

            } catch (UnknownHostException e) {
                Log.e("error","unknownHostException in ResponseToPCAsyncTask ");

            }catch (SocketException e) {
                Log.e("error","SocketException in ResponseToPCAsyncTask ");

            } catch (IOException e) {
                Log.e("error","IOException  in ResponseToPCAsyncTask ");
            }

            return objects;
        }
    }
}
