package com.example.retailsale;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.dataoption.DataOption;
import com.example.retailsale.manager.dataoption.GetDataOptionListener;
import com.example.retailsale.manager.dataoption.GsonDataOption;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.manager.userlist.GetUsetListByGroupListener;
import com.example.retailsale.manager.userlist.GsonUserByGroup;
import com.example.retailsale.util.Utility;

public class Login extends Activity implements OnClickListener
{
    private static final String TAG = "Login";

    private EditText inputUserId;
    private EditText inputUserPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        boolean hadLogin = Utility.hadLogin(Login.this);

        if (hadLogin) startManageFragment();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void findViews()
    {
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        inputUserId = (EditText) findViewById(R.id.login_input_user_id);
        inputUserPassword = (EditText) findViewById(R.id.login_input_user_pwd);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.login_btn:
            String id = inputUserId.getText().toString();
            String password = inputUserPassword.getText().toString();

            if (id.equals(""))
            {
                id = "A123456";
                password = "1qaz@wsx";
            }
            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            login(id, password);
//            startManageFragment();
            break;
        }
    }

    private void startManageFragment()
    {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
//		Login.this.finish();
    }

    private void login(final String id, final String password)
    {
        HttpManager httpManager = new HttpManager();
        httpManager.login(Login.this, id, password, new GetLoginListener()
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
                            Log.d(TAG, " userSerial : " + userSerial + " userGroup : " + userGroup + " loginKey : "
                                    + loginKey + " message : " + message);
                            if (message.equals(Login.this.getResources().getString(R.string.login_successful)))
                            {
                                Log.d(TAG, "Message is successfully");
                                Utility.saveData(Login.this, id, password, userSerial, userGroup, loginKey);
                                getDataOption();
                            }
                            else
                            {
                                Log.d(TAG, "Message is failed");
                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                            }
                        }
                        else
                        {
                            Log.d(TAG, "value is null or size less/equal than 0");
                            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        }
                    }
                    else
                    {
                        Log.d(TAG, "userInfo is null");
                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    }
                }
                else
                {
                    Log.d(TAG, "Login failed");
                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                }
            }
        });
    }

    private void getDataOption()
    {
        HttpManager httpManager = new HttpManager();
        httpManager.getDataOptions(Login.this, new GetDataOptionListener()
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
                            RetialSaleDbAdapter retialSaleDbAdapter = new RetialSaleDbAdapter(Login.this);
                            retialSaleDbAdapter.open();
                            retialSaleDbAdapter.deleteAllOption();
                            for (int i = 0; i < value.size(); i++)
                            {
                                int optSerial = value.get(i).getOptSerial();
                                String optName = value.get(i).getOptName();
                                String typeName = value.get(i).getTypeName();
                                boolean optLock = value.get(i).getOptLock();

                                Log.d(TAG, "optSerial : " + optSerial + " optName : " + optName + " typeName : "
                                        + typeName + " optLock : " + optLock);
                                retialSaleDbAdapter.create(-1, typeName, optSerial, optName);
                            }
                            retialSaleDbAdapter.close();
//                            startManageFragment();
                        }
                        else
                        {
                            Log.d(TAG, "value is null");
                        }
                        getUserList();
                    }
                    else
                    {
                        Log.d(TAG, "dataOption is null");
                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    }
                }
                else
                {
                    Log.d(TAG, "Get data option failed");
                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                }
//                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
            }
        });
    }
    
    private void getUserList()
    {
        HttpManager httpManager = new HttpManager();
        httpManager.getUserListByGroup(Login.this, new GetUsetListByGroupListener()
        {
            @Override
            public void onResult(Boolean isSuccess, GsonUserByGroup user)
            {
                if (isSuccess)
                {
                    if (user != null)
                    {
                        if (user.getValue() != null)
                        {
                            int size = user.getValue().size();
                            Log.d(TAG, "user list size : " + size);
                            
                            int userSerial, userGroup, userType;
                            String userName, userGroupNm, userTypeNm;
                            
                            RetialSaleDbAdapter retialSaleDbAdapter = new RetialSaleDbAdapter(Login.this);
                            retialSaleDbAdapter.open();
                            retialSaleDbAdapter.deleteAllUser();
                            
                            for (int i = 0; i < size; i++)
                            {
                                userSerial = user.getValue().get(i).getUserSerial();
                                userName = user.getValue().get(i).getUserName();
                                userGroup = user.getValue().get(i).getUserGroup();
                                userType = user.getValue().get(i).getUserType();
                                userGroupNm = user.getValue().get(i).getUserGroupNm();
                                userTypeNm = user.getValue().get(i).getUserTypeNm();
                                Log.d(TAG, "userSerial : " + userSerial + " userName : " + userName
                                        + " userGroup : " + userGroup + " userType : " + userType
                                        + " userGroupNm : " + userGroupNm + " userTypeNm : " + userTypeNm);
                                retialSaleDbAdapter.create(userSerial, userName, userGroup, userType, userGroupNm, userTypeNm);
                            }
                            retialSaleDbAdapter.close();
                            startManageFragment();
                        }
                        else
                        {
                            Log.d(TAG, "value is null or size less/equal than 0");
                        }
                    }
                    else
                    {
                        Log.d(TAG, "user list is null");
                    }
                }
                else
                {
                    Log.d(TAG, "Get user list failed");
                }
                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
            }
        }, Utility.getCreatorGroup(this));
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
                progressDialog = ProgressDialog.show(Login.this, Utility.SPACE_STRING,
                        getResources().getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            }
        }
    };
}
