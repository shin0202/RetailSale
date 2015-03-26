package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.retailsale.MainFragmentActivity;
import com.example.retailsale.R;
import com.example.retailsale.adapter.RetialSaleDbAdapter;
import com.example.retailsale.util.Utility;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NeedUploadListFragment extends Fragment
{
    private static final String TAG = "NotUploadListFragment";
    
    private RetialSaleDbAdapter retialSaleDbAdapter;
    private List<String> detailList;
    
    // Views
    private MainFragmentActivity mainActivity;
    private ProgressDialog progressDialog;
    
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
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
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
                    String custometName = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME));
                    detailList.add(custometName);
                }
            }
            else
            {
                detailList.add(NeedUploadListFragment.this.getResources().getString(
                        R.string.no_data));
            }

        }
        else
        {
            detailList.add(NeedUploadListFragment.this.getResources().getString(R.string.no_data));
        }
        closeDatabase();
    }
    
    private void clearList()
    {
        if (detailList != null)
        {
            detailList.clear();
            detailList = null;
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
            }
        }
    };
    
    private class UploadListThread extends Thread
    {
        @Override
        public void run()
        {
            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            if (detailList == null)
                detailList = new ArrayList<String>();
            else detailList.clear();

            getAllCustomerByCreator();

            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        }
    }
}
