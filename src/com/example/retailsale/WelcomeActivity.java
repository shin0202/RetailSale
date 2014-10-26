package com.example.retailsale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity implements OnClickListener
{
	private static final String TAG = "WelcomeActivity";
	public static final String TAB_POSITION = "tab_position";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_welcome);
    	
    	findViews();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
//    	uiHandler.sendMessageDelayed(new Message(), 1300);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
	@Override
	public void onClick(View v)
	{
		int postion = 0;
		switch (v.getId())
		{
		case R.id.welcome_browser_btn:
			postion = 0;
			break;
		case R.id.welcome_add_btn:
			postion = 1;
			break;
		case R.id.welcome_sync_btn:
			postion = 2;
			break;
		case R.id.welcome_logout_btn:
			postion = 3;
			break;
		}
		startMainActivity(postion);
	}
	
	private void findViews() {
		Button browserBtn = (Button) findViewById(R.id.welcome_browser_btn);
		Button addCustomerBtn = (Button) findViewById(R.id.welcome_add_btn);
		Button syncBtn = (Button) findViewById(R.id.welcome_sync_btn);
		Button logoutBtn = (Button) findViewById(R.id.welcome_logout_btn);
		
		browserBtn.setOnClickListener(this);
		addCustomerBtn.setOnClickListener(this);
		syncBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
	}
	
	private void startMainActivity(int tabPosition) {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		Bundle bundle = new Bundle();
		
		Log.d(TAG, "tabPosition : " + tabPosition);
		
		bundle.putInt(TAB_POSITION, tabPosition);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
		WelcomeActivity.this.finish();
	}
    
//    Handler uiHandler = new Handler() {
//    	@Override
//    	public void handleMessage(Message msg) {
//    		startMainActivity();
//    	};
//    };
}
