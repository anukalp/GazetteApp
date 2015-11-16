package com.gazette.app.callbacks;

import com.gazette.app.model.opt.OTPVerificationResponseModel;

/**
 * Created by Anil Gudigar on 11/16/2015.
 */
public interface OTPVerifySuccessListener {

    public void OnOTPSuccess(OTPVerificationResponseModel verificationResposeModel);
}
