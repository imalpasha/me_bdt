package com.metech.tbd.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.ui.Model.Receive.AboutUsReceive;
import com.metech.tbd.ui.Model.Receive.ChangePasswordReceive;
import com.metech.tbd.ui.Model.Receive.ChangeSearchFlightReceive;
import com.metech.tbd.ui.Model.Receive.CheckInListReceive;
import com.metech.tbd.ui.Model.Receive.ConfirmUpdateReceive;
import com.metech.tbd.ui.Model.Receive.ContactInfoReceive;
import com.metech.tbd.ui.Model.Receive.DeleteCCReceive;
import com.metech.tbd.ui.Model.Receive.DeleteFFReceive;
import com.metech.tbd.ui.Model.Receive.InitialLoadReceive;
import com.metech.tbd.ui.Model.Receive.FlightSummaryReceive;
import com.metech.tbd.ui.Model.Receive.ListBookingReceive;
import com.metech.tbd.ui.Model.Receive.ManageChangeContactReceive;
import com.metech.tbd.ui.Model.Receive.ManageRequestIntinenary;
import com.metech.tbd.ui.Model.Receive.MobileCheckInPassengerReceive;
import com.metech.tbd.ui.Model.Receive.MobileConfirmCheckInPassengerReceive;
import com.metech.tbd.ui.Model.Receive.PushNotificationReceive;
import com.metech.tbd.ui.Model.Receive.RetrieveBoardingPassReceive;
import com.metech.tbd.ui.Model.Receive.SSRReceive;
import com.metech.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.metech.tbd.ui.Model.Receive.LoginReceive;
import com.metech.tbd.ui.Model.Receive.MobileCheckinReceive;
import com.metech.tbd.ui.Model.Receive.PassengerInfoReveice;
import com.metech.tbd.ui.Model.Receive.PaymentInfoReceive;
import com.metech.tbd.ui.Model.Receive.PaymentReceive;
import com.metech.tbd.ui.Model.Receive.RegisterReceive;
import com.metech.tbd.ui.Model.Receive.SearchFlightReceive;
import com.metech.tbd.ui.Model.Receive.SeatSelectionReveice;
import com.metech.tbd.ui.Model.Receive.SelectFlightReceive;
import com.metech.tbd.ui.Model.Receive.ItineraryInfoReceive;
import com.metech.tbd.ui.Model.Receive.StateReceive;
import com.metech.tbd.ui.Model.Receive.TermsReceive;
import com.metech.tbd.ui.Model.Receive.UpdateProfileReceive;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Model.Request.AboutUs;
import com.metech.tbd.ui.Model.Request.ChangePasswordRequest;
import com.metech.tbd.ui.Model.Request.ChangeSSR;
import com.metech.tbd.ui.Model.Request.ConfirmUpdateRequest;
import com.metech.tbd.ui.Model.Request.ContactInfo;
import com.metech.tbd.ui.Model.Request.DeleteCCRequest;
import com.metech.tbd.ui.Model.Request.InitialLoadRequest;
import com.metech.tbd.ui.Model.Request.FlightSummary;
import com.metech.tbd.ui.Model.Request.FriendFamilyDelete;
import com.metech.tbd.ui.Model.Request.GetChangeFlight;
import com.metech.tbd.ui.Model.Request.GetFlightAvailability;
import com.metech.tbd.ui.Model.Request.GetSSR;
import com.metech.tbd.ui.Model.Request.ItineraryObj;
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
import com.metech.tbd.ui.Model.Request.PushNotificationObj;
import com.metech.tbd.ui.Model.Request.RegisterObj;
import com.metech.tbd.ui.Model.Request.RegisterRequest;
import com.metech.tbd.ui.Model.Request.RetrieveBoardingPassObj;
import com.metech.tbd.ui.Model.Request.SearchFlightObj;
import com.metech.tbd.ui.Model.Request.SeatAvailabilityRequest;
import com.metech.tbd.ui.Model.Request.SeatSelection;
import com.metech.tbd.ui.Model.Request.SelectChangeFlight;
import com.metech.tbd.ui.Model.Request.SelectFlight;
import com.metech.tbd.ui.Model.Request.SendItinenaryObj;
import com.metech.tbd.ui.Model.Request.Signature;
import com.metech.tbd.ui.Model.Request.StateRequest;
import com.metech.tbd.ui.Model.Request.TermsRequest;
import com.metech.tbd.ui.Model.Request.UpdateProfileRequest;
import com.metech.tbd.ui.Realm.RealmObjectController;
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

    @Subscribe
    public void onChangePasswordRequest(final ChangePasswordRequest event) {

        apiService.onRequestChangePassword(event, new Callback<ChangePasswordReceive>() {

            @Override
            public void success(ChangePasswordReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new ChangePasswordReceive(rhymesResponse));
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
    public void onUpdateProfileRequest(final UpdateProfileRequest data) {

        apiService.onUpdateProfileRequest(data, new Callback<UpdateProfileReceive>() {

            @Override
            public void success(UpdateProfileReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new UpdateProfileReceive(rhymesResponse));
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
    public void onSearchFlight(final SearchFlightObj event) {

        apiService.onSearchFlightRequest(event, new Callback<SearchFlightReceive>() {

            @Override
            public void success(SearchFlightReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new SearchFlightReceive(rhymesResponse));
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
    public void onMobileCheckin(final MobileCheckinObj event) {

        apiService.onMobileCheckinRequest(event, new Callback<MobileCheckinReceive>() {

            @Override
            public void success(MobileCheckinReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new MobileCheckinReceive(rhymesResponse));
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
    public void onPassengerCheckIn(final MobileCheckInPassenger event) {

        apiService.onPassengerCheckIn(event, new Callback<MobileCheckInPassengerReceive>() {

            @Override
            public void success(MobileCheckInPassengerReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new MobileCheckInPassengerReceive(rhymesResponse));
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
    public void onConfirmPassengerCheckIn(final MobileConfirmCheckInPassenger event) {

        apiService.onConfirmPassengerCheckIn(event, new Callback<MobileConfirmCheckInPassengerReceive>() {

            @Override
            public void success(MobileConfirmCheckInPassengerReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new MobileConfirmCheckInPassengerReceive(rhymesResponse));
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
    public void onSearchFlight(final SelectFlight event) {

        apiService.onSelectFlight(event, new Callback<SelectFlightReceive>() {

            @Override
            public void success(SelectFlightReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new SelectFlightReceive(retroResponse));
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
    public void onPassengerInfo(final Passenger event) {

        apiService.onPassengerInfo(event, new Callback<PassengerInfoReveice>() {

            @Override
            public void success(PassengerInfoReveice retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new PassengerInfoReveice(retroResponse));
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
    public void onContactInfo(final ContactInfo event) {

        apiService.onContactInfo(event, new Callback<ContactInfoReceive>() {

            @Override
            public void success(ContactInfoReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ContactInfoReceive(retroResponse));
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
    public void onTermsRequest(final TermsRequest data) {

        apiService.onTermsRequest(new Callback<TermsReceive>() {

            @Override
            public void success(TermsReceive rhymesResponse, Response response) {

                if (rhymesResponse != null) {
                    bus.post(new TermsReceive(rhymesResponse));
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
    public void onSeatSelection(final SeatSelection event) {

        apiService.onSeatSelection(event, new Callback<SeatSelectionReveice>() {

            @Override
            public void success(SeatSelectionReveice retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new SeatSelectionReveice(retroResponse));
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
    public void onPaymentInfo(final Signature event) {

        apiService.onPaymentInfo(event, new Callback<PaymentInfoReceive>() {

            @Override
            public void success(PaymentInfoReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new PaymentInfoReceive(retroResponse));
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
    public void onPaymentRequest(final Payment event) {

        apiService.onPaymentProcess(event, new Callback<PaymentReceive>() {

            @Override
            public void success(PaymentReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new PaymentReceive(retroResponse));
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
    public void onItineraryRequest(final ItineraryObj event) {

        apiService.onItineraryRequest(new Callback<ItineraryInfoReceive>() {

            @Override
            public void success(ItineraryInfoReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ItineraryInfoReceive(retroResponse));
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
    public void onFlightSummary(final FlightSummary event) {

        apiService.onFlightSummary(event, new Callback<FlightSummaryReceive>() {

            @Override
            public void success(FlightSummaryReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new FlightSummaryReceive(retroResponse));
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
    public void onManageFlight(final ManageFlightObj event) {

        apiService.onManageFlight(event, new Callback<FlightSummaryReceive>() {

            @Override
            public void success(FlightSummaryReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new FlightSummaryReceive(retroResponse));
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
    public void onManageFlight(final ManageFlightObjV2 event) {

        apiService.onManageFlightV2(event, new Callback<ListBookingReceive>() {

            @Override
            public void success(ListBookingReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    if (retroResponse.getStatus().equals("retry")) {
                        onManageFlight(event);
                    } else {

                        if (event.getModule().equals("manage_booking")) {
                            //save manage flight list to realm.
                            RealmObjectController.saveManageFlightList(MainFragmentActivity.getContext(), retroResponse);
                        } else if (event.getModule().equals("check_in")) {
                            //save mobile-checkin list to realm.
                            RealmObjectController.saveMobileCheckInList(MainFragmentActivity.getContext(), retroResponse);
                        } else if (event.getModule().equals("on_boarding_image")) {
                            //save mobile-checkin list to realm.
                            RealmObjectController.saveBoardingPassPNRList(MainFragmentActivity.getContext(), retroResponse);
                        }

                        bus.post(new ListBookingReceive(retroResponse));
                        RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                    }

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
    public void onManageFlight(final ManageFlightObjV3 event) {

        apiService.onManageFlightV3(event, new Callback<CheckInListReceive>() {

            @Override
            public void success(CheckInListReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new CheckInListReceive(retroResponse));
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
    public void onManageContactInfo(final ManageContactInfo event) {

        apiService.onChangeContactInfo(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
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
    public void onConfirmUpdate(final ConfirmUpdateRequest event) {

        apiService.onChangeRequestConfirm(event, new Callback<ConfirmUpdateReceive>() {

            @Override
            public void success(ConfirmUpdateReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ConfirmUpdateReceive(retroResponse));
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
    public void onChangePassenger(final ManagePassengerInfo event) {

        apiService.onChangePassenger(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
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
    public void onSeatRequest(final SeatAvailabilityRequest event) {

        apiService.onSeatRequest(event, new Callback<ContactInfoReceive>() {

            @Override
            public void success(ContactInfoReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ContactInfoReceive(retroResponse));
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
    public void onChangeSeat(final ManageSeatInfo event) {

        apiService.onChangeSeat(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
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
    public void onRequestItinenary(final SendItinenaryObj event) {

        apiService.onRequestItinenary(event, new Callback<ManageRequestIntinenary>() {

            @Override
            public void success(ManageRequestIntinenary retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ManageRequestIntinenary(retroResponse));
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
    public void onGetFlightRequest(final GetFlightAvailability event) {

        apiService.onGetFlightRequest(event, new Callback<ChangeSearchFlightReceive>() {

            @Override
            public void success(ChangeSearchFlightReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ChangeSearchFlightReceive(retroResponse));
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
    public void onGetChangeFlight(final GetChangeFlight event) {

        apiService.onGetChangeFlight(event, new Callback<SearchFlightReceive>() {

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
    public void onSelectChangeFlight(final SelectChangeFlight event) {

        apiService.onSelectChangeFlight(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
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
    public void onRetrieveBoardingPass(final RetrieveBoardingPassObj event) {

        apiService.onRetrieveBoardingPass(event, new Callback<RetrieveBoardingPassReceive>() {

            @Override
            public void success(RetrieveBoardingPassReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new RetrieveBoardingPassReceive(retroResponse));
                    if (!retroResponse.getStatus().equals("error")) {
                        RealmObjectController.cachedBoardingPass(MainFragmentActivity.getContext(), retroResponse);
                    }
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
    public void onRetrieveAboutUs(final AboutUs event) {

        apiService.onRetrieveAboutUs(event, new Callback<AboutUsReceive>() {

            @Override
            public void success(AboutUsReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new AboutUsReceive(retroResponse));
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
    public void onRetrieveSSR(final GetSSR event) {

        apiService.onRetrieveSSR(event, new Callback<SSRReceive>() {

            @Override
            public void success(SSRReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new SSRReceive(retroResponse));
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
    public void onChangeSSR(final ChangeSSR event) {

        apiService.onChangeSSR(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
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
    public void onEditFF(final PassengerInfo event) {

        apiService.onRequestEditFF(event, new Callback<SelectFlightReceive>() {

            @Override
            public void success(SelectFlightReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new SelectFlightReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                Log.e("error", error.getMessage());
            }

        });
    }

    @Subscribe
    public void onDeleteFF(final FriendFamilyDelete event) {

        apiService.onRequestDeleteFF(event, new Callback<DeleteFFReceive>() {

            @Override
            public void success(DeleteFFReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new DeleteFFReceive(retroResponse));
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
    public void onDeleteCC(final DeleteCCRequest event) {

        apiService.onRequestDeleteCC(event, new Callback<DeleteCCReceive>() {

            @Override
            public void success(DeleteCCReceive retroResponse, Response response) {

                if (retroResponse != null) {
                    bus.post(new DeleteCCReceive(retroResponse));
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
