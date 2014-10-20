package com.example.retailsale;

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
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;

public class Login extends Activity implements OnClickListener
{
	private static final String TAG = "Login";
    private static final String DATA = "data";
    private static final String ID_FIELD = "id";
    private static final String PASSWORD_FIELD = "password";
	
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
			login();
			break;
		}
	}
	
	private void startWelcomeActivity() {
		Intent intent = new Intent(Login.this, WelcomeActivity.class);
		startActivity(intent);
	}
	
	private void login()
	{
		HttpManager httpManager = new HttpManager();
		httpManager.login(Login.this, "A123456", "1qaz@wsx", new GetLoginListener()
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
							String userGroup = userInfo.getValue().get(0).getUserGroup();
							String loginKey = userInfo.getValue().get(0).getLoginKey();
							String message = userInfo.getValue().get(0).getMessage();
							Log.d(TAG, "userGroup : " + userGroup + " loginKey : " + loginKey + " message : " + message);
							if (message.equals(Login.this.getResources().getString(R.string.login_successful))) {
								Log.d(TAG, "Message is successfully");
								saveData();
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
	
	private void saveData()
	{
		settings = getSharedPreferences(DATA, 0);
		settings.edit().putString(ID_FIELD, inputUserId.getText().toString())
				.putString(PASSWORD_FIELD, inputUserPassword.getText().toString()).commit();
	}
	
	private void readData() {
        settings = getSharedPreferences(DATA, 0);
        inputUserId.setText(settings.getString(ID_FIELD, ""));
        inputUserPassword.setText(settings.getString(PASSWORD_FIELD, ""));
	}
}
