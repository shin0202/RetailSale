package com.example.retailsale.adapter;

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
import com.example.retailsale.util.Utility;

public class PhotoAdapter extends BaseAdapter
{
    public static final int BASE_INDEX = 1000;
    public static final int BROWSER_TAB = 0;
    public static final int SYNC_TAB = 1;
    private List<LocalFileInfo> photoList;
    private int selectdTab;

    // Views
    private LayoutInflater layoutInflater;
    private ViewTag viewTag;

    public PhotoAdapter(Context context, List<LocalFileInfo> photoList, int selectdTab)
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
            switch (selectdTab)
            {
            case BROWSER_TAB:
                convertView = layoutInflater.inflate(R.layout.cell_of_fragment_browser, null);
                viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.browser_photo),
                        (TextView) convertView.findViewById(R.id.photo_name));
                break;
            case SYNC_TAB:
                convertView = layoutInflater.inflate(R.layout.cell_of_fragment_sync_download_browser, null);
                viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.download_browser_photo),
                        (TextView) convertView.findViewById(R.id.download_browser_name));
                break;
            }

            convertView.setTag(viewTag);
        }
        else
        {
            viewTag = (ViewTag) convertView.getTag();
        }
        convertView.setId(BASE_INDEX + position);
        viewTag.showName.setText(photoList.get(position).getFileName()
                .replace(Utility.REPLACE_STRING, Utility.SPACE_STRING)
                .replace(Utility.REPLACE_B_STRING, Utility.SPACE_STRING)
                .replace(Utility.REPLACE_TXT_STRING, Utility.SPACE_STRING));
        // to show img
        // viewTag.showPhoto.setImageBitmap(bm);
        if (selectdTab == BROWSER_TAB)
        {
            if (photoList.get(position).getFileType() == LocalFileInfo.SELECTED_DIR) // is
                                                                                     // dir
            {
                viewTag.showPhoto.setImageResource(R.drawable.file2);
            }
            else
            {
                viewTag.showPhoto.setImageResource(R.drawable.picture);
            }
        }
        else
        {
            viewTag.showPhoto.setImageResource(R.drawable.download);
        }
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
