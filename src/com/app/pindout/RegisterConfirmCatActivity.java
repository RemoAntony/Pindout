package com.app.pindout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.app.pindout.dbadapter.CategoryDBAdapter;

public class RegisterConfirmCatActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private CategoryDBAdapter cat_db;
	private RecyclerView cat_recView;
	@SuppressWarnings("rawtypes")
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private int str_len = 0;
	private String[] myDataset;
	private Button register_btn;
	private CheckBox agree_check;
	private Intent intent;
	private String first_name = "0", last_name = "0", email_or_mob = "0",
			password = "0", business_name = "0", business_phone = "0",
			business_search = "0", address = "0", selected_city = "0",
			selected_area = "0";

	JSONParser jsonParser = new JSONParser();
	private boolean register_success = false;
	private static String url_add_details = "http://10.0.2.2/pindout/add_signup.php";
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat_confirm);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		cat_recView = (RecyclerView) findViewById(R.id.cat_recyler_view);
		register_btn = (Button) findViewById(R.id.btn_register);
		agree_check = (CheckBox) findViewById(R.id.agree_check);
		setSupportActionBar(toolbar);
		intent = getIntent();
		cat_db = new CategoryDBAdapter(this);

		displayCatList();

		myDataset = new String[str_len];

		insertData();

		cat_recView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		cat_recView.setLayoutManager(mLayoutManager);
		mAdapter = new ConfirmCatDataAdapter(myDataset);
		cat_recView.setAdapter(mAdapter);

		register_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (agree_check.isChecked()) {

					first_name = intent.getStringExtra("first_name");
					last_name = intent.getStringExtra("last_name");
					email_or_mob = intent.getStringExtra("email_or_mob");
					password = intent.getStringExtra("password");
					business_name = intent.getStringExtra("business_name");
					business_phone = intent.getStringExtra("business_phone");
					business_search = intent.getStringExtra("business_search");
					address = intent.getStringExtra("address");
					selected_city = intent.getStringExtra("selected_city");
					selected_area = intent.getStringExtra("selected_area");
					new AddDetails().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"You must agree to terms", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private void displayCatList() {
		// TODO Auto-generated method stub
		cat_db.open();
		Cursor c = cat_db.getAll();
		if (c.moveToFirst()) {
			do {
				str_len++;
			} while (c.moveToNext());
		}
		cat_db.close();

	}

	private void insertData() {
		cat_db.open();
		Cursor cursor = cat_db.getAll();
		int i = -1;
		if (cursor.moveToFirst()) {
			do {
				i++;
				myDataset[i] = cursor.getString(1);
			} while (cursor.moveToNext());
		}
		cat_db.close();
	}

	class AddDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterConfirmCatActivity.this);
			pDialog.setMessage("Registering...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("first_name", first_name));
			params.add(new BasicNameValuePair("last_name", last_name));
			params.add(new BasicNameValuePair("email_or_mob", email_or_mob));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("business_name", business_name));
			params.add(new BasicNameValuePair("business_phone", business_phone));
			params.add(new BasicNameValuePair("business_search",
					business_search));
			params.add(new BasicNameValuePair("address", address));
			params.add(new BasicNameValuePair("selected_city", selected_city));
			params.add(new BasicNameValuePair("selected_area", selected_area));

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

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (register_success) {
				Toast.makeText(getApplicationContext(),
						"Registration successful", Toast.LENGTH_SHORT).show();
				RegisterConfirmCatActivity.this.deleteDatabase("categorydb.db");
				Intent i = new Intent(RegisterConfirmCatActivity.this,
						Check_LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				overridePendingTransition(R.anim.right_to_left_enter,
						R.anim.right_to_left_exit);
			} else {
				Toast.makeText(getApplicationContext(),
						"Problem in registering", Toast.LENGTH_SHORT).show();
			}
		}
	}
}