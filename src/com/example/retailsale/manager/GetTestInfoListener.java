package com.example.retailsale.manager;

import java.util.ArrayList;


public interface GetTestInfoListener
{
	void onResult(Boolean isSuccess,ArrayList<TestInfo> testInfos);
}
