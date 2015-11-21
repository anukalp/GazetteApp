package com.gazette.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gazette.app.GazetteApplication;
import com.gazette.app.GazetteBarCodeScanActivity;
import com.gazette.app.R;
import com.gazette.app.callbacks.ProductScannerListener;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class ProductDetailsFillFragment extends Fragment implements ProductScannerListener {
    private EditText product_barcode;
    private EditText product_brand;
    private EditText product_name;
    private EditText product_serial;
    private Button nextBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_product_details, container, false);
        product_barcode = (EditText) rootView.findViewById(R.id.product_barcode);
        product_brand = (EditText) rootView.findViewById(R.id.product_brand);
        product_name = (EditText) rootView.findViewById(R.id.product_name);
        product_serial = (EditText) rootView.findViewById(R.id.product_serial);
        nextBtn = (Button) rootView.findViewById(R.id.nextBtn);
        GazetteApplication.getInstance().addProductScannerListener(this);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GazetteBarCodeScanActivity) getActivity()).moveToNextPage(2);
            }
        });
        return rootView;
    }

    private void init() {
        Log.i("Anil", " init ");
        if ((null != ((GazetteBarCodeScanActivity) getActivity()).getmProduct())) {
            Log.i("Anil", " getmProduct not null " + ((GazetteBarCodeScanActivity) getActivity()).getmProduct().getProductCode());
            product_barcode.setText(((GazetteBarCodeScanActivity) getActivity()).getmProduct().getProductCode());
        }

    }

    @Override
    public void OnProductInfoUpdate() {
        Log.i("Anil", " OnProductInfoUpdate ");
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GazetteApplication.getInstance().removeProductScannerListener(this);
    }
}
