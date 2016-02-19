package com.app.pindout;

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
 * Created by vms20591 on 29/12/15.
 */
public class TagBusiness extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.taggedbusiness,container,false);
        return v;
    }
    @Override
    public  void onStart()
    {
        try {
            super.onStart();
            RemIndUserMain act = (RemIndUserMain) getActivity();
            ArrayList<View> allv = act.disTagBus();
            LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.taggedbusiness);
            // lv.removeAllViews();
            if (allv.size() == 0) {

                TextView tv = new TextView(getActivity());
                tv.setText(" No Tagged Business ");
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