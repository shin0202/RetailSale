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

import com.example.retailsale.manager.GetLoginListener;
import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.UserInfo;

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
			saveData();
			startWelcomeActivity();
			break;
		}
	}
	
	private void startWelcomeActivity() {
		Intent intent = new Intent(Login.this, WelcomeActivity.class);
		startActivity(intent);
	}
	
	private void login() {
    	HttpManager httpManager = new HttpManager();
    	httpManager.login(Login.this, "A123456", "1qaz@wsx", new GetLoginListener() {

			@Override
			public void onResult(Boolean isSuccess, UserInfo userInfo)
			{
				Log.d(TAG, "userSerial === " + userInfo.getValue().get(0).getUserSerial());
				Log.d(TAG, "userGroup === " + userInfo.getValue().get(0).getUserGroup());
				Log.d(TAG, "loginKey === " + userInfo.getValue().get(0).getLoginKey());
				Log.d(TAG, "message === " + userInfo.getValue().get(0).getMessage());
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
