package com.app.tbd.api;

import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.app.tbd.ui.Model.Receive.LanguageCountryReceive;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Receive.ResetPasswordReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.ui.Model.Receive.TBD.LogoutReceive;
import com.app.tbd.ui.Model.Receive.NewsletterLanguageReceive;
import com.app.tbd.ui.Model.Receive.PushNotificationReceive;
import com.app.tbd.ui.Model.Receive.RegisterReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.app.tbd.ui.Model.Request.ResetPasswordRequest;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Model.Request.TBD.LoginRequest;
import com.app.tbd.ui.Model.Request.TBD.LogoutRequest;
import com.app.tbd.ui.Model.Request.NewsletterLanguageRequest;
import com.app.tbd.ui.Model.Request.PasswordRequest;
import com.app.tbd.ui.Model.Request.RegisterRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.TransactionHistoryRequest;
import com.app.tbd.ui.Model.Request.ViewUserRequest;

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

    @POST("/Logout")
    void onRequestLogout(@Body LogoutRequest task, Callback<LogoutReceive> callback);

    @POST("/Language/GetLanguage")
    void onLanguageRequest(@Body LanguageRequest obj, Callback<LanguageReceive> callback);

    @GET("/Language/GetCountry")
    void onCountryRequest(Callback<LanguageCountryReceive> callback);

    @POST("/GetUser")
    void onViewUserRequest(@Body ViewUserRequest obj, Callback<ViewUserReceive> callback);

    @POST("/GetBalance")
    void onBigPointRequest(@Body BigPointRequest obj, Callback<BigPointReceive> callback);

    @POST("/UpdatePassword")
    void onResetPasswordRequest(@Body ResetPasswordRequest obj, Callback<ResetPasswordReceive> callback);

    //@POST("/GetPointsExpiry")
    //void onTransactionHistoryRequest(@Body TransactionHistoryRequest obj, Callback<TransactionHistoryReceive> callback);

    @POST("/GetTransaction")
    void onTransactionHistoryRequest(@Body TransactionHistoryRequest obj, Callback<TransactionHistoryReceive> callback);

    /*@POST("/Language/GetLanguage")
    void onLanguageRequest(@Body LanguageRequest obj, Callback<LanguageReceive> callback);

    @POST("/UpdateUser")
    void onUpdateUserRequest(@Body UpdateUserRequest obj, Callback<UpdateUserReceive> callback);*/

    @FormUrlEncoded
    @POST("/api.php")
    void onRegisterNotification(@Field("cmd") String cmd, @Field("user_id") String user_id, @Field("token") String token, @Field("name") String name, @Field("code") String code, Callback<PushNotificationReceive> callback);

    @GET("/users/{user}")
    void getFeed2(@Path("user") String user, Callback<LoginRequest> callback);

}


