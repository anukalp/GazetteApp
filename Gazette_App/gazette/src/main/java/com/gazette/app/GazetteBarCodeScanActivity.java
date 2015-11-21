package com.gazette.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gazette.app.fragments.ProductDetailsFillFragment;
import com.gazette.app.fragments.ProductInvoiceScanFragment;
import com.gazette.app.fragments.ProductScanBarCodeFragment;
import com.gazette.app.fragments.ProductWarrantyFragment;
import com.gazette.app.model.Product;


public class GazetteBarCodeScanActivity extends GazetteBaseActivity {
    private android.support.v7.widget.Toolbar mToolbar;
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Product mProduct;
    private static final int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazette_bar_code_scan);
        mProduct = new Product();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.ifl_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ProductScanBarCodeFragment();
                case 1:
                    return new ProductDetailsFillFragment();
                case 2:
                    return new ProductInvoiceScanFragment();
                case 3:
                    return new ProductWarrantyFragment();
            }
            return new ProductScanBarCodeFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void moveToNextPage(int position) {
        mPager.setCurrentItem(position);
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Anil","onActivityResult requestCode "+requestCode);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                Image image = new Image();
                image.setBitmap(bitmap);
                mProduct.getImages().add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public Product getmProduct() {
        return mProduct;
    }

}
