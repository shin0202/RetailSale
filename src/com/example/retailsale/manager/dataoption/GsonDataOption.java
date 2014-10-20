package com.example.retailsale.manager.dataoption;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GsonDataOption
{
	@SerializedName("odata.metadata")
	private String metadata;
	@SerializedName("value")
	private List<DataOption> value;

	public String getMetadata()
	{
		return metadata;
	}

	public void setMetadata(String metadata)
	{
		this.metadata = metadata;
	}

	public List<DataOption> getValue()
	{
		return value;
	}

	public void setValue(List<DataOption> value)
	{
		this.value = value;
	}

	public class DataOption
	{
		@SerializedName("typeName")
		private String typeName;
		@SerializedName("optSerial")
		private int optSerial;
		@SerializedName("optName")
		private String optName;
		@SerializedName("optLock")
		private boolean optLock;

		public String getTypeName()
		{
			return typeName;
		}

		public void setTypeName(String typeName)
		{
			this.typeName = typeName;
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

		public boolean getOptLock()
		{
			return optLock;
		}

		public void setOptLock(boolean optLock)
		{
			this.optLock = optLock;
		}
	}
}
