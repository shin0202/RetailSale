package com.example.retailsale.manager.dataoption;

import com.google.gson.annotations.SerializedName;

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

    public DataOption(String typeName, int optSerial, String optName, boolean optLock)
    {
        this.typeName = typeName;
        this.optSerial = optSerial;
        this.optName = optName;
        this.optLock = optLock;
    }
    
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
