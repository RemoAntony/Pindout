package com.app.pindout.greetingmsg;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.pindout.JSONParser;
import com.app.pindout.R;

public class AddNewMessageActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private TextInputLayout input_layout_greetingmsg;
	private EditText input_msg;
	private Button btn_submitmsg, btn_cancel;
	private Intent intent;
	private String business_id = "";
	JSONParser jParser = new JSONParser();

	private static final String TAG_SUCCESS = "success";
	private boolean is_inserted = false;

	public static final String IsEdit = "IsEditPrefs";
	public static final String strIsEditSet = "0";
	public SharedPreferences iseditprefs;

	private static String url_insert_msg = "http://www.pindout.com/mobi/pindout/insert_greeting_msg.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_greeting_msg);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		input_layout_greetingmsg = (TextInputLayout) findViewById(R.id.input_layout_addmsg);
		input_msg = (EditText) findViewById(R.id.input_greeting_msg);
		btn_submitmsg = (Button) findViewById(R.id.btn_submitmsg);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		setSupportActionBar(toolbar);
		toolbar.setTitle("Add message");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		intent = getIntent();
		iseditprefs = getSharedPreferences(IsEdit, Context.MODE_PRIVATE);
		business_id = intent.getStringExtra("business_id");
		input_msg.addTextChangedListener(new MyTextWatcher(input_msg));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
		btn_submitmsg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
	}

	private void submit() {
		if (!validateMsg()) {
			return;
		}
		new AddMsg().execute();
	}

	private boolean validateMsg() {
		if (input_msg.getText().toString().trim().isEmpty()) {
			input_layout_greetingmsg
					.setError(getString(R.string.err_msg_greetingmsg));
			requestFocus(input_msg);
			return false;
		} else {
			input_layout_greetingmsg.setErrorEnabled(false);
		}
		return true;
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	class AddMsg extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddNewMessageActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String currentDateString = DateFormat.getDateInstance().format(
					new Date());
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("date", currentDateString));
			params.add(new BasicNameValuePair("message", input_msg.getText()
					.toString()));

			JSONObject json = jParser.makeHttpRequest(url_insert_msg, "POST",
					params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_inserted = true;
				} else {
					is_inserted = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_inserted) {
				is_inserted = false;
				Toast.makeText(getApplicationContext(), "Message saved",
						Toast.LENGTH_SHORT).show();
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "msg_altered");
				edit.commit();
				finish();
				overridePendingTransition(R.anim.left_to_right_enter,
						R.anim.left_to_right_exit);
			} else {
				Toast.makeText(getApplicationContext(), "Message not saved",
						Toast.LENGTH_SHORT).show();
				is_inserted = false;
			}
		}
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
			case R.id.input_first_name:
				validateMsg();
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		onBackEvent();
	}

	private void onBackEvent() {
		finish();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}
