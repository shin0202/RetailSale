package com.example.retailsale;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.dataoption.GetDataOptionListener;
import com.example.retailsale.manager.dataoption.GsonDataOption;
import com.example.retailsale.manager.dataoption.GsonDataOption.DataOption;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.util.Utility;

public class Login extends Activity implements OnClickListener
{
    private static final String TAG = "Login";

    private EditText inputUserId;
    private EditText inputUserPassword;

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
            login(id, password);
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
                            }
                        }
                        else
                        {
                            Log.d(TAG, "value is null or size less/equal than 0");
                        }
                    }
                    else
                    {
                        Log.d(TAG, "userInfo is null");
                    }
                }
                else
                {
                    Log.d(TAG, "Login failed");
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
                            startManageFragment();
                        }
                        else
                        {
                            Log.d(TAG, "value is null");
                        }
                    }
                    else
                    {
                        Log.d(TAG, "dataOption is null");
                    }
                }
                else
                {
                    Log.d(TAG, "Get data option failed");
                }
            }
        });
    }
}
