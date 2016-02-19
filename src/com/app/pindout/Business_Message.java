package com.app.pindout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LALLIVISWA on 12/19/2015.
 */
public class Business_Message extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v3 = inflater.inflate(R.layout.business_message, container, false);

        return v3;
    }
    @Override
    public  void onStart()
    {
        super.onStart();
        try {
            BusinessPage act = (BusinessPage) getActivity();
            ArrayList<View> allv1 = act.disMsg();
            LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.business_message);
            // lv.removeAllViews();
            if (allv1.size() == 0) {
                TextView tv = (TextView) getActivity().findViewById(R.id.notagdeal1);
                tv.setText("You have no Messages .");
            } else {
                for (int i = 0; i < allv1.size(); i++) {
                    lv.addView(allv1.get(i));
                    TextView tv = (TextView) getActivity().findViewById(R.id.notagdeal1);
                    tv.setText("");
                }
            }
        }catch (Exception e){}
    }
}
