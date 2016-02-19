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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
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
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class Check_LoginActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private TextInputLayout inputLayoutUsername, inputLayoutPassword;
	private EditText inputusername, inputpassword;
	private TextView gotoregister, forgot_password;
	private Button login_btn;
	private boolean login_success = false;

	public static final String LoginPrefs = "LoginPrefs";
	public static final String strLoginSet = "0";
	public SharedPreferences loginprefs;

	public static final String BusinessIdPrefs = "BusinessIdPrefs";
	public static final String strBusinessId = "0";
	public SharedPreferences businessidprefs;

	public static final String BusinessNamePrefs = "BusinessNamePrefs";
	public static final String strBusinessNameSet = "0";
	public SharedPreferences businessnameprefs;

	public static final String BusinessPhonePrefs = "BusinessPhonePrefs";
	public static final String strBusinessPhoneSet = "0";
	public SharedPreferences businessphoneprefs;

	public static final String BusinessAddressPrefs = "BusinessAddressPrefs";
	public static final String strBusinessAddressSet = "0";
	public SharedPreferences businessaddressprefs;

	public static final String BusinessAreaPrefs = "BusinessAreaPrefs";
	public static final String strBusinessAreaSet = "0";
	public SharedPreferences businessareaprefs;

	public static final String BusinessLogoPrefs = "BusinessLogoPrefs";
	public static final String strBusinessLogoSet = "0";
	public SharedPreferences businesslogoprefs;

	public static final String BusinessDescPrefs = "BusinessDescPrefs";
	public static final String strBusinessDescSet = "0";
	public SharedPreferences businessdescprefs;

	public static final String BusinessProd_n_ServicePrefs = "BusinessProd_n_ServicePrefs";
	public static final String strBusinessProd_n_ServiceSet = "0";
	public SharedPreferences businessprodnserviceprefs;

	public static final String BusinessUrlPrefs = "BusinessUrlPrefs";
	public static final String strBusinessUrlSet = "0";
	public SharedPreferences businessurlprefs;

	public static final String BusinessAddInfoPrefs = "BusinessAddInfoPrefs";
	public static final String strBusinessAddInfoSet = "0";
	public SharedPreferences businessaddinfoprefs;

	private String businessname = "not_set", businessphone = "not_set",
			businessaddress = "not_set", business_logo = "not_set",
			area_id = "not set", area_name = "not_set", city_id = "not set",
			id = "not set", business_desc = "not_set",
			business_prodnservice = "not_set", business_url = "not_set",
			business_addtext = "not_set", user_status = "not_set";

	JSONParser jsonParser = new JSONParser();
	JSONArray products = null;

	private static String url_check_details = "http://pindout.com/mobi/pindout/check_login.php";
	private static String url_check_status = "http://pindout.com/mobi/pindout/get_user_status.php";
	private static final String url_user_detials = "http://pindout.com/mobi/pindout/get_user_detail.php";
	private static final String url_get_area_name = "http://pindout.com/mobi/pindout/get_area_name.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_ID = "id";
	private static final String TAG_BUSINESSNAME = "business_name";
	private static final String TAG_BUSINESSPHONE = "phone";
	private static final String TAG_BUSINESSADDRESS = "address";
	private static final String TAG_BUSINESSLOGO = "business_image";
	private static final String TAG_CITYID = "city_id";
	private static final String TAG_AREAID = "area_id";
	private static final String TAG_AREANAME = "area_name";
	private static final String TAG_BUSINESS_DESC = "description";
	private static final String TAG_BUSINESS_PRODUCT_N_SERVICE = "product_service";
	private static final String TAG_BUSINESS_URL = "business_url";
	private static final String TAG_BUSINESS_ADDITIONAL_TEXT = "additional_text";
	private static final String TAG_STATUS = "status";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_login);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		inputLayoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
		inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_pass);
		inputusername = (EditText) findViewById(R.id.input_username);
		inputpassword = (EditText) findViewById(R.id.input_pass);
		login_btn = (Button) findViewById(R.id.btn_login);
		gotoregister = (TextView) findViewById(R.id.go_to_register);
		forgot_password = (TextView) findViewById(R.id.forgot_password);

		inputusername.addTextChangedListener(new MyTextWatcher(inputusername));
		inputpassword.addTextChangedListener(new MyTextWatcher(inputpassword));

		loginprefs = getSharedPreferences(LoginPrefs, Context.MODE_PRIVATE);
		businessidprefs = getSharedPreferences(BusinessIdPrefs,
				Context.MODE_PRIVATE);
		businessnameprefs = getSharedPreferences(BusinessNamePrefs,
				Context.MODE_PRIVATE);
		businessphoneprefs = getSharedPreferences(BusinessPhonePrefs,
				Context.MODE_PRIVATE);
		businessaddressprefs = getSharedPreferences(BusinessAddressPrefs,
				Context.MODE_PRIVATE);
		businessareaprefs = getSharedPreferences(BusinessAreaPrefs,
				Context.MODE_PRIVATE);
		businesslogoprefs = getSharedPreferences(BusinessLogoPrefs,
				Context.MODE_PRIVATE);
		businessdescprefs = getSharedPreferences(BusinessDescPrefs,
				Context.MODE_PRIVATE);
		businessprodnserviceprefs = getSharedPreferences(
				BusinessProd_n_ServicePrefs, Context.MODE_PRIVATE);
		businessaddinfoprefs = getSharedPreferences(BusinessAddInfoPrefs,
				Context.MODE_PRIVATE);
		businessurlprefs = getSharedPreferences(BusinessUrlPrefs,
				Context.MODE_PRIVATE);
		forgot_password.setPaintFlags(forgot_password.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);

		login_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitForm();
			}
		});
		gotoregister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Check_LoginActivity.this,
						RegisterActivity.class);
				startActivity(i);
				finish();
				overridePendingTransition(R.anim.right_to_left_enter,
						R.anim.right_to_left_exit);
			}
		});
		forgot_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Check_LoginActivity.this,
						ForgotPasswordActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.right_to_left_enter,
						R.anim.right_to_left_exit);
			}
		});
	}

	private void submitForm() {
		if (!validateUserName()) {
			return;
		}

		if (!validatePass()) {
			return;
		}
		new CheckLogin().execute();
	}

	private boolean validateUserName() {
		if (inputusername.getText().toString().trim().isEmpty()) {
			inputLayoutUsername.setError(getString(R.string.err_msg_username));
			requestFocus(inputusername);
			return false;
		} else {
			inputLayoutUsername.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validatePass() {
		if (inputpassword.getText().toString().trim().isEmpty()) {
			inputLayoutPassword.setError(getString(R.string.err_msg_enterpass));
			requestFocus(inputpassword);
			return false;
		} else {
			inputLayoutPassword.setErrorEnabled(false);
		}
		return true;
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
			case R.id.input_username:
				validateUserName();
				break;
			case R.id.input_pass:
				validatePass();
				break;
			}
		}
	}

	class CheckLogin extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Check_LoginActivity.this);
			pDialog.setMessage("Validating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String name = inputusername.getText().toString().toLowerCase();
			String password = inputpassword.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mailidornumber", name));
			params.add(new BasicNameValuePair("password", password));

			JSONObject json = jsonParser.makeHttpRequest(url_check_details,
					"POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					login_success = true;
				} else {
					login_success = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (!login_success) {
				login_success = false;
				Toast.makeText(getApplicationContext(), "Invalid credentials",
						Toast.LENGTH_SHORT).show();
			} else {
				new CheckStatus().execute();
			}
		}
	}

	class CheckStatus extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Check_LoginActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", inputusername
					.getText().toString()));

			JSONObject json = jsonParser.makeHttpRequest(url_check_status,
					"GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						user_status = c.getString(TAG_STATUS);
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (user_status.equals("1")) {
				new GetUserDetails().execute();
			} else {
				Toast.makeText(
						getApplicationContext(),
						"Your status is not active, please contact Admin to activate.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class GetUserDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Check_LoginActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username",
								inputusername.getText().toString()));
						params.add(new BasicNameValuePair("password",
								inputpassword.getText().toString()));

						JSONObject json = jsonParser.makeHttpRequest(
								url_user_detials, "GET", params);

						Log.d("Single User Details", json.toString());

						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {

							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);

							JSONObject product = productObj.getJSONObject(0);
							id = product.getString(TAG_ID);
							area_id = product.getString(TAG_AREAID);
							city_id = product.getString(TAG_CITYID);
							businessname = product.getString(TAG_BUSINESSNAME);
							businessphone = product
									.getString(TAG_BUSINESSPHONE);
							businessaddress = product
									.getString(TAG_BUSINESSADDRESS);
							business_logo = product.getString(TAG_BUSINESSLOGO);
							business_addtext = product
									.getString(TAG_BUSINESS_ADDITIONAL_TEXT);
							business_desc = product
									.getString(TAG_BUSINESS_DESC);
							business_prodnservice = product
									.getString(TAG_BUSINESS_PRODUCT_N_SERVICE);
							business_url = product.getString(TAG_BUSINESS_URL);
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
			login_success = false;
			new GetAreaName().execute();
		}
	}

	class GetAreaName extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Check_LoginActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("area_id", area_id));
						params.add(new BasicNameValuePair("city_id", city_id));

						JSONObject json = jsonParser.makeHttpRequest(
								url_get_area_name, "GET", params);

						Log.d("Single Product Details", json.toString());

						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {

							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);

							JSONObject product = productObj.getJSONObject(0);
							area_name = product.getString(TAG_AREANAME);
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();

			Editor edit = loginprefs.edit();
			edit.putString(strLoginSet, "1");
			edit.commit();

			Editor edit1 = businessidprefs.edit();
			edit1.putString(strBusinessId, id);
			edit1.commit();

			Editor edit2 = businessnameprefs.edit();
			edit2.putString(strBusinessNameSet, businessname);
			edit2.commit();

			Editor edit3 = businessphoneprefs.edit();
			edit3.putString(strBusinessPhoneSet, businessphone);
			edit3.commit();

			Editor edit4 = businessaddressprefs.edit();
			edit4.putString(strBusinessAddressSet, businessaddress);
			edit4.commit();

			Editor edit5 = businessareaprefs.edit();
			edit5.putString(strBusinessAreaSet, area_name);
			edit5.commit();

			Editor edit6 = businesslogoprefs.edit();
			edit6.putString(strBusinessLogoSet, business_logo);
			edit6.commit();

			Editor edit7 = businessdescprefs.edit();
			edit7.putString(strBusinessDescSet, business_desc);
			edit7.commit();

			Editor edit8 = businessaddinfoprefs.edit();
			edit8.putString(strBusinessAddInfoSet, business_addtext);
			edit8.commit();

			Editor edit9 = businessprodnserviceprefs.edit();
			edit9.putString(strBusinessProd_n_ServiceSet, business_prodnservice);
			edit9.commit();

			Editor edit10 = businessurlprefs.edit();
			edit10.putString(strBusinessUrlSet, business_url);
			edit10.commit();

			Intent intent = new Intent(Check_LoginActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}