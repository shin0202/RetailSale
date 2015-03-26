package com.example.retailsale.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.retailsale.R;
import com.example.retailsale.manager.addcustomer.CustomerInfo;

public class NeedUploadAdapter extends BaseAdapter
{
    private static final int BASE_INDEX = 1000;
    private List<CustomerInfo> customerList;
    private Context context;
    private ViewTag viewTag;

    public NeedUploadAdapter(Context context, List<CustomerInfo> customerList)
    {
        this.context = context;
        this.customerList = customerList;
    }

    @Override
    public int getCount()
    {
        return customerList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return customerList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.cell_of_fragment_need_upload, null);
            viewTag = new ViewTag((TextView) convertView.findViewById(R.id.need_upload_customer_name));
            convertView.setTag(viewTag);
        }
        else
        {
            viewTag = (ViewTag) convertView.getTag();
        }

        convertView.setId(BASE_INDEX + position);

        if (position < customerList.size())
        {
            viewTag.customerName.setText(customerList.get(position).getCustomerName());
        }

        return convertView;
    }

    class ViewTag
    {
        TextView customerName;

        public ViewTag(TextView customerName)
        {
            this.customerName = customerName;
        }
    }

}
