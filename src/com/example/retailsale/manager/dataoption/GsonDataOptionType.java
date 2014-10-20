package com.example.retailsale.manager.dataoption;

import com.google.gson.annotations.SerializedName;

public class GsonDataOptionType
{
	@SerializedName("typeSerial")
	private int typeSerial;
	
	@SerializedName("typeName")
	private String typeName;
	
	public GsonDataOptionType(int typeSerial, String typeName) {
	    this.typeSerial = typeSerial;
	    this.typeName = typeName;
	}
	
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
