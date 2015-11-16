package com.gazette.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class GazetteSplashActivity extends GazetteBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_gazette);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GazetteSplashActivity.this,
                        GazetteMainActivity.class));
                finish();
            }
        }, 2300);
    }

}
