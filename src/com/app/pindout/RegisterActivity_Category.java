package com.app.pindout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pindout.dbadapter.AddedCategoryDBAdapter;
import com.app.pindout.dbadapter.CategoryDBAdapter;
import com.app.pindout.dbadapter.FacilityDBAdapter;

@SuppressLint("InflateParams")
public class RegisterActivity_Category extends AppCompatActivity {

	private CategoryDBAdapter catdb;
	private AddedCategoryDBAdapter addedcatdb;
	private FacilityDBAdapter facdb;
	private ListView categoryList;
	private ListAdapter adapter;
	private EditText searchCategory;

	private ProgressDialog pDialog;

	private Toolbar toolbar;
	private String category;
	private boolean is_catexist = false;
	private ArrayList<HashMap<String, String>> catnameList;

	private JSONParser jParser = new JSONParser();
	private JSONArray products = null;
	private String catfacid = "", catid = "";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_CATNAME = "categoryname";
	private static final String TAG_CATID = "catid";
	private static final String TAG_FACILITYIDS = "facilityid";
	private static final String TAG_FACNAME = "facility_name";

	private static String url_all_categories = "http://www.pindout.com/mobi/pindout/get_all_categories.php";
	private static String url_all_facilities = "http://www.pindout.com/mobi/pindout/get_all_facilities.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_category);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		catdb = new CategoryDBAdapter(this);
		facdb = new FacilityDBAdapter(this);
		addedcatdb = new AddedCategoryDBAdapter(this);
		catnameList = new ArrayList<HashMap<String, String>>();
		categoryList = (ListView) findViewById(R.id.cat_list);

		searchCategory = (EditText) findViewById(R.id.input_search);

		categoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String category = ((TextView) view.findViewById(R.id.textloc))
						.getText().toString();
				new InsertIntoAddedCat().execute(category);
			}

		});
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		new LoadAllFacilities().execute();
	}

	class LoadAllFacilities extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity_Category.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			facdb.open();
			Cursor cursor = facdb.getAll();
			if (cursor.moveToFirst()) {
				do {

				} while (cursor.moveToNext());
			} else {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject json = jParser.makeHttpRequest(url_all_facilities,
						"GET", params);

				try {
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						products = json.getJSONArray(TAG_PRODUCTS);

						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							String facilityid = c.getString(TAG_FACILITYIDS);
							String facility_name = c.getString(TAG_FACNAME);
							facdb.insert(facility_name, facilityid);
						}
					} else {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			facdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {
			new LoadAllCategories().execute();
		}
	}

	class LoadAllCategories extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			categoryList.setVisibility(View.INVISIBLE);
		}

		protected String doInBackground(String... args) {
			catdb.open();
			Cursor cursor = catdb.getAll();
			if (cursor.moveToFirst()) {
				do {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_CATNAME, cursor.getString(2));
					catnameList.add(map);
				} while (cursor.moveToNext());
			} else {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject json = jParser.makeHttpRequest(url_all_categories,
						"GET", params);

				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						products = json.getJSONArray(TAG_PRODUCTS);

						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							String cat_id = c.getString(TAG_CATID);
							String cat_name = c.getString(TAG_CATNAME);
							String cat_facilityids = c
									.getString(TAG_FACILITYIDS);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(TAG_CATNAME, cat_name);
							catnameList.add(map);
							catdb.insert(cat_id, cat_name, cat_facilityids);
						}
					} else {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			catdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {
			adapter = new SimpleAdapter(RegisterActivity_Category.this,
					catnameList, R.layout.listcategory,
					new String[] { TAG_CATNAME }, new int[] { R.id.textloc });
			categoryList.setAdapter(adapter);
			pDialog.dismiss();
			categoryList.setVisibility(View.VISIBLE);

			searchCategory.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2,
						int arg3) {
					((SimpleAdapter) RegisterActivity_Category.this.adapter)
							.getFilter().filter(cs);
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {

				}

				@Override
				public void afterTextChanged(Editable arg0) {

				}
			});
		}
	}

	class InsertIntoAddedCat extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity_Category.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			category = params[0];
			catdb.open();
			addedcatdb.open();
			Cursor c = catdb.getAll();
			Cursor c2 = addedcatdb.getAll();
			if (c2.moveToFirst()) {
				do {
					if (category.equals(c2.getString(2))) {
						is_catexist = true;
						catid = c2.getString(1);
						catfacid = c2.getString(3);
						break;
					}
				} while (c2.moveToNext());
			}
			if (!is_catexist) {
				if (c.moveToFirst()) {
					do {
						String cat_name = c.getString(2);
						if (cat_name.equals(category)) {
							catid = c.getString(1);
							catfacid = c.getString(3);
							addedcatdb.insert(catid, cat_name, catfacid, "0");
							break;
						}
					} while (c.moveToNext());
				}
			}
			addedcatdb.close();
			catdb.close();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (is_catexist) {
				Toast.makeText(getApplicationContext(),
						category + " already exist in category list",
						Toast.LENGTH_SHORT).show();
				if (!catfacid.equals("0")) {
					Intent i = new Intent(RegisterActivity_Category.this,
							RegisterFacilityActivity.class);
					i.putExtra("cat_id", catid);
					i.putExtra("cat_name", category);
					i.putExtra("cat_fac_id", catfacid);
					startActivity(i);
				}
			} else {
				Toast.makeText(getApplicationContext(),
						category + " added in category list",
						Toast.LENGTH_SHORT).show();
				if (!catfacid.equals("0")) {
					Intent i = new Intent(RegisterActivity_Category.this,
							RegisterFacilityActivity.class);
					i.putExtra("cat_id", catid);
					i.putExtra("cat_name", category);
					i.putExtra("cat_fac_id", catfacid);
					startActivity(i);
				}
			}
			is_catexist = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_info) {
			AlertCatFacInfo();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	protected void AlertCatFacInfo() {
		ArrayList<String> catfacinfolist = new ArrayList<String>();
		catfacinfolist.add("Facilities");
		catfacinfolist.add("Categories");
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater.inflate(R.layout.catfacinfolist,
				null);
		alertDialog.setView(convertView);
		ListView lv = (ListView) convertView.findViewById(R.id.catfacListView);
		ArrayAdapter<String> catadapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, catfacinfolist);
		lv.setAdapter(catadapter);
		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				alertDialog.dismiss();
				if (position == 0) {
					Intent i = new Intent(RegisterActivity_Category.this,
							ViewFacilityList.class);
					startActivity(i);
				}
				if (position == 1) {
					Intent i = new Intent(RegisterActivity_Category.this,
							ViewCategoryList.class);
					startActivity(i);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}