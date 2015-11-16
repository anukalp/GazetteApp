package com.gazette.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gazette.app.GazetteApplication;
import com.gazette.app.model.opt.OTPVerificationRequestModel;
import com.gazette.app.model.opt.OTPVerificationResponseModel;
import com.gazette.app.utils.GazetteConstants;
import com.gazette.app.utils.RetrofitManagerClass;
import com.gazette.app.utils.SharedPreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Anil Gudigar on 11/16/2015.
 */
public class OTPVerificationService extends IntentService {

    private static String TAG = OTPVerificationService.class.getSimpleName();
    private SharedPreferenceManager pref;
    private RetrofitManagerClass mRetrofitManagerClass;

    public OTPVerificationService() {
        super(OTPVerificationService.class.getSimpleName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            pref = new SharedPreferenceManager(getApplicationContext());
          //  if (pref.isLoggedIn())
            //    return;
            mRetrofitManagerClass = new RetrofitManagerClass(getApplicationContext());
            String mobile = pref.getMobileNumber();
            String otp = intent.getStringExtra(GazetteConstants.UserDetails.OTP);
            String name = pref.getName();
            String email = pref.getEmail();
            Log.i(TAG, "mobile " + mobile + " otp " + otp + " name " + name + " email " + email);
            OTPVerificationRequestModel verificationModel = new OTPVerificationRequestModel();
            verificationModel.setOtp(otp);
            verifyOtp(verificationModel);
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param verificationModel
     */
    private void verifyOtp(final OTPVerificationRequestModel verificationModel) {

        mRetrofitManagerClass.getmOtpRequestInterface().verifyOTP(verificationModel, new Callback<OTPVerificationResponseModel>() {

            @Override
            public void success(OTPVerificationResponseModel verificationResposeModel, retrofit.client.Response response) {
              //  Toast.makeText(getApplicationContext(), "Verified " + verificationResposeModel.getName(), Toast.LENGTH_SHORT).show();
                Log.i("Anil", " verifyOtp Verified :"+verificationResposeModel.getName());
                pref.createLogin(verificationResposeModel.getName(), verificationResposeModel.getEmail(), verificationResposeModel.getMobil(), verificationResposeModel.getApikey());
                GazetteApplication.getInstance().notifyAllonotpVerifySuccessListener(verificationResposeModel);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // Log error here since request failed
                Log.e("Anil", " verifyOtp Failed ", retrofitError);
                //Toast.makeText(getApplicationContext(), "Failed " + retrofitError, Toast.LENGTH_SHORT).show();
                //Dialog.dismiss();
            }
        });
    }
}
