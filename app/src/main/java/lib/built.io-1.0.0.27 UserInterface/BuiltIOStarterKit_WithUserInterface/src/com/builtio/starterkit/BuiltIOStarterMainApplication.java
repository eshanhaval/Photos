package com.builtio.starterkit;

import android.app.Application;

import com.raweng.built.Built;


public class BuiltIOStarterMainApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();

		try {
			Built.initializeWithApiKey(getApplicationContext(), "APPLICATION_API_KEY" , "APPLICATION_UID");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
