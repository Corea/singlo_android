package com.kapp.singlo.teacher;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.util.Utility;

public class TeacherLesson_Adapter extends
		ArrayAdapter<TeacherLesson_List_Data> {

	private ArrayList<TeacherLesson_List_Data> items;

	public TeacherLesson_Adapter(Context context, int textViewResourceId,
			ArrayList<TeacherLesson_List_Data> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	LinearLayout likestar;
	ImageView inlike;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {

			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.teacher_lesson_item, null);
		}
		TeacherLesson_List_Data list_data = items.get(position);

		if (list_data != null) {
			WebView ind = (WebView) v.findViewById(R.id.webView1);
			TextView ind_01 = (TextView) v.findViewById(R.id.TextView_01);
			TextView ind_02 = (TextView) v.findViewById(R.id.TextView_02);
			TextView ind_03 = (TextView) v.findViewById(R.id.TextView_03);
			TextView ind_04 = (TextView) v.findViewById(R.id.TextView_04);
			inlike = (ImageView) v.findViewById(R.id.ImageView_9);

			ind.loadDataWithBaseURL(null,
					Utility.getImageHtmlCode(list_data.getImage_URL()),
					"text/html", "utf-8", null);
			ind.setBackgroundResource(R.anim.shape);
			ind.setPadding(1, 1, 1, 1);

			ind_01.setText(list_data.getTop_Titles());
			ind_02.setText(list_data.getMain_Titles());
			ind_03.setText(list_data.getSub_Titles());
			ind_04.setText(list_data.getText_Titles());
			inlike.setImageResource(list_data.getImage_PL());

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
		}

		return v;
	}
}

class TeacherLesson_List_Data {
	private String Image_URL;
	private String Top_Titles;
	private String Main_Titles;
	private String Sub_Titles;
	private String Text_Titles;
	private int Image_PL;

	public TeacherLesson_List_Data(String Image_URL, String Top_Titles,
			String Main_Titles, String Sub_Titles, String Text_Titles,
			int Image_PL) {
		this.setImage_URL(Image_URL);
		this.setTop_Titles(Top_Titles);
		this.setMain_Titles(Main_Titles);
		this.setSub_Titles(Sub_Titles);
		this.setText_Titles(Text_Titles);
		this.setImage_PL(Image_PL);
	}

	public String getImage_URL() {
		return Image_URL;
	}

	private void setImage_URL(String image_URL) {
		// TODO Auto-generated method stub
		Image_URL = image_URL;
	}

	public String getTop_Titles() {
		return Top_Titles;
	}

	public void setTop_Titles(String top_titles) {
		Top_Titles = top_titles;
	}

	public String getMain_Titles() {
		return Main_Titles;
	}

	public void setMain_Titles(String main_titles) {
		Main_Titles = main_titles;
	}

	public String getSub_Titles() {
		return Sub_Titles;
	}

	public void setSub_Titles(String sub_titles) {
		Sub_Titles = sub_titles;
	}

	public String getText_Titles() {
		return Text_Titles;
	}

	public void setText_Titles(String text_titles) {
		Text_Titles = text_titles;
	}

	public int getImage_PL() {
		return Image_PL;
	}

	public void setImage_PL(int image_PL) {
		Image_PL = image_PL;
	}

}