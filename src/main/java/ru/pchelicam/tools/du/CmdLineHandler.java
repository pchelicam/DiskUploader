package ru.pchelicam.tools.du;

import java.io.File;

/**
 * Handle parameters inputted in command line
 */
public class CmdLineHandler {
    /**
     * Parses parameters inputted in command line
     */
    public CmdResultParser parse(String[] s) {
        String fileNameAndPath = null;
        String fileName = null;
        String catalogName = null;
        String authToken = null;
        String username = null;
        String password = null;
        String keyID = null;
        String secretAccessKey = null;
        TypeDiskEnum typeDiskEnum = null;
        int maxFilesToUpload = 0;
        int maxFilesToDelete = 0;
        CmdParser result = new CmdParser();
        String temp = null, temp2 = null;

        for (int i = 0; i < s.length; i++) {
            temp = s[i];
            temp2 = result.parseParameterTd(temp);
            if (temp2 != null) {
                typeDiskEnum = TypeDiskEnum.getType(temp2);
            }

            temp2 = result.parseParameterFu(temp);
            if (temp2 != null) {
                fileNameAndPath = temp2;
                File path = new File(temp2);
                fileName = path.getName();
            }

            temp2 = result.parseParameterTr(temp);
            if (temp2 != null) {
                maxFilesToUpload = Integer.parseInt(temp2);
            }

            temp2 = result.parseParameterMr(temp);
            if (temp2 != null) {
                maxFilesToDelete = Integer.parseInt(temp2);
            }

            temp2 = result.parseParameterCu(temp);
            if (temp2 != null) {
                catalogName = temp2;
            }

            temp2 = result.parseParameterAt(temp);
            if (temp2 != null) {
                authToken = temp2;
            }

            temp2 = result.parseParameterUn(temp);
            if (temp2 != null) {
                username = temp2;
            }

            temp2 = result.parseParameterPw(temp);
            if (temp2 != null) {
                password = temp2;
            }

            temp2 = result.parseParameterKid(temp);
            if (temp2 != null) {
                keyID = temp2;
            }

            temp2 = result.parseParameterSac(temp);
            if (temp2 != null) {
                secretAccessKey = temp2;
            }
        }
        new CmdLineValidator().correctTRandMR(maxFilesToUpload, maxFilesToDelete);
        return new CmdResultParser(fileNameAndPath, fileName, catalogName, authToken, username, password, keyID, secretAccessKey, maxFilesToUpload, maxFilesToDelete, typeDiskEnum);
    }
}
