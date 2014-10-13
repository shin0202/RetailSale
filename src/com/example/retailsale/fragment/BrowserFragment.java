package com.example.retailsale.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private int albumCount = 0;
    private String currentParent;
    private PhotosAdapterView photosAdapterView;
    private List<LocalFileInfo> albumList = new ArrayList<LocalFileInfo>();
    private List<LocalFileInfo> photoList = new ArrayList<LocalFileInfo>();
    
    // views
    private LinearLayout albums;
    private GridView photoGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        
        // get albums from download path
        albumCount = 0;
        currentParent = Utility.FILE_PATH;
        listFolder(new File(currentParent));
        
        // get content from first album path
        listFilesInFolder(new File(albumList.get(0).getFilePath()));
        photosAdapterView = new PhotosAdapterView(getActivity(), photoList);
        photoGrid.setAdapter(photosAdapterView);
        
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PhotoPlayer.class);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//	      TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//	      txtResult.setText(value);
    }
    
    public void listFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            String name = fileEntry.getName();
            String path = fileEntry.getAbsolutePath();
            Log.d(TAG, "name: " + name + " path: " + path);
            albums.addView(addQuicklySelectedDummy(name, path, LocalFileInfo.SELECTED_DIR));
        }
    }
    
    public void listFilesInFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            String name = fileEntry.getName();
            String path = fileEntry.getAbsolutePath();
            Log.d(TAG, "name: " + name + " path: " + path);
            if (fileEntry.isDirectory()) {
                photoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_DIR));
            } 
            else {
                photoList.add(new LocalFileInfo(name, path, LocalFileInfo.SELECTED_FILE));
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
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // imageView.setImageBitmap(bm);
        imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.album));
        imageView.setTag(albumCount);
        albumCount++;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "albumCount " + v.getTag());
//                int photoCount = 0;
//                photos.clear();
//                photosAdapterView = null;
//                switch ((Integer) v.getTag()) {
//                case 0:
//                    photoCount = 50;
//                    break;
//                case 1:
//                    photoCount = 30;
//                    break;
//                case 2:
//                    photoCount = 10;
//                    break;
//                }
//                Log.d(TAG, "photoCount " + photoCount);
//                for (int i = 0; i < photoCount; i++) {
//                    photos.add(i);
//                }
//                photosAdapterView = new PhotosAdapterView(getActivity(), photos);
//                photoGrid.setAdapter(photosAdapterView);
            }
        });
        
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        
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
