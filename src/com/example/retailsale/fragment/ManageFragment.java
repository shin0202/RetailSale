package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.MainFragmentActivity;
import com.example.retailsale.R;
import com.example.retailsale.adapter.CommonAdapter;
import com.example.retailsale.util.Utility;

public class ManageFragment extends Fragment implements OnClickListener, OnLongClickListener
{
    private static final String TAG = "ManageFragment";
    private MainFragmentActivity mainActivity;
    private Dialog settingsDialog;
    private Spinner autoUploadSpinner1, autoUploadSpinner2, autoUploadSpinner3;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainFragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_manager, container, false);

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
        case R.id.dialog_settings_save_btn:
            if (settingsDialog != null)
            {
                String ip = ((EditText) settingsDialog.findViewById(R.id.dialog_settings_input_ip))
                        .getText().toString();
                
                Log.d(TAG, "ip is " + ip);
                
                Utility.setIP(getActivity(), ip);
                
                ((Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_1)).getSelectedItemPosition();
                
                int time1 = ((Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_1)).getSelectedItemPosition() + 1;
                int time2 = ((Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_2)).getSelectedItemPosition() + 1;
                int time3 = ((Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_3)).getSelectedItemPosition() + 1;
                
                Log.d(TAG, "time1 : " + time1 + " time2 : " + time2 + " time3 : " + time3);
                
                Utility.setAlarmTimeFromSettings(getActivity(), time1, time2, time3);
                
                Utility.saveAlarmTimeData(getActivity(), time1, time2, time3);
                
                settingsDialog.dismiss();
            }
            else
            {
                showToast(getResources().getString(R.string.ui_error));
            }
            break;
        case R.id.dialog_settings_cancel_btn:
            if (settingsDialog != null)
            {
                settingsDialog.dismiss();
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
        
        if (settingsDialog == null)
        {
            settingsDialog = new Dialog(getActivity());
            settingsDialog.setContentView(R.layout.dialog_settings);
        }
        else
        {
            if (settingsDialog.isShowing())
            {
                Log.d(TAG, "The settings dialog is showing! ");
                return;
            }
            else
            {
                Log.d(TAG, "The settings dialog is not showing! ");
            }
        }
        
        EditText ipContent = (EditText) settingsDialog.findViewById(R.id.dialog_settings_input_ip);
        ipContent.setText(Utility.getIP(getActivity()));
        
        Button saveBtn = (Button) settingsDialog.findViewById(R.id.dialog_settings_save_btn);
        saveBtn.setOnClickListener(this);
        
        Button cancelBtn = (Button) settingsDialog.findViewById(R.id.dialog_settings_cancel_btn);
        cancelBtn.setOnClickListener(this);
        
        autoUploadSpinner1 = (Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_1);
        autoUploadSpinner1.setAdapter(setCountSpinner(1, 25));
        int time1Position = Utility.getAlarmTime(getActivity(), Utility.AlarmTime.ALARM_TIME_1);
        
        autoUploadSpinner1.setSelection(time1Position - 1);
        
        autoUploadSpinner2 = (Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_2);
        autoUploadSpinner2.setAdapter(setCountSpinner(1, 25));
        int time2Position = Utility.getAlarmTime(getActivity(), Utility.AlarmTime.ALARM_TIME_2);
        
        autoUploadSpinner2.setSelection(time2Position - 1);
        
        autoUploadSpinner3 = (Spinner) settingsDialog.findViewById(R.id.dialog_settings_input_time_spinner_3);
        autoUploadSpinner3.setAdapter(setCountSpinner(1, 25));
        int time3Position = Utility.getAlarmTime(getActivity(), Utility.AlarmTime.ALARM_TIME_3);
        
        autoUploadSpinner3.setSelection(time3Position - 1);

        settingsDialog.setTitle(getResources().getString(R.string.server_ip));
        settingsDialog.show();
    }
    
    private void showToast(String showString)
    {
        Toast.makeText(getActivity(), showString, Toast.LENGTH_SHORT).show();
    }
    
    private CommonAdapter setCountSpinner(int startValue, int endValue)
    {
        List<String> valueList = new ArrayList<String>();
        
        for (int i = startValue; i < endValue; i++)
        {
            valueList.add(String.valueOf(i));
        }
        
        CommonAdapter valueAdapter = new CommonAdapter(this.getActivity(), valueList);

        return valueAdapter;
    }
}
