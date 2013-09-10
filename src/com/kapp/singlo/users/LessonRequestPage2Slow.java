package com.kapp.singlo.users;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.kapp.singlo.R;
import com.kapp.singlo.billing.IabHelper;
import com.kapp.singlo.billing.IabResult;
import com.kapp.singlo.billing.Purchase;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LessonRequestPage2Slow extends Activity {

	private TextView priceTextView;
	private TextView countTextView;
	private ImageButton paymentImageButton;
	private Button allProfessionalButton;
	private Button likeProfessionalButton;

	private ListView listView;

	private int price;
	private int id;
	private int sw_device;

	private Uri video;
	private String question;

	private Boolean submitProcess;
	private boolean status;

	private ArrayList<Professional> arrayData;
	private LessonRequestPageAdapter adapter;

	private SparseArray<Professional> selectedData;
	private ProgressDialog processDialog;
	private SubmitQuestion submitQuestion;

	// sajo
	private String MASTER_PRO = "com.kapp.singlo.masterpro";
	private String SEMI_PRO = "com.kapp.singlo.semipro";
	private String PRO = "com.kapp.singlo.pro";
	private String HEAD_PRO = "com.kapp.singlo.headpro";

	private String PRO_EVENT = "com.kapp.singlo.pro_event";
	private String SEMI_PRO_EVENT = "com.kapp.singlo.semipro_event";

	private int REQUEST_CODE_PURCHASE = 1001;
	private int purchaseItemCount = 0;
	private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
	private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener;
	private IabHelper mHelper;
	private Activity mActivity;
	private HashMap<Integer, String> levelHashMap;

	private ArrayList<Purchase> purchaseList;

	// List additionalSkuList = new List();
	// additionalSkuList.add("com.kapp.singlo.masterpro");
	// additionalSkuList.add("com.kapp.singlo.semipro");
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_request_page2_slow);
		levelHashMap = new HashMap<Integer, String>();
		levelHashMap.put(29900, MASTER_PRO);
		levelHashMap.put(9900, HEAD_PRO);
		levelHashMap.put(5900, PRO_EVENT); // PRO
		levelHashMap.put(3900, SEMI_PRO_EVENT); // SEMI_PRO
		levelHashMap.put(2900, SEMI_PRO_EVENT); // SEMI_PRO

		mActivity = this;
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzmpFd1j/O1CYxR6k5QKjozel0TtFhWTK6aeWB8aIp9R4W6SH//DO/9DutcGDAGs3IWBjPeYA+DLI7A3Qjx2j50K4IdYXWtKeFIQVMatuSUZxwzgB6Aj90trhmbkdPpAAlaMgqyW/ynV8lrRilV6tnzV43RNW/UcQvBL74sq+m10QMQEekOuK4i/eOw2qpwfyyz4mV5DOsMoec0d9pmHjmq+UGyRSCaV6g6Sx79PgiMfYspTtyTuYaoZDQuPQFnLvCbAV2cbcpTdZVmuAU6morsuQl+7XHYowjz+7Fg2P5jfZ6k3UmeTtEQD+8AE3YA56I7VBPIYQ3Y/1akpXymohQIDAQAB";

		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				// TODO Auto-generated method stub
				if (!result.isSuccess()) {
					Log.d("start setup error", result.getMessage());
					// Oh noes, there was a problem.

				}
				readyForPurchase();
				// Hooray, IAB is fully set up!
			}
		});
		purchaseList = new ArrayList<Purchase>();

		SharedPreferences spLogin = getSharedPreferences("login",
				Activity.MODE_PRIVATE);
		id = spLogin.getInt("id", 0);

		Intent intent = this.getIntent();
		sw_device = intent.getIntExtra("swing_device", 0);
		video = Uri.parse(intent.getStringExtra("video"));
		question = intent.getStringExtra("question");

		submitProcess = false;

		priceTextView = (TextView) findViewById(R.id.PriceTextView);
		countTextView = (TextView) findViewById(R.id.CountTextView);

		arrayData = new ArrayList<Professional>();
		adapter = new LessonRequestPageAdapter(getBaseContext(),
				android.R.layout.simple_list_item_1, arrayData);

		listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(listViewSetOnItemClickListener);
		listView.setAdapter(adapter);

		allProfessionalButton = (Button) findViewById(R.id.AllProfessionalButton);
		allProfessionalButton
				.setOnClickListener(allProfessionalButtonClickListener);
		likeProfessionalButton = (Button) findViewById(R.id.LikeProfessionalButton);
		likeProfessionalButton
				.setOnClickListener(likeProfessionalButtonClickListener);
		paymentImageButton = (ImageButton) findViewById(R.id.PaymentImageButton);
		paymentImageButton
				.setOnClickListener(paymentImageButtonOnClickListener);
		price = 0;

		selectedData = new SparseArray<Professional>();

		loading_list();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//if (mHelper != null)
		//	mHelper.dispose();
		//mHelper = null;
	}

	private void loading_list() {
		DBConnector dbConnector = new DBConnector(this);

		ArrayList<Professional> professionals = (ArrayList<Professional>) dbConnector
				.getAllProfessional();

		arrayData.clear();

		boolean interested = false;
		if (adapter != null) {
			interested = adapter.getInterested();
		}

		if (!interested) {
			allProfessionalButton
					.setBackgroundResource(R.drawable.shorttabon_btn);
			allProfessionalButton.setTextColor(Color.parseColor("#FF34A93A"));
			likeProfessionalButton
					.setBackgroundResource(R.drawable.shorttaboff_btn);
			likeProfessionalButton.setTextColor(Color.parseColor("#FF000000"));

			for (int i = 0; i < professionals.size(); i++) {
				if (professionals.get(i).getActive() == 0) {
					continue;
				}
				arrayData.add(professionals.get(i));
			}
		} else {
			allProfessionalButton
					.setBackgroundResource(R.drawable.shorttaboff_btn);
			allProfessionalButton.setTextColor(Color.parseColor("#FF000000"));
			likeProfessionalButton
					.setBackgroundResource(R.drawable.shorttabon_btn);
			likeProfessionalButton.setTextColor(Color.parseColor("#FF34A93A"));

			for (int i = 0; i < professionals.size(); i++) {
				if (professionals.get(i).getLike() == 0
						|| professionals.get(i).getActive() == 0) {
					continue;
				}
				arrayData.add(professionals.get(i));
			}

		}
		dbConnector.close();
	}

	OnItemClickListener listViewSetOnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int index,
				long id) {

			Professional professional = adapter.getItem(index);
			if (professional.getStatus() == 0) {
				String msg = "해당 프로님은 부재중입니다.";
				AlertDialog.Builder gsDialog = new AlertDialog.Builder(
						LessonRequestPage2Slow.this);
				gsDialog.setMessage(msg);
				gsDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								//
							}
						}).create().show();
			} else {
				if (selectedData.get(professional.getServerId()) == null) {
					selectedData.append(professional.getServerId(),
							professional);
					adapter.setCheck(index);
					price += professional.getPrice();
				} else {
					selectedData.remove(professional.getServerId());
					adapter.removeCheck(index);
					price -= professional.getPrice();
				}

				priceTextView.setText("￦" + price);
				countTextView.setText(selectedData.size() + "명");

				adapter.notifyDataSetChanged();
			}
		}
	};

	private OnClickListener allProfessionalButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!adapter.getInterested()) {
				return;
			}
			adapter.setIntrested(false);
			loading_list();
			adapter.notifyDataSetChanged();
		}
	};
	private OnClickListener likeProfessionalButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (adapter.getInterested()) {
				return;
			}
			adapter.setIntrested(true);
			loading_list();
			adapter.notifyDataSetChanged();
		}
	};

	OnClickListener paymentImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (submitProcess) {
				return;
			}
			if (selectedData.size() == 0) {
				Toast.makeText(LessonRequestPage2Slow.this,
						"적어도 한 명 이상의 프로를 선택해야합니다.", Toast.LENGTH_LONG).show();
				return;
			}
			submitProcess = true;
			status = true;

			purchaseItemCount = 0;
			// TODO: 주석 삭제. 그래야 결제 과정 넘어갈 수 있음.
			// requestPurchase();
			uploadVideo();
		}
	};

	private class SubmitQuestion extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			try {
				submitQuestion();
				return "download completed";
			} catch (IOException e) {
				Log.d("debug", "The msg is : " + e.getMessage());
				return "download failed";
			}
		}

		protected void onPostExecute(String result) {
			processDialog.dismiss();
			submitProcess = false;
			if (status == false) {
				Toast.makeText(LessonRequestPage2Slow.this,
						"답변 전송 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(LessonRequestPage2Slow.this, "레슨이 신청되었습니다.",
					Toast.LENGTH_SHORT).show();

			if (LessonRequestPage1.lessonRequestPage1Activity != null) {
				LessonRequestPage1.lessonRequestPage1Activity.finish();
			}
			Intent intent = new Intent(LessonRequestPage2Slow.this,
					Mylesson.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();
		}

		private void submitQuestion() throws IOException {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(video, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String fileName = cursor.getString(column_index);

			FileInputStream fileInputStream = new FileInputStream(fileName);
			URL url = new URL(Const.LESSON_ASK_SLOW_URL);

			Log.d("File Up", "mFileInputStream is " + fileInputStream);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + Const.boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			// uploadedfile 파일이 ashx 핸들러에서 파일을 찾을 때 사용함으로 이름이 반드시 동일해야함..
			// 이름을 바꾸면 ashx 파일에서도 바꿀것.

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"user_id\""
					+ Const.lineEnd + Const.lineEnd + id + Const.lineEnd);

			for (int i = 0; i < selectedData.size(); i++) {
				Professional professional = selectedData.valueAt(i);
				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"teacher_id[]\""
						+ Const.lineEnd
						+ Const.lineEnd
						+ professional.getServerId() + Const.lineEnd);
				//dos.writeBytes("Content-Disposition:form-data; name=\"purchase_id[]\""
				//		+ Const.lineEnd
				//		+ Const.lineEnd
				//		+ purchaseList.get(i).getToken() + Const.lineEnd);
				Log.d("SAJO",
						"professional.getServerId() : "
								+ professional.getServerId());
			}

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"club_type\""
					+ Const.lineEnd + Const.lineEnd + sw_device + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"question\""
					+ Const.lineEnd + Const.lineEnd + question + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"video\"; filename=\""
					+ fileName + "\"" + Const.lineEnd + Const.lineEnd);

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 8192;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			Log.d("File Up", "image byte is " + bytesRead);

			// Read 파일
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(Const.lineEnd);
			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.twoHyphens
					+ Const.lineEnd);

			// close streams
			Log.e("File Up", "File is written");
			fileInputStream.close();
			dos.flush(); // 버퍼에 있는 값을 모두 밀어냄

			InputStream is = conn.getInputStream();

			try {
				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);
				if (json.getString("result").equals("fail")) {
					status = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			dos.close();
		};
	}

	private void readyForPurchase() {
		mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
			public void onConsumeFinished(Purchase purchase, IabResult result) {
				if (result.isSuccess()) {
					if (purchaseItemCount == selectedData.size()) {
						// 모든 결제 완료
						Log.d("SAJO", "uploadVideo");
						uploadVideo();
					} else {
						Log.d("SAJO", "requestPurchase");
						requestPurchase();
					}
				} else {
					submitProcess = false;
					// handle error
				}
			}
		};
		mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			@Override
			public void onIabPurchaseFinished(IabResult result,
					Purchase purchase) {
				// TODO Auto-generated method stub
				if (result.isFailure()) {
					Log.d("SAJO", "Error purchasing: " + result);
					submitProcess = false;
					return;
				} else {
					purchaseList.add(purchase);
					Log.d("SAJO", "item : " + purchase.getSku());
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);

				}
			}

		};
	}

	private void requestPurchase() {
		Professional professional = selectedData.valueAt(purchaseItemCount);

		String item = levelHashMap.get(professional.getPrice());

		if (item != null) {
			Log.d("SAJO", professional.getPrice() + " : " + item);
			mHelper.launchPurchaseFlow(mActivity, item, REQUEST_CODE_PURCHASE,
					mPurchaseFinishedListener);
		} else {
			Log.d("SAJO", professional.getPrice() + " : " + "null");
		}
		purchaseItemCount++;

	}

	private void uploadVideo() {

		processDialog = new ProgressDialog(LessonRequestPage2Slow.this);
		processDialog.setMessage("동영상을 업로드 하고 있습니다.");
		processDialog.setIndeterminate(false);
		processDialog.show();

		submitQuestion = new SubmitQuestion();
		submitQuestion.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
		} else {
			if (requestCode == REQUEST_CODE_PURCHASE) {
				/*
				 * BILLING_RESPONSE_RESULT_OK 0 Success
				 * BILLING_RESPONSE_RESULT_USER_CANCELED 1 User pressed back or
				 * canceled a dialog BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE
				 * 3 Billing API version is not supported for the type requested
				 * BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE 4 Requested product
				 * is not available for purchase
				 * BILLING_RESPONSE_RESULT_DEVELOPER_ERROR 5 Invalid arguments
				 * provided to the API. This error can also indicate that the
				 * application was not correctly signed or properly set up for
				 * In-app Billing in Google Play, or does not have the necessary
				 * permissions in its manifest BILLING_RESPONSE_RESULT_ERROR 6
				 * Fatal error during the API action
				 * BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED 7 Failure to
				 * purchase since item is already owned
				 * BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED 8 Failure to consume
				 * since item is not owned
				 */

				// '{
				// "orderId":"12999763169054705758.1371079406387615",
				// "packageName":"com.example.app",
				// "productId":"exampleSku",
				// "purchaseTime":1345678900000,
				// "purchaseState":0,
				// "developerPayload":"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ",
				// "purchaseToken":"rojeslcdyyiapnqcynkjyyjh"
				// }'
				// int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
				// String purchaseData =
				// data.getStringExtra("INAPP_PURCHASE_DATA");
				// String dataSignature =
				// data.getStringExtra("INAPP_DATA_SIGNATURE");
				// Log.d("SAJO", "responseCode : " + responseCode);
				// Log.d("SAJO", "purchaseData : " + purchaseData);
				// Log.d("SAJO", "dataSignature : " + dataSignature);
				// if (responseCode == 0) { //결제 성공
				// JSONObject jo;
				// try {
				// jo = new JSONObject(purchaseData);
				// String sku = jo.getString("productId");
				// Log.d("SAJO", "productId : " + sku);
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			} else { // 결제 실패
				Log.d("SAJO", "purchase failed");
			}
		}

	}

}
