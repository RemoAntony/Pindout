package com.app.pindout.businessdeals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.JSONParser;
import com.app.pindout.R;
import com.app.pindout.dbadapter.ActiveBusinessDealDBAdapter;
import com.app.pindout.dbadapter.InactiveBusinessDealDBAdapter;
import com.app.pindout.fragments.ActiveDealsFragment;
import com.app.pindout.fragments.InactiveDeals;
import com.app.pindout.helpers.ActiveDealsListViewAdapter;
import com.app.pindout.helpers.InActiveDealsListViewAdapter;

public class DisplayDealsActivity extends AppCompatActivity {

	public static Activity activity;
	private Toolbar mToolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private String business_id = "", business_name = "";
	private Intent intent;
	private ActiveBusinessDealDBAdapter activebusinessdealdb;
	private InactiveBusinessDealDBAdapter inactivebusinessdealsdb;

	ArrayList<HashMap<String, String>> active_businessdeal_list;
	ArrayList<HashMap<String, String>> inactive_businessdeal_list;

	ActiveDealsListViewAdapter activedeals_adapter;
	InActiveDealsListViewAdapter inactivedeals_adapter;

	private boolean is_deal_deleted = false, is_deal_activated = false,
			is_deal_deactivated = false, is_deal_admin_approved = false;
	private String edit_catname = "", deal_image = "";

	JSONParser jsonParser = new JSONParser();
	JSONArray products = null;

	private ArrayList<String> dealname = new ArrayList<String>();
	private ArrayList<String> row_id = new ArrayList<String>();
	private ArrayList<String> dealcatid = new ArrayList<String>();
	private ArrayList<String> catname = new ArrayList<String>();
	private ArrayList<String> admin_approval = new ArrayList<String>();
	private ArrayList<String> status = new ArrayList<String>();
	private ArrayList<String> deal_desc = new ArrayList<String>();
	private ArrayList<String> main_price_list = new ArrayList<String>();
	private ArrayList<String> deal_price_list = new ArrayList<String>();
	private ArrayList<String> deal_image_list = new ArrayList<String>();

	private static final String url_get_business_deals = "http://pindout.com/mobi/pindout/get_business_deals.php";
	private static final String url_get_businesscat_name = "http://pindout.com/mobi/pindout/get_businesscat_name.php";
	private static final String url_delete_deal = "http://pindout.com/mobi/pindout/delete_businessdeal.php";
	private static final String url_activate_deal = "http://pindout.com/mobi/pindout/activate_deal.php";
	private static final String url_deactivate_deal = "http://pindout.com/mobi/pindout/deactivate_deal.php";
	private static final String url_get_edit_business_deals = "http://pindout.com/mobi/pindout/get_edit_business_deals.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_ROWID = "row_id";
	public static final String TAG_DEALNAME = "dealname";
	public static final String TAG_DEALDESC = "deal_desc";
	private static final String TAG_CATID = "catid";
	public static final String TAG_CATNAME = "catname";
	private static final String TAG_ADMINAPPROVAL = "adminapproval";
	private static final String TAG_STATUS = "status";

	private static final String TAG_DEALURL = "deal_url";
	public static final String TAG_MAINPRICE = "main_price";
	public static final String TAG_DEALPRICE = "deal_price";
	private static final String TAG_ENDDATE = "end_date";
	public static final String TAG_DEALIMG = "deal_image";

