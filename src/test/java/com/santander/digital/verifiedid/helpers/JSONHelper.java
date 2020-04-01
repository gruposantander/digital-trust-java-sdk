package com.santander.digital.verifiedid.helpers;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JSONHelper {
    public static JSONObject readJson(String path) {
        try {
            final String json = readFile(path);
            return new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile (String path) {
        try {
            final File file = new File(path);
            final InputStream inputStream = new FileInputStream(file);
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
