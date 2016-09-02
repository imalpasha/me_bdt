package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 11/9/2015.
 */
public class BaseObj {

    String username;
    String email;
    String signature;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BaseObj() {

    }

    public BaseObj(BaseObj obj) {
        this.username = obj.getUsername();
        this.signature = obj.getSignature();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
