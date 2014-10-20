package com.example.retailsale.manager.login;

public interface GetLoginListener
{
	void onResult(Boolean isSuccess, GsonLoginInfo userInfo);
}
