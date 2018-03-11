package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

    ListView listView;
    DeviceListViewAdapter listAdapter;
    Device selectedDevice;
    List<Device> deviceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device,container,false);

        listView = view.findViewById(R.id.listview_availableDeviceList);

        deviceList = new ArrayList<>();
        for(int i = 0;i <=6;i++)
        {
            deviceList.add(new Device("Device "+i));
        }

        listAdapter = new DeviceListViewAdapter(getActivity(),deviceList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDevice = deviceList.get(position);
                listAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }


    public class DeviceListViewAdapter extends BaseAdapter {

        private Activity context;
        private List<Device> deviceList;

        public DeviceListViewAdapter(Activity context, List<Device> deviceList)
        {
            super();
            this.context = context;
            this.deviceList = deviceList;
        }

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null)
            {
                holder= new ViewHolder();
                LayoutInflater inflater =context.getLayoutInflater();
                convertView=inflater.inflate(R.layout.device_item,null);
                holder.textView =convertView.findViewById(R.id.textview_device_item);
                holder.checkBox= convertView.findViewById(R.id.checkbox_device_item);
                convertView.setTag(holder);
            }else
            {
                holder =(ViewHolder) convertView.getTag();
            }

            Device device = deviceList.get(position);
            holder.textView.setText(device.getName());
            if(device.equals(selectedDevice))
            {
               holder.checkBox.setChecked(true);
            }else {
               holder.checkBox.setChecked(false);
            }

            return convertView;
        }
    }

    public final class ViewHolder
    {
        public TextView textView;
        public CheckBox checkBox;
    }


}
