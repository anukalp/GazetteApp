package com.gazette.app.model.opt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anil Gudigar on 11/16/2015.
 */
public class OTPVerificationResponseModel {

    private String Name;
    private String Email;
    private String Mobil;
    private String Apikey;
    private Integer Status;
    private String CreatedAt;

    /**
     *
     * @return
     * The Name
     */
    public String getName() {
        return Name;
    }

    /**
     *
     * @param Name
     * The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     *
     * @return
     * The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *
     * @param Email
     * The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     *
     * @return
     * The Mobil
     */
    public String getMobil() {
        return Mobil;
    }

    /**
     *
     * @param Mobil
     * The Mobil
     */
    public void setMobil(String Mobil) {
        this.Mobil = Mobil;
    }

    /**
     *
     * @return
     * The Apikey
     */
    public String getApikey() {
        return Apikey;
    }

    /**
     *
     * @param Apikey
     * The Apikey
     */
    public void setApikey(String Apikey) {
        this.Apikey = Apikey;
    }

    /**
     *
     * @return
     * The Status
     */
    public Integer getStatus() {
        return Status;
    }

    /**
     *
     * @param Status
     * The Status
     */
    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    /**
     *
     * @return
     * The CreatedAt
     */
    public String getCreatedAt() {
        return CreatedAt;
    }

    /**
     *
     * @param CreatedAt
     * The CreatedAt
     */
    public void setCreatedAt(String CreatedAt) {
        this.CreatedAt = CreatedAt;
    }


}
