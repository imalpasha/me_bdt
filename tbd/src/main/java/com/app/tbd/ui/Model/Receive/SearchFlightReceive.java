package com.app.tbd.ui.Model.Receive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/23/2015.
 */
public class SearchFlightReceive {

    private String Status;
    private String Message;
    //private List<JourneyInfo> journeys = new ArrayList<JourneyInfo>();

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public class GoingFlight {

        private String arrival_station;
        private String departure_date;
        private String departure_station;
        private String status;

        public String getArrival_station() {
            return arrival_station;
        }

        public void setArrival_station(String arrival_station) {
            this.arrival_station = arrival_station;
        }

        public String getDeparture_date() {
            return departure_date;
        }

        public void setDeparture_date(String departure_date) {
            this.departure_date = departure_date;
        }

        public String getDeparture_station() {
            return departure_station;
        }

        public void setDeparture_station(String departure_station) {
            this.departure_station = departure_station;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }


    public SearchFlightReceive() {

    }

    public SearchFlightReceive(SearchFlightReceive obj) {

        Status = obj.getStatus();
        Message = obj.getMessage();

    }

}
