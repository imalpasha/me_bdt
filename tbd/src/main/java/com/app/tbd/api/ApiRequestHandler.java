package com.app.tbd.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.app.tbd.ui.Model.Receive.EditProfileReceive;
import com.app.tbd.ui.Model.Receive.LanguageCountryReceive;
import com.app.tbd.ui.Model.Receive.ResetPasswordReceive;
import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Receive.SignatureReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceiveFailed;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.ui.Model.Receive.TBD.LogoutReceive;
import com.app.tbd.ui.Model.Receive.NewsletterLanguageReceive;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;
import com.app.tbd.ui.Model.Receive.UploadPhotoReceive;
import com.app.tbd.ui.Model.Receive.UserPhotoReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.EditProfileRequest;
import com.app.tbd.ui.Model.Request.LanguageCountryRequest;
import com.app.tbd.ui.Model.Request.ResetPasswordRequest;
import com.app.tbd.ui.Model.Request.SearchFlightRequest;
import com.app.tbd.ui.Model.Request.SignatureRequest;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Model.Request.TBD.LogoutRequest;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.app.tbd.ui.Model.Request.NewsletterLanguageRequest;
import com.app.tbd.ui.Model.Request.TransactionHistoryRequest;
import com.app.tbd.ui.Model.Request.UploadPhotoRequest;
import com.app.tbd.ui.Model.Request.UserPhotoRequest;
import com.app.tbd.ui.Model.Request.ViewUserRequest;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.PushNotificationReceive;
import com.app.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.app.tbd.ui.Model.Receive.RegisterReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.TBD.LoginRequest;
import com.app.tbd.ui.Model.Request.PassengerInfo;
import com.app.tbd.ui.Model.Request.PasswordRequest;
import com.app.tbd.ui.Model.Request.Payment;
import com.app.tbd.ui.Model.Request.PushNotificationObj;
import com.app.tbd.ui.Model.Request.RegisterRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.TermsRequest;
import com.app.tbd.ui.Model.Request.UpdateProfileRequest;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApiRequestHandler {

    private final Bus bus;
    private final ApiService apiService;
    Context context;
    ProgressDialog mProgressDialog;
    private int inc;
    private boolean retry;

    public ApiRequestHandler(Bus bus, ApiService apiService) {
        this.bus = bus;
        this.apiService = apiService;
        retry = false;
    }

    // ------------------------------------------------------------------------------ //


    // Login API //

    @Subscribe
    public void onLoginRequest(final LoginRequest event) {

        apiService.onRequestToLogin(event, new Callback<LoginReceive>() {

            @Override
            public void success(LoginReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new LoginReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onStateRequest(final StateRequest event) {

        apiService.onStateRequest(event, new Callback<StateReceive>() {

            @Override
            public void success(StateReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new StateReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onRegisterRequest(final RegisterRequest event) {

        apiService.onRegisterRequest(event, new Callback<RegisterReceive>() {

            @Override
            public void success(RegisterReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new RegisterReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onRegisterRequest(final NewsletterLanguageRequest event) {

        apiService.onRequestNewsletterLanguage(event, new Callback<NewsletterLanguageReceive>() {

            @Override
            public void success(NewsletterLanguageReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new NewsletterLanguageReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onRequestLogout(final LogoutRequest event) {

        apiService.onRequestLogout(event, new Callback<LogoutReceive>() {

            @Override
            public void success(LogoutReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new LogoutReceive(rhymesResponse));
                    RealmObjectController.cachedSearchFlightResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse), Utils.SEARCH_FLIGHT);
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onLanguageRequest(final LanguageRequest event) {

        apiService.onLanguageRequest(event, new Callback<LanguageReceive>() {

            @Override
            public void success(LanguageReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new LanguageReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onCountryRequest(final LanguageCountryRequest event) {

        apiService.onCountryRequest(new Callback<LanguageCountryReceive>() {

            @Override
            public void success(LanguageCountryReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new LanguageCountryReceive(retroResponse));
                    RealmObjectController.cachedLanguageCountry(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onBigPointRequest(final BigPointRequest event) {

        apiService.onBigPointRequest(event, new Callback<BigPointReceive>() {

            @Override
            public void success(BigPointReceive retroResponse, Response response) {

                //if (retroResponse != null) {
                bus.post(new BigPointReceive(retroResponse));
                RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                RealmObjectController.cachedBigPointRequest(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                //} else {
                //    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                //}
            }

            @Override
            public void failure(RetrofitError error) {
                bus.post(new BigPointReceiveFailed());
            }
        });
    }

    @Subscribe
    public void onResetPasswordRequest(final ResetPasswordRequest event) {

        apiService.onResetPasswordRequest(event, new Callback<ResetPasswordReceive>() {

            @Override
            public void success(ResetPasswordReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ResetPasswordReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onTransactionHistoryRequest(final TransactionHistoryRequest event) {

        apiService.onTransactionHistoryRequest(event, new Callback<TransactionHistoryReceive>() {

            @Override
            public void success(TransactionHistoryReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new TransactionHistoryReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onUpdateProfile(final EditProfileRequest event) {

        apiService.onUpdateProfileRequest(event, new Callback<EditProfileReceive>() {

            @Override
            public void success(EditProfileReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new EditProfileReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onUpdateProfile(final UploadPhotoRequest event) {

        apiService.onUploadProfilePhoto(event, new Callback<UploadPhotoReceive>() {

            @Override
            public void success(UploadPhotoReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new UploadPhotoReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onRequestUserPhoto(final UserPhotoRequest event) {

        apiService.onRequestUserPhoto(event, new Callback<UserPhotoReceive>() {

            @Override
            public void success(UserPhotoReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new UserPhotoReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onRequestSearchFlight(final SearchFlightRequest event) {

        apiService.onRequestSearchFlight(event, new Callback<SearchFlightReceive>() {

            @Override
            public void success(SearchFlightReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new SearchFlightReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onSignatureRequest(final SignatureRequest event) {

        apiService.onSignatureRequest(new Callback<SignatureReceive>() {

            @Override
            public void success(SignatureReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new SignatureReceive(retroResponse));
                    RealmObjectController.cachedSearchFlightResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse),Utils.SEARCH_FLIGHT_SIGNATURE);
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }
    ////
    ///
    //
    ///
    ///
    ////


    @Subscribe
    public void onRegisterNotification(final PushNotificationObj event) {

        //
        // apiService.onRegisterNotification(event, new Callback<PushNotificationReceive>() {
        apiService.onRegisterNotification(event.getCmd(), event.getUser_id(), event.getToken(), event.getName(), event.getCode(), new Callback<PushNotificationReceive>() {

            @Override
            public void success(PushNotificationReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new PushNotificationReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }
    // ------------------------------------------------------------------------------ //

    /* Subscribe From HomePresenter - Send Device Information to server - ImalPasha */
    @Subscribe
    public void onDeviceInfo(final InitialLoadRequest event) {

        apiService.onSendDeviceInfo(event, new Callback<InitialLoadReceive>() {

            @Override
            public void success(InitialLoadReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new InitialLoadReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }


    @Subscribe
    public void onPasswordRequest(final PasswordRequest event) {

        apiService.onRequestPassword(event, new Callback<ForgotPasswordReceive>() {

            @Override
            public void success(ForgotPasswordReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new ForgotPasswordReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }




    /*@Subscribe
    public void onLanguageRequest(final LanguageRequest event) {

        apiService.onLanguageRequest(event, new Callback<LanguageReceive>() {

            @Override
            public void success(LanguageReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new LanguageReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }

    @Subscribe
    public void onCountryRequest(final LanguageCountryRequest event) {

        apiService.onCountryRequest(new Callback<LanguageCountryReceive>() {

            @Override
            public void success(LanguageCountryReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new LanguageCountryReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }*/

    @Subscribe
    public void onViewUserRequest(final ViewUserRequest event) {

        apiService.onViewUserRequest(event, new Callback<ViewUserReceive>() {

            @Override
            public void success(ViewUserReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ViewUserReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }
        });
    }
}
