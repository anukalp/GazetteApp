package com.gazette.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gazette.app.R;
import com.gazette.app.callbacks.ProductScannerListener;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class ProductWarrantyFragment extends Fragment implements ProductScannerListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_product_warranty, container, false);

        return rootView;
    }

    @Override
    public void OnProductInfoUpdate() {

    }
}
