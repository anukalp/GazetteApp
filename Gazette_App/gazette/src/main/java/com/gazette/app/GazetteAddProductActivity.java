package com.gazette.app;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gazette.app.fragments.adapters.SubProductAdapter;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.utils.GazetteConstants;

import java.util.ArrayList;
import java.util.List;

public class GazetteAddProductActivity extends GazetteBaseActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SubProductAdapter mAdapter;
    private List<Product> products = null;
    private int mProduct_type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null != getIntent().getExtras()) {
            mProduct_type = getIntent().getExtras().getInt(GazetteConstants.PRODUCT_ID);
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
        products = new ArrayList<Product>();
        String[] Products = null;
        TypedArray images = null;
        switch (mProduct_type) {
            case 0:
                Products = getResources().getStringArray(R.array.smartphone);
                images = getResources().obtainTypedArray(R.array.smartphone_images);
                break;
        }
        if (null != products && null != images) {
            for (int i = 0; i < Products.length; i++) {
                Product product = new Product();
                product.setProductName(Products[i]);
                product.setProductId(i);
                Image image = new Image();
                image.setProductId(product.getProductId());
                image.setSrc(images.getResourceId(i, -1));
                product.setProductImage(image);
                products.add(product);
            }
        }

        mAdapter = new SubProductAdapter(this, products);
    }
}
