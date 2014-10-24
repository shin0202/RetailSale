package com.example.retailsale.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.MainActivity;
import com.example.retailsale.R;
import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.fileinfo.GetFileInfoListener;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;
import com.example.retailsale.manager.fileinfo.GsonFileInfo.FileInfo;
import com.example.retailsale.manager.fileinfo.LocalFileInfo;
import com.example.retailsale.manager.folderinfo.GetFolderInfoListener;
import com.example.retailsale.manager.folderinfo.GsonFolderInfo;
import com.example.retailsale.manager.folderinfo.LocalFolderInfo;
import com.example.retailsale.util.Utility;

public class SynchronizationFragment extends Fragment implements OnClickListener, OnItemClickListener {
    private static final String TAG = "SynchronizationFragment";
    private LinearLayout uploadConsumer, downloadPicture, syncData;
    private TextView showTitle, showDescription, showMessage;
    private GridView filesGrid;
    private View dividerView;
    private ProgressDialog progressDialog;
    
    private List<LocalFolderInfo> localFolderInfoList;
    private List<LocalFileInfo> localFileInfoList;
    private int selectedItem;
    private PhotosAdapterView photosAdapterView;
    private String currentFolderName;
    
    private class SelectedItem {
        public static final int UPLOAD_CUSTOMER = 0;
        public static final int DOWNLOAD_PICTURE = 1;
        public static final int SYNC_DATA = 2;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        if (localFolderInfoList != null) {
            localFolderInfoList.clear();
            localFolderInfoList = null;
        }
        
        if (localFileInfoList != null) {
            localFileInfoList.clear();
            localFileInfoList = null;
        }
        
        if (photosAdapterView != null) {
            photosAdapterView = null;
        }
    }
    
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		MainActivity mainActivity = (MainActivity) activity;
	}
        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.synchronization_tab, container, false);
        
        uploadConsumer = (LinearLayout) view.findViewById(R.id.sync_tab_upload_layout);
        downloadPicture = (LinearLayout) view.findViewById(R.id.sync_tab_download_layout);
        syncData = (LinearLayout) view.findViewById(R.id.sync_tab_sync_layout);
        
        showTitle = (TextView) view.findViewById(R.id.sync_tab_item_title);
        showDescription = (TextView) view.findViewById(R.id.sync_tab_item_description);
        showMessage = (TextView) view.findViewById(R.id.sync_tab_item_message);
        
        filesGrid = (GridView) view.findViewById(R.id.sync_tab_files_grid);
        filesGrid.setOnItemClickListener(this);
        
        Button startBtn = (Button) view.findViewById(R.id.sync_tab_start_btn);
        
        dividerView = (View) view.findViewById(R.id.sync_tab_divider_line);
        
        uploadConsumer.setOnClickListener(this);
        downloadPicture.setOnClickListener(this);
        syncData.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        
        focusUploadConsumer();
        
        return view;
    }
    
    private void focusUploadConsumer() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_consumer));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_description));
        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_message));
        
        selectedItem = SelectedItem.UPLOAD_CUSTOMER;
        
        filesGrid.setVisibility(View.GONE);
        dividerView.setVisibility(View.GONE);
    }
    
    private void focusDownloadPicture() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_picture));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_description));
        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_message));
        
        selectedItem = SelectedItem.DOWNLOAD_PICTURE;
        
        filesGrid.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
    }
    
    private void focusSyncData() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_data));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_description));
        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_message));
        
        selectedItem = SelectedItem.SYNC_DATA;
        
        filesGrid.setVisibility(View.GONE);
        dividerView.setVisibility(View.GONE);
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sync_tab_upload_layout:
            focusUploadConsumer();
            break;
        case R.id.sync_tab_download_layout:
            focusDownloadPicture();
