package ru.pchelicam.tools.du;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Consider inputted parameters
 */
public class HandleParamsNumOfFiles {
    /**
     * Form list of files to delete
     */
    public List<FileInfo> formFileListToDelete(List<FileInfo> fullListOfFiles, long lenOfFile, long freeSpaceOnDisk, int minRest, int topRest) throws Exception {
        FileInfo fileInfo;
        int amountFilesToDelete = 0;
        long sumOfSizes = 0; //sum of files sizes
        List<FileInfo> resList = new ArrayList<FileInfo>();
        for (int i = 0; i < fullListOfFiles.size(); i++) {
            fileInfo = fullListOfFiles.get(i);
            sumOfSizes += fileInfo.getSize();
            fileInfo.setToDelete(true);
            resList.add(fileInfo);
            amountFilesToDelete++;
            if (sumOfSizes >= lenOfFile && (freeSpaceOnDisk + sumOfSizes) >= lenOfFile) {
                if ((fullListOfFiles.size() - amountFilesToDelete + 1) >= minRest && (fullListOfFiles.size() - amountFilesToDelete) < topRest)
                    return resList;
                else if((fullListOfFiles.size() - amountFilesToDelete + 1) < minRest) {
                    throw new DiskException("Files can't be deleted, otherwise amount of files in catalog will be less than MR parameter");
                }
            }
        }
        return null;
    }

    /**
     * Form list of files to delete without
     * considering length of file and free space
     * on disk
     */
    public List<FileInfo> formFilesListToDeleteWithoutLenConsider(List<FileInfo> fullListOfFiles, int minRest, int topRest) {
        FileInfo fileInfo;
        int amountFilesToDelete = 0;
        List<FileInfo> resList = new ArrayList<FileInfo>();
        for (int i = 0; i < fullListOfFiles.size(); i++) {
            fileInfo = fullListOfFiles.get(i);
            fileInfo.setToDelete(true);
            resList.add(fileInfo);
            amountFilesToDelete++;
            if ((fullListOfFiles.size() - amountFilesToDelete + 1) >= minRest && (fullListOfFiles.size() - amountFilesToDelete) < topRest)
                return resList;
        }
        //return null;
        return new ArrayList<FileInfo>();
    }

    /**
     * Consider all parameters to form file list to delete
     */
    public List<FileInfo> workingWithParams(List<FileInfo> fullListOfFiles, long lenOfFile, long freeSpaceOnDisk, long totalSpaceOnDisk, int minRest, int topRest, int amountOfFilesInCatalog) throws Exception {
        fullListOfFiles = sortListOfFilesByDate(fullListOfFiles);
        if (lenOfFile > totalSpaceOnDisk) { //file length is more than total space on disk
            //raise exception
            throw new DiskException("File length is too big");
        } else if (minRest == 0 && topRest != 0 || minRest != 0 && topRest == 0) {
            throw new DiskException("Parameters TR and MR should both be identified");
        } else if (minRest == 0 && topRest == 0) { //minRest and topRest wasn't identified, nothing is to delete
            return new ArrayList<FileInfo>();
        } else if (lenOfFile >= freeSpaceOnDisk) { //file length is more than free space on disk or equal it
            if (topRest < minRest) {
                throw new DiskException("TR should be greater than MR");
            } else if (amountOfFilesInCatalog >= minRest && amountOfFilesInCatalog <= topRest) {
                return formFileListToDelete(fullListOfFiles, lenOfFile, freeSpaceOnDisk, minRest, topRest);
            } else if (amountOfFilesInCatalog < minRest) {
                throw new DiskException("Files can't be deleted, otherwise amount of files in catalog will be less than MR parameter");
            }
            else { //there are too much files
                return formFileListToDelete(fullListOfFiles, lenOfFile, freeSpaceOnDisk, minRest, topRest);
            }
        }
        else { //when free space is enough
            if (amountOfFilesInCatalog >= minRest && amountOfFilesInCatalog < topRest) { //everything is ok
                return new ArrayList<FileInfo>();
            } else if (topRest < minRest) {
                throw new DiskException("TR should be greater than MR");
            } else if (amountOfFilesInCatalog < minRest) {
                throw new DiskException("Files can't be deleted, otherwise amount of files in catalog will be less than MR parameter");
            } //else if (amountOfFilesInCatalog > topRest) { //there are too much files
            else { //there are too much files
                return formFilesListToDeleteWithoutLenConsider(fullListOfFiles, minRest, topRest);
            }
        }
    }

    public List<FileInfo> sortListOfFilesByDate(List<FileInfo> listToBeSorted) {
        DateComparator comparator = new DateComparator();
        Collections.sort(listToBeSorted, comparator); //sort by date
        return listToBeSorted;
    }
}
