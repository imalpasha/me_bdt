package com.app.tbd.ui.Realm;

import android.app.Activity;
import android.content.Context;

import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.FlightSummaryReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Model.Request.NotificationMessage;
import com.app.tbd.ui.Model.Request.RealmFlightObj;
import com.app.tbd.ui.Realm.Cached.CachedBigPointResult;
import com.app.tbd.ui.Realm.Cached.CachedLanguageCountry;
import com.app.tbd.ui.Realm.Cached.CachedResult;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Dell on 2/11/2016.
 */
public class RealmObjectController extends BaseFragment {


    public static Realm getRealmInstance(Activity act) {

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }
    }

    public static Realm getRealmInstanceContext(Context act) {

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }
    }





    public static RealmResults<NotificationMessage> getNotificationMessage(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<NotificationMessage> result = realm.where(NotificationMessage.class).findAll();

        return result;
    }



    public static void clearNotificationMessage(Context act) {

        Realm realm = getRealmInstanceContext(act);

        final RealmResults<NotificationMessage> result = realm.where(NotificationMessage.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }


    /*Save user info*/
    public static void saveUserInformation(Activity act, String stringfyObj) {

        Realm realm = getRealmInstance(act);

        //clear user info in realm first.
        final RealmResults<UserInfoJSON> result = realm.where(UserInfoJSON.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        //add new user info in realm
        realm.beginTransaction();
        UserInfoJSON realmObject = realm.createObject(UserInfoJSON.class);
        realmObject.setUserInfo(stringfyObj);
        realm.commitTransaction();
        realm.close();
    }


    public static void saveNotificationMessage(Context act, String message, String title) {
        Realm realm = getRealmInstanceContext(act);
        realm.beginTransaction();
        NotificationMessage realmObject = realm.createObject(NotificationMessage.class);
        realmObject.setMessage(message);
        realmObject.setTitle(title);
        realm.commitTransaction();

    }

    public static void deleteRealmFile(Activity act) {

        Realm realm = getRealmInstance(act);
        realm.beginTransaction();

        realm.commitTransaction();

        realm.close();

    }

    /* ----------------------- TBD - Retrieve cached result ----------------- */

    //get default result cached (remove later)
    public static RealmResults<CachedResult> getCachedResult(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();

        return result;
    }

    //get big point nc
    public static RealmResults<CachedBigPointResult> getCachedBigPointResult(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<CachedBigPointResult> result = realm.where(CachedBigPointResult.class).findAll();

        //final RealmResults<CachedBigPointResult> clearResult = realm.where(CachedBigPointResult.class).findAll();
        //realm.beginTransaction();
        //clearResult.clear();
        //realm.commitTransaction();

        return result;
    }

    //get choose country (language selection) nc
    public static RealmResults<CachedLanguageCountry> getCachedLanguageCountry(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<CachedLanguageCountry> result = realm.where(CachedLanguageCountry.class).findAll();

        //final RealmResults<CachedLanguageCountry> clearResult = realm.where(CachedLanguageCountry.class).findAll();
        //realm.beginTransaction();
        //clearResult.clear();
        //realm.commitTransaction();

        return result;
    }

    /* ------------------TBD - Cached Result -------------------------*/

    //cached default request
    public static void cachedResult(Activity act, String cachedResult) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        realm.beginTransaction();
        CachedResult realmObject = realm.createObject(CachedResult.class);
        realmObject.setCachedResult(cachedResult);
        realm.commitTransaction();

    }

    //cached big point request
    public static void cachedBigPointRequest(Activity act, String cachedBigPointResult) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedBigPointResult> result = realm.where(CachedBigPointResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        realm.beginTransaction();
        CachedBigPointResult realmObject = realm.createObject(CachedBigPointResult.class);
        realmObject.setCachedResult(cachedBigPointResult);
        realm.commitTransaction();

    }

    //cached big point request
    public static void cachedLanguageCountry(Activity act, String cachedLanguageCountry) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedLanguageCountry> result = realm.where(CachedLanguageCountry.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        realm.beginTransaction();
        CachedLanguageCountry realmObject = realm.createObject(CachedLanguageCountry.class);
        realmObject.setCachedResult(cachedLanguageCountry);
        realm.commitTransaction();

    }

    /* ------------------------tbd clear cached result -------------------------*/

    public static void clearCachedResult(Activity act) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }

    public static void clearBigPointCached(Activity act) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedBigPointResult> result = realm.where(CachedBigPointResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }


}
