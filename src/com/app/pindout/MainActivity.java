package com.app.pindout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pindout.cachingimages.ImageLoader;
import com.app.pindout.fragments.DealsFragment;
import com.app.pindout.fragments.FragmentDrawer;
import com.app.pindout.fragments.MessagesFragment;
import com.app.pindout.fragments.OthersFragment;
import com.app.pindout.fragments.Reviews_Fragment;
import com.app.pindout.helpers.BusinessDealsAdapter;
import com.app.pindout.helpers.MessagesAdapter;
import com.app.pindout.helpers.ReviewsAdapter;

public class MainActivity extends AppCompatActivity implements
		FragmentDrawer.FragmentDrawerListener {

	private Toolbar mToolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FragmentDrawer drawerFragment;
	private ImageView business_img, edit_business, add_business_info;
	private TextView business_name, business_address, business_phoneno,
			business_website, business_workinghrs;
	private ImageLoader img_loader;
	private int fab_position = 0;
	private ProgressDialog mainpDialog;

	private BusinessDealsAdapter business_dealsadapter;
	private MessagesAdapter messagesadapter;
	private ReviewsAdapter reviewsadapter;

	private boolean is_msg_updated = false, is_message_deleted = false,
			is_reply_succes = false, is_data_loading = false;

	public static final String IsEdit = "IsEditPrefs";
	public static final String strIsEditSet = "0";
	public SharedPreferences iseditprefs;

	public static final String LoginPrefs = "LoginPrefs";
	public static final String strLoginSet = "0";
	public SharedPreferences loginprefs;

	public static final String BusinessIdPrefs = "BusinessIdPrefs";
	public static final String strBusinessId = "0";
	public SharedPreferences businessidprefs;

	public static final String BusinessNamePrefs = "BusinessNamePrefs";
	public static final String strBusinessNameSet = "0";
	public SharedPreferences businessnameprefs;

	public static final String BusinessPhonePrefs = "BusinessPhonePrefs";
	public static final String strBusinessPhoneSet = "0";
	public SharedPreferences businessphoneprefs;

	public static final String BusinessAddressPrefs = "BusinessAddressPrefs";
	public static final String strBusinessAddressSet = "0";
	public SharedPreferences businessaddressprefs;

	public static final String BusinessAreaPrefs = "BusinessAreaPrefs";
	public static final String strBusinessAreaSet = "0";
	public SharedPreferences businessareaprefs;

	public static final String BusinessLogoPrefs = "BusinessLogoPrefs";
	public static final String strBusinessLogoSet = "0";
	public SharedPreferences businesslogoprefs;

	public static final String BusinessDescPrefs = "BusinessDescPrefs";
	public static final String strBusinessDescSet = "0";
	public SharedPreferences businessdescprefs;

	public static final String BusinessProd_n_ServicePrefs = "BusinessProd_n_ServicePrefs";
	public static final String strBusinessProd_n_ServiceSet = "0";
	public SharedPreferences businessprodnserviceprefs;

	public static final String BusinessUrlPrefs = "BusinessUrlPrefs";
	public static final String strBusinessUrlSet = "0";
	public SharedPreferences businessurlprefs;

	public static final String BusinessAddInfoPrefs = "BusinessAddInfoPrefs";
	public static final String strBusinessAddInfoSet = "0";
	public SharedPreferences businessaddinfoprefs;

	public static final String BusinessWorkingHrsPrefs = "BusinessWorkingHrsPrefs";
	public static final String strBusinessWorkingHrsSet = "0";
	public SharedPreferences businessworkinghrsprefs;

	private String businessname = "not_set", businessphone = "not_set",
			businessaddress = "not_set", business_logo = "",
			business_id = "not set", area_name = "not_set",
			business_desc = "not_set", business_prodnservice = "not_set",
			business_url = "not_set", business_addtext = "not_set",
			business_working_hrs = "not_set";

	// private String firstname = "not set", lastname = "not set";

	private String business_logo_path = "http://www.pindout.com/files/business_images/main_images/";
	private String isEdited = "not_set";

	ArrayList<HashMap<String, String>> businessdeal_list;
	ArrayList<HashMap<String, String>> inbusinessdeal_list;
	ArrayList<HashMap<String, String>> messages_list;
	ArrayList<HashMap<String, String>> reviews_list;

	JSONParser jsonParser = new JSONParser();
	JSONArray products = null;

	private static final String TAG_SUCCESS = "success";
	// private static final String TAG_PRODUCT = "product";
	private static final String TAG_PRODUCTS = "products";

	public static final String TAG_DEAL_ID = "row_id";
	public static final String TAG_DEALNAME = "dealname";
	public static final String TAG_DEALDESC = "deal_desc";
	public static final String TAG_DEALCATID = "catid";
	public static final String TAG_CATNAME = "catname";
	public static final String TAG_ADMINAPPROVAL = "adminapproval";
	public static final String TAG_STATUS = "status";
	public static final String TAG_MAINPRICE = "main_price";
	public static final String TAG_DEALPRICE = "deal_price";
	public static final String TAG_DEALIMG = "deal_image";
	public static final String TAG_DEALURL = "url_link";
	public static final String TAG_ROWID = "row_id";
	public static final String TAG_DATE = "date";
	public static final String TAG_MSG = "message";
	public static final String TAG_REVIEWID = "id";
	public static final String TAG_GROUPID = "group_id";
	public static final String TAG_USERID = "user_id";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_REVIEWTEXT = "review_text";
	public static final String TAG_REVIEWADMINAPPROVAL = "admin_approval";
	public static final String TAG_REVIEWSTATUS = "status";
	public static final String TAG_REVIEWDATETIME = "review_datetime";
	public static final String TAG_USERNAME = "firstname";
	public static final String TAG_BUSINESS_REVIEW_ID = "business_review_id";
	public static final String TAG_COMMENT_TEXT = "comment_text";
	public static final String TAG_REPLY_DATE_CREATED = "date_created";

	public static final String TAG_GET_REPLY_DATA = "reply_data";

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
	private ArrayList<String> deal_url_link_list = new ArrayList<String>();
	private ArrayList<String> review_id_list = new ArrayList<String>();
	private ArrayList<String> group_id_list = new ArrayList<String>();
	private ArrayList<String> user_id_list = new ArrayList<String>();
	private ArrayList<String> email_list = new ArrayList<String>();
	private ArrayList<String> review_text_list = new ArrayList<String>();
	private ArrayList<String> review_admin_list = new ArrayList<String>();
	private ArrayList<String> review_status_list = new ArrayList<String>();
	private ArrayList<String> review_datetime_list = new ArrayList<String>();
	private ArrayList<String> user_name_list = new ArrayList<String>();
	private ArrayList<String> reply_business_review_id_list = new ArrayList<String>();
	private ArrayList<String> reply_userid_list = new ArrayList<String>();
	private ArrayList<String> reply_comment_text_list = new ArrayList<String>();
	private ArrayList<String> reply_date_created_list = new ArrayList<String>();
	private ArrayList<String> reply_user_names = new ArrayList<String>();

	private ArrayList<String> combine_reply_data_list = new ArrayList<String>();

	// private static final String url_user_detials =
	// "http://www.pindout.com/mobi/pindout/get_user_detail.php";
	private static final String url_get_business_deals = "http://www.pindout.com/mobi/pindout/get_business_deals.php";
	private static final String url_get_businesscat_name = "http://www.pindout.com/mobi/pindout/get_businesscat_name.php";
	private static final String url_get_greeting_msg = "http://www.pindout.com/mobi/pindout/get_greeting_msg.php";
	private static final String url_update_greetingmsg = "http://www.pindout.com/mobi/pindout/update_greetingmsg.php";
	private static final String url_delete_greetingmsg = "http://www.pindout.com/mobi/pindout/delete_greetingmsg.php";
	private static final String url_get_reviews = "http://www.pindout.com/mobi/pindout/get_reviews.php";
	private static final String url_get_reviews_users = "http://www.pindout.com/mobi/pindout/get_reviews_users.php";
	private static final String url_get_reviews_reply = "http://www.pindout.com/mobi/pindout/get_reviews_reply.php";
	private static final String url_get_reviews_reply_usernames = "http://www.pindout.com/mobi/pindout/get_reviews_reply_usernames.php";
	private static final String url_upload_review_reply = "http://www.pindout.com/mobi/pindout/insert_review_reply.php";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);

		img_loader = new ImageLoader(this);

		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		business_img = (ImageView) findViewById(R.id.business_logo);
		add_business_info = (ImageView) findViewById(R.id.add_business_info);
		edit_business = (ImageView) findViewById(R.id.edit_business);
		business_name = (TextView) findViewById(R.id.business_name);
		business_address = (TextView) findViewById(R.id.address);
		business_phoneno = (TextView) findViewById(R.id.phoneno);
		business_website = (TextView) findViewById(R.id.website_url);
		business_workinghrs = (TextView) findViewById(R.id.working_hours);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		iseditprefs = getSharedPreferences(IsEdit, Context.MODE_PRIVATE);
		loginprefs = getSharedPreferences(LoginPrefs, Context.MODE_PRIVATE);
		businessidprefs = getSharedPreferences(BusinessIdPrefs,
				Context.MODE_PRIVATE);
		businessnameprefs = getSharedPreferences(BusinessNamePrefs,
				Context.MODE_PRIVATE);
		businessphoneprefs = getSharedPreferences(BusinessPhonePrefs,
				Context.MODE_PRIVATE);
		businessaddressprefs = getSharedPreferences(BusinessAddressPrefs,
				Context.MODE_PRIVATE);
		businessareaprefs = getSharedPreferences(BusinessAreaPrefs,
				Context.MODE_PRIVATE);
		businesslogoprefs = getSharedPreferences(BusinessLogoPrefs,
				Context.MODE_PRIVATE);
		businessdescprefs = getSharedPreferences(BusinessDescPrefs,
				Context.MODE_PRIVATE);
		businessprodnserviceprefs = getSharedPreferences(
				BusinessProd_n_ServicePrefs, Context.MODE_PRIVATE);
		businessaddinfoprefs = getSharedPreferences(BusinessAddInfoPrefs,
				Context.MODE_PRIVATE);
		businessurlprefs = getSharedPreferences(BusinessUrlPrefs,
				Context.MODE_PRIVATE);
		businessworkinghrsprefs = getSharedPreferences(BusinessWorkingHrsPrefs,
				Context.MODE_PRIVATE);

		drawerFragment = (FragmentDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		mToolbar.setTitle(businessname);
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		final Handler handler = new Handler();

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						super.onPageSelected(position);
						if (position == 0) {
							fab_position = position;
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									DisplayDeals();
								}
							}, 50);
						}
						if (position == 1) {
							fab_position = position;
							displayMessage();
						}
						if (position == 2) {
							DisplayReviews();
						}
					}
				});
		edit_business.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent3 = new Intent(MainActivity.this,
						EditBusinessActivity.class);
				intent3.putExtra("business_id", business_id);
				startActivity(intent3);
			}
		});
		add_business_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent4 = new Intent(MainActivity.this,
						Additional_Info_Activity.class);
				intent4.putExtra("business_id", business_id);
				startActivity(intent4);
			}
		});
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(
				getSupportFragmentManager());

		adapter.addFragment(new DealsFragment(), "Deals");
		adapter.addFragment(new MessagesFragment(), "Messages");
		adapter.addFragment(new Reviews_Fragment(), "Reviews");
		adapter.addFragment(new OthersFragment(), "Others");
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

	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}

	private void displayView(int position) {
		switch (position) {
		case 0:
			break;
		case 1:
			Editor edit = loginprefs.edit();
			edit.putString(strLoginSet, "0");
			edit.commit();
			Intent i = new Intent(MainActivity.this, MainActivityRemo.class);
			startActivity(i);
			finish();
		default:

			break;
		}
	}

	class LoadBusinessDeals extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mainpDialog = new ProgressDialog(MainActivity.this);
			mainpDialog.setMessage("Please wait...");
			mainpDialog.setIndeterminate(false);
			mainpDialog.setCancelable(false);
			mainpDialog.show();
		}

		protected String doInBackground(String... args) {

			dealname.clear();
			row_id.clear();
			dealcatid.clear();
			deal_desc.clear();
			main_price_list.clear();
			deal_price_list.clear();
			deal_image_list.clear();
			status.clear();
			admin_approval.clear();
			deal_url_link_list.clear();

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
						row_id.add(c.getString(TAG_DEAL_ID));
						dealname.add(c.getString(TAG_DEALNAME));
						dealcatid.add(c.getString(TAG_DEALCATID));
						deal_url_link_list.add(c.getString(TAG_DEALURL));
						admin_approval.add(c.getString(TAG_ADMINAPPROVAL));
						status.add(c.getString(TAG_STATUS));
						deal_desc.add(c.getString(TAG_DEALDESC));
						main_price_list.add(c.getString(TAG_MAINPRICE));
						deal_price_list.add(c.getString(TAG_DEALPRICE));
						deal_image_list
								.add("http://www.pindout.com/files/businessdeal_images/main_images/"
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
			new LoadDealCategory().execute();
		}
	}

	class LoadDealCategory extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			catname.clear();

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

			return null;
		}

		protected void onPostExecute(String file_url) {
			new InsertIntoDeals().execute();
		}
	}

	class InsertIntoDeals extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			businessdeal_list = new ArrayList<HashMap<String, String>>();
			businessdeal_list.clear();

			for (int i = 0; i < row_id.size(); i++) {
				if (status.get(i).equals("1")
						&& admin_approval.get(i).equals("1")) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("row_id", row_id.get(i));
					map.put("dealname", dealname.get(i));
					map.put("catname", catname.get(i));
					map.put("main_price", main_price_list.get(i));
					map.put("deal_price", deal_price_list.get(i));
					map.put("url_link", deal_url_link_list.get(i));
					map.put("status", status.get(i));
					map.put("adminapproval", admin_approval.get(i));
					map.put("deal_desc", deal_desc.get(i));
					map.put("deal_image", deal_image_list.get(i));
					businessdeal_list.add(map);
				}
			}

			for (int i = 0; i < row_id.size(); i++) {
				if (status.get(i).equals("0")
						|| admin_approval.get(i).equals("0")) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("row_id", row_id.get(i));
					map.put("dealname", dealname.get(i));
					map.put("catname", catname.get(i));
					map.put("main_price", main_price_list.get(i));
					map.put("deal_price", deal_price_list.get(i));
					map.put("url_link", deal_url_link_list.get(i));
					map.put("status", status.get(i));
					map.put("adminapproval", admin_approval.get(i));
					map.put("deal_desc", deal_desc.get(i));
					map.put("deal_image", deal_image_list.get(i));
					businessdeal_list.add(map);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (isEdited.equals("0")) {
				new LoadGreetingMessages().execute();
			} else {
				if (mainpDialog != null) {
					Editor edit = iseditprefs.edit();
					edit.putString(strIsEditSet, "data_loaded_completely");
					edit.commit();
					if (iseditprefs.contains(strIsEditSet)) {
						isEdited = iseditprefs.getString(strIsEditSet, "0");
					}
					mainpDialog.dismiss();
					DisplayDeals();
				}
			}
		}
	}

	class LoadGreetingMessages extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			messages_list = new ArrayList<HashMap<String, String>>();
			messages_list.clear();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));

			JSONObject json = jsonParser.makeHttpRequest(url_get_greeting_msg,
					"GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("row_id", c.getString(TAG_ROWID));
						map.put("message", c.getString(TAG_MSG));
						map.put("date", c.getString(TAG_DATE));
						messages_list.add(map);
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (isEdited.equals("0")) {
				new LoadBusinessReviews().execute();
			} else {
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "data_loaded_completely");
				edit.commit();
				if (iseditprefs.contains(strIsEditSet)) {
					isEdited = iseditprefs.getString(strIsEditSet, "0");
					mainpDialog.dismiss();
					DisplayDeals();
				}
			}
		}
	}

	class LoadBusinessReviews extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {

			review_id_list.clear();
			group_id_list.clear();
			user_id_list.clear();
			email_list.clear();
			review_text_list.clear();
			review_admin_list.clear();
			review_status_list.clear();
			review_datetime_list.clear();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));

			JSONObject json = jsonParser.makeHttpRequest(url_get_reviews,
					"GET", params);

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						review_id_list.add(c.getString(TAG_REVIEWID));
						group_id_list.add(c.getString(TAG_GROUPID));
						user_id_list.add(c.getString(TAG_USERID));
						email_list.add(c.getString(TAG_EMAIL));
						review_text_list.add(c.getString(TAG_REVIEWTEXT));
						review_admin_list.add(c
								.getString(TAG_REVIEWADMINAPPROVAL));
						review_status_list.add(c.getString(TAG_REVIEWSTATUS));
						review_datetime_list.add(c
								.getString(TAG_REVIEWDATETIME));

					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			new LoadReviewsUsers().execute();
		}
	}

	class LoadReviewsUsers extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			user_name_list.clear();

			for (int j = 0; j < user_id_list.size(); j++) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("user_id", user_id_list
						.get(j)));

				JSONObject json = jsonParser.makeHttpRequest(
						url_get_reviews_users, "GET", params);

				try {

					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						products = json.getJSONArray(TAG_PRODUCTS);

						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							user_name_list.add(c.getString(TAG_USERNAME));
						}
					} else {

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// new InsertIntoReviews().execute();
			new LoadReviewComments().execute();
		}
	}

	class LoadReviewComments extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			reply_business_review_id_list.clear();
			reply_comment_text_list.clear();
			reply_userid_list.clear();
			reply_date_created_list.clear();

			for (int j = 0; j < review_id_list.size(); j++) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("business_review_id",
						review_id_list.get(j)));

				JSONObject json = jsonParser.makeHttpRequest(
						url_get_reviews_reply, "GET", params);

				try {

					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						products = json.getJSONArray(TAG_PRODUCTS);

						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							reply_business_review_id_list.add(c
									.getString(TAG_BUSINESS_REVIEW_ID));
							reply_userid_list.add(c.getString(TAG_USERID));
							reply_comment_text_list.add(c
									.getString(TAG_COMMENT_TEXT));
							reply_date_created_list.add(c
									.getString(TAG_REPLY_DATE_CREATED));
						}
					} else {

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			new LoadReviewReplyUserNames().execute();
		}
	}

	class LoadReviewReplyUserNames extends AsyncTask<String, String, String> {
		int count_first_reply_inserted = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			reply_user_names.clear();
			combine_reply_data_list.clear();

			for (int j = 0; j < reply_userid_list.size(); j++) {
				if (reply_userid_list.get(j).equals("0")) {
					reply_user_names.add(businessname);
				} else {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("user_id",
							reply_userid_list.get(j)));

					JSONObject json = jsonParser.makeHttpRequest(
							url_get_reviews_reply_usernames, "GET", params);

					try {

						int success = json.getInt(TAG_SUCCESS);

						if (success == 1) {
							products = json.getJSONArray(TAG_PRODUCTS);
							for (int i = 0; i < products.length(); i++) {
								JSONObject c = products.getJSONObject(i);
								reply_user_names.add(c.getString(TAG_USERNAME));
							}
						} else {

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			for (int i = 0; i < review_id_list.size(); i++) {
				count_first_reply_inserted = 0;
				String combined_reply_date_str = getString(R.string.users_no_comments);
				for (int k = 0; k < reply_business_review_id_list.size(); k++) {
					if (review_id_list.get(i).equals(
							reply_business_review_id_list.get(k))) {
						if (count_first_reply_inserted == 0) {
							combined_reply_date_str = reply_user_names.get(k)
									+ ":  " + reply_comment_text_list.get(k)
									+ "\n" + reply_date_created_list.get(k);
						} else if (count_first_reply_inserted > 0) {
							combined_reply_date_str = combined_reply_date_str
									+ "\n\n" + reply_user_names.get(k) + ":  "
									+ reply_comment_text_list.get(k) + "\n"
									+ reply_date_created_list.get(k);
						}
						count_first_reply_inserted++;
					}
				}
				combine_reply_data_list.add(combined_reply_date_str);
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			new InsertIntoReviews().execute();
		}
	}

	class InsertIntoReviews extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			reviews_list = new ArrayList<HashMap<String, String>>();
			reviews_list.clear();
			for (int i = 0; i < review_id_list.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("firstname", user_name_list.get(i));
				map.put("review_text", review_text_list.get(i));
				map.put("review_datetime", review_datetime_list.get(i));
				map.put("id", review_id_list.get(i));
				map.put("user_id", user_id_list.get(i));
				map.put("group_id", group_id_list.get(i));
				map.put("reply_data", combine_reply_data_list.get(i));
				reviews_list.add(map);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mainpDialog != null) {
				mainpDialog.dismiss();
			}
			DisplayDeals();
			Editor edit = iseditprefs.edit();
			edit.putString(strIsEditSet, "data_loaded_completely");
			edit.commit();
		}
	}

	private void DisplayDeals() {
		// TODO Auto-generated method stub
		ListView listView = (ListView) findViewById(R.id.business_dealslist);
		business_dealsadapter = new BusinessDealsAdapter(MainActivity.this,
				businessdeal_list);
		listView.setAdapter(business_dealsadapter);
		is_data_loading = false;
	}

	private void displayMessage() {
		ListView listView = (ListView) findViewById(R.id.messages_list);
		messagesadapter = new MessagesAdapter(MainActivity.this, messages_list);
		listView.setAdapter(messagesadapter);
	}

	private void DisplayReviews() {
		ListView reviewlistview = (ListView) findViewById(R.id.review_list);
		reviewsadapter = new ReviewsAdapter(MainActivity.this, reviews_list);
		reviewlistview.setAdapter(reviewsadapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_main_refresh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_info) {
			Refresh();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void Refresh() {
		// TODO Auto-generated method stub
		Editor edit = iseditprefs.edit();
		edit.putString(strIsEditSet, "0");
		edit.commit();
		if (iseditprefs.contains(strIsEditSet)) {
			isEdited = iseditprefs.getString(strIsEditSet, "0");
		}
		Toast.makeText(getApplicationContext(), "Refreshing...",
				Toast.LENGTH_SHORT).show();
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);
		new LoadBusinessDeals().execute();
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { int id =
	 * item.getItemId();
	 * 
	 * if (id == R.id.action_info) { Toast.makeText(getApplicationContext(),
	 * "Manage Business details", Toast.LENGTH_SHORT).show(); return true; }
	 * 
	 * return super.onOptionsItemSelected(item); }
	 */

	/*
	 * class GetUserDetails extends AsyncTask<String, String, String> { private
	 * ProgressDialog mainpDialog;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * mainpDialog = new ProgressDialog(MainActivity.this);
	 * mainpDialog.setMessage("Please wait...");
	 * mainpDialog.setIndeterminate(false); mainpDialog.setCancelable(true);
	 * mainpDialog.show(); }
	 * 
	 * protected String doInBackground(String... params) {
	 * 
	 * runOnUiThread(new Runnable() { public void run() {
	 * 
	 * int success; try {
	 * 
	 * List<NameValuePair> params = new ArrayList<NameValuePair>();
	 * params.add(new BasicNameValuePair("id", business_id));
	 * 
	 * JSONObject json = jsonParser.makeHttpRequest( url_user_detials, "GET",
	 * params);
	 * 
	 * Log.d("Single Product Details", json.toString());
	 * 
	 * success = json.getInt(TAG_SUCCESS); if (success == 1) {
	 * 
	 * JSONArray productObj = json .getJSONArray(TAG_PRODUCT);
	 * 
	 * JSONObject product = productObj.getJSONObject(0); } else {
	 * 
	 * } } catch (JSONException e) { e.printStackTrace(); } } });
	 * 
	 * return null; }
	 * 
	 * protected void onPostExecute(String file_url) { // dismiss the dialog
	 * once got all details mainpDialog.dismiss(); setupViewPager(viewPager);
	 * Toast.makeText( getApplicationContext(), businessname + "\n" +
	 * businessaddress + "\n" + businessphone + "\n" + business_id + "\n" +
	 * firstname + "\n" + lastname, Toast.LENGTH_SHORT) .show();
	 * mToolbar.setTitle(businessname); } }
	 */

	class SaveMsgDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Updating message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String msg_id = args[0];
			String msg = args[1];
			String msg_date = args[2];

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", msg_id));
			params.add(new BasicNameValuePair("msg", msg));
			params.add(new BasicNameValuePair("date", msg_date));

			JSONObject json = jsonParser.makeHttpRequest(
					url_update_greetingmsg, "POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_msg_updated = true;
				} else {
					is_msg_updated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_msg_updated) {
				Toast.makeText(getApplicationContext(), "Message updated",
						Toast.LENGTH_SHORT).show();
				is_msg_updated = false;
				setupViewPager(viewPager);
				tabLayout.setupWithViewPager(viewPager);

				mainpDialog = new ProgressDialog(MainActivity.this);
				mainpDialog.setMessage("Please wait...");
				mainpDialog.setIndeterminate(false);
				mainpDialog.setCancelable(false);
				mainpDialog.show();

				new LoadGreetingMessages().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Message not updated",
						Toast.LENGTH_SHORT).show();
				is_msg_updated = false;
			}
		}
	}

	class DeleteMessage extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Deleting message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("business_id", args[0]));

				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_greetingmsg, "POST", params);

				Log.d("Delete Product", json.toString());

				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					is_message_deleted = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_message_deleted) {
				Toast.makeText(getApplicationContext(), "Message deleted",
						Toast.LENGTH_SHORT).show();
				is_message_deleted = false;
				setupViewPager(viewPager);
				tabLayout.setupWithViewPager(viewPager);

				mainpDialog = new ProgressDialog(MainActivity.this);
				mainpDialog.setMessage("Please wait...");
				mainpDialog.setIndeterminate(false);
				mainpDialog.setCancelable(false);
				mainpDialog.show();

				new LoadGreetingMessages().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Message not deleted",
						Toast.LENGTH_SHORT).show();
				is_message_deleted = false;
			}
		}
	}

	class UploadReviewReply extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mainpDialog = new ProgressDialog(MainActivity.this);
			mainpDialog.setMessage("Please wait...");
			mainpDialog.setIndeterminate(false);
			mainpDialog.setCancelable(false);
			mainpDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_review_id", args[0]));
			params.add(new BasicNameValuePair("user_id", args[1]));
			params.add(new BasicNameValuePair("group_id", args[2]));
			params.add(new BasicNameValuePair("comment_text", args[3]));
			params.add(new BasicNameValuePair("date_created", args[4]));

			JSONObject json = jsonParser.makeHttpRequest(
					url_upload_review_reply, "POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_reply_succes = true;
				} else {
					is_reply_succes = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			if (mainpDialog != null) {
				mainpDialog.dismiss();
			}
			if (is_reply_succes) {
				Toast.makeText(getApplicationContext(), "Reply sent",
						Toast.LENGTH_SHORT).show();
				setupViewPager(viewPager);
				tabLayout.setupWithViewPager(viewPager);

				mainpDialog = new ProgressDialog(MainActivity.this);
				mainpDialog.setMessage("Please wait...");
				mainpDialog.setIndeterminate(false);
				mainpDialog.setCancelable(false);
				mainpDialog.show();

				new LoadBusinessReviews().execute();
			}
			if (!is_reply_succes) {
				is_reply_succes = false;
				Toast.makeText(getApplicationContext(), "Reply not sent",
						Toast.LENGTH_SHORT).show();
			}
			is_reply_succes = false;
		}
	}

	public void GetMessageDetails(String msg_id, String msg, String msg_date) {
		new SaveMsgDetails().execute(msg_id, msg, msg_date);
	}

	public void GetMessageId(String msg_id) {
		new DeleteMessage().execute(msg_id);
	}

	public void GetReviewReplyDetails(String business_review_id,
			String user_id, String group_id, String comment_text,
			String currentDateString) {
		new UploadReviewReply().execute(business_review_id, user_id, group_id,
				comment_text, currentDateString);
	}

	public String sendBusinessName() {
		return businessname;
	}

	public String sendBusinessPhone() {
		return businessphone;
	}

	public String sendBusinessAddress() {
		return businessaddress;
	}

	public String sendBusinessAreaName() {
		return area_name;
	}

	public String sendBusinessLogo() {
		return business_logo;
	}

	public String sendBusinessAddInfo() {
		return business_addtext;
	}

	public String sendBusinessDesc() {
		return business_desc;
	}

	public String sendBusinessUrl() {
		return business_url;
	}

	public String sendBusinessProd_N_Service() {
		return business_prodnservice;
	}

	public String sendBusinessId() {
		return business_id;
	}

	public int sendFabPosition() {
		return fab_position;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onResume();
		if (businessidprefs.contains(strBusinessId)) {
			business_id = businessidprefs.getString(strBusinessId, "0");
		}
		if (businessnameprefs.contains(strBusinessNameSet)) {
			businessname = businessnameprefs.getString(strBusinessNameSet, "0");
		}
		if (businessphoneprefs.contains(strBusinessPhoneSet)) {
			businessphone = businessphoneprefs.getString(strBusinessPhoneSet,
					"0");
		}
		if (businessaddressprefs.contains(strBusinessAddressSet)) {
			businessaddress = businessaddressprefs.getString(
					strBusinessAddressSet, "0");
		}
		if (businessareaprefs.contains(strBusinessAreaSet)) {
			area_name = businessareaprefs.getString(strBusinessAreaSet, "0");
		}
		if (businesslogoprefs.contains(strBusinessLogoSet)) {
			business_logo = businesslogoprefs
					.getString(strBusinessLogoSet, "0");
		}
		if (businessdescprefs.contains(strBusinessDescSet)) {
			business_desc = businessdescprefs
					.getString(strBusinessDescSet, "0");
		}
		if (businessaddinfoprefs.contains(strBusinessAddInfoSet)) {
			business_addtext = businessaddinfoprefs.getString(
					strBusinessAddInfoSet, "0");
		}
		if (businessprodnserviceprefs.contains(strBusinessProd_n_ServiceSet)) {
			business_prodnservice = businessprodnserviceprefs.getString(
					strBusinessProd_n_ServiceSet, "0");
		}
		if (businessurlprefs.contains(strBusinessUrlSet)) {
			business_url = businessurlprefs.getString(strBusinessUrlSet, "0");
		}
		if (businessworkinghrsprefs.contains(strBusinessWorkingHrsSet)) {
			business_working_hrs = businessworkinghrsprefs.getString(
					strBusinessWorkingHrsSet, "0");
		}
		if (iseditprefs.contains(strIsEditSet)) {
			isEdited = iseditprefs.getString(strIsEditSet, "0");
		}
		// String str = "<a href='http://www.google.com'>Google</a>";
		business_website.setClickable(true);
		business_website.setMovementMethod(LinkMovementMethod.getInstance());
		String business_string = "<a href='" + business_url
				+ "'>Business Url</a>";
		img_loader.DisplayImage(business_logo_path + business_logo,
				business_img);
		business_name.setText(businessname);
		business_address.setText(businessaddress);
		business_phoneno.setText(businessphone);
		business_website.setText(Html.fromHtml(business_string));
		business_workinghrs.setText(business_working_hrs);
		if (!is_data_loading) {
			setupViewPager(viewPager);
			tabLayout.setupWithViewPager(viewPager);
			if (isEdited.equals("deal_altered") || isEdited.equals("0")) {
				is_data_loading = true;
				new LoadBusinessDeals().execute();
			}
			if (isEdited.equals("msg_altered")) {
				mainpDialog = new ProgressDialog(MainActivity.this);
				mainpDialog.setMessage("Please wait...");
				mainpDialog.setIndeterminate(false);
				mainpDialog.setCancelable(false);
				mainpDialog.show();
				new LoadGreetingMessages().execute();
			}
			if (isEdited.equals("data_loaded_completely")) {
				DisplayDeals();
			}
		}
	}
}