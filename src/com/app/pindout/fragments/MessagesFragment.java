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
import com.app.pindout.greetingmsg.AddNewMessageActivity;

public class MessagesFragment extends Fragment {

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
		View rootView = inflater.inflate(R.layout.messages_fragment, container,
				false);
		activity = (MainActivity) getActivity();
		FloatingActionButton add_msgfab = (FloatingActionButton) rootView
				.findViewById(R.id.addmsg_fab);
		add_msgfab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int fab_position = activity.sendFabPosition();
				if (fab_position == 1) {
					Intent intent2 = new Intent(getActivity(),
							AddNewMessageActivity.class);
					intent2.putExtra("business_id", activity.sendBusinessId());
					intent2.putExtra("is_from_addnewmsg", true);
					startActivity(intent2);
				}
			}
		});
		return rootView;
	}
}
