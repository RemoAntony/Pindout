package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

@SuppressLint("DefaultLocale")
public class ForgotPasswordActivity extends AppCompatActivity {

	private TextInputLayout inputLayoutEmail;
	private EditText inputemail;
	private Toolbar toolbar;
	private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();
	private boolean is_pass_retrieved = false, is_mail_send = false;
	private String password = "", mobile_no = "not_set", username = "not_set";
	private Button btn_submit;

	private static String url_get_password = "";
	private static String url_send_mail = "http://www.pindout.com/mobi/pindout/mail/mail.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_MOBILENO = "mobile_no";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PRODUCT = "product";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
		inputemail = (EditText) findViewById(R.id.input_email);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		inputemail.addTextChangedListener(new MyTextWatcher(inputemail));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});
	}

	private void submit() {
		String get_email_mobile = inputemail.getText().toString();

		if (!validateEmail_Mobile(get_email_mobile)) {
			return;
		}
		new Get_User_Password().execute();
	}

	private boolean validateEmail_Mobile(String get_email_mobile) {
		if (inputemail.getText().toString().trim().isEmpty()) {
			inputLayoutEmail
					.setError(getString(R.string.err_msg_enter_email_mobile));
			requestFocus(inputemail);
			return false;
		} else {
			return true;
		}
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	class Get_User_Password extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ForgotPasswordActivity.this);
			pDialog.setMessage("Validating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			if (inputemail.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
				url_get_password = "http://www.pindout.com/mobi/pindout/get_password_from_mobileno.php";
			} else {
				url_get_password = "http://www.pindout.com/mobi/pindout/get_password_from_emailid.php";
			}
			runOnUiThread(new Runnable() {
				public void run() {
					int success;
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username",
								inputemail.getText().toString()));

						JSONObject json = jParser.makeHttpRequest(
								url_get_password, "GET", params);

						Log.d("User Pass Details", json.toString());

						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {

							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);

							JSONObject product = productObj.getJSONObject(0);
							password = product.getString(TAG_PASSWORD);
							mobile_no = product.getString(TAG_MOBILENO);
							username = product.getString(TAG_USERNAME);
							is_pass_retrieved = true;
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/*
			 * Toast.makeText(getApplicationContext(), mobile_no,
			 * Toast.LENGTH_SHORT).show();
			 */

			if (is_pass_retrieved) {
				new SendMail().execute();
			} else {
				pDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						"Email/Mobile does not exist", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	class SendMail extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {

			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("username", username));
			params2.add(new BasicNameValuePair("password", password));
			params2.add(new BasicNameValuePair("mobile_no", mobile_no));

			JSONObject json = jParser.makeHttpRequest(url_send_mail, "POST",
					params2);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_mail_send = true;
				} else {
					is_mail_send = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (is_mail_send) {
				Toast.makeText(getApplicationContext(),
						"Please check your mail", Toast.LENGTH_SHORT).show();
				onBackEvent();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error in recovering password", Toast.LENGTH_SHORT)
						.show();
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
			case R.id.input_email:
				validateEmail_Mobile(inputemail.getText().toString());
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
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}