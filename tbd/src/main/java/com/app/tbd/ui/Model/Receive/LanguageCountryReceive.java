package com.app.tbd.ui.Model.Receive;

import java.util.List;

public class LanguageCountryReceive {

    private String Status;
    private String Message;
    private List<CountryList> CountryList;

    public LanguageCountryReceive(LanguageCountryReceive returnData) {
        Status = returnData.getStatus();
        CountryList = returnData.getCountryList();
        Message = returnData.getMessage();
    }

    public List<LanguageCountryReceive.CountryList> getCountryList() {
        return CountryList;
    }

    public void setCountryList(List<LanguageCountryReceive.CountryList> countryList) {
        CountryList = countryList;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


    public class CountryList {

        private String CountryName;
        private String CountryCode;

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String countryCode) {
            CountryCode = countryCode;
        }

        public String getCountryName() {
            return CountryName;
        }

        public void setCountryName(String countryName) {
            CountryName = countryName;
        }

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}