package com.erginus.klips.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.erginus.klips.Fragments.FavImagesFragment;
import com.erginus.klips.Fragments.FavQuotesFragment;
import com.erginus.klips.Fragments.FavSongsFragment;
import com.erginus.klips.Fragments.FavVideosFragment;
import com.erginus.klips.Fragments.PlayListSongsFragment;
import com.erginus.klips.Fragments.PlayListVideosFragment;

import java.util.List;

/**
 * Created by paramjeet on 1/9/15.
 */
public class MyFavouritesAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    List<Fragment> NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public MyFavouritesAdapter(FragmentManager fm, CharSequence mTitles[], List<Fragment> mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {


        if(position == 0) // if the position is 0 we are returning the First tab
        {
            FavSongsFragment fragment=new FavSongsFragment();
            return fragment;


        }

        if(position == 1) // if the position is 0 we are returning the First tab
        {
            FavVideosFragment fragment=new FavVideosFragment();
            return  fragment;

        }
        if(position == 2) // if the position is 0 we are returning the First tab
        {
            FavImagesFragment fragment=new FavImagesFragment();
            return  fragment;

        }
       else// if the position is 0 we are returning the First tab
        {
            FavQuotesFragment fragment=new FavQuotesFragment();
            return  fragment;
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
        return 4;
    }
}