package com.gazette.app;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gazette.app.fragments.GazetteProductDetailFragment;

/**
 * An activity representing a single Product detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 */
public class GazetteProductDetailActivity extends AppCompatActivity {

    private GazetteProductDetailFragment fragment = null;
    private boolean chatVisible = false;
    private AppBarLayout mAppBarLayout = null;
    private CoordinatorLayout coordinatorLayout = null;
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.productcoordinatorLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != fragment) {
                    if (!chatVisible) {
                        fragment.lunchChatview();
                        chatVisible = true;
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
                        if (behavior != null) {
                            behavior.onNestedFling(coordinatorLayout, mAppBarLayout, null, 0, 10000, true);
                        }
                    } else {
                        fragment.hideChatview();
                        chatVisible = false;
                    }
                }
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(GazetteProductDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(GazetteProductDetailFragment.ARG_ITEM_ID));
            fragment = new GazetteProductDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();
        }
    }


}