//            getFolderListFromServerTest();
            getFolderListFromServer();
            break;
        case R.id.sync_tab_sync_layout:
            focusSyncData();
            break;
        case R.id.sync_tab_start_btn:
            handleEvent();
            break;
        }
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (position < localFileInfoList.size())
		{
			currentFolderName = localFileInfoList.get(position).getFileName();
			Toast.makeText(
					SynchronizationFragment.this.getActivity(),
					SynchronizationFragment.this.getResources().getString(R.string.select)
							+ currentFolderName, Toast.LENGTH_SHORT).show();
		}
		else
		{	
			showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.incorrect));
		}
	}
    
    private void handleEvent() {
        switch (selectedItem) {
        case SelectedItem.UPLOAD_CUSTOMER:
            break;
        case SelectedItem.DOWNLOAD_PICTURE:
            if (!selectFolder(currentFolderName))
            	showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_no_file));
            break;
        case SelectedItem.SYNC_DATA:
            break;
        }
    }
    
    private void getFolderListFromServer() {
        HttpManager httpManager = new HttpManager();
        httpManager.getFolderInfo(SynchronizationFragment.this.getActivity(), new GetFolderInfoListener() {
            @Override
            public void onResult(Boolean isSuccess, GsonFolderInfo folderInfo) {
                if (isSuccess) {
                    if (folderInfo != null) {
                        String value = folderInfo.getValue();
                        Log.d(TAG, "value is " + value);
                        if (value != null) {
                            handleFolder(value);
                        } else {
                            Log.d(TAG, "value is null");
                        }
                    } else {
                        Log.d(TAG, "folderInfo is null");
                    }
                } else {
                    Log.d(TAG, "Get folder info failed");
                }
            }
        });
    }
    
    private void getFolderListFromServerTest() {
        handleFolder(
                "[{\"Id\":1,\"path\":\"C:\\\\Project\\\\_code\\\\testFolder\"," +
                "\"file\":[{\"folderId\":1,\"fileId\":1,\"fileName\":\"127121.jpg\"}]}," +
        		"{\"Id\":2,\"path\":\"C:\\\\Project\\\\_code\\\\testFolder\\\\123\"," +
        		"\"file\":[{\"folderId\":2,\"fileId\":2,\"fileName\":\"127121.jpg\"}]}," +
        		"{\"Id\":3,\"path\":\"C:\\\\Project\\\\_code\\\\testFolder\\\\456\"," +
        		"\"file\":[{\"folderId\":3,\"fileId\":3,\"fileName\":\"127121.jpg\"}]}," +
        		"{\"Id\":4,\"path\":\"C:\\\\Project\\\\_code\\\\testFolder\\\\789\"," +
        		"\"file\":[{\"folderId\":4,\"fileId\":4,\"fileName\":\"127121 - 複製.jpg\"}," +
        		"{\"folderId\":4,\"fileId\":5,\"fileName\":\"127121.jpg\"}]}]");
    }
    
    private void handleFolder(String json) {
        localFolderInfoList = new ArrayList<LocalFolderInfo>();
        try {
            JSONArray jsonArray = new JSONArray(json);

            Log.d(TAG, "jsonArray == " + jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String folderId = jsonObject.getString("Id");
                
                // handle folder path
                String folderPath = jsonObject.getString("path")
                        .replace(Utility.REPLACE_SERVER_FOLDER, Utility.FILE_PATH_2).replace("\\", "/");
                folderPath += "/";
                Log.d(TAG, "folderId : " + folderId + " folderPath : " + folderPath);
                
                // create the folder
                Utility.createFolder(folderPath);

                LocalFolderInfo localFolderInfo = new LocalFolderInfo(folderId, folderPath);

                JSONArray fileJsonArray = jsonObject.getJSONArray("file");

                for (int j = 0; j < fileJsonArray.length(); j++) {
                    JSONObject fileJsonObject = fileJsonArray.getJSONObject(j);
                    String fileFolderId = fileJsonObject.getString("folderId");
                    String fileId = fileJsonObject.getString("fileId");
                    String fileName = fileJsonObject.getString("fileName");
                    Log.d(TAG, "fileFolderId : " + fileFolderId + " fileId : " + fileId + " fileName : " + fileName);
                    localFolderInfo.addFileToList(fileFolderId, fileId, fileName);
                }
                localFolderInfoList.add(localFolderInfo);
            }
            
            // show file list and set adapter
            listFilesInFolder(new File(Utility.FILE_PATH));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private void listFilesInFolder(final File folder)
    {
        if (localFileInfoList != null) {
            localFileInfoList.clear();
        }
        
        localFileInfoList = new ArrayList<LocalFileInfo>();
        
        if (folder.listFiles() != null)
        {
            for (final File fileEntry : folder.listFiles())
            {
                String name = fileEntry.getName();
                String path = fileEntry.getAbsolutePath();
                Log.d(TAG, "file name: " + name + " path: " + path);
                if (fileEntry.isDirectory())
                {
                    localFileInfoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));
                }
            }
        }
        else
        {
            Log.d(TAG, "listFile is null ");
        }
        
        // to set adapter
        photosAdapterView = new PhotosAdapterView(SynchronizationFragment.this.getActivity(), localFileInfoList,
                PhotosAdapterView.SYNC_TAB);
        filesGrid.setAdapter(photosAdapterView);
    }
    
    private boolean selectFolder(String folderName) {
    	boolean hadFile = false;
        for (int i = 0; i < localFolderInfoList.size(); i++) {
            LocalFolderInfo localFolderInfo = localFolderInfoList.get(i);
            String path = localFolderInfo.getFolderPath();

            Log.d(TAG, "path : " + path);

            if (path.contains(folderName)) {
                Log.d(TAG, "it contains " + folderName);

                for (int j = 0; j < localFolderInfo.getFileListSize(); j++) {
                    String fileFolderId = localFolderInfo.getFileFolderId(j);
                    String fileId = localFolderInfo.getFileId(j);
                    Log.d(TAG, "fileFolderId : " + fileFolderId + " fileId : " + fileId);
                    hadFile = true;
                    getFile(fileFolderId, fileId);
                }
            } else {
                Log.d(TAG, "no contain " + folderName);
            }
        }
        return hadFile;
    }
    
    private void getFile(String folderId, String fileId) {
        HttpManager httpManager = new HttpManager();
        httpManager.getFileInfo(SynchronizationFragment.this.getActivity(), Integer.valueOf(folderId),
                Integer.valueOf(fileId), new GetFileInfoListener() {
                    @Override
                    public void onResult(Boolean isSuccess, GsonFileInfo fileInfo) {
                        if (isSuccess) {
                            if (fileInfo != null) {
                                List<FileInfo> value = fileInfo.getValue();
                                if (value != null) {
                                    for (int i = 0; i < value.size(); i++) {
                                        String path = value.get(i).getPath();
                                        String fileName = value.get(i).getFileName();
                                        String fileStream = value.get(i).getFileStream();

                                        Log.d(TAG, "path : " + path + " fileName : " + fileName + " fileStream : "
                                                + fileStream);
                                        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_download_success)
                                        		+ fileName);
                                    }
                                } else {
                                    Log.d(TAG, "value is null");
                                }
                            } else {
                                Log.d(TAG, "fileInfo is null");
                            }
                        } else {
                            Log.d(TAG, "Get file info failed");
                        }
                    }
                }, showMessageHandler);
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
				progressDialog = ProgressDialog
						.show(SynchronizationFragment.this.getActivity(),
								"",
								SynchronizationFragment.this.getResources().getString(
										R.string.loading));
				break;
			case Utility.DISMISS_WAITING_DIALOG:
				Log.d(TAG, "dismiss dialog ");
				if (progressDialog != null) progressDialog.dismiss();
				break;
			}
		}
	};
	
	private Handler showMessageHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
            showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_save_success));
		}
	};
}
