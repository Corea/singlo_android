package com.garagestory.singlo.http;

import java.net.URL;

import org.json.JSONObject;

import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.JSONParser;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class HttpPost {

	public static boolean check_version(Context context) {
		String web_version = "";
		String app_version = "";

		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			app_version = i.versionName;
		} catch (NameNotFoundException e) {
		}

		try {
			URL url = new URL(Const.VERSION_URL);

			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromStream(url.openStream());

			String result = json.getString("result");
			if (result.equals("success")) {
				web_version = json.getString("version");
			}
		} catch (Exception e) {
			Log.d("disp", "err : " + e.getMessage());
		}

		Log.d("Web Version : ", web_version);
		Log.d("App Version : ", app_version);

		if (web_version.equals(app_version)) {
			return true;
		}

		return false;
	}
}
