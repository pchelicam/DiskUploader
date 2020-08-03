package ru.pchelicam.tools.du;

/**
 * Contains information about disk
 */
public class DiskInfo {
    private long remainSpace;
    private long maxFileSize;

    public long getRemainSpace() {
        return remainSpace;
    }

    public void setRemainSpace(long remainSpace) {
        this.remainSpace = remainSpace;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
