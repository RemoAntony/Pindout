package com.app.pindout.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.pindout.MainActivity;
import com.app.pindout.R;
import com.app.pindout.businessdeals.DealActivity;
import com.app.pindout.cachingimages.ImageLoader;

@SuppressLint("ViewHolder")
public class BusinessDealsAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public BusinessDealsAdapter(Context context,
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
		TextView dealname, main_price, deal_price;
		ImageView deal_img;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.business_deals_row, parent,
				false);

		resultp = data.get(position);

		RelativeLayout deals_row_lay = (RelativeLayout) itemView
				.findViewById(R.id.deals_row_layout);

		if (resultp.get(MainActivity.TAG_STATUS).equals("0")
				|| resultp.get(MainActivity.TAG_ADMINAPPROVAL).equals("0")) {
			deals_row_lay.setBackgroundColor(context.getResources().getColor(
					R.color.VioletRed));
		} else {
			deals_row_lay.setBackgroundColor(context.getResources().getColor(
					android.R.color.holo_green_light));
		}

		dealname = (TextView) itemView.findViewById(R.id.deal_name);
		main_price = (TextView) itemView.findViewById(R.id.showmain_price);
		deal_price = (TextView) itemView.findViewById(R.id.showdeal_price);
		// cat_name = (TextView) itemView.findViewById(R.id.deal_cat);
		deal_img = (ImageView) itemView.findViewById(R.id.deal_img);
		// deal_desc = (TextView) itemView.findViewById(R.id.deal_desc);

		dealname.setText(resultp.get(MainActivity.TAG_DEALNAME));
		main_price.setText(resultp.get(MainActivity.TAG_MAINPRICE));
		deal_price.setText(resultp.get(MainActivity.TAG_DEALPRICE));
		// cat_name.setText(resultp.get(MainActivity.TAG_CATNAME));
		// deal_desc.setText(resultp.get(MainActivity.TAG_DEALDESC));

		imageLoader.DisplayImage(resultp.get(MainActivity.TAG_DEALIMG),
				deal_img);

		deal_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resultp = data.get(position);
				String deal_img = resultp.get(MainActivity.TAG_DEALIMG);
				DealImageDialog(deal_img);
			}
		});

		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resultp = data.get(position);
				Intent intent = new Intent(context, DealActivity.class);
				intent.putExtra("deal_id",
						resultp.get(MainActivity.TAG_DEAL_ID));
				intent.putExtra("deal_name",
						resultp.get(MainActivity.TAG_DEALNAME));
				intent.putExtra("deal_cat",
						resultp.get(MainActivity.TAG_CATNAME));
				intent.putExtra("deal_main_price",
						resultp.get(MainActivity.TAG_MAINPRICE));
				intent.putExtra("deal_price",
						resultp.get(MainActivity.TAG_DEALPRICE));
				intent.putExtra("deal_url",
						resultp.get(MainActivity.TAG_DEALURL));
				intent.putExtra("deal_status",
						resultp.get(MainActivity.TAG_STATUS));
				intent.putExtra("deal_adminapproval",
						resultp.get(MainActivity.TAG_ADMINAPPROVAL));
				intent.putExtra("deal_desc",
						resultp.get(MainActivity.TAG_DEALDESC));
				intent.putExtra("deal_image",
						resultp.get(MainActivity.TAG_DEALIMG));
				context.startActivity(intent);
			}
		});

		return itemView;
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void DealImageDialog(String deal_img) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT).create();
		LayoutInflater inflater = ((MainActivity) context).getLayoutInflater();
		View convertView = (View) inflater.inflate(R.layout.deal_img_dialog,
				null);
		alertDialog.setView(convertView);

		ImageView dealimg = (ImageView) convertView.findViewById(R.id.deal_img);
		ImageView close_deal_img = (ImageView) convertView
				.findViewById(R.id.close_deal_img);

		imageLoader.DisplayImage(deal_img, dealimg);

		close_deal_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});

		alertDialog.show();
	}
}
