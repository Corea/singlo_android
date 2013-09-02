package com.kapp.singlo.bg;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

import android.os.AsyncTask;

public class APIPostAction extends AsyncTask<HashMap<String, String>, Void, JSONObject>{
	
	private String URL;
	
	private getAPIConnetorResultListener mListener;
	
	public APIPostAction(String url, getAPIConnetorResultListener listener){
		this.URL = url;
		this.mListener = listener;
	}
	
	@Override
	protected JSONObject doInBackground(HashMap<String, String>... params) {
		// TODO Auto-generated method stub
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		System.out.println("URL = " + URL);
		InputStream is;
		JSONObject json = null;
		try {
			
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			
			Iterator<String> iterator = params[0].keySet().iterator();
			while(iterator.hasNext()){
				String key = (String)iterator.next();					
				nameValuePairs.add(new BasicNameValuePair(URLEncoder.encode(key, "UTF-8"), 
						URLEncoder.encode(params[0].get(key), "UTF-8")));
				System.out.println("key = " + key);
				System.out.println("value = " + params[0].get(key));
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			is = httpResponse.getEntity().getContent();	
			
			JSONParser jParser = new JSONParser();
			json = jParser.getJSONFromStream(is);
			
			System.out.println("json = " + json);
			
			/*String result = json.getString("photo");
			System.out.println("result = " + result);*/
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		mListener.result(result);
		
	}
	
	public interface getAPIConnetorResultListener{
		public void result(JSONObject object);
	}
}
