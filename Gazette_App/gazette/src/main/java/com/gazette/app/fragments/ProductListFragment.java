package com.gazette.app.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.gazette.app.R;
import com.gazette.app.fragments.adapters.ProductAdapter;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.utils.DividerItemDecoration;
import com.gazette.app.utils.GazetteConstants;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductAdapter mAdapter;
    private String mColletion_ID = null;
    protected boolean isFetching = false;
    private ProgressDialog progressDialog;
    private List<Product> products = null;

    private static String TAG = ProductListFragment.class
            .getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColletion_ID = (String) getArguments().get(GazetteConstants.COLLECTION_ID);
        _init();
        initializeProgressDialog();
    }

    private void _init() {
        products = new ArrayList<Product>();
        String[] Products = getActivity().getResources().getStringArray(R.array.products);
        TypedArray images = getActivity().getResources().obtainTypedArray(R.array.products_images);
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
        mAdapter = new ProductAdapter(getActivity(), products);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productlist, container,
                false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    public String getColletionID() {
        return mColletion_ID;
    }

    public void setColletionID(String collectionid) {
        this.mColletion_ID = collectionid;
    }

    /**
     * Present the progress dialog.
     *
     * @param messageId The identifier (R.string value) of the string to display in the dialog.
     */
    protected void showLoadingDialog(final int messageId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(getActivity().getString(messageId));
                progressDialog.show();
            }
        });
    }

    protected void dismissLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * Initializes a simple progress dialog that gets presented while the app is communicating with the server.
     */
    private void initializeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }
}
