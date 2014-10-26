package com.example.retailsale;

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
import com.example.retailsale.manager.fileinfo.LocalFileInfo;
import com.example.retailsale.photoview.PhotoViewAttacher;
import com.example.retailsale.util.Utility;

public class PhotoPlayer extends Activity implements OnClickListener {
    private static final String TAG = "PhotoPlayer";

    private LinearLayout photosLayout;
    private ImageView scalableIV;
    private Button backBtn;
    PhotoViewAttacher attacher;
    private ProgressDialog progressDialog;
    
    private int showAlbumCount = 0;
    private int currentPosition = 0;
    private List<LocalFileInfo> photoList = new ArrayList<LocalFileInfo>();
    
    private Bitmap bm;

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
        attacher = new PhotoViewAttacher(scalableIV, PhotoPlayer.this);
        
		if (currentPosition < photoList.size())
		{
			decodeSampledBitmapFromUri(photoList.get(currentPosition).getFilePath(),
					photoList.get(currentPosition).getFileName());
		}
		else
		{
			Log.d(TAG, "showAlbumCount over to photoList size");
		}
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        showAlbumCount = 0;
    }
    
    private void findViews() {
        scalableIV = (ImageView) findViewById(R.id.photo_player_display);
        
//      scalableIV.setFit(true);
//      scalableIV.setMaxZoom(10);
//      scalableIV.setMinZoom(1);
        
        photosLayout = (LinearLayout) findViewById(R.id.photots);
        backBtn = (Button) findViewById(R.id.photo_player_cancel_btn);
        backBtn.setOnClickListener(this);
    }
    
    private void getBundle() {
        Bundle bundle = this.getIntent().getExtras();
        currentPosition = 0;
        
        if (bundle != null) {
            photoList = bundle.getParcelableArrayList(BrowserFragment.FILE_LIST);
            currentPosition = bundle.getInt(BrowserFragment.FILE_POSITION);
            Log.d(TAG, "currentPosition is " + currentPosition);
            if (photoList != null) {
                for (int i = 0; i < photoList.size(); i++) {
                    Log.d(TAG, "Get data showAlbumCount : " + i + " name: " + photoList.get(i).getFileName() + 
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
        imageView.setTag(showAlbumCount);
        showAlbumCount++;
        imageView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	int position = (Integer)v.getTag();
                Log.d(TAG, "position : " + position);
//                scalableIV.initStanScalableImageView(PhotoPlayer.this);
                
                if (photoList != null && photoList.size() > 0 && position < photoList.size())
                {
                	currentPosition = position;
                	decodeSampledBitmapFromUri(photoList.get(position).getFilePath(), photoList.get(position).getFileName());
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
		textView.setText(name.replace(Utility.REPLACE_STRING, ""));

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
    
    private void decodeSampledBitmapFromUri(final String path, final String fileName) 
    {
    	dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
//        Bitmap bm = null;
        scalableIV.setImageBitmap(null);
        
        if (bm != null) {
        	bm.recycle();
        	bm = null;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

//        bm = BitmapFactory.decodeFile(path, options);
        
		// 5. to read file and remove md5
		String readContent = Utility.readFile(path);
//		Log.d(TAG, "readContent is " + readContent);
		Log.d(TAG, "*********************************************************** ");
		String realFileName = fileName.replace(".txt", "");
//		Log.d(TAG, "realFileName is " + realFileName);
		Log.d(TAG, "*********************************************************** ");
		String md5String = Utility.generateMD5String(realFileName);
//		Log.d(TAG, "md5String is " + md5String);
		Log.d(TAG, "*********************************************************** ");
		
		String realData = readContent.replace(md5String, "");
//		Log.d(TAG, "realData is " + realData);
		Log.d(TAG, "*********************************************************** ");
		// 6. decode Base64 to byte[]
        
		byte[] photo = Utility.decodeBase64(realData);
		
		try {
			bm = BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		
//		Utility.writeFile("/sdcard/retailSale/123/java2.txt", Utility
//				.encodeBase64(new BitmapFactory.decodeFile("/sdcard/retailSale/123/test1.jpg")));
//		Log.d(TAG,
//				"****************************HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH******************************* ");
//		byte[] photo = Utility.decodeBase64(Utility.readFile("/sdcard/retailSale/123/java2.txt"));
//		try
//		{
//			bm = BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		catch (OutOfMemoryError e)
//		{
//			e.printStackTrace();
//		}
		
		scalableIV.setImageBitmap(bm);
		
		dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
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
				progressDialog = ProgressDialog.show(PhotoPlayer.this, "", PhotoPlayer.this.getResources().getString(R.string.loading));
				break;
			case Utility.DISMISS_WAITING_DIALOG:
				Log.d(TAG, "dismiss dialog ");
				if (progressDialog != null) progressDialog.dismiss();
				break;
			}
		}
	};
	
	public void changeImage(boolean isNext)
	{
		if (isNext)	// change to next
		{
			if (currentPosition + 1 < photoList.size())
			{
				currentPosition += 1;
				decodeSampledBitmapFromUri(photoList.get(currentPosition).getFilePath(), photoList.get(currentPosition).getFileName());
				attacher.update();
			}
			else
			{
				
			}
		}
		else		// change to preview
		{
			if (currentPosition - 1 < 0)
			{
				currentPosition -= 1;
				decodeSampledBitmapFromUri(photoList.get(currentPosition).getFilePath(), photoList.get(currentPosition).getFileName());
				attacher.update();
			}
			else
			{
				
			}
		}
	}
}
