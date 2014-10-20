package com.example.retailsale.manager.dataoption;

import com.google.gson.annotations.SerializedName;

public class GsonDataOption
{
	@SerializedName("optType")
	private int optType;
	
	@SerializedName("optSerial")
	private int optSerial;

	@SerializedName("optName")
	private String optName;

	@SerializedName("optLock")
	private String optLock;
	
	public int getOptType()
	{

		return optType;
	}

	public void setOptType(int optType)
	{

		this.optType = optType;
	}
	
	public int getOptSerial()
	{

		return optSerial;
	}

	public void setOptSerial(int optSerial)
	{

		this.optSerial = optSerial;
	}
	
	public String getOptName()
	{

		return optName;
	}

	public void setOptName(String optName)
	{

		this.optName = optName;
	}
	
	public String getOptLock()
	{

		return optLock;
	}

	public void setOptLock(String optLock)
	{

		this.optLock = optLock;
	}
}
