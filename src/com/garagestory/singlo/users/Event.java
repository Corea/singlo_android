package com.garagestory.singlo.users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.garagestory.singlo.R;
import com.garagestory.singlo.action.EventAction;
import com.garagestory.singlo.action.EventAction.eventListener;
import com.garagestory.singlo.util.Const;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: corea
 * Date: 13. 9. 15.
 * Time: 오전 1:33
 * To change this template use File | Settings | File Templates.
 */
public class Event extends Activity {

    private ViewPager mViewPager;
    private ProgressDialog progressDialog;
    private EventAction eventAction;

    private ArrayList<Bitmap> bitmapList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);

        progressDialog = ProgressDialog.show(Event.this, "", "준비중입니다.", true,
                false);
        eventAction = new EventAction(this, Const.EVENT_URL, mEventListener);
        eventAction.execute();

    }

    private void endAction() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private eventListener mEventListener = new eventListener() {

        @Override
        public void result(ArrayList<String> imageList) {
            setEventList(imageList);
        }
    };

    private View.OnClickListener closeImageButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            endAction();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                endAction();
        }

        return false;
    }


    private void setEventList(ArrayList<String> imageList) {
        if (imageList.size() > 0) {
            bitmapList = new ArrayList<Bitmap>();

            for (String image : imageList) {
                String path = Const.EVENT_IMAGE_URL + image;
                File cacheDir = this.getCacheDir();
                cacheDir.mkdirs();
                File cacheFile = new File(cacheDir, "" + path.hashCode());
                bitmapList.add(BitmapFactory.decodeFile(cacheFile
                        .getAbsolutePath()));
            }

            mViewPager = (ViewPager) findViewById(R.id.ViewPager);
            mViewPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
            endAction();
        }
    }

    private class PagerAdapterClass extends PagerAdapter {

        private LayoutInflater mInflater;
        private ImageButton closeImageButton;

        public PagerAdapterClass(Context context) {
            super();
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return bitmapList.size();
        }


        @Override
        public Object instantiateItem(View container, final int position) {

            View mView = mInflater.inflate(R.layout.event_item, null);
            ImageView mAdImg = (ImageView) mView
                    .findViewById(R.id.EventImageView);
            try {
                mAdImg.setImageBitmap(bitmapList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeImageButton = (ImageButton) mView.findViewById(R.id.CloseImageButton);
            closeImageButton.setOnClickListener(closeImageButtonOnClickListener);

            ((ViewPager) container).addView(mView, 0);

            return mView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


}
