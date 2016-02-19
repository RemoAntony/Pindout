package com.app.pindout.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.pindout.R;
import com.app.pindout.businessdeals.DealActivity;
import com.app.pindout.dbadapter.InactiveBusinessDealDBAdapter;

public class InactiveDeals extends Fragment {

	private InactiveBusinessDealDBAdapter inactivedealsdb;

	public InactiveDeals() {

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
		View rootView = inflater.inflate(R.layout.fragment_inactive_deals,
				container, false);

		inactivedealsdb = new InactiveBusinessDealDBAdapter(getActivity());
		ListView listView = (ListView) rootView
				.findViewById(R.id.inactive_dealslist);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position++;
				inactivedealsdb.open();
				Cursor c = inactivedealsdb.get(position);
				Intent intent = new Intent(getActivity(), DealActivity.class);
				intent.putExtra("deal_name", c.getString(2));
				intent.putExtra("deal_cat", c.getString(3));
				intent.putExtra("deal_main_price", c.getString(4));
				intent.putExtra("deal_price", c.getString(5));
				intent.putExtra("deal_desc", c.getString(6));
				intent.putExtra("deal_image", c.getString(7));
				c.close();
				inactivedealsdb.close();
				startActivity(intent);
				getActivity().overridePendingTransition(
						R.anim.right_to_left_enter, R.anim.right_to_left_exit);
			}
		});
		return rootView;
	}
}