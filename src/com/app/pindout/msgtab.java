package com.app.pindout;

/**
 * Created by REMO on 20-11-2015.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class msgtab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.msgtablayout,container,false);
        return v;
    }
    @Override
    public  void onStart()
    {
        super.onStart();
        try {
            RemIndUserMain act = (RemIndUserMain) getActivity();
            ArrayList<View> allv1 = act.disBdeal();
            LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.msgtablayout);
            // lv.removeAllViews();
            if (allv1.size() == 0) {
                TextView tv = (TextView) getActivity().findViewById(R.id.notagdeal);
                tv.setText("You have no Tagged Business Deals .");
            } else {
                for (int i = 0; i < allv1.size(); i++) {
                    lv.addView(allv1.get(i));
                    TextView tv = (TextView) getActivity().findViewById(R.id.notagdeal);
                    tv.setText("");
                }
            }
        }catch (Exception e){}
    }

}