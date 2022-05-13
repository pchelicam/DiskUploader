package ru.pchelicam.tools.du;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DiskActionYandex implements IActionDisk {
    private String authToken = null;
    private String wURL = "https://cloud-api.yandex.net";

    public void authorize(String authToken) {
        this.authToken = authToken;
    }

    public void authorize(String username, String password){

    }

    public DiskInfo getInfoOfDisk() throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/");
        ClientResponse response = null;
        response = webResource.header("Authorization", authToken).get(ClientResponse.class);
        String result = response.getEntity(String.class);
        System.out.println(result);
        return null;
    }

    public long getFreeSpaceSize() throws Exception {
        long result = 0;
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/");
        ClientResponse response = null;
        response = webResource.header("Authorization", authToken).get(ClientResponse.class);
        JSONObject json = new JSONObject(response.getEntity(String.class));
        Object ts = json.get("total_space");
        Object us = json.get("used_space");
        String total_space = ts.toString();
        String used_space = us.toString();
        result = Long.parseLong(total_space) - Long.parseLong(used_space);
        return result;
    }

    public long getTotalSpaceSize() throws Exception {
        long result = 0;
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/");
        ClientResponse response = null;
        response = webResource.header("Authorization", authToken).get(ClientResponse.class);
        JSONObject json = new JSONObject(response.getEntity(String.class));
        Object ts = json.get("total_space");
        String total_space = ts.toString();
        result = Long.parseLong(total_space);
        return result;
    }

    public boolean isFolderExist(String folderName) throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/resources");
        ClientResponse response = null;
        response = webResource.queryParam("path", folderName).header("Authorization", authToken).get(ClientResponse.class);
        JSONObject json = new JSONObject(response.getEntity(String.class));
        Object descr = json.get("description");
        String description = descr.toString();
        if (description.equals("Resource not found."))
            return false;
        else return true;
    }

    public DiskResultOperation uploadFile(String folderName, String fileName, File file) throws Exception {
        ClientConfig cc = new DefaultClientConfig();
        Client client;
        cc.getClasses().add(MultiPartWriter.class);
        client = Client.create(cc);
        WebResource webResource = client.resource(wURL + "/v1/disk/resources/upload");
        ClientResponse response = null;
        response = webResource.queryParam("path", folderName + fileName).header("Authorization", authToken).header("Content-type", "application/json;charset=UTF-8").get(ClientResponse.class);
        if (response.getStatus() == 409) {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus() + ", " + "May be name of the file is occurred twice in catalog and should be renamed");
        }
        JSONObject json = new JSONObject(response.getEntity(String.class));
        Object href = json.get("href");
        String u = href.toString();
        webResource = client.resource(u);
        final FormDataMultiPart m = new FormDataMultiPart();
        if (file != null)
            m.bodyPart(new FileDataBodyPart("file", file, MediaType.MULTIPART_FORM_DATA_TYPE));
        final ClientResponse clientResponse = webResource.type(MediaType.MULTIPART_FORM_DATA).put(ClientResponse.class, m);
        if (clientResponse.getStatus() == 507) {
            //disk is overflowed
            //log it
            throw new DiskException("HTTP Status: " + clientResponse.getStatus() + ", " + "Disk is overflowed. May be you should empty trash");
        } else if (clientResponse.getStatus() == 201) {
            DiskLogger.info("HTTP Status: " + clientResponse.getStatus() + ", " + "HTTP Response: " + clientResponse.getClientResponseStatus() + ", " + "File has successfully been uploaded");
            return null;
        } else if (clientResponse.getStatus() == 413) {
            throw new DiskException("HTTP Status: " + clientResponse.getStatus() + ", " + "HTTP Response: " + clientResponse.getClientResponseStatus() + ", " + "File length is more than 10 Gb");
        } else {
            throw new DiskException("HTTP Status: " + clientResponse.getStatus() + ", " + "HTTP Response: " + clientResponse.getClientResponseStatus());
        }
    }

    public DiskResultOperation deleteFile(String folderName, String fileName) throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/resources");
        ClientResponse response = null;
        response = webResource.queryParam("path", folderName + fileName).header("Authorization", authToken).delete(ClientResponse.class);
        DiskLogger.info("File " + fileName + " was deleted");
        return null;
    }

    public DiskResultOperation deleteListOfFiles(List<FileInfo> listOfFilesToDelete, String folderName) throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/resources");
        ClientResponse response = null;
        FileInfo fileInfo;
        for (int i = 0; i < listOfFilesToDelete.size(); i++) {
            fileInfo = listOfFilesToDelete.get(i);
            response = webResource.queryParam("path", folderName + fileInfo.getFileName()).header("Authorization", authToken).delete(ClientResponse.class);
            DiskLogger.info("File " + fileInfo.getFileName().replaceAll("%20", " ") + " was deleted");
        }
        return null;
    }

    public int getNumOfFiles(String folderName) throws Exception {
        ClientResponse response = null;
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/resources");
        response = webResource.queryParam("path", folderName).queryParam("fields", "_embedded.total").header("Authorization", authToken).get(ClientResponse.class);
        JSONObject json = new JSONObject(response.getEntity(String.class));
        JSONObject emb = json.getJSONObject("_embedded");
        Object t = emb.get("total");
        String t2 = t.toString();
        int total = Integer.parseInt(t2);
        return total;
    }

    public List<FileInfo> getListOfFiles(String folderName) throws Exception {
        List<FileInfo> list = new ArrayList<FileInfo>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/v1/disk/resources");
        ClientResponse response = null;
        response = webResource.queryParam("path", folderName).queryParam("fields", "_embedded.items.name,_embedded.items.created, _embedded.items.size").header("Authorization", authToken).get(ClientResponse.class);
        if (response.getStatus() == 419) {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus() + ", " + "Authentication token is expired");
        } else if (response.getStatus() == 401) {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus() + ", " + "Authentication token is incorrect");
        } else if (response.getStatus() == 200) {
            JSONObject jsonObject = new JSONObject(response.getEntity(String.class));
            JSONObject embedded = jsonObject.getJSONObject("_embedded");
            JSONArray array = embedded.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                Date date = dateFormat.parse(array.getJSONObject(i).getString("created"));
                FileInfo fileInfo = new FileInfo(array.getJSONObject(i).getString("name"), date, array.getJSONObject(i).getLong("size"));
                list.add(fileInfo);
            }
            return list;
        } else {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus());
        }
    }
}
