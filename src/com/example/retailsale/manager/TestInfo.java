package com.example.retailsale.manager;

import java.lang.reflect.Field;

import com.google.gson.annotations.SerializedName;

public class TestInfo
{

	@SerializedName("id")
	private String id;
	
	@SerializedName("orgname")
	private String orgName;

	@SerializedName("groupname")
	private String groupName;

	@SerializedName("title")
	private String title;
	
	@SerializedName("filepath")
	private String filePath;
	
	@SerializedName("created")
	private String created;

	public String getOrgName()
	{

		return orgName;
	}

	public void setOrgName(String orgName)
	{

		this.orgName = orgName;
	}

	public String getGroupName()
	{

		return groupName;
	}

	public void setGroupName(String groupName)
	{

		this.groupName = groupName;
	}

	public String getCreated()
	{

		return created;
	}

	public void setCreated(String created)
	{

		this.created = created;
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

	public String getFilePath()
	{
	
		return filePath;
	}

	public void setFilePath(String filePath)
	{
	
		this.filePath = filePath;
	}

	public String getTitle()
	{
	
		return title;
	}

	public void setTitle(String title)
	{
	
		this.title = title;
	}

	public String getID()
	{
	
		return id;
	}

	public void setID(String id)
	{
	
		this.id = id; 
	}

}