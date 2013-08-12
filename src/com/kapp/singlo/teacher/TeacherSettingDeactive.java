package com.kapp.singlo.teacher;

import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class TeacherSettingDeactive extends SingloTeacherActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_setting_deactive);

		setTopMenu(3);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(TeacherSettingDeactive.this,
					TeacherSetting.class);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}

		return false;
	}

}
