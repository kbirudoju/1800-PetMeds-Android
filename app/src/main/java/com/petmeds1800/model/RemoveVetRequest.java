package com.petmeds1800.model;

/**
 * Created by pooja on 10/19/2016.
 */
public class RemoveVetRequest {
    private String vetId;
    private String _dynSessConf;

    public RemoveVetRequest(String vetId, String _dynSessConf) {
        this.vetId = vetId;
        this._dynSessConf = _dynSessConf;
    }

    public String getVetId() {
        return vetId;
    }

    public void setVetId(String vetId) {
        this.vetId = vetId;
    }

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }
}
