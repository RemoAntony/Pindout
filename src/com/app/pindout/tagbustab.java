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

public class tagbustab extends Fragment {
    View v1;
    LinearLayout tagbuslay;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v1 =inflater.inflate(R.layout.tagbuslayout,container,false);
        return v1;
    }
@Override
    public  void onStart()
{
    try {
        super.onStart();
        RemIndUserMain act = (RemIndUserMain) getActivity();
        ArrayList<View> allv = act.disBname();
        LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.tagbuslayout);
        //  lv.removeAllViews();
        if (allv.size() == 0) {
            TextView tv = (TextView) getActivity().findViewById(R.id.notagbus);
            tv.setText("You have no Messages For Today.");
        } else {
            for (int i = 0; i < allv.size(); i++) {
                lv.addView(allv.get(i));
                TextView tv = (TextView) getActivity().findViewById(R.id.notagbus);
                tv.setText("");
            }
        }
    }catch (Exception e){}
}


}