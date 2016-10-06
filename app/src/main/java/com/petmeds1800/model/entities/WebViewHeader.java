package com.petmeds1800.model.entities;

/**
 * Created by pooja on 8/22/2016.
 */
public class WebViewHeader {
    public WebViewHeader(String webviewHeader,int id,String itemID,String productId) {
        this.webviewHeader = webviewHeader;
        this.id=id;
        this.itemId=itemID;
        this.productId=productId;
    }

    private String webviewHeader;
    private int id;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    private String productId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    private String itemId;

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
