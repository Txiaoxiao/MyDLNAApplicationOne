package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ContentFragment extends Fragment {

    ListView listViewContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_content,container,false);

        listViewContent = view.findViewById(R.id.listview_content);

        List<Folder> folderList = new ArrayList<>();
        //Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),R.id.imageview_content_item_folder);
        //Bitmap arrow = BitmapFactory.decodeResource(getActivity().getResources(),R.id.imageview_content_item_arrow);
        folderList.add(new Folder("Images"));
        folderList.add(new Folder("Videos"));
        folderList.add(new Folder("Audios"));

        FolderListViewAdapter adapter = new FolderListViewAdapter(getActivity(),folderList);
        listViewContent.setAdapter(adapter);

        listViewContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击item跳转到对应的文件列表fragment。

            }
        });

        return view;
    }

   public class FolderListViewAdapter extends BaseAdapter{

        private Activity context;
        private List<Folder> folderList;
        private Bitmap arrow;
        private  Bitmap folderImage;

        public FolderListViewAdapter(Activity context, List<Folder> folderList)
        {
            super();
            this.context = context;
            this.folderList = folderList;

            //arrow = BitmapFactory.decodeResource( context.getBaseContext().getResources(),R.drawable.ic_contentitem_folder);
            //folderImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_arrow);
            arrow = Utils.getBitmap(context,R.drawable.ic_arrow);
            folderImage = Utils.getBitmap(context,R.drawable.ic_contentitem_folder);
        }

       @Override
       public int getCount() {
           return folderList.size();
       }

       @Override
       public Object getItem(int position) {
           return folderList.get(position);
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
            FolderHolder holder;
            if(convertView == null)
            {
                holder = new FolderHolder();
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.content_item,null);
                holder.folder = convertView.findViewById(R.id.imageview_content_item_folder);
                holder.folderName = convertView.findViewById(R.id.textview_content_item_foldername);
                holder.arrow = convertView.findViewById(R.id.imageview_content_item_arrow);
                convertView.setTag(holder);
            }else {
                holder = (FolderHolder) convertView.getTag();
            }

            Folder folder = folderList.get(position);
            holder.folder.setImageBitmap(folderImage);
            holder.arrow.setImageBitmap(arrow);
            holder.folderName.setText(folder.getName());

           return convertView;
       }
   }

   public final class  FolderHolder{
        ImageView folder;
        TextView folderName;
        ImageView arrow;
   }

}
