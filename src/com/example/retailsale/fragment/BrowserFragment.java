package com.example.retailsale.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.MainFragmentActivity;
import com.example.retailsale.PhotoPlayerActivity;
import com.example.retailsale.R;
import com.example.retailsale.adapter.PhotoAdapter;
import com.example.retailsale.manager.fileinfo.LocalFileInfo;
import com.example.retailsale.util.Utility;

public class BrowserFragment extends Fragment implements OnItemClickListener, OnClickListener, OnItemLongClickListener
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
    private static final int REFRESH_ADAPTER = 5;
    private int albumNum = 0, currentAlbumPosition, currentLongClickPosition = 0;
    private String currentParentPath;
    private boolean isDeletedState = false;
    private PhotoAdapter photosAdapterView;
    private List<LocalFileInfo> albumList, photoList; // albumList is in the bottom, photoList is in the top
    private Stack<Integer> positionStack;
    // views
    private MainFragmentActivity mainActivity;
    private LinearLayout albums;
    private GridView photoGrid;
    private ProgressDialog progressDialog;
    private Button deleteBtn;
    
    // load thread ----> handlePageRefresh -----> listFolder(uiHandler ---> addQuicklySelectedDummy), listFilesInFolder
    // onItemClick ----> isFile ----->  PhotoPlayer
    //             ----> isDir  ----->  load thread ----> ...
    // back btn    ----> is parent ----> mainActivity.setManageTab()
    //             ----> not parent -----> load thread ----> ...

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate  ");
        
        currentAlbumPosition = 0;

        positionStack = new Stack<Integer>();
        currentParentPath = Utility.FILE_PATH;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume  ");
        isDeletedState = false;
        currentLongClickPosition = 0;
        albumList = new ArrayList<LocalFileInfo>();
        photoList = new ArrayList<LocalFileInfo>();
        LoadFileThread loadFileThread = new LoadFileThread(currentParentPath);
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
        mainActivity = (MainFragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        photoGrid = (GridView) view.findViewById(R.id.browser_tab_files_grid);
        photoGrid.setOnItemClickListener(this);
        photoGrid.setOnItemLongClickListener(this);
        albums = (LinearLayout) view.findViewById(R.id.albums);
        Button backBtn = (Button) view.findViewById(R.id.browser_back_btn);
        backBtn.setOnClickListener(this);
        deleteBtn = (Button) view.findViewById(R.id.browser_delete_btn);
        deleteBtn.setOnClickListener(this);
        removeAllAlbums();

        Log.d(TAG, "onCreateView  ");

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        if (!isDeletedState)
        {
            if (photoList != null)
            {
                LocalFileInfo localFileInfo = photoList.get(position);

                showToast(getResources().getString(R.string.select) + localFileInfo.getFileName());

                if (localFileInfo != null && position < photoList.size())
                {
                    int fileType = localFileInfo.getFileType();
                    
                    if (fileType == LocalFileInfo.SELECTED_FILE) // is file, need to send list(path) for PhotoPlayer
                    {
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                        {
                            Intent intent = new Intent(getActivity(), PhotoPlayerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(FILE_LIST,
                                    (ArrayList<? extends Parcelable>) photoList);
                            Log.d(TAG, "position === " + position);
                            Log.d(TAG, "currentAlbumPosition === " + currentAlbumPosition);
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
                        if (currentAlbumPosition < albumList.size())
                        {
                            currentParentPath = currentParentPath
                                    + albumList.get(currentAlbumPosition).getFileName() + "/";
                            removeAllAlbums();

                            positionStack.push(currentAlbumPosition); // push current position to stack

                            currentAlbumPosition = position;

                            LoadFileThread loadFileThread = new LoadFileThread(currentParentPath);
                            loadFileThread.start();
                        }
                    }
                }
            }
        }
        else
        {
            Log.d(TAG, "OnItemClickListener, but in deleted action. ");
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
            Log.d(TAG, "back action currentParentPath is " + currentParentPath);

            if (!currentParentPath.equals(Utility.FILE_PATH))
            {
                File file = new File(currentParentPath);
                
                currentParentPath = file.getParent() + "/";
                
                Log.d(TAG, "parent is " + currentParentPath);

                removeAllAlbums();
                
                currentAlbumPosition = positionStack.pop(); // pop last position from stack

                LoadFileThread loadFileThread = new LoadFileThread(currentParentPath);
                loadFileThread.start();
            }
            else
            {
                Log.d(TAG, "it is in retalesale folder, no need back! ");
                mainActivity.setManageTab();
            }
        }
        else //doing delete action
        {
            Log.d(TAG, "onClick get delete action, isDeletedState : " + isDeletedState + " currentLongClickPosition : " + currentLongClickPosition);
            
            if (currentLongClickPosition < photoList.size())
            {
                Log.d(TAG, "get folder from photoList by position " + photoList.get(currentLongClickPosition).toString());
                
                if (isDeletedState)
                {
                    File file = new File(photoList.get(currentLongClickPosition).getFilePath());
                    
                    dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
                    
                    if (photoList.get(currentLongClickPosition).getFileType() == LocalFileInfo.SELECTED_DIR) // is dir
                    {
                        Utility.removeDirectory(file);
                    }
                    else // is File
                    {
                        Utility.removeFile(file);
                    }
                    
                    dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    
                    changeDeletedState();
                    
                    removeAllAlbums();

                    LoadFileThread loadFileThread = new LoadFileThread(currentParentPath);
                    loadFileThread.start();
                }
                else
                {
                    Log.d(TAG, "onClick get delete action, delete state incorrect! ");
                }
            }
            else
            {
                Log.d(TAG, "onClick get delete action, it get error! ");
            }
        }
    }
    
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.d(TAG, "onItemLongClick position : " + position);
        
        currentLongClickPosition = position;
        
        changeDeletedState();
        
        return false;
    }

    private void handlePageRefresh(String currentParent)
    {
        // get albums from download path
        Log.d(TAG, "handlePageRefresh albumNum : " + albumNum + " currentAlbumPosition : "
                + currentAlbumPosition + " currentParent : " + currentParent
                + " currentParentPath : " + currentParentPath);
        albumNum = 0;

        Log.d(TAG, "current parent is " + currentParent);


        listFolder(new File(currentParent));
                
        // get content from first album path

        if (albumList.size() > 0)
        {
            String path = albumList.get(currentAlbumPosition).getFilePath();
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
                if (fileEntry.isDirectory())    // only add the folder
                {
                    String name = fileEntry.getName();
                    String path = fileEntry.getAbsolutePath();
                    Log.d(TAG, "folder name: " + name + " path: " + path);

                    if (!name.equals(Utility.THUMB_PATH_FOR_SHOW))
                    {
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
        
        uiHandler.sendEmptyMessage(REFRESH_ADAPTER);
        
        List<LocalFileInfo> folderList = new ArrayList<LocalFileInfo>();
        List<LocalFileInfo> fileList = new ArrayList<LocalFileInfo>();
        boolean hadFile = false;
        boolean hadFolder = false;
        
        if (folder.listFiles() != null)
        {
            for (final File fileEntry : folder.listFiles())
            {
                String name = fileEntry.getName();
                String path = fileEntry.getAbsolutePath();
                Log.d(TAG, "file name: " + name + " path: " + path);
                if (!name.equals(Utility.THUMB_PATH_FOR_SHOW))
                {
                    if (fileEntry.isDirectory())
                    {
                        folderList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));
                        hadFolder = true;
                    }
                    else
                    {
                        fileList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_FILE));
                        hadFile = true;
                    }
                }
            }
            
            if (hadFolder && hadFile) // Both folder and file are exist, then clear file
            {
                photoList.addAll(folderList);
            } 
            else if (!hadFolder && hadFile)
            {
                photoList.addAll(fileList);
            }
            else if (hadFolder && !hadFile)
            {
                photoList.addAll(folderList);
            }
            else
            {
                Log.d(TAG, "no file ");
            }
            
            // clear content
            if (folderList != null)
            {
                folderList.clear();
                folderList = null;
            }
            
            if (fileList != null)
            {
                fileList.clear();
                fileList = null;
            }
        }
        else
        {
            Log.d(TAG, "listFile is null ");
        }
    }

    private View addQuicklySelectedDummy(String name, String path, int type)
    {
        // Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
        int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
        int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);
        int txtSize = (int) getResources().getDimension(R.dimen.scrollview_txt_size);
        
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // imageView.setImageBitmap(bm);
        imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.file1));
        imageView.setTag(albumNum);
        
        albumNum++;
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentAlbumPosition = (Integer) v.getTag();
                LocalFileInfo localFileInfo = albumList.get(currentAlbumPosition);
                
                Log.d(TAG, " currentAlbumPosition : " + currentAlbumPosition + " file path : " + localFileInfo.getFilePath());
                
                showToast(getResources().getString(R.string.select) + localFileInfo.getFileName());
                
                if (photoList != null)
                {
                    listFilesInFolder(new File(localFileInfo.getFilePath()));

                    uiHandler.sendEmptyMessage(SET_ADAPTER);
                }
            }
        });
        
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(txtSize);
        textView.setTextColor(Color.BLACK);
        textView.setText(name);
        
        layout.addView(imageView);
        layout.addView(textView);
        
        return layout;
    }
    
    private void removeAllAlbums()
    {
        if (albums != null)
        {
            albums.removeAllViews();
        }
    }
    
    private void showToast(String showString)
    {
        Toast.makeText(getActivity(), showString, Toast.LENGTH_SHORT).show();
    }
    
    private void changeDeletedState()
    {
        if (isDeletedState)
        {
            isDeletedState = false;
            showToast(getResources().getString(R.string.browser_tab_close_deleted_state));
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        else
        {
            isDeletedState = true;
            showToast(getResources().getString(R.string.browser_tab_in_deleted_state));
            deleteBtn.setVisibility(View.VISIBLE);
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
                progressDialog = ProgressDialog.show(getActivity(), Utility.SPACE_STRING,
                        getResources().getString(R.string.loading));
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
                    photosAdapterView = new PhotoAdapter(getActivity(), photoList, PhotoAdapter.BROWSER_TAB);
                    photoGrid.setAdapter(photosAdapterView);
                }
                break;
            case SD_NOT_EXIST:
                showToast(getResources().getString(R.string.sd_not_exist));
                break;
            case REFRESH_ADAPTER:
                if (photosAdapterView != null)
                {
                    photosAdapterView.notifyDataSetChanged();
                    photosAdapterView = null;
                }
                break;
            }
        }
    };
}
