package ru.pchelicam.tools.du;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HandleParamsNumOfFilesTest {
    /**
     * file length is more than total space on disk
     */
    @Test
    public void testWorkingWithParams1() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        lenOfFile = 500;
        freeSpaceOnDisk = 25;
        totalSpaceOnDisk = 200;
        minRest = 2;
        topRest = 5;
        amountOfFilesInCatalog = 3;

        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("File length is too big", e.getMessage());
        }
    }

    /**
     * MR or TR are not identified
     */
    @Test
    public void testWorkingWithParams2() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        lenOfFile = 7;
        freeSpaceOnDisk = 5;
        totalSpaceOnDisk = 1000;
        minRest = 4;
        topRest = 0;
        amountOfFilesInCatalog = 0;
        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("Parameters TR and MR should both be identified", e.getMessage());
        }

        minRest = 0;
        topRest = 4;
        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("Parameters TR and MR should both be identified", e.getMessage());
        }
    }

    /**
     * MR and TR aren't identified, nothing is to delete
     */
    @Test
    public void testWorkingWithParams3() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        lenOfFile = 2;
        freeSpaceOnDisk = 25;
        totalSpaceOnDisk = 1000;
        minRest = 0;
        topRest = 0;
        amountOfFilesInCatalog = 3;

        List<FileInfo> resList = new ArrayList<FileInfo>();
        try {
            resList = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(resList, new ArrayList<FileInfo>());
    }

    /**
     * file length is more than free space on disk or equal it and
     * TR is less than MR
     */
    @Test
    public void testWorkingWithParams4() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        lenOfFile = 7;
        freeSpaceOnDisk = 1;
        totalSpaceOnDisk = 1000;
        minRest = 2;
        topRest = 7;
        amountOfFilesInCatalog = 3;

        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("TR should be greater than MR", e.getMessage());
        }
    }

    /**
     * file length is more than free space on disk or equal it and
     * MR and TR are correct
     */
    @Test
    public void testWorkingWithParams5() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        FileInfo fileInfo1 = new FileInfo();
        FileInfo fileInfo2 = new FileInfo();
        FileInfo fileInfo3 = new FileInfo();
        FileInfo fileInfo4 = new FileInfo();

        fileInfo1.setFileName("Name1");
        fileInfo2.setFileName("Name2");
        fileInfo3.setFileName("Name3");
        fileInfo4.setFileName("Name4");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2020-06-18");
            fileInfo1.setCreationDate(date);
            fileInfo1.setSize(1);
            fullListOfFiles.add(fileInfo1);

            date = dateFormat.parse("2020-06-19");
            fileInfo2.setCreationDate(date);
            fileInfo2.setSize(2);
            fullListOfFiles.add(fileInfo2);

            date = dateFormat.parse("2020-06-17");
            fileInfo3.setCreationDate(date);
            fileInfo3.setSize(3);
            fullListOfFiles.add(fileInfo3);

            date = dateFormat.parse("2020-06-15");
            fileInfo4.setCreationDate(date);
            fileInfo4.setSize(4);
            fullListOfFiles.add(fileInfo4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullListOfFiles = handleParamsNumOfFiles.sortListOfFilesByDate(fullListOfFiles);

        lenOfFile = 7;
        freeSpaceOnDisk = 1;
        totalSpaceOnDisk = 1000;
        minRest = 2;
        topRest = 7;
        amountOfFilesInCatalog = 3;

        List<FileInfo> resList = new ArrayList<FileInfo>();
        try {
            resList = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<FileInfo> listOfFilesToDelete = new ArrayList<FileInfo>();
        listOfFilesToDelete.add(fileInfo4);
        listOfFilesToDelete.add(fileInfo3);

        Assert.assertEquals(listOfFilesToDelete, resList);
    }

    /**
     * file length is more than free space on disk or equal it
     * and files can't be deleted, otherwise amount of files
     * in catalog will be less than MR parameter
     */
    @Test
    public void testWorkingWithParams6() {

        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        FileInfo fileInfo1 = new FileInfo();
        FileInfo fileInfo2 = new FileInfo();
        FileInfo fileInfo3 = new FileInfo();
        FileInfo fileInfo4 = new FileInfo();

        fileInfo1.setFileName("Name1");
        fileInfo2.setFileName("Name2");
        fileInfo3.setFileName("Name3");
        fileInfo4.setFileName("Name4");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2020-06-18");
            fileInfo1.setCreationDate(date);
            fileInfo1.setSize(1);
            fullListOfFiles.add(fileInfo1);

            date = dateFormat.parse("2020-06-19");
            fileInfo2.setCreationDate(date);
            fileInfo2.setSize(2);
            fullListOfFiles.add(fileInfo2);

            date = dateFormat.parse("2020-06-17");
            fileInfo3.setCreationDate(date);
            fileInfo3.setSize(3);
            fullListOfFiles.add(fileInfo3);

            date = dateFormat.parse("2020-06-15");
            fileInfo4.setCreationDate(date);
            fileInfo4.setSize(4);
            fullListOfFiles.add(fileInfo4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullListOfFiles = handleParamsNumOfFiles.sortListOfFilesByDate(fullListOfFiles);

        lenOfFile = 7;
        freeSpaceOnDisk = 5;
        totalSpaceOnDisk = 1000;
        minRest = 3;
        topRest = 5;
        amountOfFilesInCatalog = fullListOfFiles.size();

        List<FileInfo> resList = new ArrayList<FileInfo>();
        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("Files can't be deleted, otherwise amount of files in catalog will be less than MR parameter", e.getMessage());
        }
    }

    /**
     * file length is more than free space on disk or equal it and
     * amount files in catalog is greater than TR parameter
     */
    @Test
    public void testWorkingWithParams7() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        FileInfo fileInfo1 = new FileInfo();
        FileInfo fileInfo2 = new FileInfo();
        FileInfo fileInfo3 = new FileInfo();
        FileInfo fileInfo4 = new FileInfo();

        fileInfo1.setFileName("Name1");
        fileInfo2.setFileName("Name2");
        fileInfo3.setFileName("Name3");
        fileInfo4.setFileName("Name4");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2020-06-18");
            fileInfo1.setCreationDate(date);
            fileInfo1.setSize(1);
            fullListOfFiles.add(fileInfo1);

            date = dateFormat.parse("2020-06-19");
            fileInfo2.setCreationDate(date);
            fileInfo2.setSize(2);
            fullListOfFiles.add(fileInfo2);

            date = dateFormat.parse("2020-06-17");
            fileInfo3.setCreationDate(date);
            fileInfo3.setSize(3);
            fullListOfFiles.add(fileInfo3);

            date = dateFormat.parse("2020-06-15");
            fileInfo4.setCreationDate(date);
            fileInfo4.setSize(4);
            fullListOfFiles.add(fileInfo4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullListOfFiles = handleParamsNumOfFiles.sortListOfFilesByDate(fullListOfFiles);

        lenOfFile = 7;
        freeSpaceOnDisk = 1;
        totalSpaceOnDisk = 1000;
        minRest = 2;
        topRest = 7;
        amountOfFilesInCatalog = 8;

        List<FileInfo> resList = new ArrayList<FileInfo>();
        try {
            resList = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<FileInfo> listOfFilesToDelete = new ArrayList<FileInfo>();
        listOfFilesToDelete.add(fileInfo4);
        listOfFilesToDelete.add(fileInfo3);

        Assert.assertEquals(listOfFilesToDelete, resList);
    }

    /**
     * everything is ok and file can be uploaded without deleting anything
     */
    @Test
    public void testWorkingWithParams8() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        FileInfo fileInfo1 = new FileInfo();
        FileInfo fileInfo2 = new FileInfo();
        FileInfo fileInfo3 = new FileInfo();
        FileInfo fileInfo4 = new FileInfo();

        fileInfo1.setFileName("Name1");
        fileInfo2.setFileName("Name2");
        fileInfo3.setFileName("Name3");
        fileInfo4.setFileName("Name4");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2020-06-18");
            fileInfo1.setCreationDate(date);
            fileInfo1.setSize(1);
            fullListOfFiles.add(fileInfo1);

            date = dateFormat.parse("2020-06-19");
            fileInfo2.setCreationDate(date);
            fileInfo2.setSize(2);
            fullListOfFiles.add(fileInfo2);

            date = dateFormat.parse("2020-06-17");
            fileInfo3.setCreationDate(date);
            fileInfo3.setSize(3);
            fullListOfFiles.add(fileInfo3);

            date = dateFormat.parse("2020-06-15");
            fileInfo4.setCreationDate(date);
            fileInfo4.setSize(4);
            fullListOfFiles.add(fileInfo4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullListOfFiles = handleParamsNumOfFiles.sortListOfFilesByDate(fullListOfFiles);

        lenOfFile = 7;
        freeSpaceOnDisk = 20;
        totalSpaceOnDisk = 1000;
        minRest = 1;
        topRest = 8;
        amountOfFilesInCatalog = fullListOfFiles.size();

        List<FileInfo> resList = new ArrayList<FileInfo>();
        try {
            resList = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(resList, new ArrayList<FileInfo>());
    }

    /**
     * free space is enough but TR is less than MR
     */
    @Test
    public void testWorkingWithParams9() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;


        lenOfFile = 7;
        freeSpaceOnDisk = 20;
        totalSpaceOnDisk = 1000;
        minRest = 10;
        topRest = 8;
        amountOfFilesInCatalog = fullListOfFiles.size();

        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("TR should be greater than MR", e.getMessage());
        }
    }

    /**
     * free space is enough and amount of files
     * in catalog is equal a TR parameter
     */
    @Test
    public void testWorkingWithParams10() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        FileInfo fileInfo1 = new FileInfo();
        FileInfo fileInfo2 = new FileInfo();
        FileInfo fileInfo3 = new FileInfo();
        FileInfo fileInfo4 = new FileInfo();

        fileInfo1.setFileName("Name1");
        fileInfo2.setFileName("Name2");
        fileInfo3.setFileName("Name3");
        fileInfo4.setFileName("Name4");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2020-06-18");
            fileInfo1.setCreationDate(date);
            fileInfo1.setSize(1);
            fullListOfFiles.add(fileInfo1);

            date = dateFormat.parse("2020-06-19");
            fileInfo2.setCreationDate(date);
            fileInfo2.setSize(2);
            fullListOfFiles.add(fileInfo2);

            date = dateFormat.parse("2020-06-17");
            fileInfo3.setCreationDate(date);
            fileInfo3.setSize(3);
            fullListOfFiles.add(fileInfo3);

            date = dateFormat.parse("2020-06-15");
            fileInfo4.setCreationDate(date);
            fileInfo4.setSize(4);
            fullListOfFiles.add(fileInfo4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullListOfFiles = handleParamsNumOfFiles.sortListOfFilesByDate(fullListOfFiles);

        lenOfFile = 7;
        freeSpaceOnDisk = 20;
        totalSpaceOnDisk = 1000;
        minRest = 3;
        topRest = 4;
        amountOfFilesInCatalog = fullListOfFiles.size();

        List<FileInfo> resList = new ArrayList<>();
        try {
            resList = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<FileInfo> listOfFilesToDelete = new ArrayList<FileInfo>();
        listOfFilesToDelete.add(fileInfo4);

        Assert.assertEquals(listOfFilesToDelete, resList);
    }

    /**
     * free space is enough and amount of files
     *  in catalog is less than MR parameter
     */
    @Test
    public void testWorkingWithParams11() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        lenOfFile = 7;
        freeSpaceOnDisk = 20;
        totalSpaceOnDisk = 1000;
        minRest = 5;
        topRest = 7;
        amountOfFilesInCatalog = 3;

        try {
            handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            Assert.assertEquals("Files can't be deleted, otherwise amount of files in catalog will be less than MR parameter", e.getMessage());
        }
    }

    /**
     * free space is enough but there are too much files
     */
    @Test
    public void testWorkingWithParams12() {
        HandleParamsNumOfFiles handleParamsNumOfFiles = new HandleParamsNumOfFiles();
        List<FileInfo> fullListOfFiles = new ArrayList<FileInfo>();
        long lenOfFile;
        long freeSpaceOnDisk;
        long totalSpaceOnDisk;
        int minRest;
        int topRest;
        int amountOfFilesInCatalog;

        FileInfo fileInfo1 = new FileInfo();
        FileInfo fileInfo2 = new FileInfo();
        FileInfo fileInfo3 = new FileInfo();
        FileInfo fileInfo4 = new FileInfo();

        fileInfo1.setFileName("Name1");
        fileInfo2.setFileName("Name2");
        fileInfo3.setFileName("Name3");
        fileInfo4.setFileName("Name4");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2020-06-18");
            fileInfo1.setCreationDate(date);
            fileInfo1.setSize(1);
            fullListOfFiles.add(fileInfo1);

            date = dateFormat.parse("2020-06-19");
            fileInfo2.setCreationDate(date);
            fileInfo2.setSize(2);
            fullListOfFiles.add(fileInfo2);

            date = dateFormat.parse("2020-06-17");
            fileInfo3.setCreationDate(date);
            fileInfo3.setSize(3);
            fullListOfFiles.add(fileInfo3);

            date = dateFormat.parse("2020-06-15");
            fileInfo4.setCreationDate(date);
            fileInfo4.setSize(4);
            fullListOfFiles.add(fileInfo4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullListOfFiles = handleParamsNumOfFiles.sortListOfFilesByDate(fullListOfFiles);

        lenOfFile = 7;
        freeSpaceOnDisk = 20;
        totalSpaceOnDisk = 1000;
        minRest = 1;
        topRest = 3;
        amountOfFilesInCatalog = fullListOfFiles.size();


        List<FileInfo> resList = new ArrayList<FileInfo>();
        resList = new ArrayList<FileInfo>();
        try {
            resList = handleParamsNumOfFiles.workingWithParams(fullListOfFiles, lenOfFile, freeSpaceOnDisk, totalSpaceOnDisk, minRest, topRest, amountOfFilesInCatalog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<FileInfo> listOfFilesToDelete = new ArrayList<FileInfo>();
        listOfFilesToDelete.add(fileInfo4);
        listOfFilesToDelete.add(fileInfo3);

        Assert.assertEquals(listOfFilesToDelete, resList);
    }
}
