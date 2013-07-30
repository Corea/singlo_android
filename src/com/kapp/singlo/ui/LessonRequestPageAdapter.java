package com.kapp.singlo.ui;

import java.util.ArrayList;

import com.kapp.singlo.R;
import com.kapp.singlo.data.Professional;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LessonRequestPageAdapter extends ArrayAdapter<Professional> {

	private Context context;
	private ArrayList<Professional> items;
	private SparseBooleanArray checkList;
	
	private boolean is_interested;

	
	public LessonRequestPageAdapter(Context context, int textViewResourceId,
			ArrayList<Professional> items) {
		super(context, textViewResourceId, items);

		this.context = context;
		this.items = items;
		
		checkList = new SparseBooleanArray();
		is_interested = false;
	}

	public void setCheck(int index) {
		checkList.append(items.get(index).getServerId(), true);
	}

	public void removeCheck(int index) {
		checkList.delete(items.get(index).getServerId());
	}

	public void setIntrested(Boolean interested) {
		is_interested = interested;
	}

	public boolean getInterested() {
		return is_interested;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.lesson_request_page2_slow_item, null);
		}

		Professional professional = items.get(index);
		if (professional != null) {
			TextView nameTextView = (TextView) v
					.findViewById(R.id.NameTextView);
			TextView priceTextView = (TextView) v
					.findViewById(R.id.PriceTextView);
			TextView certificationTextView = (TextView) v
					.findViewById(R.id.CertificationTextView);
			ImageView statusImageView = (ImageView) v
					.findViewById(R.id.StatusImageView);


			nameTextView.setText(professional.getName() + "/");
			priceTextView.setText("￦" + professional.getPrice());
			certificationTextView.setText(professional.getCertification());
			if (checkList.get(professional.getServerId()) == true) {
				statusImageView.setImageResource(R.drawable.selected_checkbox);
			} else {
				statusImageView.setImageResource(R.drawable.nonselected_checkbox);
			}

			nameTextView.setFocusable(false);
			priceTextView.setFocusable(false);
			certificationTextView.setFocusable(false);
			statusImageView.setFocusable(false);
		}
		return v;

	}
}
