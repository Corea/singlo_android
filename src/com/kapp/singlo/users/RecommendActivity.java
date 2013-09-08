/**
 * 
 */
package com.kapp.singlo.users;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.kapp.singlo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Baek
 * 
 */
public class RecommendActivity extends Activity {

	private AQuery mAq;
	private ViewPager mViewPager;
	private ArrayList<Integer> recommendIDList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.recommend_image_activity);

		init();

	}

	private void init() {

		recommendIDList = new ArrayList<Integer>();
		recommendIDList.add(R.drawable.recommend_1);
		recommendIDList.add(R.drawable.recommend_2);
		recommendIDList.add(R.drawable.recommend_3);
		recommendIDList.add(R.drawable.recommend_4);
		recommendIDList.add(R.drawable.recommend_5);
		recommendIDList.add(R.drawable.recommend_6);
		recommendIDList.add(R.drawable.recommend_7);
		recommendIDList.add(R.drawable.recommend_8);
		recommendIDList.add(R.drawable.recommend_9);
		recommendIDList.add(R.drawable.recommend_10);
		recommendIDList.add(R.drawable.recommend_11);
		recommendIDList.add(R.drawable.recommend_12);
		recommendIDList.add(R.drawable.recommend_13);
		recommendIDList.add(R.drawable.recommend_14);
		recommendIDList.add(R.drawable.recommend_15);
		recommendIDList.add(R.drawable.recommend_16);
		recommendIDList.add(R.drawable.recommend_17);
		recommendIDList.add(R.drawable.recommend_18);
		recommendIDList.add(R.drawable.recommend_19);
		recommendIDList.add(R.drawable.recommend_20);
		recommendIDList.add(R.drawable.recommend_21);
		recommendIDList.add(R.drawable.recommend_22);
		recommendIDList.add(R.drawable.recommend_23);
		recommendIDList.add(R.drawable.recommend_24);
		recommendIDList.add(R.drawable.recommend_25);
		recommendIDList.add(R.drawable.recommend_26);
		recommendIDList.add(R.drawable.recommend_27);
		recommendIDList.add(R.drawable.recommend_28);
		recommendIDList.add(R.drawable.recommend_29);
		recommendIDList.add(R.drawable.recommend_30);
		recommendIDList.add(R.drawable.recommend_31);
		recommendIDList.add(R.drawable.recommend_32);
		recommendIDList.add(R.drawable.recommend_33);
		recommendIDList.add(R.drawable.recommend_34);
		recommendIDList.add(R.drawable.recommend_35);
		recommendIDList.add(R.drawable.recommend_36);
		recommendIDList.add(R.drawable.recommend_37);
		recommendIDList.add(R.drawable.recommend_38);
		recommendIDList.add(R.drawable.recommend_39);
		recommendIDList.add(R.drawable.recommend_40);
		recommendIDList.add(R.drawable.recommend_41);
		recommendIDList.add(R.drawable.recommend_42);
		recommendIDList.add(R.drawable.recommend_43);
		recommendIDList.add(R.drawable.recommend_44);
		recommendIDList.add(R.drawable.recommend_45);
		recommendIDList.add(R.drawable.recommend_46);
		recommendIDList.add(R.drawable.recommend_47);
		recommendIDList.add(R.drawable.recommend_48);
		recommendIDList.add(R.drawable.recommend_49);
		recommendIDList.add(R.drawable.recommend_50);
		recommendIDList.add(R.drawable.recommend_51);

		ArrayList<Integer> mRecommendArray = new ArrayList<Integer>();
		mRecommendArray = (ArrayList<Integer>) getIntent()
				.getSerializableExtra("recommend");

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new PagerAdapterClass(getApplicationContext(),
				mRecommendArray));
	}

	private class PagerAdapterClass extends PagerAdapter {

		private LayoutInflater mInflater;
		private ArrayList<Integer> mRecommendImgArray;

		public PagerAdapterClass(Context c, ArrayList<Integer> data) {
			super();
			mInflater = LayoutInflater.from(c);
			this.mRecommendImgArray = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mRecommendImgArray.size();
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			// TODO Auto-generated method stub

			View mView = null;

			mView = mInflater.inflate(R.layout.recommend_image_item, null);
			ImageView mAdImg = (ImageView) mView
					.findViewById(R.id.recommend_img);
			mAdImg.setImageResource(recommendIDList.get(mRecommendImgArray
					.get(position) - 1));
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
