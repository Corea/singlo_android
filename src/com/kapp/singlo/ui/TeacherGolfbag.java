package com.kapp.singlo.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kapp.singlo.R;

@SuppressLint("NewApi")
public class TeacherGolfbag extends Activity {
/*
	ImageButton menu01;
	ImageButton menu02;
	ImageButton menu03;
	ImageButton menu04;
	
	WebView user_photo;
	
	TextView user_name;
	TextView user_id;
	TextView user_golfcash;
	
	ImageButton indexmenu01;
	ImageButton indexmenu02;
	ImageButton indexmenu03;
	ImageButton submenu01;
	ImageButton submenu02;
	ImageButton buybutton;
	
	static int choice_count[]=new int[3];
	static String google_id="";
	
	public static SharedPreferences supersave;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teachergolfbag);
		
		menu01 = (ImageButton)findViewById(R.id.menu01);
			menu01.setOnTouchListener(menu01TouchListener);
		menu02 = (ImageButton)findViewById(R.id.menu02);
			menu02.setOnTouchListener(menu02TouchListener);
		menu03 = (ImageButton)findViewById(R.id.menu03);
			menu03.setOnTouchListener(menu03TouchListener);
		menu04 = (ImageButton)findViewById(R.id.menu04);
			menu04.setOnTouchListener(menu04TouchListener);
		submenu01 = (ImageButton)findViewById(R.id.submenu01);
			submenu01.setOnTouchListener(submenu01TouchListener);
		submenu02 = (ImageButton)findViewById(R.id.submenu02);		
			submenu02.setOnTouchListener(submenu02TouchListener);
			
		indexmenu01 = (ImageButton)findViewById(R.id.indexmenu01);
			indexmenu01.setOnTouchListener(indexmenu01TouchListener);
		indexmenu02 = (ImageButton)findViewById(R.id.indexmenu02);
			indexmenu02.setOnTouchListener(indexmenu02TouchListener);
		indexmenu03 = (ImageButton)findViewById(R.id.indexmenu03);
			indexmenu03.setOnTouchListener(indexmenu03TouchListener);
		buybutton = (ImageButton)findViewById(R.id.buybutton);
			buybutton.setOnTouchListener(buybuttonTouchListener);
			
		menu01.setBackgroundResource(R.drawable.main_top_menua01);
		menu02.setBackgroundResource(R.drawable.main_top_menub01);
		menu03.setBackgroundResource(R.drawable.main_top_menuc02);
		menu04.setBackgroundResource(R.drawable.main_top_menud01);
		
		submenu01.setBackgroundResource(R.drawable.golfbag_button_charge02);
		submenu02.setBackgroundResource(R.drawable.golfbag_button_history01);
	    
		google_id=getAccountName().toString();
		
		user_photo = (WebView)findViewById(R.id.user_photo);
			user_photo.setHorizontalScrollBarEnabled(false); // 세로 scroll 제거
			user_photo.setVerticalScrollBarEnabled(false); // 가로 scroll 제거            
			user_photo.setInitialScale(100);			
			user_photo.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // webview 깜박임 방지.
			user_photo.loadDataWithBaseURL(null, getImageHtmlCode("http://garagestory.cafe24.com/img/teacher/"+SplashActivity.pro_photo[TeacherHome_inside1.positions]), "text/html", "utf-8", null);
		
		user_name = (TextView)findViewById(R.id.user_name);
		user_id = (TextView)findViewById(R.id.user_id);
		user_golfcash = (TextView)findViewById(R.id.user_golfcash);
			user_name.setText(Login.return_name);
			user_id.setText(Login.return_id);
			user_golfcash.setText("999");
			
	}
	
	public static String getImageHtmlCode(String _imageURL){
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("<style type='text/css'>");
		sb.append("body {");
		sb.append("margin-left: 0px;");
		sb.append("margin-top: 0px;");
		sb.append("margin-right: 0px;");
		sb.append("margin-bottom: 0px;");
		sb.append("}");
		sb.append("</style>");  
		sb.append("</HEAD>");
		sb.append("<BODY>");		
		sb.append("<img width=\"100%\" src=\""+_imageURL +"\"/>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		return sb.toString();
    }
	// 사용자 계정 정보 얻기 - 함수부분
	private String getAccountName() {
		AccountManager mgr = AccountManager.get(this); 
		Account[] accts = mgr.getAccounts(); 
		final int count = accts.length; 
		Account acct = null; 
		String tmpStr = "";        
			        
		for(int i=0;i<count;i++) { 
			acct = accts[i]; 
			// Toast.makeText(this,  "eclair account - name="+acct.name+", type="+acct.type, Toast.LENGTH_LONG).show();
			Log.d("ACCT", "eclair account - name="+acct.name+", type="+acct.type); 
			// txt.setText("acct.name : " + acct.name + ", acct.type : " + acct.type);
			if (acct.type.equals("com.google")) { // 구글 계정만 가져올겁니다. just catch google account
				//tmpStr += "acct.name : " + acct.name + ", acct.type : " + acct.type + "\n";
				tmpStr=acct.name;
			}           
		}
		return tmpStr;
	}    
	private OnTouchListener submenu01TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	submenu01.setBackgroundResource(R.drawable.golfbag_button_charge02);		    	
		    	submenu02.setBackgroundResource(R.drawable.golfbag_button_history01);	        	
	        		        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener submenu02TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	submenu01.setBackgroundResource(R.drawable.golfbag_button_charge01);		    	
		    	submenu02.setBackgroundResource(R.drawable.golfbag_button_history02);	        	
		    	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener indexmenu01TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	indexmenu01.setBackgroundResource(R.drawable.golfbag_5000_02);
	        	indexmenu02.setBackgroundResource(R.drawable.golfbag_10000_01);
	        	indexmenu03.setBackgroundResource(R.drawable.golfbag_15000_01);
	        	choice_count[0]=1;
	        	choice_count[1]=0;
	        	choice_count[2]=0;
	        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener indexmenu02TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	indexmenu01.setBackgroundResource(R.drawable.golfbag_5000_01);
	        	indexmenu02.setBackgroundResource(R.drawable.golfbag_10000_02);
	        	indexmenu03.setBackgroundResource(R.drawable.golfbag_15000_01);
	        	choice_count[0]=0;
	        	choice_count[1]=1;
	        	choice_count[2]=0;      	
	        		        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener indexmenu03TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	indexmenu01.setBackgroundResource(R.drawable.golfbag_5000_01);
	        	indexmenu02.setBackgroundResource(R.drawable.golfbag_10000_01);
	        	indexmenu03.setBackgroundResource(R.drawable.golfbag_15000_02);
	        	choice_count[0]=0;
	        	choice_count[1]=0;
	        	choice_count[2]=1;	        	
	        		        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener buybuttonTouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	buybutton.setBackgroundResource(R.drawable.golfbag_buy02);       	
	        	Intent intent = new Intent(TeacherGolfbag.this,TeacherGolfbag.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.fade,R.anim.hold);	             
	            finish();
	            
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener menu01TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	menu01.setBackgroundResource(R.drawable.main_top_menua01);
	    		menu02.setBackgroundResource(R.drawable.main_top_menub01);
	    		menu03.setBackgroundResource(R.drawable.main_top_menuc01);
	    		menu04.setBackgroundResource(R.drawable.main_top_menud01);
	        	Intent intent = new Intent(TeacherGolfbag.this,TeacherHome_inside1.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.fade,R.anim.hold);	             
	            finish();
	        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener menu02TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	menu01.setBackgroundResource(R.drawable.main_top_menua01);
	    		menu02.setBackgroundResource(R.drawable.main_top_menub02);
	    		menu03.setBackgroundResource(R.drawable.main_top_menuc02);
	    		menu04.setBackgroundResource(R.drawable.main_top_menud01);
	        	Intent intent = new Intent(TeacherGolfbag.this,TeacherMylession.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.fade,R.anim.hold);	             
	            finish();
	        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener menu03TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            
	        	menu01.setBackgroundResource(R.drawable.main_top_menua01);
	    		menu02.setBackgroundResource(R.drawable.main_top_menub01);
	    		menu03.setBackgroundResource(R.drawable.main_top_menuc02);
	    		menu04.setBackgroundResource(R.drawable.main_top_menud01);
	        	Intent intent = new Intent(TeacherGolfbag.this,TeacherGolfbag.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.fade,R.anim.hold);	             
	            finish();
	        	
	           	break;
	 
	        }
			return true;
		}
	};
	private OnTouchListener menu04TouchListener=new OnTouchListener(){
		public boolean onTouch(View v,MotionEvent event){
			
			switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	           
	        	menu01.setBackgroundResource(R.drawable.main_top_menua01);
	    		menu02.setBackgroundResource(R.drawable.main_top_menub01);
	    		menu03.setBackgroundResource(R.drawable.main_top_menuc01);
	    		menu04.setBackgroundResource(R.drawable.main_top_menud02);
	        	Intent intent = new Intent(TeacherGolfbag.this,TeacherSetting.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.fade,R.anim.hold);	             
	            finish();
	        	
	           	break;
	 
	        }
			return true;
		}
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {	 

		switch (keyCode) {
		
		case KeyEvent.KEYCODE_BACK:
			//Toast.makeText(this, "Did's you want cancel this app?", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(TeacherGolfbag.this,TeacherHome_inside1.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.fade,R.anim.hold);	             
            finish();
            
		break;
		}	 

		return super.onKeyDown(keyCode, event);

	}*/
}