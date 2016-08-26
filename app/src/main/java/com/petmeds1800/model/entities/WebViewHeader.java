package com.petmeds1800.model.entities;

/**
 * Created by pooja on 8/22/2016.
 */
public class WebViewHeader {
    public WebViewHeader(String webviewHeader,int id) {
        this.webviewHeader = webviewHeader;
        this.id=id;
    }

    private String webviewHeader;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebviewHeader() {
        return webviewHeader;
    }

    public void setWebviewHeader(String webviewHeader) {

        this.webviewHeader = webviewHeader;
    }
}
