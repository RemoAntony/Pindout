package com.app.pindout.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pindout.MainActivity;
import com.app.pindout.R;

@SuppressLint({ "ViewHolder", "SimpleDateFormat" })
public class ReviewsAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();

	private String business_review_id = "", group_id = "";

	public ReviewsAdapter(Context context,
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
		TextView review_username, review_msg, review_datentime, review_comment;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.reviews_row, parent, false);

		resultp = data.get(position);

		review_username = (TextView) itemView
				.findViewById(R.id.review_username);
		review_msg = (TextView) itemView.findViewById(R.id.review_msg);
		review_datentime = (TextView) itemView
				.findViewById(R.id.reviewmsgdatentime);
		review_comment = (TextView) itemView.findViewById(R.id.review_comment);

		review_username.setText(resultp.get(MainActivity.TAG_USERNAME));
		review_msg.setText(resultp.get(MainActivity.TAG_REVIEWTEXT));
		review_datentime.setText(resultp.get(MainActivity.TAG_REVIEWDATETIME));
		review_comment.setText(resultp.get(MainActivity.TAG_GET_REPLY_DATA));

		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resultp = data.get(position);
				business_review_id = resultp.get(MainActivity.TAG_REVIEWID);
				group_id = resultp.get(MainActivity.TAG_GROUPID);
				OpenDialog();
			}
		});

		return itemView;
	}

	@SuppressWarnings("deprecation")
	protected void OpenDialog() {
		// TODO Auto-generated method stub
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();

		alertDialog.setTitle("Reply");

		InputFilter[] filter = new InputFilter[1];
		int max_length = 150;
		filter[0] = new InputFilter.LengthFilter(max_length);
		final EditText input = new EditText(context);
		input.setId(0);
		input.setHint("Enter reply");
		input.setHintTextColor(context.getResources().getColor(R.color.bg_blue));
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
		input.setLines(10);
		input.setFilters(filter);
		input.setSingleLine(false);
		alertDialog.setView(input);
		alertDialog.setButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String get_replytext = input.getText().toString();
				if (get_replytext.equals("")) {
					Toast.makeText(context, "Reply text cannot be blank",
							Toast.LENGTH_SHORT).show();
				} else {
					alertDialog.dismiss();
					DateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					String currentDateString = dateFormat.format(date);
					((MainActivity) context).GetReviewReplyDetails(
							business_review_id, "0", group_id, get_replytext,
							currentDateString);

				}
			}
		});
		alertDialog.setButton2("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}
}
