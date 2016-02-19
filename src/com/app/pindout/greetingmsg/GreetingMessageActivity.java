package com.app.pindout.greetingmsg;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.pindout.JSONParser;
import com.app.pindout.R;
import com.app.pindout.dbadapter.GreetingmsgDBAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GreetingMessageActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private FloatingActionButton addmsg;
	JSONParser jsonParser = new JSONParser();
	JSONArray products = null;
	private Editable update_edittext;

	private Intent intent;
	private SimpleCursorAdapter dataAdapter;
	private boolean is_update_success = false, is_deleted_success = false;

	private static final String url_get_greeting_msg = "http://pindout.com/mobi/pindout/get_greeting_msg.php";
	private static final String url_update_greetingmsg = "http://pindout.com/mobi/pindout/update_greetingmsg.php";
	private static final String url_delete_greetingmsg = "http://pindout.com/mobi/pindout/delete_greetingmsg.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_ROWID = "row_id";
	private static final String TAG_DATE = "date";
	private static final String TAG_MSG = "message";

	private GreetingmsgDBAdapter greetingmsgdb;
	private String business_id = "";
	private boolean is_from_addnewmsgactivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greeting_msg);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		addmsg = (FloatingActionButton) findViewById(R.id.addmsg_fab);
		setSupportActionBar(toolbar);
		toolbar.setTitle("Messages");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		greetingmsgdb = new GreetingmsgDBAdapter(this);
		intent = getIntent();
		business_id = intent.getStringExtra("business_id");
		is_from_addnewmsgactivity = intent.getBooleanExtra("is_from_addnewmsg",
				false);
		if (is_from_addnewmsgactivity) {
			new LoadGreetingMessages().execute();
		} else {
			displayListView();
		}
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackEvent();
			}
		});
		addmsg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(GreetingMessageActivity.this,
						AddNewMessageActivity.class);
				intent2.putExtra("business_id", business_id);
				startActivity(intent2);
				finish();
			}
		});
	}

	class LoadGreetingMessages extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GreetingMessageActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			GreetingMessageActivity.this.deleteDatabase("Greetingmsg.db");
			greetingmsgdb.open();
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
						greetingmsgdb.insert(c.getString(TAG_ROWID),
								c.getString(TAG_MSG), c.getString(TAG_DATE));
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
			displayListView();
		}
	}

	private void displayListView() {
		greetingmsgdb.open();
		Cursor c = greetingmsgdb.getAll();
		if (c.moveToFirst()) {
			is_update_success = false;
			is_deleted_success = false;
			Cursor cursor = greetingmsgdb.getAll();
			String[] columns = new String[] { GreetingmsgDBAdapter.KEY_MESSAGE,
					GreetingmsgDBAdapter.KEY_MSGDATE };
			int[] to = new int[] { R.id.textgreetingmsg, R.id.greetingmsgdate };
			dataAdapter = new SimpleCursorAdapter(this,
					R.layout.greeting_msg_row, cursor, columns, to, 0);
			ListView listView = (ListView) findViewById(R.id.greeting_msglistview);
			listView.setAdapter(dataAdapter);
			greetingmsgdb.close();
		} else {
			setContentView(R.layout.activity_no_record_found);
			toolbar = (Toolbar) findViewById(R.id.tool_bar);
			addmsg = (FloatingActionButton) findViewById(R.id.addmsg_fab);
			setSupportActionBar(toolbar);
			toolbar.setTitle("Messages");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackEvent();
				}
			});
			addmsg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent2 = new Intent(GreetingMessageActivity.this,
							AddNewMessageActivity.class);
					intent2.putExtra("business_id", business_id);
					startActivity(intent2);
					finish();
				}
			});
			greetingmsgdb.close();
		}
	}

	public void Update_msg(View v) {
		ListView listview = (ListView) findViewById(R.id.greeting_msglistview);
		int position = listview.getPositionForView(v);
		position++;
		greetingmsgdb.open();
		Cursor c = greetingmsgdb.get(position);
		String business_id = c.getString(1);
		String message = c.getString(2);
		greetingmsgdb.close();
		alertEdit(business_id, message);
	}

	@SuppressWarnings("deprecation")
	private void alertEdit(final String business_id, String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				GreetingMessageActivity.this).create();

		alertDialog.setTitle("Update Message");

		InputFilter[] filter = new InputFilter[1];
		int max_length = 150;
		filter[0] = new InputFilter.LengthFilter(max_length);
		final EditText input = new EditText(this);
		input.setId(0);
		input.setHint("Enter message");
		input.setText(message);
		input.setHintTextColor(getResources().getColor(R.color.bg_blue));
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
		input.setLines(10);
		input.setFilters(filter);
		input.setSingleLine(false);
		alertDialog.setView(input);
		update_edittext = input.getText();

		alertDialog.setButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String currentDateString = DateFormat.getDateInstance().format(
						new Date());
				alertDialog.dismiss();
				new SaveMsgDetails().execute(business_id,
						update_edittext.toString(), currentDateString);
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	class SaveMsgDetails extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GreetingMessageActivity.this);
			pDialog.setMessage("Updating message...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			String business_id = args[0];
			String msg = args[1];
			String date = args[2];

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("business_id", business_id));
			params.add(new BasicNameValuePair("msg", msg));
			params.add(new BasicNameValuePair("date", date));

			JSONObject json = jsonParser.makeHttpRequest(
					url_update_greetingmsg, "POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					is_update_success = true;
				} else {
					is_update_success = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_update_success) {
				Toast.makeText(getApplicationContext(), "Message updated",
						Toast.LENGTH_SHORT).show();
				is_update_success = false;
				new LoadGreetingMessages().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Message not updated",
						Toast.LENGTH_SHORT).show();
				is_update_success = false;
			}
		}
	}

	public void Delete_msg(View v) {
		ListView listview = (ListView) findViewById(R.id.greeting_msglistview);
		int position = listview.getPositionForView(v);
		position++;
		greetingmsgdb.open();
		Cursor c = greetingmsgdb.get(position);
		String business_id = c.getString(1);
		String message = c.getString(2);
		greetingmsgdb.close();
		alertDelete(business_id, message);
	}

	@SuppressWarnings("deprecation")
	private void alertDelete(final String business_id, String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				GreetingMessageActivity.this).create();

		alertDialog.setTitle("Delete Message " + "\"" + message + "\"" + " ?");

		alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				new DeleteMessage().execute(business_id);
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	class DeleteMessage extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GreetingMessageActivity.this);
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
					is_deleted_success = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (is_deleted_success) {
				Toast.makeText(getApplicationContext(), "Message deleted",
						Toast.LENGTH_SHORT).show();
				is_deleted_success = false;
				new LoadGreetingMessages().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Message not deleted",
						Toast.LENGTH_SHORT).show();
				is_deleted_success = false;
			}
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