package com.example.retailsale.manager;

import java.lang.reflect.Field;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserInfo
{
	@SerializedName("odata.metadata")
	private String metadata;
	@SerializedName("value")
	private List<UserData> value;

	public String getMetadata()
	{
		return metadata;
	}

	public void setMetadata(String metadata)
	{
		this.metadata = metadata;
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
	
	public List<UserData> getValue()
	{
		return value;
	}

	public void setValue(List<UserData> value)
	{
		this.value = value;
	}

	public class UserData
	{
		@SerializedName("userSerial")
		private String userSerial;
		
		@SerializedName("userGroup")
		private String userGroup;
		
		@SerializedName("loginKey")
		private String loginKey;
		
		@SerializedName("message")
		private String message;
		
		@SerializedName("flag")
		private String flag;

		public String getUserSerial()
		{
			return userSerial;
		}

		public void setUserSerial(String userSerial)
		{
			this.userSerial = userSerial;
		}

		public String getUserGroup()
		{
			return userGroup;
		}

		public void setUserGroup(String userGroup)
		{
			this.userGroup = userGroup;
		}

		public String getLoginKey()
		{
			return loginKey;
		}

		public void setLoginKey(String loginKey)
		{
			this.loginKey = loginKey;
		}

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public String getFlag()
		{
			return flag;
		}

		public void setFlag(String flag)
		{
			this.flag = flag;
		}
	}
}
