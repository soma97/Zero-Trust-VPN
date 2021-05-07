package org.unibl.etf.srs.consumer.rest;

import com.google.gson.Gson;
import org.unibl.etf.srs.consumer.Constants;
import org.unibl.etf.srs.consumer.Consumer;
import org.unibl.etf.srs.model.User;
import org.unibl.etf.srs.model.UserRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RestConsumer implements Consumer {
    private final Gson gson = new Gson();

    @Override
    public User[] getUsersForAuth() {
        return gson.fromJson(sendHttpRequest(Constants.AUTH_GW_BASE_URL + Constants.AUTH_ALL_URL,
                "GET", null), User[].class);
    }

    @Override
    public User getObjectById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public String[] getAuthenticated() {
        return gson.fromJson(sendHttpRequest(Constants.AUTH_GW_BASE_URL + Constants.AUTH_AUTHENTICATED,
                "GET", null), String[].class);
    }

    @Override
    public String[] getLogs() {
        return gson.fromJson(sendHttpRequest(Constants.ACCESS_GW_BASE_URL + Constants.LOGS,
                "GET", null), String[].class);
    }

    @Override
    public void insertObject(UserRequest object) {
        String res = sendHttpRequest(Constants.AUTH_GW_BASE_URL + Constants.AUTH_USER_URL,
                "POST", gson.toJson(object));
    }

    @Override
    public void updateObject(UserRequest object) {
        throw new NotImplementedException();
    }

    @Override
    public void removeObjectById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public void sendCommands(List<String> commands) {
        sendHttpRequest(Constants.ACCESS_GW_BASE_URL + Constants.COMMANDS,
                "POST", gson.toJson(commands));
    }

    private String sendHttpRequest(String endpoint, String requestMethod, String data) {
        String output, result = "";
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Accept", "application/json");

            if(data != null && (requestMethod.equals("POST") || requestMethod.equals("PUT"))) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                try(OutputStream os = conn.getOutputStream()) {
                    byte[] input = data.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            if (conn.getResponseCode() != 200 && conn.getResponseCode() != 201 && conn.getResponseCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                result += output;
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
