package com.example.retailsale.manager;

import java.util.ArrayList;

public interface GetLoginHistoryListener
{
	void onResult(Boolean isSuccess,ArrayList<LoginHistory> loginHistories);
}
