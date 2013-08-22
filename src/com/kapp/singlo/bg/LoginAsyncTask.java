package com.kapp.singlo.bg;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

public class LoginAsyncTask extends AsyncTask<String, Void, Void> {

	private Context context;
	private CallbackListener callbackListener;

	private int count;
	private int id;
	private String name;
	private String birthday;
	private String phone;
	private String photo;

	private boolean loginSuccess = false;
	private boolean isProfessional = false;

	public void setContext(Context context) {
		this.context = context;
	}

	public void setCallbackListener(CallbackListener callbackListener) {
		this.callbackListener = callbackListener;
	}

	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... p) {
		if (p.length != 3) {
			return null;
		}

		name = p[0];
		birthday = p[1];
		phone = p[2];

		login();
		return null;
	}

	public boolean getLoginSuccess() {
		return loginSuccess;
	}

	public boolean isProfessional() {
		return isProfessional;
	}

	public int getCount() {
		return count;
	}

	public int getID() {
		return id;
	}

	public String getPhoto() {
		return photo;
	}

	@Override
	protected void onPostExecute(Void result) {
		callbackListener.callback(this);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	void login() {
		String url = Const.LOGIN_URL;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		InputStream is;

		Log.d("Login", url);
		try {
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
			nameValuePairs.add(new BasicNameValuePair("phone", phone));

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			is = httpResponse.getEntity().getContent();

			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromStream(is);

			name = URLDecoder.decode(name, "UTF-8");
			int now_id = json.getInt("id");
			String now_photo = URLDecoder.decode(json.getString("photo"),
					"UTF-8");

			if (json.getString("type").equals("teacher")) {
				isProfessional = true;

				Professional professional = new Professional(now_id, name,
						URLDecoder.decode(json.getString("certification"),
								"UTF-8"), json.getInt("price"),
						URLDecoder.decode(json.getString("profile"), "UTF-8"),
						URLDecoder.decode(json.getString("photo"), "UTF-8"),
						URLDecoder.decode(json.getString("url"), "UTF-8"), 0,
						json.getBoolean("active") ? 1 : 0,
						json.getBoolean("status") ? 1 : 0,
						json.getString("status_message"),
						json.getInt("evaluation_count"),
						json.getDouble("evaluation_score"), URLDecoder.decode(
								json.getString("company"), "UTF-8"));
				DBConnector db = new DBConnector(context);
				db.addProfessional(professional);
				db.close();
			} else {
				isProfessional = false;
			}
			count = json.getInt("count");

			photo = now_photo;
			id = now_id;
			loginSuccess = true;
		} catch (Exception e) {
			Log.d("disp", "err : " + e.getMessage());
		}
	}
}
