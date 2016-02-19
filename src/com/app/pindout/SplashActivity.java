package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.app.pindout.helpers.FlyTxtView;

public class SplashActivity extends Activity {

	private FlyTxtView flyTxtView;
	private static final int SPLASH_TIME = 200;
	private String img_name = "not_set", business_id = "",
			business_desc = "not_set", business_prodnservice = "not_set",
			business_url = "not_set", business_addtext = "not_set",
			business_working_hrs = "not_set";

	public static final String IsEdit = "IsEditPrefs";
	public static final String strIsEditSet = "0";
	public SharedPreferences iseditprefs;

	public static final String LoginPrefs = "LoginPrefs";
	public static final String strLoginSet = "0";
	public SharedPreferences loginprefs;

	public static final String BusinessIdPrefs = "BusinessIdPrefs";
	public static final String strBusinessId = "0";
	public SharedPreferences businessidprefs;

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

	public static final String BusinessWorkingHrsPrefs = "BusinessWorkingHrsPrefs";
	public static final String strBusinessWorkingHrsSet = "0";
	public SharedPreferences businessworkinghrsprefs;

	JSONParser jsonParser = new JSONParser();

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_BUSINESS_LOGO = "business_image";
	private static final String TAG_BUSINESS_DESC = "description";
	private static final String TAG_BUSINESS_PRODUCT_N_SERVICE = "product_service";
	private static final String TAG_BUSINESS_URL = "business_url";
	private static final String TAG_BUSINESS_ADDITIONAL_TEXT = "additional_text";
	private static final String TAG_BUSINESS_WORKING_HRS = "working_hrs";

	private static final String url_imagenuser_details = "http://www.pindout.com/mobi/pindout/get_userdetailnimagedetail_detail.php";

	public SharedPreferences onetimeshow;
	public SharedPreferences remosapp;
	public String onetimeshowvalue;
	public String remosappvalue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_splash);

		iseditprefs = getSharedPreferences(IsEdit, Context.MODE_PRIVATE);
		loginprefs = getSharedPreferences(LoginPrefs, Context.MODE_PRIVATE);
		businessidprefs = getSharedPreferences(BusinessIdPrefs,
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
		businessworkinghrsprefs = getSharedPreferences(BusinessWorkingHrsPrefs,
				Context.MODE_PRIVATE);

		onetimeshow = getSharedPreferences("onetimeshow", MODE_PRIVATE);
		onetimeshowvalue = onetimeshow.getString("oneshow", null);
		remosapp = getSharedPreferences("remosapp", MODE_PRIVATE);

		remosappvalue = remosapp.getString("remoapp", null);

		SplashActivity.this.deleteDatabase("Greetingmsg.db");
		if (businessidprefs.contains(strBusinessId)) {
			business_id = businessidprefs.getString(strBusinessId, "0");
		}

		Editor edit = iseditprefs.edit();
		edit.putString(strIsEditSet, "0");
		edit.commit();

		setupViews();
	}

	public void setupViews() {
		flyTxtView = (FlyTxtView) findViewById(R.id.fly_txt);
		flyTxtView.setTextSize(36);
		flyTxtView.setTextColor(Color.BLUE);
		flyTxtView.setTexts(getResources().getString(R.string.splash_label));
		flyTxtView.startAnimation();

		/*
		 * if(onetimeshowvalue==null){
		 * //Toast.makeText(SplashActivity.this,"Remo "
		 * ,Toast.LENGTH_LONG).show(); Handler handler = new Handler();
		 * handler.postDelayed(new Runnable() {
		 * 
		 * 
		 * @Override public void run() { Intent intent = new
		 * Intent(SplashActivity.this, OneTimeShow.class);
		 * startActivity(intent); finish(); } }, 2200);
		 * 
		 * 
		 * }
		 */remosappvalue = "1";
		if (remosappvalue.equals("1")) {
			// Toast.makeText(SplashActivity.this,"Antony",Toast.LENGTH_LONG).show();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(SplashActivity.this,
							MainActivityRemo.class);
					startActivity(intent);
					finish();
				}
			}, 2200);

		} else {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					new GetImageDetails().execute();
				}
			}, 2200);
		}
	}

	class GetImageDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id", business_id));

						JSONObject json = jsonParser.makeHttpRequest(
								url_imagenuser_details, "GET", params);

						Log.d("Single Product Details", json.toString());

						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {

							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);

							JSONObject product = productObj.getJSONObject(0);
							img_name = product.getString(TAG_BUSINESS_LOGO);
							business_addtext = product
									.getString(TAG_BUSINESS_ADDITIONAL_TEXT);
							business_desc = product
									.getString(TAG_BUSINESS_DESC);
							business_prodnservice = product
									.getString(TAG_BUSINESS_PRODUCT_N_SERVICE);
							business_url = product.getString(TAG_BUSINESS_URL);
							business_working_hrs = product
									.getString(TAG_BUSINESS_WORKING_HRS);
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

			Editor edit = businesslogoprefs.edit();
			edit.putString(strBusinessLogoSet, img_name);
			edit.commit();

			Editor edit1 = businessdescprefs.edit();
			edit1.putString(strBusinessDescSet, business_desc);
			edit1.commit();

			Editor edit2 = businessaddinfoprefs.edit();
			edit2.putString(strBusinessAddInfoSet, business_addtext);
			edit2.commit();

			Editor edit3 = businessprodnserviceprefs.edit();
			edit3.putString(strBusinessProd_n_ServiceSet, business_prodnservice);
			edit3.commit();

			Editor edit4 = businessurlprefs.edit();
			edit4.putString(strBusinessUrlSet, business_url);
			edit4.commit();

			Editor edit5 = businessworkinghrsprefs.edit();
			edit5.putString(strBusinessWorkingHrsSet, business_working_hrs);
			edit5.commit();

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					String isLoginset = "0";
					if (loginprefs.contains(strLoginSet)) {
						isLoginset = loginprefs.getString(strLoginSet, "0");
					}
					if (isLoginset.equals("0")) { // not logged in
						Intent i = new Intent(SplashActivity.this,
								MainActivityRemo.class);
						startActivity(i);
						finish();
					}
					if (isLoginset.equals("1")) { // logged in
						Intent intent = new Intent(SplashActivity.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}, SPLASH_TIME);
		}
	}
}