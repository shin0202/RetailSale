package com.example.retailsale.manager.folderinfo;

import java.util.ArrayList;
import java.util.List;

public class LocalFolderInfo
{
    private String folderId;
    private String folderPath;
    private List<FileObject> fileList;
    
    public LocalFolderInfo(String folderId, String folderPath) {
        this.folderId = folderId;
        this.folderPath = folderPath;
        
        fileList = new ArrayList<FileObject>();
    }
    
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
            
    public void addFileToList(String folderId, String fileId, String fileName) {
        if (fileList == null)
            fileList = new ArrayList<FileObject>();
        
        fileList.add(new FileObject(folderId, fileId, fileName));
    }
    
    public int getFileListSize() {
        if (fileList != null)
            return fileList.size();
        else
            return 0;
    }
    
    public String getFileFolderId(int index) {
        if (fileList != null)
            return fileList.get(index).getFolderId();
        else
            return "";
    }
    
    public String getFileId(int index) {
        if (fileList != null)
            return fileList.get(index).getFileId();
        else
            return "";
    }
    
    public String getFileName(int index) {
        if (fileList != null)
            return fileList.get(index).getFileName();
        else
            return "";
    }
    
    private class FileObject
    {
        private String folderId;
        private String fileId;
        private String fileName;
        
        private FileObject(String folderId, String fileId, String fileName) {
            this.folderId = folderId;
            this.fileId = fileId;
            this.fileName = fileName;
        }
        
        public String getFolderId() {
            return folderId;
        }
        public void setFolderId(String folderId) {
            this.folderId = folderId;
        }
        public String getFileId() {
            return fileId;
        }
        public void setFileId(String fileId) {
            this.fileId = fileId;
        }
        public String getFileName() {
            return fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
