package com.kapp.singlo.http;

import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class HttpPost {

	public static boolean check_version(Context context) {
		
		String web_version="",app_version="";
		boolean same_version;
		
		try {
		   PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		   app_version = i.versionName;
		} catch(NameNotFoundException e) { }
		
		try{
			String userurl= ("http://garagestory.cafe24.com/admin/versionparse.php");
			Log.d("url", userurl);
			URL url = new URL(userurl);
			
			// XmlPullParser 생성
			XmlPullParserFactory xpf = XmlPullParserFactory.newInstance();
			XmlPullParser xp = xpf.newPullParser();
			// XML 파일 읽어오기
			xp.setInput(url.openStream(), "UTF-8");

			int parserEvent = xp.getEventType();
			String tag = null;
			// XML 문서가 끝날때 까지 반복 작업합니다.
			while(parserEvent != XmlPullParser.END_DOCUMENT) {
				switch(parserEvent) {
				// 시작태그들을 만났을 때 진행합니다.
				case XmlPullParser.START_TAG:
					tag = xp.getName();
					// 태그명이 "node" 일 때
					if(tag.compareTo("node") == 0) {
					
						web_version=xp.getAttributeValue(1);
					}
					break;
					// 종료태그들을 만났을 때 진행합니다.
				case XmlPullParser.END_TAG:
					tag = xp.getName();
					// 종료태그 후에도 TEXT 이벤트가 발생하니 tag에 null 대입해서 중복 실행을 방지합니다.
					tag = null;
					Log.d("result", "완료");
					break;
					// 시작, 종료 태그 사이의 text를 만났을 때 진행합니다.
				case XmlPullParser.TEXT: 
					if(tag == null) break; // 중복실행 방지를 위한 구문
					//String text = xp.getText();
					Log.d("result", "text부분은 통과");
					break;
				}
				Log.d("result", "다음");
				parserEvent = xp.next();
			}
		 } catch (Exception e) {
			 Log.d("disp", "err : " + e.getMessage());
 		 }
		
		if (web_version.equals(app_version)) {
			same_version=true;
		}
		else {
			same_version=false;
		}
		
		Log.d("web version : ", web_version);
		Log.d("app version : ", app_version);
		
		return same_version;
	}
}
