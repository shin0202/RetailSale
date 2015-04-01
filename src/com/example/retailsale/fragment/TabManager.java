package com.example.retailsale.fragment;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.example.retailsale.MainFragmentActivity;
import com.example.retailsale.R;

public class TabManager implements OnTabChangeListener
{
    private static final String TAG = "TabManager";
    private final FragmentActivity activity;
    private final TabHost tabhost;
    private final int containerId;
    private final HashMap<String, TabInfo> tabs = new HashMap<String, TabInfo>();
    TabInfo lastTab;

    static final class TabInfo
    {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args)
        {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory
    {
        private final Context mContext;

        public DummyTabFactory(Context context)
        {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag)
        {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabManager(FragmentActivity activity, TabHost tabHost, int containerId)
    {
        this.activity = activity;
        tabhost = tabHost;
        this.containerId = containerId;
        tabhost.setOnTabChangedListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
    {
        tabSpec.setContent(new DummyTabFactory(activity));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);

        info.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached())
        {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
        }

        tabs.put(tag, info);
        tabhost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId)
    {
        TabInfo newTab = tabs.get(tabId);

        ((MainFragmentActivity) activity).setTabColor(tabhost);

        if (lastTab != newTab)
        {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            if (lastTab != null)
            {
                if (lastTab.fragment != null)
                {
                    ft.detach(lastTab.fragment);
                }
            }

            if (newTab != null)
            {
                newTab.fragment = Fragment.instantiate(activity, newTab.clss.getName(), newTab.args);
                ft.add(containerId, newTab.fragment, newTab.tag);
                if (newTab.fragment == null)
                {
                    ft.detach(lastTab.fragment);
                }
                else
                {
                    activity.getSupportFragmentManager().popBackStack();
                    ft.replace(containerId, newTab.fragment);
                    ft.attach(newTab.fragment);
                }
                
                if (lastTab != null)
                {
                    Log.d(TAG, "lastTab name " + lastTab.clss.getName() + " newTab name " + newTab.clss.getName());
                    
                    if (!lastTab.clss.getName().equals(newTab.clss.getName()) && lastTab.clss.getName().equals("com.example.retailsale.fragment.AddFragment"))
                    {
                        Toast.makeText(activity, activity.getResources().getString(R.string.data_auto_save), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            lastTab = newTab;
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }

    }
}
