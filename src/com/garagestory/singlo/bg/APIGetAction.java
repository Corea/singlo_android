package com.garagestory.singlo.bg;

import android.os.AsyncTask;
import com.garagestory.singlo.util.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Vector;

public abstract class APIGetAction extends
        AsyncTask<HashMap<String, String>, Void, JSONObject> {

    private String URL;

    public APIGetAction(String url) {
        this.URL = url;
    }

    protected abstract void onActionPost(JSONObject object);

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        HttpClient httpClient = new DefaultHttpClient();

        InputStream is;
        JSONObject json = null;
        try {

            Vector<NameValuePair> nameValuePairs = new Vector<NameValuePair>();

            if( params.length > 0) {
                for (String key : params[0].keySet()) {
                    nameValuePairs.add(new BasicNameValuePair(key, URLEncoder
                            .encode(params[0].get(key), "UTF-8")));
                    System.out.println("key = " + key);
                    System.out.println("value = " + params[0].get(key));
                }
            }

            HttpGet httpGet = new HttpGet(URL + "?"
                    + URLEncodedUtils.format(nameValuePairs, "UTF-8"));
            System.out.println("URL = " + URL + "?"
                    + URLEncodedUtils.format(nameValuePairs, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpGet);
            is = httpResponse.getEntity().getContent();

            JSONParser jParser = new JSONParser();
            json = jParser.getJSONFromStream(is);

			/*
             * String result = json.getString("result");
			 * System.out.println("result = " + result);
			 */

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        onActionPost(result);
    }
}
