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

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.SSLSocketFactory;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Anil Gudigar on 11/10/2015.
 */
public class GazetteApplication extends Application {
    private static GazetteApplication _instance;
    private ArrayList<OTPVerifySuccessListener> otpVerifySuccessListenerList = null;
    private ArrayList<ProductScannerListener> productScannerListenersList = null;
    private ArrayList<OnProductAddedListener> onProductAddedListenersList = null;
    private XMPPTCPConnection mJabberconnection = null;

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
        init_Jabber("anil", "anil");
    }

    public static GazetteApplication getInstance() {
        return _instance;
    }

    private void init_Jabber(String USER_ID, String key) {
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setUsernameAndPassword(USER_ID + "@" + GazetteConstants.Jabber.DOMAIN, key);
        config.setServiceName(GazetteConstants.Jabber.DOMAIN);
        config.setHost(GazetteConstants.Jabber.DOMAIN);
        config.setPort(GazetteConstants.Jabber.PORT);
        config.setDebuggerEnabled(true);
        config.setSocketFactory(SSLSocketFactory.getDefault());

        mJabberconnection = new XMPPTCPConnection(config.build());
        try {
            mJabberconnection.connect();
            Log.i("XMPPClient", "[SettingsDialog] Connected to " + mJabberconnection.getHost());
        } catch (SmackException | IOException | XMPPException e) {
            Log.e("Anil", "[SettingsDialog] Failed to connect to " + mJabberconnection.getHost());
            Log.e("Anil", e.toString());
            e.printStackTrace();
        }
    }

    public void launchAddProductDetailsActivity(Activity activity, Category category) {
        Intent intent = new Intent(this, GazetteAddProductActivity.class);
        Log.i("Anil", "category id :" + category.getName());
        intent.putExtra(GazetteConstants.PRODUCT_ID, category.getName());
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
