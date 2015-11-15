package com.gazette.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gazette.app.model.Product;
import com.gazette.app.utils.GazetteConstants;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Anil Gudigar on 11/10/2015.
 */
public class GazetteApplication extends Application {
    private static GazetteApplication _instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Anil", "GazetteApplication onCreate");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ProximaNovaregular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        _instance = this;
    }

    public static GazetteApplication getInstance() {
        return _instance;
    }

    public void launchAddProductDetailsActivity(Activity activity, Product product) {
        Intent intent =new Intent(this,GazetteAddProductActivity.class);
        Log.i("Anil", "getProductId :" + product.getProductId());
        intent.putExtra(GazetteConstants.PRODUCT_ID, product.getProductId());
        activity.startActivity(intent);
    }

    public void launchLoginActivity(Activity activity) {
        Intent intent =new Intent(this,GazetteLoginActivity.class);
        activity.startActivity(intent);
    }
    public void launchSpalshActivity(Activity activity) {
        Intent intent =new Intent(this,GazetteSplashActivity.class);
        activity.startActivity(intent);
    }
}
