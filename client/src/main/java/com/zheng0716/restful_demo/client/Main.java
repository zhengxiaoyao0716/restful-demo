package com.zheng0716.restful_demo.client;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 入口类.
 * Created by zhengxiaoyao0716 on 2016/9/16.
 */
public class Main {
    private static final String HOST = "http://localhost:8080";
    private static final String ACCESS_KEY = "test";
    private static final SecurityUtil securityUtil = new SecurityUtil("Secret key for Restful Demo, gen@2016/9/11");

    public static void main(String[] args) throws IOException {
        httpReq(HOST + "/api/guest/time/now", "GET", null);

        Map<String, Object> data = new HashMap<>();
        data.put("year", null);
        data.put("month", 0);
        data.put("date", 3);
        data = securityUtil.signed(data);
        httpReq(HOST + "/api/guest/time/add", "POST", JSONObject.toJSONBytes(data));
    }

    private static void authorize(URLConnection connection) {
        connection.setRequestProperty("accesskey", ACCESS_KEY);
        String timestamp = String.valueOf(System.currentTimeMillis());
        connection.setRequestProperty("timestamp", timestamp);
        connection.setRequestProperty("authorization", securityUtil.hmacSha256Hex(timestamp));
    }

    private static void httpReq(String url, String method, byte[] data) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("content-type", "application/json;charset=UTF-8");
        connection.setConnectTimeout(6000);
        authorize(connection);
        connection.setRequestMethod(method);

        if (data != null) {
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("Http connect error, code: " + responseCode);
            System.out.println(connection.getErrorStream());
        }
        InputStream inputStream = connection.getInputStream();
        int cacheLength;
        byte[] cacheBytes = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((cacheLength = inputStream.read(cacheBytes)) != -1) {
            outputStream.write(cacheBytes, 0, cacheLength);
        }
        outputStream.close();
        inputStream.close();
        System.out.println(outputStream);
    }
}
