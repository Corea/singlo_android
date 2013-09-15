package com.garagestory.singlo.action;

import android.content.Context;
import com.garagestory.singlo.bg.APIGetAction;
import com.garagestory.singlo.util.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: corea
 * Date: 13. 9. 15.
 * Time: 오후 6:01
 * To change this template use File | Settings | File Templates.
 */

public class EventAction extends APIGetAction {

    private Context context;
    private eventListener mListener;


    public EventAction(Context context, String url, eventListener listener) {
        super(url);
        this.context = context;
        this.mListener = listener;
    }

    @Override
    protected void onActionPost(JSONObject object) {
        try {
            ArrayList<String> mListData = new ArrayList<String>();
            JSONArray mJSONArray = object.getJSONArray("events");

            for(int i = 0; i < mJSONArray.length(); i++){
                JSONObject mObject = mJSONArray.getJSONObject(i);
                mListData.add(mObject.getString("image"));
                getFile(mObject.getString("image"));
            }
            mListener.result(mListData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getFile(String filename) {
        String path = Const.EVENT_IMAGE_URL + filename;

        File cacheDir = context.getCacheDir();
        cacheDir.mkdirs();
        File cacheFile = new File(cacheDir, "" + path.hashCode());
        if (cacheFile.exists()) {
            return;
        }

        try {
            URLConnection cn = new java.net.URL(path).openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();

            InputStream input = new BufferedInputStream(stream);
            FileOutputStream out = new FileOutputStream(cacheFile);

            byte buf[] = new byte[16384];
            int numread;

            while ((numread = stream.read(buf)) > 0) {
                out.write(buf, 0, numread);
            }

            out.flush();
            out.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface eventListener{
        public void result(ArrayList<String> imageList);
    }
}
