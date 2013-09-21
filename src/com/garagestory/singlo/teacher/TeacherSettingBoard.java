package com.garagestory.singlo.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import com.garagestory.singlo.R;
import com.garagestory.singlo.action.BoardAction;
import com.garagestory.singlo.adapter.SettingBoardAdapter;
import com.garagestory.singlo.meta.SingloTeacherActivity;
import com.garagestory.singlo.util.Const;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: corea
 * Date: 13. 9. 21.
 * Time: 오후 6:08
 * To change this template use File | Settings | File Templates.
 */
public class TeacherSettingBoard extends SingloTeacherActivity {

    private String board_name;

    private ListView boardListView;

    private ProgressDialog progressDialog;
    private BoardAction boardAction;
    private SettingBoardAdapter settingBoardAdapter;

    private ArrayList<BoardAction.Article> articleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_board);

        setTopMenu(3);

        Intent intent = this.getIntent();
        board_name = intent.getStringExtra("board_name");
        articleArrayList = new ArrayList<BoardAction.Article>();

        progressDialog = ProgressDialog.show(TeacherSettingBoard.this, "", "준비중입니다.", true,
                false);

        HashMap<String, String> mParam = new HashMap<String, String>();
        mParam.put("board_name", board_name);
        boardAction = new BoardAction(Const.BOARD_URL, mBoardListener);
        boardAction.execute(mParam);
    }


    private BoardAction.BoardListener mBoardListener = new BoardAction.BoardListener() {

        @Override
        public void result(ArrayList<BoardAction.Article> articleArrayList) {
            if (articleArrayList != null) {
                TeacherSettingBoard.this.articleArrayList = articleArrayList;

                boardListView = (ListView) findViewById(R.id.BoardListView);
                settingBoardAdapter = new SettingBoardAdapter(getBaseContext(),
                        android.R.layout.simple_list_item_1, (ArrayList<BoardAction.Article>)articleArrayList.clone());
                boardListView.setAdapter(settingBoardAdapter);
                settingBoardAdapter.notifyDataSetChanged();
            }

            progressDialog.dismiss();
        }
    };

}
