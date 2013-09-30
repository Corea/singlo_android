package com.garagestory.singlo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.garagestory.singlo.R;
import com.garagestory.singlo.action.BoardAction;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: corea
 * Date: 13. 9. 15.
 * Time: 오후 10:36
 * To change this template use File | Settings | File Templates.
 */
public class SettingBoardAdapter extends ArrayAdapter<BoardAction.Article> {
    private Context mContext;

    private ArrayList<BoardAction.Article> noticeArrayList;
    private ArrayList<View> viewArrayList;

    public SettingBoardAdapter(Context context, int textViewResourceId, ArrayList<BoardAction.Article> noticeArrayList) {
        super(context, textViewResourceId, noticeArrayList);
        this.mContext = context;
        this.noticeArrayList = noticeArrayList;
        this.viewArrayList = new ArrayList<View>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_board_item, null);
        }

        try {
            BoardAction.Article mNoticeData = noticeArrayList.get(position);

            RelativeLayout boardRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.BoardRelativeLayout);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.TitleTextView);
            TextView contentTextView = (TextView) convertView.findViewById(R.id.ContentTextView);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.DateTextView);

            boardRelativeLayout.setTag(position);
            titleTextView.setText(mNoticeData.title);
            contentTextView.setText(mNoticeData.content);
            dateTextView.setText(mNoticeData.date);

            viewArrayList.add(convertView);
            boardRelativeLayout.setOnClickListener(boardRelativeLayoutOnClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private View.OnClickListener boardRelativeLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = (Integer) view.getTag();

            Log.d("index", String.valueOf(index));
            View convertView = viewArrayList.get(index);
            TextView contentTextView = (TextView) convertView.findViewById(R.id.ContentTextView);
            ImageButton toggleContentImageButton = (ImageButton) convertView.findViewById(R.id.ToggleContentImageButton);

            if (contentTextView.getVisibility() == View.GONE) {
                contentTextView.setVisibility(View.VISIBLE);
                contentTextView.setBackgroundResource(R.drawable.singlo_setting_notice_border);
                toggleContentImageButton.setImageResource(R.drawable.hideinfo_btn);
            } else {
                contentTextView.setVisibility(View.GONE);
                contentTextView.setBackgroundResource(android.R.color.transparent);
                toggleContentImageButton.setImageResource(R.drawable.showinfo_btn);
            }
            SettingBoardAdapter.this.notifyDataSetChanged();
        }
    };
}
