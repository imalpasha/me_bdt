package com.metech.tbd.ui.Model.Request;

/**
 * Created by Dell on 11/9/2015.
 */
public class InitialLoadRequest extends BaseObj {


    private String LanguageCode;

    /*Initiate Class*/
    public InitialLoadRequest() {
    }

    /*Initiate Class With Data*/
    public InitialLoadRequest(InitialLoadRequest info){
        LanguageCode = info.getLanguageCode();
    }

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        LanguageCode = languageCode;
    }


}
