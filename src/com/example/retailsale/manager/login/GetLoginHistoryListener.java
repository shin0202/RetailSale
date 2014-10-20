package com.example.retailsale.manager.login;

import java.util.ArrayList;

public interface GetLoginHistoryListener
{
	void onResult(Boolean isSuccess,ArrayList<GsonLoginHistory> loginHistories);
}
