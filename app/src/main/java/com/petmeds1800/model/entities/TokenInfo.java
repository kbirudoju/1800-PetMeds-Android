package com.petmeds1800.model.entities;

/**
 * Created by Siddharth on 4/4/2017.
 */

public class TokenInfo {

    private String token;
    private boolean validToken;
    private String matchedProfileId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValidToken() {
        return validToken;
    }

    public void setValidToken(boolean validToken) {
        this.validToken = validToken;
    }

    public String getMatchedProfileId() {
        return matchedProfileId;
    }

    public void setMatchedProfileId(String matchedProfileId) {
        this.matchedProfileId = matchedProfileId;
    }
}
