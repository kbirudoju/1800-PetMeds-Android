package com.petmeds1800.model.entities;

/**
 * Created by Digvijay on 8/11/2016.
 */

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("_dynSessConf")
    private String sessionConfNumber;

    public LoginRequest(String login, String password, String sessionConfNumber) {
        this.login = login;
        this.password = password;
        this.sessionConfNumber = sessionConfNumber;
    }

    /**
     * @return The login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login The login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The sessionConfNumber
     */
    public String getSessionConfNumber() {
        return sessionConfNumber;
    }

    /**
     * @param sessionConfNumber The _dynSessConf
     */
    public void setSessionConfNumber(String sessionConfNumber) {
        this.sessionConfNumber = sessionConfNumber;
    }

}
