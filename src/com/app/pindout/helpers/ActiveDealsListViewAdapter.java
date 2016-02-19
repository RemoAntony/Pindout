package com.app.pindout.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.pindout.MainActivity;
import com.app.pindout.R;
import com.app.pindout.cachingimages.ImageLoader;

@SuppressLint("ViewHolder")
public class ActiveDealsListViewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ActiveDealsListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
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

	@SuppressWarnings("deprecation")
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView dealname, main_price, deal_price, cat_name, deal_desc;
		ImageView deal_img;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.active_businessdeals_row,
				parent, false);

		resultp = data.get(position);

		RelativeLayout deals_row_lay = (RelativeLayout) itemView
				.findViewById(R.id.deals_row_layout);

		if (resultp.get(MainActivity.TAG_STATUS).equals("0")
				|| resultp.get(MainActivity.TAG_ADMINAPPROVAL).equals("0")) {
			deals_row_lay.setBackgroundColor(context.getResources().getColor(
					R.color.VioletRed));
		} else {
			deals_row_lay.setBackgroundColor(context.getResources().getColor(
					R.color.greenish_white));
		}

		dealname = (TextView) itemView.findViewById(R.id.deal_name);
		main_price = (TextView) itemView.findViewById(R.id.showmain_price);
		deal_price = (TextView) itemView.findViewById(R.id.showdeal_price);
		cat_name = (TextView) itemView.findViewById(R.id.deal_cat);
		deal_img = (ImageView) itemView.findViewById(R.id.deal_img);
		deal_desc = (TextView) itemView.findViewById(R.id.deal_desc);

		main_price.setPaintFlags(main_price.getPaintFlags()
				| Paint.STRIKE_THRU_TEXT_FLAG);

		dealname.setText(resultp.get(MainActivity.TAG_DEALNAME));
		main_price.setText(context.getResources().getString(
				R.string.rupee_symbol)
				+ " " + resultp.get(MainActivity.TAG_MAINPRICE));
		deal_price.setText(context.getResources().getString(
				R.string.rupee_symbol)
				+ " " + resultp.get(MainActivity.TAG_DEALPRICE));
		cat_name.setText(resultp.get(MainActivity.TAG_CATNAME));
		deal_desc.setText(resultp.get(MainActivity.TAG_DEALDESC));

		imageLoader.DisplayImage(resultp.get(MainActivity.TAG_DEALIMG),
				deal_img);

		return itemView;
	}
}