package com.example.retailsale;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.retailsale.fragment.AddFragment;
import com.example.retailsale.fragment.BrowserFragment;
import com.example.retailsale.fragment.ManageFragment;
import com.example.retailsale.fragment.SynchronizationFragment;
import com.example.retailsale.fragment.TabManager;

public class MainActivity extends FragmentActivity
{
    private TabHost tabHost;
    private TabManager tabManager;
    private int currentTab;

    public class TabPosition
    {
        public static final int BROWSER = 0;
        public static final int ADD = 1;
        public static final int SYNC = 2;
        public static final int MANAGE = 3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        Resources res = getResources();
        String browserString = res.getString(R.string.browser_all_photo);
        String addString = res.getString(R.string.add_consumer);
        String synchronizationString = res.getString(R.string.sync_with_server);
        String manageString = res.getString(R.string.manage);

        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

        tabManager.addTab(tabHost.newTabSpec(browserString).setIndicator(browserString), BrowserFragment.class, null);
        tabManager.addTab(tabHost.newTabSpec(addString).setIndicator(addString), AddFragment.class, null);
        tabManager.addTab(tabHost.newTabSpec(synchronizationString).setIndicator(synchronizationString),
                SynchronizationFragment.class, null);
        tabManager.addTab(tabHost.newTabSpec(manageString).setIndicator(manageString), ManageFragment.class, null);

        setManageTab();

        setTabColor(tabHost);
    }

    public void setTabColor(TabHost tabhost)
    {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
        {
            tabhost.getTabWidget().getChildAt(i)
                    .setBackgroundColor(getResources().getColor(R.color.common_btn_tab_bg_normal)); // unselected
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); // Unselected Tabs
            tv.setTextColor(getResources().getColor(R.color.common_layout_bg));
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
                .setBackgroundColor(getResources().getColor(R.color.common_btn_tab_bg_focus)); // selected
    }

    public void finishActivity()
    {
        this.finish();
    }

    public void setManageTab()
    {
        currentTab = TabPosition.MANAGE;

        tabHost.setCurrentTab(TabPosition.MANAGE); // set the current tab
    }
}
