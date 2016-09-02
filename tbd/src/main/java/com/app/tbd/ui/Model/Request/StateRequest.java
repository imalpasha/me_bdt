package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 8/26/2016.
 */
public class StateRequest {

    private String LanguageCode;
    private String CountryCode;

    public StateRequest() {
    }

    public StateRequest(StateRequest data) {
        LanguageCode = data.getLanguageCode();
        CountryCode = data.getCountryCode();
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        LanguageCode = languageCode;
    }


}
