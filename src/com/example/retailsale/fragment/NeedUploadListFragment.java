package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.retailsale.MainFragmentActivity;
import com.example.retailsale.R;
import com.example.retailsale.adapter.NeedUploadAdapter;
import com.example.retailsale.adapter.RetialSaleDbAdapter;
import com.example.retailsale.manager.addcustomer.CustomerInfo;
import com.example.retailsale.util.Utility;

public class NeedUploadListFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "NotUploadListFragment";
    
    private RetialSaleDbAdapter retialSaleDbAdapter;
    private List<CustomerInfo> customerList;
    
    // Views
    private MainFragmentActivity mainActivity;
    private ProgressDialog progressDialog;
    private ListView uploadListView;
    private TextView noDataView;
    private NeedUploadAdapter commonAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
        
        UploadListThread uploadListThread = new UploadListThread();
        uploadListThread.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
        closeDatabase();
        clearList();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        mainActivity = (MainFragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_need_upload, container, false);
        
        findViews(view);
        
        showLayout(false);
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.need_upload_tab_add_btn:
            Log.d(TAG, "start add customer page");
            break;
        default:
            break;
        }
    }
    
    private void findViews(View view)
    {
        // init upload list view
        uploadListView = (ListView) view.findViewById(R.id.need_upload_tab_content_list);
        
        // init no data view
        noDataView = (TextView) view.findViewById(R.id.need_upload_tab_no_data);
        
        // init add button
        Button addBtn = (Button) view.findViewById(R.id.need_upload_tab_add_btn);
        addBtn.setOnClickListener(this);
        
        // for test
        List<CustomerInfo> testList = new ArrayList<CustomerInfo>();
        testList.add(new CustomerInfo("111", "Customer 1", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 2", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 3", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 4", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 5", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 6", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 7", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 8", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 9", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));
        testList.add(new CustomerInfo("111", "Customer 10", "0912345678", "033651469", "0212345678",
                10, 20, "aa@aa.aa", "2015/03/03", 30, "John", 40, 50, "Memo", "1986/02/02", 60, 70,
                "2015/03/03", "2015/03/03", "06:11", "xxx", "workPostcode", "reservationWorkAlias",
                "reservationContact", "contactPostcode", 70, 80, "reservationUpateTime",
                "reservationStatusComment", 90, 100, 110));

        commonAdapter = new NeedUploadAdapter(mainActivity, testList);
        
        uploadListView.setAdapter(commonAdapter);
    }
    
    private void openDatabase()
    {
        if (retialSaleDbAdapter == null)
        {
            retialSaleDbAdapter = new RetialSaleDbAdapter(NeedUploadListFragment.this.getActivity());
        }
        if (!retialSaleDbAdapter.isDbOpen())
        { // not open, then open it
            retialSaleDbAdapter.open();
        }
    }
    
    private void closeDatabase()
    {
        if (retialSaleDbAdapter == null)
        {
            retialSaleDbAdapter.close();
        }
    }
    
    private void getAllCustomerByCreator()
    {
        openDatabase();
        Cursor cursor = retialSaleDbAdapter.getCustomerNotUpload();
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String customerName = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME));
                    customerList.add(new CustomerInfo(
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_ID)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_MOBILE)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_HOME)),
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_COMPANY)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_SEX)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_TITLE)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_EMAIL)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_VISIT_DATE)),
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INFO)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INTRODUCER)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_JOB)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_AGE)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_MEMO)),
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_BIRTHDAY)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATOR)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATOR_GROUP)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATE_DATE)), 
                            cursor.getString(cursor
                                            .getColumnIndex(RetialSaleDbAdapter.KEY_RESERVATION_DATE)),
                            "reservationTime", 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_WORK)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_WROK_POSTCODE)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_WORK_ALIAS)),
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_CONTACT)), 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_CONTACT_POSTCODE)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_SPACE)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS)),
                            "reservationUpateTime", 
                            cursor.getString(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS_COMMENT)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_BUDGET)),
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_REPAIR_ITEM)), 
                            cursor.getInt(cursor
                                    .getColumnIndex(RetialSaleDbAdapter.KEY_AREA))));
                }
            }
            else
            {
                handler.sendEmptyMessage(Utility.NO_DATA);
            }

        }
        else
        {
            handler.sendEmptyMessage(Utility.NO_DATA);
        }
        closeDatabase();
    }
    
    private void clearList()
    {
        if (customerList != null)
        {
            customerList.clear();
            customerList = null;
        }
    }
    
    private void showLayout(boolean isNoData)
    {
        if (isNoData)
        {
            noDataView.setVisibility(View.VISIBLE);
            uploadListView.setVisibility(View.INVISIBLE);
        }
        else
        {
            noDataView.setVisibility(View.INVISIBLE);
            uploadListView.setVisibility(View.VISIBLE);
        }
    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case Utility.SHOW_WAITING_DIALOG:
                Log.d(TAG, "show waiting dialog ");
                progressDialog = ProgressDialog.show(NeedUploadListFragment.this.getActivity(),
                        Utility.SPACE_STRING, NeedUploadListFragment.this.getResources()
                                .getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            case Utility.NO_DATA:
                Log.d(TAG, "No data ");
                showLayout(true);
                break;
            }
        }
    };
    
    private class UploadListThread extends Thread
    {
        @Override
        public void run()
        {
            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            if (customerList == null)
                customerList = new ArrayList<CustomerInfo>();
            else customerList.clear();

            getAllCustomerByCreator();

            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
    }
}
