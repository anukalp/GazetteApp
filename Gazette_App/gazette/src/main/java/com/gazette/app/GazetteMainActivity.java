package com.gazette.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gazette.app.fragments.adapters.FragmentAdapter;
import com.gazette.app.utils.GazetteConstants;
import com.gazette.app.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GazetteMainActivity extends GazetteBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentAdapter adapter;
    private TabLayout mTabLayout;
    private android.support.v7.widget.Toolbar mToolbar;
    private ViewPager mPager;
    private ProgressDialog progressDialog;
    private TextView username;
    private TextView navigationusername;
    private List<String> mCollections = null;
    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private SharedPreferenceManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazette_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        pref = new SharedPreferenceManager(this);
        setSupportActionBar(mToolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchScanner();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.ifl_pager);
        mTabLayout = (TabLayout) findViewById(R.id.ifl_tab);

        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        mCollections.get(tab.getPosition());
                        break;
                    case 1:
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        _init();
    }

    private void _init() {
        navigationusername = (TextView) findViewById(R.id.headerusername);
        if (null != navigationusername && pref.isLoggedIn() && null != pref.getName()) {
            navigationusername.setText(pref.getName());
        }
        mCollections = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Tabs)));
        adapter = new FragmentAdapter(GazetteMainActivity.this, getSupportFragmentManager(), mCollections);
        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(adapter);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(mPager);
                LinearLayout tabOne = (LinearLayout) LayoutInflater.from(GazetteMainActivity.this).inflate(R.layout.custom_tab, null);
                username = (TextView) tabOne.findViewById(R.id.username);
                Log.i("Anil", "logged in :" + pref.isLoggedIn() + "Username :" + pref.getName());
                if (pref.isLoggedIn() && null != pref.getName()) {
                    Log.i("Anil", "Username :" + pref.getName());
                    username.setText(pref.getName());
                }
                mTabLayout.getTabAt(0).setCustomView(tabOne);
            }
        });
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                mTabLayout));
    GazetteApplication.getInstance().setupJabber();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gazette_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {

        } else if (id == R.id.nav_add_products) {

        } else if (id == R.id.nav_rate_us) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_settings) {
            launchSettings();
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchSettings() {
        Intent intent = new Intent(this, GazetteSettingsActivity.class);
        startActivity(intent);
    }

    public void launchScanner() {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, GazetteBarCodeScanActivity.class);
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Scan Result = " + data.getStringExtra(GazetteConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(GazetteConstants.ERROR_INFO);
                    if (!TextUtils.isEmpty(error)) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
