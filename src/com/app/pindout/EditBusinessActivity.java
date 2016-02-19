package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.dbadapter.AddedCategoryDBAdapter;
import com.app.pindout.dbadapter.AddedFacilityDBAdapter;
import com.app.pindout.dbadapter.Upload_User_Categories_DBAdapter;

@SuppressLint("InflateParams")
public class EditBusinessActivity extends AppCompatActivity {

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

	private String business_id = "", area_name = "", city_name = "";
	private Intent intent;

	private AddedCategoryDBAdapter addedcatdb;
	private AddedFacilityDBAdapter addedfacdb;
	private Upload_User_Categories_DBAdapter uploadusercatdb;

	private Button selectCat, btnsave, btncancel, selectCity, selectArea;
	private Toolbar toolbar;
	private boolean update_success = false;

	JSONArray products = null;
	JSONParser jParser = new JSONParser();
	private boolean is_category_selected = false;

	private String id_city = "0", id_area = "0", selected_city = "0",
			selected_area = "0", latitude = "0", longitude = "0";

	private EditText inputFirstName, inputEmail, inputBusinessName,
			inputBusinessPhone, inputBusinessSearch, inputAddress;

	private TextInputLayout inputLayoutFirstName, inputLayoutEmail,
			inputLayoutBusinessName, inputLayoutBusinessPhone,
			inputLayoutBusinessSearch, inputLayoutAddress;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_FIRSTNAME = "first_name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_BUSINESSNAME = "business_name";
	private static final String TAG_BUSINESSPHONE = "business_phone";
	private static final String TAG_SEARCHKEYWORD = "search_keywords";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_CITYID = "cityid";
	private static final String TAG_AREAID = "areaid";
	private static final String TAG_AREANAME = "areaname";
	private static final String TAG_CITYNAME = "cityname";
	private static final String TAG_CATID = "catid";
	private static final String TAG_TODELETECATIDS = "id";
	private static final String TAG_CATNAME = "catname";
	private static final String TAG_FACILITYIDS = "facilityid";
	private static final String TAG_FACILITYNAME = "facilityname";

	private String firts_name = "hi", email = "", businessname = "",
			businessphone = "", searchkeyword = "", address = "", city_id = "",
			area_id = "";

	private ArrayList<String> catids = new ArrayList<String>();
	private ArrayList<String> facids = new ArrayList<String>();
	private ArrayList<String> to_deletecat = new ArrayList<String>();
	private ArrayList<String> catnames = new ArrayList<String>();
	private ArrayList<String> facnames = new ArrayList<String>();
	private ArrayList<String> citylist = new ArrayList<String>();
	private ArrayList<String> cityid = new ArrayList<String>();
	private ArrayList<String> citylatitude = new ArrayList<String>();
	private ArrayList<String> citylongitude = new ArrayList<String>();
	private ArrayList<String> arealist = new ArrayList<String>();
	private ArrayList<String> areaid = new ArrayList<String>();

