package com.example.retailsale.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retailsale.MainActivity;
import com.example.retailsale.R;
import com.example.retailsale.util.Utility;

public class ManageFragment extends Fragment implements OnClickListener, OnLongClickListener
{
    private static final String TAG = "ManageFragment";
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.manage_tab, container, false);

        TextView logoutText = (TextView) view.findViewById(R.id.logout_btn);
        logoutText.setOnClickListener(this);
        
        ImageView welcomeLogo = (ImageView) view.findViewById(R.id.welcome_logo);
        welcomeLogo.setOnLongClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.logout_btn)
        { // didn't clear option data, photo, customer data
            Log.d(TAG, "Now logout");
            Utility.saveData(mainActivity, Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.DEFAULT_NEGATIVE_VALUE,
                    Utility.DEFAULT_NEGATIVE_VALUE, Utility.SPACE_STRING);
            
            SharedPreferences settings = getActivity().getSharedPreferences(Utility.LoginField.APP_DATA, Utility.DEFAULT_ZERO_VALUE);
            settings.edit().putString(Utility.LoginField.APP_ACCOUNT, Utility.SPACE_STRING).putString(Utility.LoginField.APP_PASSWORD, Utility.SPACE_STRING)
                    .putInt(Utility.LoginField.APP_GROUP, Utility.DEFAULT_NEGATIVE_VALUE).commit();
            
            
            mainActivity.finish();
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        return false;
    }
    
    private void modifyIPDialog()
    {
        
    }
}
