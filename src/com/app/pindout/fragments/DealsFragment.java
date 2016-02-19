package com.app.pindout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pindout.MainActivity;
import com.app.pindout.R;
import com.app.pindout.businessdeals.AddDealActivity;

public class DealsFragment extends Fragment {
	private MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.deals_fragment, container,
				false);
		activity = (MainActivity) getActivity();
		FloatingActionButton add_dealfab = (FloatingActionButton) rootView
				.findViewById(R.id.adddeal_fab);
		add_dealfab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int fab_position = activity.sendFabPosition();
				if (fab_position == 0) {
					Intent intent = new Intent(getActivity(),
							AddDealActivity.class);
					intent.putExtra("business_id", activity.sendBusinessId());
					startActivity(intent);
				}
			}
		});
		return rootView;

	}
}
