package com.garagestory.singlo.bg;

import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThumbnailUrlAction {

    public String getThumbnailUrl(HashMap<String, String> params) {

        String url = Const.LESSON_CAPTURE_GET_URL;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        InputStream is;

        String result = null;

        try {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair((key), params.get(key)));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            is = httpResponse.getEntity().getContent();

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromStream(is);

            result = json.getString("result");
            if ("success".equals(result)) {
                result = json.getString("path");
            } else {
                result = "fail";
            }

            System.out.println("result = " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
