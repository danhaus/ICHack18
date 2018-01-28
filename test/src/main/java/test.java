import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class test {
    public static void main(String[] args) {
        final String signature = "q9vxQS4qeeR82lRPGB3rjyrBOYY=";
        final String language = "en";
        final String app_key = "/032ziiUlvzA";
        final Pattern kcal = Pattern.compile("[0-9]+kcal");
        String upc_code = "5449000124999";
        String message = "";


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://digit-eyes.com/gtin/v2_0/?upc_code=" + upc_code + "&app_key="
                        + app_key  + "&language=" + language + "&signature=" + signature)
                       .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonArray = response.body().string();
            JSONObject Jobject = new JSONObject(jsonArray);
            String name = Jobject.getString("description");
            String nut = Jobject.getString("nutrition");

            Matcher m = kcal.matcher(nut);
//                String s = m.group(1);
            m.find();
            String kcalm = m.group(0);
            int s = parseInt(kcalm.substring(0, kcalm.length() - 4));
            System.out.println("name: "+name);
            System.out.println("kcal: "+s);


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
    }
}
