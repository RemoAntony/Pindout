package com.app.pindout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

@SuppressLint("InflateParams")
public class RegisterActivity_Fourth extends AppCompatActivity {

	private Toolbar toolbar;
	private EditText inputAddress;
	private TextInputLayout inputLayoutAddress;
	private Button selectCity, selectArea, btnNextFourth;
	private String id_city = "0", selected_city = "0", selected_area = "0";
	private Intent intent;
	JSONArray products = null;
	JSONParser jParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_CITYNAME = "cityname";
	private static final String TAG_AREANAME = "areaname";
	private static final String TAG_CITYID = "cityid";

	private ArrayList<String> citylist = new ArrayList<String>();
	private ArrayList<String> cityid = new ArrayList<String>();
	private ArrayList<String> arealist = new ArrayList<String>();

	private static String url_all_city = "http://10.0.2.2/pindout/get_all_city.php";
	private static String url_all_area = "http://10.0.2.2/pindout/get_area.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_fourth);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		intent = getIntent();
		inputLayoutAddress = (TextInputLayout) findViewById(R.id.input_layout_address);
		inputAddress = (EditText) findViewById(R.id.input_address);
		btnNextFourth = (Button) findViewById(R.id.btn_next_fourth);
		selectCity = (Button) findViewById(R.id.selectcity);
		selectArea = (Button) findViewById(R.id.selectarea);
		inputAddress.addTextChangedListener(new MyTextWatcher(inputAddress));

		selectCity.setText("Select Your City");
		selectArea.setText("Select Your Area");

		btnNextFourth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitForm();
			}
		});
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
				selectCity.setText(citylist.get(arg2));
				id_city = cityid.get(arg2);
				alertDialog.dismiss();
				selected_city = citylist.get(arg2);
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
				selectArea.setText(arealist.get(arg2));
				selected_area = arealist.get(arg2);
				alertDialog.dismiss();
			}
		});
	}

	private void submitForm() {
		if (!validateAddress()) {
			return;
		}
		if (selected_area.equals("0")) {
			Toast.makeText(getApplicationContext(), "Please select area",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (selected_city.equals("0")) {
			Toast.makeText(getApplicationContext(), "Please select city",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent i = new Intent(RegisterActivity_Fourth.this,
				RegisterActivity_Category.class);
		i.putExtra("first_name", intent.getStringExtra("first_name"));
		i.putExtra("last_name", intent.getStringExtra("last_name"));
		i.putExtra("email_or_mob", intent.getStringExtra("email_or_mob"));
		i.putExtra("password", intent.getStringExtra("password"));
		i.putExtra("business_name", intent.getStringExtra("business_name"));
		i.putExtra("business_phone", intent.getStringExtra("business_phone"));
		i.putExtra("business_search", intent.getStringExtra("business_search"));
		i.putExtra("address", inputAddress.getText().toString());
		i.putExtra("selected_city", selected_city);
		i.putExtra("selected_area", selected_area);
		startActivity(i);
		overridePendingTransition(R.anim.right_to_left_enter,
				R.anim.right_to_left_exit);
	}

	class LoadAllCity extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity_Fourth.this);
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
						String cityname = c.getString(TAG_CITYNAME);
						String city_id = c.getString(TAG_CITYID);
						citylist.add(cityname);
						cityid.add(city_id);
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
			pDialog = new ProgressDialog(RegisterActivity_Fourth.this);
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
						String city_id = c.getString(TAG_CITYID);
						if (city_id.equals(id_city)) {
							arealist.add(areaname);
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
			Collections.sort(arealist, String.CASE_INSENSITIVE_ORDER);
			AreaAlertDialog();
		}
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
			case R.id.input_address:
				validateAddress();
				break;
			}
		}
	}
}
