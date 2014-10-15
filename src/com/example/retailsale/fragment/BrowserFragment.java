package com.example.retailsale.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retailsale.MainActivity;
import com.example.retailsale.PhotoPlayer;
import com.example.retailsale.R;
import com.example.retailsale.manager.LocalFileInfo;
import com.example.retailsale.util.Utility;

public class BrowserFragment extends Fragment implements OnItemClickListener {
    private static final String TAG = "BrowserFragment";
    public static final String FILE_LIST = "file_list";
    private int albumNum = 0;
    private String currentParent;
    private PhotosAdapterView photosAdapterView;
    private List<LocalFileInfo> albumList = new ArrayList<LocalFileInfo>();
    private List<LocalFileInfo> photoList = new ArrayList<LocalFileInfo>();
    private ProgressDialog dialog;
    
    // views
    private LinearLayout albums;
    private GridView photoGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        // value = mainActivity.getBrowserData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browser_tab, container, false);
        photoGrid = (GridView) view.findViewById(R.id.browser_tab_files_grid);
        photoGrid.setOnItemClickListener(this);
        albums = (LinearLayout) view.findViewById(R.id.albums);
        
        handlePageRefresh(Utility.FILE_PATH);
        
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (photoList != null) 
        {
            LocalFileInfo localFileInfo = photoList.get(position);
            if (localFileInfo != null && position < photoList.size())
            {
                int fileType = localFileInfo.getFileType();
                if (fileType == LocalFileInfo.SELECTED_FILE) // is file, need to send list(path) for PhotoPlayer
                {
                  Intent intent = new Intent(getActivity(), PhotoPlayer.class);
                  Bundle bundle = new Bundle();
                  bundle.putParcelableArrayList(FILE_LIST, (ArrayList<? extends Parcelable>) photoList);
                  intent.putExtras(bundle);
                  startActivity(intent);
                } else { // is directory
                    String currentParent = this.currentParent + localFileInfo.getFileName();
                    handlePageRefresh(currentParent);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//	      TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//	      txtResult.setText(value);
    }
    
    private void handlePageRefresh(String currentParent) {
        dialog = ProgressDialog.show(BrowserFragment.this.getActivity(),
                "讀取中", "請等待...",true);
        // get albums from download path
        albumNum = 0;
        this.currentParent = currentParent;
        Log.d(TAG, "current parent is " + this.currentParent);
        listFolder(new File(this.currentParent));
        
        // get content from first album path
        if (albumList.size() > 0)
        {
            listFilesInFolder(new File(albumList.get(0).getFilePath()));
        }
        
        if (photoList.size() != 0)
        {
            photosAdapterView = new PhotosAdapterView(getActivity(), photoList);
            photoGrid.setAdapter(photosAdapterView);
        }
        
        dialog.dismiss();
    }
    
	public void listFolder(final File folder)
	{
	    if (albums != null) {
	        albums.removeAllViews();
	    }
	    
		if (folder.listFiles() != null)
		{
			for (final File fileEntry : folder.listFiles())
			{
				String name = fileEntry.getName();
				String path = fileEntry.getAbsolutePath();
				Log.d(TAG, "name: " + name + " path: " + path);
				albums.addView(addQuicklySelectedDummy(name, path, LocalFileInfo.SELECTED_DIR));
			}
		} else {
			Log.d(TAG, "listFolder listFiles is null ");
		}
	}
    
	public void listFilesInFolder(final File folder)
	{
	    if (photoList != null) {
	        photoList.clear();
	    }
		if (folder.listFiles() != null)
		{
			for (final File fileEntry : folder.listFiles())
			{
				String name = fileEntry.getName();
				String path = fileEntry.getAbsolutePath();
				Log.d(TAG, "name: " + name + " path: " + path);
				if (fileEntry.isDirectory())
				{
					photoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));
				}
				else
				{
					photoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_FILE));
				}
			}
		}
	}

    private View addQuicklySelectedDummy(String name, String path, int type) {
        // Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
        int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
        int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // imageView.setImageBitmap(bm);
        imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.album));
        imageView.setTag(albumNum);
        albumNum++;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "albumNum " + v.getTag());
                photoList.clear();
                photosAdapterView = null;
                
                listFilesInFolder(new File(albumList.get((Integer)v.getTag()).getFilePath()));
            	
				if (photoList.size() != 0)
				{
					photosAdapterView = new PhotosAdapterView(getActivity(), photoList);
					photoGrid.setAdapter(photosAdapterView);
				}
            }
        });
        
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setText(name);
        
        layout.addView(imageView);
        layout.addView(textView);
        
        albumList.add(new LocalFileInfo(name, path, type));
        
        return layout;
    }

    public class PhotosAdapterView extends BaseAdapter {
        public static final int BASE_INDEX = 1000;
        private List<LocalFileInfo> photoList;
        private Context context;
        // Views
        private LayoutInflater layoutInflater;
        private ViewTag viewTag;

        public PhotosAdapterView(Context context, List<LocalFileInfo> photoList) {
            this.context = context;
            this.photoList = photoList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public Object getItem(int position) {
            return photoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.cell_of_browser_tab, null);
                viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.browser_photo),
                        (TextView) convertView.findViewById(R.id.photo_name));
                convertView.setTag(viewTag);
            } else {
                viewTag = (ViewTag) convertView.getTag();
            }
            convertView.setId(BASE_INDEX + position);
            
            viewTag.showName.setText(photoList.get(position).getFileName());
            
            // to show img
            //viewTag.showPhoto.setImageBitmap(bm);
            
            return convertView;
        }

        class ViewTag {
            ImageView showPhoto;
            TextView showName;
            public ViewTag(ImageView showPhoto, TextView showName) {
                this.showPhoto = showPhoto;
                this.showName = showName;
            }
        }
    }
}
