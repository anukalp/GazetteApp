package com.gazette.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.gazette.app.callbacks.OTPVerifySuccessListener;
import com.gazette.app.callbacks.OnProductAddedListener;
import com.gazette.app.callbacks.ProductScannerListener;
import com.gazette.app.model.Category;
import com.gazette.app.model.opt.OTPVerificationResponseModel;
import com.gazette.app.utils.GazetteConstants;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Anil Gudigar on 11/10/2015.
 */
public class GazetteApplication extends Application {
    private static GazetteApplication _instance;
    private ArrayList<OTPVerifySuccessListener> otpVerifySuccessListenerList = null;
    private ArrayList<ProductScannerListener> productScannerListenersList = null;
    private ArrayList<OnProductAddedListener> onProductAddedListenersList = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Anil", "GazetteApplication onCreate");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ProximaNovaregular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        otpVerifySuccessListenerList = new ArrayList<>();
        productScannerListenersList = new ArrayList<>();
        onProductAddedListenersList = new ArrayList<>();
        _instance = this;
    }

    public static GazetteApplication getInstance() {
        return _instance;
    }

    public void launchAddProductDetailsActivity(Activity activity, Category category) {
        Intent intent = new Intent(this, GazetteAddProductActivity.class);
        Log.i("Anil", "category id :" + category.getId());
        intent.putExtra(GazetteConstants.PRODUCT_ID, category.getId());
        activity.startActivity(intent);
    }

    public void launchLoginActivity(Activity activity) {
        Intent intent = new Intent(this, GazetteLoginActivity.class);
        activity.startActivity(intent);
    }

    public void launchSpalshActivity(Activity activity) {
        Intent intent = new Intent(this, GazetteSplashActivity.class);
        activity.startActivity(intent);
    }

    public void launchMainActivity(Activity activity) {
        Intent intent = new Intent(this, GazetteMainActivity.class);
        activity.startActivity(intent);
    }


    public void addotpVerifySuccessListener(OTPVerifySuccessListener onOtpVerifySuccessListener) {
        otpVerifySuccessListenerList.add(onOtpVerifySuccessListener);
    }

    public void removeonotpVerifySuccessListener(OTPVerifySuccessListener onOtpVerifySuccessListener) {
        otpVerifySuccessListenerList.remove(onOtpVerifySuccessListener);
    }

    public void notifyAllonotpVerifySuccessListener(OTPVerificationResponseModel otpVerificationResponseModel) {
        for (OTPVerifySuccessListener callback : otpVerifySuccessListenerList) {
            callback.OnOTPSuccess(otpVerificationResponseModel);
        }
    }

    public void addProductScannerListener(ProductScannerListener onProductScannerListener) {
        productScannerListenersList.add(onProductScannerListener);
    }

    public void removeProductScannerListener(ProductScannerListener onProductScannerListener) {
        productScannerListenersList.remove(onProductScannerListener);
    }

    public void notifyAllProductScannerListener() {
        for (ProductScannerListener callback : productScannerListenersList) {
            callback.OnProductInfoUpdate();
        }
    }


    public void addOnProductAddedListener(OnProductAddedListener onProductAddedListener) {
        onProductAddedListenersList.add(onProductAddedListener);
    }

    public void removeOnProductAddedListener(OnProductAddedListener onProductAddedListener) {
        onProductAddedListenersList.remove(onProductAddedListener);
    }

    public void notifyAllonProductAddedListenerr() {
        for (OnProductAddedListener callback : onProductAddedListenersList) {
            callback.OnProductAdded();
        }
    }
}
