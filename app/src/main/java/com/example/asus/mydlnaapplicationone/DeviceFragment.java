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
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.mydlnaapplicationone.Adapters.DeviceListViewAdapter;

import java.util.List;

;import GlobalVariables.Globals;

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


        /*deviceList = new ArrayList<>();

        try {
            searchRemoteDevices();
        } catch (IOException e) {

            //模拟device数据
            for(int i = 0;i <=6;i++)
            {
                deviceList.add(new Device("Device "+i));
            }
        }


        listAdapter = new DeviceListViewAdapter(getActivity(),deviceList);
        listView.setAdapter(listAdapter);
*/
        listAdapter = new DeviceListViewAdapter(getActivity(),deviceList);

        new DeviceSearch().getDevicesByThread(getActivity(),listAdapter,listView,deviceList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Globals.getInstance().setSelectedDevice(deviceList.get(position));;
                listAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}
