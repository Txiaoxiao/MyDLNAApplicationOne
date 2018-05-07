package com.example.asus.mydlnaapplicationone;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;
import com.example.asus.mydlnaapplicationone.HttpServer.MultiThreadServer;
import com.example.asus.mydlnaapplicationone.MediaServer.VideoServer;
import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContentFragment extends Fragment {

    int httpServerPort = 8080;
    ListView listViewContent;
    Button buttonReturn;
    List<ContentItem> imageItemList;//图片集
    List<ContentItem> videoItemList;
    List<ContentItem> audioItemList;
    List<ContentItem> folderList;   //文件夹集
    List<ContentItem> list;//listview当前显示的item集
    int contentTypeTag = 0;//0表示当前listview中显示的为folders,1表示当前显示的为具体的媒体文件。
    int mediaTypeTag = 0;//0 for Image,1 for video,2 for audio
    Bitmap imageIcon;
    String path;
    int width =0;
    int height=0;


    @Override
    public void onResume() {
        super.onResume();
        contentTypeTag = 0;
        Log.i("test", "onResume, contentTypeTag:" + contentTypeTag);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_content, container, false);

        listViewContent = view.findViewById(R.id.listview_content);
        buttonReturn = view.findViewById(R.id.button_return);
        buttonReturn.setVisibility(View.GONE);

        initFolderList();
        imageIcon = Utils.getBitmap(getActivity(), R.drawable.ic_imageicon);

        list = new ArrayList<>();
        for (ContentItem contentItem : folderList
                ) {
            list.add(contentItem);
        }
        final ContentListViewAdapter adapter = new ContentListViewAdapter(getActivity(), list);
        listViewContent.setAdapter(adapter);

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentTypeTag == 1) {
                    contentTypeTag = 0;

                    list.clear();
                    for (ContentItem contentItem : folderList
                            ) {
                        list.add(contentItem);
                    }
                    buttonReturn.setVisibility(View.GONE);

                    adapter.notifyDataSetChanged();
                }
            }
        });
        listViewContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //使用同一个list，通过更新list数据实现该功能------------>
                if (contentTypeTag == 1) {
                    //点击媒体文件时的操作
                    View customDialogSendFile = inflater.inflate(R.layout.dialog_sendfile, container, false);
                    ImageView imageViewCustomDialog = customDialogSendFile.findViewById(R.id.imageView_filetosend);
                    ContentItem contentItem = null;

                    switch (mediaTypeTag) {
                        //Image
                        case 0:
                            contentItem = imageItemList.get(position);
                            Log.i("path", Uri.parse(imageItemList.get(position).getPath()).toString());
                            //获取原图，失败
                            //imageViewCustomDialog.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(imageItemList.get(position).getPath())));
                            imageViewCustomDialog.setImageBitmap(contentItem.getThumbnail());


                            break;
                        //video
                        case 1:
                            contentItem = videoItemList.get(position);
                            imageViewCustomDialog.setImageBitmap(contentItem.getThumbnail());
                            break;

                        //audio
                        case 2:
                            contentItem = audioItemList.get(position);
                            imageViewCustomDialog.setImageBitmap(imageIcon);
                            break;

                        default:
                            break;
                    }

                    path = contentItem.getPath();
                    width = contentItem.getWidth();
                    height = contentItem.getHeight();
                    final ContentType contentType = contentItem.getType();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("   ");
                    builder.setView(customDialogSendFile);
                    builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //发送file的url到远程设备Send
                            //开一个线程发送url，并监听客户端的获取请求。serverSocket？

                            if(SsdpConstants.selectedDevice==null)
                            {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Attention:")
                                        .setMessage("please select a remote device first!")
                                        .setPositiveButton("OK", null)
                                        .show();
                               return;
                            }

                            if(width == 0 || height ==0)
                            {
                                width = 1000;
                                height = 1000;
                            }

                            VideoServer mediaserver = Globals.getInstance().getMediaServer();

                            if (mediaserver != null) {
                                mediaserver.closeAllConnections();
                                mediaserver.stop();
                            }

                            mediaserver = new VideoServer(path, width, height, httpServerPort,contentType);
                            Globals.getInstance().setMediaServer(mediaserver);

                            try {
                                mediaserver.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Error")
                                        .setMessage("IOException was thrown when mediaserver start.")
                                        .setPositiveButton("Close", null)
                                        .show();
                            }

                            responseToPC();//通知pc发送了媒体文件

                            // new multiThreadServerAsyncTask().execute();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing.
                        }
                    });

                    builder.create().show();


                } else {  //点击文件夹时的操作
                    contentTypeTag = 1;
                    buttonReturn.setVisibility(View.VISIBLE);
                    switch (position) {
                        case 0:
                            mediaTypeTag = 0;
                            list.clear();
                            String[] imageProjection = {MediaStore.Images.Media._ID,
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    MediaStore.Images.Media.DATA,
                                    MediaStore.Images.Media.DEFAULT_SORT_ORDER,
                                    MediaStore.Images.Media.WIDTH,
                                    MediaStore.Images.Media.HEIGHT};
                            String imageOrderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER;
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            imageItemList = getContentProvider(uri, imageProjection, imageOrderBy, ContentType.Image);
                            for (ContentItem item : imageItemList
                                    ) {
                                list.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        case 1:
                            mediaTypeTag = 1;
                            list.clear();
                            String[] videoProjection = {MediaStore.Video.Media._ID,
                                    MediaStore.Video.Media.DISPLAY_NAME,
                                    MediaStore.Video.Media.DATA,
                                    MediaStore.Video.Media.DEFAULT_SORT_ORDER,
                                    MediaStore.Video.Media.WIDTH,
                                    MediaStore.Video.Media.HEIGHT};
                            String videoOrderBy = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
                            Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            videoItemList = getContentProvider(videoUri, videoProjection, videoOrderBy, ContentType.Video);
                            for (ContentItem item : videoItemList
                                    ) {
                                list.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        case 2:
                            mediaTypeTag = 2;
                            list.clear();
                            String[] audioProjection = {MediaStore.Audio.Media._ID,
                                    MediaStore.Audio.Media.DISPLAY_NAME,
                                    MediaStore.Audio.Media.DATA,
                                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER};
                            String audioOrderBy = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
                            Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            audioItemList = getContentProvider(audioUri, audioProjection, audioOrderBy, ContentType.Audio);
                            for (ContentItem item : audioItemList
                                    ) {
                                list.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            break;
                        default:
                    }
                }
            }
        });

        return view;
    }

    private void responseToPC() {
        new ResponseToPCAsyncTask().execute();
    }

    private void initFolderList() {
        folderList = new ArrayList<>();
        folderList.add(new ContentItem("Images", ContentType.Folder));
        folderList.add(new ContentItem("Videos", ContentType.Folder));
        folderList.add(new ContentItem("Audios", ContentType.Folder));
    }


    public class ContentListViewAdapter extends BaseAdapter {

        private Activity context;
        private List<ContentItem> contentItemList;
        private Bitmap arrowIcon;
        private Bitmap folderIcon;
        private Bitmap imageIcon;

        public ContentListViewAdapter(Activity context, List<ContentItem> contentItemList) {
            super();
            this.context = context;
            this.contentItemList = contentItemList;

            imageIcon = Utils.getBitmap(context, R.drawable.ic_imageicon);
            arrowIcon = Utils.getBitmap(context, R.drawable.ic_arrow);
            folderIcon = Utils.getBitmap(context, R.drawable.ic_contentitem_folder);
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
            if (convertView == null) {
                holder = new ContentItemHolder();
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.content_item, null);
                holder.icon = convertView.findViewById(R.id.imageview_content_item_folder);
                holder.contentName = convertView.findViewById(R.id.textview_content_item_foldername);
                holder.arrowIcon = convertView.findViewById(R.id.imageview_content_item_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ContentItemHolder) convertView.getTag();
            }

            ContentItem contentItem = contentItemList.get(position);

            if (!contentItem.getType().equals(ContentType.Folder)) {
                holder.icon.setImageBitmap(imageIcon);
                holder.contentName.setText(contentItem.getName());

                Long id = contentItem.getId();
                holder.icon.setTag(id);

                if (contentItem.getType().equals(ContentType.Image)) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    new ImageLoader().getThumbnailByThread(contentResolver, holder.icon, id, contentItem);
                } else if (contentItem.getType().equals(ContentType.Video)) {
                    String path = contentItem.getPath();
                    new ImageLoader().getVideoThumbnail(path, holder.icon, id, contentItem);
                }

                holder.arrowIcon.setVisibility(View.GONE);

            } else {
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

    /**
     * 获取ContentProvider
     *
     * @param projection:要查询的字段
     * @param orderBy：排序方式
     */
    public List<ContentItem> getContentProvider(Uri uri, String[] projection, String orderBy, ContentType contentType) {

        List<ContentItem> listContentItem = new ArrayList<>();

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, orderBy);

        ContentItem contentItem;
        if (null == cursor) {
            return null;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();  // imageProjection = {MediaStore.Images.Media._ID,
            // MediaStore.Images.Media.DISPLAY_NAME,
            // MediaStore.Images.Media.DATA,
            // MediaStore.Images.Media.DEFAULT_SORT_ORDER
            // MediaStore.Images.Media.WIDTH,
            // MediaStore.Images.Media.HEIGHT};
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
                Log.i("content_item", projection[i] + ":::::::" + cursor.getString(i) + "\n");
            }

            String displayName = map.get(projection[1]);
            contentItem = new ContentItem(displayName.substring(displayName.lastIndexOf("/") + 1, displayName.length()), Long.parseLong(map.get(projection[0])), null, contentType, map.get(projection[2]));

            if (contentType.equals(ContentType.Audio)) {
                listContentItem.add(contentItem);

            } else {
                Log.i("display_name", displayName);
                Log.i("display_name_substring", displayName.substring(displayName.lastIndexOf("/") + 1, displayName.length()));
                String width = map.get(projection[4]);
                String height = map.get(projection[5]);
                if (width != null && height != null) {
                    contentItem.setWidth(Integer.parseInt(width));
                    contentItem.setHeight(Integer.parseInt(height));
                }
                listContentItem.add(contentItem);
            }
        }
        cursor.close();
        return listContentItem;
    }

    private class ResponseToPCAsyncTask extends AsyncTask{

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            new AlertDialog.Builder(getActivity())
                    .setMessage("Visit Address: \n"+Utils.pathToUrl(getActivity(),path,httpServerPort))
                    .setPositiveButton("OK ",null)
                    .show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            InetAddress address = null;
            DatagramSocket socket =null;

            try {
                address = SsdpConstants.selectedDevice.getDeviceInetAddress();
                int port = 10003;
                byte data[] = Utils.pathToUrl(getActivity(),path,httpServerPort).getBytes("utf-8");
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket = new DatagramSocket();
                socket.send(packet);

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

    private class multiThreadServerAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            MultiThreadServer server = new MultiThreadServer(httpServerPort);

            try {
                server.run();

            } catch (IOException e) {
                Log.e("error", "IOException was thrown when multithreadserver run.");
                System.out.println("IOException was thrown when multithreadserver run.");
            }

            return objects;
        }
    }
}
