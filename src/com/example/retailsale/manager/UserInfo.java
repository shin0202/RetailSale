package com.example.retailsale.manager;

import java.lang.reflect.Field;

import com.google.gson.annotations.SerializedName;

public class UserInfo
{
	@SerializedName("userSerial")
	private int userSerial;
	@SerializedName("userAccount")
	private String userAccount;
	@SerializedName("userName")
	private String userName;
	@SerializedName("userMobile")
	private String userMobile;
	@SerializedName("userKey")
	private String userKey;
	@SerializedName("userGroup")
	private int userGroup;
	@SerializedName("userType")
	private int userType;
	@SerializedName("lastTime")
	private String lastTime;
	
	// no userSerial
	public UserInfo(String userAccount, String userName, String userMobile, String userKey,
			int userGroup, int userType, String lastTime)
	{
		setUserAccount(userAccount);
		setUserName(userName);
		setUserMobile(userMobile);
		setUserKey(userKey);
		setUserGroup(userGroup);
		setUserType(userType);
		setLastTime(lastTime);
	}

	public UserInfo(int userSerial, String userAccount, String userName, String userMobile, String userKey,
			int userGroup, int userType, String lastTime)
	{
		setUserSerial(userSerial);
		setUserAccount(userAccount);
		setUserName(userName);
		setUserMobile(userMobile);
		setUserKey(userKey);
		setUserGroup(userGroup);
		setUserType(userType);
		setLastTime(lastTime);
	}

	public int getUserSerial()
	{
		return userSerial;
	}

	public void setUserSerial(int userSerial)
	{
		this.userSerial = userSerial;
	}
	
	public String getUserAccount()
	{
		return userAccount;
	}

	public void setUserAccount(String userAccount)
	{
		this.userAccount = userAccount;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserMobile()
	{
		return userMobile;
	}

	public void setUserMobile(String userMobile)
	{
		this.userMobile = userMobile;
	}

	public String getUserKey()
	{
		return userKey;
	}

	public void setUserKey(String userKey)
	{
		this.userKey = userKey;
	}

	public int getUserGroup()
	{
		return userGroup;
	}

	public void setUserGroup(int userGroup)
	{
		this.userGroup = userGroup;
	}
	
	public int getUserType()
	{
		return userType;
	}

	public void setUserType(int userType)
	{
		this.userType = userType;
	}

	public String getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(String lastTime)
	{
		this.lastTime = lastTime;
	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);
		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();
		// print field names paired with their values
		for (Field field : fields)
		{
			result.append("  ");
			try
			{
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			}
			catch (IllegalAccessException ex)
			{
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}
}
