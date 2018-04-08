package com.example.asus.mydlnaapplicationone.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asus.mydlnaapplicationone.Device;
import com.example.asus.mydlnaapplicationone.R;

import java.util.List;

import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;

/**
 * Created by Tang on 2018/3/10.
 */

public class DeviceListViewAdapter extends BaseAdapter {

    private Activity context;
    private List<Device> deviceList;

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }



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

        DeviceViewHolder holder;
        if(convertView == null)
        {
            holder= new DeviceViewHolder();
            LayoutInflater inflater =context.getLayoutInflater();
            convertView=inflater.inflate(R.layout.device_item,null);
            holder.textView =convertView.findViewById(R.id.textview_device_item);
            holder.checkBox= convertView.findViewById(R.id.checkbox_device_item);
            convertView.setTag(holder);
        }else
        {
            holder =(DeviceViewHolder) convertView.getTag();
        }

        Device device = deviceList.get(position);
        holder.textView.setText(device.getName());
        if(device.equals(Globals.getInstance().getSelectedDevice()))
        {
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        return convertView;
    }
}

