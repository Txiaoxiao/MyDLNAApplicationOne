package com.example.asus.mydlnaapplicationone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device,container,false);

        listView = (ListView)view.findViewById(R.id.listview_availableDeviceList);

        listView.setAdapter(listAdapter);

        return view;
    }

}
