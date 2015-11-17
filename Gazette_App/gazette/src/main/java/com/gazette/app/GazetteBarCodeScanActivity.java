package com.gazette.app;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.gazette.app.utils.SharedPreferenceManager;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class GazetteBarCodeScanActivity extends GazetteBaseActivity {
    private CaptureManager capture;
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
        barcodeScannerView = (CompoundBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
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

}
