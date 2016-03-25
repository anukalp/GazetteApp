package com.gazette.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gazette.app.callbacks.OTPVerifySuccessListener;
import com.gazette.app.model.APIResponse;
import com.gazette.app.model.opt.OTPRequestModel;
import com.gazette.app.model.opt.OTPVerificationResponseModel;
import com.gazette.app.utils.RetrofitManagerClass;
import com.gazette.app.utils.SharedPreferenceManager;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import retrofit.Callback;
import retrofit.RetrofitError;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class GazetteLoginActivity extends GazetteBaseActivity implements LoaderCallbacks<Cursor>, OTPVerifySuccessListener {

    private RetrofitManagerClass mRetrofitManagerClass;
    private SharedPreferenceManager pref;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mMobileView;
    private EditText mNameView;
    private View mLoginFormView;
    private RelativeLayout loadingView;
    private TextView mVerifyMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRetrofitManagerClass = new RetrofitManagerClass(this);
        pref = new SharedPreferenceManager(this);
        GazetteApplication.getInstance().addotpVerifySuccessListener(this);
        setContentView(R.layout.activity_gazette_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        mNameView = (EditText) findViewById(R.id.name);

        mMobileView = (EditText) findViewById(R.id.mobilenumber);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        loadingView = (RelativeLayout) findViewById(R.id.loadingView);
        mVerifyMobile = (TextView) findViewById(R.id.verify_number);
    }

    private void populateAutoComplete() {
        Log.i("Anil", "mayRequestContacts() :" + mayRequestContacts());
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mMobileView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String mobile = mMobileView.getText().toString();
        pref.setUser(name, email, mobile);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mobile) && !isValidPhoneNumber(mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mVerifyMobile.setText("+91 " + mobile);
            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            showProgress(true);
            OTPRequestModel otpRequest = new OTPRequestModel();
            otpRequest.setName(name);
            otpRequest.setEmail(email);
            otpRequest.setMobile(mobile);
            pref.setUser(name, email, mobile);
            requestForSMS(otpRequest);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        if (show) {
            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                if (!emails.contains(possibleEmail))
                    emails.add(possibleEmail);
            }
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(GazetteLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private void requestForSMS(final OTPRequestModel request) {
        mRetrofitManagerClass.getmOtpRequestInterface().requestOTP(request, new Callback<APIResponse>() {
            @Override
            public void success(APIResponse data, retrofit.client.Response response) {
                if (data.isSuccess()) {
                    pref.setIsWaitingForSms(true);
                    Log.e("Anil", " new User ");
                } else if (data.getCode() == 2) {
                    Log.e("Anil", " user exists ");
                    getUserDetail(request);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Anil", "Failed ", retrofitError);
                showProgress(false);
                mMobileView.setError(getString(R.string.error_invalid_mobile));
            }
        });
    }

    private void getUserDetail(final OTPRequestModel request) {
        mRetrofitManagerClass.getmOtpRequestInterface().requestUserDetail(request.getMobile(), new Callback<OTPVerificationResponseModel>() {
            @Override
            public void success(OTPVerificationResponseModel verificationResposeModel, retrofit.client.Response response) {
                if (response.getStatus() == HttpsURLConnection.HTTP_OK) {
                    if (null != verificationResposeModel) {
                        Log.e("Anil", "success ");
                        Log.i("Anil", " verifyOtp Verified :" + verificationResposeModel.getName());
                        pref.createLogin(verificationResposeModel.getName(), verificationResposeModel.getEmail(), verificationResposeModel.getMobile(), verificationResposeModel.getApikey());
                        GazetteApplication.getInstance().notifyAllonotpVerifySuccessListener(verificationResposeModel);
                    }
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Anil", "Failed ", retrofitError);
                showProgress(false);
                mMobileView.setError(getString(R.string.error_invalid_mobile));
            }
        });
    }

    @Override
    public void OnOTPSuccess(OTPVerificationResponseModel verificationResponseModel) {
        // showProgress(false);
        GazetteApplication.getInstance().launchMainActivity(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GazetteApplication.getInstance().removeonotpVerifySuccessListener(this);
    }
}

