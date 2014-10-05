package com.example.retailsale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_welcome);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	uiHandler.sendMessageDelayed(new Message(), 1300);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    Handler uiHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		startMainActivity();
    	};
    };
    
	private void startMainActivity() {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(intent);
		WelcomeActivity.this.finish();
	}
}
