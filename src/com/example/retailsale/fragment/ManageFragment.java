package com.example.retailsale.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.MainActivity;
import com.example.retailsale.R;
import com.example.retailsale.util.Utility;

public class ManageFragment extends Fragment implements OnClickListener, OnLongClickListener
{
    private static final String TAG = "ManageFragment";
    private MainActivity mainActivity;
    private Dialog inputIpDialog;

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
        switch (v.getId())
        {
        case R.id.logout_btn:   // didn't clear option data, photo, customer data
            Log.d(TAG, "Now logout");
            Utility.saveData(mainActivity, Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.DEFAULT_NEGATIVE_VALUE,
                    Utility.DEFAULT_NEGATIVE_VALUE, Utility.SPACE_STRING);
            
            SharedPreferences settings = getActivity().getSharedPreferences(Utility.LoginField.APP_DATA, Utility.DEFAULT_ZERO_VALUE);
            settings.edit().putString(Utility.LoginField.APP_ACCOUNT, Utility.SPACE_STRING).putString(Utility.LoginField.APP_PASSWORD, Utility.SPACE_STRING)
                    .putInt(Utility.LoginField.APP_GROUP, Utility.DEFAULT_NEGATIVE_VALUE).commit();
            
            
            mainActivity.finish();
            break;
        case R.id.input_ip_dialog_btn:
            if (inputIpDialog != null)
            {
                String ip = ((EditText) inputIpDialog.findViewById(R.id.input_ip_dialog_input_user_id))
                        .getText().toString();
                
                Log.d(TAG, "ip is " + ip);
                
                Utility.setIP(getActivity(), ip);
                
                inputIpDialog.dismiss();
            }
            else
            {
                showToast(getResources().getString(R.string.ui_error));
            }
            break;
        case R.id.input_ip_dialog_cancel_btn:
            if (inputIpDialog != null)
            {
                inputIpDialog.dismiss();
            }
            break;
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        modifyIPDialog();
        return false;
    }
    
    private void modifyIPDialog()
    {
        Log.d(TAG, "server ip is " + Utility.getIP(getActivity()));
        
        if (inputIpDialog == null)
        {
            inputIpDialog = new Dialog(getActivity());
            inputIpDialog.setContentView(R.layout.input_ip_dialog);
        }
        else
        {
            if (inputIpDialog.isShowing())
            {
                Log.d(TAG, "The input ip dialog is showing! ");
                return;
            }
            else
            {
                Log.d(TAG, "The input ip dialog is not showing! ");
            }
        }
        
        EditText ipContent = (EditText) inputIpDialog.findViewById(R.id.input_ip_dialog_input_user_id);
        ipContent.setText(Utility.getIP(getActivity()));
        
        Button loginBtn = (Button) inputIpDialog.findViewById(R.id.input_ip_dialog_btn);
        loginBtn.setOnClickListener(this);
        
        Button cancelBtn = (Button) inputIpDialog.findViewById(R.id.input_ip_dialog_cancel_btn);
        cancelBtn.setOnClickListener(this);

        inputIpDialog.setTitle(getResources().getString(R.string.server_ip));
        inputIpDialog.show();
    }
    
    private void showToast(String showString)
    {
        Toast.makeText(getActivity(), showString, Toast.LENGTH_SHORT).show();
    }
}
