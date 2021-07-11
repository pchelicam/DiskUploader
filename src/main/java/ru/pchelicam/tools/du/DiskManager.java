package ru.pchelicam.tools.du;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry class - starts Disk Uploader application
 */
public class DiskManager {
    public static boolean upload(DiskParameters diskParameters) {
        DiskLogger.info("Disk Uploader was started");
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        String typeOfTheDisk = null;
        CmdResultParser result = new CmdResultParser(diskParameters.getFileNameAndPath(), diskParameters.getFileName(),
                diskParameters.getNameOfCatalog(), diskParameters.getAuthenticationToken(),
                diskParameters.getUsername(), diskParameters.getPassword(), diskParameters.getKeyID(),
                diskParameters.getSecretAccessKey(), diskParameters.getTopRest(), diskParameters.getMinRest(), diskParameters.getTypeDiskEnum());
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
        if (result.getTypeDiskEnum().equals(TypeDiskEnum.MRC)) {
            DiskLogger.info("The type of the disk is Mail.ru Cloud");
            typeOfTheDisk = "Mail.ru Cloud";
        }
        if (result.getTypeDiskEnum().equals(TypeDiskEnum.YOS)) {
            DiskLogger.info("The type of the disk is Yandex Object Storage");
            typeOfTheDisk = "Yandex Object Storage";
        }
        if (result.getAuthToken() != null) {
            actDisk.authorize(result.getAuthToken());
        } else if (result.getUsername() != null && result.getPassword() != null) {
            actDisk.authorize(result.getUsername(), result.getPassword());
        } else if (result.getKeyID() != null && result.getSecretAccessKey() != null) {
            actDisk.authorize(result.getKeyID(), result.getSecretAccessKey());
        }
        try {
            File file = new File(result.getFileNameAndPath());
            List<FileInfo> fullListOfFiles = actDisk.getListOfFiles(result.getCatalogName()); //full list of files
            List<FileInfo> listOfFilesToDelete = new ArrayList<>();
            listOfFilesToDelete = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, file.length(), actDisk.getFreeSpaceSize(),
                    actDisk.getTotalSpaceSize(), result.getMinRest(), result.getTopRest(),
                    actDisk.getNumOfFiles(result.getCatalogName()));
            if (listOfFilesToDelete != null && listOfFilesToDelete.size() != 0)
                actDisk.deleteListOfFiles(listOfFilesToDelete, result.getCatalogName());
            actDisk.uploadFile(result.getCatalogName(), result.getFileName(), file);
            NotifyManager.notifications("Disk Uploader: File has successfully been uploaded", "File " + result.getFileNameAndPath() + " is now uploaded at " + typeOfTheDisk + ".");
        } catch (Exception e1) {
            DiskLogger.error(e1.getMessage(), e1);

            try {
                NotifyManager.notifications("Disk Uploader: There was an error with file uploading", "File " + result.getFileNameAndPath() + " wasn't uploaded. See disk_log.txt for more information.");
                return false;
            } catch (Exception e2) {
                DiskLogger.error(e2.getMessage(), e2);
                return false;
            }
        }
        DiskLogger.info("Disk Uploader was finished");
        return true;
    }

    private static boolean controller(String[] s) {
        DiskLogger.info("Disk Uploader was started");
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
        if (result.getTypeDiskEnum().equals(TypeDiskEnum.MRC)) {
            DiskLogger.info("The type of the disk is Mail.ru Cloud");
            typeOfTheDisk = "Mail.ru Cloud";
        }
        if (result.getTypeDiskEnum().equals(TypeDiskEnum.YOS)) {
            DiskLogger.info("The type of the disk is Yandex Object Storage");
            typeOfTheDisk = "Yandex Object Storage";
        }
        if (result.getAuthToken() != null) {
            actDisk.authorize(result.getAuthToken());
        } else if (result.getUsername() != null && result.getPassword() != null) {
            actDisk.authorize(result.getUsername(), result.getPassword());
        } else if (result.getKeyID() != null && result.getSecretAccessKey() != null) {
            actDisk.authorize(result.getKeyID(), result.getSecretAccessKey());
        }
        try {
            File file = new File(result.getFileNameAndPath());
            List<FileInfo> fullListOfFiles = actDisk.getListOfFiles(result.getCatalogName()); //full list of files
            List<FileInfo> listOfFilesToDelete = new ArrayList<>();
            listOfFilesToDelete = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, file.length(), actDisk.getFreeSpaceSize(),
                    actDisk.getTotalSpaceSize(), result.getMinRest(), result.getTopRest(),
                    actDisk.getNumOfFiles(result.getCatalogName()));
            if (listOfFilesToDelete != null && listOfFilesToDelete.size() != 0)
                actDisk.deleteListOfFiles(listOfFilesToDelete, result.getCatalogName());
            actDisk.uploadFile(result.getCatalogName(), result.getFileName(), file);
            NotifyManager.notifications("Disk Uploader: File has successfully been uploaded", "File " + result.getFileNameAndPath() + " is now uploaded at " + typeOfTheDisk + ".");
        } catch (Exception e1) {
            DiskLogger.error(e1.getMessage(), e1);
            try {
                NotifyManager.notifications("Disk Uploader: There was an error with file uploading", "File " + result.getFileNameAndPath() + " wasn't uploaded. See disk_log.txt for more information.");
                return false;
            } catch (Exception e2) {
                DiskLogger.error(e2.getMessage(), e2);
                return false;
            }
        }
        DiskLogger.info("Disk Uploader was finished");
        return true;
    }

    public static void main(String[] s) {
        //String s1 = "--td=YOS --fu=C:\\\\Users\\\\user\\\\Downloads\\\\File3.txt --cu=testing5678 --kid=V6-bJVh0xk6MZqgfzCG- --sac=_ANhe9az5ePRnc4cb9nTDiWFCcYiCy76yYPnxV6B --tr=3 --mr=1";
        //String s1 = "--td=MRC --fu=C:\\\\Users\\\\user\\\\Downloads\\\\File1.txt --cu=/Test3/ --un=shaimardanova_anar@mail.ru --pw=u_Ik33pciXDT";
        //String s1 = "--td=MRC --fu=C:\\Users\\user\\Downloads\\Projects123.zip --cu=/Test3/ --un=shaimardanova_anar@mail.ru --pw=u_Ik33pciXDT --mr=7 --tr=8";
        //String s1 = "--td=YA --fu=C:\\Users\\user\\Downloads\\fegh.txt --cu=/Test/ --at=AgAAAAAEvAoZAAXGiyNvxjNX1Ub3lKZgMN9a2qw --mr=1 --tr=10";

        //String [] s2 = s1.split(" ");
        controller(s);
    }
}
