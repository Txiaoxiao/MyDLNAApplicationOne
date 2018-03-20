package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    ListView listViewImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_image,container,false);

        listViewImage = view.findViewById(R.id.listview_content_image);
/*        List<ImageItem> imageList = new ArrayList<>();
        ImageListViewAdapter adapter = new ImageListViewAdapter(getActivity(),imageList);*/




        return view;
    }

    private class ImageListViewAdapter extends BaseAdapter {
        Activity context;
        List<ImageItem> imageList;

        public ImageListViewAdapter(Activity context,List<ImageItem> imageList){
            this.context = context;
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            return convertView;
        }
    }


}
