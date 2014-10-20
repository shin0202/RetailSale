package com.example.retailsale.manager.service;

import java.util.ArrayList;

public interface GetServiceContentListener
{
	void onResult(Boolean isSuccess,ArrayList<GsonServiceInfo> serviceContents);
}
