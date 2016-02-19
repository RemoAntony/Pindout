package com.app.pindout.businessdeals;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pindout.JSONParser;
import com.app.pindout.R;
import com.app.pindout.cachingimages.ImageLoader;

public class DealActivity extends AppCompatActivity {

	private Toolbar toolBar;
	private ImageView deal_image;
	private TextView deal_name, deal_main_price, deal_price, deal_cat,
			deal_desc, deal_url, deal_end_date;
	private String deal_id = "not_set", deal_status = "not_set",
			deal_adminapproval = "not_set", stringmain_price = "not_set",
			stringdeal_price = "not_set";
	private String deal_catid = "", end_date = "", stringdeal_img = "",
			stringdeal_url;
	private ImageLoader imageLoader;
	private ImageView activate_deal, inactivate_deal, delete_deal, edit_deal;
	private Intent intent;

	public static final String BusinessIdPrefs = "BusinessIdPrefs";
	public static final String strBusinessId = "0";
	public SharedPreferences businessidprefs;

	public static final String BusinessNamePrefs = "BusinessNamePrefs";
	public static final String strBusinessNameSet = "0";
	public SharedPreferences businessnameprefs;

	public static final String IsEdit = "IsEditPrefs";
	public static final String strIsEditSet = "0";
	public SharedPreferences iseditprefs;

	JSONParser jsonParser = new JSONParser();
	private boolean is_deal_status_changed = false, is_deal_deleted = false;

	JSONArray products = null;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	public static final String TAG_MAINPRICE = "main_price";
	public static final String TAG_DEALPRICE = "deal_price";
	private static final String TAG_ENDDATE = "end_date";
	public static final String TAG_DEALIMG = "deal_image";
	public static final String TAG_DEALNAME = "dealname";
	public static final String TAG_DEALDESC = "deal_desc";
	private static final String TAG_CATID = "catid";

	private static final String url_delete_deal = "http://www.pindout.com/mobi/pindout/delete_businessdeal.php";
	private static final String url_activate_deal = "http://www.pindout.com/mobi/pindout/activate_deal.php";
	private static final String url_deactivate_deal = "http://www.pindout.com/mobi/pindout/deactivate_deal.php";
	private static final String url_get_edit_business_deals = "http://www.pindout.com/mobi/pindout/get_edit_business_deals.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_deal);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		deal_name = (TextView) findViewById(R.id.deal_name);
		deal_main_price = (TextView) findViewById(R.id.showmain_price);
		deal_price = (TextView) findViewById(R.id.showdeal_price);
		deal_cat = (TextView) findViewById(R.id.deal_cat);
		deal_desc = (TextView) findViewById(R.id.deal_desc);
		deal_url = (TextView) findViewById(R.id.deal_url);
		deal_end_date = (TextView) findViewById(R.id.deal_end_date);
		deal_image = (ImageView) findViewById(R.id.deal_img);
		activate_deal = (ImageView) findViewById(R.id.activate_deal);
		inactivate_deal = (ImageView) findViewById(R.id.deactivate_deal);
		delete_deal = (ImageView) findViewById(R.id.delete_deal);
		edit_deal = (ImageView) findViewById(R.id.edit_deal);

		businessidprefs = getSharedPreferences(BusinessIdPrefs,
				Context.MODE_PRIVATE);
		businessnameprefs = getSharedPreferences(BusinessNamePrefs,
				Context.MODE_PRIVATE);
		iseditprefs = getSharedPreferences(IsEdit, Context.MODE_PRIVATE);

		intent = getIntent();
		imageLoader = new ImageLoader(this);
		deal_id = intent.getStringExtra("deal_id");
		deal_status = intent.getStringExtra("deal_status");
		deal_adminapproval = intent.getStringExtra("deal_adminapproval");
		deal_name.setText(intent.getStringExtra("deal_name"));
		stringdeal_price = intent.getStringExtra("deal_price");
		stringmain_price = intent.getStringExtra("deal_main_price");
		deal_main_price.setText(stringmain_price);
		deal_price.setText(stringdeal_price);
		deal_cat.setText(intent.getStringExtra("deal_cat"));
		deal_desc.setText(intent.getStringExtra("deal_desc"));
		stringdeal_url = intent.getStringExtra("deal_url");
		deal_url.setClickable(true);
		deal_url.setMovementMethod(LinkMovementMethod.getInstance());
		String url = "Click here to open deal on web";
		deal_url.setText(Html.fromHtml(url));
		deal_url.setVisibility(View.INVISIBLE);
		imageLoader.DisplayImage(intent.getStringExtra("deal_image"),
				deal_image);

		// Toast.makeText(getApplicationContext(),
		// deal_status + "\n" + deal_adminapproval, Toast.LENGTH_SHORT)
		// .show();

		if (deal_status.equals("1") && deal_adminapproval.equals("1")) {
			activate_deal.setVisibility(View.INVISIBLE);
		} else {
			inactivate_deal.setVisibility(View.INVISIBLE);
		}

