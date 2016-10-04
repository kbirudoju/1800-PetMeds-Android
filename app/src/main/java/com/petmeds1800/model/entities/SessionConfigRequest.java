package com.petmeds1800.model.entities;

/**
 * Created by Sdixit on 12-09-2016.
 */

public class SessionConfigRequest {

    /* @SerializedName("_dynSessConf")*/
    private String _dynSessConf;

    public String get_dynSessConf() {
        return _dynSessConf;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this._dynSessConf = _dynSessConf;
    }

    public SessionConfigRequest(String _dynSessConf) {

        this._dynSessConf = _dynSessConf;
    }
}
