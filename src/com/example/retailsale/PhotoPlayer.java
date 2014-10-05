package com.example.retailsale;

import java.io.File;

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

import com.example.retailsale.photoview.PhotoViewAttacher;

public class PhotoPlayer extends Activity implements OnClickListener {
    private static final String TAG = "Main";

    private LinearLayout photos;
    private ImageView scalableImageView;
    private Button backBtn;
    PhotoViewAttacher mAttacher;
    
    private int count = 0;
    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoplayer);

        scalableImageView = (ImageView) findViewById(R.id.testnewphoto);
        scalableImageView.setImageResource(R.drawable.test);

//        scalableImageView.setFit(true);
//        scalableImageView.setMaxZoom(10);
//        scalableImageView.setMinZoom(1);

        photos = (LinearLayout) findViewById(R.id.photots);
        backBtn = (Button) findViewById(R.id.photo_player_cancel_btn);
        backBtn.setOnClickListener(this);
        
        for (int i = 0; i < 3; i++) {
        	photos.addView(insertPhoto("test"));
        }

//        String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();

//        String targetPath = ExternalStorageDirectoryPath + "/DCIM/Camera/";

//        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
//        File targetDirector = new File(targetPath);
//        
//        Log.d(TAG, "targetPath ==== " + targetPath);
//
//        files = targetDirector.listFiles();
//		if (files != null)
//		{
//			for (File file : files)
//			{
//				photos.addView(insertPhoto(file.getAbsolutePath()));
//			}
//		}
        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(scalableImageView);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        count = 0;
    }

    View insertPhoto(String path) {
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
        imageView.setTag(count);
        count++;
        imageView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.d(TAG, "count " + v.getTag());
//                scalableImageView.initStanScalableImageView(MainActivity.this);
                
//                scalableImageView.setImageBitmap(decodeSampledBitmapFromUri(files[(Integer)v.getTag()].getAbsolutePath()));
                
             // If you later call mImageView.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
//                mAttacher.update();
            }
        });

        layout.addView(imageView);
        return layout;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
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
    
    public Bitmap decodeSampledBitmapFromUri(String path) {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

    BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
