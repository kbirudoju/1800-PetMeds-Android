package com.petmeds1800.model.entities;

/**
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpResponse {

    private Status status;

    private Profile profile;

    public class Profile{

        private String firstName;

        private String lastName;

        private boolean pushNotification;

        private String email;

        private String userId;
    }

    public SignUpResponse(Status status, Profile profile) {
        this.status = status;
        this.profile = profile;
    }
}
