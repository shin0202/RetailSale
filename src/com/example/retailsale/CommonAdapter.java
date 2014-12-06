package com.example.retailsale;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommonAdapter extends BaseAdapter
{
    private static final int BASE_INDEX = 1000;
    private List<String> birthList;
    private Context context;
    private ViewTag viewTag;

    public CommonAdapter(Context context, List<String> birthList)
    {
        this.context = context;
        this.birthList = birthList;
    }

    @Override
    public int getCount()
    {
        return birthList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return birthList.get(position);
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
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.option_layout, null);
            viewTag = new ViewTag((TextView) convertView.findViewById(R.id.option_text));
            convertView.setTag(viewTag);
        }
        else
        {
            viewTag = (ViewTag) convertView.getTag();
        }

        convertView.setId(BASE_INDEX + position);

        if (position < birthList.size())
        {
            viewTag.itemName.setText(birthList.get(position));
        }

        return convertView;
    }

    class ViewTag
    {
        TextView itemName;

        public ViewTag(TextView itemName)
        {
            this.itemName = itemName;
        }
    }
}
