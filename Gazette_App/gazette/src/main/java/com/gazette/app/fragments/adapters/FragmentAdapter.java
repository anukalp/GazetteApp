package com.gazette.app.fragments.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gazette.app.fragments.ProductListFragment;
import com.gazette.app.utils.GazetteConstants;

import java.util.List;


public class FragmentAdapter extends FragmentPagerAdapter {

    private List<String> mCollections;

    public FragmentAdapter(Activity activity, FragmentManager fm,
                           List<String> titles) {
        super(fm);
        mCollections = titles;
    }

    @Override
    public Fragment getItem(int position) {
        ProductListFragment aProductListFragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(GazetteConstants.COLLECTION_ID, mCollections.get(position));
        aProductListFragment.setArguments(args);
        return aProductListFragment;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCollections.get(position);
    }
}
