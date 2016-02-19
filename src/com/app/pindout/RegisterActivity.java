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
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pindout.dbadapter.AddedCategoryDBAdapter;
import com.app.pindout.dbadapter.AddedFacilityDBAdapter;
import com.app.pindout.dbadapter.Upload_User_Categories_DBAdapter;

@SuppressLint({ "InflateParams", "DefaultLocale" })
public class RegisterActivity extends AppCompatActivity {

	private ProgressDialog pDialog;
	private Toolbar toolbar;
	private AddedCategoryDBAdapter addedcatdb;
	private AddedFacilityDBAdapter addedfacdb;
	private Upload_User_Categories_DBAdapter uploadusercatdb;

	private EditText inputFirstName, inputEmail, inputPassword,
			inputConfirmPass, inputBusinessName, inputBusinessPhone,
			inputBusinessSearch, inputAddress;

	private CheckBox agreeCheckBox;

	private TextInputLayout inputLayoutFirstName, inputLayoutEmail,
			inputLayoutPassword, inputLayoutConfirmPass,
			inputLayoutBusinessName, inputLayoutBusinessPhone,
			inputLayoutBusinessSearch, inputLayoutAddress;

	private TextView tnc_textView;

	private Button btn_register, selectCity, selectArea, addCategory;

	private String id_city = "0", id_area = "0", selected_city = "0",
			selected_area = "0", latitude = "0", longitude = "0",
			last_businessid = "0";

	private boolean is_user_exist = false, is_category_selected = false;

	JSONArray products = null;
	JSONParser jParser = new JSONParser();

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_CITYNAME = "cityname";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_AREANAME = "areaname";
	private static final String TAG_AREAID = "areaid";
	private static final String TAG_CITYID = "cityid";
	private static final String TAG_LASTID = "businessid";

	private ArrayList<String> citylist = new ArrayList<String>();
	private ArrayList<String> cityid = new ArrayList<String>();
	private ArrayList<String> citylatitude = new ArrayList<String>();
	private ArrayList<String> citylongitude = new ArrayList<String>();
	private ArrayList<String> arealist = new ArrayList<String>();
	private ArrayList<String> areaid = new ArrayList<String>();

	private boolean register_success = false;

	private static String url_add_details = "http://www.pindout.com/mobi/pindout/add_signup.php";
	private static String url_add_facilites = "http://www.pindout.com/mobi/pindout/add_multi.php";
	private static String url_all_city = "http://www.pindout.com/mobi/pindout/get_all_city.php";
	private static String url_validate_userid = "http://www.pindout.com/mobi/pindout/validate_emailid.php";
	private static String url_all_area = "http://www.pindout.com/mobi/pindout/get_area.php";
	private static String url_last_business_id = "http://www.pindout.com/mobi/pindout/get_last_businessid.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		RegisterActivity.this.deleteDatabase("Category.db");
		RegisterActivity.this.deleteDatabase("AddedCategory.db");
		RegisterActivity.this.deleteDatabase("AddedFacility.db");
		RegisterActivity.this.deleteDatabase("UploadUserCategories.db");
		RegisterActivity.this.deleteDatabase("Facility.db");

		inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);

		inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_mobileemail);
		inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
		inputLayoutConfirmPass = (TextInputLayout) findViewById(R.id.input_layout_confirmpass);
		inputLayoutBusinessName = (TextInputLayout) findViewById(R.id.input_layout_businessname);
		inputLayoutBusinessPhone = (TextInputLayout) findViewById(R.id.input_layout_businessphone);
		inputLayoutBusinessSearch = (TextInputLayout) findViewById(R.id.input_layout_businessearch);
		inputLayoutAddress = (TextInputLayout) findViewById(R.id.input_layout_address);

		inputFirstName = (EditText) findViewById(R.id.input_first_name);
		inputEmail = (EditText) findViewById(R.id.input_mobile_or_email);
		inputPassword = (EditText) findViewById(R.id.input_password);
		inputConfirmPass = (EditText) findViewById(R.id.input_confirm_pass);
		inputBusinessName = (EditText) findViewById(R.id.input_businessname);
		inputBusinessPhone = (EditText) findViewById(R.id.input_businessphone);
		inputBusinessSearch = (EditText) findViewById(R.id.input_businessearch);
		inputAddress = (EditText) findViewById(R.id.input_address);

		btn_register = (Button) findViewById(R.id.btn_register);
		selectCity = (Button) findViewById(R.id.selectcity);
		selectArea = (Button) findViewById(R.id.selectarea);
		addCategory = (Button) findViewById(R.id.addcategory);

		tnc_textView = (TextView) findViewById(R.id.tnc_text);

		agreeCheckBox = (CheckBox) findViewById(R.id.agree_check);

		addedcatdb = new AddedCategoryDBAdapter(this);
		addedfacdb = new AddedFacilityDBAdapter(this);
		uploadusercatdb = new Upload_User_Categories_DBAdapter(this);

		inputFirstName
				.addTextChangedListener(new MyTextWatcher(inputFirstName));
		inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
		inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
		inputConfirmPass.addTextChangedListener(new MyTextWatcher(
				inputConfirmPass));
		inputBusinessName.addTextChangedListener(new MyTextWatcher(
				inputBusinessName));
		inputBusinessPhone.addTextChangedListener(new MyTextWatcher(
				inputBusinessPhone));
		inputBusinessSearch.addTextChangedListener(new MyTextWatcher(
				inputBusinessSearch));
		inputAddress.addTextChangedListener(new MyTextWatcher(inputAddress));

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

		addCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(RegisterActivity.this,
						RegisterActivity_Category.class);
				startActivity(i);
				overridePendingTransition(R.anim.right_to_left_enter,
						R.anim.right_to_left_exit);
				// new LoadAllCategories().execute();
			}
		});

		tnc_textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(RegisterActivity.this, Terms.class);
				startActivity(i);
			}
		});

		btn_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submitForm();
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
		if (!validatePassword()) {
			return;
		}
		if (!validateConfirmPassword()) {
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
		if (id_city.equals("0")) {
			Toast.makeText(getApplicationContext(), "Please select city",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (id_area.equals("0")) {
			Toast.makeText(getApplicationContext(), "Please select area",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!is_category_selected) {
			Toast.makeText(getApplicationContext(),
					"You must select at least one category", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (!agreeCheckBox.isChecked()) {
			Toast.makeText(getApplicationContext(), "You must agree to terms",
					Toast.LENGTH_SHORT).show();
			return;
		}
		new ValidateEmail_or_Username().execute();
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

	private boolean validatePassword() {
		if (inputPassword.getText().toString().trim().isEmpty()) {
			inputLayoutPassword.setError(getString(R.string.err_msg_enterpass));
			requestFocus(inputPassword);
			return false;
		} else {
			if (inputPassword.length() < 8) {
				inputLayoutPassword
						.setError(getString(R.string.err_msg_pass_eight_char));
				requestFocus(inputPassword);
				return false;
			} else {
				inputLayoutPassword.setErrorEnabled(false);
			}
		}
		return true;
	}

	private boolean validateConfirmPassword() {
		if (inputConfirmPass.getText().toString().trim().isEmpty()) {
			inputLayoutConfirmPass
					.setError(getString(R.string.err_msg_enterconfirmpass));
			requestFocus(inputConfirmPass);
			return false;
		} else {
			String pass = inputPassword.getText().toString();
			String confirmpass = inputConfirmPass.getText().toString();
			if (pass.equals(confirmpass)) {
				inputLayoutConfirmPass.setErrorEnabled(false);
			} else {
				inputLayoutConfirmPass
						.setError(getString(R.string.err_msg_passdonotmatch));
				requestFocus(inputConfirmPass);
				return false;
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

	class ValidateEmail_or_Username extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Validating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", inputEmail.getText()
					.toString()));

			JSONObject json = jParser.makeHttpRequest(url_validate_userid,
					"POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_user_exist = true;
				} else {
					is_user_exist = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			if (is_user_exist) {
				pDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						"Email id already exist", Toast.LENGTH_SHORT).show();
			} else {
				is_user_exist = false;
				new AddDetails().execute();
			}
		}
	}

	class LoadAllCity extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
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
			pDialog = new ProgressDialog(RegisterActivity.this);
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

	class AddDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			String slug = "";
			String slug_parts[];
			slug = inputBusinessName.getText().toString().toLowerCase() + " "
					+ selected_area.toLowerCase();
			slug_parts = slug.split(" ");
			for (int i = 0; i < slug_parts.length; i++) {
				if (i == 0) {
					slug = slug_parts[i];
				} else {
					slug = slug + "_" + slug_parts[i];
				}
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("first_name", inputFirstName
					.getText().toString()));
			params.add(new BasicNameValuePair("last_name", "   "));
			params.add(new BasicNameValuePair("email_or_mob", inputEmail
					.getText().toString().toLowerCase()));
			params.add(new BasicNameValuePair("password", inputPassword
					.getText().toString()));
			params.add(new BasicNameValuePair("business_name",
					inputBusinessName.getText().toString()));
			params.add(new BasicNameValuePair("business_phone",
					inputBusinessPhone.getText().toString()));
			params.add(new BasicNameValuePair("business_search",
					inputBusinessSearch.getText().toString()));
			params.add(new BasicNameValuePair("address", inputAddress.getText()
					.toString()));
			params.add(new BasicNameValuePair("selected_city", selected_city));
			params.add(new BasicNameValuePair("selected_area", selected_area));
			params.add(new BasicNameValuePair("areaid", id_area));
			params.add(new BasicNameValuePair("cityid", id_city));
			params.add(new BasicNameValuePair("latitude", latitude));
			params.add(new BasicNameValuePair("longitude", longitude));
			params.add(new BasicNameValuePair("slug", slug));

			JSONObject json = jParser.makeHttpRequest(url_add_details, "POST",
					params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			new LoadLastid().execute();
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
							last_businessid));
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
							register_success = true;
						} else {
							register_success = false;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} while (c.moveToNext());
			} else {
				register_success = true;
			}
			c.close();
			uploadusercatdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (register_success) {
				Toast.makeText(getApplicationContext(),
						"Registration successful", Toast.LENGTH_SHORT).show();
				setContentView(R.layout.register_success_screen);
				register_success = false;
			} else {
				Toast.makeText(getApplicationContext(),
						"Problem in registering", Toast.LENGTH_SHORT).show();
				register_success = false;
				Intent i = new Intent(RegisterActivity.this,
						RegisterActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);
			}
		}
	}

	public void RegisterSuccessOk(View v) {
		Intent i = new Intent(RegisterActivity.this, MainActivityRemo.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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

	class LoadLastid extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			JSONObject json = jParser.makeHttpRequest(url_last_business_id,
					"GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					JSONObject c = products.getJSONObject(0);
					last_businessid = c.getString(TAG_LASTID);
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			new AddFacilities().execute();
		}
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
			case R.id.input_password:
				validatePassword();
				break;
			case R.id.input_confirm_pass:
				validateConfirmPassword();
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

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { int id =
	 * item.getItemId();
	 * 
	 * if (id == R.id.action_info) { AlertCatFacInfo(); return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

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
			addCategory.setText(catbtntext);
		} else {
			is_category_selected = false;
			addCategory.setText(catbtntext);
		}
		c4.close();
		addedcatdb.close();
	}
}