		toolBar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
		activate_deal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (deal_status.equals("0")) {
					ActivateDeal_alertDialog();
				} else {
					Toast.makeText(getApplicationContext(),
							"Admin approval required", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		inactivate_deal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeActivateDeal_alertDialog();
			}
		});
		delete_deal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDeleteDealdialog();
			}
		});
		edit_deal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertEditDealdialog();
			}
		});
		new GetBusinessDealDetails().execute();
	}

	@SuppressWarnings("deprecation")
	private void ActivateDeal_alertDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DealActivity.this).create();

		alertDialog.setTitle("Activate Deal " + "\""
				+ deal_name.getText().toString() + "\"" + " ?");

		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new ActivateDeal().execute();
			}
		});
		alertDialog.setButton2("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void DeActivateDeal_alertDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DealActivity.this).create();

		alertDialog.setTitle("Deactivate Deal " + "\""
				+ deal_name.getText().toString() + "\"" + " ?");

		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new DeactivateDeal().execute();
			}
		});
		alertDialog.setButton2("No", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertDeleteDealdialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DealActivity.this).create();

		alertDialog.setTitle("Delete Deal " + "\""
				+ deal_name.getText().toString() + "\"" + " ?");

		alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new DeleteDeal().execute();
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertEditDealdialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				DealActivity.this).create();
		alertDialog.setTitle("Edit Deal " + "\""
				+ deal_name.getText().toString() + "\"" + " ?");
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SendToEditDealActiivty();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	/*
	 * class CheckDealAdminApproval extends AsyncTask<String, String, String> {
	 * private ProgressDialog pDialog;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); pDialog =
	 * new ProgressDialog(DealActivity.this);
	 * pDialog.setMessage("Updating Deal status...");
	 * pDialog.setIndeterminate(false); pDialog.setCancelable(false);
	 * pDialog.show(); }
	 * 
	 * protected String doInBackground(String... args) {
	 * 
	 * List<NameValuePair> params = new ArrayList<NameValuePair>();
	 * params.add(new BasicNameValuePair("deal_id", deal_id));
	 * 
	 * JSONObject json = jsonParser.makeHttpRequest(
	 * url_get_catids_for_business, "POST", params);
	 * 
	 * try { int success = json.getInt(TAG_SUCCESS);
	 * 
	 * if (success == 1) { is_deal_admin_approved = true; } else {
	 * is_deal_admin_approved = false; } } catch (JSONException e) {
	 * e.printStackTrace(); }
	 * 
	 * return null; }
	 * 
	 * protected void onPostExecute(String file_url) { pDialog.dismiss(); if
	 * (is_deal_admin_approved) { is_deal_admin_approved = false; new
	 * ActivateDeal().execute();
	 * 
	 * } else { Toast.makeText(getApplicationContext(),
	 * "Admin approval required", Toast.LENGTH_SHORT).show();
	 * is_deal_admin_approved = false; } } }
	 */

	class ActivateDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DealActivity.this);
			pDialog.setMessage("Updating Deal status...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", deal_id));

			JSONObject json = jsonParser.makeHttpRequest(url_activate_deal,
					"POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					is_deal_status_changed = true;
				} else {
					is_deal_status_changed = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_deal_status_changed) {
				Toast.makeText(getApplicationContext(),
						"Deal status activated", Toast.LENGTH_SHORT).show();
				if (deal_adminapproval.equals("0")) {
					Toast.makeText(getApplicationContext(),
							"Admin approval required", Toast.LENGTH_SHORT)
							.show();
				}
				is_deal_status_changed = false;
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "deal_altered");
				edit.commit();
				finish();
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);
			} else {
				Toast.makeText(getApplicationContext(), "No action performed",
						Toast.LENGTH_SHORT).show();
				is_deal_status_changed = false;
			}
		}
	}

	class DeactivateDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DealActivity.this);
			pDialog.setMessage("Updating Deal status...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", deal_id));

			JSONObject json = jsonParser.makeHttpRequest(url_deactivate_deal,
					"POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_deal_status_changed = true;
				} else {
					is_deal_status_changed = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_deal_status_changed) {
				Toast.makeText(getApplicationContext(), "Deal status changed",
						Toast.LENGTH_SHORT).show();
				is_deal_status_changed = false;
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "deal_altered");
				edit.commit();
				finish();
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);

			} else {
				Toast.makeText(getApplicationContext(), "No action performed",
						Toast.LENGTH_SHORT).show();
				is_deal_status_changed = false;
			}
		}
	}

	class DeleteDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DealActivity.this);
			pDialog.setMessage("Deleting deal...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("deal_id", deal_id));

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
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "deal_altered");
				edit.commit();
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

	class GetBusinessDealDetails extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DealActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", deal_id));
			JSONObject json = jsonParser.makeHttpRequest(
					url_get_edit_business_deals, "GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						deal_catid = c.getString(TAG_CATID);
						stringdeal_img = c.getString(TAG_DEALIMG);
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
			String[] date_parts = end_date.split(" ");
			deal_end_date.setText(date_parts[0]);
		}
	}

	protected void SendToEditDealActiivty() {
		String business_name = "", business_id = "";
		if (businessidprefs.contains(strBusinessId)) {
			business_id = businessidprefs.getString(strBusinessId, "0");
		}
		if (businessnameprefs.contains(strBusinessNameSet)) {
			business_name = businessnameprefs
					.getString(strBusinessNameSet, "0");
		}
		Intent intent = new Intent(DealActivity.this, EditDealActivity.class);
		intent.putExtra("deal_id", deal_id);
		intent.putExtra("deal_title", deal_name.getText().toString());
		intent.putExtra("deal_catid", deal_catid);
		intent.putExtra("deal_catname", deal_cat.getText().toString());
		intent.putExtra("deal_desc", deal_desc.getText().toString());
		intent.putExtra("deal_url", stringdeal_url);
		intent.putExtra("deal_image", stringdeal_img);
		intent.putExtra("deal_mainprice", stringmain_price);
		intent.putExtra("deal_dealprice", stringdeal_price);
		String[] date_parts = end_date.split(" ");
		intent.putExtra("end_date", date_parts[0]);
		intent.putExtra("business_id", business_id);
		intent.putExtra("businessname", business_name);

		startActivity(intent);
		overridePendingTransition(R.anim.right_to_left_enter,
				R.anim.right_to_left_exit);
		finish();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		onBackEvent();
	}

	public void onBackEvent() {
		finish();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}