package com.app.pindout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by LALLIVISWA on 12/19/2015.
 */
public class BusinessPageAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when DashViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the DashViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public BusinessPageAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
           return new Business_deals();
        }
       else if(position == 1) // if the position is 0 we are returning the First tab
        {
            return new Business_Message();
        }
       else  // if the position is 0 we are returning the First tab
        {
            return new BusinessReview();
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