package com.example.retailsale.manager.addcustomer;

import java.lang.reflect.Field;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.retailsale.util.Utility;
import com.google.gson.annotations.SerializedName;

public class CustomerInfo implements Parcelable
{
    @SerializedName("customerAccount")
    private String customerAccount;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("customerMobile")
    private String customerMobile;
    @SerializedName("customerHome")
    private String customerHome;
    @SerializedName("customerCompany")
    private String customerCompany;
    @SerializedName("customerSex")
    private int customerSex;
    @SerializedName("customerTitle")
    private int customerTitle;
    @SerializedName("customerMail")
    private String customerMail;
    @SerializedName("customerVisitDate")
    private String customerVisitDate;
    @SerializedName("customerInfo")
    private int customerInfo;
    @SerializedName("customerIntroducer")
    private String customerIntroducer;
    @SerializedName("customerJob")
    private int customerJob;
    @SerializedName("customerAge")
    private int customerAge;
    @SerializedName("customerMemo")
    private String customerMemo;
    @SerializedName("customerBirth")
    private String customerBirth;
    @SerializedName("creator")
    private int creator;
    @SerializedName("creatorGroup")
    private int creatorGroup;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("reservationDate")
    private String reservationDate;
    @SerializedName("reservationTime")
    private String reservationTime;
    @SerializedName("reservationWork")
    private String reservationWork;
    @SerializedName("workPostcode")
    private String workPostcode;
    @SerializedName("reservationWorkAlias")
    private String reservationWorkAlias;
    @SerializedName("reservationContact")
    private String reservationContact;
    @SerializedName("contactPostcode")
    private String contactPostcode;
    @SerializedName("reservationSpace")
    private int reservationSpace;
    @SerializedName("reservationStatus")
    private int reservationStatus;
    @SerializedName("reservationUpateTime")
    private String reservationUpateTime;
    @SerializedName("reservationStatusComment")
    private String reservationStatusComment;
    @SerializedName("reservationBudget")
    private int reservationBudget;
    @SerializedName("reservationRepairItem")
    private int reservationRepairItem;
    @SerializedName("reservationArea")
    private int reservationArea;
    
    private int contactCountySelectedPosition;
    private int contactCitySelectedPosition;
    private int workCountySelectedPosition;
    private int workCitySelectedPosition;
    
    private long rowId;
    
    private int reservationYear;
    private int reservationMonth;
    private int reservationDay;
    private int reservationHour;
    private int reservationMinute;

    private int reservationSpacePosition = 0;
    private int reservationStatusPosition = 0;
    private int reservationBudgetPosition = 0;
    
    public CustomerInfo()
    {
    }

    public CustomerInfo(long rowId, String customerAccount, String customerName, String customerMobile, String customerHome,
            String customerCompany, int customerSex, int customerTitle, String customerMail, String customerVisitDate,
            int customerInfo, String customerIntroducer, int customerJob, int customerAge, String customerMemo,
            String customerBirth, int creator, int creatorGroup, String createTime, String reservationDate,
            String reservationTime, String reservationWork, String workPostcode, String reservationWorkAlias,
            String reservationContact, String contactPostcode, int reservationSpace, int reservationStatus,
            String reservationUpateTime, String reservationStatusComment, int reservationBudget,
            int reservationRepairItem, int reservationArea)
    {
        super();
        setCustomerInfo(rowId, customerAccount, customerName, customerMobile, customerHome, customerCompany,
                customerSex, customerTitle, customerMail, customerVisitDate, customerInfo, customerIntroducer,
                customerJob, customerAge, customerMemo, customerBirth, creator, creatorGroup, createTime,
                reservationDate, reservationTime, reservationWork, workPostcode, reservationWorkAlias,
                reservationContact, contactPostcode, reservationSpace, reservationStatus, reservationUpateTime,
                reservationStatusComment, reservationBudget, reservationRepairItem, reservationArea);
    }

    // for Add Fragment
    public CustomerInfo(String customerAccount, String customerName, String customerMobile, String customerHome,
            String customerCompany, int customerSex, int customerTitle, String customerMail, String customerVisitDate,
            int customerInfo, String customerIntroducer, int customerJob, int customerAge, String customerBirth,
            int creator, int creatorGroup, String createTime, int reservationRepairItem, int reservationArea)
    {
        super();
        this.customerAccount = customerAccount;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerHome = customerHome;
        this.customerCompany = customerCompany;
        this.customerSex = customerSex;
        this.customerTitle = customerTitle;
        this.customerMail = customerMail;
        this.customerVisitDate = customerVisitDate;
        this.customerInfo = customerInfo;
        this.customerIntroducer = customerIntroducer;
        this.customerJob = customerJob;
        this.customerAge = customerAge;
        this.customerBirth = customerBirth;
        this.creator = creator;
        this.creatorGroup = creatorGroup;
        this.createTime = createTime;
        this.reservationRepairItem = reservationRepairItem;
        this.reservationArea = reservationArea;
    }

