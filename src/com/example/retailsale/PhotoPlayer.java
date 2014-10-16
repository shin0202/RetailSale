package com.example.retailsale;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retailsale.fragment.BrowserFragment;
import com.example.retailsale.manager.LocalFileInfo;
import com.example.retailsale.photoview.PhotoViewAttacher;
import com.example.retailsale.util.Utility;

public class PhotoPlayer extends Activity implements OnClickListener {
    private static final String TAG = "PhotoPlayer";

    private LinearLayout photosLayout;
    private ImageView scalableIV;
    private Button backBtn;
    PhotoViewAttacher attacher;
    private ProgressDialog progressDialog;
    
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
            photosLayout.addView(insertPhoto(photoList.get(i).getFileName(), photoList.get(i).getFilePath()));
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

    private View insertPhoto(String name, String path) {
//        Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
    	
    	int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
		int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);

        LinearLayout layout = new LinearLayout(PhotoPlayer.this);
        layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(PhotoPlayer.this);
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageBitmap(bm);
        imageView.setImageDrawable(PhotoPlayer.this.getResources().getDrawable(R.drawable.img));
        imageView.setTag(position);
        position++;
        imageView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	int position = (Integer)v.getTag();
                Log.d(TAG, "position : " + position);
//                scalableIV.initStanScalableImageView(PhotoPlayer.this);
                
                if (photoList != null && photoList.size() > 0 && position < photoList.size())
                {
                	scalableIV.setImageBitmap(decodeSampledBitmapFromUri(photoList.get(position).getFilePath()));
                
                	// If you later call scalableIV.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
                	attacher.update();
                }
            }
        });
        
		TextView textView = new TextView(PhotoPlayer.this);
		textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextSize(16);
		textView.setTextColor(Color.BLACK);
		textView.setText(name);

        layout.addView(imageView);
        layout.addView(textView);
        
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
	
	private Handler dialogHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case Utility.SHOW_WAITING_DIALOG:
				Log.d(TAG, "show waiting dialog ");
				progressDialog = ProgressDialog.show(PhotoPlayer.this, "", "讀取中");
				break;
			case Utility.DISMISS_WAITING_DIALOG:
				Log.d(TAG, "dismiss dialog ");
				if (progressDialog != null) progressDialog.dismiss();
				break;
			}
		}
	};
}
