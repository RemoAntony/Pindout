package com.app.pindout;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.pindout.dbadapter.AddedFacilityDBAdapter;

public class ViewFacilityList extends AppCompatActivity {

	private AddedFacilityDBAdapter addedfacdb;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> cat_name_list, fac_name_list, show_cat_fac_list;
	private ListView disp_list;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catfacdeletelist);
		disp_list = (ListView) findViewById(R.id.catfacdeleteListView);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addedfacdb = new AddedFacilityDBAdapter(this);
		cat_name_list = new ArrayList<String>();
		fac_name_list = new ArrayList<String>();
		show_cat_fac_list = new ArrayList<String>();
		InsertData();
		DisplayData();
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void InsertData() {
		// TODO Auto-generated method stub
		addedfacdb.open();
		Cursor c = addedfacdb.getAll();
		if (c.moveToFirst()) {
			do {
				cat_name_list.add(c.getString(2));
				fac_name_list.add(c.getString(4));
				show_cat_fac_list.add(c.getString(4) + "  (" + c.getString(2)
						+ ")");
			} while (c.moveToNext());
		}
		c.close();
		addedfacdb.close();
	}

	private void DisplayData() {
		// TODO Auto-generated method stub
		adapter = new ArrayAdapter<String>(this, R.layout.catnfaclistitem,
				R.id.catfac_text, show_cat_fac_list);
		disp_list.setAdapter(adapter);
	}

	public void DeleteCat(View v) {
		View parentRow = (View) v.getParent();
		ListView list = (ListView) parentRow.getParent();
		int positionofrow = list.getPositionForView(parentRow);

		addedfacdb.open();
		addedfacdb.delete_by_cat_n_fac_name(cat_name_list.get(positionofrow),
				fac_name_list.get(positionofrow));
		addedfacdb.close();

		cat_name_list.clear();
		fac_name_list.clear();
		show_cat_fac_list.clear();
		InsertData();
		DisplayData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}