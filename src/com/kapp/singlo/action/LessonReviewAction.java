package com.kapp.singlo.action;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kapp.singlo.bg.APIGetAction;
import com.kapp.singlo.data.LessonReviewData;
import com.kapp.singlo.util.Utility;

public class LessonReviewAction extends APIGetAction{
	
	private lessonReviewListener mListener;

	public LessonReviewAction(String url, lessonReviewListener listener) {
		super(url);
		// TODO Auto-generated constructor stub
		this.mListener = listener;
	}

	@Override
	protected void onActionPost(JSONObject object) {
		// TODO Auto-generated method stub	
		try {
			ArrayList<LessonReviewData> mListData = new ArrayList<LessonReviewData>();
			JSONArray mJSONArray = object.getJSONArray("evaluations");
			
			for(int i = 0; i < mJSONArray.length(); i++){
				JSONObject mObject = mJSONArray.getJSONObject(i);
				LessonReviewData mData = new LessonReviewData();
				mData.id = Utility.strDecoder(mObject.getString("id"));
				mData.review = Utility.strDecoder(mObject.getString("review"));
				mData.user_name = Utility.strDecoder(mObject.getString("user_name"));
				mData.user_photo = Utility.strDecoder(mObject.getString("user_photo"));
				mData.created_datetime = Utility.strDecoder(mObject.getString("created_datetime"));
				mListData.add(mData);
			}
			
			mListener.result(mListData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public interface lessonReviewListener{
		public void result(ArrayList<LessonReviewData> list);
	}
}
