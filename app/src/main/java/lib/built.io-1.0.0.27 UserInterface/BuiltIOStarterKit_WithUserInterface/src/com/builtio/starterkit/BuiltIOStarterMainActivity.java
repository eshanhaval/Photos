package com.builtio.starterkit;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class BuiltIOStarterMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_built_iostarter_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.built_iostarter_main, menu);
		return true;
	}


}
