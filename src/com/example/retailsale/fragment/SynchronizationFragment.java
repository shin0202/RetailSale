package com.example.retailsale.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
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
import com.example.retailsale.RetialSaleDbAdapter;
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
    private Button startBtn;
    
    private List<LocalFolderInfo> localFolderInfoList;
    private List<LocalFileInfo> localFileInfoList;
    private int selectedItem;
    private PhotosAdapterView photosAdapterView;
    private String currentFolderName;
    private StringBuilder messageStringBuilder;
    
    private int currentCount = 0;
    private int needCount = 0;
    
    public static final class SelectedItem {
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
        
        dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        
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
        
        startBtn = (Button) view.findViewById(R.id.sync_tab_start_btn);
        
        dividerView = (View) view.findViewById(R.id.sync_tab_divider_line);
        
        uploadConsumer.setOnClickListener(this);
        downloadPicture.setOnClickListener(this);
        syncData.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        
        messageStringBuilder = new StringBuilder();
        
        focusUploadConsumer();
        
        return view;
    }
    
    private void focusUploadConsumer() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_consumer));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_description));
        
        selectedItem = SelectedItem.UPLOAD_CUSTOMER;
        
        filesGrid.setVisibility(View.GONE);
        dividerView.setVisibility(View.GONE);
        
        startBtn.setText(SynchronizationFragment.this.getResources().getString(R.string.start_upload));
    }
    
    private void focusDownloadPicture() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_picture));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_description));
        
        selectedItem = SelectedItem.DOWNLOAD_PICTURE;
        
        filesGrid.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
        
        startBtn.setText(SynchronizationFragment.this.getResources().getString(R.string.start_download));
    }
    
    private void focusSyncData() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_data));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_description));
        
        selectedItem = SelectedItem.SYNC_DATA;
        
        filesGrid.setVisibility(View.GONE);
        dividerView.setVisibility(View.GONE);
        
        startBtn.setText(SynchronizationFragment.this.getResources().getString(R.string.start_sync));
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
            addCustomer();
            break;
        case SelectedItem.DOWNLOAD_PICTURE:
            if (!selectFolder(currentFolderName))
            	showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_no_file));
            break;
        case SelectedItem.SYNC_DATA:
            syncData();
            break;
        }
    }
    
	private void showMessage(String itemName, int resID)
	{
		messageStringBuilder.append(itemName)
				.append(SynchronizationFragment.this.getResources().getString(resID))
				.append(Utility.LINE_FEED);
		showMessage.setText(messageStringBuilder.toString());
	}
    
    /* *************************************************Download Related******************************************************* */
    private void getFolderListFromServer() {
        dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
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
                            dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                            showMessage("", R.string.sync_tab_sync_get_server_directory_failed);
                        }
                    } else {
                        Log.d(TAG, "folderInfo is null");
                        dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        showMessage("", R.string.sync_tab_sync_get_server_directory_failed);
                    }
                } else {
                    Log.d(TAG, "Get folder info failed");
                    dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    showMessage("", R.string.sync_tab_sync_get_server_directory_failed);
                }
            }
        });
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
            dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
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
        dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        showMessage("", R.string.sync_tab_sync_get_server_directory_success);
    }
    
    private boolean selectFolder(String folderName) {
        dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
    	boolean hadFile = false;
    	needCount = 0;
    	currentCount = 0;
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
                    needCount++;
                    getFile(fileFolderId, fileId);
                }
            } else {
                Log.d(TAG, "no contain " + folderName);
            }
        }
        
        Log.d(TAG, "needCount : " + needCount);
        return hadFile;
    }
    
    private void getFile(String folderId, String fileId) {
        HttpManager httpManager = new HttpManager();
        httpManager.getFileInfo(SynchronizationFragment.this.getActivity(), Integer.valueOf(folderId),
                Integer.valueOf(fileId), new GetFileInfoListener() {
                    @Override
                    public void onResult(Boolean isSuccess, GsonFileInfo fileInfo) {
                        currentCount++;
                        if (isSuccess) {
                            if (fileInfo != null) {
                                List<FileInfo> value = fileInfo.getValue();
                                if (value != null) {
                                    for (int i = 0; i < value.size(); i++) {
                                        String path = value.get(i).getPath();
                                        String fileName = value.get(i).getFileName();
                                        String fileStream = value.get(i).getFileStream();

//                                        Log.d(TAG, "path : " + path + " fileName : " + fileName + " fileStream : "
//                                                + fileStream);
                                        showMessage(fileName, R.string.sync_tab_sync_download_success);
                                    }
                                } else {
                                    Log.d(TAG, "value is null");
                                    showMessage("", R.string.sync_tab_sync_download_failed);
                                }
                            } else {
                                Log.d(TAG, "fileInfo is null");
                                showMessage("", R.string.sync_tab_sync_download_failed);
                            }
                        } else {
                            Log.d(TAG, "Get file info failed");
                            showMessage("", R.string.sync_tab_sync_download_failed);
                        }
                        
                        if (currentCount == needCount) {
                            dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        }
                    }
                }, showMessageHandler);
    }
    
    /* *************************************************Download Related******************************************************* */
    
    /* *************************************************Upload Related******************************************************* */
    private void addCustomer() {
        RetialSaleDbAdapter retialSaleDbAdapter = new RetialSaleDbAdapter(SynchronizationFragment.this.getActivity());
        retialSaleDbAdapter.open();
                
        int creator = Utility.getCreator(SynchronizationFragment.this.getActivity());
        int creatorGroup = Utility.getCreatorGroup(SynchronizationFragment.this.getActivity());
        
        Log.d(TAG, "creator is " + creator);
        
        needCount = 0;
        currentCount = 0;
        dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
        
        Cursor cursor = retialSaleDbAdapter.getCustomerByCreator(creator);
        
        if (cursor != null) {
            needCount = cursor.getCount();
            if (needCount > 0) {
                while (cursor.moveToNext()) {
                    String customerAccount = "-1";
                    String custometName =  cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME));
                    String customerMobile = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_MOBILE));
                    String customerHome = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_HOME));
                    String customerCompany = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_COMPANY));
                    int customerSex = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_SEX));
                    int customerTitle = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_TITLE));
                    String customerMail = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_EMAIL));
                    String customerVisitDate = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_VISIT_DATE));
                    int customerInfo = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INFO));
                    String customerIntroducer = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INTRODUCER));
                    int customerJob = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_JOB));
                    int customerAge = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_AGE));
                    String customerBirth = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_BIRTHDAY));
                    String createDate = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATE_DATE));
                    
                    
                    /***************************************************************/
                    
                    int reservationSerial = -1;
                    int customerSerial = -1;
                    String reservationDate = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_RESERVATION_DATE));
                    String reservationWork = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_WORK));
                    String reservationWorkAlias = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_WORK_ALIAS));
                    String reservationContact = cursor.getString(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_CONTACT));
                    int reservationSpace = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_SPACE));
                    int reservationStatus = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_STATUS));
                    String reservationUpateTime = createDate;       // not incorrect?
                    String reservationStatusComment = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS_COMMENT));
                    int reservationBudget = cursor.getInt(cursor.getColumnIndex(RetialSaleDbAdapter.KEY_BUDGET));
                    String reservationDataSerial = "-1";
                    
                    // didn't have the field "comment", "send note"
                    JSONStringer json = null, customerReservationJson = null;
                    try {
                        customerReservationJson = new JSONStringer().object()
                                .key(Utility.AddCustomerJsonTag.RESERVATION_SERIAL).value(reservationSerial)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_SERIAL).value(customerSerial)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_DATE).value(reservationDate)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_WORK).value(reservationWork)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_WORK_ALIAS).value(reservationWorkAlias)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_CONTACT).value(reservationContact)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_SPACE).value(reservationSpace)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_STATUS).value(reservationStatus)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_UPDATE_TIME).value(reservationUpateTime)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_STATUS_COMMENT).value(reservationStatusComment)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_BUDGET).value(reservationBudget)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_DATA_SERIAL).value(reservationDataSerial)
                                .key(Utility.AddCustomerJsonTag.CREATOR).value(creator)
                                .key(Utility.AddCustomerJsonTag.CREATOR_GROUP).value(creatorGroup)
                                .key(Utility.AddCustomerJsonTag.CREATE_TIME).value(createDate).endObject();
                        Log.d(TAG, "customerReservationJson === " + customerReservationJson.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        json = new JSONStringer().object()
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_ACCOUNT).value(customerAccount)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_NAME).value(custometName)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_Mobile).value(customerMobile)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_HOME).value(customerHome)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_COMPANY).value(customerCompany)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_SEX).value(customerSex)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_TITLE).value(customerTitle)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_MAIL).value(customerMail)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_VISIT_DATE).value(customerVisitDate)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_INFO).value(customerInfo)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_INTRODUCER).value(customerIntroducer)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_JOB).value(customerJob)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_AGE).value(customerAge)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_BIRTH).value(customerBirth)
                                .key(Utility.AddCustomerJsonTag.CREATOR).value(creator)
                                .key(Utility.AddCustomerJsonTag.CREATOR_GROUP).value(creatorGroup)
                                .key(Utility.AddCustomerJsonTag.CREATE_TIME).value(createDate)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_RESERVATION).value(customerReservationJson)
                                .endObject();
                        Log.d(TAG, "json === " + json.toString());
                        POSTThread postThread = new POSTThread(json, custometName, showMessageHandler);
                        postThread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    }
                }
            } else {
                dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
            }
            cursor.close();
        } else {
            dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
        
        retialSaleDbAdapter.close();
    }
    
	private void addCustomerInfo(JSONStringer json, String custometName, Handler handler)
	{
		HttpManager httpManager = new HttpManager();
		httpManager.addCustomerInfo(SynchronizationFragment.this.getActivity(), custometName,
				handler, HttpManager.LogType.Login, "095050", "", HttpManager.USER_HOST,
				HttpManager.ACTION_NAME, json);
	}
	
	/* *************************************************Upload Related******************************************************* */
    
    private void syncData() {
        
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
	
    private Handler showMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SelectedItem.UPLOAD_CUSTOMER:
            	currentCount++;
            	
            	if (currentCount == needCount) {
            		dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
            	}
            	
                String customerName = (String)msg.obj;
                int statusCode = msg.arg1;
                
                if (statusCode == HttpStatus.SC_CREATED)
                {
                    showMessage(customerName, R.string.sync_tab_sync_upload_success);
                }
                else
                {
                    showMessage(customerName, R.string.sync_tab_sync_upload_failed);
                }
                break;
            case SelectedItem.DOWNLOAD_PICTURE:
                String fileName = (String)msg.obj;
                int status = msg.arg1;
                
                if (status == Utility.SUCCESS) 
                {
                	showMessage(fileName, R.string.sync_tab_sync_save_success);
                }
                else
                {
                	showMessage(fileName, R.string.sync_tab_sync_save_failed);
                }
                
                break;
            case SelectedItem.SYNC_DATA:
                break;
            }
        }
    };
    
    private class POSTThread extends Thread {
        private JSONStringer json;
        private String custometName;
        private Handler handler;
        
        public POSTThread(JSONStringer json, String custometName, Handler handler) {
            this.json = json;
            this.custometName = custometName;
            this.handler = handler;
        }
        
        @Override
        public void run() {
            addCustomerInfo(json, custometName, handler);
        }
    }
}
