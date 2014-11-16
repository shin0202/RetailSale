package com.example.retailsale.manager.userlist;

import java.lang.reflect.Field;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GsonUserByGroup
{
    @SerializedName("odata.metadata")
    private String metadata;
    @SerializedName("value")
    private List<UserDataForList> value;

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
    
    public List<UserDataForList> getValue()
    {
        return value;
    }

    public void setValue(List<UserDataForList> value)
    {
        this.value = value;
    }
}
