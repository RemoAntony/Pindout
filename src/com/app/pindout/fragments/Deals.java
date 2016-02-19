package com.app.pindout.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pindout.MainActivity;
import com.app.pindout.R;
import com.app.pindout.cachingimages.ImageLoader;
import com.app.pindout.helpers.RoundedImageView;

@SuppressLint("ViewHolder")
public class Deals extends Fragment {

	private String business_name = "",
			business_phone = "",
			business_address = "",
			business_area = "",
			stringBusinessLogo = "",
			image_path = "http://pindout.com/files/business_images/main_images/",
			business_desc = "not_set", business_prodnservice = "not_set",
			business_url = "not_set", business_addtext = "not_set";
	private TextView businessname, businessphone, businessaddress,
			businessarea;
	private RoundedImageView ic_business_logo;

	private ArrayList<HashMap<String, String>> text_list;

	public static final String TAG_TEXT = "text";
	public static final String TAG_SHOWTEXT = "show_text";
	private ImageLoader image_Loader;

	private ListViewAdapter listAdapter;

	public Deals() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.my_business_fragment,
				container, false);
		return rootView;

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		image_Loader = new ImageLoader(this.getActivity());
		MainActivity activity = (MainActivity) getActivity();
		business_name = activity.sendBusinessName();
		business_phone = activity.sendBusinessPhone();
		business_address = activity.sendBusinessAddress();
		business_area = activity.sendBusinessAreaName();
		stringBusinessLogo = activity.sendBusinessLogo();
		business_addtext = activity.sendBusinessAddInfo();
		business_desc = activity.sendBusinessDesc();
		business_prodnservice = activity.sendBusinessProd_N_Service();

		business_url = activity.sendBusinessUrl();
		businessname = (TextView) getActivity().findViewById(R.id.businessname);
		businessphone = (TextView) getActivity().findViewById(
				R.id.businessphone);
		businessaddress = (TextView) getActivity().findViewById(
				R.id.businessaddress);
		businessarea = (TextView) getActivity().findViewById(R.id.businessarea);
		ic_business_logo = (RoundedImageView) getActivity().findViewById(
				R.id.business_img);

		text_list = new ArrayList<HashMap<String, String>>();
		text_list.clear();

		businessname.setText(business_name);
		businessphone.setText(business_phone);
		businessaddress.setText(business_address);
		businessarea.setText(business_area);

		if (business_desc.equals("")) {
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("text", "Business Description");
			map.put("show_text", business_desc);
			text_list.add(map);
		}

		if (business_addtext.equals("")) {
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("text", "Additional Info");
			map.put("show_text", business_addtext);
			text_list.add(map);
		}

		if (business_prodnservice.equals("")) {
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("text", "Products & Services");
			map.put("show_text", business_prodnservice);
			text_list.add(map);
		}

		if (business_url.equals("")) {
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("text", "Website");
			map.put("show_text", business_url);
			text_list.add(map);
		}

		if (stringBusinessLogo.equals("")) {

		} else {
			stringBusinessLogo = image_path + stringBusinessLogo;
			image_Loader.DisplayImage(stringBusinessLogo, ic_business_logo);
		}
		ListView listView = (ListView) getActivity().findViewById(
				R.id.list_info);
		listAdapter = new ListViewAdapter(getActivity(), text_list);
		listView.setAdapter(listAdapter);
	}

	public class ListViewAdapter extends BaseAdapter {

		Context context;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> data;
		HashMap<String, String> resultp = new HashMap<String, String>();

		public ListViewAdapter(Context context,
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView text, loaded_text;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView = inflater.inflate(R.layout.landing_info_row, parent,
					false);

			resultp = data.get(position);

			text = (TextView) itemView
					.findViewById(R.id.textbusinessdescription);
			loaded_text = (TextView) itemView
					.findViewById(R.id.businessdescription);

			text.setText(resultp.get(Deals.TAG_TEXT));
			loaded_text.setText(resultp.get(Deals.TAG_SHOWTEXT));

			return itemView;
		}
	}
}