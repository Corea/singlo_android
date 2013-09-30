package com.garagestory.singlo.action;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.garagestory.singlo.bg.APIGetAction;
import com.garagestory.singlo.util.Utility;

public class BoardAction extends APIGetAction{

    public class Article {
        public int id;
        public String title;
        public String content;
        public String date;
        public Article() {

        }
    }

    private BoardListener mListener;

    public BoardAction(String url, BoardListener listener) {
        super(url);
        this.mListener = listener;
    }

    @Override
    protected void onActionPost(JSONObject object) {
        try {
            ArrayList<Article> mListData = new ArrayList<Article>();
            JSONArray mJSONArray = object.getJSONArray("articles");

            for(int i = 0; i < mJSONArray.length(); i++){
                JSONObject mObject = mJSONArray.getJSONObject(i);
                Article mData = new Article();
                mData.id = mObject.getInt("id");
                mData.title = Utility.strDecoder(mObject.getString("title"));
                mData.content = Utility.strDecoder(mObject.getString("content"));
                mData.date = Utility.strDecoder(mObject.getString("date"));
                mListData.add(mData);
            }

            mListener.result(mListData);
        } catch (JSONException e) {
            e.printStackTrace();
            mListener.result(null);
        }
    }

    public interface BoardListener{
        public void result(ArrayList<Article> list);
    }
}
