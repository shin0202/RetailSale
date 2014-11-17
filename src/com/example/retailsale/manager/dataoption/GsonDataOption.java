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
}