    // for Order Measure
    public CustomerInfo(String reservationDate, String reservationTime, String reservationWork, String workPostcode,
            String reservationWorkAlias, String reservationContact, String contactPostcode, int reservationSpace,
            int reservationStatus, String reservationUpateTime, String reservationStatusComment, int reservationBudget)
    {
        super();
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationWork = reservationWork;
        this.workPostcode = workPostcode;
        this.reservationWorkAlias = reservationWorkAlias;
        this.reservationContact = reservationContact;
        this.contactPostcode = contactPostcode;
        this.reservationSpace = reservationSpace;
        this.reservationStatus = reservationStatus;
        this.reservationUpateTime = reservationUpateTime;
        this.reservationStatusComment = reservationStatusComment;
        this.reservationBudget = reservationBudget;
    }
    
    public void modifyCustomerInfo(String customerAccount, String customerName, String customerMobile,
            String customerHome, String customerCompany, int customerSex, int customerTitle, String customerMail,
            String customerVisitDate, int customerInfo, String customerIntroducer, int customerJob, int customerAge,
            String customerMemo, String customerBirth, int creator, int creatorGroup, String createTime,
            int reservationRepairItem, int reservationArea, int reservationBudget, String reservationContact, String contactPostcode
            , String reservationWork, String workPostcode)
    {
        this.customerAccount = customerAccount;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerHome = customerHome;
        this.customerCompany = customerCompany;
        this.customerSex = customerSex;
        this.customerTitle = customerTitle;
        this.customerMail = customerMail;
        this.customerVisitDate = customerVisitDate;
        this.customerInfo = customerInfo;
        this.customerIntroducer = customerIntroducer;
        this.customerJob = customerJob;
        this.customerAge = customerAge;
        this.customerMemo = customerMemo;
        this.customerBirth = customerBirth;
        this.creator = creator;
        this.creatorGroup = creatorGroup;
        this.createTime = createTime;
        this.reservationRepairItem = reservationRepairItem;
        this.reservationArea = reservationArea;
        this.reservationBudget = reservationBudget;
        this.reservationContact = reservationContact;
        this.contactPostcode = contactPostcode;
        this.reservationWork = reservationWork;
        this.workPostcode = workPostcode;
    }
    
    public void setCustomerInfo(long rowId, String customerAccount, String customerName, String customerMobile,
            String customerHome, String customerCompany, int customerSex, int customerTitle, String customerMail,
            String customerVisitDate, int customerInfo, String customerIntroducer, int customerJob, int customerAge,
            String customerMemo, String customerBirth, int creator, int creatorGroup, String createTime,
            String reservationDate, String reservationTime, String reservationWork, String workPostcode,
            String reservationWorkAlias, String reservationContact, String contactPostcode, int reservationSpace,
            int reservationStatus, String reservationUpateTime, String reservationStatusComment, int reservationBudget,
            int reservationRepairItem, int reservationArea)
    {
        this.rowId = rowId;
        setCustomerInfo(customerAccount, customerName, customerMobile, customerHome, customerCompany, customerSex,
                customerTitle, customerMail, customerVisitDate, customerInfo, customerIntroducer, customerJob,
                customerAge, customerMemo, customerBirth, creator, creatorGroup, createTime, reservationDate,
                reservationTime, reservationWork, workPostcode, reservationWorkAlias, reservationContact,
                contactPostcode, reservationSpace, reservationStatus, reservationUpateTime, reservationStatusComment,
                reservationBudget, reservationRepairItem, reservationArea);
    }
    
