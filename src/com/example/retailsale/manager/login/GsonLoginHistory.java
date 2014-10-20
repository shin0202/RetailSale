package com.example.retailsale.manager.login;

import com.google.gson.annotations.SerializedName;

public class GsonLoginHistory
{
	@SerializedName("loginSerial")
	private int loginSerial;
	
	@SerializedName("userSerial")
	private int userSerial;

	@SerializedName("loginKey")
	private String loginKey;

	@SerializedName("loginTime")
	private String loginTime;
	
	@SerializedName("loginExpire")
	private String loginExpire;
	
	@SerializedName("lastTime")
	private String lastTime;
	
	public int getLoginSerial()
	{

		return loginSerial;
	}

	public void setLoginSerial(int loginSerial)
	{

		this.loginSerial = loginSerial;
	}
	
	public int getUserSerial()
	{

		return userSerial;
	}

	public void setUserSerial(int userSerial)
	{

		this.userSerial = userSerial;
	}
	
	public String getLoginKey()
	{

		return loginKey;
	}

	public void setLoginKey(String loginKey)
	{

		this.loginKey = loginKey;
	}
	
	public String getLoginTime()
	{

		return loginTime;
	}

	public void setLoginTime(String loginTime)
	{

		this.loginTime = loginTime;
	}
	
	public String getLoginExpire()
	{

		return loginExpire;
	}

	public void setLoginExpire(String loginExpire)
	{

		this.loginExpire = loginExpire;
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
