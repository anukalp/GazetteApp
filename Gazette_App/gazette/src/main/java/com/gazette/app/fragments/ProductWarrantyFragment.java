package com.gazette.app.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gazette.app.R;
import com.gazette.app.callbacks.ProductScannerListener;
import com.gazette.app.utils.GazetteUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class ProductWarrantyFragment extends Fragment implements ProductScannerListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private Spinner warranty_period_spinner;
    private Spinner warranty_provider_spinner;
    private EditText purchase_date;
    private Button date_picker;

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
                dpd.showYearPickerFirst(true);
                if (true) {
                    dpd.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                }
                if (true) {
                    dpd.setTitle("DatePicker Title");
                }
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        return rootView;
    }

    @Override
    public void OnProductInfoUpdate() {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"-"+(++monthOfYear)+"-"+year;
        purchase_date.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }
}
