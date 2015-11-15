package com.gazette.app;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dev.sacot41.scviewpager.DotsView;
import com.dev.sacot41.scviewpager.SCPositionAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimationUtil;
import com.dev.sacot41.scviewpager.SCViewPager;
import com.dev.sacot41.scviewpager.SCViewPagerAdapter;

public class GazetteWizardMainActivity extends GazetteBaseActivity {

    private static final int NUM_PAGES = 5;

    private SCViewPager mViewPager;
    private SCViewPagerAdapter mPageAdapter;
    private DotsView mDotsView;
    private Button mGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazette_wizard_main);
        mGetStarted = (Button) findViewById(R.id.get_started);
        mViewPager = (SCViewPager) findViewById(R.id.viewpager_main_activity);
        mDotsView = (DotsView) findViewById(R.id.dotsview_main);
        mDotsView.setDotRessource(R.drawable.page_indiactor_selected, R.drawable.page_indiactor_normal);
        mDotsView.setNumberOfPage(NUM_PAGES);

        mPageAdapter = new SCViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.setNumberOfPage(NUM_PAGES);
        mPageAdapter.setFragmentBackgroundColor(R.color.colorPrimary);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mDotsView.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GazetteApplication.getInstance().launchSpalshActivity(GazetteWizardMainActivity.this);
            }
        });
        final Point size = SCViewAnimationUtil.getDisplaySize(this);

        View nameTag = findViewById(R.id.imageview_main_activity_name_tag);
        SCViewAnimation nameTagAnimation = new SCViewAnimation(nameTag);
        nameTagAnimation.addPageAnimation(new SCPositionAnimation(this, 0, 0, -size.y / 2));
        mViewPager.addAnimation(nameTagAnimation);

        View currentlyWork = findViewById(R.id.imageview_main_activity_currently_work);
        SCViewAnimation currentlyWorkAnimation = new SCViewAnimation(currentlyWork);
        currentlyWorkAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(currentlyWorkAnimation);

        View mobileView = findViewById(R.id.imageview_main_activity_mobile);
        SCViewAnimation mobileAnimation = new SCViewAnimation(mobileView);
        mobileAnimation.startToPosition((int) (size.x * 1.5), null);
        mobileAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -(int) (size.x * 1.5), 0));
        mobileAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -(int) (size.x * 1.5), 0));
        mViewPager.addAnimation(mobileAnimation);

        View djangoView = findViewById(R.id.imageview_main_activity_django_python);
        SCViewAnimation djangoAnimation = new SCViewAnimation(djangoView);
        djangoAnimation.startToPosition(null, -size.y);
        djangoAnimation.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        djangoAnimation.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(djangoAnimation);

        View commonlyView = findViewById(R.id.imageview_main_activity_commonly);
        SCViewAnimation commonlyAnimation = new SCViewAnimation(commonlyView);
        commonlyAnimation.startToPosition(size.x, null);
        commonlyAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        commonlyAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        mViewPager.addAnimation(commonlyAnimation);

        View butView = findViewById(R.id.imageview_main_activity_but);
        SCViewAnimation butAnimation = new SCViewAnimation(butView);
        butAnimation.startToPosition(size.x, null);
        butAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        butAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        mViewPager.addAnimation(butAnimation);

        View diplomeView = findViewById(R.id.imageview_main_activity_diploma);
        SCViewAnimation diplomeAnimation = new SCViewAnimation(diplomeView);
        diplomeAnimation.startToPosition((size.x * 2), null);
        diplomeAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x * 2, 0));
        diplomeAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x * 2, 0));
        mViewPager.addAnimation(diplomeAnimation);

        View whyView = findViewById(R.id.imageview_main_activity_why);
        SCViewAnimation whyAnimation = new SCViewAnimation(whyView);
        whyAnimation.startToPosition(size.x, null);
        whyAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        whyAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        mViewPager.addAnimation(whyAnimation);

        View futureView = findViewById(R.id.imageview_main_future);
        SCViewAnimation futureAnimation = new SCViewAnimation(futureView);
        futureAnimation.startToPosition(null, -size.y);
        futureAnimation.addPageAnimation(new SCPositionAnimation(this, 2, 0, size.y));
        futureAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(futureAnimation);

        View arduinoView = findViewById(R.id.imageview_main_arduino);
        SCViewAnimation arduinoAnimation = new SCViewAnimation(arduinoView);
        arduinoAnimation.startToPosition(size.x * 2, null);
        arduinoAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x * 2, 0));
        arduinoAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(arduinoAnimation);

        View raspberryView = findViewById(R.id.imageview_main_raspberry_pi);
        SCViewAnimation raspberryAnimation = new SCViewAnimation(raspberryView);
        raspberryAnimation.startToPosition(-size.x, null);
        raspberryAnimation.addPageAnimation(new SCPositionAnimation(this, 2, size.x, 0));
        raspberryAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(raspberryAnimation);

        View connectedDeviceView = findViewById(R.id.imageview_main_connected_device);
        SCViewAnimation connectedDeviceAnimation = new SCViewAnimation(connectedDeviceView);
        connectedDeviceAnimation.startToPosition((int) (size.x * 1.5), null);
        connectedDeviceAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -(int) (size.x * 1.5), 0));
        connectedDeviceAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(connectedDeviceAnimation);

        View checkOutView = findViewById(R.id.imageview_main_check_out);
        SCViewAnimation checkOutAnimation = new SCViewAnimation(checkOutView);
        checkOutAnimation.startToPosition(size.x, null);
        checkOutAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(checkOutAnimation);

        View linkedinView = findViewById(R.id.textview_main_linkedin_link);
        SCViewAnimation linkedinAnimation = new SCViewAnimation(linkedinView);
        linkedinAnimation.startToPosition(size.x, null);
        linkedinAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(linkedinAnimation);

        View githubView = findViewById(R.id.textview_main_github_link);
        SCViewAnimation githubAnimation = new SCViewAnimation(githubView);
        githubAnimation.startToPosition(size.x, null);
        githubAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(githubAnimation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gazette_wizard_main, menu);
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


}
