package ru.pchelicam.tools.du;

import java.io.File;
import java.util.List;

/**
 * Interface that describes actions
 * of disk
 */
public interface IActionDisk {
    /**
     * Uses to declare authentication
     * token in class
     */
    void authorize(String authToken);

    /**
     * Uses to declare username and
     * password in class
     */
    void authorize(String username, String password);

    /**
     * Prints information of the disk
     */
    DiskInfo getInfoOfDisk() throws Exception;

    /**
     * Access to disk to get information
     * of free space that left on it
     */
    long getFreeSpaceSize() throws Exception;

    /**
     * Access to disk to get information
     * of total space on it
     */
    long getTotalSpaceSize() throws Exception;

    /**
     * Access to disk to check if catalog exist
     * or not
     */
    boolean isFolderExist(String folderName) throws Exception;

    /**
     * Access to disk to upload file
     */
    DiskResultOperation uploadFile(String folderName, String fileName, File file) throws Exception;

    /**
     * Access to disk to delete file
     */
    DiskResultOperation deleteFile(String folderName, String fileName) throws Exception;

    /**
     * Access to disk to delete list of files
     */
    DiskResultOperation deleteListOfFiles(List<FileInfo> listOfFilesToDelete, String folderName) throws Exception;

    /**
     * Access to disk to identify
     * the amount of files in catalog
     */
    int getNumOfFiles(String folderName) throws Exception;

    /**
     * Access to disk to get list of
     * files in catalog
     */
    List<FileInfo> getListOfFiles(String folderName) throws Exception;
}
