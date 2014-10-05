package com.example.retailsale.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retailsale.MainActivity;
import com.example.retailsale.R;

public class LogoutFragment extends Fragment {
    
//    public LogoutFragment(Context context) {
//        
//    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		MainActivity mainActivity = (MainActivity) activity;
		// value = mainActivity.getLogoutData();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.logout_tab, container, false);
        
        return view;
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
//		TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//		txtResult.setText(value);
	}
}
