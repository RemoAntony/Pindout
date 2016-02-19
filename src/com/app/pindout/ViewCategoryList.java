package com.app.pindout;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.pindout.dbadapter.AddedCategoryDBAdapter;
import com.app.pindout.dbadapter.AddedFacilityDBAdapter;

public class ViewCategoryList extends AppCompatActivity {

	private AddedCategoryDBAdapter addedcatdb;
	private AddedFacilityDBAdapter addedfacdb;
	private ArrayAdapter<String> catadapter;
	private Toolbar toolbar;
	private ArrayList<String> catlist = new ArrayList<String>();
	private ListView lv;
	private int delete_position = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catfacdeletelist);
		lv = (ListView) findViewById(R.id.catfacdeleteListView);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addedcatdb = new AddedCategoryDBAdapter(this);
		addedfacdb = new AddedFacilityDBAdapter(this);
		displayCatList();
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void displayCatList() {
		// TODO Auto-generated method stub
		addedcatdb.open();
		Cursor c = addedcatdb.getAll();
		if (c.moveToFirst()) {
			do {
				catlist.add(c.getString(2));
			} while (c.moveToNext());
		}
		catadapter = new ArrayAdapter<String>(this, R.layout.catnfaclistitem,
				R.id.catfac_text, catlist);
		lv.setAdapter(catadapter);
		c.close();
		addedcatdb.close();

	}

	public void DeleteCat(View v) {
		View parentRow = (View) v.getParent();
		ListView list = (ListView) parentRow.getParent();
		delete_position = list.getPositionForView(parentRow);
		new DeleteCatnFac().execute();
	}

	class DeleteCatnFac extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewCategoryList.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			DeleteCategory();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			displayCatList();
		}
	}

	public void DeleteCategory() {
		// TODO Auto-generated method stub
		addedcatdb.open();
		String cat_name = catlist.get(delete_position);
		catlist.clear();
		addedcatdb.delete_by_cat_name(cat_name);
		addedcatdb.close();
		addedfacdb.open();
		Cursor c2 = addedfacdb.getAll();
		if (c2.moveToFirst()) {
			do {
				if (cat_name.equals(c2.getString(2))) {
					addedfacdb.delete_by_cat_name(cat_name);
				}
			} while (c2.moveToNext());
		}
		c2.close();
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