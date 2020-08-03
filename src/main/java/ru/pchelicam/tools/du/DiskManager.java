package ru.pchelicam.tools.du;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry class - starts IDisk application
 */
public class DiskManager {
    public static boolean controller(String[] s) {
        DiskLogger.info("IDisk was started");
        CmdLineHandler handler = new CmdLineHandler();
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        String typeOfTheDisk = null;
        CmdResultParser result = handler.parse(s);
        DiskFactory diskFactory = new DiskFactory();
        IActionDisk actDisk = diskFactory.getDisk(result.getTypeDiskEnum());
        if (result.getTypeDiskEnum().equals(TypeDiskEnum.YA)) {
            DiskLogger.info("The type of the disk is Yandex Disk");
            typeOfTheDisk = "Yandex Disk";
        }
        if (result.getTypeDiskEnum().equals(TypeDiskEnum.DB)) {
            DiskLogger.info("The type of the disk is Dropbox");
            typeOfTheDisk = "Dropbox";
        }
        if (result.getAuthToken() != null) {
            actDisk.authorize(result.getAuthToken());
        }
        try {
            File file = new File(result.getFileNameAndPath());
            List<FileInfo> fullListOfFiles = actDisk.getListOfFiles(result.getCatalogName()); //full list of files
            FileInfo fileInfo;
            List<FileInfo> listOfFilesToDelete = new ArrayList<>();
            listOfFilesToDelete = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, file.length(), actDisk.getFreeSpaceSize(),
                    actDisk.getTotalSpaceSize(), result.getMinRest(), result.getTopRest(),
                    actDisk.getNumOfFiles(result.getCatalogName()));
            if (listOfFilesToDelete != null && listOfFilesToDelete.size() != 0)
                actDisk.deleteListOfFiles(listOfFilesToDelete, result.getCatalogName());
            actDisk.uploadFile(result.getCatalogName(), result.getFileName(), file);
            NotifyManager.notifications("IDisk: File has successfully been uploaded", "File " + result.getFileNameAndPath() + " is now uploaded at " + typeOfTheDisk + ".");
        } catch (Exception e1) {
            //System.out.println(e1.getMessage());
            DiskLogger.error(e1.getMessage(), e1);

            try {
                NotifyManager.notifications("IDisk: There was an error with file uploading", "File " + result.getFileNameAndPath() + " wasn't uploaded. See disk_log.txt for more information.");
            } catch (Exception e2) {
                DiskLogger.error(e2.getMessage(), e2);
            }
        }
        DiskLogger.info("IDisk was finished");
        return true;
    }

    public static void main(String[] s) {
        controller(s);
    }
}
