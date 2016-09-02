package com.app.tbd.ui.Model.Receive;

import com.app.tbd.ui.Model.Request.SeatInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/16/2015.
 */
public class ContactInfoReceive {

    private String pnr;
    private String booking_status;
    private String status;
    private String booking_id;
    private ContactInfoReceive obj;
    private List<PasssengerInfo> passengers = new ArrayList<PasssengerInfo>();
    private List<Journeys> journeys = new ArrayList<Journeys>();
    private List<SeatFare> seat_fare = new ArrayList<SeatFare>();
    private String message;
    private String flight_type;


    //itinerary
    private List<FlightDetails> flight_details;
    private List<PriceDetails> price_details;
    private List<Services> services;
    private String total_price;
    private String total_due;


    public String getFlight_type() {
        return flight_type;
    }

    public void setFlight_type(String flight_type) {
        this.flight_type = flight_type;
    }

    public ContactInfoReceive(ContactInfoReceive param_obj){
        this.obj = param_obj;
        pnr = param_obj.getPnr();
        booking_status = param_obj.getBooking_status();
        status = param_obj.getStatus();
        booking_id = param_obj.getBooking_id();
        passengers = param_obj.getPassengers();
        journeys = param_obj.getJourneys();
        message = param_obj.getMessage();

        flight_details = param_obj.getFlight_details();
        price_details = param_obj.getPrice_details();
        services = param_obj.getServices();
        total_price = param_obj.getTotal_price();
        total_due =  param_obj.getTotal_due();
        flight_type = param_obj.getFlight_type();
        seat_fare = param_obj.getSeat_fare();

    }

    public class SeatFare{

        private String name;
        private String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }


    public List<SeatFare> getSeat_fare() {
        return seat_fare;
    }

    public void setSeat_fare(List<SeatFare> seat_fare) {
        this.seat_fare = seat_fare;
    }

    public String getTotal_due() {
        return total_due;
    }

    public void setTotal_due(String total_due) {
        this.total_due = total_due;
    }


    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getTotal_price() {return total_price;}

    public void setTotal_price(String total_price) {this.total_price = total_price;}


    public List<FlightDetails> getFlight_details() {
        return flight_details;
    }

    public void setFlight_details(List<FlightDetails> flight_details) {
        this.flight_details = flight_details;
    }

    public List<PriceDetails> getPrice_details() {
        return price_details;
    }

    public void setPrice_details(List<PriceDetails> price_details) {
        this.price_details = price_details;
    }

