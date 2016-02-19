package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class DeleteAfterCheck extends AppCompatActivity {

	String username = "", password = "", firstname = "not set",
			lastname = "not set", id = "not set";

	// Progress Dialog
	private ProgressDialog pDialog;
	private Intent intent;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single product url
	private static final String url_user_detials = "http://10.0.2.2/pindout/get_user_detail.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_ID = "id";
	private static final String TAG_FIRSTNAME = "firstname";
	private static final String TAG_LASTNAME = "lastname";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.only_btn);
		intent = getIntent();
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		new GetUserDetails().execute();
	}

	class GetUserDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DeleteAfterCheck.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username",
								"kingprppt"));
						params.add(new BasicNameValuePair("password", ""));

						JSONObject json = jsonParser.makeHttpRequest(
								url_user_detials, "GET", params);

						Log.d("Single Product Details", json.toString());

						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {

							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT); // JSON Array

							JSONObject product = productObj.getJSONObject(0);
							id = product.getString(TAG_ID);
							firstname = product.getString(TAG_FIRSTNAME);
							lastname = product.getString(TAG_LASTNAME);

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
			Toast.makeText(getApplicationContext(),
					firstname + "\n" + lastname, Toast.LENGTH_SHORT).show();
		}
	}
}