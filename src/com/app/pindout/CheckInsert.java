package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CheckInsert extends AppCompatActivity {
	JSONParser jsonParser = new JSONParser();
	private boolean register_success = false;
	private static String url_add_details = "http://10.0.2.2/pindout/add_multi.php";
	private static String url_last_business_id = "http://10.0.2.2/pindout/get_last_businessid.php";
	private static final String url_get_cityid = "http://10.0.2.2/pindout/testing_get_city_id.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID = "id";
	private static final String TAG_PRODUCT = "product";
	JSONArray products = null;
	String lastid = "blank";
	private ArrayList<String> cityidlist = new ArrayList<String>();

	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_LASTID = "id";
	private static ArrayList<String> insert = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.only_btn);
		Button click = (Button) findViewById(R.id.btnclick);

		click.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new LoadLastid().execute();
			}
		});
		cityidlist.clear();
		new LoadLastid().execute();
	}

	class AddDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CheckInsert.this);
			pDialog.setMessage("Inserting...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			for (int i = 0; i < insert.size(); i++) {
				register_success = false;
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("first_name", insert.get(i)));
				params.add(new BasicNameValuePair("last_name", insert.get(i)));
				params.add(new BasicNameValuePair("email_or_mob", insert.get(i)));
				params.add(new BasicNameValuePair("password", insert.get(i)));

				JSONObject json = jsonParser.makeHttpRequest(url_add_details,
						"POST", params);

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
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (register_success) {
				Toast.makeText(getApplicationContext(), "successful",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(), "Problem",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class LoadLastid extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CheckInsert.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("city_name", "Mumbai"));

			JSONObject json = jsonParser.makeHttpRequest(url_get_cityid, "GET",
					params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						cityidlist.add(c.getString(TAG_LASTID));
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
			for (int i = 0; i < cityidlist.size(); i++) {
				Toast.makeText(getApplicationContext(), cityidlist.get(i),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
