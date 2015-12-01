package com.gazette.app.model.opt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anil Gudigar on 11/15/2015.
 */
public class OTPRequestModel {
    private String name;
    private String email;
    private String mobile;


    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile The mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}
