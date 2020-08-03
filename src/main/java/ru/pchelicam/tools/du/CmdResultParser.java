package ru.pchelicam.tools.du;

/**
 * Contains variables that used to work with disk
 */
public class CmdResultParser {
    private String fileNameAndPath = null;
    private String fileName = null;
    private String catalogName = null;
    private String authToken = null;
    private String login = null;
    private String password = null;
    private int topRest = 0;
    private int minRest = 0;
    private TypeDiskEnum typeDiskEnum = null;

    public CmdResultParser(String fileNameAndPath, String fileName, String catalogName, String authToken, int topRest, int minRest, TypeDiskEnum typeDiskEnum) {
        this.fileNameAndPath = fileNameAndPath;
        this.fileName = fileName;
        this.catalogName = catalogName;
        this.authToken = authToken;
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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public TypeDiskEnum getTypeDiskEnum() {
        return typeDiskEnum;
    }
}
