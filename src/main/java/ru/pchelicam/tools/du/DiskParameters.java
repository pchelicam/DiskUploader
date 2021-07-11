package ru.pchelicam.tools.du;

import java.io.File;

public class DiskParameters {
    private String stringTypeOfTheDisk = null;
    private TypeDiskEnum typeDiskEnum = null;
    private String fileNameAndPath = null;
    private String fileName = null;
    private String nameOfCatalog = null;
    private String authenticationToken = null;
    private String username = null;
    private String password = null;
    private String keyID = null;
    private String secretAccessKey = null;
    private int topRest = 0;
    private int minRest = 0;

    public DiskParameters() {
    }

    public TypeDiskEnum getTypeDiskEnum() {
        return typeDiskEnum;
    }

    private void setTypeDiskEnum() {
        typeDiskEnum = TypeDiskEnum.getType(stringTypeOfTheDisk);
    }

    public String getFileNameAndPath() {
        return fileNameAndPath;
    }

    public void setFileNameAndPath(String fileNameAndPath) {
        this.fileNameAndPath = fileNameAndPath;
        setFileName();
    }

    public String getNameOfCatalog() {
        return nameOfCatalog;
    }

    public void setNameOfCatalog(String nameOfCatalog) {
        this.nameOfCatalog = nameOfCatalog;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public int getTopRest() {
        return topRest;
    }

    public void setTopRest(int topRest) {
        this.topRest = topRest;
    }

    public int getMinRest() {
        return minRest;
    }

    public void setMinRest(int minRest) {
        this.minRest = minRest;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public void setStringTypeOfTheDisk(String stringTypeOfTheDisk) {
        this.stringTypeOfTheDisk = stringTypeOfTheDisk;
        setTypeDiskEnum();
    }

    private void setFileName() {
        File file = new File(fileNameAndPath);
        fileName = file.getName();
    }
}
