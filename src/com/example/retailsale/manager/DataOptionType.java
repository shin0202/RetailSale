package com.example.retailsale.manager;

import com.google.gson.annotations.SerializedName;

public class DataOptionType
{
	@SerializedName("typeSerial")
	private int typeSerial;
	
	@SerializedName("typeName")
	private String typeName;
	
	public int getTypeSerial()
	{

		return typeSerial;
	}

	public void setTypeSerial(int typeSerial)
	{

		this.typeSerial = typeSerial;
	}
	
	public String getTypeName()
	{

		return typeName;
	}

	public void setTypeName(String typeName)
	{

		this.typeName = typeName;
	}
}
