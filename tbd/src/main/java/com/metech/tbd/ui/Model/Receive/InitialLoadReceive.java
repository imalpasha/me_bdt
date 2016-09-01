package com.metech.tbd.ui.Model.Receive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/9/2015.
 */
public class InitialLoadReceive {

    private final InitialLoadReceive userObj;
    private String Status;
    private String Message;
    private List<TitleList> TitleList = new ArrayList<TitleList>();
    private List<CountryList> CountryList = new ArrayList<CountryList>();

    public List<CountryList> getData_country() {
        return CountryList;
    }

    public void setData_country(List<CountryList> data_country) {
        this.CountryList = data_country;
    }

    public List<TitleList> getData_title() {
        return TitleList;
    }

    public void setData_title(List<TitleList> data_title) {
        this.TitleList = data_title;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public class TitleList {

        private String CultureCode;
        private String Description;
        private String GenderCode;
        private String Title;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getCultureCode() {
            return CultureCode;
        }

        public void setCultureCode(String cultureCode) {
            CultureCode = cultureCode;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getGenderCode() {
            return GenderCode;
        }

        public void setGenderCode(String genderCode) {
            GenderCode = genderCode;
        }

    }

    public class CountryList {

        private String CountryCode;
        private String CountryName;
        private String CultureCode;
        private String DialingCode;

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

        public String getCultureCode() {
            return CultureCode;
        }

        public void setCultureCode(String cultureCode) {
            CultureCode = cultureCode;
        }

        public String getDialingCode() {
            return DialingCode;
        }

        public void setDialingCode(String dialingCode) {
            DialingCode = dialingCode;
        }

    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public InitialLoadReceive(InitialLoadReceive param_obj) {
        this.userObj = param_obj;
    }

    public InitialLoadReceive getObj() {
        return userObj;
    }

}


