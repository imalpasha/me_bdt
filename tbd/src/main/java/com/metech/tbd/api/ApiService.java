package com.metech.tbd.api;

import com.metech.tbd.ui.Model.Receive.AboutUsReceive;
import com.metech.tbd.ui.Model.Receive.ChangePasswordReceive;
import com.metech.tbd.ui.Model.Receive.ChangeSearchFlightReceive;
import com.metech.tbd.ui.Model.Receive.CheckInListReceive;
import com.metech.tbd.ui.Model.Receive.ConfirmUpdateReceive;
import com.metech.tbd.ui.Model.Receive.ContactInfoReceive;
import com.metech.tbd.ui.Model.Receive.DeleteCCReceive;
import com.metech.tbd.ui.Model.Receive.DeleteFFReceive;
import com.metech.tbd.ui.Model.Receive.DeviceInfoSuccess;
import com.metech.tbd.ui.Model.Receive.FlightSummaryReceive;
import com.metech.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.metech.tbd.ui.Model.Receive.ListBookingReceive;
import com.metech.tbd.ui.Model.Receive.LoginReceive;
import com.metech.tbd.ui.Model.Receive.ManageChangeContactReceive;
import com.metech.tbd.ui.Model.Receive.ManageRequestIntinenary;
import com.metech.tbd.ui.Model.Receive.MobileCheckInPassengerReceive;
import com.metech.tbd.ui.Model.Receive.MobileCheckinReceive;
import com.metech.tbd.ui.Model.Receive.MobileConfirmCheckInPassengerReceive;
import com.metech.tbd.ui.Model.Receive.PassengerInfoReveice;
import com.metech.tbd.ui.Model.Receive.PaymentInfoReceive;
import com.metech.tbd.ui.Model.Receive.PaymentReceive;
import com.metech.tbd.ui.Model.Receive.PushNotificationReceive;
import com.metech.tbd.ui.Model.Receive.RegisterReceive;
import com.metech.tbd.ui.Model.Receive.RetrieveBoardingPassReceive;
import com.metech.tbd.ui.Model.Receive.SSRReceive;
import com.metech.tbd.ui.Model.Receive.SearchFlightReceive;
import com.metech.tbd.ui.Model.Receive.SeatSelectionReveice;
import com.metech.tbd.ui.Model.Receive.SelectFlightReceive;
import com.metech.tbd.ui.Model.Receive.ItineraryInfoReceive;
import com.metech.tbd.ui.Model.Receive.TermsReceive;
import com.metech.tbd.ui.Model.Receive.UpdateProfileReceive;
import com.metech.tbd.ui.Model.Receive.tryObj;
import com.metech.tbd.ui.Model.Request.AboutUs;
import com.metech.tbd.ui.Model.Request.ChangePasswordRequest;
import com.metech.tbd.ui.Model.Request.ChangeSSR;
import com.metech.tbd.ui.Model.Request.ConfirmUpdateRequest;
import com.metech.tbd.ui.Model.Request.ContactInfo;
import com.metech.tbd.ui.Model.Request.DeleteCCRequest;
import com.metech.tbd.ui.Model.Request.DeviceInformation;
import com.metech.tbd.ui.Model.Request.FlightSummary;
import com.metech.tbd.ui.Model.Request.FriendFamilyDelete;
import com.metech.tbd.ui.Model.Request.GetChangeFlight;
import com.metech.tbd.ui.Model.Request.GetFlightAvailability;
import com.metech.tbd.ui.Model.Request.GetSSR;
import com.metech.tbd.ui.Model.Request.LoginRequest;
import com.metech.tbd.ui.Model.Request.ManageContactInfo;
import com.metech.tbd.ui.Model.Request.ManageFlightObj;
import com.metech.tbd.ui.Model.Request.ManageFlightObjV2;
import com.metech.tbd.ui.Model.Request.ManageFlightObjV3;
import com.metech.tbd.ui.Model.Request.ManagePassengerInfo;
import com.metech.tbd.ui.Model.Request.ManageSeatInfo;
import com.metech.tbd.ui.Model.Request.MobileCheckInPassenger;
import com.metech.tbd.ui.Model.Request.MobileCheckinObj;
import com.metech.tbd.ui.Model.Request.MobileConfirmCheckInPassenger;
import com.metech.tbd.ui.Model.Request.Passenger;
import com.metech.tbd.ui.Model.Request.PassengerInfo;
import com.metech.tbd.ui.Model.Request.PasswordRequest;
import com.metech.tbd.ui.Model.Request.Payment;
import com.metech.tbd.ui.Model.Request.RegisterObj;
import com.metech.tbd.ui.Model.Request.RetrieveBoardingPassObj;
import com.metech.tbd.ui.Model.Request.SearchFlightObj;
import com.metech.tbd.ui.Model.Request.SeatAvailabilityRequest;
import com.metech.tbd.ui.Model.Request.SeatSelection;
import com.metech.tbd.ui.Model.Request.SelectChangeFlight;
import com.metech.tbd.ui.Model.Request.SelectFlight;
import com.metech.tbd.ui.Model.Request.SendItinenaryObj;
import com.metech.tbd.ui.Model.Request.Signature;
import com.metech.tbd.ui.Model.Request.UpdateProfileRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {


    //@POST("/api.php")
    //void onRegisterNotification(@Body PushNotificationObj task, Callback<PushNotificationReceive> callback);

    @FormUrlEncoded
    @POST("/api.php")
    void onRegisterNotification(@Field("cmd")String cmd,@Field("user_id")String user_id,@Field("token")String token,@Field("name")String name,@Field("code")String code, Callback<PushNotificationReceive> callback);

    @GET("/users/{user}")
    void getFeed2(@Path("user") String user, Callback<LoginRequest> callback);

    @GET("/apis/8891c88e")
        //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    void getGoogleSpreedSheetData(Callback<tryObj> callback);

    //@FormUrlEncoded
    @POST("/login")
    //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    //void onRequestToLogin(@Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    //void onRequestToLogin(@Field("number") int number, @Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    void onRequestToLogin(@Body LoginRequest task, Callback<LoginReceive> callback);

    // @Body JSONObject searchstring

    @POST("/loading")
    void onSendDeviceInfo(@Body DeviceInformation task, Callback<DeviceInfoSuccess> callback);

    @POST("/register")
    void onRegisterRequest(@Body RegisterObj obj, Callback<RegisterReceive> callback);

    @POST("/searchFlight")
    void onSearchFlightRequest(@Body SearchFlightObj obj, Callback<SearchFlightReceive> callback);

    @POST("/forgotPassword")
    void onRequestPassword(@Body PasswordRequest task, Callback<ForgotPasswordReceive> callback);

    @POST("/changePassword")
    void onRequestChangePassword(@Body ChangePasswordRequest task, Callback<ChangePasswordReceive> callback);

    @POST("/updateProfile")
    void onUpdateProfileRequest(@Body UpdateProfileRequest task, Callback<UpdateProfileReceive> callback);

    @POST("/selectFlight")
    void onSelectFlight(@Body SelectFlight task, Callback<SelectFlightReceive> callback);

    @POST("/passengerDetails")
    void onPassengerInfo(@Body Passenger task, Callback<PassengerInfoReveice> callback);

    @POST("/contactDetails")
    void onContactInfo(@Body ContactInfo task, Callback<ContactInfoReceive> callback);

    @POST("/checkIn")
    void onMobileCheckinRequest(@Body MobileCheckinObj obj, Callback<MobileCheckinReceive> callback);

    @POST("/checkInPassengerList")
    void onPassengerCheckIn(@Body MobileCheckInPassenger obj, Callback<MobileCheckInPassengerReceive> callback);

    @POST("/checkInConfirmation")
    void onConfirmPassengerCheckIn(@Body MobileConfirmCheckInPassenger obj, Callback<MobileConfirmCheckInPassengerReceive> callback);

    @POST("/flightSummary")
    void onItineraryRequest( Callback<ItineraryInfoReceive> callback);

    @POST("/seatMap")
    void onSeatSelection(@Body SeatSelection obj, Callback<SeatSelectionReveice> callback);

    @POST("/terms")
    //void onTermsRequest(@Body TermsRequest obj, Callback<TermsReceive> callback);
    void onTermsRequest(Callback<TermsReceive> callback);

    @POST("/selectionPayment")
    void onPaymentInfo(@Body Signature obj, Callback<PaymentInfoReceive> callback);

    @POST("/paymentProcess")
    void onPaymentProcess(@Body Payment obj, Callback<PaymentReceive> callback);

    @POST("/flightSummary")
    void onFlightSummary(@Body FlightSummary obj, Callback<FlightSummaryReceive> callback);

    @POST("/retrieveBooking")
    void onManageFlight(@Body ManageFlightObj obj, Callback<FlightSummaryReceive> callback);

    @POST("/retrieveBookingList")
    void onManageFlightV2(@Body ManageFlightObjV2 obj, Callback<ListBookingReceive> callback);

    @POST("/retrieveBookingList")
    void onManageFlightV3(@Body ManageFlightObjV3 obj, Callback<CheckInListReceive> callback);

    @POST("/changeContact")
    void onChangeContactInfo(@Body ManageContactInfo obj, Callback<ManageChangeContactReceive> callback);

    @POST("/changeConfirmation")
    void onChangeRequestConfirm(@Body ConfirmUpdateRequest obj, Callback<ConfirmUpdateReceive> callback);

    @POST("/editPassengers")
    void onChangePassenger(@Body ManagePassengerInfo obj, Callback<ManageChangeContactReceive> callback);

    @POST("/getSeatAvailability")
    void onSeatRequest(@Body SeatAvailabilityRequest obj, Callback<ContactInfoReceive> callback);

    @POST("/changeSeat")
    void onChangeSeat(@Body ManageSeatInfo obj, Callback<ManageChangeContactReceive> callback);


    @POST("/sendItinerary")
    void onRequestItinenary(@Body SendItinenaryObj obj, Callback<ManageRequestIntinenary> callback);

    @POST("/getFlightAvailability")
    void onGetFlightRequest(@Body GetFlightAvailability obj, Callback<ChangeSearchFlightReceive> callback);

    @POST("/searchChangeFlight")
    void onGetChangeFlight(@Body GetChangeFlight obj, Callback<SearchFlightReceive> callback);

    @POST("/selectChangeFlight")
    void onSelectChangeFlight(@Body SelectChangeFlight obj, Callback<ManageChangeContactReceive> callback);

    @POST("/getBoardingPass")
    void onRetrieveBoardingPass(@Body RetrieveBoardingPassObj obj, Callback<RetrieveBoardingPassReceive> callback);

    @POST("/getAboutUS")
    void onRetrieveAboutUs(@Body AboutUs obj, Callback<AboutUsReceive> callback);

    @POST("/getMealSSR")
    void onRetrieveSSR(@Body GetSSR obj, Callback<SSRReceive> callback);

    @POST("/changeSSR")
    void onChangeSSR(@Body ChangeSSR obj, Callback<ManageChangeContactReceive> callback);

    @POST("/editFamilyFriends")
    void onRequestEditFF(@Body PassengerInfo obj, Callback<SelectFlightReceive> callback);

    @POST("/deleteFamilyFriends")
    void onRequestDeleteFF(@Body FriendFamilyDelete obj, Callback<DeleteFFReceive> callback);

    @POST("/removeCC")
    void onRequestDeleteCC(@Body DeleteCCRequest obj, Callback<DeleteCCReceive> callback);




    //@POST("/retrieveBooking")
    //void onChangeContactInfo(@Body ManageContactInfo obj, Callback<ManageChangeContactReceive> callback);

    //onRetrieveFlightSummary

}


