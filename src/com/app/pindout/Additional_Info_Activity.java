package com.app.pindout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uploading.Config;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.helpers.RoundedImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Additional_Info_Activity extends AppCompatActivity {

	private boolean add_view_activated = false, is_data_updated = false,
			is_image_selected = false, image_uploaded_success = false;
	JSONParser jParser = new JSONParser();
	private ProgressDialog endpDialog;

	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static int RESULT_LOAD_IMG = 1;
	private Uri fileUri;

	private RequestParams params = new RequestParams();
	private String imgPath, fileName;
	private String business_id = "";
	private String encodedString;
	private Bitmap bitmap;
	private String zip_code = "not_set", owner_ph = "", business_url = "",
			working_hrs = "";
	private String about_business, greeting_text, product_n_services,
			additional_info;
	private String password;
	private Intent intent;

	private static String capture_img_time = "";

	private Toolbar toolBar;

	public static final String BusinessLogoPrefs = "BusinessLogoPrefs";
	public static final String strBusinessLogoSet = "0";
	public SharedPreferences businesslogoprefs;

	public static final String BusinessUrlPrefs = "BusinessUrlPrefs";
	public static final String strBusinessUrlSet = "0";
	public SharedPreferences businessurlprefs;

	public static final String BusinessWorkingHrsPrefs = "BusinessWorkingHrsPrefs";
	public static final String strBusinessWorkingHrsSet = "0";
	public SharedPreferences businessworkinghrsprefs;

	private EditText input_owner_phone, input_zipcode, input_business_url,
			input_working_hrs;
	private EditText input_about_business, input_greeting_text,
			input_additional_text, input_product_services;
	private EditText input_pass, input_confirm_pass;

	private TextInputLayout inputLayoutConfirmPassword;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_ZIPCODE = "zip_code";
	private static final String TAG_OWNERPH = "owner_phone";
	private static final String TAG_BUSINESSURL = "business_url";
	private static final String TAG_WORKING_HOURS = "working_hrs";
	private static final String TAG_ABOUT_BUSINESS = "description";
	private static final String TAG_GREETING_TEXT = "greeting_text";
	private static final String TAG_PRODUCT_SERVICE = "product_service";
	private static final String TAG_ADDITIONAL_TEXT = "additional_text";
	private static final String TAG_BUSINESSLOGO = "business_image";
	private static final String TAG_PASS = "password";

	private static String url_get_info_for_first = "http://www.pindout.com/mobi/pindout/get_info_for_first.php";
	private static String url_get_info_for_second = "http://www.pindout.com/mobi/pindout/get_info_for_second.php";
	private static String url_get_info_for_third = "http://www.pindout.com/mobi/pindout/get_info_for_third.php";

	private static String url_update_info_for_first = "http://www.pindout.com/mobi/pindout/update_info_for_first.php";
	private static String url_update_info_for_second = "http://www.pindout.com/mobi/pindout/update_info_for_second.php";
	private static String url_update_info_for_third = "http://www.pindout.com/mobi/pindout/update_info_for_third.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additional_info_activity);
		businesslogoprefs = getSharedPreferences(BusinessLogoPrefs,
				Context.MODE_PRIVATE);
		businessurlprefs = getSharedPreferences(BusinessUrlPrefs,
				Context.MODE_PRIVATE);
		businessworkinghrsprefs = getSharedPreferences(BusinessWorkingHrsPrefs,
				Context.MODE_PRIVATE);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		toolBar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
		intent = getIntent();
		business_id = intent.getStringExtra("business_id");
	}

	public void OpenInfoFirst(View v) {
		add_view_activated = true;
		setContentView(R.layout.add_info_first);
		new GetInfoFirst().execute();
	}

	public void OpenInfoSecond(View v) {
		add_view_activated = true;
		setContentView(R.layout.add_info_second);
		new GetInfoSecond().execute();
	}

	public void OpenInfoThird(View v) {
		add_view_activated = true;
		setContentView(R.layout.add_info_third);
		new GetInfoThird().execute();
	}

	public void OpenInfoFourth(View v) {
		Intent intent = new Intent(Additional_Info_Activity.this,
				AddBusinessImageTagActivity.class);
		intent.putExtra("business_id", business_id);
		startActivity(intent);
		overridePendingTransition(R.anim.right_to_left_enter,
				R.anim.right_to_left_exit);
	}

	public void AddBusinessLogo(View v) {
		UploadImageDialog();
	}

	public void CloseInfoFirst(View v) {
		setContentView(R.layout.additional_info_activity);
		overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_in);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolBar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
	}

	public void Cancel(View v) {
		setContentView(R.layout.additional_info_activity);
		overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_in);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolBar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
	}

	public void Submit_First(View v) {
		new UpdateInfoFirst().execute();
	}

	public void Submit_Second(View v) {
		new UpdateInfoSecond().execute();
	}

	public void Submit_Third(View v) {
		inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
		input_pass.addTextChangedListener(new MyTextWatcher(input_pass));
		input_confirm_pass.addTextChangedListener(new MyTextWatcher(
				input_confirm_pass));
		if (!validateConfirmPassword()) {
			return;
		}
		new UpdateInfoThird().execute();
	}

	private boolean validateConfirmPassword() {
		if (input_confirm_pass.getText().toString().trim().isEmpty()) {
			inputLayoutConfirmPassword
					.setError(getString(R.string.err_msg_enterconfirmpass));
			requestFocus(input_confirm_pass);
			return false;
		} else {
			String pass = input_pass.getText().toString();
			String confirmpass = input_confirm_pass.getText().toString();
			if (pass.equals(confirmpass)) {
				inputLayoutConfirmPassword.setErrorEnabled(false);
			} else {
				inputLayoutConfirmPassword
						.setError(getString(R.string.err_msg_passdonotmatch));
				requestFocus(input_confirm_pass);
				return false;
			}
		}
		return true;
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	class GetInfoFirst extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Additional_Info_Activity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("business_id",
								business_id));
						JSONObject json = jParser.makeHttpRequest(
								url_get_info_for_first, "GET", params);
						Log.d("Single User Details", json.toString());
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);
							JSONObject product = productObj.getJSONObject(0);
							owner_ph = product.getString(TAG_OWNERPH);
							zip_code = product.getString(TAG_ZIPCODE);
							fileName = product.getString(TAG_BUSINESSLOGO);
							business_url = product.getString(TAG_BUSINESSURL);
							working_hrs = product.getString(TAG_WORKING_HOURS);
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
			pDialog.dismiss();
			input_owner_phone = (EditText) findViewById(R.id.input_owner_phone);
			input_zipcode = (EditText) findViewById(R.id.input_pincode);
			input_business_url = (EditText) findViewById(R.id.input_businessurl);
			input_working_hrs = (EditText) findViewById(R.id.input_workinghrs);
			Button btn_business_logo = (Button) findViewById(R.id.addbusinesslogo);
			input_owner_phone.setText(owner_ph);
			input_zipcode.setText(zip_code);
			input_business_url.setText(business_url);
			input_working_hrs.setText(working_hrs);
			btn_business_logo.setText(fileName);
		}
	}

	class GetInfoSecond extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Additional_Info_Activity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("business_id",
								business_id));
						JSONObject json = jParser.makeHttpRequest(
								url_get_info_for_second, "GET", params);
						Log.d("Single User Details", json.toString());
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);
							JSONObject product = productObj.getJSONObject(0);
							about_business = product
									.getString(TAG_ABOUT_BUSINESS);
							greeting_text = product
									.getString(TAG_GREETING_TEXT);
							product_n_services = product
									.getString(TAG_PRODUCT_SERVICE);
							additional_info = product
									.getString(TAG_ADDITIONAL_TEXT);
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
			pDialog.dismiss();
			input_about_business = (EditText) findViewById(R.id.input_about_business);
			input_greeting_text = (EditText) findViewById(R.id.input_greetingtext);
			input_product_services = (EditText) findViewById(R.id.input_product_n_services);
			input_additional_text = (EditText) findViewById(R.id.input_additional_info);
			input_about_business.setText(about_business);
			input_greeting_text.setText(greeting_text);
			input_product_services.setText(product_n_services);
			input_additional_text.setText(additional_info);
		}
	}

	class GetInfoThird extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Additional_Info_Activity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params) {

			runOnUiThread(new Runnable() {
				public void run() {

					int success;
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("business_id",
								business_id));
						JSONObject json = jParser.makeHttpRequest(
								url_get_info_for_third, "GET", params);
						Log.d("Single User Details", json.toString());
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);
							JSONObject product = productObj.getJSONObject(0);
							password = product.getString(TAG_PASS);
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
			pDialog.dismiss();
			input_pass = (EditText) findViewById(R.id.input_password);
			input_confirm_pass = (EditText) findViewById(R.id.input_confirm_password);
			input_pass.setText(password);
			input_confirm_pass.setText(password);
		}
	}

	class UpdateInfoFirst extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Additional_Info_Activity.this);
			pDialog.setMessage("Updating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("owner_phone", input_owner_phone
					.getText().toString()));
			params.add(new BasicNameValuePair("business_image", business_id
					+ "_" + fileName));
			params.add(new BasicNameValuePair("zip_code", input_zipcode
					.getText().toString()));
			params.add(new BasicNameValuePair("business_url",
					input_business_url.getText().toString()));
			params.add(new BasicNameValuePair("working_hrs", input_working_hrs
					.getText().toString()));
			JSONObject json = jParser.makeHttpRequest(
					url_update_info_for_first, "POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_data_updated = true;
				} else {
					is_data_updated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_data_updated) {
				if (is_image_selected) {
					uploadImage();
				}
				Toast.makeText(getApplicationContext(), "Information updated",
						Toast.LENGTH_SHORT).show();

				Editor edit1 = businessurlprefs.edit();
				edit1.putString(strBusinessUrlSet, input_business_url.getText()
						.toString());
				edit1.commit();
				Editor edit2 = businessworkinghrsprefs.edit();
				edit2.putString(strBusinessWorkingHrsSet, input_working_hrs
						.getText().toString());
				edit2.commit();

				is_data_updated = false;
				setContentView(R.layout.additional_info_activity);
				overridePendingTransition(R.anim.abc_fade_out,
						R.anim.abc_fade_in);
				toolBar = (Toolbar) findViewById(R.id.tool_bar);
				setSupportActionBar(toolBar);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayShowHomeEnabled(true);
				toolBar.setNavigationOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onBackEvent();
					}
				});

			} else {
				Toast.makeText(getApplicationContext(), "Problem in updating",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
			}
		}
	}

	class UpdateInfoSecond extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Additional_Info_Activity.this);
			pDialog.setMessage("Updating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("description",
					input_about_business.getText().toString()));
			params.add(new BasicNameValuePair("greeting_text",
					input_greeting_text.getText().toString()));
			params.add(new BasicNameValuePair("product_service",
					input_product_services.getText().toString()));
			params.add(new BasicNameValuePair("additional_text",
					input_additional_text.getText().toString()));
			JSONObject json = jParser.makeHttpRequest(
					url_update_info_for_second, "POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_data_updated = true;
				} else {
					is_data_updated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_data_updated) {
				Toast.makeText(getApplicationContext(), "Information updated",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
				setContentView(R.layout.additional_info_activity);
				overridePendingTransition(R.anim.abc_fade_out,
						R.anim.abc_fade_in);
				toolBar = (Toolbar) findViewById(R.id.tool_bar);
				setSupportActionBar(toolBar);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayShowHomeEnabled(true);
				toolBar.setNavigationOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onBackEvent();
					}
				});
			} else {
				Toast.makeText(getApplicationContext(), "Problem in updating",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
			}
		}
	}

	class UpdateInfoThird extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Additional_Info_Activity.this);
			pDialog.setMessage("Updating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("password", input_confirm_pass
					.getText().toString()));
			JSONObject json = jParser.makeHttpRequest(
					url_update_info_for_third, "POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_data_updated = true;
				} else {
					is_data_updated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_data_updated) {
				Toast.makeText(getApplicationContext(), "Information updated",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
				setContentView(R.layout.additional_info_activity);
				overridePendingTransition(R.anim.abc_fade_out,
						R.anim.abc_fade_in);
				toolBar = (Toolbar) findViewById(R.id.tool_bar);
				setSupportActionBar(toolBar);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayShowHomeEnabled(true);
				toolBar.setNavigationOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onBackEvent();
					}
				});
			} else {
				Toast.makeText(getApplicationContext(), "Problem in updating",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
			}
		}
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
			case R.id.input_confirm_pass:
				validateConfirmPassword();
				break;
			}
		}
	}

	public void loadImagefromGallery(View view) {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
					&& null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				imgPath = cursor.getString(columnIndex);
				cursor.close();
				ImgPreviewDialog();
				String fileNameSegments[] = imgPath.split("/");
				fileName = fileNameSegments[fileNameSegments.length - 1];
				params.put("filename", business_id + "_" + fileName);

			}
			// if the result is capturing Image
			else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					imgPath = fileUri.getPath();
					ImgPreviewDialog();
					fileName = "IMG_" + capture_img_time + ".jpg";
					params.put("filename", business_id + "_" + fileName);

				} else if (resultCode == RESULT_CANCELED) {

					// user cancelled Image capture
					Toast.makeText(getApplicationContext(),
							"User cancelled image capture", Toast.LENGTH_SHORT)
							.show();

				} else {
					// failed to capture image
					Toast.makeText(getApplicationContext(),
							"Sorry! Failed to capture image",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "You haven't picked Image",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void ImgPreviewDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Deal Image Preview");
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater.inflate(R.layout.alert_img_preview,
				null);
		alertDialog.setView(convertView);

		RoundedImageView imgPreview = (RoundedImageView) convertView
				.findViewById(R.id.img_preview);

		Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);

		Display display = getWindowManager().getDefaultDisplay();

		int width = display.getWidth();
		btn_ok.setWidth(width / 2);
		btn_cancel.setWidth(width / 2);

		imgPreview.setImageBitmap(BitmapFactory.decodeFile(imgPath));
		alertDialog.show();

		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
				Button btn_logo = (Button) findViewById(R.id.addbusinesslogo);
				btn_logo.setText(fileName);
				is_image_selected = true;
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void UploadImageDialog() {
		final ArrayList<String> city_deals = new ArrayList<String>();
		city_deals.add("Camera");
		city_deals.add("Gallery");
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Select image option");
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);

		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, city_deals);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				alertDialog.dismiss();
				if (position == 0) {
					captureImage();
				}
				if (position == 1) {
					loadImagefromGallery(view);
				}
			}
		});
	}

	public void uploadImage() {

		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString();
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
	}

	public void encodeImagetoString() {
		new AsyncTask<Void, Void, String>() {
			private ProgressDialog pDialog;

			protected void onPreExecute() {
				pDialog = new ProgressDialog(Additional_Info_Activity.this);
				pDialog.setMessage("Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				bitmap = BitmapFactory.decodeFile(imgPath, options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				pDialog.dismiss();
				params.put("image", encodedString);
				triggerImageUpload();
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload() {
		makeHTTPCall();
	}

	public void makeHTTPCall() {
		endpDialog = new ProgressDialog(Additional_Info_Activity.this);
		endpDialog.setMessage("Please wait...");
		endpDialog.setIndeterminate(false);
		endpDialog.setCancelable(false);
		endpDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(
				"http://www.pindout.com/mobi/pindout/upload_business_logo.php",
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						endpDialog.hide();
						image_uploaded_success = true;
						if (image_uploaded_success) {
							Editor edit6 = businesslogoprefs.edit();
							edit6.putString(strBusinessLogoSet, business_id
									+ "_" + fileName);
							edit6.commit();
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						endpDialog.hide();

						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}

						else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
											+ statusCode, Toast.LENGTH_LONG)
									.show();
						}
					}
				});
	}

	// Camera capture image code

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Config.IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Image Capture", "Oops! Failed create "
						+ Config.IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		capture_img_time = timeStamp;
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}
		return mediaFile;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (add_view_activated) {
			setContentView(R.layout.additional_info_activity);
			overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_in);
			toolBar = (Toolbar) findViewById(R.id.tool_bar);
			setSupportActionBar(toolBar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			toolBar.setNavigationOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackEvent();
				}
			});
			add_view_activated = false;
		} else {
			onBackEvent();
		}
	}

	private void onBackEvent() {
		finish();
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	}
}
