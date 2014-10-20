package com.example.retailsale.manager.fileinfo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GsonFileInfo
{
	@SerializedName("odata.metadata")
	private String metadata;
	@SerializedName("value")
	private List<FileInfo> value;
	
	public String getMetadata()
	{
		return metadata;
	}

	public void setMetadata(String metadata)
	{
		this.metadata = metadata;
	}

	public List<FileInfo> getValue()
	{
		return value;
	}

	public void setValue(List<FileInfo> value)
	{
		this.value = value;
	}

	public class FileInfo
	{
		@SerializedName("path")
		private String path;
		@SerializedName("fileName")
		private String fileName;
		@SerializedName("fileStream")
		private String fileStream;

		public String getFileName()
		{
			return fileName;
		}

		public void setFileName(String fileName)
		{
			this.fileName = fileName;
		}

		public String getPath()
		{
			return path;
		}

		public void setPath(String path)
		{
			this.path = path;
		}

		public String getFileStream()
		{
			return fileStream;
		}

		public void setFileStream(String fileStream)
		{
			this.fileStream = fileStream;
		}
	}
}
