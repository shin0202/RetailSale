package com.example.retailsale.manager;

import com.google.gson.annotations.SerializedName;

public class FileInfo
{
	@SerializedName("optType")
	private String fileName;
	
	@SerializedName("filePath")
	private int filePath;

	@SerializedName("fileData")
	private String fileData;

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public int getFilePath()
	{
		return filePath;
	}

	public void setFilePath(int filePath)
	{
		this.filePath = filePath;
	}

	public String getFileData()
	{
		return fileData;
	}

	public void setFileData(String fileData)
	{
		this.fileData = fileData;
	}
}
