package com.gazette.app.model;

/**
 * Created by Anil on 3/25/2016.
 */
public class APIResponse {
    private boolean Success = false;
    private String mMesaage;
    private int code;

    public APIResponse(boolean sucess, String msg, int c) {
        Success = sucess;
        mMesaage = msg;
        code = c;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getmMesaage() {
        return mMesaage;
    }

    public void setmMesaage(String mMesaage) {
        this.mMesaage = mMesaage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
