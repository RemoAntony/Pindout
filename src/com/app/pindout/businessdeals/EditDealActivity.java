package com.app.pindout.businessdeals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.JSONParser;
import com.app.pindout.R;
import com.app.pindout.helpers.RoundedImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class EditDealActivity extends AppCompatActivity {

	private String encodedString;
	private RequestParams params = new RequestParams();

	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri;
	private static String capture_img_time = "";

	private String imgPath, fileName = "not_available", deal_id = "",
			is_display_in_city = "", deal_catid = "", business_id = "";
	private Bitmap bitmap;
	private static int RESULT_LOAD_IMG = 1;
	private Intent intent;
	private boolean is_deal_updated = false;

	private Toolbar toolBar;
	private TextInputLayout input_layout_deal_title, input_layout_dealdesc,
			input_layout_deal_mainprice, input_layout_deal_price;
	private EditText input_deal_title, input_deal_url, input_deal_desc,
			input_deal_mainprice, input_deal_price;
	private Button selectimage, selectdealcat, selectenddate, btncancel,
			btnsubmit;
	private boolean is_image_selected = false;

	private ArrayList<String> deal_cat_list = new ArrayList<String>();
	private ArrayList<String> deal_cat_id = new ArrayList<String>();
	private Calendar calendar;
	private int year, month, day;

	JSONArray products = null;
	JSONParser jParser = new JSONParser();
	ProgressDialog endpDialog;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_DEALCATNAME = "deal_category_name";
	private static final String TAG_DEALCATID = "id";

	private static String url_deal_categories = "http://www.pindout.com/mobi/pindout/get_deal_category.php";
	private static String url_update_deal = "http://www.pindout.com/mobi/pindout/update_businessdeal.php";

	public static final String LoginPrefs = "LoginPrefs";
	public static final String strloginprefs = "0";
	public SharedPreferences loginprefs;

	public static final String IsEdit = "IsEditPrefs";
	public static final String strIsEditSet = "0";
	public SharedPreferences iseditprefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_deal);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolBar.setTitle("Edit Deal");
		intent = getIntent();
		iseditprefs = getSharedPreferences(IsEdit, Context.MODE_PRIVATE);

		input_layout_deal_title = (TextInputLayout) findViewById(R.id.input_layout_dealtitle);
		input_layout_deal_mainprice = (TextInputLayout) findViewById(R.id.input_layout_dealmainprice);
		input_layout_deal_price = (TextInputLayout) findViewById(R.id.input_layout_dealprice);
		input_layout_dealdesc = (TextInputLayout) findViewById(R.id.input_layout_dealdesc);

		input_deal_desc = (EditText) findViewById(R.id.input_deal_desc);
		input_deal_title = (EditText) findViewById(R.id.input_deal_title);
		input_deal_url = (EditText) findViewById(R.id.input_deal_url);
		input_deal_mainprice = (EditText) findViewById(R.id.input_deal_mainprice);
		input_deal_price = (EditText) findViewById(R.id.input_deal_price);

		selectenddate = (Button) findViewById(R.id.selectenddate);
		selectdealcat = (Button) findViewById(R.id.selectdealcategory);
		selectimage = (Button) findViewById(R.id.selectdealimage);
		btncancel = (Button) findViewById(R.id.btn_cancel);
		btnsubmit = (Button) findViewById(R.id.btn_submit);

		deal_id = intent.getStringExtra("deal_id");
		fileName = intent.getStringExtra("deal_image");
		deal_catid = intent.getStringExtra("deal_catid");
		input_deal_desc.setText(intent.getStringExtra("deal_desc"));
		input_deal_title.setText(intent.getStringExtra("deal_title"));
		input_deal_url.setText(intent.getStringExtra("deal_url"));
		input_deal_mainprice.setText(intent.getStringExtra("deal_mainprice"));
		input_deal_price.setText(intent.getStringExtra("deal_dealprice"));
		business_id = intent.getStringExtra("business_id");

		selectenddate.setText(intent.getStringExtra("end_date"));
		selectimage.setText(fileName);
		selectdealcat.setText(intent.getStringExtra("deal_catname"));

		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		btnsubmit.setText("Save");
		btncancel.setWidth(width / 2);
		btnsubmit.setWidth(width / 2);

		input_deal_title.addTextChangedListener(new MyTextWatcher(
				input_deal_title));
		input_deal_desc.addTextChangedListener(new MyTextWatcher(
				input_deal_desc));
		input_deal_url
				.addTextChangedListener(new MyTextWatcher(input_deal_url));
		input_deal_mainprice.addTextChangedListener(new MyTextWatcher(
				input_deal_mainprice));
		input_deal_price.addTextChangedListener(new MyTextWatcher(
				input_deal_price));

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);

		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		toolBar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});

		selectdealcat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (deal_cat_id.size() > 0) {
					DealsCatDialog();
				} else {
					new GetDealCategory().execute();
				}
			}
		});
		selectenddate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDate(v);
			}
		});
		selectimage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UploadImageDialog();
			}
		});
		btnsubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SubmitBtn(v);
			}
		});
		btncancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
	}

	public void SubmitBtn(View v) {
		if (!validateDealTitle()) {
			return;
		}
		if (!validateDealMainPrice()) {
			return;
		}
		if (!validateDealPrice()) {
			return;
		}
		if (!validateDealDesc()) {
			return;
		}
		CityDealsDialog();
	}

	private boolean validateDealTitle() {
		if (input_deal_title.getText().toString().trim().isEmpty()) {
			input_layout_deal_title
					.setError(getString(R.string.err_msg_dealtitle));
			requestFocus(input_deal_title);
			return false;
		} else {
			input_layout_deal_title.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateDealMainPrice() {
		if (input_deal_mainprice.getText().toString().trim().isEmpty()) {
			input_layout_deal_mainprice
					.setError(getString(R.string.err_msg_deal_mainprice));
			requestFocus(input_deal_mainprice);
			return false;
		} else {
			input_layout_deal_mainprice.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateDealDesc() {
		if (input_deal_desc.getText().toString().trim().isEmpty()) {
			input_layout_dealdesc
					.setError(getString(R.string.err_msg_deal_desc));
			requestFocus(input_deal_desc);
			return false;
		} else {
			input_layout_dealdesc.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateDealPrice() {
		if (input_deal_price.getText().toString().trim().isEmpty()) {
			input_layout_deal_price
					.setError(getString(R.string.err_msg_deal_price));
			requestFocus(input_deal_price);
			return false;
		} else {
			input_layout_deal_price.setErrorEnabled(false);
		}
		return true;
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	public void setDate(View view) {
		showDialog(999);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stud
			showDate(arg1, arg2 + 1, arg3);
		}
	};

	private void showDate(int year, int month, int day) {
		selectenddate.setText(new StringBuilder().append(day).append("-")
				.append(month).append("-").append(year));
	}

	class GetDealCategory extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditDealActivity.this);
			pDialog.setMessage("Searching...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			deal_cat_id.clear();
			deal_cat_list.clear();

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			JSONObject json = jParser.makeHttpRequest(url_deal_categories,
					"GET", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						deal_cat_list.add(c.getString(TAG_DEALCATNAME));
						deal_cat_id.add(c.getString(TAG_DEALCATID));
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
			DealsCatDialog();
		}
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

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void CityDealsDialog() {
		final ArrayList<String> city_deals = new ArrayList<String>();
		city_deals.add("Yes");
		city_deals.add("No");
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Should Display in city deals page?");
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
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				alertDialog.dismiss();
				if (position == 0) {

					is_display_in_city = "1";
					new UpdatdeDeal().execute();
				}
				if (position == 1) {

					is_display_in_city = "0";
					new UpdatdeDeal().execute();
				}
			}
		});
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void DealsCatDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Select Deal Category");
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);

		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.city_list_items, R.id.city_text, deal_cat_list);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				alertDialog.dismiss();
				selectdealcat.setText(deal_cat_list.get(position));
				deal_catid = deal_cat_id.get(position);
			}
		});
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
				is_image_selected = true;
				selectimage.setText(fileName);
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

	public void uploadImage() {
		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString();
		}
	}

	public void encodeImagetoString() {
		new AsyncTask<Void, Void, String>() {
			private ProgressDialog pDialog;

			protected void onPreExecute() {
				pDialog = new ProgressDialog(EditDealActivity.this);
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
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
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

		endpDialog = new ProgressDialog(EditDealActivity.this);
		endpDialog.setMessage("Please wait...");
		endpDialog.setIndeterminate(false);
		endpDialog.setCancelable(false);
		endpDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post("http://www.pindout.com/mobi/pindout/upload_image.php", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						endpDialog.hide();
						finish();
						overridePendingTransition(R.anim.abc_fade_in,
								R.anim.abc_fade_out);
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						endpDialog.hide();

						if (statusCode == 404) {
							// Requested resource not found
							Toast.makeText(getApplicationContext(),
									"Unable to upload Deal Image",
									Toast.LENGTH_LONG).show();
						}

						else if (statusCode == 500) {
							// Something went wrong at server end
							Toast.makeText(getApplicationContext(),
									"Unable to upload Deal Image",
									Toast.LENGTH_LONG).show();
						}

						else {
							// Error Occured \n Most Common Error: \n1. Device
							// not connected to Internet\n2. Web App is not
							// deployed in App server\n3. App server is not
							// running\n HTTP Status code :
							// + statusCode
							Toast.makeText(getApplicationContext(),
									"Unable to upload Deal Image",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	class UpdatdeDeal extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		Calendar cal = Calendar.getInstance();

		int minute = cal.get(Calendar.MINUTE);
		int hourofday = cal.get(Calendar.HOUR_OF_DAY);
		int secondofday = cal.get(Calendar.SECOND);

		String Time = Integer.toString(hourofday) + ":"
				+ Integer.toString(minute) + ":"
				+ Integer.toString(secondofday);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditDealActivity.this);
			pDialog.setMessage("Updating deal...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("deal_id", deal_id));
			params.add(new BasicNameValuePair("deal_cat_id", deal_catid));
			params.add(new BasicNameValuePair("deal_title", input_deal_title
					.getText().toString()));
			params.add(new BasicNameValuePair("deal_desc", input_deal_desc
					.getText().toString()));
			if (!is_image_selected) {
				params.add(new BasicNameValuePair("deal_image", fileName));
			}
			if (is_image_selected) {
				params.add(new BasicNameValuePair("deal_image", business_id
						+ "_" + fileName));
			}
			params.add(new BasicNameValuePair("url_link", input_deal_url
					.getText().toString()));
			params.add(new BasicNameValuePair("main_price",
					input_deal_mainprice.getText().toString()));
			params.add(new BasicNameValuePair("deal_price", input_deal_price
					.getText().toString()));
			params.add(new BasicNameValuePair("display_in_city",
					is_display_in_city));
			params.add(new BasicNameValuePair("end_date", selectenddate
					.getText().toString() + " " + Time));

			JSONObject json = jParser.makeHttpRequest(url_update_deal, "POST",
					params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_deal_updated = true;

				} else {
					is_deal_updated = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_image_selected) {
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "deal_altered");
				edit.commit();
				uploadImage();
			} else {
				Editor edit = iseditprefs.edit();
				edit.putString(strIsEditSet, "deal_altered");
				edit.commit();
				finish();
				overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out);
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
			case R.id.input_deal_title:
				validateDealTitle();
				break;
			case R.id.input_deal_desc:
				validateDealDesc();
				break;
			case R.id.input_deal_mainprice:
				validateDealMainPrice();
				break;
			case R.id.input_deal_price:
				validateDealPrice();
				break;
			}
		}
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

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		onBackEvent();
	}

	private void onBackEvent() {
		if (is_deal_updated) {
			finish();
			overridePendingTransition(R.anim.left_to_right_enter,
					R.anim.left_to_right_exit);
		} else {
			finish();
			overridePendingTransition(R.anim.left_to_right_enter,
					R.anim.left_to_right_exit);
		}
	}
}
