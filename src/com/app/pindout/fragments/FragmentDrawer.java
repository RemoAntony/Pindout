package com.app.pindout.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pindout.R;
import com.app.pindout.adapter.NavigationDrawerAdapter;
import com.app.pindout.cachingimages.ImageLoader;
import com.app.pindout.helpers.RoundedImageView;
import com.app.pindout.model.NavDrawerItem;

public class FragmentDrawer extends Fragment {

	private RecyclerView recyclerView;
	private RoundedImageView ic_profile;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private NavigationDrawerAdapter adapter;
	private View containerView;
	private static String[] titles = null;
	private FragmentDrawerListener drawerListener;
	private String BusinessName = "", Business_logo = "";
	private ImageLoader image_loader;

	public static final String BusinessNamePrefs = "BusinessNamePrefs";
	public static final String strBusinessNameSet = "0";
	public SharedPreferences businessnameprefs;

	public static final String BusinessLogoPrefs = "BusinessLogoPrefs";
	public static final String strBusinessLogoSet = "0";
	public SharedPreferences businesslogoprefs;

	public FragmentDrawer() {

	}

	public void setDrawerListener(FragmentDrawerListener listener) {
		this.drawerListener = listener;
	}

	public static List<NavDrawerItem> getData() {
		List<NavDrawerItem> data = new ArrayList<NavDrawerItem>();

		// preparing navigation drawer items
		for (int i = 0; i < titles.length; i++) {
			NavDrawerItem navItem = new NavDrawerItem();
			navItem.setTitle(titles[i]);
			data.add(navItem);
		}
		return data;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// drawer labels
		businessnameprefs = this.getActivity().getSharedPreferences(
				BusinessNamePrefs, Context.MODE_PRIVATE);
		businesslogoprefs = this.getActivity().getSharedPreferences(
				BusinessLogoPrefs, Context.MODE_PRIVATE);
		titles = getActivity().getResources().getStringArray(
				R.array.nav_drawer_labels);
		image_loader = new ImageLoader(this.getActivity());
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflating view layout
		View layout = inflater.inflate(R.layout.fragment_navigation_drawer,
				container, false);
		recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
		ic_profile = (RoundedImageView) layout
				.findViewById(R.id.ic_profile_pic);
		if (businessnameprefs.contains(strBusinessNameSet)) {
			BusinessName = businessnameprefs.getString(strBusinessNameSet, "0");
		}
		if (businesslogoprefs.contains(strBusinessLogoSet)) {
			Business_logo = businesslogoprefs
					.getString(strBusinessLogoSet, "0");
		}
		TextView drawer_businessname = (TextView) layout
				.findViewById(R.id.drawer_businessname);
		drawer_businessname.setText(BusinessName);

		if (Business_logo.equals("")) {
			ic_profile.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ic_business_black_24dp));
		} else {
			image_loader.DisplayImage(
					"http://pindout.com/files/business_images/main_images/"
							+ Business_logo, ic_profile);
		}
		adapter = new NavigationDrawerAdapter(getActivity(), getData());
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
				getActivity(), recyclerView, new ClickListener() {
					@Override
					public void onClick(View view, int position) {
						drawerListener.onDrawerItemSelected(view, position);
						mDrawerLayout.closeDrawer(containerView);
					}

					@Override
					public void onLongClick(View view, int position) {

					}
				}));

		return layout;
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout,
			final Toolbar toolbar) {
		containerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
				toolbar, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				toolbar.setAlpha(1 - slideOffset / 2);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

	}

	public static interface ClickListener {
		public void onClick(View view, int position);

		public void onLongClick(View view, int position);
	}

	static class RecyclerTouchListener implements
			RecyclerView.OnItemTouchListener {

		private GestureDetector gestureDetector;
		private ClickListener clickListener;

		public RecyclerTouchListener(Context context,
				final RecyclerView recyclerView,
				final ClickListener clickListener) {
			this.clickListener = clickListener;
			gestureDetector = new GestureDetector(context,
					new GestureDetector.SimpleOnGestureListener() {
						@Override
						public boolean onSingleTapUp(MotionEvent e) {
							return true;
						}

						@SuppressWarnings("deprecation")
						@Override
						public void onLongPress(MotionEvent e) {
							View child = recyclerView.findChildViewUnder(
									e.getX(), e.getY());
							if (child != null && clickListener != null) {
								clickListener.onLongClick(child,
										recyclerView.getChildPosition(child));
							}
						}
					});
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

			View child = rv.findChildViewUnder(e.getX(), e.getY());
			if (child != null && clickListener != null
					&& gestureDetector.onTouchEvent(e)) {
				clickListener.onClick(child, rv.getChildPosition(child));
			}
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) {
		}

		@Override
		public void onRequestDisallowInterceptTouchEvent(
				boolean disallowIntercept) {

		}

	}

	public interface FragmentDrawerListener {
		public void onDrawerItemSelected(View view, int position);
	}
}
