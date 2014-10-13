package com.example.retailsale.manager;

public class LocalFileInfo {
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
}
