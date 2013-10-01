package com.kapp.singlo.users;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.kapp.singlo.R;
import com.kapp.singlo.bg.CallbackListener;
import com.kapp.singlo.meta.SingloUserActivity;



public class teacherBlog extends SingloUserActivity implements CallbackListener, OnClickListener {

	WebView webview;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	 
	    // TODO Auto-generated method stub
	    setContentView(R.layout.teacher_blog);
	    
	    webview = (WebView)findViewById(R.id.teacherBlogWebview);
        webview.setWebViewClient(new WebClient()); // 응룡프로그램에서 직접 url 처리
        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        webview.loadUrl("http://www.google.com");
	}
	
	class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callback(Object... params) {
		// TODO Auto-generated method stub
		
	}

}