    // for edit
    public void setCustomerInfo(String customerAccount, String customerName, String customerMobile,
            String customerHome, String customerCompany, int customerSex, int customerTitle, String customerMail,
            String customerVisitDate, int customerInfo, String customerIntroducer, int customerJob, int customerAge,
            String customerMemo, String customerBirth, int creator, int creatorGroup, String createTime,
            String reservationDate, String reservationTime, String reservationWork, String workPostcode,
            String reservationWorkAlias, String reservationContact, String contactPostcode, int reservationSpace,
            int reservationStatus, String reservationUpateTime, String reservationStatusComment, int reservationBudget,
            int reservationRepairItem, int reservationArea)
    {
        this.customerAccount = customerAccount;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerHome = customerHome;
        this.customerCompany = customerCompany;
        this.customerSex = customerSex;
        this.customerTitle = customerTitle;
        this.customerMail = customerMail;
        this.customerVisitDate = customerVisitDate;
        this.customerInfo = customerInfo;
        this.customerIntroducer = customerIntroducer;
        this.customerJob = customerJob;
        this.customerAge = customerAge;
        this.customerMemo = customerMemo;
        this.customerBirth = customerBirth;
        this.creator = creator;
        this.creatorGroup = creatorGroup;
        this.createTime = createTime;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationWork = reservationWork;
        this.workPostcode = workPostcode;
        this.reservationWorkAlias = reservationWorkAlias;
        this.reservationContact = reservationContact;
        this.contactPostcode = contactPostcode;
        this.reservationSpace = reservationSpace;
        this.reservationStatus = reservationStatus;
        this.reservationUpateTime = reservationUpateTime;
        this.reservationStatusComment = reservationStatusComment;
        this.reservationBudget = reservationBudget;
        this.reservationRepairItem = reservationRepairItem;
        this.reservationArea = reservationArea;
    }
    
    public long getRowId()
    {
        return rowId;
    }

    public void setRowId(long rowId)
    {
        this.rowId = rowId;
    }

    public int getContactCountySelectedPosition()
    {
    
        return contactCountySelectedPosition;
    }

    public void setContactCountySelectedPosition(int contactCountySelectedPosition)
    {
    
        this.contactCountySelectedPosition = contactCountySelectedPosition;
    }

    public int getContactCitySelectedPosition()
    {
    
        return contactCitySelectedPosition;
    }

    public void setContactCitySelectedPosition(int contactCitySelectedPosition)
    {
    
        this.contactCitySelectedPosition = contactCitySelectedPosition;
    }

    public int getWorkCountySelectedPosition()
    {
    
        return workCountySelectedPosition;
    }

    public void setWorkCountySelectedPosition(int workCountySelectedPosition)
    {
    
        this.workCountySelectedPosition = workCountySelectedPosition;
    }

    public int getWorkCitySelectedPosition()
    {
    
        return workCitySelectedPosition;
    }

    public void setWorkCitySelectedPosition(int workCitySelectedPosition)
    {
    
        this.workCitySelectedPosition = workCitySelectedPosition;
    }
    
    public String getWorkPostcode()
    {
        return workPostcode;
    }

    public void setWorkPostcode(String workPostcode)
    {
        this.workPostcode = workPostcode;
    }

    public String getContactPostcode()
    {
        return contactPostcode;
    }

    public void setContactPostcode(String contactPostcode)
    {
        this.contactPostcode = contactPostcode;
    }

    public int getReservationRepairItem()
    {
        return reservationRepairItem;
    }

    public void setReservationRepairItem(int reservationRepairItem)
    {
        this.reservationRepairItem = reservationRepairItem;
    }

    public int getReservationArea()
    {
        return reservationArea;
    }

    public void setReservationArea(int reservationArea)
    {
        this.reservationArea = reservationArea;
    }

    public int getReservationSpacePosition()
    {
        return reservationSpacePosition;
    }

    public void setReservationSpacePosition(int reservationSpacePosition)
    {
        this.reservationSpacePosition = reservationSpacePosition;
    }

    public int getReservationStatusPosition()
    {
        return reservationStatusPosition;
    }

    public void setReservationStatusPosition(int reservationStatusPosition)
    {
        this.reservationStatusPosition = reservationStatusPosition;
    }

    public int getReservationBudgetPosition()
    {
        return reservationBudgetPosition;
    }

    public void setReservationBudgetPosition(int reservationBudgetPosition)
    {
        this.reservationBudgetPosition = reservationBudgetPosition;
    }

    public int getReservationYear()
    {
        return reservationYear;
    }

    public void setReservationYear(int reservationYear)
    {
        this.reservationYear = reservationYear;
    }

    public int getReservationMonth()
    {
        return reservationMonth;
    }

    public void setReservationMonth(int reservationMonth)
    {
        this.reservationMonth = reservationMonth;
    }

    public int getReservationDay()
    {
        return reservationDay;
    }

    public void setReservationDay(int reservationDay)
    {
        this.reservationDay = reservationDay;
    }

