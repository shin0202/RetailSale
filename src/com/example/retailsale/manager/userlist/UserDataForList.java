package com.example.retailsale.manager.userlist;

import com.google.gson.annotations.SerializedName;

public class UserDataForList
{
    @SerializedName("userSerial")
    private int userSerial;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("userGroup")
    private int userGroup;
    
    @SerializedName("userType")
    private int userType;
    
    @SerializedName("userGroup_Nm")
    private String userGroupNm;
    
    @SerializedName("userType_Nm")
    private String userTypeNm;
    
    public UserDataForList(int userSerial, String userName, int userGroup, int userType, String userGroupNm, String userTypeNm)
    {
        this.userSerial = userSerial;
        this.userName = userName;
        this.userGroup = userGroup;
        this.userType = userType;
        this.userGroupNm = userGroupNm;
        this.userTypeNm = userTypeNm;
    }

    public int getUserSerial()
    {
        return userSerial;
    }

    public void setUserSerial(int userSerial)
    {
        this.userSerial = userSerial;
    }
    
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getUserGroup()
    {
        return userGroup;
    }

    public void setUserGroup(int userGroup)
    {
        this.userGroup = userGroup;
    }

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public String getUserGroupNm()
    {
        return userGroupNm;
    }

    public void setFlag(String userGroupNm)
    {
        this.userGroupNm = userGroupNm;
    }
    
    public String getUserTypeNm()
    {
        return userTypeNm;
    }

    public void setUserTypeNm(String userTypeNm)
    {
        this.userTypeNm = userTypeNm;
    }
}