	private static String url_get_catids_for_business = "http://www.pindout.com/mobi/pindout/get_catids_for_business.php";
	private static String url_get_catnames_for_business = "http://www.pindout.com/mobi/pindout/get_catnames_for_business.php";
	private static String url_get_facnames_for_business = "http://www.pindout.com/mobi/pindout/get_facnames_for_business.php";
	private static String url_user_detials_for_edit = "http://www.pindout.com/mobi/pindout/get_user_detail_for_edit.php";
	private static String url_all_city = "http://www.pindout.com/mobi/pindout/get_all_city.php";
	private static String url_all_area = "http://www.pindout.com/mobi/pindout/get_area.php";
	private static String url_get_city = "http://www.pindout.com/mobi/pindout/get_edit_city.php";
	private static String url_get_area = "http://www.pindout.com/mobi/pindout/get_edit_area.php";
	private static String url_update_details = "http://www.pindout.com/mobi/pindout/update_user_details.php";
	private static String url_add_editcatids = "http://www.pindout.com/mobi/pindout/insert_edit_catids.php";
	private static String url_delete_catids = "http://www.pindout.com/mobi/pindout/delete_saved_catids.php";
	private static String url_add_facilites = "http://www.pindout.com/mobi/pindout/add_multi.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_edit_business);
		selectCat = (Button) findViewById(R.id.addcategory);
		btnsave = (Button) findViewById(R.id.btn_submit);
		btncancel = (Button) findViewById(R.id.btn_cancel);
		selectCity = (Button) findViewById(R.id.selectcity);
		selectArea = (Button) findViewById(R.id.selectarea);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);

		inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_mobileemail);
		inputLayoutBusinessName = (TextInputLayout) findViewById(R.id.input_layout_businessname);
		inputLayoutBusinessPhone = (TextInputLayout) findViewById(R.id.input_layout_businessphone);
		inputLayoutBusinessSearch = (TextInputLayout) findViewById(R.id.input_layout_businessearch);
		inputLayoutAddress = (TextInputLayout) findViewById(R.id.input_layout_address);

		inputFirstName = (EditText) findViewById(R.id.input_first_name);
		inputEmail = (EditText) findViewById(R.id.input_mobile_or_email);
		inputBusinessName = (EditText) findViewById(R.id.input_businessname);
		inputBusinessPhone = (EditText) findViewById(R.id.input_businessphone);
		inputBusinessSearch = (EditText) findViewById(R.id.input_businessearch);
		inputAddress = (EditText) findViewById(R.id.input_address);

		businessnameprefs = getSharedPreferences(BusinessNamePrefs,
				Context.MODE_PRIVATE);
		businessphoneprefs = getSharedPreferences(BusinessPhonePrefs,
				Context.MODE_PRIVATE);
		businessaddressprefs = getSharedPreferences(BusinessAddressPrefs,
				Context.MODE_PRIVATE);
		businessareaprefs = getSharedPreferences(BusinessAreaPrefs,
				Context.MODE_PRIVATE);

		inputFirstName
				.addTextChangedListener(new MyTextWatcher(inputFirstName));
		inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
		inputBusinessName.addTextChangedListener(new MyTextWatcher(
				inputBusinessName));
		inputBusinessPhone.addTextChangedListener(new MyTextWatcher(
				inputBusinessPhone));
		inputBusinessSearch.addTextChangedListener(new MyTextWatcher(
				inputBusinessSearch));
		inputAddress.addTextChangedListener(new MyTextWatcher(inputAddress));

		intent = getIntent();
		business_id = intent.getStringExtra("business_id");
		addedcatdb = new AddedCategoryDBAdapter(this);
		addedfacdb = new AddedFacilityDBAdapter(this);
		uploadusercatdb = new Upload_User_Categories_DBAdapter(this);
		EditBusinessActivity.this.deleteDatabase("AddedCategory.db");
		EditBusinessActivity.this.deleteDatabase("AddedFacility.db");

		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		btnsave.setText(getResources().getString(R.string.save));
		btncancel.setWidth(width / 2);
		btnsave.setWidth(width / 2);

		new GetUserDetails().execute();

		selectCity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				citylist.clear();
				cityid.clear();
				new LoadAllCity().execute();
			}
		});

		selectArea.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!id_city.equals("0")) {
					arealist.clear();
					new LoadAllArea().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please select your city ", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		selectCat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EditBusinessActivity.this,
						EditBusinessCategoryActivity.class);
				startActivity(i);
			}
		});

		btnsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submitForm();
			}
		});

		btncancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);
			}
		});
	}

	private void submitForm() {
		if (!validateFirstName()) {
			return;
		}
		if (!validateEmail()) {
			return;
		}
		if (!validateBusinessName()) {
			return;
		}
		if (!validateBusinessPhone()) {
			return;
		}
		if (!validateBusinessSearchKey()) {
			return;
		}
		if (!validateAddress()) {
			return;
		}
		if (!is_category_selected) {
			Toast.makeText(getApplicationContext(),
					"You must select at least one category", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		new DeleteCatIds().execute();
		new AddFacilities().execute();
		new UpdateUserDetails().execute();
	}

	private boolean validateFirstName() {
		if (inputFirstName.getText().toString().trim().isEmpty()) {
			inputLayoutFirstName.setError(getString(R.string.err_msg_name));
			requestFocus(inputFirstName);
			return false;
		} else {
			inputLayoutFirstName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateEmail() {
		if (inputEmail.getText().toString().trim().isEmpty()
				|| !isValidEmail(inputEmail.getText().toString())) {
			inputLayoutEmail.setError(getString(R.string.err_msg_email));
			requestFocus(inputEmail);
			return false;
		} else {
			if (!isValidEmail(inputEmail.getText().toString())) {
				inputLayoutEmail
						.setError(getString(R.string.err_msg_invalidemail));
			} else {
				inputLayoutEmail.setErrorEnabled(false);
			}
		}
		return true;
	}

	private boolean validateBusinessName() {
		if (inputBusinessName.getText().toString().trim().isEmpty()) {
			inputLayoutBusinessName
					.setError(getString(R.string.err_msg_businessname));
			requestFocus(inputBusinessName);
			return false;
		} else {
			inputLayoutBusinessName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateBusinessPhone() {
		if (inputBusinessPhone.getText().toString().trim().isEmpty()) {
			inputLayoutBusinessPhone
					.setError(getString(R.string.err_msg_businessphone));
			requestFocus(inputBusinessPhone);
			return false;
		} else {
			if (inputBusinessPhone.length() < 10) {
				inputLayoutBusinessPhone
						.setError(getString(R.string.err_msg_invalid_mobileno));
				requestFocus(inputBusinessPhone);
				return false;
			} else {
				inputLayoutBusinessPhone.setErrorEnabled(false);
			}
		}
		return true;
	}

	private boolean validateBusinessSearchKey() {
		if (inputBusinessSearch.getText().toString().trim().isEmpty()) {
			inputLayoutBusinessSearch
					.setError(getString(R.string.err_msg_search_key));
			requestFocus(inputBusinessSearch);
			return false;
		} else {
			inputLayoutBusinessSearch.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateAddress() {
		if (inputAddress.getText().toString().trim().isEmpty()) {
			inputLayoutAddress.setError(getString(R.string.err_msg_address));
			requestFocus(inputAddress);
			return false;
		} else {
			inputLayoutAddress.setErrorEnabled(false);
		}
		return true;
	}

	private static boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	class GetUserDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
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
						params.add(new BasicNameValuePair("business_id",
								business_id));
						JSONObject json = jParser.makeHttpRequest(
								url_user_detials_for_edit, "GET", params);
						Log.d("Single User Details", json.toString());
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);
							JSONObject product = productObj.getJSONObject(0);
							firts_name = product.getString(TAG_FIRSTNAME);
							email = product.getString(TAG_EMAIL);
							businessname = product.getString(TAG_BUSINESSNAME);
							businessphone = product
									.getString(TAG_BUSINESSPHONE);
							address = product.getString(TAG_ADDRESS);
							searchkeyword = product
									.getString(TAG_SEARCHKEYWORD);
							city_id = product.getString(TAG_CITYID);
							area_id = product.getString(TAG_AREAID);
							latitude = product.getString(TAG_LATITUDE);
							longitude = product.getString(TAG_LONGITUDE);
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
			id_city = city_id;
			id_area = area_id;
			new LoadCatId().execute();
		}
	}

	class LoadCatId extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			catids.clear();
			to_deletecat.clear();
			facids.clear();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			JSONObject json = jParser.makeHttpRequest(
					url_get_catids_for_business, "GET", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						catids.add(c.getString(TAG_CATID));
						facids.add(c.getString(TAG_FACILITYIDS));
						to_deletecat.add(c.getString(TAG_TODELETECATIDS));
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
			new LoadFacNames().execute();
		}
	}

	class LoadFacNames extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			facnames.clear();
			for (int i = 0; i < catids.size(); i++) {
				if (!(facids.get(i).equals("0") || facids.get(i).equals(""))) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("fac_id", facids.get(i)));
					JSONObject json = jParser.makeHttpRequest(
							url_get_facnames_for_business, "GET", params);

					try {
						int success = json.getInt(TAG_SUCCESS);

						if (success == 1) {
							products = json.getJSONArray(TAG_PRODUCTS);
							for (int j = 0; j < products.length(); j++) {
								JSONObject c = products.getJSONObject(j);
								facnames.add(c.getString(TAG_FACILITYNAME));
							}
						} else {
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					facnames.add("no_name");
				}
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			new LoadCatNames().execute();
		}
	}

	class LoadCatNames extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			catnames.clear();
			for (int i = 0; i < catids.size(); i++) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("cat_id", catids.get(i)));
				JSONObject json = jParser.makeHttpRequest(
						url_get_catnames_for_business, "GET", params);

				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						products = json.getJSONArray(TAG_PRODUCTS);
						for (int j = 0; j < products.length(); j++) {
							JSONObject c = products.getJSONObject(j);
							catnames.add(c.getString(TAG_CATNAME));
						}
					} else {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			addedcatdb.open();
			for (int k = 0; k < catids.size(); k++) {
				addedcatdb.insert(catids.get(k), catnames.get(k),
						facids.get(k), facnames.get(k));
			}
			addedcatdb.close();
			addedfacdb.open();
			for (int m = 0; m < facnames.size(); m++) {
				if (!facnames.get(m).equals("no_name")) {
					addedfacdb.insert(catids.get(m), catnames.get(m),
							facids.get(m), facnames.get(m));
				}
			}
			addedfacdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			new LoadCityName().execute();
		}
	}

	class LoadAllCity extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Searching...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			JSONObject json = jParser.makeHttpRequest(url_all_city, "GET",
					params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						String city_id = c.getString(TAG_CITYID);
						String cityname = c.getString(TAG_CITYNAME);
						String city_lat = c.getString(TAG_LATITUDE);
						String city_lon = c.getString(TAG_LONGITUDE);
						if (!city_id.equals("26")) {
							citylist.add(cityname);
							cityid.add(city_id);
							citylatitude.add(city_lat);
							citylongitude.add(city_lon);
						}
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
			CityAlertDialog();
		}
	}

	class LoadAllArea extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Searching...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(url_all_area, "GET",
					params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						String areaname = c.getString(TAG_AREANAME);
						String area_id = c.getString(TAG_AREAID);
						String city_id = c.getString(TAG_CITYID);

						if (city_id.equals(id_city)) {
							arealist.add(areaname);
							areaid.add(area_id);
						}
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
			AreaAlertDialog();
		}
	}

	class LoadCityName extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("city_id", city_id));
			JSONObject json = jParser.makeHttpRequest(url_get_city, "GET",
					params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						city_name = c.getString(TAG_CITYNAME);
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
			new LoadAreaName().execute();
		}
	}

	class LoadAreaName extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("area_id", area_id));
			JSONObject json = jParser.makeHttpRequest(url_get_area, "GET",
					params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						area_name = c.getString(TAG_AREANAME);
					}
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			selected_area = area_name;
			inputFirstName.setText(firts_name);
			inputEmail.setText(email);
			inputBusinessName.setText(businessname);
			inputBusinessPhone.setText(businessphone);
			inputBusinessSearch.setText(searchkeyword);
			inputAddress.setText(address);
			String categories = "";
			for (int i = 0; i < catnames.size(); i++) {
				if (i == 0) {
					categories = catnames.get(i);
				}
				if (i > 0) {
					categories = categories + ", " + catnames.get(i);
				}
			}
			selectCat.setText(categories);
			selectCity.setText(city_name);
			selectArea.setText(area_name);
			pDialog.dismiss();
		}
	}

	class UpdateUserDetails extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("first_name", inputFirstName
					.getText().toString()));
			params.add(new BasicNameValuePair("email_or_mob", inputEmail
					.getText().toString()));
			params.add(new BasicNameValuePair("business_name",
					inputBusinessName.getText().toString()));
			params.add(new BasicNameValuePair("business_phone",
					inputBusinessPhone.getText().toString()));
			params.add(new BasicNameValuePair("business_search",
					inputBusinessSearch.getText().toString()));
			params.add(new BasicNameValuePair("address", inputAddress.getText()
					.toString()));
			params.add(new BasicNameValuePair("areaid", id_area));
			params.add(new BasicNameValuePair("cityid", id_city));
			params.add(new BasicNameValuePair("latitude", latitude));
			params.add(new BasicNameValuePair("longitude", longitude));

			JSONObject json = jParser.makeHttpRequest(url_update_details,
					"POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					update_success = true;
				} else {
					update_success = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (update_success) {
				Toast.makeText(getApplicationContext(), "Updated",
						Toast.LENGTH_SHORT).show();
				update_success = false;

				Editor edit2 = businessnameprefs.edit();
				edit2.putString(strBusinessNameSet, inputBusinessName.getText()
						.toString());
				edit2.commit();

				Editor edit3 = businessphoneprefs.edit();
				edit3.putString(strBusinessPhoneSet, inputBusinessPhone
						.getText().toString());
				edit3.commit();

				Editor edit4 = businessaddressprefs.edit();
				edit4.putString(strBusinessAddressSet, inputAddress.getText()
						.toString());
				edit4.commit();

				Editor edit5 = businessareaprefs.edit();
				edit5.putString(strBusinessAreaSet, selected_area);
				edit5.commit();

				Intent i = new Intent(EditBusinessActivity.this,
						MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);
			} else {
				Toast.makeText(getApplicationContext(), "Not Updated",
						Toast.LENGTH_SHORT).show();
				update_success = false;
				finish();
			}
		}
	}

	class AddEditedCats extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			addedcatdb.open();
			int count = 0;
			String is_primary = "0";
			Cursor c = addedcatdb.getAll();
			if (c.moveToFirst()) {
				do {
					count++;
					if (count == 1) {
						is_primary = "1";
					}
					if (count > 1) {
						is_primary = "0";
					}
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("business_id",
							business_id));
					params.add(new BasicNameValuePair("category_id", c
							.getString(1)));
					params.add(new BasicNameValuePair("facility_ids", c
							.getString(3)));
					params.add(new BasicNameValuePair("is_primary", is_primary));

					JSONObject json = jParser.makeHttpRequest(
							url_add_editcatids, "POST", params);

					Log.d("Create Response", json.toString());

					try {
						int success = json.getInt(TAG_SUCCESS);

						if (success == 1) {

						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} while (c.moveToNext());
			}
			addedcatdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {

		}
	}

	class DeleteCatIds extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			for (int i = 0; i < to_deletecat.size(); i++) {
				int success;
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("id", to_deletecat.get(i)));

					JSONObject json = jParser.makeHttpRequest(
							url_delete_catids, "POST", params);

					Log.d("Delete Product", json.toString());

					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						update_success = true;
					} else {
						update_success = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
		}
	}

	class AddFacilities extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			CheckCatData();
			uploadusercatdb.open();
			int count = 0;
			String is_primary = "0";
			Cursor c = uploadusercatdb.getAll();
			if (c.moveToFirst()) {
				do {
					count++;
					if (count == 1) {
						is_primary = "1";
					}
					if (count > 1) {
						is_primary = "0";
					}
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("business_id",
							business_id));
					params.add(new BasicNameValuePair("category_id", c
							.getString(1)));
					params.add(new BasicNameValuePair("facility_ids", c
							.getString(2)));
					params.add(new BasicNameValuePair("is_primary", is_primary));

					JSONObject json = jParser.makeHttpRequest(
							url_add_facilites, "POST", params);

					Log.d("Create Response", json.toString());

					try {
						int success = json.getInt(TAG_SUCCESS);

						if (success == 1) {

						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} while (c.moveToNext());
			} else {

			}
			c.close();
			uploadusercatdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {

		}
	}

	private void CheckCatData() {
		addedcatdb.open();
		String cat_id = "";
		Cursor c1 = addedcatdb.getAll();
		if (c1.moveToFirst()) {
			do {
				cat_id = c1.getString(1);
				CheckFacData(cat_id);
			} while (c1.moveToNext());
		}
		c1.close();
		addedcatdb.close();
	}

	private void CheckFacData(String cat_id) {
		// TODO Auto-generated method stub
		boolean added = false;
		addedfacdb.open();
		uploadusercatdb.open();
		Cursor c2 = addedfacdb.getAll();
		if (c2.moveToFirst()) {
			do {
				if (c2.getString(1).equals(cat_id)) {
					uploadusercatdb.insert(c2.getString(1), c2.getString(3));
					added = true;
				}
			} while (c2.moveToNext());
		}
		if (!added) {
			uploadusercatdb.insert(cat_id, "0");
		}
		c2.close();
		addedfacdb.close();
		uploadusercatdb.close();
	}

	@SuppressWarnings("deprecation")
	protected void CityAlertDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);

		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, citylist);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected_city = citylist.get(arg2);
				selectCity.setText(selected_city);
				id_city = cityid.get(arg2);
				latitude = citylatitude.get(arg2);
				longitude = citylongitude.get(arg2);
				alertDialog.dismiss();

			}
		});
	}

	@SuppressWarnings("deprecation")
	protected void AreaAlertDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);
		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, arealist);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected_area = arealist.get(arg2);
				id_area = areaid.get(arg2);
				selectArea.setText(selected_area);
				alertDialog.dismiss();
			}
		});
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
				validateFirstName();
				break;
			case R.id.input_layout_mobileemail:
				validateEmail();
				break;
			case R.id.input_businessname:
				validateBusinessName();
				break;
			case R.id.input_businessphone:
				validateBusinessPhone();
				break;
			case R.id.input_businessearch:
				validateBusinessSearchKey();
				break;
			case R.id.input_address:
				validateAddress();
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		is_category_selected = false;
		super.onResume();
		String catbtntext = getResources().getString(
				R.string.hint_btn_add_category);
		int count = 0;
		addedcatdb.open();
		Cursor c4 = addedcatdb.getAll();
		if (c4.moveToFirst()) {
			do {
				is_category_selected = true;
				count++;
				if (count == 1) {
					catbtntext = c4.getString(2);
				}
				if (count > 1) {
					catbtntext = catbtntext + ", " + c4.getString(2);
				}
			} while (c4.moveToNext());
			selectCat.setText(catbtntext);
		} else {
			is_category_selected = false;
			selectCat.setText(catbtntext);
		}
		c4.close();
		addedcatdb.close();
	}
}