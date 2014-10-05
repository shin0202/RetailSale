package com.example.retailsale.manager;

import com.google.gson.annotations.SerializedName;

public class ServiceInfo
{
	@SerializedName("serviceSerial")
	private int serviceSerial;
	
	@SerializedName("serviceName")
	private String serviceName;

	@SerializedName("serviceType")
	private int serviceType;

	@SerializedName("servicePath")
	private String servicePath;
	
	@SerializedName("serviceEnable")
	private int serviceEnable;
	
	@SerializedName("lastTime")
	private String lastTime;
	
	public int getServiceSerial()
	{

		return serviceSerial;
	}

	public void setServiceSerial(int serviceSerial)
	{

		this.serviceSerial = serviceSerial;
	}
	
	public String getServiceName()
	{

		return serviceName;
	}

	public void setServiceName(String serviceName)
	{

		this.serviceName = serviceName;
	}
	
	public int getServiceType()
	{

		return serviceType;
	}

	public void setServiceType(int serviceType)
	{

		this.serviceType = serviceType;
	}
	
	public String getServicePath()
	{

		return servicePath;
	}

	public void setServicePath(String servicePath)
	{

		this.servicePath = servicePath;
	}
	
	public int getServiceEnable()
	{

		return serviceEnable;
	}

	public void setServiceEnable(int serviceEnable)
	{

		this.serviceEnable = serviceEnable;
	}
	
	public String getLastTime()
	{

		return lastTime;
	}

	public void setLastTime(String lastTime)
	{

		this.lastTime = lastTime;
	}
}
