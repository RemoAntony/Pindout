package com.app.pindout;

/**
 * Created by REMO on 20-11-2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.LinearLayout;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when DashViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the DashViewPagerAdapter is created
    LinearLayout tagbuslay;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public MainViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            return new TagBusiness();


        }

        else if(position == 1)           // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            msgtab t2=new msgtab();
            return t2;

        }
        else if(position ==2)       // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            tagbustab t1=new tagbustab();
            //LinearLayout tagbuslay=(LinearLayout)t1.getView().findViewById(R.id.tagbuslayout);
            return  t1;
        }
        else
        {
            Review tab2 = new Review();
            return tab2;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }


}