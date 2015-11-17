package com.gazette.app;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

public class GazetteBarCodeScanActivity extends GazetteBaseActivity {
    private CompoundBarcodeView barcodeScannerView;
    private android.support.v7.widget.Toolbar mToolbar;
    private boolean flashToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
            return;
        }

        setContentView(R.layout.activity_gazette_bar_code_scan);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.flash_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashlight();
            }
        });
        if (!hasFlash()) {
            fab.setVisibility(View.GONE);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        barcodeScannerView = (CompoundBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        triggerScan();
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
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
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
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
        return getApplicationContext().getPackageManager()
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

    public boolean barcodeScannedConfimation(String barcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
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
