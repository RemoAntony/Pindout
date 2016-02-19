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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.helpers.RoundedImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddBusinessImageTagActivity extends AppCompatActivity {

	private EditText input_img_tab_name;
	private Button btn_add_first_img, btn_add_second_img, btn_add_third_img,
			btn_add_fourth_img, btn_add_fifth_img, btn_submit, btn_cancel;
	private ImageView close_img;
	private String business_id = "", img_tab_name = "", tab_img1, tab_img2,
			tab_img3, tab_img4, tab_img5;
	private boolean is_first_img_selected = false,
			is_second_img_selected = false, is_third_img_selected = false,
			is_fourth_img_selected = false, is_fifth_img_selected = false,
			is_data_updated = false;
	private boolean finish_on_first = false, finish_on_second = false,
			finish_on_third = false, finish_on_fourth = false,
			finish_on_fifth = false;

	private RequestParams params1 = new RequestParams();
	private RequestParams params2 = new RequestParams();
	private RequestParams params3 = new RequestParams();
	private RequestParams params4 = new RequestParams();
	private RequestParams params5 = new RequestParams();

	private int[] tab_imgs = new int[5];
	private String[] img_path = new String[5];
	private String[] filename = new String[5];

	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static int RESULT_LOAD_IMG = 1;
	private Uri fileUri;

	private static String capture_img_time = "";

	private Intent intent;

	JSONParser jParser = new JSONParser();

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_IMG_TAG_NAME = "img_tab_name";
	private static final String TAG_IMG1 = "img1";
	private static final String TAG_IMG2 = "img2";
	private static final String TAG_IMG3 = "img3";
	private static final String TAG_IMG4 = "img4";
	private static final String TAG_IMG5 = "img5";

	private static String url_get_info_for_fourth = "http://www.pindout.com/mobi/pindout/get_info_for_fourth.php";

	private static String url_update_info_for_img_tabs = "http://www.pindout.com/mobi/pindout/update_info_for_img_tabs.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_info_fourth);
		input_img_tab_name = (EditText) findViewById(R.id.input_image_tab_name);
		btn_add_first_img = (Button) findViewById(R.id.addfirstimg);
		btn_add_second_img = (Button) findViewById(R.id.addsecondimg);
		btn_add_third_img = (Button) findViewById(R.id.addthirdimg);
		btn_add_fourth_img = (Button) findViewById(R.id.addfourthimg);
		btn_add_fifth_img = (Button) findViewById(R.id.addfifthimg);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		close_img = (ImageView) findViewById(R.id.close_img);
		intent = getIntent();
		business_id = intent.getStringExtra("business_id");

		tab_imgs[0] = 0;
		tab_imgs[1] = 0;
		tab_imgs[2] = 0;
		tab_imgs[3] = 0;
		tab_imgs[4] = 0;

		img_path[0] = "0";
		img_path[1] = "0";
		img_path[2] = "0";
		img_path[3] = "0";
		img_path[4] = "0";

		btn_add_first_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tab_imgs[0] = 1;
				UploadImageDialog();
			}
		});

		btn_add_second_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tab_imgs[1] = 1;
				UploadImageDialog();
			}
		});

		btn_add_third_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tab_imgs[2] = 1;
				UploadImageDialog();
			}
		});

		btn_add_fourth_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tab_imgs[3] = 1;
				UploadImageDialog();
			}
		});

		btn_add_fifth_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tab_imgs[4] = 1;
				UploadImageDialog();
			}
		});

		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!img_path[0].equals("0")) {
					finish_on_first = true;
					finish_on_second = false;
					finish_on_third = false;
					finish_on_fourth = false;
					finish_on_fifth = false;
				}
				if (!img_path[1].equals("0")) {
					finish_on_first = false;
					finish_on_second = true;
					finish_on_third = false;
					finish_on_fourth = false;
					finish_on_fifth = false;
				}
				if (!img_path[2].equals("0")) {
					finish_on_first = false;
					finish_on_second = false;
					finish_on_third = true;
					finish_on_fourth = false;
					finish_on_fifth = false;
				}
				if (!img_path[3].equals("0")) {
					finish_on_first = false;
					finish_on_second = false;
					finish_on_third = false;
					finish_on_fourth = true;
					finish_on_fifth = false;
				}
				if (!img_path[4].equals("0")) {
					finish_on_first = false;
					finish_on_second = false;
					finish_on_third = false;
					finish_on_fourth = false;
					finish_on_fifth = true;
				}

				/*
				 * if (is_first_img_selected) { new
				 * one_upload().execute(img_path[0]); } if
				 * (is_second_img_selected) { new
				 * two_upload().execute(img_path[1]); } if
				 * (is_third_img_selected) { new
				 * three_upload().execute(img_path[2]); } if
				 * (is_fourth_img_selected) { new
				 * four_upload().execute(img_path[3]); } if
				 * (is_fifth_img_selected) { new
				 * five_upload().execute(img_path[4]); }
				 */

				new one_upload().execute();
			}
		});

		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});

		close_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});

		new GetInfoFourth().execute();
	}

	class GetInfoFourth extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
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
								url_get_info_for_fourth, "GET", params);
						Log.d("Single User Details", json.toString());
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT);
							JSONObject product = productObj.getJSONObject(0);
							img_tab_name = product.getString(TAG_IMG_TAG_NAME);
							tab_img1 = product.getString(TAG_IMG1);
							tab_img2 = product.getString(TAG_IMG2);
							tab_img3 = product.getString(TAG_IMG3);
							tab_img4 = product.getString(TAG_IMG4);
							tab_img5 = product.getString(TAG_IMG5);
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
			if (!img_tab_name.equals(""))
				input_img_tab_name.setText(img_tab_name);
			if (!tab_img1.equals(""))
				btn_add_first_img.setText(tab_img1);
			if (!tab_img2.equals(""))
				btn_add_second_img.setText(tab_img2);
			if (!tab_img3.equals(""))
				btn_add_third_img.setText(tab_img3);
			if (!tab_img4.equals(""))
				btn_add_fourth_img.setText(tab_img4);
			if (!tab_img5.equals(""))
				btn_add_fifth_img.setText(tab_img5);
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
				if (tab_imgs[0] == 1) {
					tab_imgs[0] = 1;
					tab_imgs[1] = 0;
					tab_imgs[2] = 0;
					tab_imgs[3] = 0;
					tab_imgs[4] = 0;
					if (position == 0) {
						captureImage();
					}
					if (position == 1) {
						loadImagefromGallery(view);
					}
				}
				if (tab_imgs[1] == 1) {
					tab_imgs[0] = 0;
					tab_imgs[1] = 1;
					tab_imgs[2] = 0;
					tab_imgs[3] = 0;
					tab_imgs[4] = 0;
					if (position == 0) {
						captureImage();
					}
					if (position == 1) {
						loadImagefromGallery(view);
					}
				}
				if (tab_imgs[2] == 1) {
					tab_imgs[0] = 0;
					tab_imgs[1] = 0;
					tab_imgs[2] = 1;
					tab_imgs[3] = 0;
					tab_imgs[4] = 0;
					if (position == 0) {
						captureImage();
					}
					if (position == 1) {
						loadImagefromGallery(view);
					}
				}
				if (tab_imgs[3] == 1) {
					tab_imgs[0] = 0;
					tab_imgs[1] = 0;
					tab_imgs[2] = 0;
					tab_imgs[3] = 1;
					tab_imgs[4] = 0;
					if (position == 0) {
						captureImage();
					}
					if (position == 1) {
						loadImagefromGallery(view);
					}
				}
				if (tab_imgs[4] == 1) {
					tab_imgs[0] = 0;
					tab_imgs[1] = 0;
					tab_imgs[2] = 0;
					tab_imgs[3] = 0;
					tab_imgs[4] = 1;
					if (position == 0) {
						captureImage();
					}
					if (position == 1) {
						loadImagefromGallery(view);
					}
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
				if (tab_imgs[0] == 1) {
					img_path[0] = cursor.getString(columnIndex);
					cursor.close();
					ImgPreviewDialog();
					String fileNameSegments[] = img_path[0].split("/");
					filename[0] = fileNameSegments[fileNameSegments.length - 1];
					params1.put("filename", business_id + "_" + filename[0]);
				}
				if (tab_imgs[1] == 1) {
					img_path[1] = cursor.getString(columnIndex);
					cursor.close();
					ImgPreviewDialog();
					String fileNameSegments[] = img_path[1].split("/");
					filename[1] = fileNameSegments[fileNameSegments.length - 1];
					params2.put("filename", business_id + "_" + filename[1]);
				}
				if (tab_imgs[2] == 1) {
					img_path[2] = cursor.getString(columnIndex);
					cursor.close();
					ImgPreviewDialog();
					String fileNameSegments[] = img_path[2].split("/");
					filename[2] = fileNameSegments[fileNameSegments.length - 1];
					params3.put("filename", business_id + "_" + filename[2]);
				}
				if (tab_imgs[3] == 1) {
					img_path[3] = cursor.getString(columnIndex);
					cursor.close();
					ImgPreviewDialog();
					String fileNameSegments[] = img_path[3].split("/");
					filename[3] = fileNameSegments[fileNameSegments.length - 1];
					params4.put("filename", business_id + "_" + filename[3]);
				}
				if (tab_imgs[4] == 1) {
					img_path[4] = cursor.getString(columnIndex);
					cursor.close();
					ImgPreviewDialog();
					String fileNameSegments[] = img_path[4].split("/");
					filename[4] = fileNameSegments[fileNameSegments.length - 1];
					params5.put("filename", business_id + "_" + filename[4]);
				}
			}
			// if the result is capturing Image
			else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					if (tab_imgs[0] == 1) {
						img_path[0] = fileUri.getPath();
						ImgPreviewDialog();
						filename[0] = "IMG_" + capture_img_time + ".jpg";
						params1.put("filename", business_id + "_" + filename[0]);
					}
					if (tab_imgs[1] == 1) {
						img_path[1] = fileUri.getPath();
						ImgPreviewDialog();
						filename[1] = "IMG_" + capture_img_time + ".jpg";
						params2.put("filename", business_id + "_" + filename[1]);
					}
					if (tab_imgs[2] == 1) {
						img_path[2] = fileUri.getPath();
						ImgPreviewDialog();
						filename[2] = "IMG_" + capture_img_time + ".jpg";
						params3.put("filename", business_id + "_" + filename[2]);
					}
					if (tab_imgs[3] == 1) {
						img_path[3] = fileUri.getPath();
						ImgPreviewDialog();
						filename[3] = "IMG_" + capture_img_time + ".jpg";
						params4.put("filename", business_id + "_" + filename[3]);
					}
					if (tab_imgs[4] == 1) {
						img_path[4] = fileUri.getPath();
						ImgPreviewDialog();
						filename[4] = "IMG_" + capture_img_time + ".jpg";
						params5.put("filename", business_id + "_" + filename[4]);
					}
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

		if (tab_imgs[0] == 1) {
			imgPreview.setImageBitmap(BitmapFactory.decodeFile(img_path[0]));
		}
		if (tab_imgs[1] == 1) {
			imgPreview.setImageBitmap(BitmapFactory.decodeFile(img_path[1]));
		}
		if (tab_imgs[2] == 1) {
			imgPreview.setImageBitmap(BitmapFactory.decodeFile(img_path[2]));
		}
		if (tab_imgs[3] == 1) {
			imgPreview.setImageBitmap(BitmapFactory.decodeFile(img_path[3]));
		}
		if (tab_imgs[4] == 1) {
			imgPreview.setImageBitmap(BitmapFactory.decodeFile(img_path[4]));
		}
		alertDialog.show();

		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tab_imgs[0] == 1) {
					is_first_img_selected = true;
					btn_add_first_img.setText(filename[0]);
					tab_imgs[0] = 0;
					// Toast.makeText(getApplicationContext(), img_path[0],
					// Toast.LENGTH_SHORT).show();
				}
				if (tab_imgs[1] == 1) {
					is_second_img_selected = true;
					btn_add_second_img.setText(filename[1]);
					tab_imgs[1] = 0;
					// Toast.makeText(getApplicationContext(), img_path[1],
					// Toast.LENGTH_SHORT).show();
				}
				if (tab_imgs[2] == 1) {
					is_third_img_selected = true;
					btn_add_third_img.setText(filename[2]);
					tab_imgs[2] = 0;
					// Toast.makeText(getApplicationContext(), img_path[2],
					// Toast.LENGTH_SHORT).show();
				}
				if (tab_imgs[3] == 1) {
					is_fourth_img_selected = true;
					btn_add_fourth_img.setText(filename[3]);
					tab_imgs[3] = 0;
					// Toast.makeText(getApplicationContext(), img_path[3],
					// Toast.LENGTH_SHORT).show();
				}
				if (tab_imgs[4] == 1) {
					is_fifth_img_selected = true;
					btn_add_fifth_img.setText(filename[4]);
					tab_imgs[4] = 0;
					// Toast.makeText(getApplicationContext(), img_path[4],
					// Toast.LENGTH_SHORT).show();
				}
				alertDialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tab_imgs[0] == 1) {
					tab_imgs[0] = 0;
				}
				if (tab_imgs[1] == 1) {
					tab_imgs[1] = 0;
				}
				if (tab_imgs[2] == 1) {
					tab_imgs[2] = 0;
				}
				if (tab_imgs[3] == 1) {
					tab_imgs[3] = 0;
				}
				if (tab_imgs[4] == 1) {
					tab_imgs[4] = 0;
				}
				alertDialog.dismiss();
			}
		});
	}

	class UpdateInfoImgTabs extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
			pDialog.setMessage("Updating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("img_tab_name",
					input_img_tab_name.getText().toString()));
			if (is_first_img_selected) {
				params.add(new BasicNameValuePair("img1", business_id + "_"
						+ btn_add_first_img.getText().toString()));
			} else {
				if (btn_add_first_img.getText().toString()
						.equals("First Image")) {
					params.add(new BasicNameValuePair("img1", ""));
				} else {
					params.add(new BasicNameValuePair("img1", btn_add_first_img
							.getText().toString()));
				}
			}
			if (is_second_img_selected) {
				params.add(new BasicNameValuePair("img2", business_id + "_"
						+ btn_add_second_img.getText().toString()));
			} else {
				if (btn_add_second_img.getText().toString()
						.equals("Second Image")) {
					params.add(new BasicNameValuePair("img2", ""));
				} else {
					params.add(new BasicNameValuePair("img2",
							btn_add_second_img.getText().toString()));
				}
			}
			if (is_third_img_selected) {
				params.add(new BasicNameValuePair("img3", business_id + "_"
						+ btn_add_third_img.getText().toString()));
			} else {
				if (btn_add_third_img.getText().toString()
						.equals("Third Image")) {
					params.add(new BasicNameValuePair("img3", ""));
				} else {
					params.add(new BasicNameValuePair("img3", btn_add_third_img
							.getText().toString()));
				}
			}
			if (is_fourth_img_selected) {
				params.add(new BasicNameValuePair("img4", business_id + "_"
						+ btn_add_fourth_img.getText().toString()));
			} else {
				if (btn_add_fourth_img.getText().toString()
						.equals("Fourth Image")) {
					params.add(new BasicNameValuePair("img4", ""));
				} else {
					params.add(new BasicNameValuePair("img4",
							btn_add_fourth_img.getText().toString()));
				}
			}
			if (is_fifth_img_selected) {
				params.add(new BasicNameValuePair("img5", business_id + "_"
						+ btn_add_fifth_img.getText().toString()));
			} else {
				if (btn_add_fifth_img.getText().toString()
						.equals("Fifth Image")) {
					params.add(new BasicNameValuePair("img5", ""));
				} else {
					params.add(new BasicNameValuePair("img5", btn_add_fifth_img
							.getText().toString()));
				}
			}
			JSONObject json = jParser.makeHttpRequest(
					url_update_info_for_img_tabs, "POST", params);

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

			// one_upload oneUpload = new one_upload();
			// two_upload twoUpload = new two_upload();
			// three_upload threeUpload = new three_upload();
			// four_upload fourUpload = new four_upload();
			// five_upload fiveUpload = new five_upload();

			if (is_data_updated) {
				Toast.makeText(getApplicationContext(), "Information updated",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
				onBackEvent();
			} else {
				Toast.makeText(getApplicationContext(), "Problem in updating",
						Toast.LENGTH_SHORT).show();
				is_data_updated = false;
			}
		}
	}

	class one_upload extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// uploadImage1(params[0]);
			if (is_first_img_selected) {
				uploadImage1(img_path[0]);
			}
			if (is_second_img_selected) {
				uploadImage2(img_path[1]);
			}
			if (is_third_img_selected) {
				uploadImage3(img_path[2]);
			}
			if (is_fourth_img_selected) {
				uploadImage4(img_path[3]);
			}
			if (is_fifth_img_selected) {
				uploadImage5(img_path[4]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			new UpdateInfoImgTabs().execute();
		}
	}

	class two_upload extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			uploadImage2(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (finish_on_second) {
				new UpdateInfoImgTabs().execute();
			}
		}
	}

	class three_upload extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			uploadImage3(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (finish_on_third) {
				new UpdateInfoImgTabs().execute();
			}
		}
	}

	class four_upload extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			uploadImage4(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (finish_on_fourth) {
				new UpdateInfoImgTabs().execute();
			}
		}
	}

	class five_upload extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddBusinessImageTagActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			uploadImage5(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (finish_on_fifth) {
				new UpdateInfoImgTabs().execute();
			}
		}
	}

	// first upload image method

	public void uploadImage1(String imgPath) {
		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString1(imgPath);
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
	}

	public void encodeImagetoString1(final String imgPath) {
		new AsyncTask<Void, Void, String>() {

			String encodedString;

			protected void onPreExecute() {

			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				params1.put("image", encodedString);
				triggerImageUpload1();
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload1() {
		makeHTTPCall1();
	}

	public void makeHTTPCall1() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(
				"http://www.pindout.com/mobi/pindout/upload_tab_images.php",
				params1, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// is_image_uploaded = true;
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// is_image_uploaded = false;
						if (statusCode == 404) {

						}

						else if (statusCode == 500) {

						} else {

						}
					}
				});
	}

	// Second upload image method

	public void uploadImage2(String imgPath) {
		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString2(imgPath);
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
	}

	public void encodeImagetoString2(final String imgPath) {
		new AsyncTask<Void, Void, String>() {
			String encodedString;

			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				params2.put("image", encodedString);
				triggerImageUpload2();
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload2() {
		makeHTTPCall2();
	}

	public void makeHTTPCall2() {
		AsyncHttpClient client2 = new AsyncHttpClient();
		client2.post(
				"http://www.pindout.com/mobi/pindout/upload_tab_images.php",
				params2, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// is_image_uploaded = true;
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// is_image_uploaded = false;
						if (statusCode == 404) {

						}

						else if (statusCode == 500) {

						} else {

						}
					}
				});
	}

	// Third image upload

	public void uploadImage3(String imgPath) {
		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString3(imgPath);
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
	}

	public void encodeImagetoString3(final String imgPath) {
		new AsyncTask<Void, Void, String>() {
			String encodedString;

			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				params3.put("image", encodedString);
				triggerImageUpload3();
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload3() {
		makeHTTPCall3();
	}

	public void makeHTTPCall3() {
		AsyncHttpClient client2 = new AsyncHttpClient();
		client2.post(
				"http://www.pindout.com/mobi/pindout/upload_tab_images.php",
				params3, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// is_image_uploaded = true;
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// is_image_uploaded = false;
						if (statusCode == 404) {

						}

						else if (statusCode == 500) {

						} else {

						}
					}
				});
	}

	// Fourth Image Dialog

	public void uploadImage4(String imgPath) {
		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString4(imgPath);
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
	}

	public void encodeImagetoString4(final String imgPath) {
		new AsyncTask<Void, Void, String>() {
			String encodedString;

			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				params4.put("image", encodedString);
				triggerImageUpload4();
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload4() {
		makeHTTPCall4();
	}

	public void makeHTTPCall4() {
		AsyncHttpClient client2 = new AsyncHttpClient();
		client2.post(
				"http://www.pindout.com/mobi/pindout/upload_tab_images.php",
				params4, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// is_image_uploaded = true;
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// is_image_uploaded = false;
						if (statusCode == 404) {

						}

						else if (statusCode == 500) {

						} else {

						}
					}
				});
	}

	// Fifth image upload

	public void uploadImage5(String imgPath) {
		if (imgPath != null && !imgPath.isEmpty()) {
			encodeImagetoString5(imgPath);
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
	}

	public void encodeImagetoString5(final String imgPath) {
		new AsyncTask<Void, Void, String>() {
			String encodedString;

			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				params5.put("image", encodedString);
				triggerImageUpload5();
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload5() {
		makeHTTPCall5();
	}

	public void makeHTTPCall5() {
		AsyncHttpClient client2 = new AsyncHttpClient();
		client2.post(
				"http://www.pindout.com/mobi/pindout/upload_tab_images.php",
				params5, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// is_image_uploaded = true;
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// is_image_uploaded = false;
						if (statusCode == 404) {

						}

						else if (statusCode == 500) {

						} else {

						}
					}
				});
	}

	@Override
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
