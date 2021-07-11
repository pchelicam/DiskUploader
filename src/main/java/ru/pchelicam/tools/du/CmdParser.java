package ru.pchelicam.tools.du;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses parameters using regular expressions
 */
public class CmdParser {
    private final static String PAR_REG_TD = "[\\-][\\-]td[\\=](\\S*)";
    private final static String PAR_REG_FU_APS = "[\\-][\\-]fu[\\=](\\\")(.*)(\\\")";
    private final static String PAR_REG_FU = "[\\-][\\-]fu[\\=](.\\S*)";
    private final static String PAR_REG_TR = "[\\-][\\-]tr[\\=](\\d*)";
    private final static String PAR_REG_MR = "[\\-][\\-]mr[\\=](\\d*)";
    private final static String PAR_REG_CU_APS = "[\\-][\\-]cu[\\=](\\\")(.*)(\\\")";
    private final static String PAR_REG_CU = "[\\-][\\-]cu[\\=](\\S*)";
    private final static String PAR_REG_AT = "[\\-][\\-]at[\\=](\\S*)";

    private final static String PAR_REG_UN = "[\\-][\\-]un[\\=](\\S*)";
    private final static String PAR_REG_PW = "[\\-][\\-]pw[\\=](\\S*)";

    private final static String PAR_REG_KID = "[\\-][\\-]kid[\\=](\\S*)";
    private final static String PAR_REG_SAC = "[\\-][\\-]sac[\\=](\\S*)";

    /**
     * Parses td parameter
     */
    public String parseParameterTd(String par) {
        Pattern pattern = Pattern.compile(PAR_REG_TD, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(par);
        String typeDisk = null;
        if (matcher.find()) {
            typeDisk = matcher.group(1).trim();
        }
        return typeDisk;
    }

    /**
     * Parses fu parameter
     */
    public String parseParameterFu(String par) {
        Pattern patternAPS = Pattern.compile(PAR_REG_FU_APS, Pattern.DOTALL);
        Matcher matcherAPS = patternAPS.matcher(par);
        String nameFile = null;
        if (matcherAPS.find()) {
            nameFile = matcherAPS.group(2).trim();
        } else {
            Pattern pattern = Pattern.compile(PAR_REG_FU, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(par);
            if (matcher.find()) {
                nameFile = matcher.group(1).trim();
            }
        }
        return nameFile;
    }

    /**
     * Parses tr parameter
     */
    public String parseParameterTr(String par) {
        Pattern patternMS = Pattern.compile(PAR_REG_TR, Pattern.DOTALL);
        Matcher matcherMS = patternMS.matcher(par);
        String maxFilesSave = null;
        if (matcherMS.find()) {
            maxFilesSave = matcherMS.group(1).trim();
        }
        return maxFilesSave;
    }

    /**
     * Parses mr parameter
     */
    public String parseParameterMr(String par) {
        Pattern patternMD = Pattern.compile(PAR_REG_MR, Pattern.DOTALL);
        Matcher matcherMD = patternMD.matcher(par);
        String maxFilesDelete = null;
        if (matcherMD.find()) {
            maxFilesDelete = matcherMD.group(1).trim();
        }
        return maxFilesDelete;
    }

    /**
     * Parses cu parameter
     */
    public String parseParameterCu(String par) {
        Pattern patternCU = Pattern.compile(PAR_REG_CU_APS, Pattern.DOTALL);
        Matcher matcherCU = patternCU.matcher(par);
        String path = null;

        if (matcherCU.find()) {
            path = matcherCU.group(2).trim();
        } else {
            Pattern pattern = Pattern.compile(PAR_REG_CU, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(par);
            if (matcher.find()) {
                path = matcher.group(1).trim();
            }
        }
        return path;
    }

    /**
     * Parses at parameter
     */
    public String parseParameterAt(String par) {
        Pattern patternAt = Pattern.compile(PAR_REG_AT, Pattern.DOTALL);
        Matcher matcherAt = patternAt.matcher(par);
        String token = null;
        if (matcherAt.find()) {
            token = matcherAt.group(1).trim();
        }
        return token;
    }

    /**
     * Parses un parameter
     */
    public String parseParameterUn(String par) {
        Pattern patternAt = Pattern.compile(PAR_REG_UN, Pattern.DOTALL);
        Matcher matcherAt = patternAt.matcher(par);
        String token = null;
        if (matcherAt.find()) {
            token = matcherAt.group(1).trim();
        }
        return token;
    }

    /**
     * Parses pw parameter
     */
    public String parseParameterPw(String par) {
        Pattern patternAt = Pattern.compile(PAR_REG_PW, Pattern.DOTALL);
        Matcher matcherAt = patternAt.matcher(par);
        String token = null;
        if (matcherAt.find()) {
            token = matcherAt.group(1).trim();
        }
        return token;
    }

    /**
     * Parses kid parameter
     */
    public String parseParameterKid(String par) {
        Pattern patternAt = Pattern.compile(PAR_REG_KID, Pattern.DOTALL);
        Matcher matcherAt = patternAt.matcher(par);
        String token = null;
        if (matcherAt.find()) {
            token = matcherAt.group(1).trim();
        }
        return token;
    }

    /**
     * Parses sac parameter
     */
    public String parseParameterSac(String par) {
        Pattern patternAt = Pattern.compile(PAR_REG_SAC, Pattern.DOTALL);
        Matcher matcherAt = patternAt.matcher(par);
        String token = null;
        if (matcherAt.find()) {
            token = matcherAt.group(1).trim();
        }
        return token;
    }
}
