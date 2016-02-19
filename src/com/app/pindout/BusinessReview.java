package com.app.pindout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by LALLIVISWA on 12/19/2015.
 */
public class BusinessReview extends Fragment {
    EditText t;
    BusinessPage act;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v3 = inflater.inflate(R.layout.business_review, container, false);

        return v3;
    }
    @Override
    public  void onStart()
    {
        super.onStart();
        try {
            act = (BusinessPage) getActivity();
            ArrayList<View> allv1 = act.disReview();
            LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.business_review);
            Button b=(Button) lv.findViewById(R.id.ar);

            final Context context =act;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  Toast.makeText(act.getApplicationContext(),but_name,Toast.LENGTH_SHORT).show();
                    final String but_name=act.getName();
                    if(but_name.equals("Tag"))
                    {
                        Toast.makeText(act.getApplicationContext(),"Tag To add review",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.reviewdialog);
                        dialog.setTitle("Review");

                        // set the custom dialog components - text, image and button

                        t = (EditText) dialog.findViewById(R.id.text1);

                        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonCan);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
                        // if button is clicked, close the custom dialog
                        dialogButton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String t1 = t.getText().toString();
                                if(!TextUtils.isEmpty(t1))
                                    act.Add_Review(t1);
                                else
                                    Toast.makeText(context,"Review cant be empty",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                    }
                }
            });
            // lv.removeAllViews();
            if (allv1.size() == 0)
            {
               // Toast.makeText(act.getApplicationContext(),"NoReviews",Toast.LENGTH_SHORT).show();
                TextView tv = (TextView) getActivity().findViewById(R.id.reviewtxt);
                tv.setText("No reviews");
                tv.setVisibility(View.VISIBLE);
            }
        else {

        for (int i = 0; i < allv1.size(); i++) {
                    lv.addView(allv1.get(i));
                    TextView tv = (TextView) getActivity().findViewById(R.id.reviewtxt);
                    tv.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){}
    }

}

