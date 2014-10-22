package com.example.retailsale.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retailsale.MainActivity;
import com.example.retailsale.R;

public class SynchronizationFragment extends Fragment implements OnClickListener {
    private LinearLayout uploadConsumer, downloadPicture, syncData;
    private TextView showTitle, showDescription, showMessage;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		MainActivity mainActivity = (MainActivity) activity;
	}
        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.synchronization_tab, container, false);
        
        uploadConsumer = (LinearLayout) view.findViewById(R.id.sync_tab_upload_layout);
        downloadPicture = (LinearLayout) view.findViewById(R.id.sync_tab_download_layout);
        syncData = (LinearLayout) view.findViewById(R.id.sync_tab_sync_layout);
        
        showTitle = (TextView) view.findViewById(R.id.sync_tab_item_title);
        showDescription = (TextView) view.findViewById(R.id.sync_tab_item_description);
        showMessage = (TextView) view.findViewById(R.id.sync_tab_item_message);
        
        uploadConsumer.setOnClickListener(this);
        downloadPicture.setOnClickListener(this);
        syncData.setOnClickListener(this);
        
        focusUploadConsumer();
        
        return view;
    }
    
    private void focusUploadConsumer() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_consumer));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_description));
        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_upload_message));
    }
    
    private void focusDownloadPicture() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_picture));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_description));
        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_download_message));
    }
    
    private void focusSyncData() {
        uploadConsumer.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        downloadPicture.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.antiquewhite));
        syncData.setBackgroundColor(SynchronizationFragment.this.getResources().getColor(R.color.lemonchiffon));
        
        showTitle.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_data));
        showDescription.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_description));
        showMessage.setText(SynchronizationFragment.this.getResources().getString(R.string.sync_tab_sync_message));
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sync_tab_upload_layout:
            focusUploadConsumer();
            break;
        case R.id.sync_tab_download_layout:
            focusDownloadPicture();
            break;
        case R.id.sync_tab_sync_layout:
            focusSyncData();
            break;
        }
    }
}
