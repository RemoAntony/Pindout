package com.app.pindout;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class ConfirmCatDataAdapter extends
		RecyclerView.Adapter<ConfirmCatDataAdapter.ViewHolder> {
	public static String[] mDataset;

	public ConfirmCatDataAdapter(String[] myDataset) {
		mDataset = myDataset;
	}

	@Override
	public ConfirmCatDataAdapter.ViewHolder onCreateViewHolder(
			ViewGroup parent, int viewType) {

		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.cat_confirm_row, null);

		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {

		viewHolder.tvtinfo_text.setText(mDataset[position].toString());

	}

	@Override
	public int getItemCount() {
		return mDataset.length;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {

		public TextView tvtinfo_text;

		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);
			itemLayoutView.setOnClickListener(this);
			itemLayoutView.setOnLongClickListener((OnLongClickListener) this);
			tvtinfo_text = (TextView) itemLayoutView
					.findViewById(R.id.info_text);

		}

		@SuppressWarnings({ "deprecation" })
		@Override
		public void onClick(View v) {
			Toast.makeText(
					v.getContext(),
					"Position is: " + Integer.toString(getPosition()) + "  "
							+ mDataset[getPosition()], Toast.LENGTH_SHORT)
					.show();
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean onLongClick(View v) {
			Toast.makeText(
					v.getContext(),
					"On LOng Click Position is: "
							+ Integer.toString(getPosition()) + "  "
							+ mDataset[getPosition()], Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}
}