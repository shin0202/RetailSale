package com.example.retailsale.fragment;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retailsale.R;
import com.example.retailsale.manager.fileinfo.LocalFileInfo;

public class PhotosAdapterView extends BaseAdapter
{
    public static final int BASE_INDEX = 1000;
    public static final int BROWSER_TAB = 0;
    public static final int SYNC_TAB = 1;
    private List<LocalFileInfo> photoList;
    private int selectdTab;

    // Views
    private LayoutInflater layoutInflater;
    private ViewTag viewTag;

    public PhotosAdapterView(Context context, List<LocalFileInfo> photoList, int selectdTab)
    {
        this.photoList = photoList;
        this.selectdTab = selectdTab;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return photoList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.cell_of_browser_tab, null);
            viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.browser_photo),
                    (TextView) convertView.findViewById(R.id.photo_name));
            
            switch (selectdTab) {
            case BROWSER_TAB:
                viewTag.showPhoto.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            case SYNC_TAB:
                viewTag.showPhoto.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
                break;
            }
            
            convertView.setTag(viewTag);
        }
        else
        {
            viewTag = (ViewTag) convertView.getTag();
        }
        convertView.setId(BASE_INDEX + position);
        viewTag.showName.setText(photoList.get(position).getFileName());
        // to show img
        // viewTag.showPhoto.setImageBitmap(bm);
        return convertView;
    }

    class ViewTag
    {
        ImageView showPhoto;
        TextView showName;

        public ViewTag(ImageView showPhoto, TextView showName)
        {
            this.showPhoto = showPhoto;
            this.showName = showName;
        }
    }
}
