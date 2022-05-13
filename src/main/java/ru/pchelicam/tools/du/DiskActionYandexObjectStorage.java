package ru.pchelicam.tools.du;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DiskActionYandexObjectStorage implements IActionDisk {
    private static String keyID = null;
    private static String secretAccessKey = null;

    public void authorize(String authToken) {

    }

    public void authorize(String keyID, String secretAccessKey) {
        this.keyID = keyID;
        this.secretAccessKey = secretAccessKey;
    }

    public DiskInfo getInfoOfDisk() throws Exception {
        return null;
    }

    public long getFreeSpaceSize() throws Exception {
        return 2 * (long) Math.pow(2, 30); //two terabytes
    }

    public long getTotalSpaceSize() throws Exception {
        return 2 * (long) Math.pow(2, 30); //two terabytes
    }

    public static byte[] calcHmacSHA256(String data, byte[] key) throws Exception {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF-8"));
    }

    public static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF-8");
        byte[] kDate = calcHmacSHA256(dateStamp, kSecret);
        byte[] kRegion = calcHmacSHA256(regionName, kDate);
        byte[] kService = calcHmacSHA256(serviceName, kRegion);
        byte[] kSigning = calcHmacSHA256("aws4_request", kService);
        return kSigning;
    }

    public static String UriEncode(String str) throws Exception {
        return URLEncoder.encode(str, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
    }

    public boolean isFolderExist(String folderName) throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(timeZone);
        String dateAsISO = dateFormat.format(new Date());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDate = dateFormat2.format(new Date());
        String canonicalRequest = "GET" + "\n"
                + "/" + UriEncode(folderName) + "\n"
                + "\n"
                + "host:storage.yandexcloud.net" + "\n"
                + "x-amz-date:" + dateAsISO + "\n"
                + "\n"
                + "host;x-amz-date" + "\n"
                + DigestUtils.sha256Hex("");
        byte[] signingKeyB = getSignatureKey(secretAccessKey, currentDate, "ru-central1", "s3");
        String stringToSign = "AWS4-HMAC-SHA256" + "\n"
                + dateAsISO + "\n"
                + currentDate + "/ru-central1/s3/aws4_request" + "\n"
                + DigestUtils.sha256Hex(canonicalRequest);
        byte[] signature1 = calcHmacSHA256(stringToSign, signingKeyB);
        String signature = Hex.encodeHexString(signature1);
        String authorization = "AWS4-HMAC-SHA256 Credential=" + keyID + "/" + currentDate + "/ru-central1/s3/aws4_request" + ",SignedHeaders=host;x-amz-date,Signature=" + signature;
        Client client = Client.create();
        WebResource webResource = client.resource("https://storage.yandexcloud.net/" + folderName);
        ClientResponse response = webResource.header("Host", "storage.yandexcloud.net")
                .header("Authorization", authorization)
                .header("X-Amz-Date", dateAsISO)
                .get(ClientResponse.class);
        response.close();
        if (response.getStatus() == 404)
            return false;
        else if (response.getStatus() == 200)
            return true;
        else
            return false;
    }

    private boolean areFolderAndFileExist(String folderName, String fileName) throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(timeZone);
        String dateAsISO = dateFormat.format(new Date());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDate = dateFormat2.format(new Date());
        String canonicalRequest = "GET" + "\n"
                + "/" + UriEncode(folderName) + "/" + UriEncode(fileName) + "\n"
                + "\n"
                + "host:storage.yandexcloud.net" + "\n"
                + "x-amz-date:" + dateAsISO + "\n"
                + "\n"
                + "host;x-amz-date" + "\n"
                + DigestUtils.sha256Hex("");
        byte[] signingKeyB = getSignatureKey(secretAccessKey, currentDate, "ru-central1", "s3");
        String stringToSign = "AWS4-HMAC-SHA256" + "\n"
                + dateAsISO + "\n"
                + currentDate + "/ru-central1/s3/aws4_request" + "\n"
                + DigestUtils.sha256Hex(canonicalRequest);
        byte[] signature1 = calcHmacSHA256(stringToSign, signingKeyB);
        String signature = Hex.encodeHexString(signature1);
        String authorization = "AWS4-HMAC-SHA256 Credential=" + keyID + "/" + currentDate + "/ru-central1/s3/aws4_request" + ",SignedHeaders=host;x-amz-date,Signature=" + signature;
        Client client = Client.create();
        WebResource webResource = client.resource("https://storage.yandexcloud.net/" + UriEncode(folderName) + "/" + UriEncode(fileName));
        ClientResponse response = webResource.header("Host", "storage.yandexcloud.net")
                .header("Authorization", authorization)
                .header("X-Amz-Date", dateAsISO)
                .get(ClientResponse.class);
        response.close();
        if (response.getStatus() == 404)
            return false;
        else if (response.getStatus() == 200)
            return true;
        else
            return false;
    }

    public DiskResultOperation uploadFile(String folderName, String fileName, File file) throws Exception {
        if (areFolderAndFileExist(folderName, fileName)) { //file exist on disk
            throw new DiskException("Name of the file is occurred twice in catalog and should be renamed");
        }
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(timeZone);
        String dateAsISO = dateFormat.format(new Date());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDate = dateFormat2.format(new Date());
        String canonicalRequest = "PUT" + "\n"
                + "/" + UriEncode(folderName) + "/" + UriEncode(fileName) + "\n"
                + "\n"
                + "host:storage.yandexcloud.net" + "\n"
                + "x-amz-date:" + dateAsISO + "\n"
                + "x-amz-content-sha256:" + DigestUtils.sha256Hex(FileUtils.readFileToByteArray(file)) + "\n"
                + "\n"
                + "host;x-amz-date;x-amz-content-sha256" + "\n"
                + DigestUtils.sha256Hex(FileUtils.readFileToByteArray(file));
        byte[] signingKeyB = getSignatureKey(secretAccessKey, currentDate, "ru-central1", "s3");
        String stringToSign = "AWS4-HMAC-SHA256" + "\n"
                + dateAsISO + "\n"
                + currentDate + "/ru-central1/s3/aws4_request" + "\n"
                + DigestUtils.sha256Hex(canonicalRequest);
        byte[] signatureB = calcHmacSHA256(stringToSign, signingKeyB);
        String signature = Hex.encodeHexString(signatureB);
        String authorization = "AWS4-HMAC-SHA256 Credential=" + keyID + "/" + currentDate + "/ru-central1/s3/aws4_request" + ",SignedHeaders=host;x-amz-date;x-amz-content-sha256,Signature=" + signature;
        Client client = Client.create();
        WebResource webResource = client.resource("https://storage.yandexcloud.net/" + folderName + "/" + fileName);
        ClientResponse response = webResource.header("Host", "storage.yandexcloud.net")
                .header("Content-Length", String.valueOf(file.length()))
                .header("Authorization", authorization)
                .header("X-Amz-Date", dateAsISO)
                .header("Expect", "100-continue")
                .header("X-Amz-Content-SHA256", DigestUtils.sha256Hex(FileUtils.readFileToByteArray(file)))
                .put(ClientResponse.class, file);
        response.close();
        DiskLogger.info("File has successfully been uploaded");
        return null;
    }

    public DiskResultOperation deleteFile(String folderName, String fileName) throws Exception {
        if (!areFolderAndFileExist(folderName, fileName))
            throw new DiskException("File " + fileName + " does not exist in storage");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(timeZone);
        String dateAsISO = dateFormat.format(new Date());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDate = dateFormat2.format(new Date());
        String canonicalRequest = "DELETE" + "\n"
                + "/" + UriEncode(folderName) + "/" + UriEncode(fileName) + "\n"
                + "\n"
                + "host:storage.yandexcloud.net" + "\n"
                + "x-amz-date:" + dateAsISO + "\n"
                + "\n"
                + "host;x-amz-date" + "\n"
                + DigestUtils.sha256Hex("");

        byte[] signingKeyB = getSignatureKey(secretAccessKey, currentDate, "ru-central1", "s3");
        String stringToSign = "AWS4-HMAC-SHA256" + "\n"
                + dateAsISO + "\n"
                + currentDate + "/ru-central1/s3/aws4_request" + "\n"
                + DigestUtils.sha256Hex(canonicalRequest);
        byte[] signatureB = calcHmacSHA256(stringToSign, signingKeyB);
        String signature = Hex.encodeHexString(signatureB);
        String authorization = "AWS4-HMAC-SHA256 Credential=" + keyID + "/" + currentDate + "/ru-central1/s3/aws4_request" + ",SignedHeaders=host;x-amz-date,Signature=" + signature;

        Client client = Client.create();
        WebResource webResource = client.resource("https://storage.yandexcloud.net/" + folderName + "/" + fileName);
        ClientResponse response = webResource.header("Host", "storage.yandexcloud.net")
                .header("Authorization", authorization)
                .header("X-Amz-Date", dateAsISO)
                .delete(ClientResponse.class);
        response.close();
        DiskLogger.info("File " + fileName + " was deleted");
        return null;
    }

    public DiskResultOperation deleteListOfFiles(List<FileInfo> listOfFilesToDelete, String folderName) throws Exception {
        if (!isFolderExist(folderName))
            throw new DiskException("Catalog " + folderName + " does not exist in storage");
        for (FileInfo fileInfo : listOfFilesToDelete) {
            deleteFile(folderName, fileInfo.getFileName().replaceAll("%20", " "));
            DiskLogger.info("File " + fileInfo.getFileName().replaceAll("%20", " ") + " was deleted");
        }
        return null;
    }

    public int getNumOfFiles(String folderName) throws Exception {
        List<FileInfo> listOfFiles = getListOfFiles(folderName);
        return listOfFiles.size();
    }

    public List<FileInfo> getListOfFiles(String folderName) throws Exception {
        if (!isFolderExist(folderName))
            throw new DiskException("Catalog " + folderName + " does not exist in storage");
        List<FileInfo> listOfFiles = new ArrayList<>();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(timeZone);
        String dateAsISO = dateFormat.format(new Date());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDate = dateFormat2.format(new Date());
        String canonicalRequest = "GET" + "\n"
                + "/" + UriEncode(folderName) + "\n"
                + "\n"
                + "host:storage.yandexcloud.net" + "\n"
                + "x-amz-date:" + dateAsISO + "\n"
                + "\n"
                + "host;x-amz-date" + "\n"
                + DigestUtils.sha256Hex("");
        byte[] signingKeyB = getSignatureKey(secretAccessKey, currentDate, "ru-central1", "s3");
        String stringToSign = "AWS4-HMAC-SHA256" + "\n"
                + dateAsISO + "\n"
                + currentDate + "/ru-central1/s3/aws4_request" + "\n"
                + DigestUtils.sha256Hex(canonicalRequest);
        byte[] signature1 = calcHmacSHA256(stringToSign, signingKeyB);
        String signature = Hex.encodeHexString(signature1);
        String authorization = "AWS4-HMAC-SHA256 Credential=" + keyID + "/" + currentDate + "/ru-central1/s3/aws4_request" + ",SignedHeaders=host;x-amz-date,Signature=" + signature;
        Client client = Client.create();
        WebResource webResource = client.resource("https://storage.yandexcloud.net/" + folderName);
        ClientResponse response = webResource.header("Host", "storage.yandexcloud.net")
                .header("Authorization", authorization)
                .header("X-Amz-Date", dateAsISO)
                .get(ClientResponse.class);
        String responseEntity = response.getEntity(String.class);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new InputSource(new StringReader(responseEntity)));
        NodeList nodeList = document.getElementsByTagName("Contents");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element1 = (Element) nodeList.item(i);
            Date tmpDate = dateFormat4.parse(element1.getElementsByTagName("LastModified").item(0).getTextContent());
            String dateStr = dateFormat3.format(tmpDate);
            Date date = dateFormat3.parse(dateStr);
            FileInfo fileInfo = new FileInfo(element1.getElementsByTagName("Key").item(0).getTextContent().replaceAll(" ", "%20")
                    , date
                    , Long.parseLong(element1.getElementsByTagName("Size").item(0).getTextContent()));
            listOfFiles.add(fileInfo);
        }
        response.close();
        return listOfFiles;
    }
}
