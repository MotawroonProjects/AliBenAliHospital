package com.alibenalihospital.models;

import java.io.Serializable;

public class UserModel {
    private int code;
    private User data;

    public User getUser() {
        return data;
    }

    public int getStatus() {
        return code;
    }

    public static class User implements Serializable {
        private int id;
        private String name;
        private String phone_code;
        private String phone;
        private String is_login;
        private String created_at;
        private String updated_at;
        private String firebaseToken;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getIs_login() {
            return is_login;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getFirebaseToken() {
            return firebaseToken;
        }

        public void setFirebaseToken(String firebaseToken) {
            this.firebaseToken = firebaseToken;
        }
    }


}
