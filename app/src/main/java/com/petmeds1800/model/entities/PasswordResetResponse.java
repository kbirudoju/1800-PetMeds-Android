package com.petmeds1800.model.entities;

/**
 * Created by Siddharth on 4/4/2017.
 */

public class PasswordResetResponse {
    private Status status;
    private TokenInfo tokenInfo;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

}
