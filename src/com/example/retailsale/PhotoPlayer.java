package com.example.retailsale;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.retailsale.fragment.BrowserFragment;
import com.example.retailsale.manager.LocalFileInfo;
import com.example.retailsale.photoview.PhotoViewAttacher;

public class PhotoPlayer extends Activity implements OnClickListener {
    private static final String TAG = "PhotoPlayer";

    private LinearLayout photosLayout;
    private ImageView scalableIV;
    private Button backBtn;
    PhotoViewAttacher attacher;
    
    private int position = 0;
    File[] files;
    private List<LocalFileInfo> photoList = new ArrayList<LocalFileInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoplayer);

        findViews();
        
        getBundle();
        
        for (int i = 0; i < photoList.size(); i++) {
            photosLayout.addView(insertPhoto(photoList.get(i).getFilePath()));
        }

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        attacher = new PhotoViewAttacher(scalableIV);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        position = 0;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        position = 0;
    }
    
    private void findViews() {
        scalableIV = (ImageView) findViewById(R.id.photo_player_display);
        scalableIV.setImageResource(R.drawable.test);
        
//      scalableIV.setFit(true);
//      scalableIV.setMaxZoom(10);
//      scalableIV.setMinZoom(1);
        
        photosLayout = (LinearLayout) findViewById(R.id.photots);
        backBtn = (Button) findViewById(R.id.photo_player_cancel_btn);
        backBtn.setOnClickListener(this);
    }
    
    private void getBundle() {
        Bundle bundle = this.getIntent().getExtras();
        
        if (bundle != null) {
            photoList = bundle.getParcelableArrayList(BrowserFragment.FILE_LIST);
            
            if (photoList != null) {
                for (int i = 0; i < photoList.size(); i++) {
                    Log.d(TAG, "Get data position : " + i + " name: " + photoList.get(i).getFileName() + 
                            " path: " + photoList.get(i).getFilePath() + " type: " + photoList.get(i).getFileType());
                }
            } else {
                Log.d(TAG, "It is no data from BrowserFragment(list is null)!");
            }
        } else {
            Log.d(TAG, "It is no data from BrowserFragment(bundle is null)!");
        }
    }

    private View insertPhoto(String path) {
//        Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
    	
    	int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
		int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageBitmap(bm);
        imageView.setImageDrawable(PhotoPlayer.this.getResources().getDrawable(R.drawable.img));
        imageView.setTag(position);
        position++;
        imageView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.d(TAG, "position : " + v.getTag());
//                scalableIV.initStanScalableImageView(MainActivity.this);
                
//                scalableIV.setImageBitmap(decodeSampledBitmapFromUri(files[(Integer)v.getTag()].getAbsolutePath()));
                
             // If you later call scalableIV.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
//                attacher.update();
            }
        });

        layout.addView(imageView);
        return layout;
    }

    private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) 
    {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }
    
    private Bitmap decodeSampledBitmapFromUri(String path) 
    {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.photo_player_cancel_btn:
			this.finish();
			break;
		}
	}
}
