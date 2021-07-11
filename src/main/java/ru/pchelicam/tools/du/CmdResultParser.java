package ru.pchelicam.tools.du;

/**
 * Contains variables that used to work with disk
 */
public class CmdResultParser {
    private String fileNameAndPath = null;
    private String fileName = null;
    private String catalogName = null;
    private String authToken = null;
    private String username = null;
    private String password = null;
    private String keyID = null;
    private String secretAccessKey = null;
    private int topRest = 0;
    private int minRest = 0;
    private TypeDiskEnum typeDiskEnum = null;

    public CmdResultParser(String fileNameAndPath, String fileName, String catalogName, String authToken, String username, String password, String keyID, String secretAccessKey, int topRest, int minRest, TypeDiskEnum typeDiskEnum) {
        this.fileNameAndPath = fileNameAndPath;
        this.fileName = fileName;
        this.catalogName = catalogName;
        this.authToken = authToken;
        this.username = username;
        this.password = password;
        this.keyID = keyID;
        this.secretAccessKey = secretAccessKey;
        this.topRest = topRest;
        this.minRest = minRest;
        this.typeDiskEnum = typeDiskEnum;
    }

    public String getFileNameAndPath() {
        return fileNameAndPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getTopRest() {
        return topRest;
    }

    public int getMinRest() {
        return minRest;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getKeyID() {
        return keyID;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public TypeDiskEnum getTypeDiskEnum() {
        return typeDiskEnum;
    }
}
