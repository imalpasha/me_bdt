package com.app.tbd.api;

import com.app.tbd.ui.Model.Receive.AboutUsReceive;
import com.app.tbd.ui.Model.Receive.ChangePasswordReceive;
import com.app.tbd.ui.Model.Receive.ChangeSearchFlightReceive;
import com.app.tbd.ui.Model.Receive.CheckInListReceive;
import com.app.tbd.ui.Model.Receive.ConfirmUpdateReceive;
import com.app.tbd.ui.Model.Receive.ContactInfoReceive;
import com.app.tbd.ui.Model.Receive.DeleteCCReceive;
import com.app.tbd.ui.Model.Receive.DeleteFFReceive;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.FlightSummaryReceive;
import com.app.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.app.tbd.ui.Model.Receive.ListBookingReceive;
import com.app.tbd.ui.Model.Receive.LoginReceive;
import com.app.tbd.ui.Model.Receive.ManageChangeContactReceive;
import com.app.tbd.ui.Model.Receive.ManageRequestIntinenary;
import com.app.tbd.ui.Model.Receive.MobileCheckInPassengerReceive;
import com.app.tbd.ui.Model.Receive.MobileCheckinReceive;
import com.app.tbd.ui.Model.Receive.MobileConfirmCheckInPassengerReceive;
import com.app.tbd.ui.Model.Receive.NewsletterLanguageReceive;
import com.app.tbd.ui.Model.Receive.PassengerInfoReveice;
import com.app.tbd.ui.Model.Receive.PaymentInfoReceive;
import com.app.tbd.ui.Model.Receive.PaymentReceive;
import com.app.tbd.ui.Model.Receive.PushNotificationReceive;
import com.app.tbd.ui.Model.Receive.RegisterReceive;
import com.app.tbd.ui.Model.Receive.RetrieveBoardingPassReceive;
import com.app.tbd.ui.Model.Receive.SSRReceive;
import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Receive.SeatSelectionReveice;
import com.app.tbd.ui.Model.Receive.SelectFlightReceive;
import com.app.tbd.ui.Model.Receive.ItineraryInfoReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.TermsReceive;
import com.app.tbd.ui.Model.Receive.UpdateProfileReceive;
import com.app.tbd.ui.Model.Receive.tryObj;
import com.app.tbd.ui.Model.Request.AboutUs;
import com.app.tbd.ui.Model.Request.ChangePasswordRequest;
import com.app.tbd.ui.Model.Request.ChangeSSR;
import com.app.tbd.ui.Model.Request.ConfirmUpdateRequest;
import com.app.tbd.ui.Model.Request.ContactInfo;
import com.app.tbd.ui.Model.Request.DeleteCCRequest;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.FlightSummary;
import com.app.tbd.ui.Model.Request.FriendFamilyDelete;
import com.app.tbd.ui.Model.Request.GetChangeFlight;
import com.app.tbd.ui.Model.Request.GetFlightAvailability;
import com.app.tbd.ui.Model.Request.GetSSR;
import com.app.tbd.ui.Model.Request.LoginRequest;
import com.app.tbd.ui.Model.Request.ManageContactInfo;
import com.app.tbd.ui.Model.Request.ManageFlightObj;
import com.app.tbd.ui.Model.Request.ManageFlightObjV2;
import com.app.tbd.ui.Model.Request.ManageFlightObjV3;
import com.app.tbd.ui.Model.Request.ManagePassengerInfo;
import com.app.tbd.ui.Model.Request.ManageSeatInfo;
import com.app.tbd.ui.Model.Request.MobileCheckInPassenger;
import com.app.tbd.ui.Model.Request.MobileCheckinObj;
import com.app.tbd.ui.Model.Request.MobileConfirmCheckInPassenger;
import com.app.tbd.ui.Model.Request.NewsletterLanguageRequest;
import com.app.tbd.ui.Model.Request.Passenger;
import com.app.tbd.ui.Model.Request.PassengerInfo;
import com.app.tbd.ui.Model.Request.PasswordRequest;
import com.app.tbd.ui.Model.Request.Payment;
import com.app.tbd.ui.Model.Request.RegisterRequest;
import com.app.tbd.ui.Model.Request.RetrieveBoardingPassObj;
import com.app.tbd.ui.Model.Request.SearchFlightObj;
import com.app.tbd.ui.Model.Request.SeatAvailabilityRequest;
import com.app.tbd.ui.Model.Request.SeatSelection;
import com.app.tbd.ui.Model.Request.SelectChangeFlight;
import com.app.tbd.ui.Model.Request.SelectFlight;
import com.app.tbd.ui.Model.Request.SendItinenaryObj;
import com.app.tbd.ui.Model.Request.Signature;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.UpdateProfileRequest;

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

    //GET ALL DATA
    @POST("/GetAllData")
    void onSendDeviceInfo(@Body InitialLoadRequest task, Callback<InitialLoadReceive> callback);

    //LOGIN
    @POST("/AuthenticateUser")
    void onRequestToLogin(@Body LoginRequest task, Callback<LoginReceive> callback);

    //GET STATE
    @POST("/GetProvinceState")
    void onStateRequest(@Body StateRequest task, Callback<StateReceive> callback);

    @POST("/RegisterUser")
    void onRegisterRequest(@Body RegisterRequest obj, Callback<RegisterReceive> callback);

    @POST("/ForgottenPassword")
    void onRequestPassword(@Body PasswordRequest task, Callback<ForgotPasswordReceive> callback);

    @POST("/GetPreferredLanguage")
    void onRequestNewsletterLanguage(@Body NewsletterLanguageRequest task, Callback<NewsletterLanguageReceive> callback);


























    @FormUrlEncoded
    @POST("/api.php")
    void onRegisterNotification(@Field("cmd")String cmd,@Field("user_id")String user_id,@Field("token")String token,@Field("name")String name,@Field("code")String code, Callback<PushNotificationReceive> callback);

    @GET("/users/{user}")
    void getFeed2(@Path("user") String user, Callback<LoginRequest> callback);

    @GET("/apis/8891c88e")
        //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    void getGoogleSpreedSheetData(Callback<tryObj> callback);

    //@FormUrlEncoded
    //@POST("/login")
    //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    //void onRequestToLogin(@Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    //void onRequestToLogin(@Field("number") int number, @Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    //void onRequestToLogin(@Body LoginRequest task, Callback<LoginReceive> callback);

    // @Body JSONObject searchstring

    @POST("/searchFlight")
    void onSearchFlightRequest(@Body SearchFlightObj obj, Callback<SearchFlightReceive> callback);


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


