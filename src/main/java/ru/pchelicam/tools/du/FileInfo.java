package ru.pchelicam.tools.du;

import java.util.Date;

/**
 * Contains information about files
 */
public class FileInfo {
    private String fileName;
    private Date creationDate;
    private long size;
    private boolean toDelete;

    public FileInfo() {
    }

    public FileInfo(String fileName, Date creationDate, long size) {
        this.fileName = fileName;
        this.creationDate = creationDate;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }
}