    public int getReservationHour()
    {
        return reservationHour;
    }

    public void setReservationHour(int reservationHour)
    {
        this.reservationHour = reservationHour;
    }

    public int getReservationMinute()
    {
        return reservationMinute;
    }

    public void setReservationMinute(int reservationMinute)
    {
        this.reservationMinute = reservationMinute;
    }

    public String getCustomerAccount()
    {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount)
    {
        this.customerAccount = customerAccount;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerMobile()
    {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile)
    {
        this.customerMobile = customerMobile;
    }

    public String getCustomerHome()
    {
        return customerHome;
    }

    public void setCustomerHome(String customerHome)
    {
        this.customerHome = customerHome;
    }

    public String getCustomerCompany()
    {
        return customerCompany;
    }

    public void setCustomerCompany(String customerCompany)
    {
        this.customerCompany = customerCompany;
    }

    public int getCustomerSex()
    {
        return customerSex;
    }

    public void setCustomerSex(int customerSex)
    {
        this.customerSex = customerSex;
    }

    public int getCustomerTitle()
    {
        return customerTitle;
    }

    public void setCustomerTitle(int customerTitle)
    {
        this.customerTitle = customerTitle;
    }

    public String getCustomerMail()
    {
        return customerMail;
    }

    public void setCustomerMail(String customerMail)
    {
        this.customerMail = customerMail;
    }

    public String getCustomerVisitDate()
    {
        return customerVisitDate;
    }

    public void setCustomerVisitDate(String customerVisitDate)
    {
        this.customerVisitDate = customerVisitDate;
    }

    public int getCustomerInfo()
    {
        return customerInfo;
    }

    public void setCustomerInfo(int customerInfo)
    {
        this.customerInfo = customerInfo;
    }

    public String getCustomerIntroducer()
    {
        return customerIntroducer;
    }

    public void setCustomerIntroducer(String customerIntroducer)
    {
        this.customerIntroducer = customerIntroducer;
    }

    public int getCustomerJob()
    {
        return customerJob;
    }

    public void setCustomerJob(int customerJob)
    {
        this.customerJob = customerJob;
    }

    public int getCustomerAge()
    {
        return customerAge;
    }

    public void setCustomerAge(int customerAge)
    {
        this.customerAge = customerAge;
    }

    public String getCustomerMemo()
    {
        return customerMemo;
    }

    public void setCustomerMemo(String customerMemo)
    {
        this.customerMemo = customerMemo;
    }

    public String getCustomerBirth()
    {
        return customerBirth;
    }

    public void setCustomerBirth(String customerBirth)
    {
        this.customerBirth = customerBirth;
    }

    public int getCreator()
    {
        return creator;
    }

    public void setCreator(int creator)
    {
        this.creator = creator;
    }

    public int getCreatorGroup()
    {
        return creatorGroup;
    }

    public void setCreatorGroup(int creatorGroup)
    {
        this.creatorGroup = creatorGroup;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getReservationDate()
    {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate)
    {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime()
    {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime)
    {
        this.reservationTime = reservationTime;
    }

    public String getReservationWork()
    {
        return reservationWork;
    }

    public void setReservationWork(String reservationWork)
    {
        this.reservationWork = reservationWork;
    }

    public String getReservationWorkAlias()
    {
        return reservationWorkAlias;
    }

    public void setReservationWorkAlias(String reservationWorkAlias)
    {
        this.reservationWorkAlias = reservationWorkAlias;
    }

    public String getReservationContact()
    {
        return reservationContact;
    }

    public void setReservationContact(String reservationContact)
    {
        this.reservationContact = reservationContact;
    }

    public int getReservationSpace()
    {
        return reservationSpace;
    }

    public void setReservationSpace(int reservationSpace)
    {
        this.reservationSpace = reservationSpace;
    }

    public int getReservationStatus()
    {
        return reservationStatus;
    }

    public void setReservationStatus(int reservationStatus)
    {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationUpateTime()
    {
        return reservationUpateTime;
    }

    public void setReservationUpateTime(String reservationUpateTime)
    {
        this.reservationUpateTime = reservationUpateTime;
    }

    public String getReservationStatusComment()
    {
        return reservationStatusComment;
    }

    public void setReservationStatusComment(String reservationStatusComment)
    {
        this.reservationStatusComment = reservationStatusComment;
    }

    public int getReservationBudget()
    {
        return reservationBudget;
    }

    public void setReservationBudget(int reservationBudget)
    {
        this.reservationBudget = reservationBudget;
    }

    // ///////////////////////////////// Parcel need add // ////////////////////////////////////////////
    @Override
    public int describeContents()
    {
        return Utility.DEFAULT_ZERO_VALUE;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(customerAccount);
        dest.writeString(customerName);
        dest.writeString(customerMobile);
        dest.writeString(customerHome);
        dest.writeString(customerCompany);
        dest.writeInt(customerSex);
        dest.writeInt(customerTitle);
        dest.writeString(customerMail);
        dest.writeString(customerVisitDate);
        dest.writeInt(customerInfo);
        dest.writeString(customerIntroducer);
        dest.writeInt(customerJob);
        dest.writeInt(customerAge);
        dest.writeString(customerMemo);
        dest.writeString(customerBirth);
        // customer reservation
        dest.writeInt(creator);
        dest.writeInt(creatorGroup);
        dest.writeString(createTime);
        dest.writeString(reservationDate);
        dest.writeString(reservationTime);
        dest.writeString(reservationWork);
        dest.writeString(workPostcode);
        dest.writeString(reservationWorkAlias);
        dest.writeString(reservationContact);
        dest.writeString(contactPostcode);
        dest.writeInt(reservationSpace);
        dest.writeInt(reservationStatus);
        dest.writeString(reservationUpateTime);
        dest.writeString(reservationStatusComment);
        dest.writeInt(reservationBudget);
        dest.writeInt(reservationRepairItem);
        dest.writeInt(reservationArea);
        dest.writeInt(reservationYear);
        dest.writeInt(reservationMonth);
        dest.writeInt(reservationDay);
        dest.writeInt(reservationHour);
        dest.writeInt(reservationMinute);
        dest.writeInt(reservationStatusPosition);
        dest.writeInt(reservationSpacePosition);
        dest.writeInt(reservationBudgetPosition);
        dest.writeInt(contactCountySelectedPosition);
        dest.writeInt(contactCitySelectedPosition);
        dest.writeInt(workCountySelectedPosition);
        dest.writeInt(workCitySelectedPosition);
    }

    public CustomerInfo(Parcel in)
    {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<CustomerInfo> CREATOR = new Parcelable.Creator<CustomerInfo>()
    {
        public CustomerInfo createFromParcel(Parcel in)
        {
            return new CustomerInfo(in);
        }

        @Override
        public CustomerInfo[] newArray(int size)
        {
            return new CustomerInfo[size];
        }
    };

    public void readFromParcel(Parcel in)
    {
        this.customerAccount = in.readString();
        this.customerName = in.readString();
        this.customerMobile = in.readString();
        this.customerHome = in.readString();
        this.customerCompany = in.readString();
        this.customerSex = in.readInt();
        this.customerTitle = in.readInt();
        this.customerMail = in.readString();
        this.customerVisitDate = in.readString();
        this.customerInfo = in.readInt();
        this.customerIntroducer = in.readString();
        this.customerJob = in.readInt();
        this.customerAge = in.readInt();
        this.customerMemo = in.readString();
        this.customerBirth = in.readString();
        // customer reservation
        this.creator = in.readInt();
        this.creatorGroup = in.readInt();
        this.createTime = in.readString();
        this.reservationDate = in.readString();
        this.reservationTime = in.readString();
        this.reservationWork = in.readString();
        this.workPostcode = in.readString();
        this.reservationWorkAlias = in.readString();
        this.reservationContact = in.readString();
        this.contactPostcode = in.readString();
        this.reservationSpace = in.readInt();
        this.reservationStatus = in.readInt();
        this.reservationUpateTime = in.readString();
        this.reservationStatusComment = in.readString();
        this.reservationBudget = in.readInt();
        this.reservationRepairItem = in.readInt();
        this.reservationArea = in.readInt();
        this.reservationYear = in.readInt();
        this.reservationMonth = in.readInt();
        this.reservationDay = in.readInt();
        this.reservationHour = in.readInt();
        this.reservationMinute = in.readInt();
        this.reservationStatusPosition = in.readInt();
        this.reservationSpacePosition = in.readInt();
        this.reservationBudgetPosition = in.readInt();
        
        this.contactCountySelectedPosition = in.readInt();
        this.contactCitySelectedPosition = in.readInt();
        this.workCountySelectedPosition = in.readInt();
        this.workCitySelectedPosition = in.readInt();
    }

    // ///////////////////////////////// Parcel need add// ////////////////////////////////////////////
    
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
}
