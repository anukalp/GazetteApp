package com.gazette.app.fragments;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gazette.app.R;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class ProductScanBarCodeFragment extends Fragment {
    private CompoundBarcodeView barcodeScannerView;
    private boolean flashToggle = false;
    ImageView imageView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_product_barcodescan, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.flash_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashlight();
            }
        });
        if (!hasFlash()) {
            fab.setVisibility(View.GONE);
        }
        barcodeScannerView = (CompoundBarcodeView) rootView.findViewById(R.id.zxing_barcode_scanner);
        imageView = (ImageView) rootView.findViewById(R.id.barcodePreview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        triggerScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }


    public void triggerScan() {
        barcodeScannerView.decodeSingle(callback);
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getActivity().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (!flashToggle) {
            barcodeScannerView.setTorchOn();
            flashToggle = true;
        } else {
            barcodeScannerView.setTorchOff();
            flashToggle = false;
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getActivity().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                //barcodeScannerView.setStatusText(result.getText());
                barcodeScannedConfimation(result.getText());
            }
            //Added preview of scanned barcode
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public boolean barcodeScannedConfimation(String barcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.alertdialog_style);
        builder.setTitle(getString(R.string.barcode_scanned));
        builder.setMessage(barcode);
        builder.setPositiveButton(getString(R.string.scan_again),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        triggerScan();
                    }
                });
        builder.setNegativeButton(getString(R.string.thats_right), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked OK so do some stuff */
            }
        });
        builder.show();
        return false;
    }

}
