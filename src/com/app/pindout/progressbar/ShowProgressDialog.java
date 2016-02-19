package com.app.pindout.progressbar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.app.pindout.R;

public class ShowProgressDialog extends Activity {

	private ProgressWheel progresswheel;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("finish_activity")) {
				unregisterReceiver(receiver);
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressdialog);
		registerReceiver(receiver, new IntentFilter("finish_activity"));
		progresswheel = (ProgressWheel) findViewById(R.id.progress_wheel);
		progresswheel.spin();
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(),
				"Please wait,  loading data...", Toast.LENGTH_SHORT).show();
	}
}
