package com.example.retailsale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

public class MainTab extends ActionBarActivity {
    private static final String TAB_KEY_INDEX = "tab_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintab);

        final ActionBar actionbar = getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // add tab
        ActionBar.Tab addTab = actionbar.newTab().setText(getResources().getString(R.string.add_consumer));
        ActionBar.Tab browserTab = actionbar.newTab().setText(getResources().getString(R.string.browser_all_photo));
        ActionBar.Tab synchronizationTab = actionbar.newTab().setText(getResources().getString(R.string.sync_with_server));
        ActionBar.Tab logoutTab = actionbar.newTab().setText(getResources().getString(R.string.logout));

        // create the fragments
//        Fragment addFragment = new AddFragment(MainTab.this);
//        Fragment browserFragment = new BrowserFragment(MainTab.this);
//        Fragment synchronizationFragment = new SynchronizationFragment(MainTab.this);
//        Fragment logoutFragment = new LogoutFragment(MainTab.this);
        
        Fragment addFragment = null;
        Fragment browserFragment = null;
        Fragment synchronizationFragment = null;
        Fragment logoutFragment = null;

        // bind the fragments to the tabs - set up tabListeners for each tab
        addTab.setTabListener(new TabsListener(addFragment, getApplicationContext()));
        browserTab.setTabListener(new TabsListener(browserFragment, getApplicationContext()));
        synchronizationTab.setTabListener(new TabsListener(synchronizationFragment, getApplicationContext()));
        logoutTab.setTabListener(new TabsListener(logoutFragment, getApplicationContext()));

        // add the tabs to the action bar
        actionbar.addTab(browserTab);
        actionbar.addTab(addTab);
        actionbar.addTab(synchronizationTab);
        actionbar.addTab(logoutTab);

        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowHomeEnabled(false);

        // restore to navigation
        if (savedInstanceState != null) {
            actionbar.setSelectedNavigationItem(savedInstanceState.getInt(TAB_KEY_INDEX, 0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_KEY_INDEX, getActionBar().getSelectedNavigationIndex());

    }

    private class TabsListener implements ActionBar.TabListener {
        public Fragment fragment;
        public Context context;

        public TabsListener(Fragment fragment, Context context) {
            this.fragment = fragment;
            this.context = context;
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }
    }
}
