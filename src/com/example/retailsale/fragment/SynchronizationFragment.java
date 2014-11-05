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
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.MainActivity;
import com.example.retailsale.R;
import com.example.retailsale.RetialSaleDbAdapter;
import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.dataoption.GetDataOptionListener;
import com.example.retailsale.manager.dataoption.GsonDataOption;
import com.example.retailsale.manager.dataoption.GsonDataOption.DataOption;
import com.example.retailsale.manager.fileinfo.GetFileInfoListener;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;
import com.example.retailsale.manager.fileinfo.GsonFileInfo.FileInfo;
import com.example.retailsale.manager.fileinfo.LocalFileInfo;
import com.example.retailsale.manager.folderinfo.GetFolderInfoListener;
import com.example.retailsale.manager.folderinfo.GsonFolderInfo;
import com.example.retailsale.manager.folderinfo.LocalFolderInfo;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.util.Utility;

public class SynchronizationFragment extends Fragment implements OnClickListener,
        OnItemClickListener
{
    private static final String TAG = "SynchronizationFragment";

    private LinearLayout uploadConsumer, downloadPicture, syncData;

    private TextView showTitle, showDescription, showMessage;

    private GridView filesGrid;

    private ListView contentListView;

    private View dividerView;

    private ProgressDialog progressDialog;

    private Button startBtn;

    private List<LocalFolderInfo> localFolderInfoList;

    private List<LocalFileInfo> localFileInfoList;

    private int selectedItem;

    private PhotosAdapterView photosAdapterView;

    private ContentListAdapter contentListAdapter;

    private String currentFolderName;

    private StringBuilder messageStringBuilder;

    private int currentCount = 0;

    private int needCount = 0;

    private RetialSaleDbAdapter retialSaleDbAdapter;

    private List<String> detailList;

    public static final class SelectedItem
    {
        public static final int UPLOAD_CUSTOMER = 0;

        public static final int DOWNLOAD_PICTURE = 1;

        public static final int SYNC_DATA = 2;

        public static final int SHOW_SYNC_LIST = 6;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {

        super.onResume();
        openDb();
    }

    @Override
    public void onPause()
    {

        super.onPause();

        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);

        clearList();

        closeDb();

        retialSaleDbAdapter = null;
    }

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

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
        contentListView = (ListView) view.findViewById(R.id.sync_tab_content_list);
        focusUploadConsumer();
        return view;
    }

    private void focusUploadConsumer()
    {

        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_focus));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_normal));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_normal));
        showTitle.setText(SynchronizationFragment.this.getResources().getString(
                R.string.sync_tab_upload_consumer));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(
                R.string.sync_tab_upload_description));
        selectedItem = SelectedItem.UPLOAD_CUSTOMER;
        filesGrid.setVisibility(View.GONE);
        contentListView.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
//        startBtn.setText(SynchronizationFragment.this.getResources().getString(
//                R.string.start_upload));

        clearList();

        UploadThread uploadThread = new UploadThread();
        uploadThread.start();
    }

    private void focusDownloadPicture()
    {

        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_normal));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_focus));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_normal));
        showTitle.setText(SynchronizationFragment.this.getResources().getString(
                R.string.sync_tab_download_picture));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(
                R.string.sync_tab_download_description));
        selectedItem = SelectedItem.DOWNLOAD_PICTURE;
        filesGrid.setVisibility(View.VISIBLE);
        contentListView.setVisibility(View.GONE);
        dividerView.setVisibility(View.VISIBLE);
//        startBtn.setText(SynchronizationFragment.this.getResources().getString(
//                R.string.start_download));

        clearList();
    }

    private void focusSyncData()
    {

        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_normal));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_normal));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(
                R.color.sync_item_focus));
        showTitle.setText(SynchronizationFragment.this.getResources().getString(
                R.string.sync_tab_sync_data));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(
                R.string.sync_tab_sync_description));
        selectedItem = SelectedItem.SYNC_DATA;
        filesGrid.setVisibility(View.GONE);
        contentListView.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
