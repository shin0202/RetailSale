package com.example.retailsale.manager;

import java.util.ArrayList;

public interface GetServiceContentListener
{
	void onResult(Boolean isSuccess,ArrayList<ServiceInfo> serviceContents);
}
