package com.example.retailsale;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.retailsale.manager.fileinfo.GetFileInfoListener;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;
import com.example.retailsale.manager.fileinfo.GsonFileInfo.FileInfo;
import com.example.retailsale.manager.folderinfo.GetFolderInfoListener;
import com.example.retailsale.manager.folderinfo.GsonFolderInfo;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.util.Utility;

public class Login extends Activity implements OnClickListener
{
	private static final String TAG = "Login";
	
	private EditText  inputUserId;
	private EditText inputUserPassword;
	
	private SharedPreferences settings;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_login);
    	
    	findViews();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	readData();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    private void findViews() {
    	Button loginBtn = (Button) findViewById(R.id.login_btn);
    	loginBtn.setOnClickListener(this);
    	
    	inputUserId = (EditText) findViewById(R.id.login_input_user_id);
    	inputUserPassword = (EditText) findViewById(R.id.login_input_user_pwd);
    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.login_btn:
            String id = inputUserId.getText().toString();
            String password = inputUserPassword.getText().toString();

            if (id.equals("")) {
                id = "A123456";
                password = "1qaz@wsx";
            }
			login(id, password);
			break;
		}
	}
	
	private void startWelcomeActivity() {
		Intent intent = new Intent(Login.this, WelcomeActivity.class);
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
						    String userSerial = userInfo.getValue().get(0).getUserSerial();
							String userGroup = userInfo.getValue().get(0).getUserGroup();
							String loginKey = userInfo.getValue().get(0).getLoginKey();
							String message = userInfo.getValue().get(0).getMessage();
							Log.d(TAG, " userSerial : " + userSerial + " userGroup : " + userGroup + " loginKey : " + loginKey + " message : " + message);
							if (message.equals(Login.this.getResources().getString(R.string.login_successful))) {
								Log.d(TAG, "Message is successfully");
								saveData(id, password, userSerial, userGroup, loginKey);
								startWelcomeActivity();
							} else {
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
	
	private void saveData(String id, String password, String userSerial, String userGroup, String loginKey)
	{
		settings = getSharedPreferences(Utility.LoginField.DATA, 0);
		settings.edit()
		        .putString(Utility.LoginField.ID, id)
				.putString(Utility.LoginField.PASSWORD, password)
				.putString(Utility.LoginField.USER_SERIAL, userSerial)
				.putString(Utility.LoginField.USER_GROUP, userGroup)
				.putString(Utility.LoginField.LOGIN_KEY, loginKey)
				.commit();
	}
	
	private void readData() {
        settings = getSharedPreferences(Utility.LoginField.DATA, 0);
//        inputUserId.setText(settings.getString(Utility.LoginField.ID, ""));
//        inputUserPassword.setText(settings.getString(Utility.LoginField.PASSWORD, ""));
//        
//        Log.d(TAG,
//                "User group : " + settings.getString(Utility.LoginField.USER_GROUP, "") + " Login key : "
//                        + settings.getString(Utility.LoginField.LOGIN_KEY, ""));
        String id = settings.getString(Utility.LoginField.ID, "");
        
        if (id != null && !id.equals("")) {
            startWelcomeActivity();
        }
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
							for (int i = 0; i < value.size(); i++)
							{
								int optSerial = value.get(i).getOptSerial();
								String optName = value.get(i).getOptName();
								String typeName = value.get(i).getTypeName();
								boolean optLock = value.get(i).getOptLock();
								
								Log.d(TAG, "optSerial : " + optSerial + " optName : " + " typeName : " + typeName + " optLock : " + optLock);
							}
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
	
	private void getFile() {
		HttpManager httpManager = new HttpManager();
		httpManager.getFileInfo(Login.this, 2, 2, new GetFileInfoListener()
		{
			@Override
			public void onResult(Boolean isSuccess, GsonFileInfo fileInfo)
			{
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
								
								Log.d(TAG, "path : " + path + " fileName : " + fileName + " fileStream : " + fileStream);
							}
						}
						else
						{
							Log.d(TAG, "value is null");
						}
					}
					else
					{
						Log.d(TAG, "fileInfo is null");
					}
				}
				else
				{
					Log.d(TAG, "Get file info failed");
				}
			}
		});
	}
	
	private void getFolder() {
		HttpManager httpManager = new HttpManager();
		httpManager.getFolderInfo(Login.this, new GetFolderInfoListener()
		{
			@Override
			public void onResult(Boolean isSuccess, GsonFolderInfo folderInfo)
			{
				if (isSuccess)
				{
					if (folderInfo != null)
					{
						Log.d(TAG, "value is " + folderInfo.getValue());
//						List<FileInfo> value = fileInfo.getValue();
//						if (value != null)
//						{
//							for (int i = 0; i < value.size(); i++)
//							{
//								String path = value.get(i).getPath();
//								String fileName = value.get(i).getFileName();
//								String fileStream = value.get(i).getFileStream();
//								
//								Log.d(TAG, "path : " + path + " fileName : " + fileName + " fileStream : " + fileStream);
//							}
//						}
//						else
//						{
//							Log.d(TAG, "value is null");
//						}
					}
					else
					{
						Log.d(TAG, "folderInfo is null");
					}
				}
				else
				{
					Log.d(TAG, "Get folder info failed");
				}
			}
		});
	}
}
