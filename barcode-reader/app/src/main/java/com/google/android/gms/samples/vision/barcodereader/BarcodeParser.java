package com.google.android.gms.samples.vision.barcodereader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

/**
 * Created by daniel on 28/01/2018.
 */

public class BarcodeParser {
    public String makeCall(String upc_code) {
        final String signature = "2IpbWWjfLtc8hqi7K4fxbKY5koQ=";
        final String language = "en";
        final String app_key = "/032ziiUlvzA";
        final Pattern kcal = Pattern.compile("[0-9]+kcal");
//        String upc_code = "5449000124999";
        String name = "empty";
        String message = "empty";


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://digit-eyes.com/gtin/v2_0/?upc_code=" + upc_code + "&app_key="
                        + app_key + "&language=" + language + "&signature=" + signature)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonArray = response.body().string();
            JSONObject Jobject = new JSONObject(jsonArray);
            name = Jobject.getString("description");
            String nut = Jobject.getString("nutrition");

            Matcher m = kcal.matcher(nut);
//                String s = m.group(1);
            m.find();
            String kcalm = m.group(0);
            int s = parseInt(kcalm.substring(0, kcalm.length() - 4));
//            System.out.println("kcal: " + s);
//            System.out.println("name: " + name);

//            if (!jsonArray.equals("null")) {
//                JSONArray array = new JSONArray(jsonArray);
//
//                JSONObject object = array.getJSONObject(0);
//
//            } else {
//                message = "Team does not exist";
//            }


        } catch (IOException e) {
            e.printStackTrace();
            message = "Could not connect to server";
        } catch (JSONException e) {
            e.printStackTrace();
            message = "Could not connect to server";
        }
//        System.out.print(message);
        return name;
    }
}
