package com.metech.tbd.ui.Model.Request;

/**
 * Created by Dell on 11/4/2015.
 */
public class PasswordRequest extends BaseObj {

    String status;

    /*Initiate Class*/
    public PasswordRequest(){
    }

    public PasswordRequest(PasswordRequest data){
        email = data.getEmail();
        signature = data.getSignature();
    }
    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    /*Response Data From Server*/

    public String getStatus() {
        return status;
    }


}
