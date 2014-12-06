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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsale.fragment.BrowserFragment;
import com.example.retailsale.manager.fileinfo.LocalFileInfo;
import com.example.retailsale.photoview.PhotoViewAttacher;
import com.example.retailsale.util.Utility;

public class PhotoPlayer extends Activity implements OnClickListener
{
    private static final String TAG = "PhotoPlayer";

    // views
    private LinearLayout photosLayout;
    private ImageView scalableIV;
    private TextView displayPositionTV;
    private Button backBtn;
    PhotoViewAttacher attacher;
    private ProgressDialog progressDialog;
    private Bitmap bm;

    private int showAlbumCount = 0;
    private int currentPosition = 0;
    private List<LocalFileInfo> photoList = new ArrayList<LocalFileInfo>();
    private boolean hideController = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photoplayer);

        findViews();

        getBundle();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        hideController = false;

        backBtn.setVisibility(View.VISIBLE);
        photosLayout.setVisibility(View.VISIBLE);
        displayPositionTV.setVisibility(View.VISIBLE);

        setPhotosLayout();

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        if (attacher == null) attacher = new PhotoViewAttacher(scalableIV, PhotoPlayer.this);

        setInitImage();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        hideController = false;

        showAlbumCount = 0;

        removeAllAlbums();

        scalableIV.setImageBitmap(null);

        recycleBitmap();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (attacher != null)
        {
            attacher.cleanup();
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.photo_player_cancel_btn:
            this.finish();
            break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return super.onKeyDown(keyCode, event);
    }
    

    private void findViews()
    {
        scalableIV = (ImageView) findViewById(R.id.photo_player_display);

//      scalableIV.setFit(true);
//      scalableIV.setMaxZoom(10);
//      scalableIV.setMinZoom(1);

        photosLayout = (LinearLayout) findViewById(R.id.photots);
        backBtn = (Button) findViewById(R.id.photo_player_cancel_btn);
        backBtn.setOnClickListener(this);
        
        displayPositionTV = (TextView) findViewById(R.id.photo_player_display_position);
    }

    private void getBundle()
    {
        Bundle bundle = this.getIntent().getExtras();
        currentPosition = 0;

        if (bundle != null)
        {
            photoList = bundle.getParcelableArrayList(BrowserFragment.FILE_LIST);
            currentPosition = bundle.getInt(BrowserFragment.FILE_POSITION);
            Log.d(TAG, "currentPosition is " + currentPosition);
            if (photoList != null)
            {
                for (int i = 0; i < photoList.size(); i++)
                {
                    Log.d(TAG, "Get data showAlbumCount : " + i + " name: " + photoList.get(i).getFileName()
                            + " path: " + photoList.get(i).getFilePath() + " type: " + photoList.get(i).getFileType());
                }
                
                showPosition();
            }
            else
            {
                Log.d(TAG, "It is no data from BrowserFragment(list is null)!");
            }
        }
        else
        {
            Log.d(TAG, "It is no data from BrowserFragment(bundle is null)!");
        }
    }
    
    private void showPosition()
    {
        displayPositionTV.setText((currentPosition + 1) + "/" + photoList.size());
    }

    private void setPhotosLayout()
    {
        for (int i = 0; i < photoList.size(); i++)
        {
            photosLayout.addView(insertPhoto(photoList.get(i).getFileName(), photoList.get(i).getFilePath()));
        }
    }

    private void setInitImage()
    {
        if (currentPosition < photoList.size())
        {
            decodeSampledBitmapFromUri(photoList.get(currentPosition).getFilePath(), photoList.get(currentPosition)
                    .getFileName());
        }
        else
        {
            Log.d(TAG, "showAlbumCount over to photoList size");
        }
    }

    private View insertPhoto(String name, String path)
    {
        Log.d(TAG, "name : " + name + " path : " + path);
        StringBuilder thumbFilePath = new StringBuilder().append(path.replace(name, Utility.THUMB_PATH)).append(name.replace(Utility.REPLACE_TXT_STRING, ""));

        Log.d(TAG, "thumbFilePath is " + thumbFilePath.toString());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        
        byte[] img = readThumbnail(thumbFilePath.toString());
        Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length, options);

        int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
        int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);
        int txtSize = (int) getResources().getDimension(R.dimen.scrollview_txt_size);

        LinearLayout layout = new LinearLayout(PhotoPlayer.this);
        layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        ImageView imageView = new ImageView(PhotoPlayer.this);
        imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
//        imageView.setImageDrawable(PhotoPlayer.this.getResources().getDrawable(R.drawable.picture));
        imageView.setTag(showAlbumCount);
        
        showAlbumCount++;
        imageView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                int position = (Integer) v.getTag();
                Log.d(TAG, "position : " + position);
