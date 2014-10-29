package com.example.retailsale.manager.fileinfo;

import java.lang.reflect.Field;

import com.example.retailsale.util.Utility;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalFileInfo implements Parcelable {
    public static final int SELECTED_FILE = 0;
    public static final int SELECTED_DIR = 1;

    private String fileName;
    private String filePath;
    private int fileType;
    
    public LocalFileInfo (String fileName, String filePath, int fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }
    
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public int getFileType() {
        return fileType;
    }
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
    
    /////////////////////////////////// Parcel need add
    @Override
    public int describeContents()
    {
        return Utility.DEFAULT_ZERO_VALUE;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeInt(fileType);
    }
    
    public static final Parcelable.Creator<LocalFileInfo> CREATOR = new Parcelable.Creator<LocalFileInfo>()
    {
        public LocalFileInfo createFromParcel(Parcel in)
        {
            return new LocalFileInfo(in);
        }

        @Override
        public LocalFileInfo[] newArray(int size)
        {
            return new LocalFileInfo[size];
        }
    };
    
    public LocalFileInfo(Parcel in) {
        super();
        readFromParcel(in);
    }
    
    public void readFromParcel(Parcel in)
    {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.fileType = in.readInt();
    }
    /////////////////////////////////// Parcel need add
    
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
