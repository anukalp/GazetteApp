package com.gazette.app;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gazette.app.fragments.adapters.SubProductAdapter;
import com.gazette.app.utils.GazetteConstants;

public class GazetteAddProductActivity extends GazetteBaseActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SubProductAdapter mAdapter;
    private String mProduct_type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null != getIntent().getExtras()) {
            mProduct_type = getIntent().getExtras().getString(GazetteConstants.PRODUCT_ID);
            Log.i("Anil", "getProductId :" + mProduct_type);
            _init();
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_add_product);

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
        }
    }


    private void _init() {
        mAdapter = new SubProductAdapter(this, mProduct_type);
    }
}