//                scalableIV.initStanScalableImageView(PhotoPlayer.this);

                if (photoList != null && photoList.size() > 0 && position < photoList.size())
                {
                    currentPosition = position;
                    if (decodeSampledBitmapFromUri(photoList.get(position).getFilePath(), photoList.get(position)
                            .getFileName()))
                        // If you later call scalableIV.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
                        attacher.update();
                }
                showPosition();
            }
        });

        TextView textView = new TextView(PhotoPlayer.this);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(txtSize);
        textView.setTextColor(Color.BLACK);
        textView.setText(name.replace(Utility.REPLACE_STRING, Utility.SPACE_STRING));

        layout.addView(imageView);
        layout.addView(textView);

        return layout;
    }

    private void removeAllAlbums()
    {
        if (photosLayout != null)
        {
            photosLayout.removeAllViews();
        }
    }

    private void recycleBitmap()
    {
        if (bm != null)
        {
            bm.recycle();
            bm = null;
        }
    }

    @SuppressWarnings("unused")
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

    private boolean decodeSampledBitmapFromUri(final String path, final String fileName)
    {

        try
        {
            dialogHandler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            // Bitmap bm = null;
            scalableIV.setImageBitmap(null);

            recycleBitmap();

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();

            // bm = BitmapFactory.decodeFile(path, options);

            // 5. to read file and remove md5
            String readContent = Utility.readFile(path);
            // Log.d(TAG, "readContent is " + readContent);
            Log.d(TAG, "*********************************************************** ");
            String realFileName = fileName
                    .replace(Utility.REPLACE_TXT_STRING, Utility.SPACE_STRING);
            // Log.d(TAG, "realFileName is " + realFileName);
            Log.d(TAG, "*********************************************************** ");
            String md5String = Utility.generateMD5String(realFileName);
            // Log.d(TAG, "md5String is " + md5String);
            Log.d(TAG, "*********************************************************** ");

            String realData = readContent.replace(md5String, Utility.SPACE_STRING);
            // Log.d(TAG, "realData is " + realData);
            Log.d(TAG, "*********************************************************** ");
            // 6. decode Base64 to byte[]

            byte[] photo = Utility.decodeBase64(realData);

            bm = BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            bm = null;
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
            bm = null;
        }

        scalableIV.setImageBitmap(bm);

        dialogHandler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
        
        if (bm == null)
        {
            dialogHandler.sendEmptyMessage(Utility.FAILED);
            return false;
        }
        else
        {
            dialogHandler.sendEmptyMessage(Utility.SUCCESS);
            return true;
        }
    }
    
    private byte[] readThumbnail(String path)
    {
        byte[] thumbnail = null;
        
        // 1. to read file
        String readContent = Utility.readFile(path);
        
        // 2. to decode
        thumbnail = Utility.decodeBase64(readContent);
        
        return thumbnail;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            if (width > height)
            {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            }
            else
            {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
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
                progressDialog = ProgressDialog.show(PhotoPlayer.this, Utility.SPACE_STRING, PhotoPlayer.this
                        .getResources().getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            case Utility.SUCCESS:
                Log.d(TAG, getResources().getString(R.string.decode_success));
//                showToast(getResources().getString(R.string.decode_success));
                break;
            case Utility.FAILED:
                Log.d(TAG, getResources().getString(R.string.decode_failed));
//                showToast(getResources().getString(R.string.decode_failed));
                break;
            }
        }
    };

    public void changeImage(boolean isNext)
    {
        if (isNext) // change to next
        {
            if (currentPosition < photoList.size() - 1)
            {
                currentPosition += 1;
                if(decodeSampledBitmapFromUri(photoList.get(currentPosition).getFilePath(), photoList.get(currentPosition)
                        .getFileName()))
                    attacher.update();
            }
            else
            {

            }
        }
        else
        // change to preview
        {
            if (currentPosition > 0)
            {
                currentPosition -= 1;
                if (decodeSampledBitmapFromUri(photoList.get(currentPosition).getFilePath(), photoList.get(currentPosition)
                        .getFileName()))
                    attacher.update();
            }
            else
            {

            }
        }
        showPosition();
    }

    public void handleController()
    {
        if (hideController)
        {
            backBtn.setVisibility(View.VISIBLE);
            photosLayout.setVisibility(View.VISIBLE);
            displayPositionTV.setVisibility(View.VISIBLE);
        }
        else
        {
            backBtn.setVisibility(View.INVISIBLE);
            photosLayout.setVisibility(View.INVISIBLE);
            displayPositionTV.setVisibility(View.INVISIBLE);
        }
        hideController = !hideController;
    }
    
    @SuppressWarnings("unused")
    private void showToast(String showString)
    {
        Toast.makeText(this, showString, Toast.LENGTH_SHORT).show();
    }
}
