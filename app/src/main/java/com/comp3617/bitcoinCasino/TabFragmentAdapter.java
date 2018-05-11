package com.comp3617.bitcoinCasino;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabFragmentAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;
    public static String tabTitles[] = new String[] {"Asset", "Coins", "Charts",  "News" };
    private Context context;

    public TabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment_Asset tabFragment_asset  = new TabFragment_Asset();
                return tabFragment_asset;
            case 1:
                TabFragment_Coins tabFragment_coins = new TabFragment_Coins();
                return tabFragment_coins;
            case 2:
                TabFragment_Charts tabFragment_charts = new TabFragment_Charts();
                return tabFragment_charts;
            case 3:
                TabFragment_News tabFragment_news = new TabFragment_News();
                return tabFragment_news;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
