package com.petmeds1800.model.entities;

/**
 * Created by pooja on 8/22/2016.
 */
public class OrderDetailHeader {
    public OrderDetailHeader(String header){
        this.header=header;

    }
    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
