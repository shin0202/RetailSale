package com.example.retailsale.manager.folderinfo;

import com.google.gson.annotations.SerializedName;

public class GsonFolderInfo
{
	@SerializedName("odata.metadata")
	private String metadata;
	@SerializedName("value")
	private String value;
	public String getMetadata()
	{
		return metadata;
	}
	public void setMetadata(String metadata)
	{
		this.metadata = metadata;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
}
