package com.app.pindout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pindout.dbadapter.AddedFacilityDBAdapter;

public class RegisterActivity_Facility extends AppCompatActivity {

	private ListView categoryList;
	private ListAdapter adapter;
	private Toolbar toolbar;
	private Intent intent;
	private AddedFacilityDBAdapter addedfacdb;
	private boolean is_catnfac_exist = false;
	private ArrayList<HashMap<String, String>> facnameList;
	private ArrayList<String> facids = new ArrayList<String>();

	private JSONParser jParser = new JSONParser();
	private JSONArray products = null;
	private String catfacid = "", catid = "", catname = "",
			selectedfacname = "";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_FACILITYNAME = "facilityname";
	private static final String TAG_FACILITYID = "facilityid";

	private static String url_all_categories = "http://10.0.2.2/pindout/get_all_facilities.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_facility);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		intent = getIntent();
		catid = intent.getStringExtra("catid");
		catfacid = intent.getStringExtra("catfacid");
		catname = intent.getStringExtra("catname");
		facnameList = new ArrayList<HashMap<String, String>>();
		categoryList = (ListView) findViewById(R.id.cat_list);
		addedfacdb = new AddedFacilityDBAdapter(this);

		categoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String fac_name = ((TextView) view.findViewById(R.id.textloc))
						.getText().toString();
				String fac_id = facids.get(position);
				new InsertIntoAddedCat().execute(fac_name, fac_id);
			}

		});
		String facparts[] = catfacid.split(",");
		for (int i = 0; i < facparts.length; i++) {
			facids.add(facparts[i]);
		}
		new LoadAllCategories().execute();
	}

	class InsertIntoAddedCat extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity_Facility.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			selectedfacname = params[0];
			String fac_id = params[1];
			addedfacdb.open();
			Cursor c = addedfacdb.getAll();
			if (c.moveToFirst()) {
				do {
					String get_catid = c.getString(1);
					String get_facid = c.getString(3);
					if (get_catid.equals(catid) && get_facid.equals(fac_id)) {
						is_catnfac_exist = true;
						break;
					}
				} while (c.moveToNext());
			}
			if (!is_catnfac_exist) {
				addedfacdb.insert(catid, selectedfacname, fac_id, catname);
			}
			addedfacdb.close();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (is_catnfac_exist) {
				Toast.makeText(getApplicationContext(),
						selectedfacname + " already exist in facility list",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						selectedfacname + " is added in facility list",
						Toast.LENGTH_SHORT).show();

			}
			is_catnfac_exist = false;
		}
	}

	class LoadAllCategories extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity_Facility.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			categoryList.setVisibility(View.INVISIBLE);
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			JSONObject json = jParser.makeHttpRequest(url_all_categories,
					"GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						String fac_name = c.getString(TAG_FACILITYNAME);
						String facilityids = c.getString(TAG_FACILITYID);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_FACILITYNAME, fac_name);
						for (int j = 0; j < facids.size(); j++) {
							if (facilityids.equals(facids.get(j))) {
								facnameList.add(map);
							}
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

			adapter = new SimpleAdapter(RegisterActivity_Facility.this,
					facnameList, R.layout.listcategory,
					new String[] { TAG_FACILITYNAME },
					new int[] { R.id.textloc });
			categoryList.setAdapter(adapter);
			pDialog.dismiss();
			categoryList.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}