    public List<Journeys> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journeys> journeys) {
        this.journeys = journeys;
    }



    public class Journeys{

        private String departure_station;
        private String arrival_station;
        private String flight_status;
        private List<SeatInfo> seat_info = new ArrayList<SeatInfo>();
        private List<PasssengerInfo> passengers = new ArrayList<PasssengerInfo>();

        public String getFlight_status() {
            return flight_status;
        }

        public void setFlight_status(String flight_status) {
            this.flight_status = flight_status;
        }

        public List<PasssengerInfo> getPassengers() {
            return passengers;
        }

        public void setPassengers(List<PasssengerInfo> passengers) {
            this.passengers = passengers;
        }

        public String getDeparture_station() {
            return departure_station;
        }

        public void setDeparture_station(String departure_station) {
            this.departure_station = departure_station;
        }

        public String getArrival_station() {
            return arrival_station;
        }

        public void setArrival_station(String arrival_station) {
            this.arrival_station = arrival_station;
        }

        public List<SeatInfo> getSeat_info() {
            return seat_info;
        }

        public void setSeat_info(List<SeatInfo> seat_info) {
            this.seat_info = seat_info;
        }

    }






























    public class PasssengerInfo{

        private String title;
        private String first_name;
        private String last_name;
        private String dob;
        private String passenger_below_182;
        private String with_infant;
        private boolean selected;
        private boolean active;
        private String seat;
        private String compartment;
        private String compartmentReturn;
        private String seatReturn;
        private String checked_in;

        public String getChecked_in() {
            return checked_in;
        }

        public void setChecked_in(String checked_in) {
            this.checked_in = checked_in;
        }

        public String getCompartment_designator() {
            return compartment_designator;
        }

        public void setCompartment_designator(String compartment_designator) {
            this.compartment_designator = compartment_designator;
        }

        private String compartment_designator;

        public String getUnit_designator() {
            return unit_designator;
        }

        public void setUnit_designator(String unit_designator) {
            this.unit_designator = unit_designator;
        }

        private String unit_designator;
       /* private PassengerSeatStatus seatStatus;

        public PassengerSeatStatus getSeatStatus() {
            return seatStatus;
        }

        public void setSeatStatus(PassengerSeatStatus seatStatus) {
            this.seatStatus = seatStatus;
        }*/



        public String getCompartmentReturn() {
            return compartmentReturn;
        }

        public void setCompartmentReturn(String compartmentReturn) {
            this.compartmentReturn = compartmentReturn;
        }

        public String getSeatReturn() {
            return seatReturn;
        }

        public void setSeatReturn(String seatReturn) {
            this.seatReturn = seatReturn;
        }



        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCompartment() {
            return compartment;
        }

        public void setCompartment(String compartment) {
            this.compartment = compartment;
        }


        public String getSeat() {
            return seat;
        }

        public void setSeat(String seat) {
            this.seat = seat;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getPassenger_below_18() {
            return passenger_below_182;
        }

        public void setPassenger_below_18(String passenger_below_18) {
            this.passenger_below_182 = passenger_below_18;
        }

        public String getWith_infant() {
            return with_infant;
        }

        public void setWith_infant(String with_infant) {
            this.with_infant = with_infant;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public class FlightDetails {

        private String date;
        private String station;
        private String flight_number;
        private String time;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getFlight_number() {
            return flight_number;
        }

        public void setFlight_number(String flight_number) {
            this.flight_number = flight_number;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }



    }

    public class PriceDetails{


        private String title;
        private String guest;
        private String total_guest;
        private String infant;
        private String total_infant;
        private taxes_or_fees taxes_or_fees;


        public List<TaxOrFee> getTaxes_or_fees_array() {
            return taxes_or_fees_array;
        }

        public void setTaxes_or_fees_array(List<TaxOrFee> taxes_or_fees_array) {
            this.taxes_or_fees_array = taxes_or_fees_array;
        }

        private List<TaxOrFee> taxes_or_fees_array;


        private String total_taxes_or_fees;
        private List<Services> services;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;

        public List<Services> getServices() {
            return services;
        }

        public void setServices(List<Services> services) {
            this.services = services;
        }


        public class Services{

            private String service_name;
            private String service_price;

            public String getService_name() {
                return service_name;
            }

            public void setService_name(String service_name) {
                this.service_name = service_name;
            }

            public String getService_price() {
                return service_price;
            }

            public void setService_price(String service_price) {
                this.service_price = service_price;
            }
        }


        public PriceDetails.taxes_or_fees getTaxes_or_fees() {
            return taxes_or_fees;
        }

        public void setTaxes_or_fees(PriceDetails.taxes_or_fees taxes_or_fees) {
            this.taxes_or_fees = taxes_or_fees;
        }

        public String getTotal_taxes_or_fees() {
            return total_taxes_or_fees;
        }

        public void setTotal_taxes_or_fees(String total_taxes_or_fees) {
            this.total_taxes_or_fees = total_taxes_or_fees;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGuest() {
            return guest;
        }

        public void setGuest(String guest) {
            this.guest = guest;
        }

        public String getTotal_guest() {
            return total_guest;
        }

        public void setTotal_guest(String total_guest) {
            this.total_guest = total_guest;
        }

        public String getInfant() {
            return infant;
        }

        public void setInfant(String infant) {
            this.infant = infant;
        }

        public String getTotal_infant() {
            return total_infant;
        }

        public void setTotal_infant(String total_infant) {
            this.total_infant = total_infant;
        }

        public class TaxOrFee{

            private String tax_fee_name;
            private String tax_fee_price;

            public String getTax_fee_price() {
                return tax_fee_price;
            }

            public void setTax_fee_price(String tax_fee_price) {
                this.tax_fee_price = tax_fee_price;
            }

            public String getTax_fee_name() {
                return tax_fee_name;
            }

            public void setTax_fee_name(String tax_fee_name) {
                this.tax_fee_name = tax_fee_name;
            }

        }


        public class taxes_or_fees{

            private String admin_fee;
            private String airport_tax;
            private String fuel_surcharge;
            private String goods_and_services_tax;
            private String total;


            public String getAdmin_fee() {
                return admin_fee;
            }

            public void setAdmin_fee(String admin_fee) {
                this.admin_fee = admin_fee;
            }

            public String getAirport_tax() {
                return airport_tax;
            }

            public void setAirport_tax(String airport_tax) {
                this.airport_tax = airport_tax;
            }

            public String getFuel_surcharge() {
                return fuel_surcharge;
            }

            public void setFuel_surcharge(String fuel_surcharge) {
                this.fuel_surcharge = fuel_surcharge;
            }

            public String getGoods_and_services_tax() {
                return goods_and_services_tax;
            }

            public void setGoods_and_services_tax(String goods_and_services_tax) {
                this.goods_and_services_tax = goods_and_services_tax;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
        }


    }


    public class Services{

        private String service_name;
        private String service_price;

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getService_price() {
            return service_price;
        }

        public void setService_price(String service_price) {
            this.service_price = service_price;
        }
    }

    public List<PasssengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PasssengerInfo> passengers) {
        this.passengers = passengers;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ContactInfoReceive getObj() {
        return obj;
    }

    public void setObj(ContactInfoReceive obj) {
        this.obj = obj;
    }

}


