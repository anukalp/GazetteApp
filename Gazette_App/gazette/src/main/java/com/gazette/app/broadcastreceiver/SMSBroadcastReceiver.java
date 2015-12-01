package com.gazette.app.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gazette.app.services.OTPVerificationService;
import com.gazette.app.utils.GazetteConstants;
import com.gazette.app.utils.SharedPreferenceManager;

import java.util.HashMap;

/**
 * Created by Anil Gudigar on 11/16/2015.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    private SharedPreferenceManager pref;

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            Log.d("Anil", "Got a new sms");
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e("Anil", "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(GazetteConstants.SMS_ORIGIN.toLowerCase())) {
                        Log.e("Anil", "SMS is not for our app!");
                        return;
                    }
                    pref = new SharedPreferenceManager(context);
                    HashMap<String, String> userDetail = pref.getUserDetails();
                    String name = userDetail.get(GazetteConstants.UserDetails.NAME);
                    String mobile = userDetail.get(GazetteConstants.UserDetails.MOBILE);
                    String email = userDetail.get(GazetteConstants.UserDetails.EMAIL);
                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e("Anil", "OTP received: " + verificationCode);

                    Intent httpIntent = new Intent(context, OTPVerificationService.class);
                    httpIntent.putExtra(GazetteConstants.UserDetails.OTP, verificationCode);
                    httpIntent.putExtra(GazetteConstants.UserDetails.MOBILE, mobile);
                    httpIntent.putExtra(GazetteConstants.UserDetails.NAME, name);
                    httpIntent.putExtra(GazetteConstants.UserDetails.EMAIL, email);
                    context.startService(httpIntent);
                }
            }
        } catch (Exception e) {
            Log.e("Anil", "Exception: ", e);
        }
    }

    /**
     * Getting the OTP from sms message body
     * ')' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        String opt = null;
        String[] codes = message.split(GazetteConstants.OTP_DELIMITER);
        if (codes.length > 1 && null != codes[1]) {
            code = codes[1];

            if (null != code) {
                code = code.trim();
                Log.e("Anil", "code : " + code);
                String[] smscode = code.split("\\s+");
                if (null != smscode[0]) {
                    opt = smscode[0].trim();
                    Log.e("Anil", "code : " + opt);
                }
            }
        } else {
            Log.e("Anil", "Empty Message: ");
        }
        return opt;
    }
}
