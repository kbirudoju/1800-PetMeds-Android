package com.petmeds1800.model.entities;

import java.io.Serializable;

/**
 * Created by pooja on 9/13/2016.
 */
public class ImageData implements Serializable {
    private String url;
    private String mimeType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
