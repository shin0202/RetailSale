package com.example.retailsale.manager.dataoption;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.retailsale.R;

public class OptionAdapter extends BaseAdapter
{
    private static final String TAG = "OptionAdapter";
    private static final int BASE_INDEX = 1000;
    private List<DataOption> optionList;
    private Context context;
    private ViewTag viewTag;

    public OptionAdapter(Context context, List<DataOption> optionList)
    {
        this.context = context;
        this.optionList = optionList;
    }

    @Override
    public int getCount()
    {
        return optionList.size();
    }

    @Override
    public Object getItem(int position)
    {
        if (position > optionList.size())
            return optionList.get(position);
        else
            return null;
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

        if (position < optionList.size())
        {
            viewTag.itemName.setText(optionList.get(position).getOptName());
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
