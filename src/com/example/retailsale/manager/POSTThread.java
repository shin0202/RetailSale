package com.example.retailsale.manager;

import org.json.JSONStringer;

import android.content.Context;
import android.os.Handler;

import com.example.retailsale.RetialSaleDbAdapter;
import com.example.retailsale.fragment.SynchronizationFragment;
import com.example.retailsale.util.Utility;

public class POSTThread extends Thread
{
    private JSONStringer json;

    private String custometName;

    private Handler handler;

    private long rowId;

    private RetialSaleDbAdapter retialSaleDbAdapter;
    
    private Context context;

    public POSTThread(Context context, JSONStringer json, String custometName, Handler handler, long rowId,
            RetialSaleDbAdapter retialSaleDbAdapter)
    {
        this.context = context;
        this.json = json;
        this.custometName = custometName;
        this.handler = handler;
        this.rowId = rowId;
        this.retialSaleDbAdapter = retialSaleDbAdapter;
    }

    @Override
    public void run()
    {
        if (Utility.isInternetAvailable(context))
        {
            addCustomerInfo(json, custometName, handler, rowId, retialSaleDbAdapter);
        }
        else
        {
            if (handler != null)
            {
                handler.sendEmptyMessage(SynchronizationFragment.SHOW_NO_NETWORK_TOAST);
            }
        }
    }
    
    private void addCustomerInfo(JSONStringer json, String custometName, Handler handler, long rowId,
            RetialSaleDbAdapter retialSaleDbAdapter)
    {

        HttpManager httpManager = new HttpManager();
        httpManager.addCustomerInfo(context, custometName, handler, HttpManager.LogType.OPERATION,
                String.valueOf(Utility.getCreator(context)), Utility.SPACE_STRING, HttpManager.USER_HOST,
                HttpManager.ACTION_NAME, Utility.getLoginKey(context), json, rowId, retialSaleDbAdapter);
    }
}
