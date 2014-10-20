package com.example.retailsale.manager.fileinfo;

public interface GetFileInfoListener
{
	void onResult(Boolean isSuccess, GsonFileInfo fileInfo);
}
