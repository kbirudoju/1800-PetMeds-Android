package com.petmeds1800.model.entities;

/**
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpResponse {

    private Status status;

    private Profile profile;

    public Status getStatus() {
        return status;
    }

    public Profile getProfile() {
        return profile;
    }

    public class Profile{

        private String firstName;

        private String lastName;

        private boolean pushNotification;

        private String email;

        private String userId;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public boolean isPushNotification() {
            return pushNotification;
        }

        public String getEmail() {
            return email;
        }

        public String getUserId() {
            return userId;
        }
    }

}
