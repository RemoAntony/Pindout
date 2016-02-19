package com.app.pindout;

import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.dbadapter.AddedFacilityDBAdapter;
import com.app.pindout.dbadapter.FacilityDBAdapter;

public class RegisterFacilityActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private FacilityDBAdapter facdb;
	private AddedFacilityDBAdapter addedfacdb;
	private ArrayAdapter<String> adapter;
	private ListView fac_listview;
	private String get_cat_id, get_cat_name, get_cat_facids;
	private static String fac_id_parts[];
	private ArrayList<String> fac_nameList;
	private int get_position;
	private boolean is_facility_exist = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_facility_list);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		facdb = new FacilityDBAdapter(this);
		addedfacdb = new AddedFacilityDBAdapter(this);
		fac_listview = (ListView) findViewById(R.id.addfaclistview);
		Intent i = getIntent();
		get_cat_id = i.getStringExtra("cat_id");
		get_cat_name = i.getStringExtra("cat_name");
		get_cat_facids = i.getStringExtra("cat_fac_id");
		fac_id_parts = get_cat_facids.split(",");
		SetGetfac_ids();
		DisplayFacList();
		fac_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				get_position = position;
				new CheckFacility_n_List().execute();
			}
		});
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void SetGetfac_ids() {
		// TODO Auto-generated method stub
		fac_nameList = new ArrayList<String>();
		facdb.open();
		for (int i = 0; i < fac_id_parts.length; i++) {
			Cursor c = facdb.getAll();
			if (c.moveToFirst()) {
				do {
					if (c.getString(2).equals(fac_id_parts[i])) {
						fac_nameList.add(c.getString(1));
					}
				} while (c.moveToNext());
			}
			c.close();
		}
		facdb.close();
	}

	private void DisplayFacList() {
		// TODO Auto-generated method stub
		adapter = new ArrayAdapter<String>(this, R.layout.add_facility_row,
				R.id.fac_text, fac_nameList);
		fac_listview.setAdapter(adapter);
	}

	class CheckFacility_n_List extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterFacilityActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			CheckFacility();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (is_facility_exist) {
				pDialog.dismiss();
				Toast.makeText(
						getApplicationContext(),
						fac_nameList.get(get_position) + " of " + get_cat_name
								+ " category already exist in facility list",
						Toast.LENGTH_SHORT).show();
				is_facility_exist = false;
			} else {
				addedfacdb.open();
				addedfacdb.insert(get_cat_id, get_cat_name,
						fac_id_parts[get_position],
						fac_nameList.get(get_position));
				addedfacdb.close();
				pDialog.dismiss();
				Toast.makeText(
						getApplicationContext(),
						fac_nameList.get(get_position) + " for " + get_cat_name
								+ " added in facility list", Toast.LENGTH_SHORT)
						.show();
				is_facility_exist = false;
			}

		}
	}

	public void CheckFacility() {
		// TODO Auto-generated method stub
		String fac_id = fac_id_parts[get_position];
		addedfacdb.open();
		Cursor c = addedfacdb.getAll();
		if (c.moveToFirst()) {
			do {
				if (get_cat_id.equals(c.getString(1))
						&& fac_id.equals(c.getString(3))) {
					is_facility_exist = true;
					break;
				}
			} while (c.moveToNext());
		}
		c.close();
		addedfacdb.close();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}