package com.example.retailsale.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.MainActivity;
import com.example.retailsale.PhotoPlayer;
import com.example.retailsale.R;
import com.example.retailsale.manager.fileinfo.LocalFileInfo;
import com.example.retailsale.util.Utility;

public class BrowserFragment extends Fragment implements OnItemClickListener, OnClickListener
{
    private static final String TAG = "BrowserFragment";
    private static final String GET_NAME = "get_name";
    private static final String GET_PATH = "get_path";
    public static final String FILE_LIST = "file_list";
    public static final String FILE_POSITION = "file_position";
    private static final int LOAD_FILE_ERROR = 0;
    private static final int LOAD_FILE_COMPLETE = 1;
    private static final int ADD_VIEW = 2;
    private static final int SET_ADAPTER = 3;
    private static final int SD_NOT_EXIST = 4;
    private int albumNum = 0;
    private String currentParentPath;
    private int currentAlbumPosition;
    private PhotosAdapterView photosAdapterView;
    private List<LocalFileInfo> albumList;
    private List<LocalFileInfo> photoList;
    private ProgressDialog progressDialog;
    // views
    private MainActivity mainActivity;
    private LinearLayout albums;
    private GridView photoGrid;
    private Button backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate  ");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume  ");
        albumList = new ArrayList<LocalFileInfo>();
        photoList = new ArrayList<LocalFileInfo>();
        LoadFileThread loadFileThread = new LoadFileThread(Utility.FILE_PATH);
        loadFileThread.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        Log.d(TAG, "onPauses  ");

        removeAllAlbums();

        if (albumList != null)
        {
            albumList.clear();
            albumList = null;
        }

        if (photoList != null)
        {
            photoList.clear();
            photoList = null;
        }

        if (photosAdapterView != null)
        {
            photosAdapterView = null;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.d(TAG, "onDestroy  ");
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        Log.d(TAG, "onAttach  ");
        mainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.browser_tab, container, false);
        photoGrid = (GridView) view.findViewById(R.id.browser_tab_files_grid);
        photoGrid.setOnItemClickListener(this);
        albums = (LinearLayout) view.findViewById(R.id.albums);
        backBtn = (Button) view.findViewById(R.id.browser_back_btn);
        backBtn.setOnClickListener(this);
        removeAllAlbums();

        Log.d(TAG, "onCreateView  ");

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (photoList != null)
        {
            LocalFileInfo localFileInfo = photoList.get(position);
            if (localFileInfo != null && position < photoList.size())
            {
                int fileType = localFileInfo.getFileType();
                if (fileType == LocalFileInfo.SELECTED_FILE) // is file, need to send list(path) for PhotoPlayer
                {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
                        Intent intent = new Intent(getActivity(), PhotoPlayer.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(FILE_LIST, (ArrayList<? extends Parcelable>) photoList);
                        Log.d(TAG, "position === " + position);
                        bundle.putInt(FILE_POSITION, position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else
                    {
                        uiHandler.sendEmptyMessage(SD_NOT_EXIST);
                    }
                }
                else
                { // is directory
                    String currentParent = this.currentParentPath + albumList.get(currentAlbumPosition).getFileName()
                            + "/";
                    removeAllAlbums();

                    LoadFileThread loadFileThread = new LoadFileThread(currentParent);
                    loadFileThread.start();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated  ");
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.browser_back_btn)
        {
            // to do back action
            Log.d(TAG, "currentParentPath is " + currentParentPath);

            if (!currentParentPath.equals(Utility.FILE_PATH))
            {
                File file = new File(currentParentPath);
                Log.d(TAG, "parent is " + file.getParent() + "/");

                removeAllAlbums();

                LoadFileThread loadFileThread = new LoadFileThread(file.getParent() + "/");
                loadFileThread.start();
            }
            else
            {
                Log.d(TAG, "it is in retalesale folder, no need back! ");
                mainActivity.setManageTab();
            }
        }
    }

    private void handlePageRefresh(String currentParent)
    {
        // get albums from download path
        albumNum = 0;
        currentAlbumPosition = 0;
        this.currentParentPath = currentParent;
        Log.d(TAG, "current parent is " + this.currentParentPath);
        listFolder(new File(this.currentParentPath));
        // get content from first album path

        if (albumList.size() > 0)
        {
            String path = albumList.get(0).getFilePath();
            Log.d(TAG, "XXXXXXXXXXXXXXXXXXXXXXX " + path);
            listFilesInFolder(new File(path));
        }
        else
        {
            Log.d(TAG, "size is  " + albumList.size());
        }

        uiHandler.sendEmptyMessage(SET_ADAPTER);
    }

    private void listFolder(final File folder)
    {
        if (albumList != null)
        {
            albumList.clear();
        }
        if (folder.listFiles() != null)
        {
            for (final File fileEntry : folder.listFiles())
            {
                String name = fileEntry.getName();
                String path = fileEntry.getAbsolutePath();
                Log.d(TAG, "folder name: " + name + " path: " + path);

                Message msg = new Message();
                msg.what = ADD_VIEW;
                Bundle bundle = new Bundle();
                bundle.putString(GET_NAME, name);
                bundle.putString(GET_PATH, path);
                msg.setData(bundle);

                albumList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));

                uiHandler.sendMessage(msg);
            }
        }
        else
        {
            Log.d(TAG, "listFolder is null ");
        }
    }

    private void listFilesInFolder(final File folder)
    {
        if (photoList != null)
        {
            photoList.clear();
        }
        if (folder.listFiles() != null)
        {
            for (final File fileEntry : folder.listFiles())
            {
                String name = fileEntry.getName();
                String path = fileEntry.getAbsolutePath();
                Log.d(TAG, "file name: " + name + " path: " + path);
                if (fileEntry.isDirectory())
                {
                    photoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));
                }
                else
                {
                    photoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_FILE));
                }
            }
        }
        else
        {
            Log.d(TAG, "listFile is null ");
        }
    }

    private View addQuicklySelectedDummy(String name, String path, int type)
    {
        Log.d(TAG, "addQuicklySelectedDummy ");
        // Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
        int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
        int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // imageView.setImageBitmap(bm);
        imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.album));
        imageView.setTag(albumNum);
        albumNum++;
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "albumNum " + v.getTag());
                currentAlbumPosition = (Integer) v.getTag();
                if (photoList != null)
                {
                    photoList.clear();
                    if (photosAdapterView != null)
                    {
                        photosAdapterView.notifyDataSetChanged();
                        photosAdapterView = null;
                    }
                    listFilesInFolder(new File(albumList.get((Integer) v.getTag()).getFilePath()));
                    if (photoList.size() != 0)
                    {
                        photosAdapterView = new PhotosAdapterView(getActivity(), photoList,
                                PhotosAdapterView.BROWSER_TAB);
                        photoGrid.setAdapter(photosAdapterView);
                    }
                }
            }
        });
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setText(name);
        layout.addView(imageView);
        layout.addView(textView);
        Log.d(TAG, "addQuicklySelectedDummy end");
        return layout;
    }

    private void removeAllAlbums()
    {
        if (albums != null)
        {
            albums.removeAllViews();
        }
    }

    private class LoadFileThread extends Thread
    {
        String currentParentPath;

        public LoadFileThread(String currentParentPath)
        {
            this.currentParentPath = currentParentPath;
        }

        @Override
        public void run()
        {
            dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) handlePageRefresh(currentParentPath);
            else
                uiHandler.sendEmptyMessage(SD_NOT_EXIST);

            dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
    }

    private Handler dialogHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case Utility.SHOW_WAITING_DIALOG:
                Log.d(TAG, "show waiting dialog ");
                progressDialog = ProgressDialog.show(BrowserFragment.this.getActivity(), Utility.SPACE_STRING,
                        BrowserFragment.this.getResources().getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            }
        }
    };

    private Handler uiHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case LOAD_FILE_ERROR:
                break;
            case LOAD_FILE_COMPLETE:
                break;
            case ADD_VIEW:
                Log.d(TAG, "ADD_VIEW ");
                Bundle bundle = msg.getData();
                if (bundle != null)
                {
                    String name = bundle.getString(GET_NAME);
                    String path = bundle.getString(GET_PATH);
                    albums.addView(addQuicklySelectedDummy(name, path, LocalFileInfo.SELECTED_DIR));
                }
                break;
            case SET_ADAPTER:
                if (photoList.size() != 0)
                {
                    photosAdapterView = new PhotosAdapterView(getActivity(), photoList, PhotosAdapterView.BROWSER_TAB);
                    photoGrid.setAdapter(photosAdapterView);
                }
                break;
            case SD_NOT_EXIST:
                Toast.makeText(BrowserFragment.this.getActivity(),
                        BrowserFragment.this.getResources().getString(R.string.sd_not_exist), Toast.LENGTH_SHORT)
                        .show();
                break;
            }
        }
    };
}
