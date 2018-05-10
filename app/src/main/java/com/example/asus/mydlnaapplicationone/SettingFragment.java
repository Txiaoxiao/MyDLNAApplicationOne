package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_setting, container, false);

         listview = view.findViewById(R.id.listview_setting);
         textviewItemTitle=view.findViewById(R.id.textview_setting_title);
         textViewItemDetails=view.findViewById(R.id.textview_setting_detail);

         initData();

         SettingAdapter adapter = new SettingAdapter(getActivity(),listSettingData);
         listview.setAdapter(adapter);

         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 if(listSettingData.get(position).getTitle().equals("Network Setting"))
                 {
                     Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                     startActivity(intent);
                 }
             }
         });

        return view;
    }

    private void initData() {
        listSettingData = new ArrayList<>();
        SettingData datafWifiManager = new SettingData("Network Setting","manager wireless network.");
        listSettingData.add(datafWifiManager);
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
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            SettingData data = settingDataList.get(position);
            holder.settingDataTitle.setText(data.getTitle());
            holder.settingDataDescription.setText(data.getDescription());

            return convertView;
        }

        private class ViewHolder {
            TextView settingDataTitle;
            TextView settingDataDescription;
        }
    }
}
