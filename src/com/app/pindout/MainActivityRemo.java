package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MainActivityRemo extends ActionBarActivity {

	private static final String url_user_details = "http://www.pindout.com/remuser/pindout/get_user_detail.php";
	private static final String TAG_SUCCESS = "success";
	public static final String BusinessIdPrefs = "BusinessIdPrefs";
	public static final String strBusinessId = "0";
	public SharedPreferences businessidprefs;
	public static final String strLoginSet = "0";
	public static final String BusinessNamePrefs = "BusinessNamePrefs";
	public static final String strBusinessNameSet = "0";
	public SharedPreferences businessnameprefs;
	String img_name = "not_set";
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

	public static final String BusinessWorkingHrsPrefs = "BusinessWorkingHrsPrefs";
	public static final String strBusinessWorkingHrsSet = "0";
	public SharedPreferences businessworkinghrsprefs;

	String business_id = "";

	public static final String BusinessAddInfoPrefs = "BusinessAddInfoPrefs";
	public static final String strBusinessAddInfoSet = "0";
	public SharedPreferences businessaddinfoprefs;
	public SharedPreferences loginprefs;

	JSONParser jParser = new JSONParser();
	String firstname, lastname;
	String ename, epass;
	String business_working_hrs = "not_set";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		} else {
			setContentView(R.layout.activity_main_remo);

			final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.loginpage);
			Picasso.with(MainActivityRemo.this).load(R.drawable.background)
					.into(new Target() {

						@Override
						public void onBitmapLoaded(Bitmap bitmap,
								Picasso.LoadedFrom from) {
							relativeLayout.setBackground(new BitmapDrawable(
									MainActivityRemo.this.getResources(),
									bitmap));
						}

						@Override
						public void onBitmapFailed(final Drawable errorDrawable) {
							Log.d("TAG", "FAILED");
						}

						@Override
						public void onPrepareLoad(
								final Drawable placeHolderDrawable) {
							Log.d("TAG", "Prepare Load");
						}
					});

			Button lbut = (Button) findViewById(R.id.btnSingIn);
			lbut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// starting background task to update product
					uname = (EditText) findViewById(R.id.MainUserName);
					upass = (EditText) findViewById(R.id.MainPass);
					ename = uname.getText().toString();
					epass = upass.getText().toString();
					if (ename == null || ename.equals("") || epass.equals("")
							|| epass == null) {
						Toast.makeText(MainActivityRemo.this,
								"Please fill in both Username and Password",
								Toast.LENGTH_LONG).show();
						return;
					}
					new GetProductDetails().execute();
				}
			});

			SharedPreferences pref = getSharedPreferences("loginpref",
					MODE_PRIVATE);
			String username = pref.getString("name", null);
			String email = pref.getString("email", null);

			if (username == null || email == null) {
				new GetImageDetails().execute();
			} else {
				Intent intent = new Intent(MainActivityRemo.this,
						com.app.pindout.RemIndUserMain.class);
				startActivity(intent);
			}
		}

		String LoginPrefs = "LoginPrefs";

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
		businessworkinghrsprefs = getSharedPreferences(BusinessWorkingHrsPrefs,
				Context.MODE_PRIVATE);

		if (businessidprefs.contains(strBusinessId)) {
			business_id = businessidprefs.getString(strBusinessId, "0");
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent ke) {
		if (keycode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Are you sure you wanna Exit ?");
			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
							// System.exit(0);
						}
					});
			alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							return;
						}
					});
			AlertDialog ad = alert.create();
			ad.show();
		}
		return super.onKeyDown(keycode, ke);
	}

	public void bususerview(View view) {
		getSharedPreferences("remosapp", MODE_PRIVATE).edit()
				.putString("remoapp", "2").commit();
		Intent intent = new Intent(MainActivityRemo.this, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	String idd, email, fname, lname;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void signup(View view) {
		Intent intent = new Intent(MainActivityRemo.this, OneTimeShow.class);
		startActivity(intent);
	}

	EditText uname, upass;

	public void login(View view) {
		uname = (EditText) findViewById(R.id.MainUserName);
		upass = (EditText) findViewById(R.id.MainPass);
		new GetProductDetails().execute();
	}

	class GetProductDetails extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		int success = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivityRemo.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", ename));
			params.add(new BasicNameValuePair("password", epass));
			JSONObject json = jParser.makeHttpRequest(url_user_details, "GET",
					params);

			try {
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					JSONArray products = json.getJSONArray("product");
					JSONObject c = products.getJSONObject(0);
					fname = c.getString("firstname");
					lname = c.getString("lastname");
					email = c.getString("email");
					idd = c.getString("id");
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (success == 1) {
				// Toast.makeText(MainActivityRemo.this, "" + fname + lname + id
				// + email, Toast.LENGTH_LONG).show();
				getSharedPreferences("loginpref", MODE_PRIVATE).edit()
						.putString("email", email).putString("name", fname)
						.putString("state", "yes").putString("id", idd)
						.commit();
				Intent intent = new Intent(MainActivityRemo.this,
						RemIndUserMain.class);
				startActivity(intent);
			} else {
				new CheckLogin().execute();
			}
		}
	}

	String url_check_details = "http://www.pindout.com/mobi/pindout/check_login.php";
	private boolean login_success = false;

	class CheckLogin extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivityRemo.this);
			pDialog.setMessage("Validating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String name = ename;
			String password = epass;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mailidornumber", name));
			params.add(new BasicNameValuePair("password", password));

			JSONObject json = jParser.makeHttpRequest(url_check_details,
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

	String url_check_status = "http://www.pindout.com/mobi/pindout/get_user_status.php";
	String TAG_PRODUCT = "product";
	String TAG_STATUS = "status";
	String TAG_PRODUCTS = "products";
	private String businessname = "not_set", businessphone = "not_set",
			businessaddress = "not_set", business_logo = "not_set",
			area_id = "not set", area_name = "not_set", city_id = "not set",
			id = "not set", business_desc = "not_set",
			business_prodnservice = "not_set", business_url = "not_set",
			business_addtext = "not_set", user_status = "not_set";

	JSONArray products = null;

	class CheckStatus extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivityRemo.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", ename));

			JSONObject json = jParser.makeHttpRequest(url_check_status, "GET",
					params);

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
						"Your status is deactive, please contact Admin to activate.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	String url_user_detials = "http://www.pindout.com/mobi/pindout/get_user_detail.php";
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
	private static final String TAG_ID = "id";
	private static final String TAG_BUSINESS_WORKING_HRS = "working_hrs";

	class GetUserDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivityRemo.this);
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
						params.add(new BasicNameValuePair("username", ename));
						params.add(new BasicNameValuePair("password", epass));

						JSONObject json = jParser.makeHttpRequest(
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
			// dismiss the dialog once got all details
			pDialog.dismiss();
			login_success = false;
			new GetAreaName().execute();
		}
	}

	String url_get_area_name = "http://www.pindout.com/mobi/pindout/get_area_name.php";

	class GetAreaName extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivityRemo.this);
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

						JSONObject json = jParser.makeHttpRequest(
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

			SharedPreferences.Editor edit = loginprefs.edit();
			edit.putString(strLoginSet, "1");
			edit.commit();

			SharedPreferences.Editor edit1 = businessidprefs.edit();
			edit1.putString(strBusinessId, id);
			edit1.commit();

			SharedPreferences.Editor edit2 = businessnameprefs.edit();
			edit2.putString(strBusinessNameSet, businessname);
			edit2.commit();

			SharedPreferences.Editor edit3 = businessphoneprefs.edit();
			edit3.putString(strBusinessPhoneSet, businessphone);
			edit3.commit();

			SharedPreferences.Editor edit4 = businessaddressprefs.edit();
			edit4.putString(strBusinessAddressSet, businessaddress);
			edit4.commit();

			SharedPreferences.Editor edit5 = businessareaprefs.edit();
			edit5.putString(strBusinessAreaSet, area_name);
			edit5.commit();

			SharedPreferences.Editor edit6 = businesslogoprefs.edit();
			edit6.putString(strBusinessLogoSet, business_logo);
			edit6.commit();

			SharedPreferences.Editor edit7 = businessdescprefs.edit();
			edit7.putString(strBusinessDescSet, business_desc);
			edit7.commit();

			SharedPreferences.Editor edit8 = businessaddinfoprefs.edit();
			edit8.putString(strBusinessAddInfoSet, business_addtext);
			edit8.commit();

			SharedPreferences.Editor edit9 = businessprodnserviceprefs.edit();
			edit9.putString(strBusinessProd_n_ServiceSet, business_prodnservice);
			edit9.commit();

			SharedPreferences.Editor edit10 = businessurlprefs.edit();
			edit10.putString(strBusinessUrlSet, business_url);
			edit10.commit();

			Editor edit11 = businessworkinghrsprefs.edit();
			edit11.putString(strBusinessWorkingHrsSet, business_working_hrs);
			edit11.commit();

			Intent intent = new Intent(MainActivityRemo.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private static final String url_imagenuser_details = "http://www.pindout.com/mobi/pindout/get_userdetailnimagedetail_detail.php";
	private static final String TAG_BUSINESS_LOGO = "business_image";

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

						JSONObject json = jParser.makeHttpRequest(
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

			SharedPreferences.Editor edit = businesslogoprefs.edit();
			edit.putString(strBusinessLogoSet, img_name);
			edit.commit();

			SharedPreferences.Editor edit1 = businessdescprefs.edit();
			edit1.putString(strBusinessDescSet, business_desc);
			edit1.commit();

			SharedPreferences.Editor edit2 = businessaddinfoprefs.edit();
			edit2.putString(strBusinessAddInfoSet, business_addtext);
			edit2.commit();

			SharedPreferences.Editor edit3 = businessprodnserviceprefs.edit();
			edit3.putString(strBusinessProd_n_ServiceSet, business_prodnservice);
			edit3.commit();

			SharedPreferences.Editor edit4 = businessurlprefs.edit();
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

					}
					if (isLoginset.equals("1")) { // logged in
						Intent intent = new Intent(MainActivityRemo.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}, 200);
		}
	}

}
