package com.example.retailsale;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;

import com.example.retailsale.fragment.AddFragment;
import com.example.retailsale.fragment.BrowserFragment;
import com.example.retailsale.fragment.LogoutFragment;
import com.example.retailsale.fragment.SynchronizationFragment;
import com.example.retailsale.fragment.TabManager;

public class MainActivity extends FragmentActivity
{
    private TabHost tabHost;
    private TabManager tabManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		Resources res = getResources();
		String browserString = res.getString(R.string.browser_all_photo);
		String addString = res.getString(R.string.add_consumer);
		String synchronizationString = res.getString(R.string.sync_with_server);
		String logoutString = res.getString(R.string.logout);
		
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
        
        tabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        tabManager.addTab(
        		tabHost.newTabSpec(browserString).setIndicator(browserString),
        		BrowserFragment.class, null);
        tabManager.addTab(
        		tabHost.newTabSpec(addString).setIndicator(addString),
        		AddFragment.class, null);
        tabManager.addTab(
        		tabHost.newTabSpec(synchronizationString).setIndicator(synchronizationString),
        		SynchronizationFragment.class, null);
        tabManager.addTab(
        		tabHost.newTabSpec(logoutString).setIndicator(logoutString),
        		LogoutFragment.class, null);
	}

	public String getBrowserData()
	{
		return "1";
	}

	public String getAddData()
	{
		return "2";
	}

	public String getSyncData()
	{
		return "3";
	}

	public String getLogoutData()
	{
		return "4";
	}
	
	public void finishActivity() {
		this.finish();
	}
}
