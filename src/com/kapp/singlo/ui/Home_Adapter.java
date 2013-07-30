package com.kapp.singlo.ui;

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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

public class Home_Adapter extends ArrayAdapter<Professional> {
	private ArrayList<Professional> items;

	private static final int likeImage = R.drawable.prolist_fav01;
	private static final int unlikeImage = R.drawable.prolist_fav02;

	LinearLayout likestar;
	ImageView inlike;

	private boolean is_interested;

	private int user_id;
	private Context context;

	public Home_Adapter(Context context, int textViewResourceId,
			ArrayList<Professional> items) {
		super(context, textViewResourceId, items);

		this.context = context;
		this.items = items;

		is_interested = false;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public void setIntrested(Boolean interested) {
		is_interested = interested;
	}

	public boolean getInterested() {
		return is_interested;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {

			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.home_item, null);
		}
		Professional professional = items.get(position);

		if (professional != null) {
			WebView ind = (WebView) v.findViewById(R.id.webView1);
			TextView ind_01 = (TextView) v.findViewById(R.id.TextView_01);
			TextView ind_02 = (TextView) v.findViewById(R.id.TextView_02);
			TextView ind_03 = (TextView) v.findViewById(R.id.TextView_03);
			TextView ind_04 = (TextView) v.findViewById(R.id.TextView_04);
			inlike = (ImageView) v.findViewById(R.id.ImageView_9);

			String image_url = "http://garagestory.cafe24.com/img/teacher/"
					+ professional.getPhoto();
			ind.loadDataWithBaseURL(null,
					Home_Adapter.getImageHtmlCode(image_url), "text/html",
					"utf-8", null);

			ind.setBackgroundResource(R.anim.shape);
			ind.setPadding(1, 1, 1, 1);

			ind_01.setText(professional.getName() + "/");
			ind_02.setText("￦" + professional.getPrice());
			ind_03.setText(professional.getCertification());
			ind_04.setText("추천 레슨 : " + professional.getLesson());
			inlike.setImageResource((professional.getLike() == 1) ? likeImage
					: unlikeImage);

			// focus disable
			ind.setFocusable(false);
			ind.setClickable(false);
			ind.setLongClickable(false);
			ind.setFocusableInTouchMode(false);
			ind.setHorizontalScrollBarEnabled(false);
			ind.setVerticalScrollBarEnabled(false);
			ind.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

			ind_01.setFocusable(false);
			ind_02.setFocusable(false);
			ind_03.setFocusable(false);
			ind_04.setFocusable(false);

			likestar = (LinearLayout) v.findViewById(R.id.likestar);
			likestar.setOnClickListener(likestarOnClickListener);
			likestar.setTag(position);
	
		}

		return v;
	}

	public static String getImageHtmlCode(String _imageURL) {
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("<style type='text/css'>");
		sb.append("body {");
		sb.append("margin-left: 0px;");
		sb.append("margin-top: 0px;");
		sb.append("margin-right: 0px;");
		sb.append("margin-bottom: 0px;");
		sb.append("}");
		sb.append("</style>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<img width=\"100%\" height=\"100%\" src=\"" + _imageURL
				+ "\"/>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		return sb.toString();
	}

	private OnClickListener likestarOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			ImageView imglike = (ImageView) v.findViewById(R.id.ImageView_9);

			int index = (Integer) v.getTag();

			Log.d("index", String.valueOf(index));

			if (!is_interested) {
				if (items.get(index).getLike() == 0) {
					imglike.setImageResource(likeImage);
					items.get(index).setLike(1);
				} else {
					imglike.setImageResource(unlikeImage);
					items.get(index).setLike(0);
				}

				likestar(index);

			} else {
				// TODO: 관심 골퍼들 중에서 빼고 더하기 가능하게
				int count = 0;
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i).getLike() == 1) {
						if (count == index) {
							index = i;
							break;
						}
						count++;
					}
				}

				imglike.setImageResource(unlikeImage);
				items.get(index).setLike(0);
				likestar(index);
				items.remove(index);
				Home_Adapter.this.notifyDataSetChanged();

				/*
				 * int flag = -1; for (int i = 0; i < professionals.size(); i++)
				 * { if
				 * (Home.array_name[pos].equals(professionals.get(i).getName()))
				 * { flag=i; } Log.d("array_name", Home.array_name[pos]);
				 * Log.d("teacher_name", professionals.get(i).getName()); }
				 * pos=flag; Log.d("search_pos", String.valueOf(pos)); if
				 * (Home.likestar_check[pos]!=1) {
				 * imglike.setImageResource(R.drawable.prolist_fav01);
				 * Home.likestar_check[pos]=1; } else if
				 * (Home.likestar_check[pos]==1) {
				 * imglike.setImageResource(R.drawable.prolist_fav02);
				 * Home.likestar_check[pos]=0; }
				 * 
				 * likestar(pos);
				 */

			}

		}

	};

	private LikeTeacherAyncTask likeTeacherAyncTask;

	void likestar(int index) {
		Log.d("index", String.valueOf(index));

		String return_like = "";

		likeTeacherAyncTask = new LikeTeacherAyncTask();
		likeTeacherAyncTask.execute(user_id, items.get(index).getServerId(),
				items.get(index).getLike());

		Log.d("upload", "change_userlikestar : " + return_like);
	}

	public class LikeTeacherAyncTask extends AsyncTask<Integer, Void, Void> {

		private int user_id;
		private int teacher_id;
		private int status;
		private Professional professional;

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
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getServerId() == professional.getServerId()) {
					items.get(i).setLike(professional.getLike());
					break;
				}
			}
			Home_Adapter.this.notifyDataSetChanged();
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
					Toast.makeText(context, "관심 프로 변경에 실패하였습니다.",
							Toast.LENGTH_LONG).show();
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
}