//        startBtn.setText(SynchronizationFragment.this.getResources().getString(R.string.start_sync));

        clearList();

        SyncThread syncThread = new SyncThread();
        syncThread.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
        case R.id.sync_tab_upload_layout:
            focusUploadConsumer();
            break;
        case R.id.sync_tab_download_layout:
            focusDownloadPicture();
            // getFolderListFromServer();
            // To avoid loginkey is expired, so need login before call any APIs.
            login(R.id.sync_tab_download_layout);
            break;
        case R.id.sync_tab_sync_layout:
            focusSyncData();
            break;
        case R.id.sync_tab_start_btn:
            // handleEvent();
            // To avoid loginkey is expired, so need login before call any APIs.
            login(R.id.sync_tab_start_btn);
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
            showMessage.setText(SynchronizationFragment.this.getResources().getString(
                    R.string.incorrect));
        }
    }

    private void handleEvent()
    {

        switch (selectedItem)
        {
        case SelectedItem.UPLOAD_CUSTOMER:
            addCustomer();
            break;
        case SelectedItem.DOWNLOAD_PICTURE:
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                if (!selectFolder(currentFolderName))
                    showMessage.setText(SynchronizationFragment.this.getResources().getString(
                            R.string.sync_tab_sync_no_file));
            }
            else
            {
                showMessage.setText(SynchronizationFragment.this.getResources().getString(
                        R.string.sd_not_exist));
            }
            break;
        case SelectedItem.SYNC_DATA:
            syncData();
            break;
        }
    }

    private void handleLoginError()
    {

        switch (selectedItem)
        {
        case SelectedItem.UPLOAD_CUSTOMER:
            showMessage(R.string.sync_tab_upload_error);
            break;
        case SelectedItem.DOWNLOAD_PICTURE:
            showMessage(R.string.sync_tab_download);
            break;
        case SelectedItem.SYNC_DATA:
            showMessage(R.string.sync_tab_sync_error);
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

    private void showMessage(int resID)
    {

        messageStringBuilder.append(SynchronizationFragment.this.getResources().getString(resID))
                .append(Utility.LINE_FEED);
        showMessage.setText(messageStringBuilder.toString());
    }

    private void openDb()
    {

        if (retialSaleDbAdapter == null)
            retialSaleDbAdapter = new RetialSaleDbAdapter(
                    SynchronizationFragment.this.getActivity());

        retialSaleDbAdapter.open();
    }

    private void closeDb()
    {

        if (retialSaleDbAdapter.isDbOpen())
        {
            retialSaleDbAdapter.close();
        }
    }

    private void clearList()
    {

        if (localFolderInfoList != null)
        {
            localFolderInfoList.clear();
            localFolderInfoList = null;
        }
        if (localFileInfoList != null)
        {
            localFileInfoList.clear();
            localFileInfoList = null;
        }
        if (photosAdapterView != null)
        {
            photosAdapterView = null;
        }
        if (contentListAdapter != null)
        {
            contentListAdapter = null;
        }
        if (detailList != null)
        {
            detailList.clear();
            detailList = null;
        }
    }

    /*
     * *************************************************Login Related*******************************************************
     */
    private void login(final int resId)
    {

        handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
        SharedPreferences settings = getActivity().getSharedPreferences(Utility.LoginField.DATA,
                Utility.DEFAULT_ZERO_VALUE);
        final String id = settings.getString(Utility.LoginField.ID, "");
        final String password = settings.getString(Utility.LoginField.PASSWORD, "");

        HttpManager httpManager = new HttpManager();
        httpManager.login(SynchronizationFragment.this.getActivity(), id, password,
                new GetLoginListener()
                {
                    @Override
                    public void onResult(Boolean isSuccess, GsonLoginInfo userInfo)
                    {

                        if (isSuccess)
                        {
                            if (userInfo != null)
                            {
                                if (userInfo.getValue() != null && userInfo.getValue().size() > 0)
                                {
                                    int userSerial = userInfo.getValue().get(0).getUserSerial();
                                    int userGroup = userInfo.getValue().get(0).getUserGroup();
                                    String loginKey = userInfo.getValue().get(0).getLoginKey();
                                    String message = userInfo.getValue().get(0).getMessage();
                                    Log.d(TAG, " userSerial : " + userSerial + " userGroup : "
                                            + userGroup + " loginKey : " + loginKey + " message : "
                                            + message);
                                    if (message.equals(SynchronizationFragment.this.getActivity()
                                            .getResources().getString(R.string.login_successful)))
                                    {
                                        Log.d(TAG, "Message is successfully");
                                        Utility.saveData(
                                                SynchronizationFragment.this.getActivity(), id,
                                                password, userSerial, userGroup, loginKey);
                                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);

                                        // get folder from server, not get file
                                        if (resId == R.id.sync_tab_download_layout)
                                        {
                                            getFolderListFromServer();
                                        }
                                        else
                                        // get options, upload customer,
                                        // download file
                                        {
                                            handleEvent();
                                        }
                                    }
                                    else
                                    {
                                        Log.d(TAG, "Message is failed");
                                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                                        handleLoginError();
                                    }
                                }
                                else
                                {
                                    Log.d(TAG, "value is null or size less/equal than 0");
                                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                                    handleLoginError();
                                }
                            }
                            else
                            {
                                Log.d(TAG, "userInfo is null");
                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                                handleLoginError();
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Login failed");
                            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                            handleLoginError();
                        }
                    }
                });
    }

    /*
     * *************************************************Login Related*******************************************************
     */

    /*
     * *************************************************Download Related*******************************************************
     */
    private void getFolderListFromServer()
    {

        handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
        HttpManager httpManager = new HttpManager();
        httpManager.getFolderInfo(SynchronizationFragment.this.getActivity(),
                new GetFolderInfoListener()
                {
                    @Override
                    public void onResult(Boolean isSuccess, GsonFolderInfo folderInfo)
                    {

                        if (isSuccess)
                        {
                            if (folderInfo != null)
                            {
                                String value = folderInfo.getValue();
                                Log.d(TAG, "value is " + value);
                                if (value != null)
                                {
                                    if (Environment.getExternalStorageState().equals(
                                            Environment.MEDIA_MOUNTED))
                                    {
                                        handleFolder(value);
                                    }
                                    else
                                    {
                                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                                        showMessage(Utility.SPACE_STRING, R.string.sd_not_exist);
                                    }
                                }
                                else
                                {
                                    Log.d(TAG, "value is null");
                                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                                    showMessage(Utility.SPACE_STRING,
                                            R.string.sync_tab_sync_get_server_directory_failed);
                                }
                            }
                            else
                            {
                                Log.d(TAG, "folderInfo is null");
                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                                showMessage(Utility.SPACE_STRING,
                                        R.string.sync_tab_sync_get_server_directory_failed);
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Get folder info failed");
                            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                            showMessage(Utility.SPACE_STRING,
                                    R.string.sync_tab_sync_get_server_directory_failed);
                        }
                    }
                });
    }

    private void handleFolder(String json)
    {

        if (localFolderInfoList == null)
            localFolderInfoList = new ArrayList<LocalFolderInfo>();
        else localFolderInfoList.clear();
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            Log.d(TAG, "jsonArray == " + jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String folderId = jsonObject.getString("Id");
                // handle folder path
                String folderPath = jsonObject.getString("path")
                        .replace(Utility.REPLACE_SERVER_FOLDER, Utility.FILE_PATH_2)
                        .replace("\\", "/");
                folderPath += "/";
                Log.d(TAG, "folderId : " + folderId + " folderPath : " + folderPath);
                // create the folder
                Utility.createFolder(folderPath);
                LocalFolderInfo localFolderInfo = new LocalFolderInfo(folderId, folderPath);
                JSONArray fileJsonArray = jsonObject.getJSONArray("file");
                for (int j = 0; j < fileJsonArray.length(); j++)
                {
                    JSONObject fileJsonObject = fileJsonArray.getJSONObject(j);
                    String fileFolderId = fileJsonObject.getString("folderId");
                    String fileId = fileJsonObject.getString("fileId");
                    String fileName = fileJsonObject.getString("fileName");
                    Log.d(TAG, "fileFolderId : " + fileFolderId + " fileId : " + fileId
                            + " fileName : " + fileName);
                    localFolderInfo.addFileToList(fileFolderId, fileId, fileName);
                }
                localFolderInfoList.add(localFolderInfo);
            }
            // show file list and set adapter
            listFilesInFolder(new File(Utility.FILE_PATH));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
    }

    private void listFilesInFolder(final File folder)
    {

        if (localFileInfoList == null)
            localFileInfoList = new ArrayList<LocalFileInfo>();
        else localFileInfoList.clear();

        if (photosAdapterView != null) photosAdapterView = null;

        if (folder.listFiles() != null)
        {
            for (final File fileEntry : folder.listFiles())
            {
                String name = fileEntry.getName();
                String path = fileEntry.getAbsolutePath();
                Log.d(TAG, "file name: " + name + " path: " + path);
                if (fileEntry.isDirectory())
                {
                    localFileInfoList
                            .add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));
                }
            }
        }
        else
        {
            Log.d(TAG, "listFile is null ");
        }
        // to set adapter
        photosAdapterView = new PhotosAdapterView(SynchronizationFragment.this.getActivity(),
                localFileInfoList, PhotosAdapterView.SYNC_TAB);
        filesGrid.setAdapter(photosAdapterView);
        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_get_server_directory_success);
    }

    private boolean selectFolder(String folderName)
    {

        if (folderName == null)
        {
            Toast.makeText(
                    SynchronizationFragment.this.getActivity(),
                    SynchronizationFragment.this.getResources().getString(
                            R.string.sync_tab_sync_no_select_any_folder), Toast.LENGTH_SHORT)
                    .show();
            return true;
        }
        handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
        boolean hadFile = false;
        needCount = 0;
        currentCount = 0;
        for (int i = 0; i < localFolderInfoList.size(); i++)
        {
            LocalFolderInfo localFolderInfo = localFolderInfoList.get(i);
            String path = localFolderInfo.getFolderPath();
            Log.d(TAG, "path : " + path);
            if (path.contains(folderName))
            {
                Log.d(TAG, "it contains " + folderName);
                for (int j = 0; j < localFolderInfo.getFileListSize(); j++)
                {
                    String fileFolderId = localFolderInfo.getFileFolderId(j);
                    String fileId = localFolderInfo.getFileId(j);
                    Log.d(TAG, "fileFolderId : " + fileFolderId + " fileId : " + fileId);
                    hadFile = true;
                    needCount++;
                    getFile(fileFolderId, fileId);
                }
            }
            else
            {
                Log.d(TAG, "no contain " + folderName);
            }
        }
        Log.d(TAG, "needCount : " + needCount);
        return hadFile;
    }

    private void getFile(String folderId, String fileId)
    {

        HttpManager httpManager = new HttpManager();
        httpManager.getFileInfo(SynchronizationFragment.this.getActivity(),
                Integer.valueOf(folderId), Integer.valueOf(fileId), new GetFileInfoListener()
                {
                    @Override
                    public void onResult(Boolean isSuccess, GsonFileInfo fileInfo)
                    {

                        currentCount++;
                        if (isSuccess)
                        {
                            if (fileInfo != null)
                            {
                                List<FileInfo> value = fileInfo.getValue();
                                if (value != null)
                                {
                                    for (int i = 0; i < value.size(); i++)
                                    {
                                        String path = value.get(i).getPath();
                                        String fileName = value.get(i).getFileName();
                                        String fileStream = value.get(i).getFileStream();
                                        // Log.d(TAG, "path : " + path +
                                        // " fileName : " + fileName +
                                        // " fileStream : "
                                        // + fileStream);
                                        showMessage(fileName,
                                                R.string.sync_tab_sync_download_success);
                                    }
                                }
                                else
                                {
                                    Log.d(TAG, "value is null");
                                    showMessage(Utility.SPACE_STRING,
                                            R.string.sync_tab_sync_download_failed);
                                }
                            }
                            else
                            {
                                Log.d(TAG, "fileInfo is null");
                                showMessage(Utility.SPACE_STRING,
                                        R.string.sync_tab_sync_download_failed);
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Get file info failed");
                            showMessage(Utility.SPACE_STRING,
                                    R.string.sync_tab_sync_download_failed);
                        }
                        if (currentCount == needCount)
                        {
                            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        }
                    }
                }, handler);
    }

    /*
     * *************************************************Download Related*******************************************************
     */

    /*
     * *************************************************Upload Related*******************************************************
     */
    private void addCustomer()
    {

        openDb();

        int creator = Utility.getCreator(SynchronizationFragment.this.getActivity());
        int creatorGroup = Utility.getCreatorGroup(SynchronizationFragment.this.getActivity());
        Log.d(TAG, "creator is " + creator);
        needCount = 0;
        currentCount = 0;
        handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
        Cursor cursor = retialSaleDbAdapter.getCustomerByCreatorNotUpload(creator);
        if (cursor != null)
        {
            needCount = cursor.getCount();
            if (needCount > 0)
            {
                while (cursor.moveToNext())
                {
                    long rowId = cursor.getLong(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_ID));
                    String customerAccount = Utility.DEFAULT_VALUE_STRING;
                    String custometName = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME));
                    String customerMobile = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_MOBILE));
                    String customerHome = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_HOME));
                    String customerCompany = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_COMPANY));
                    int customerSex = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_SEX));
                    int customerTitle = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_TITLE));
                    String customerMail = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_EMAIL));
                    String customerVisitDate = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_VISIT_DATE));
                    int customerInfo = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INFO));
                    String customerIntroducer = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INTRODUCER));
                    int customerJob = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_JOB));
                    int customerAge = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_AGE));
                    String customerBirth = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_BIRTHDAY));
                    String createDate = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATE_DATE));
                    /***************************************************************/
                    int reservationSerial = -1;
                    int customerSerial = -1;
                    String reservationDate = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_RESERVATION_DATE));
                    String reservationWork = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_WORK));
                    String reservationWorkAlias = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_WORK_ALIAS));
                    String reservationContact = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_CONTACT));
                    int reservationSpace = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_SPACE));
                    int reservationStatus = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS));
                    String reservationUpateTime = createDate; // not incorrect?
                    String reservationStatusComment = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS_COMMENT));
                    int reservationBudget = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_BUDGET));
                    String reservationDataSerial = Utility.DEFAULT_VALUE_STRING;
                    // didn't have the field "comment", "send note"
                    JSONStringer json = null, customerReservationJson = null;
                    try
                    {
                        customerReservationJson = new JSONStringer().object()
                                .key(Utility.AddCustomerJsonTag.RESERVATION_SERIAL)
                                .value(reservationSerial)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_SERIAL)
                                .value(customerSerial)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_DATE)
                                .value(reservationDate)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_WORK)
                                .value(reservationWork)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_WORK_ALIAS)
                                .value(reservationWorkAlias)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_CONTACT)
                                .value(reservationContact)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_SPACE)
                                .value(reservationSpace)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_STATUS)
                                .value(reservationStatus)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_UPDATE_TIME)
                                .value(reservationUpateTime)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_STATUS_COMMENT)
                                .value(reservationStatusComment)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_BUDGET)
                                .value(reservationBudget)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_DATA_SERIAL)
                                .value(reservationDataSerial)
                                .key(Utility.AddCustomerJsonTag.CREATOR).value(creator)
                                .key(Utility.AddCustomerJsonTag.CREATOR_GROUP).value(creatorGroup)
                                .key(Utility.AddCustomerJsonTag.CREATE_TIME).value(createDate)
                                .endObject();
                        // Log.d(TAG, "customerReservationJson === " +
                        // customerReservationJson.toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_db_error);
                        break;
                    }
                    try
                    {
                        json = new JSONStringer().object()
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_ACCOUNT)
                                .value(customerAccount)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_NAME).value(custometName)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_Mobile)
                                .value(customerMobile)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_HOME).value(customerHome)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_COMPANY)
                                .value(customerCompany)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_SEX).value(customerSex)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_TITLE)
                                .value(customerTitle).key(Utility.AddCustomerJsonTag.CUSTOMER_MAIL)
                                .value(customerMail)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_VISIT_DATE)
                                .value(customerVisitDate)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_INFO).value(customerInfo)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_INTRODUCER)
                                .value(customerIntroducer)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_JOB).value(customerJob)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_AGE).value(customerAge)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_BIRTH)
                                .value(customerBirth).key(Utility.AddCustomerJsonTag.CREATOR)
                                .value(creator).key(Utility.AddCustomerJsonTag.CREATOR_GROUP)
                                .value(creatorGroup).key(Utility.AddCustomerJsonTag.CREATE_TIME)
                                .value(createDate)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_RESERVATION)
                                .value(customerReservationJson).endObject();
                        // Log.d(TAG, "json === " + json.toString());
                        POSTThread postThread = new POSTThread(json, custometName, handler, rowId,
                                retialSaleDbAdapter);
                        postThread.start();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_db_error);
                        break;
                    }
                }
            }
            else
            {
                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_no_customer);
            }
            cursor.close();
        }
        else
        {
            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
            showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_no_customer);
        }
    }

    private void addCustomerInfo(JSONStringer json, String custometName, Handler handler, long rowId,
            RetialSaleDbAdapter retialSaleDbAdapter)
    {

        HttpManager httpManager = new HttpManager();
        httpManager.addCustomerInfo(getActivity(), custometName, handler, HttpManager.LogType.Operation,
                String.valueOf(Utility.getCreator(getActivity())), Utility.SPACE_STRING, HttpManager.USER_HOST,
                HttpManager.ACTION_NAME, Utility.getLoginKey(getActivity()), json, rowId, retialSaleDbAdapter);
    }

    private void getAllCustomerByCreator()
    {

        int creator = Utility.getCreator(SynchronizationFragment.this.getActivity());
        openDb();
        Cursor cursor = retialSaleDbAdapter.getCustomerByCreatorNotUpload(creator);
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String custometName = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME));
                    detailList.add(SynchronizationFragment.this.getResources().getString(
                            R.string.customer_name)
                            + custometName);
                }
            }
            else
            {
                detailList.add(SynchronizationFragment.this.getResources().getString(
                        R.string.no_data));
            }

        }
        else
        {
            detailList.add(SynchronizationFragment.this.getResources().getString(R.string.no_data));
        }
    }

    /*
     * *************************************************Upload Related*******************************************************
     */
    

    /*
     * *************************************************Sync Related*******************************************************
     */
    private void syncData()
    {

        // 1. clear all folder
        boolean isSuccess = Utility.removeDirectory(new File(Utility.FILE_PATH));
        if (isSuccess)
        {
            showMessage(Utility.FILE_PATH, R.string.sync_tab_sync_clear_folder_success);
        }
        else
        {
            showMessage(Utility.FILE_PATH, R.string.sync_tab_sync_clear_folder_failed);
        }
        // 2. clear data option in DB
        openDb();
        isSuccess = retialSaleDbAdapter.deleteAllOption();
        if (isSuccess)
        {
            showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_clear_data_option_success);
        }
        else
        {
            showMessage(Utility.SPACE_STRING, R.string.sync_tab_sync_clear_data_option_failed);
        }
        // 3. download data option from server
        getDataOption();
    }

    private void getDataOption()
    {

        HttpManager httpManager = new HttpManager();
        httpManager.getDataOptions(SynchronizationFragment.this.getActivity(),
                new GetDataOptionListener()
                {
                    @Override
                    public void onResult(Boolean isSuccess, GsonDataOption dataOption)
                    {

                        if (isSuccess)
                        {
                            if (dataOption != null)
                            {
                                List<DataOption> value = dataOption.getValue();
                                if (value != null)
                                {
                                    for (int i = 0; i < value.size(); i++)
                                    {
                                        int optSerial = value.get(i).getOptSerial();
                                        String optName = value.get(i).getOptName();
                                        String typeName = value.get(i).getTypeName();
                                        boolean optLock = value.get(i).getOptLock();
                                        Log.d(TAG, "optSerial : " + optSerial + " optName : "
                                                + optName + " typeName : " + typeName
                                                + " optLock : " + optLock);
                                        retialSaleDbAdapter
                                                .create(-1, typeName, optSerial, optName);
                                    }
                                    closeDb();
                                    showMessage(Utility.SPACE_STRING,
                                            R.string.sync_tab_sync_download_data_option_success);
                                }
                                else
                                {
                                    Log.d(TAG, "value is null");
                                    showMessage(Utility.SPACE_STRING,
                                            R.string.sync_tab_sync_download_data_option_failed);
                                }
                            }
                            else
                            {
                                Log.d(TAG, "dataOption is null");
                                showMessage(Utility.SPACE_STRING,
                                        R.string.sync_tab_sync_download_data_option_failed);
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Get data option failed");
                            showMessage(Utility.SPACE_STRING,
                                    R.string.sync_tab_sync_download_data_option_failed);
                        }
                    }
                });
    }

    private List<String> getAllFolderNameFromParent()
    {

        List<String> folderList = new ArrayList<String>();

        File folder = new File(Utility.FILE_PATH);
        if (folder.listFiles() != null)
        {
            for (final File fileEntry : folder.listFiles())
            {
                if (fileEntry.isDirectory())    // only add the folder
                {
                    folderList.add(SynchronizationFragment.this.getResources().getString(
                            R.string.folder)
                            + fileEntry.getName());
                }
            }
        }

        return folderList;
    }

    private List<String> getAllOptionNameFromDB()
    {

        List<String> optionList = new ArrayList<String>();

        openDb();

        Cursor cursor = retialSaleDbAdapter.getOptionGroupByOptionAlias();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String optionAlias = cursor.getString(cursor
                        .getColumnIndex(RetialSaleDbAdapter.KEY_DATA_OPTION_ALIAS));

                optionList.add(SynchronizationFragment.this.getResources().getString(
                        R.string.db_item)
                        + optionAlias);
            }
        }

        return optionList;
    }

    /*
     * *************************************************Sync Related*******************************************************
     */

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {
            case SelectedItem.UPLOAD_CUSTOMER:
                currentCount++;
                if (currentCount == needCount)
                {
                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    closeDb();
                }
                String customerName = (String) msg.obj;
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
                String fileName = (String) msg.obj;
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
            case Utility.SHOW_WAITING_DIALOG:
                Log.d(TAG, "show waiting dialog ");
                progressDialog = ProgressDialog.show(SynchronizationFragment.this.getActivity(),
                        Utility.SPACE_STRING, SynchronizationFragment.this.getResources()
                                .getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            case SelectedItem.SHOW_SYNC_LIST:
                if (detailList != null)
                {
                    for (int i = 0; i < detailList.size(); i++)
                    {
                        Log.d(TAG, "detailList item is " + detailList.get(i));
                    }
                }

                if (contentListAdapter != null) contentListAdapter = null;

                contentListAdapter = new ContentListAdapter(
                        SynchronizationFragment.this.getActivity(), detailList);
                contentListView.setAdapter(contentListAdapter);

                break;
            }
        }
    };

    private class POSTThread extends Thread
    {
        private JSONStringer json;

        private String custometName;

        private Handler handler;

        private long rowId;

        private RetialSaleDbAdapter retialSaleDbAdapter;

        public POSTThread(JSONStringer json, String custometName, Handler handler, long rowId,
                RetialSaleDbAdapter retialSaleDbAdapter)
        {

            this.json = json;
            this.custometName = custometName;
            this.handler = handler;
            this.rowId = rowId;
            this.retialSaleDbAdapter = retialSaleDbAdapter;
        }

        @Override
        public void run()
        {

            addCustomerInfo(json, custometName, handler, rowId, retialSaleDbAdapter);
        }
    }

    private class SyncThread extends Thread
    {
        @Override
        public void run()
        {

            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            if (detailList == null)
                detailList = new ArrayList<String>();
            else detailList.clear();

            // 1. list all folder from parent
            detailList.addAll(getAllFolderNameFromParent());

            // 2. list all option alias
            detailList.addAll(getAllOptionNameFromDB());

            handler.sendEmptyMessage(SelectedItem.SHOW_SYNC_LIST);

            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
    }

    private class UploadThread extends Thread
    {
        @Override
        public void run()
        {

            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            if (detailList == null)
                detailList = new ArrayList<String>();
            else detailList.clear();

            getAllCustomerByCreator();

            handler.sendEmptyMessage(SelectedItem.SHOW_SYNC_LIST);

            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
    }

    private class ContentListAdapter extends BaseAdapter
    {
        private List<String> contentList;

        // Views
        private LayoutInflater layoutInflater;

        private ViewTag viewTag;

        public ContentListAdapter(Context context, List<String> contentList)
        {

            this.contentList = contentList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount()
        {

            return contentList.size();
        }

        @Override
        public Object getItem(int position)
        {

            return contentList.get(position);
        }

        @Override
        public long getItemId(int position)
        {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.cell_of_sync_tab_content_list, null);
                viewTag = new ViewTag((TextView) convertView.findViewById(R.id.content_list_name));

                convertView.setTag(viewTag);
            }
            else
            {
                viewTag = (ViewTag) convertView.getTag();
            }
            convertView.setId(PhotosAdapterView.BASE_INDEX + position);
            if (position < contentList.size()) viewTag.showName.setText(contentList.get(position));
            return convertView;
        }

        class ViewTag
        {
            TextView showName;

            public ViewTag(TextView showName)
            {

                this.showName = showName;
            }
        }
    }
}
