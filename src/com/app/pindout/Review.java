package com.app.pindout;

/**
 * Created by REMO on 19-11-2015.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Review extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reviewtab,container,false);
        return v;
    }
    @Override
    public  void onStart()
    {
        try {
            super.onStart();
            RemIndUserMain act = (RemIndUserMain) getActivity();
            ArrayList<View> allv = act.disReview();
            LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.reviewtab);
            // lv.removeAllViews();
            if (allv.size() == 0) {

                TextView tv = new TextView(getActivity());
                tv.setText("No Reviews ");
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                lv.addView(tv);
            } else {
                for (int i = 0; i < allv.size(); i++) {
                    lv.addView(allv.get(i));

                }
            }
        }catch (Exception e){
            Log.d("Review", "" + e);
        }
    }
}