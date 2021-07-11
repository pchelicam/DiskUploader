package ru.pchelicam.tools.du;

import com.github.sardine.*;
import com.github.sardine.impl.SardineException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiskActionMailRu implements IActionDisk {
    private String username = null;
    private String password = null;
    private String wURL = "https://webdav.cloud.mail.ru";

    public void authorize(String authToken) {

    }

    public void authorize(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public DiskInfo getInfoOfDisk() throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        DavQuota davQuota = sardine.getQuota(wURL);
        long freeSpaceSize = davQuota.getQuotaAvailableBytes();
        long totalSpaceSize = davQuota.getQuotaAvailableBytes() + davQuota.getQuotaUsedBytes();
        System.out.println("Free space size: " + freeSpaceSize + ", total space size: " + totalSpaceSize + ".");
        return null;
    }

    public long getFreeSpaceSize() throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        DavQuota davQuota = sardine.getQuota(wURL);
        long freeSpaceSize = davQuota.getQuotaAvailableBytes();
        return freeSpaceSize;
    }

    public long getTotalSpaceSize() throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        DavQuota davQuota = sardine.getQuota(wURL);
        long totalSpaceSize = davQuota.getQuotaAvailableBytes() + davQuota.getQuotaUsedBytes();
        return totalSpaceSize;
    }

    public boolean isFolderExist(String folderName) throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        if (sardine.exists(wURL + folderName))
            return true;
        else
            return false;
    }

    public DiskResultOperation uploadFile(String folderName, String fileName, File file) throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        if (sardine.exists(wURL + folderName + fileName)) { //file exist on disk
            throw new DiskException("Name of the file is occurred twice in catalog and should be renamed");
        }
        InputStream fis = new FileInputStream(file);
        sardine.enablePreemptiveAuthentication("webdav.cloud.mail.ru");
        try {
            sardine.put(wURL + folderName + fileName, fis);
        } catch (SardineException e) {
            if (e.getStatusCode() == 507) {
                throw new DiskException("HTTP Status: " + e.getStatusCode() + ", " + "HTTP Response: " + e.getResponsePhrase() + "Disk is overflowed. May be you should empty trash");
            } else {
                throw new DiskException("HTTP Status: " + e.getStatusCode() + ", " + "HTTP Response: " + e.getResponsePhrase());
            }
        }
        DiskLogger.info("File has successfully been uploaded");
        return null;
    }

    public DiskResultOperation deleteFile(String folderName, String fileName) throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        if (!isFolderExist(folderName)) {
            throw new DiskException("Catalog " + folderName + " does not exist on disk");
        }
        try {
            sardine.delete(wURL + folderName + fileName);
        } catch (SardineException e) {
            if (e.getStatusCode() == 404) {
                throw new DiskException("HTTP Status: " + e.getStatusCode() + ", " + "HTTP Response: " + e.getResponsePhrase() + "File was not found");
            } else
                throw new DiskException("HTTP Status: " + e.getStatusCode() + ", " + "HTTP Response: " + e.getResponsePhrase());
        }
        DiskLogger.info("File " + fileName + " was deleted");
        return null;
    }

    public DiskResultOperation deleteListOfFiles(List<FileInfo> listOfFilesToDelete, String folderName) throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        if (!isFolderExist(folderName)) {
            throw new DiskException("Catalog " + folderName + " does not exist on disk");
        }
        for (FileInfo fileInfo : listOfFilesToDelete) {
            try {
                sardine.delete(wURL + folderName + fileInfo.getFileName());
                DiskLogger.info("File " + fileInfo.getFileName().replaceAll("%20", " ") + " was deleted");
            } catch (SardineException e) {
                if (e.getStatusCode() == 404) {
                    throw new DiskException("HTTP Status: " + e.getStatusCode() + ", " + "HTTP Response: " + e.getResponsePhrase() + "File was not found");
                } else
                    throw new DiskException("HTTP Status: " + e.getStatusCode() + ", " + "HTTP Response: " + e.getResponsePhrase());
            }
        }
        return null;
    }

    public int getNumOfFiles(String folderName) throws Exception {
        List<FileInfo> listOfFiles = getListOfFiles(folderName);
        return listOfFiles.size();
    }

    public List<FileInfo> getListOfFiles(String folderName) throws Exception {
        List<FileInfo> listOfFiles = new ArrayList<FileInfo>();
        Sardine sardine = SardineFactory.begin(username, password);
        if (!isFolderExist(folderName)) {
            throw new DiskException("Catalog " + folderName + " was not found");
        }
        List<DavResource> resources = sardine.list(wURL + folderName);
        resources.remove(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        for (DavResource res : resources) {
            if (res.isDirectory())
                continue;
            String dateStr = dateFormat.format(res.getModified());
            Date date = dateFormat.parse(dateStr);
            FileInfo fileInfo = new FileInfo(res.getDisplayName().replaceAll(" ", "%20"), date, res.getContentLength());
            listOfFiles.add(fileInfo);
        }
        return listOfFiles;
    }
}