	private String deal_url = "", deal_name = "", deal_id = "",
			deal_catid = "", deal_description = "", main_price = "",
			deal_price = "", end_date = "";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_inactive_deals);
		activity = this;
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(mToolbar);
		mToolbar.setTitle("My Deals");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		setupViewPager(viewPager);
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
		intent = getIntent();
		business_id = intent.getStringExtra("business_id");
		business_name = intent.getStringExtra("businessname");

		activebusinessdealdb = new ActiveBusinessDealDBAdapter(this);
		inactivebusinessdealsdb = new InactiveBusinessDealDBAdapter(this);

		new LoadBusinessDeals().execute();

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						super.onPageSelected(position);
						inactivedisplayListView();
					}
				});
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(
				getSupportFragmentManager());
		adapter.addFragment(new ActiveDealsFragment(), "Active Deals");
		adapter.addFragment(new InactiveDeals(), "Inactive Deals");
		viewPager.setAdapter(adapter);
	}

	class ViewPagerAdapter extends FragmentPagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
		private final List<String> mFragmentTitleList = new ArrayList<String>();

		public ViewPagerAdapter(FragmentManager manager) {
			super(manager);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title) {
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}

	class LoadBusinessDeals extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayDealsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			dealname.clear();
			row_id.clear();
			dealcatid.clear();
			deal_desc.clear();
			main_price_list.clear();
			deal_price_list.clear();
			DisplayDealsActivity.this.deleteDatabase("ActiveBusinessDeal.db");
			DisplayDealsActivity.this.deleteDatabase("InActiveBusinessDeal.db");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));

			JSONObject json = jsonParser.makeHttpRequest(
					url_get_business_deals, "GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						row_id.add(c.getString(TAG_ROWID));
						dealname.add(c.getString(TAG_DEALNAME));
						dealcatid.add(c.getString(TAG_CATID));
						admin_approval.add(c.getString(TAG_ADMINAPPROVAL));
						status.add(c.getString(TAG_STATUS));
						deal_desc.add(c.getString(TAG_DEALDESC));
						main_price_list.add(c.getString(TAG_MAINPRICE));
						deal_price_list.add(c.getString(TAG_DEALPRICE));
						deal_image_list
								.add("http://pindout.com/files/businessdeal_images/main_images/"
										+ c.getString(TAG_DEALIMG));
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
			if (row_id.size() == 0) {
				setContentView(R.layout.activity_no_deal_found);
				Toast.makeText(getApplicationContext(), "Add a deal first",
						Toast.LENGTH_SHORT).show();
			} else {
				new LoadDealCategory().execute();
			}
		}
	}

	class LoadDealCategory extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayDealsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			catname.clear();
			active_businessdeal_list = new ArrayList<HashMap<String, String>>();
			inactive_businessdeal_list = new ArrayList<HashMap<String, String>>();
			active_businessdeal_list.clear();
			inactive_businessdeal_list.clear();

			for (int j = 0; j < row_id.size(); j++) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("dealcat_id", dealcatid
						.get(j)));

				JSONObject json = jsonParser.makeHttpRequest(
						url_get_businesscat_name, "GET", params);

				try {

					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						products = json.getJSONArray(TAG_PRODUCTS);

						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							catname.add(c.getString(TAG_CATNAME));
						}
					} else {

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			activebusinessdealdb.open();
			inactivebusinessdealsdb.open();
			for (int i = 0; i < row_id.size(); i++) {
				if (admin_approval.get(i).equals("1")
						&& status.get(i).equals("1")) {
					activebusinessdealdb.insert(row_id.get(i), dealname.get(i),
							catname.get(i), main_price_list.get(i),
							deal_price_list.get(i), deal_desc.get(i),
							deal_image_list.get(i));
				} else {
					inactivebusinessdealsdb.insert(row_id.get(i),
							dealname.get(i), catname.get(i),
							main_price_list.get(i), deal_price_list.get(i),
							deal_desc.get(i), deal_image_list.get(i));
				}
			}
			activebusinessdealdb.close();
			inactivebusinessdealsdb.close();

			activebusinessdealdb.open();
			inactivebusinessdealsdb.open();

			Cursor c = activebusinessdealdb.getAll();
			if (c.moveToFirst()) {
				do {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("dealname", c.getString(2));
					map.put("catname", c.getString(3));
					map.put("main_price", c.getString(4));
					map.put("deal_price", c.getString(5));
					map.put("deal_desc", c.getString(6));
					map.put("deal_image", c.getString(7));
					active_businessdeal_list.add(map);
				} while (c.moveToNext());
			}

			Cursor c2 = inactivebusinessdealsdb.getAll();
			if (c2.moveToFirst()) {
				do {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("dealname", c2.getString(2));
					map.put("catname", c2.getString(3));
					map.put("main_price", c2.getString(4));
					map.put("deal_price", c2.getString(5));
					map.put("deal_desc", c2.getString(6));
					map.put("deal_image", c2.getString(7));
					inactive_businessdeal_list.add(map);
				} while (c2.moveToNext());
			}

			activebusinessdealdb.close();
			inactivebusinessdealsdb.close();
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			activedisplayListView();
		}
	}

	private void activedisplayListView() {
		ListView listView = (ListView) findViewById(R.id.active_dealslist);
		activedeals_adapter = new ActiveDealsListViewAdapter(
				DisplayDealsActivity.this, active_businessdeal_list);
		listView.setAdapter(activedeals_adapter);
	}

	private void inactivedisplayListView() {
		ListView listView = (ListView) findViewById(R.id.inactive_dealslist);
		inactivedeals_adapter = new InActiveDealsListViewAdapter(
				DisplayDealsActivity.this, inactive_businessdeal_list);
		listView.setAdapter(inactivedeals_adapter);
	}

	public void Activate_Deal(View v) {
		ListView listview = (ListView) findViewById(R.id.inactive_dealslist);
		int position = listview.getPositionForView(v);
		position++;
		inactivebusinessdealsdb.open();
		Cursor c = inactivebusinessdealsdb.get(position);
		String deal_id = c.getString(1);
		String deal_name = c.getString(2);
		inactivebusinessdealsdb.close();
		ActivateDeal_alertDialog(deal_id, deal_name);
	}

	public void InActivate_Deal(View v) {
		ListView listview = (ListView) findViewById(R.id.active_dealslist);
		int position = listview.getPositionForView(v);
		position++;
		activebusinessdealdb.open();
		Cursor c = activebusinessdealdb.get(position);
		String deal_id = c.getString(1);
		String deal_name = c.getString(2);
		activebusinessdealdb.close();
		DeActivateDeal_alertDialog(deal_id, deal_name);
	}

	public void Delete_Inactive_Deal(View v) {
		ListView listview = (ListView) findViewById(R.id.inactive_dealslist);
		int position = listview.getPositionForView(v);
		position++;
		inactivebusinessdealsdb.open();
		Cursor c = inactivebusinessdealsdb.get(position);
		String deal_id = c.getString(1);
		String deal_name = c.getString(2);
		inactivebusinessdealsdb.close();
		alertDelete(deal_id, deal_name);
	}

	public void Delete_ActiveDeal(View v) {
		ListView listview = (ListView) findViewById(R.id.active_dealslist);
		int position = listview.getPositionForView(v);
		position++;
		activebusinessdealdb.open();
		Cursor c = activebusinessdealdb.get(position);
		String deal_id = c.getString(1);
		String deal_name = c.getString(2);
		activebusinessdealdb.close();
		alertDelete(deal_id, deal_name);
	}

	public void ActiveMoreOption(View view) {
		ListView listview = (ListView) findViewById(R.id.active_dealslist);
		int dataposition = listview.getPositionForView(view);
		dataposition++;
		activebusinessdealdb.open();
		Cursor c = activebusinessdealdb.get(dataposition);
		String deal_id = c.getString(1);
		String deal_name = c.getString(2);
		String cat_name = c.getString(3);
		alertEditDealdialog(deal_id, deal_name, cat_name);
	}

	public void InactiveMoreOption(View view) {
		ListView listview = (ListView) findViewById(R.id.inactive_dealslist);
		int dataposition = listview.getPositionForView(view);
		dataposition++;
		inactivebusinessdealsdb.open();
		Cursor c = inactivebusinessdealsdb.get(dataposition);
		String deal_id = c.getString(1);
		String deal_name = c.getString(2);
		String cat_name = c.getString(3);
		alertEditDealdialog(deal_id, deal_name, cat_name);
	}

	@SuppressWarnings("deprecation")
	private void ActivateDeal_alertDialog(final String deal_id, String deal_name) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DisplayDealsActivity.this).create();

		alertDialog.setTitle("Activate Deal " + "\"" + deal_name + "\"" + " ?");

		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new ActivateDeal().execute(deal_id);
			}
		});
		alertDialog.setButton2("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void DeActivateDeal_alertDialog(final String deal_id,
			String deal_name) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DisplayDealsActivity.this).create();

		alertDialog.setTitle("Deactivate Deal " + "\"" + deal_name + "\""
				+ " ?");

		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new DeactivateDeal().execute(deal_id);
			}
		});
		alertDialog.setButton2("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertDelete(final String deal_id, String deal_name) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DisplayDealsActivity.this).create();

		alertDialog.setTitle("Delete Deal " + "\"" + deal_name + "\"" + " ?");

		alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new DeleteDeal().execute(deal_id);
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertActiveDealDescdialog(final String deal_desc) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DisplayDealsActivity.this).create();

		alertDialog.setTitle("Deal Description");
		alertDialog.setMessage(deal_desc);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertEditDealdialog(final String deal_id,
			final String deal_name, final String cat_name) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DisplayDealsActivity.this).create();
		alertDialog.setTitle("Edit Deal " + "\"" + deal_name + "\"" + " ?");
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				new LoadEditDataBusinessDeal().execute(deal_id, deal_name,
						cat_name);
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void ActiveAlertEditDescDialog(final View view) {
		final ArrayList<String> editdesc_deals = new ArrayList<String>();
		editdesc_deals.add("Edit Deal");
		editdesc_deals.add("View Description");
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Edit/View");
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);

		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, editdesc_deals);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				alertDialog.dismiss();
				if (position == 0) {
					ListView listview = (ListView) findViewById(R.id.active_dealslist);
					int dataposition = listview.getPositionForView(view);
					dataposition++;
					activebusinessdealdb.open();
					Cursor c = activebusinessdealdb.get(dataposition);
					String deal_id = c.getString(1);
					String deal_name = c.getString(2);
					String cat_name = c.getString(3);
					alertEditDealdialog(deal_id, deal_name, cat_name);
				}
				if (position == 1) {
					ListView listview = (ListView) findViewById(R.id.active_dealslist);
					int dataposition = listview.getPositionForView(view);
					dataposition++;
					activebusinessdealdb.open();
					Cursor c = activebusinessdealdb.get(dataposition);
					String deal_desc = c.getString(6);
					alertActiveDealDescdialog(deal_desc);
				}
			}
		});
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void InActiveAlertEditDescDialog(final View view) {
		final ArrayList<String> editdesc_deals = new ArrayList<String>();
		editdesc_deals.add("Edit Deal");
		editdesc_deals.add("View Description");
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Edit/View");
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);

		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, editdesc_deals);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				alertDialog.dismiss();
				if (position == 0) {
					ListView listview = (ListView) findViewById(R.id.inactive_dealslist);
					int dataposition = listview.getPositionForView(view);
					dataposition++;
					inactivebusinessdealsdb.open();
					Cursor c = inactivebusinessdealsdb.get(dataposition);
					String deal_id = c.getString(1);
					String deal_name = c.getString(2);
					String cat_name = c.getString(3);
					alertEditDealdialog(deal_id, deal_name, cat_name);
					inactivebusinessdealsdb.close();
				}
				if (position == 1) {
					ListView listview = (ListView) findViewById(R.id.inactive_dealslist);
					int dataposition = listview.getPositionForView(view);
					dataposition++;
					inactivebusinessdealsdb.open();
					Cursor c = inactivebusinessdealsdb.get(dataposition);
					String deal_desc = c.getString(6);
					alertActiveDealDescdialog(deal_desc);
					inactivebusinessdealsdb.close();
				}
			}
		});
	}

	class DeleteDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayDealsActivity.this);
			pDialog.setMessage("Deleting deal...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("deal_id", args[0]));

				JSONObject json = jsonParser.makeHttpRequest(url_delete_deal,
						"POST", params);

				Log.d("Delete Product", json.toString());

				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					is_deal_deleted = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_deal_deleted) {
				Toast.makeText(getApplicationContext(), "Deal deleted",
						Toast.LENGTH_SHORT).show();
				is_deal_deleted = false;
				Intent intent1 = new Intent(DisplayDealsActivity.this,
						DisplayDealsActivity.class);
				intent1.putExtra("business_id", business_id);
				intent1.putExtra("businessname", business_name);
				startActivity(intent1);
				finish();
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);
			} else {
				Toast.makeText(getApplicationContext(), "Deal not deleted",
						Toast.LENGTH_SHORT).show();
				is_deal_deleted = false;
			}
		}
	}

	class ActivateDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayDealsActivity.this);
			pDialog.setMessage("Updating Deal status...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", args[0]));

			JSONObject json = jsonParser.makeHttpRequest(url_activate_deal,
					"POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					is_deal_activated = true;
				} else {
					is_deal_activated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < row_id.size(); i++) {
				if (row_id.get(i).equals(args[0])) {
					if (admin_approval.get(i).equals("1")) {
						is_deal_admin_approved = true;
						break;
					}
				}
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_deal_admin_approved) {
				if (is_deal_activated) {
					Toast.makeText(getApplicationContext(),
							"Deal status activated", Toast.LENGTH_SHORT).show();
					is_deal_activated = false;
					Intent intent1 = new Intent(DisplayDealsActivity.this,
							DisplayDealsActivity.class);
					intent1.putExtra("business_id", business_id);
					intent1.putExtra("businessname", business_name);
					startActivity(intent1);
					finish();
					overridePendingTransition(R.anim.abc_fade_in,
							R.anim.abc_fade_out);

				} else {
					Toast.makeText(getApplicationContext(),
							"No action performed", Toast.LENGTH_SHORT).show();
					is_deal_activated = false;
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Admin approval required", Toast.LENGTH_SHORT).show();
				is_deal_activated = false;
				is_deal_admin_approved = false;
			}
		}
	}

	class DeactivateDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayDealsActivity.this);
			pDialog.setMessage("Updating Deal status...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", args[0]));

			JSONObject json = jsonParser.makeHttpRequest(url_deactivate_deal,
					"POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_deal_deactivated = true;
				} else {
					is_deal_deactivated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_deal_deactivated) {
				Toast.makeText(getApplicationContext(),
						"Deal status deactivated", Toast.LENGTH_SHORT).show();
				is_deal_deactivated = false;
				Intent intent1 = new Intent(DisplayDealsActivity.this,
						DisplayDealsActivity.class);
				intent1.putExtra("business_id", business_id);
				intent1.putExtra("businessname", business_name);
				startActivity(intent1);
				finish();
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);

			} else {
				Toast.makeText(getApplicationContext(), "No action performed",
						Toast.LENGTH_SHORT).show();
				is_deal_deactivated = false;
			}
		}
	}

	class LoadEditDataBusinessDeal extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayDealsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", args[0]));
			deal_id = args[0];
			deal_name = args[1];
			edit_catname = args[2];
			JSONObject json = jsonParser.makeHttpRequest(
					url_get_edit_business_deals, "GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						deal_catid = c.getString(TAG_CATID);
						deal_description = c.getString(TAG_DEALDESC);
						deal_url = c.getString(TAG_DEALURL);
						deal_image = c.getString(TAG_DEALIMG);
						main_price = c.getString(TAG_MAINPRICE);
						deal_price = c.getString(TAG_DEALPRICE);
						end_date = c.getString(TAG_ENDDATE);
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
			Intent intent = new Intent(DisplayDealsActivity.this,
					EditDealActivity.class);
			intent.putExtra("deal_id", deal_id);
			intent.putExtra("deal_title", deal_name);
			intent.putExtra("deal_catid", deal_catid);
			intent.putExtra("deal_catname", edit_catname);
			intent.putExtra("deal_desc", deal_description);
			intent.putExtra("deal_url", deal_url);
			intent.putExtra("deal_image", deal_image);
			intent.putExtra("deal_mainprice", main_price);
			intent.putExtra("deal_dealprice", deal_price);
			String[] date_parts = end_date.split(" ");
			intent.putExtra("end_date", date_parts[0]);
			intent.putExtra("business_id", business_id);
			intent.putExtra("businessname", business_name);
			startActivity(intent);
			overridePendingTransition(R.anim.right_to_left_enter,
					R.anim.right_to_left_exit);
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		onBackEvent();
	}

	private void onBackEvent() {
		finish();
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	}
}
