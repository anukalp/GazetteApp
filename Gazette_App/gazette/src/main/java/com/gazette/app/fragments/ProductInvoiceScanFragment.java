package com.gazette.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gazette.app.GazetteApplication;
import com.gazette.app.GazetteBarCodeScanActivity;
import com.gazette.app.R;
import com.gazette.app.model.Image;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class ProductInvoiceScanFragment extends Fragment {
    private Button cameraButton;
    private Button mediaButton;
    private ImageView scannedImageView;
    private static final int REQUEST_CODE = 99;
    private TextView skip_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_product_invoice_scan, container, false);
        cameraButton = (Button) rootView.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));
        mediaButton = (Button) rootView.findViewById(R.id.mediaButton);
        mediaButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_MEDIA));
        scannedImageView = (ImageView) rootView.findViewById(R.id.invoice_view);
        skip_button = (TextView) rootView.findViewById(R.id.skip_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GazetteBarCodeScanActivity) getActivity()).moveToNextPage(3);
            }
        });
        return rootView;
    }

    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            startScan(preference);
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(getActivity(), ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Anil", "onActivityResult requestCode " + requestCode);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Log.i("Anil", "onActivityResult uri " + uri.toString());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                getActivity().getContentResolver().delete(uri, null, null);
                Image image = new Image();
                image.setBitmap(bitmap);
                ((GazetteBarCodeScanActivity) getActivity()).getmProduct().setProductInvoice(image);
                GazetteApplication.getInstance().notifyAllProductScannerListener();
                scannedImageView.setImageBitmap(bitmap);
                ((GazetteBarCodeScanActivity) getActivity()).moveToNextPage(3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }


}
