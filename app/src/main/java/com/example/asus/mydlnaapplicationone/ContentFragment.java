package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;


public class ContentFragment extends Fragment {

    ListView listViewContent;
    List<ContentItem> imageItemList;//图片集
    List<ContentItem> folderList;   //文件夹集
    List<ContentItem> list;//listview当前显示的item集


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_content,container,false);

        listViewContent = view.findViewById(R.id.listview_content);

        initFolderList();
        initImageItemList();

        list = new ArrayList<>();
        for (ContentItem contentItem:folderList
             ) {
            list.add(contentItem);
        }
        new ScannerAnsyTask().execute();
        final ContentListViewAdapter adapter = new ContentListViewAdapter(getActivity(),list);
        listViewContent.setAdapter(adapter);

        listViewContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击item跳转到对应的文件列表fragment。如何实现？？
                //使用同一个list，通过更新list实现该功能
                //可否通过新建adapter？？
                switch (position){
                    case 0:
                        list.clear();
                        for (ContentItem item:imageItemList
                             ) {
                            list.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    default:
                }
            }
        });

        return view;
    }

    private void initFolderList() {
        folderList = new ArrayList<>();
        folderList.add(new ContentItem("Images",ContentType.Folder));
        folderList.add(new ContentItem("Videos",ContentType.Folder));
        folderList.add(new ContentItem("Audios",ContentType.Folder));
    }


    private void initImageItemList() {
        imageItemList = new ArrayList<>();
        imageItemList.add(new ContentItem("new Images",ContentType.File));
        imageItemList.add(new ContentItem("Videos",ContentType.File));
        imageItemList.add(new ContentItem("Audios",ContentType.File));
    }

    public class ContentListViewAdapter extends BaseAdapter{

        private Activity context;
        private List<ContentItem> contentItemList;
        private Bitmap arrowIcon;
        private Bitmap folderIcon;
        private Bitmap imageIcon;

        public ContentListViewAdapter(Activity context, List<ContentItem> contentItemList)
        {
            super();
            this.context = context;
            this.contentItemList = contentItemList;

            imageIcon = Utils.getBitmap(context,R.drawable.ic_imageicon);
            arrowIcon = Utils.getBitmap(context,R.drawable.ic_arrow);
            folderIcon = Utils.getBitmap(context,R.drawable.ic_contentitem_folder);
        }

       @Override
       public int getCount() {
           return contentItemList.size();
       }

       @Override
       public Object getItem(int position) {
           return contentItemList.get(position);
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
            ContentItemHolder holder;
            if(convertView == null)
            {
                holder = new ContentItemHolder();
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.content_item,null);
                holder.icon = convertView.findViewById(R.id.imageview_content_item_folder);
                holder.contentName = convertView.findViewById(R.id.textview_content_item_foldername);
                holder.arrowIcon = convertView.findViewById(R.id.imageview_content_item_arrow);
                convertView.setTag(holder);
            }else {
                holder = (ContentItemHolder) convertView.getTag();
            }

            ContentItem contentItem = contentItemList.get(position);
            if(contentItem.getType().equals(ContentType.File))
            {
                holder.icon.setImageBitmap(imageIcon);
                //holder.arrowIcon.setImageBitmap(arrowIcon);
                holder.contentName.setText(contentItem.getName());


                //loader 缩略图到imageIcon : imageLoader.displayImage(imageUrl, localHolder.folder,xxxx

                holder.arrowIcon.setVisibility(View.GONE);

            }else {
                holder.icon.setImageBitmap(folderIcon);
                holder.arrowIcon.setImageBitmap(arrowIcon);
                holder.contentName.setText(contentItem.getName());
                holder.arrowIcon.setVisibility(View.VISIBLE);
            }


           return convertView;
       }
   }

   public final class ContentItemHolder {
        ImageView icon;
        TextView contentName;
        ImageView arrowIcon;
   }

    public class ScannerAnsyTask extends AsyncTask<Void,Integer,List<ImageItem>> {
        private List<ImageItem> videoInfos=new ArrayList<>();
        @Override
        protected List<ImageItem> doInBackground(Void... params) {
            File file =Environment.getExternalStorageDirectory();
            Log.i("tga","getExternalStorageDirectory:"+file.getName()+"  "+file.getPath());
            videoInfos=getVideoFile(videoInfos, file);
            videoInfos=filterVideo(videoInfos);
            Log.i("tga","最后的大小"+videoInfos.size());
            return videoInfos;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<ImageItem> videoInfos) {
            super.onPostExecute(videoInfos);


        }

        /**
         * 获取视频文件
         * @param list
         * @param file
         * @return
         */
        private List<ImageItem> getVideoFile(final List<ImageItem> list, File file) {

            file.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    String name = file.getName();

                    int i = name.indexOf('.');
                    if (i != -1) {
                        name = name.substring(i);
                        if (name.equalsIgnoreCase(".mp4")
                                || name.equalsIgnoreCase(".3gp")
                                || name.equalsIgnoreCase(".wmv")
                                || name.equalsIgnoreCase(".ts")
                                || name.equalsIgnoreCase(".rmvb")
                                || name.equalsIgnoreCase(".mov")
                                || name.equalsIgnoreCase(".m4v")
                                || name.equalsIgnoreCase(".avi")
                                || name.equalsIgnoreCase(".m3u8")
                                || name.equalsIgnoreCase(".3gpp")
                                || name.equalsIgnoreCase(".3gpp2")
                                || name.equalsIgnoreCase(".mkv")
                                || name.equalsIgnoreCase(".flv")
                                || name.equalsIgnoreCase(".divx")
                                || name.equalsIgnoreCase(".f4v")
                                || name.equalsIgnoreCase(".rm")
                                || name.equalsIgnoreCase(".asf")
                                || name.equalsIgnoreCase(".ram")
                                || name.equalsIgnoreCase(".mpg")
                                || name.equalsIgnoreCase(".v8")
                                || name.equalsIgnoreCase(".swf")
                                || name.equalsIgnoreCase(".m2v")
                                || name.equalsIgnoreCase(".asx")
                                || name.equalsIgnoreCase(".ra")
                                || name.equalsIgnoreCase(".ndivx")
                                || name.equalsIgnoreCase(".xvid")) {
                            ImageItem video = new ImageItem();
                            file.getUsableSpace();
                            video.setDisplayName(file.getName());
                            video.setPath(file.getAbsolutePath());
                            Log.i("tga","name"+video.getPath());
                            list.add(video);
                            return true;
                        }
                        //判断是不是目录
                    } else if (file.isDirectory()) {
                        getVideoFile(list, file);
                    }
                    return false;
                }
            });

            return list;
        }

        /**10M=10485760 b,小于10m的过滤掉
         * 过滤视频文件
         * @param videoInfos
         * @return
         */
        private List<ImageItem> filterVideo(List<ImageItem> videoInfos){
            List<ImageItem> newVideos=new ArrayList<ImageItem>();
            for(ImageItem videoInfo:videoInfos){
                File f=new File(videoInfo.getPath());
                if(f.exists()&&f.isFile()&&f.length()>10485760){
                    newVideos.add(videoInfo);
                    Log.i("TGA","文件大小"+f.length());
                }else {
                    Log.i("TGA","文件太小或者不存在");
                }
            }
            return newVideos;
        }
    }

}
