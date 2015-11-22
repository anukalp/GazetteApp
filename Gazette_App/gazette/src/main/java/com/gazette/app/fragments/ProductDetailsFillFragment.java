package com.gazette.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    private Spinner category_spinner;

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
        category_spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        String[] category = getActivity().getResources().getStringArray(R.array.products);
        ArrayAdapter<String> category_spinner_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_item, category);
        category_spinner_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_spinner_adapter);

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
            Log.i("Anil", " getmProduct not null " + ((GazetteBarCodeScanActivity) getActivity()).getmProduct().getProductBarCode());
            product_barcode.setText(((GazetteBarCodeScanActivity) getActivity()).getmProduct().getProductBarCode());
            product_name.setText(((GazetteBarCodeScanActivity) getActivity()).getmProduct().getProductName());
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
