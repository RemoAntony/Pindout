package com.app.pindout.helpers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pindout.MainActivity;
import com.app.pindout.R;

@SuppressLint("ViewHolder")
public class MessagesAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();

	private String msg = "", msg_id = "";

	public MessagesAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView textmsg, msg_date;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.greeting_msg_row, parent,
				false);

		resultp = data.get(position);

		textmsg = (TextView) itemView.findViewById(R.id.textgreetingmsg);
		msg_date = (TextView) itemView.findViewById(R.id.greetingmsgdate);

		textmsg.setText(resultp.get(MainActivity.TAG_MSG));
		msg_date.setText(resultp.get(MainActivity.TAG_DATE));

		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resultp = data.get(position);
				msg = resultp.get(MainActivity.TAG_MSG);
				msg_id = resultp.get(MainActivity.TAG_ROWID);
				MessageUpdateDelete();
			}
		});

		return itemView;
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void MessageUpdateDelete() {
		final ArrayList<String> message_details = new ArrayList<String>();
		message_details.add("Update Message");
		message_details.add("Delete Message");
		final AlertDialog alertDialog = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Update/Delete " + "\"" + msg + "\"" + " ?");
		LayoutInflater inflater = ((MainActivity) context).getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.location_list, null);
		alertDialog.setView(convertView);

		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.city_list_items, R.id.city_text, message_details);
		lv.setAdapter(adapter);

		alertDialog.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				alertDialog.dismiss();
				if (position == 0) {
					alertEdit();
				}
				if (position == 1) {
					alertDelete();
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void alertEdit() {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();

		alertDialog.setTitle("Update Message");

		InputFilter[] filter = new InputFilter[1];
		int max_length = 150;
		filter[0] = new InputFilter.LengthFilter(max_length);
		final EditText input = new EditText(context);
		input.setId(0);
		input.setHint("Enter message");

		input.setHintTextColor(context.getResources().getColor(R.color.bg_blue));
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
		input.setLines(10);
		input.setFilters(filter);
		input.setSingleLine(false);
		alertDialog.setView(input);

		alertDialog.setButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String updated_msg = input.getText().toString();
				String currentDateString = DateFormat.getDateInstance().format(
						new Date());
				alertDialog.dismiss();

				((MainActivity) context).GetMessageDetails(msg_id,
						updated_msg.toString(), currentDateString);
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertDelete() {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();

		alertDialog.setTitle("Delete Message " + "\"" + msg + "\"" + " ?");

		alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				((MainActivity) context).GetMessageId(msg_id);
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}
}
