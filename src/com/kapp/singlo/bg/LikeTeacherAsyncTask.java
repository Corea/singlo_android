package com.kapp.singlo.bg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LikeTeacherAsyncTask extends AsyncTask<Integer, Void, Void> {

	Context context;

	private int user_id;
	private int teacher_id;
	private int status;
	private Professional professional;

	private CallbackListener callbackListener;

	public void setContext(Context context) {
		this.context = context;
	}

	public void setListener(CallbackListener listener) {
		callbackListener = listener;
	}

	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Integer... v) {
		user_id = v[0];
		teacher_id = v[1];
		status = v[2];

		like();

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		callbackListener.callback(professional);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	void like() {
		String url = Const.TEACHER_LIKE_URL;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		InputStream is;

		Log.d("Login", url);
		try {
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			nameValuePairs.add(new BasicNameValuePair("teacher_id", String
					.valueOf(teacher_id)));
			nameValuePairs.add(new BasicNameValuePair("status", String
					.valueOf(status)));

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			is = httpResponse.getEntity().getContent();

			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromStream(is);

			String result = json.getString("result");

			if (!result.equals("success")) {
				Toast.makeText(context, "관심 프로 변경에 실패하였습니다.", Toast.LENGTH_LONG)
						.show();
			} else {
				DBConnector dbConnector = new DBConnector(context);
				professional = dbConnector
						.getProfessionalByServerID(teacher_id);
				professional.setLike(1 - professional.getLike());
				dbConnector.updateProfessional(professional);
				dbConnector.close();
			}
		} catch (Exception e) {
			Log.d("disp", "err : " + e.getMessage());
		}
	}
}