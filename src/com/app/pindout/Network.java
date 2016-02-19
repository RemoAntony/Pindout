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
public class Network extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.networktab,container,false);
        return v;
    }
    @Override
    public  void onStart()
    {
        try {
            super.onStart();
            DashBoard act = (DashBoard) getActivity();
            //ArrayList<View> allv = act.disNetwork();
           ArrayList<String> followers=act.getFollowersname();
            ArrayList<String> following=act.getFollowingname();

            LinearLayout lv1 = (LinearLayout) getActivity().findViewById(R.id.followerstab);
            LinearLayout lv2 = (LinearLayout) getActivity().findViewById(R.id.followingtab);

            int nfollowers=followers.size();
            int nfollowing=following.size();

            TextView follcount=(TextView) getActivity().findViewById(R.id.followerscount);
            follcount.setText("Followers : "+String.valueOf(nfollowers));

            TextView follingcount=(TextView) getActivity().findViewById(R.id.followingcount);
            follingcount.setText("Following : "+String.valueOf(nfollowing));

            if(nfollowers==0)
            {
                TextView tv=new TextView(getActivity());
                tv.setText("You have no Followers");
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                lv1.addView(tv);
            }
            else
            {
                for(int i=0;i<followers.size();i++)
                {
                    TextView tv=new TextView(getActivity());
                    tv.setText((i+1)+". "+followers.get(i));
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    lv1.addView(tv);
                }
            }

            if(nfollowing==0)
            {
                TextView tv=new TextView(getActivity());
                tv.setText("You are not Following Anyone");
                lv2.addView(tv);
            }
            else
            {
                for(int i=0;i<following.size();i++)
                {
                    TextView tv=new TextView(getActivity());
                    tv.setText((i+1)+". "+following.get(i));
                    lv2.addView(tv);
                }
            }

        }catch (Exception e){
            Log.d("Profile", "" + e);
        }
    }
}