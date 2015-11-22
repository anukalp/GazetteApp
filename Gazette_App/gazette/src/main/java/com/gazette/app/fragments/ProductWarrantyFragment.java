package com.gazette.app.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.gazette.app.model.Product;
import com.gazette.app.utils.GazeteDBUtils;
import com.gazette.app.utils.GazetteUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class ProductWarrantyFragment extends Fragment implements ProductScannerListener,
        DatePickerDialog.OnDateSetListener {
    private Spinner warranty_period_spinner;
    private Spinner warranty_provider_spinner;
    private EditText product_price;
    private EditText place_of_purchase;
    private EditText purchase_date;
    private Button date_picker;
    private Button Done_btn;
    private UIHandler uiHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_product_warranty, container, false);

        warranty_period_spinner = (Spinner) rootView.findViewById(R.id.warranty_period);
        String[] warranty_period = getActivity().getResources().getStringArray(R.array.warranty_period);
        ArrayAdapter<String> warranty_period_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_item, warranty_period);
        warranty_period_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        warranty_period_spinner.setAdapter(warranty_period_adapter);

        warranty_provider_spinner = (Spinner) rootView.findViewById(R.id.warranty_provider);
        String[] warranty_provider = getActivity().getResources().getStringArray(R.array.warranty_provider);
        ArrayAdapter<String> warranty_provider_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_item, warranty_provider);
        warranty_provider_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        warranty_provider_spinner.setAdapter(warranty_provider_adapter);

        purchase_date = (EditText) rootView.findViewById(R.id.purchase_date);
        purchase_date.setText(GazetteUtils.gettodatDate() + " " + getActivity().getResources().getString(R.string.today));

        product_price = (EditText) rootView.findViewById(R.id.price);
        place_of_purchase = (EditText) rootView.findViewById(R.id.place_of_purchase);

        Done_btn = (Button) rootView.findViewById(R.id.done_btn);
        Done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
        date_picker = (Button) rootView.findViewById(R.id.date_picker);
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ProductWarrantyFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.showYearPickerFirst(false);
                dpd.setMaxDate(now);
                if (true) {
                    dpd.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                }
                if (true) {
                    dpd.setTitle("Purchase Date");
                }
                dpd.show(getActivity().getFragmentManager(), getActivity().getResources().getString(R.string.app_name));
            }
        });
        uiHandler = new UIHandler();
        return rootView;
    }

    @Override
    public void OnProductInfoUpdate() {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
        purchase_date.setText(date);
    }


    private void saveProduct() {
        // Reset errors.
        String purchasedate = purchase_date.getText().toString();
        String warranty_period = warranty_period_spinner.getSelectedItem().toString();
        String warranty_provider = warranty_provider_spinner.getSelectedItem().toString();
        String price = product_price.getText().toString();
        String placeofpurchase = place_of_purchase.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(placeofpurchase)) {
            place_of_purchase.setError(getString(R.string.error_field_required));
            focusView = place_of_purchase;
            cancel = true;
        }

        if (warranty_provider.equalsIgnoreCase("select warranty provider")) {
            Snackbar.make(warranty_provider_spinner, "Please select warranty provider", Snackbar.LENGTH_SHORT).show();
            cancel = true;
        }


        if (cancel) {
            if (null != focusView)
                focusView.requestFocus();
        } else {
            Product product = ((GazetteBarCodeScanActivity) getActivity()).getmProduct();
            product.setProductVendor(placeofpurchase);
            product.setProductPrice(price);
            product.setProductWarrantyDuration(warranty_period);
            product.setProductWarrantyProvider(warranty_provider);
            product.setProductPurchaseDate(purchasedate);
            GazetteApplication.getInstance().notifyAllProductScannerListener();
            //Save in DB
            Uri producturi = GazeteDBUtils.persistProduct(getActivity(), product);
            Log.i("Anil", "producturi :" + producturi.toString());
            if (null != producturi) {
                Snackbar.make(purchase_date, "Product saved!", Snackbar.LENGTH_SHORT).show();
                GazetteApplication.getInstance().notifyAllonProductAddedListenerr();
                uiHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    }

    class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getActivity().finish();
                    break;
            }
        }
    }

}
