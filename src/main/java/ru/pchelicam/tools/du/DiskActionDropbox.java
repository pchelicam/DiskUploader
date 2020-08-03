package ru.pchelicam.tools.du;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import com.sun.jersey.multipart.impl.MultiPartWriter;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
//import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiskActionDropbox implements IActionDisk {
    private String authToken;
    private String wURL = "https://api.dropboxapi.com";

    public void authorize(String authToken) {
        this.authToken = authToken;
    }

    public DiskInfo getInfoOfDisk() {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/users/get_space_usage");
        ClientResponse response = null;
        response = webResource.header("Authorization", "Bearer " + authToken).post(ClientResponse.class);
        String result = response.getEntity(String.class);
        System.out.println(result);
        return null;
    }

    public long getFreeSpaceSize() throws Exception {
        long res = 0;
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/users/get_space_usage");
        ClientResponse response = null;
        response = webResource.header("Authorization", "Bearer " + authToken).post(ClientResponse.class);
        JSONObject jsonObject = new JSONObject(response.getEntity(String.class));
        JSONObject alloc = jsonObject.getJSONObject("allocation");
        String strTmp = alloc.get("allocated").toString();
        long totalSpace = Long.parseLong(strTmp);
        long usedSpace = Long.parseLong(jsonObject.get("used").toString());
        res = totalSpace - usedSpace;
        return res;
    }

    public long getTotalSpaceSize() throws Exception {
        long res = 0;
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/users/get_space_usage");
        ClientResponse response = null;
        response = webResource.header("Authorization", "Bearer " + authToken).post(ClientResponse.class);
        JSONObject jsonObject = new JSONObject(response.getEntity(String.class));
        JSONObject alloc = jsonObject.getJSONObject("allocation");
        String strTmp = alloc.get("allocated").toString();
        res = Long.parseLong(strTmp);


        return res;
    }

    public boolean isFolderExist(String folderName) throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/files/list_folder");
        ClientResponse response = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", folderName);
        jsonObject.put("recursive", false);
        jsonObject.put("include_media_info", false);
        jsonObject.put("include_deleted", false);
        jsonObject.put("include_has_explicit_shared_members", false);
        jsonObject.put("include_mounted_folders", true);
        jsonObject.put("include_non_downloadable_files", true);
        response = webResource.header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .post(ClientResponse.class, jsonObject.toString());
        JSONObject json = new JSONObject(response.getEntity(String.class));
        try {
            if ((json.get("error_summary").toString().equals("path/not_found/.")))
                return false;

        } catch (Exception e) {
            throw new DiskException("");
        }
        return false;
    }

    public DiskResultOperation uploadFile(String folderName, String fileName, File file) throws Exception {
        ClientConfig cc = new DefaultClientConfig();
        Client client;
        cc.getClasses().add(MultiPartWriter.class);
        client = Client.create(cc);
        WebResource webResource = client.resource("https://content.dropboxapi.com/2/files/upload");
        ClientResponse response = null;
        response = webResource.header("Authorization", "Bearer " + authToken)
                .header("Dropbox-API-Arg", "{\"path\": \"/Test/" + fileName + "\", \"mode\": \"add\", \"autorename\": true, \"mute\": false, \"strict_conflict\": false}")
                .header("Content-Type", "application/octet-stream")
                .post(ClientResponse.class, file);
        if (response.getStatus() == 409) {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus() + ", " + "Disk can be overflowed");
        } else if (response.getStatus() == 200) {
            DiskLogger.info("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus() + ", "
                    + "File has successfully been uploaded");
            return null;
        } else {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus());
        }
    }

    public DiskResultOperation deleteFile(String folderName, String fileName) throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/files/delete_v2");
        ClientResponse response = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", folderName + fileName);
        response = webResource.header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .post(ClientResponse.class, jsonObject.toString());

        return null;
    }

    public DiskResultOperation deleteListOfFiles(List<FileInfo> listOfFilesToDelete, String folderName) throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/files/delete_v2");
        ClientResponse response = null;
        FileInfo fileInfo;
        for (int i = 0; i < listOfFilesToDelete.size(); i++) {
            fileInfo = listOfFilesToDelete.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("path", folderName + fileInfo.getFileName());
            response = webResource.header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .post(ClientResponse.class, jsonObject.toString());
        }
        return null;

    }

    public int getNumOfFiles(String folderName) throws Exception {
        List<FileInfo> list = new ArrayList<FileInfo>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/files/list_folder");
        ClientResponse response = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", folderName);
        jsonObject.put("recursive", false);
        jsonObject.put("include_media_info", false);
        jsonObject.put("include_deleted", false);
        jsonObject.put("include_has_explicit_shared_members", false);
        jsonObject.put("include_mounted_folders", true);
        jsonObject.put("include_non_downloadable_files", true);
        response = webResource.header("Authorization", "Bearer " + authToken).header("Content-Type", "application/json").post(ClientResponse.class, jsonObject.toString());
        JSONObject json = new JSONObject(response.getEntity(String.class));
        JSONArray array = json.getJSONArray("entries");
        return array.length();
    }

    public List<FileInfo> getListOfFiles(String folderName) throws Exception {
        List<FileInfo> list = new ArrayList<FileInfo>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Client client = Client.create();
        WebResource webResource = client.resource(wURL + "/2/files/list_folder");
        ClientResponse response = null;
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("path", folderName);
        jsonObject1.put("recursive", false);
        jsonObject1.put("include_media_info", false);
        jsonObject1.put("include_deleted", false);
        jsonObject1.put("include_has_explicit_shared_members", false);
        jsonObject1.put("include_mounted_folders", true);
        jsonObject1.put("include_non_downloadable_files", true);
        response = webResource.header("Authorization", "Bearer " + authToken).header("Content-Type", "application/json").post(ClientResponse.class, jsonObject1.toString());
        if (response.getStatus() == 401) {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus() + ", " + "Authentication token is expired");
        } else if (response.getStatus() == 200) {

            JSONObject jsonObject2 = new JSONObject(response.getEntity(String.class));
            JSONArray array = jsonObject2.getJSONArray("entries");
            for (int i = 0; i < array.length(); i++) {
                Date date = dateFormat.parse(array.getJSONObject(i).getString("client_modified"));
                FileInfo fi = new FileInfo(array.getJSONObject(i).getString("name"), date, array.getJSONObject(i).getLong("size"));
                list.add(fi);
            }
            return list;
        } else {
            throw new DiskException("HTTP Status: " + response.getStatus() + ", " + "HTTP Response: " + response.getClientResponseStatus());
        }
    }
}
